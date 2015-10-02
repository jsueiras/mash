//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.09.30 at 04:12:24 PM BST 
//


package com.mash.model.catalog;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GenericParticipant complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GenericParticipant">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.mash.data.com/mashModel}Participation">
 *       &lt;choice>
 *         &lt;element ref="{http://www.mash.data.com/mashModel}act"/>
 *         &lt;element ref="{http://www.mash.data.com/mashModel}entity"/>
 *       &lt;/choice>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GenericParticipant", propOrder = {
    "entity",
    "act"
})
public class GenericParticipant
    extends Participation
{

    @XmlElementRef(name = "entity", namespace = "http://www.mash.data.com/mashModel", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends Entity> entity;
    @XmlElementRef(name = "act", namespace = "http://www.mash.data.com/mashModel", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends Act> act;

    /**
     * Gets the value of the entity property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Person }{@code >}
     *     {@link JAXBElement }{@code <}{@link Entity }{@code >}
     *     {@link JAXBElement }{@code <}{@link EntitySummary }{@code >}
     *     {@link JAXBElement }{@code <}{@link Location }{@code >}
     *     
     */
    public JAXBElement<? extends Entity> getEntity() {
        return entity;
    }

    /**
     * Sets the value of the entity property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Person }{@code >}
     *     {@link JAXBElement }{@code <}{@link Entity }{@code >}
     *     {@link JAXBElement }{@code <}{@link EntitySummary }{@code >}
     *     {@link JAXBElement }{@code <}{@link Location }{@code >}
     *     
     */
    public void setEntity(JAXBElement<? extends Entity> value) {
        this.entity = value;
    }

    /**
     * Gets the value of the act property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Act }{@code >}
     *     {@link JAXBElement }{@code <}{@link Referral }{@code >}
     *     {@link JAXBElement }{@code <}{@link ActSummary }{@code >}
     *     {@link JAXBElement }{@code <}{@link Crime }{@code >}
     *     
     */
    public JAXBElement<? extends Act> getAct() {
        return act;
    }

    /**
     * Sets the value of the act property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Act }{@code >}
     *     {@link JAXBElement }{@code <}{@link Referral }{@code >}
     *     {@link JAXBElement }{@code <}{@link ActSummary }{@code >}
     *     {@link JAXBElement }{@code <}{@link Crime }{@code >}
     *     
     */
    public void setAct(JAXBElement<? extends Act> value) {
        this.act = value;
    }

}