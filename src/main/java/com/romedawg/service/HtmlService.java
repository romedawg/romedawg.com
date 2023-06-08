package com.romedawg.service;

import java.io.IOException;
import java.net.URISyntaxException;

public interface HtmlService {
    String markdownToHtml(String html);
    String markdownDocument(String filename) throws IOException, URISyntaxException;
}
