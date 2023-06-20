package com.romedawg.service;

import com.romedawg.controller.MetraController;
import org.commonmark.html.HtmlRenderer;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URISyntaxException;




@Service
public class HtmlServiceImpl implements HtmlService{

    private static final Logger log = LoggerFactory.getLogger(MetraController.class);

    @Override
    public String markdownToHtml(String markdown) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();

        return renderer.render(document);
    }

    @Override
    public String markdownDocument(String filename) throws IOException, URISyntaxException {
        Parser parser = Parser.builder().build();
        log.info("mardownDocument: create markdown to HTML object");

        // Breaks here
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("/markdownPages/" + filename + ".md");
        String data = readFromInputStream(inputStream);

        Node document = parser.parse(data);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }


    private String readFromInputStream(InputStream inputStream) throws IOException {

        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))){
            String line;
            while ((line = br.readLine()) != null){
                stringBuilder.append(line).append("\n");
            }
        }
        return stringBuilder.toString();

    }

}
