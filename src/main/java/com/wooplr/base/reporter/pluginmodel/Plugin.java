package com.wooplr.base.reporter.pluginmodel;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}test" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="purpose" use="required" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" />
 *       &lt;attribute name="class-name" use="required" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "test" })
@XmlRootElement(name = "plugin")
public class Plugin {

	@XmlElement(required = true)
	protected List<Test> test;
	@XmlAttribute(required = true)
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	@XmlSchemaType(name = "NMTOKEN")
	protected String purpose;
	@XmlAttribute(name = "class-name", required = true)
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	@XmlSchemaType(name = "NMTOKEN")
	protected String className;

	/**
	 * Gets the value of the className property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * Gets the value of the purpose property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPurpose() {
		return purpose;
	}

	/**
	 * Gets the value of the test property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the test property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getTest().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link Test }
	 * 
	 * 
	 */
	public List<Test> getTest() {
		if (test == null) {
			test = new ArrayList<Test>();
		}
		return this.test;
	}

	/**
	 * Sets the value of the className property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setClassName(String value) {
		this.className = value;
	}

	/**
	 * Sets the value of the purpose property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setPurpose(String value) {
		this.purpose = value;
	}

}
