package com.winio94.recruitment.schoolregistration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.winio94.recruitment.schoolregistration.api.CreateNewStudent;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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
        String expectedResponse = readFileAsString("response/getAllStudents.json");

        mvc.perform(get("/students").contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(content().json(expectedResponse));
    }

    @ParameterizedTest
    @MethodSource("studentsByUuid")
    public void shouldReturnStudentByUuid(String uuid, String expectedResponseFile)
        throws Exception {
        String expectedResponse = readFileAsString(expectedResponseFile);

        mvc.perform(get("/students/{uuid}", uuid).contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(content().json(expectedResponse));
    }

    @Test
    public void shouldReturnReturnNotFoundResponseIfStudentNotFound() throws Exception {
        mvc.perform(get("/students/{uuid}", "suchStudentDoesNotExist").contentType(
            MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    public void shouldCreateNewStudent() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(
            new CreateNewStudent("Monica", "Bellucci"));

        mvc.perform(post("/students").contentType(MediaType.APPLICATION_JSON).content(requestBody))
           .andExpect(status().isCreated())
           .andExpect(content().json(requestBody, false))
           .andExpect(jsonPath("$.uuid", Matchers.not(Matchers.empty())));
    }

    public static Stream<Arguments> studentsByUuid() {
        return Stream.of(
            Arguments.of("59ee2a28-24fd-49fb-9bd7-eb34f77c6dc1", "response/studentJohn.json"),
            Arguments.of("0992e41b-78e9-4cb2-83de-5816e7c59283", "response/studentKaren.json"));
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


