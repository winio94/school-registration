package com.winio94.recruitment.schoolregistration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.test.web.servlet.ResultActions;

public class TestUtils {

    static final CustomComparator anyUuidComparator = new CustomComparator(
        JSONCompareMode.NON_EXTENSIBLE,
        new Customization("*.uuid", JsonAssertComparators.NON_NULL_VALUE_MATCHER));

    static String getUuidFromResponse(ResultActions createEntityResponse)
        throws JsonProcessingException, UnsupportedEncodingException {
        return String.valueOf(new ObjectMapper().readTree(
                         createEntityResponse.andReturn().getResponse().getContentAsString()).get("uuid"))
                     .replaceAll("\"", "");
    }

    static Path getResourceFilePath(String relativeResourcePath) {
        try {
            return Paths.get(ClassLoader.getSystemResource(relativeResourcePath).toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(
                String.format("Could not find file %s.", relativeResourcePath));
        }
    }

    static String readFileAsString(String filePath) {
        try {
            return new String(Files.readAllBytes(getResourceFilePath(filePath)),
                              StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Could not find file %s.", filePath));
        }
    }
}
