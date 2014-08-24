package com.elevenware.ladybird.extensions.jaxb;

import com.elevenware.ladybird.entities.ContentHandler;

public class JaxbContentHandler implements ContentHandler {

    public static void register() {
        ContentHandlers.getInstance();
    }

}
