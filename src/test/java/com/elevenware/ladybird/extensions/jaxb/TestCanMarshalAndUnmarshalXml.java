package com.elevenware.ladybird.extensions.jaxb;

import com.elevenware.ladybird.client.LadybirdClient;
import com.elevenware.ladybird.kit.AbstractHttpRecordingTestCase;
import com.elevenware.ladybird.kit.AbstractHttpServingTestCase;
import com.elevenware.ladybird.kit.RecordableHttpRequest;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;

import static org.junit.Assert.*;

public class TestCanMarshalAndUnmarshalXml extends AbstractHttpRecordingTestCase {

    @Test
    public void xmlMarshalling() throws JAXBException, IOException {

        Address address = new Address();
        LadybirdClient client = LadybirdClient.forLocalhost();
        client.sendXml().post("/xml", address);

        RecordableHttpRequest request = getLast();

        JAXBContext jaxb = JAXBContext.newInstance(Address.class);
        StreamSource xml = new StreamSource(request.getInputStream());
        JAXBElement<Address> jxe = jaxb.createUnmarshaller().unmarshal(xml, Address.class);
        Address other = jxe.getValue();

        assertEquals(address.getLineOne(), other.getLineOne());


    }

}
