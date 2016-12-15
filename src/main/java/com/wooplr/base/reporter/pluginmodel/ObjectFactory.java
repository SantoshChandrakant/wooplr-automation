package com.wooplr.base.reporter.pluginmodel;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the generated package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

	/**
	 * Create a new ObjectFactory that can be used to create new instances of
	 * schema derived classes for package: generated
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link Reportplugins }
	 * 
	 */
	public Reportplugins createMauiplugins() {
		return new Reportplugins();
	}

	/**
	 * Create an instance of {@link Method }
	 * 
	 */
	public Method createMethod() {
		return new Method();
	}

	/**
	 * Create an instance of {@link Page }
	 * 
	 */
	public Page createPage() {
		return new Page();
	}

	/**
	 * Create an instance of {@link Plugin }
	 * 
	 */
	public Plugin createPlugin() {
		return new Plugin();
	}

	/**
	 * Create an instance of {@link Test }
	 * 
	 */
	public Test createTest() {
		return new Test();
	}

}
