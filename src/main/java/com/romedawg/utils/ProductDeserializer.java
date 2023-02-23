package com.romedawg.utils;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.json.async.NonBlockingJsonParserBase;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.romedawg.domain.Metra.Product;

import java.io.IOException;
import java.util.ArrayList;

public class ProductDeserializer extends StdDeserializer<Product> {

    public ProductDeserializer() {
        this(null);
    }

    public ProductDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Product deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JacksonException {
        JsonNode productNode = jp.getCodec().readTree(jp);
        Product product = new Product();
        product.setId(String.valueOf(productNode.get("id").textValue()));
        product.setName(productNode.get("name").textValue());
        product.setBrandName(productNode.get("brand")
                .get("name").textValue());
        product.setOwnerName(productNode.get("brand").get("owner")
                .get("name").textValue());
        product.setAlertUrl(productNode.get("alert").get("url")
                .get("translation").get(0).get("text").textValue());
//                .get("translation").get("text").textValue());
        return product;
    }
}
