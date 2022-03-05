package com.winio94.recruitment.schoolregistration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.winio94.recruitment.schoolregistration.api.Course;
import com.winio94.recruitment.schoolregistration.api.CreateNewCourse;
import com.winio94.recruitment.schoolregistration.service.CoursesService;
import java.util.stream.Stream;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest
public class CoursesControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CoursesService coursesService;

    @BeforeEach
    void beforeEach() {
        coursesService.getAll().forEach(course -> coursesService.delete(course.getUuid()));
    }

    @Test
    public void shouldReturnListOfCourses() throws Exception {
        createNewCourse(new CreateNewCourse("Maths", "001"));
        createNewCourse(new CreateNewCourse("Physics", "002"));
        String expectedResponse = TestUtils.readFileAsString("response/getAllCourses.json");

        String response = mvc.perform(get("/courses"))
                             .andExpect(status().isOk())
                             .andReturn()
                             .getResponse()
                             .getContentAsString();

        JSONAssert.assertEquals(expectedResponse, response, TestUtils.anyUuidComparator);
    }

    @Test
    public void shouldReturnCourseByUuid() throws Exception {
        CreateNewCourse newCourse = new CreateNewCourse("PT", "003");
        ResultActions createCourseResponse = createNewCourse(newCourse);
        String uuid = TestUtils.getUuidFromResponse(createCourseResponse);

        mvc.perform(get("/courses/{uuid}", uuid))
           .andExpect(status().isOk())
           .andExpect(
               content().json(new ObjectMapper().writeValueAsString(Course.from(newCourse, uuid))));
    }

    @ParameterizedTest
    @MethodSource("byUuidMethods")
    public void shouldReturnNotFoundErrorIfCourseDoesNotExist(RequestBuilder requestBuilder)
        throws Exception {
        mvc.perform(requestBuilder).andExpect(status().isNotFound());
    }

    @Test
    public void shouldCreateNewCourse() throws Exception {
        createNewCourse(new CreateNewCourse("PT", "003"));
    }

    @Test
    public void shouldDeleteExistingCourse() throws Exception {
        ResultActions createCourseResponse = createNewCourse(new CreateNewCourse("PT", "003"));
        String uuid = TestUtils.getUuidFromResponse(createCourseResponse);

        mvc.perform(delete("/courses/{uuid}", uuid)).andExpect(status().isNoContent());
    }

    private ResultActions createNewCourse(CreateNewCourse newCourse) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(newCourse);

        return mvc.perform(
                      post("/courses").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                  .andExpect(status().isCreated())
                  .andExpect(content().json(requestBody, false))
                  .andExpect(jsonPath("$.uuid", Matchers.not(Matchers.empty())));
    }

    public static Stream<Arguments> byUuidMethods() {
        return Stream.of(Arguments.of(get("/courses/{uuid}", "suchCourseDoesNotExist")),
                         Arguments.of(delete("/courses/{uuid}", "suchCourseDoesNotExist")));
    }
}


