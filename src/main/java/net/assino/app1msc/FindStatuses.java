
package net.assino.app1msc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="NameSubstring" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "nameSubstring"
})
@XmlRootElement(name = "FindStatuses")
public class FindStatuses {

    @XmlElement(name = "NameSubstring", required = true)
    protected String nameSubstring;

    /**
     * Gets the value of the nameSubstring property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNameSubstring() {
        return nameSubstring;
    }

    /**
     * Sets the value of the nameSubstring property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNameSubstring(String value) {
        this.nameSubstring = value;
    }

}