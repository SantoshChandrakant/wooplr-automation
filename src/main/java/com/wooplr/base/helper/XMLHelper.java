package com.wooplr.base.helper;

import java.io.File;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class XMLHelper {
	public static NodeList getXMLNodes(String xmlFileName, String tagName) {
		NodeList nList = null;
		try {
			File xmlFile = new File(xmlFileName);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);
			doc.getDocumentElement().normalize();
			nList = doc.getElementsByTagName(tagName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nList;
	}
	
	public static <T> String getXml(T response) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(response.getClass());
			Marshaller jaxbm = jaxbContext.createMarshaller();

			// output pretty printed
			jaxbm.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			StringWriter writer = new StringWriter();

			jaxbm.marshal(response, writer);
			return writer.toString();

		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return "";
	}

}
