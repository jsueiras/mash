//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.09.30 at 04:12:24 PM BST 
//


package com.mash.model.catalog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Victim complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Victim">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.mash.data.com/mashModel}Participation">
 *       &lt;choice>
 *         &lt;element ref="{http://www.mash.data.com/mashModel}crime"/>
 *         &lt;element ref="{http://www.mash.data.com/mashModel}person"/>
 *       &lt;/choice>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Victim", propOrder = {
    "person",
    "crime"
})
public class Victim
    extends Participation
{

    protected Person person;
    protected Crime crime;

    /**
     * Gets the value of the person property.
     * 
     * @return
     *     possible object is
     *     {@link Person }
     *     
     */
    public Person getPerson() {
        return person;
    }

    /**
     * Sets the value of the person property.
     * 
     * @param value
     *     allowed object is
     *     {@link Person }
     *     
     */
    public void setPerson(Person value) {
        this.person = value;
    }

    /**
     * Gets the value of the crime property.
     * 
     * @return
     *     possible object is
     *     {@link Crime }
     *     
     */
    public Crime getCrime() {
        return crime;
    }

    /**
     * Sets the value of the crime property.
     * 
     * @param value
     *     allowed object is
     *     {@link Crime }
     *     
     */
    public void setCrime(Crime value) {
        this.crime = value;
    }

}
