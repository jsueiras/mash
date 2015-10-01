//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.09.30 at 04:12:24 PM BST 
//


package com.mash.model.catalog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for Act complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Act">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.mash.data.com/mashModel}Root">
 *       &lt;sequence>
 *         &lt;element name="ocurrenceDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="participants" type="{http://www.mash.data.com/mashModel}Participations" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Act", propOrder = {
    "ocurrenceDate",
    "participants"
})
@XmlSeeAlso({
    ActSummary.class,
    Referral.class,
    Crime.class
})
public abstract class Act
    extends Root
{

    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar ocurrenceDate;
    protected Participations participants;

    /**
     * Gets the value of the ocurrenceDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getOcurrenceDate() {
        return ocurrenceDate;
    }

    /**
     * Sets the value of the ocurrenceDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setOcurrenceDate(XMLGregorianCalendar value) {
        this.ocurrenceDate = value;
    }

    /**
     * Gets the value of the participants property.
     * 
     * @return
     *     possible object is
     *     {@link Participations }
     *     
     */
    public Participations getParticipants() {
        return participants;
    }

    /**
     * Sets the value of the participants property.
     * 
     * @param value
     *     allowed object is
     *     {@link Participations }
     *     
     */
    public void setParticipants(Participations value) {
        this.participants = value;
    }

}
