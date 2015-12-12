package com.test.service;

import com.test.model.Book;
import com.test.model.Entity;
import com.test.model.User;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.base.Strings;
import org.elasticsearch.common.collect.Lists;
import org.elasticsearch.common.jackson.core.JsonEncoding;
import org.elasticsearch.common.jackson.core.JsonFactory;
import org.elasticsearch.common.jackson.core.JsonGenerator;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;
import org.reflections.Reflections;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.DefaultResultMapper;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.data.elasticsearch.core.FacetedPageImpl;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentEntity;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentProperty;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.context.MappingContext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HierarchyResultsMapper extends DefaultResultMapper {

    private MappingContext<? extends ElasticsearchPersistentEntity<?>, ElasticsearchPersistentProperty> mappingContext;

    public HierarchyResultsMapper(MappingContext<? extends ElasticsearchPersistentEntity<?>, ElasticsearchPersistentProperty> mappingContext) {
        super(mappingContext);
        this.mappingContext = mappingContext;
    }

    public <T> String[] getTypes(Class<T> clazz) {
        Map<String, Class<? extends T>> reverseMap = getReverseTypeMap(clazz);
        return reverseMap.keySet().toArray(new String[reverseMap.size()]);
    }

    private <T> Map<String, Class<? extends T>> getReverseTypeMap(Class<T> clazz) {
        Reflections reflections = new Reflections(clazz.getPackage().getName());
        Set<Class<? extends T>> subTypes = reflections.getSubTypesOf(clazz);
        subTypes.add(clazz);
        return subTypes
                .stream()
                .collect(Collectors.toMap(subClazz -> mappingContext.getPersistentEntity(subClazz).getIndexType(),
                        Function.identity()));
    }

    @Override
    public <T> FacetedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
        long totalHits = response.getHits().totalHits();
        List<T> results = new ArrayList<>();
        for (SearchHit hit : response.getHits()) {
            if (hit != null) {
                T result;
                Class<? extends T> actualClass = getReverseTypeMap(clazz).get(hit.getType());
                if (!Strings.isNullOrEmpty(hit.sourceAsString())) {
                    result = mapEntity(hit.sourceAsString(), actualClass);
                } else {
                    result = mapEntity(hit.getFields().values(), actualClass);
                }
                setPersistentEntityId(result, hit.getId(), clazz);
                results.add(result);
            }
        }
        return new FacetedPageImpl<>(results, pageable, totalHits, Lists.newArrayList());
    }

    private <T> void setPersistentEntityId(T result, String id, Class<T> clazz) {
        if (mappingContext != null && clazz.isAnnotationPresent(Document.class)) {
            PersistentProperty<ElasticsearchPersistentProperty> idProperty = mappingContext.getPersistentEntity(clazz).getIdProperty();
            // Only deal with String because ES generated Ids are strings !
            if (idProperty != null && idProperty.getType().isAssignableFrom(String.class)) {
                Method setter = idProperty.getSetter();
                if (setter != null) {
                    try {
                        setter.invoke(result, id);
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        }
    }

    private String buildJSONFromFields(Collection<SearchHitField> values) {
        JsonFactory nodeFactory = new JsonFactory();
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            JsonGenerator generator = nodeFactory.createGenerator(stream, JsonEncoding.UTF8);
            generator.writeStartObject();
            for (SearchHitField value : values) {
                if (value.getValues().size() > 1) {
                    generator.writeArrayFieldStart(value.getName());
                    for (Object val : value.getValues()) {
                        generator.writeObject(val);
                    }
                    generator.writeEndArray();
                } else {
                    generator.writeObjectField(value.getName(), value.getValue());
                }
            }
            generator.writeEndObject();
            generator.flush();
            return new String(stream.toByteArray(), Charset.forName("UTF-8"));
        } catch (IOException e) {
            return null;
        }
    }

    private <T> T mapEntity(Collection<SearchHitField> values, Class<T> clazz) {
        return mapEntity(buildJSONFromFields(values), clazz);
    }
}
