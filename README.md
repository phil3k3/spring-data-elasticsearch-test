# spring-data-elasticsearch-test

It is often the case that you have a hierarchy of classes you want to index. Hence, upon providing the base class, it is sometimes required to search across all its sub classes.
Unfortunately, at the time of this example is built, this was not possible out of the box.

The example assumes having a simple class hierarchy of one base class and two sub classes:

* Entity
    * Book
    * User

Basically two steps were required to make this work:

* Find the sub classes of the base class, convert them to the corresponding document's \_type and add them to the SearchQuery
* Provide a custom ResultsMapper which looks up the actual class from the result document's \_type and use this class for mapping the JSON result back to Java