package com.elevenware.ladybird.extensions.jaxb;

import com.elevenware.ladybird.entities.ContentHandler;
import com.elevenware.ladybird.entities.ContentHandlers;

import java.io.InputStream;

public class JaxbContentHandler implements ContentHandler {

    public static void register() {
        ContentHandlers.getInstance();
    }

    @Override
    public <T> T unmarshal(String payload, Class<T> clazz) {
        return null;
    }

    @Override
    public String marshal(Object payload) {
        return null;
    }

    @Override
    public <T> T unmarshal(InputStream inputStream, Class<T> clazz) {
        return null;
    }
}
