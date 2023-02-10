//package com.romedawg;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.romedawg.domain.Metra.Product;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class AlertTests {
//
//    private static String data = " {\n" +
//            " \"id\": \"1\",\n" +
//            " \"myid\": \"957c43f2-fa2e-42f9-bf75-6e3d5bb6960a\",\n" +
//            " \"name\": \"The Best Product\",\n" +
//            " \"brand\": {\n" +
//            "     \"id\": \"9bcd817d-0141-42e6-8f04-e5aaab0980b6\",\n" +
//            "     \"name\": \"ACME Products\",\n" +
//            "     \"owner\": {\n" +
//            "         \"id\": \"b21a80b1-0c09-4be3-9ebd-ea3653511c13\",\n" +
//            "         \"name\": \"Ultimate Corp, Inc.\"\n" +
//            "         }\n" +
//            "     }\n" +
//            " }";
//
//    private static final byte[] SOURCE_JSON = data.getBytes(StandardCharsets.UTF_8);
//
//    @Test
//    public void whenUsing_Annotations_thenOk() throws IOException {
//        Product product = new ObjectMapper().readerFor(Product.class).readValue(SOURCE_JSON);
//
//        assertEquals(product.getName(), "The Best Product");
//        assertEquals(product.getBrandName(), "ACME Products");
//        assertEquals(product.getOwnerName(), "Ultimate Corp, Inc.");
//    }
//}
