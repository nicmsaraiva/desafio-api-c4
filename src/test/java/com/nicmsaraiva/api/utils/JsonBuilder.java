package com.nicmsaraiva.api.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;

public class JsonBuilder {

    private static final ObjectMapper mapper = new ObjectMapper();
    private final ObjectNode node;

    private JsonBuilder(String path) throws Exception {
        File file = new File("src/test/resources/payloads" + path);
        this.node = (ObjectNode) mapper.readTree(file);
    }

    public static JsonBuilder from(String path) throws Exception {
        return new JsonBuilder(path);
    }

    public JsonBuilder with(String field, Object value) {
        this.node.set(field, mapper.valueToTree(value));
        return this;
    }

    public JsonBuilder without(String field) {
        this.node.remove(field);
        return this;
    }

    public String build() {
        return node.toString();
    }
}