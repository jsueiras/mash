//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.02 at 01:06:53 PM GMT 
//


package com.mash.model.catalog;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Entity complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Entity">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.mash.data.com/mashModel}Root">
 *       &lt;sequence>
 *         &lt;element name="warningMarkers" type="{http://www.mash.data.com/mashModel}Marker" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Entity", propOrder = {
    "warningMarkers"
})
@XmlSeeAlso({
    Person.class,
    EntitySummary.class,
    Location.class
})
public abstract class Entity
    extends Root
{

    protected List<Marker> warningMarkers;

    /**
     * Gets the value of the warningMarkers property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the warningMarkers property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getWarningMarkers().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Marker }
     * 
     * 
     */
    public List<Marker> getWarningMarkers() {
        if (warningMarkers == null) {
            warningMarkers = new ArrayList<Marker>();
        }
        return this.warningMarkers;
    }

}
