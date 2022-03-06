package com.winio94.recruitment.schoolregistration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.winio94.recruitment.schoolregistration.api.NewCourse;
import com.winio94.recruitment.schoolregistration.api.NewStudent;
import com.winio94.recruitment.schoolregistration.service.CoursesService;
import com.winio94.recruitment.schoolregistration.service.StudentsService;
import java.io.UnsupportedEncodingException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest
abstract class AbstractControllerTest {

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    MockMvc mvc;

    @Autowired
    private CoursesService coursesService;

    @Autowired
    private StudentsService studentsService;

    @BeforeEach
    void beforeEach() {
        coursesService.getAll().forEach(course -> coursesService.delete(course.getUuid()));
        studentsService.getAll().forEach(student -> studentsService.delete(student.getUuid()));
    }

    String getUuidFromResponse(ResultActions createEntityResponse)
        throws JsonProcessingException, UnsupportedEncodingException {
        return String.valueOf(objectMapper.readTree(
                         createEntityResponse.andReturn().getResponse().getContentAsString()).get("uuid"))
                     .replaceAll("\"", "");
    }

    ResultActions createNewStudent(NewStudent newStudent) throws Exception {
        String requestBody = toJsonString(newStudent);

        return mvc.perform(
                      post("/students").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                  .andExpect(status().isCreated())
                  .andExpect(content().json(requestBody, false))
                  .andExpect(jsonPath("$.uuid", Matchers.not(Matchers.empty())));
    }

    ResultActions createNewCourse(NewCourse newCourse) throws Exception {
        String requestBody = toJsonString(newCourse);

        return mvc.perform(
                      post("/courses").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                  .andExpect(status().isCreated())
                  .andExpect(content().json(requestBody, false))
                  .andExpect(jsonPath("$.uuid", Matchers.not(Matchers.empty())));
    }

    String toJsonString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException("Unable to serialize object to json string", e);
        }
    }
}
