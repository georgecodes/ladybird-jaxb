package com.elevenware.ladybird.extensions.jaxb;

import com.elevenware.ladybird.entities.ContentHandler;
import com.elevenware.ladybird.entities.ContentHandlers;
import java.io.InputStream;
import org.apache.http.entity.ContentType;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

public class JaxbContentHandler implements ContentHandler {

    static {
        ContentHandlers.getInstance().registerHandler(ContentType.APPLICATION_XML, new JaxbContentHandler());
    }

    @Override
    public <T> T unmarshal(String payload, Class<T> clazz) {
        JAXBContext jaxb = null;
        try {
            jaxb = JAXBContext.newInstance(clazz);
            StreamSource xml = new StreamSource(new StringReader(payload));
            JAXBElement<T> jxe = jaxb.createUnmarshaller().unmarshal(xml, clazz);
            return jxe.getValue();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        throw new RuntimeException();
    }

    @Override
    public String marshal(Object payload) {
        JAXBContext jaxb = null;
        try {
            jaxb = JAXBContext.newInstance(payload.getClass());
            StringWriter writer = new StringWriter();
            JAXBElement je2 = new JAXBElement(new QName(payload.getClass().getSimpleName().toLowerCase()),  payload.getClass(), payload);
            jaxb.createMarshaller().marshal(je2, writer);
            String ret =  writer.toString();
            return ret;
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        throw new RuntimeException();
    }

    @Override
    public <T> T unmarshal(InputStream inputStream, Class<T> clazz) {
        JAXBContext jaxb = null;
        try {
            jaxb = JAXBContext.newInstance(clazz);
            StreamSource xml = new StreamSource(inputStream);
            JAXBElement<T> jxe = jaxb.createUnmarshaller().unmarshal(xml, clazz);
            return jxe.getValue();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        throw new RuntimeException();
    }
}
