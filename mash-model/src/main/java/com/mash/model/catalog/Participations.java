//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.09.30 at 04:12:24 PM BST 
//


package com.mash.model.catalog;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Participations complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Participations">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.mash.data.com/mashModel}participation" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Participations", propOrder = {
    "participations"
})
public class Participations {

    @XmlElementRef(name = "participation", namespace = "http://www.mash.data.com/mashModel", type = JAXBElement.class, required = false)
    protected List<JAXBElement<? extends Participation>> participations;

    /**
     * Gets the value of the participations property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the participations property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getParticipations().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link GenericParticipant }{@code >}
     * {@link JAXBElement }{@code <}{@link SubjectOf }{@code >}
     * {@link JAXBElement }{@code <}{@link Victim }{@code >}
     * {@link JAXBElement }{@code <}{@link Participation }{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends Participation>> getParticipations() {
        if (participations == null) {
            participations = new ArrayList<JAXBElement<? extends Participation>>();
        }
        return this.participations;
    }

}
