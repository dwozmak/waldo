//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.11.12 at 03:01:40 AM EST 
//


package com.amazonaws.s3.doc._2006_03_01;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.amazonaws.s3.doc._2006_03_01 package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.amazonaws.s3.doc._2006_03_01
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ListBucketResult }
     * 
     */
    public ListBucketResult createListBucketResult() {
        return new ListBucketResult();
    }

    /**
     * Create an instance of {@link ListBucketResult.Contents }
     * 
     */
    public ListBucketResult.Contents createListBucketResultContents() {
        return new ListBucketResult.Contents();
    }

}
