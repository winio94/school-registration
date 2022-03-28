package com.winio94.recruitment.schoolregistration;

import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;

public class TestUtils {

    static final CustomComparator anyUuidComparator = new CustomComparator(JSONCompareMode.NON_EXTENSIBLE,
                                                                           new Customization("*.uuid",
                                                                                             JsonAssertComparators.NON_NULL_VALUE_MATCHER));

    static Path getResourceFilePath(String relativeResourcePath) {
        try {
            return Paths.get(ClassLoader.getSystemResource(relativeResourcePath)
                                        .toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(String.format("Could not find file %s.", relativeResourcePath));
        }
    }

    static String readFileAsString(String filePath) {
        try {
            return new String(Files.readAllBytes(getResourceFilePath(filePath)), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Could not find file %s.", filePath));
        }
    }
}
