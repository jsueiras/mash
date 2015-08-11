//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.08.11 at 11:15:24 AM BST 
//


package com.mash.model.catalog;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Acts complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Acts">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.mash.data.com/mashModel}act" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Acts", propOrder = {
    "acts"
})
@XmlRootElement(name = "acts")
public class Acts {

    @XmlElementRef(name = "act", namespace = "http://www.mash.data.com/mashModel", type = JAXBElement.class, required = false)
    protected List<JAXBElement<? extends Act>> acts;

    /**
     * Gets the value of the acts property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the acts property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getActs().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link Crime }{@code >}
     * {@link JAXBElement }{@code <}{@link Act }{@code >}
     * {@link JAXBElement }{@code <}{@link Referral }{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends Act>> getActs() {
        if (acts == null) {
            acts = new ArrayList<JAXBElement<? extends Act>>();
        }
        return this.acts;
    }

}
