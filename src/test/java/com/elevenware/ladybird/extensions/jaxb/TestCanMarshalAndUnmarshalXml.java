package com.elevenware.ladybird.extensions.jaxb;

import com.elevenware.ladybird.HttpResponse;
import com.elevenware.ladybird.client.LadybirdClient;
import com.elevenware.ladybird.kit.AbstractHttpRecordingTestCase;
import com.elevenware.ladybird.kit.AbstractHttpServingTestCase;
import com.elevenware.ladybird.kit.RecordableHttpRequest;
import org.apache.http.entity.ContentType;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.*;

public class TestCanMarshalAndUnmarshalXml extends AbstractHttpRecordingTestCase {

    @BeforeClass
    public static void start() throws ClassNotFoundException {
        Class.forName(JaxbContentHandler.class.getCanonicalName());
    }

    @Test
    public void xmlMarshallingWithNoSchemaOrAnnotation() throws JAXBException, IOException, ClassNotFoundException {

        Address address = new Address();
        address.setLineOne("123 Fake Street");

        LadybirdClient client = LadybirdClient.forLocalhost();
        client.sendXml().post("/xml", address);

        RecordableHttpRequest request = getLast();

        JAXBContext jaxb = JAXBContext.newInstance(Address.class);
        StreamSource xml = new StreamSource(request.getInputStream());

        JAXBElement<Address> jxe = jaxb.createUnmarshaller().unmarshal(xml, Address.class);
        Address other = jxe.getValue();

        assertEquals(address.getLineOne(), other.getLineOne());

    }

    @Test
    public void xmlUnmarshallingWithNoSchemaOrAnnotation() throws JAXBException, IOException, ClassNotFoundException {


        setRealHandler(new AbstractHandler() {
            @Override
            public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

                Address address = new Address();
                address.setLineOne("123 Fake Street");

                JAXBContext jaxb = null;
                try {
                    jaxb = JAXBContext.newInstance(Address.class);
                    StringWriter writer = new StringWriter();
                    JAXBElement je2 = new JAXBElement(new QName("address"),  Address.class, address);
                    jaxb.createMarshaller().marshal(je2, writer);
                    String ret =  writer.toString();
                    response.setContentType(ContentType.APPLICATION_XML.getMimeType());
                    response.getOutputStream().println(ret);
                    response.flushBuffer();
                    baseRequest.setHandled(true);
            } catch (JAXBException e) {
                    e.printStackTrace();
                }
            }});

        LadybirdClient client = LadybirdClient.forLocalhost();
        HttpResponse response = client.acceptXml().get("/xml");
        getLast();

        Address address = response.getEntity(Address.class);

    }

}
