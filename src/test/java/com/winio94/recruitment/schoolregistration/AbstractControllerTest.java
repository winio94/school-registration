package com.winio94.recruitment.schoolregistration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.winio94.recruitment.schoolregistration.api.NewCourse;
import com.winio94.recruitment.schoolregistration.api.NewStudent;
import com.winio94.recruitment.schoolregistration.db.DbCourseRepository;
import com.winio94.recruitment.schoolregistration.db.DbRegistrationRepository;
import com.winio94.recruitment.schoolregistration.db.DbStudentsRepository;
import java.io.UnsupportedEncodingException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
abstract class AbstractControllerTest {

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    MockMvc mvc;

    @Autowired
    private DbRegistrationRepository dbRegistrationRepository;

    @Autowired
    private DbCourseRepository dbCourseRepository;

    @Autowired
    private DbStudentsRepository dbStudentsRepository;

    @BeforeEach
    void beforeEach() {
        dbRegistrationRepository.deleteAll();
        dbCourseRepository.deleteAll();
        dbStudentsRepository.deleteAll();
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
