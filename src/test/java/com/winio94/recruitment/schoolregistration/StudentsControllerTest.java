package com.winio94.recruitment.schoolregistration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
public class StudentsControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void shouldReturnAllStudents() throws Exception {
        String expectedStudentsResponse = readFileAsString("response/getAllStudents.json");

        mvc.perform(get("/students").contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(content().json(expectedStudentsResponse));
    }

    Path getResourceFilePath(String relativeResourcePath) {
        try {
            return Paths.get(ClassLoader.getSystemResource(relativeResourcePath).toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(
                String.format("Could not find file %s.", relativeResourcePath));
        }
    }

    String readFileAsString(String filePath) {
        try {
            return new String(Files.readAllBytes(getResourceFilePath(filePath)),
                StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Could not find file %s.", filePath));
        }
    }
}


