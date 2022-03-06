package com.winio94.recruitment.schoolregistration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.winio94.recruitment.schoolregistration.api.Course;
import com.winio94.recruitment.schoolregistration.api.NewCourse;
import com.winio94.recruitment.schoolregistration.api.NewStudent;
import com.winio94.recruitment.schoolregistration.api.RegisterStudentToCourse;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;

public class CoursesControllerTest extends AbstractControllerTest {
    //todo test for null/empty payloads & path variables

    @Test
    public void shouldReturnListOfCourses() throws Exception {
        createNewCourse(new NewCourse("Maths", "001"));
        createNewCourse(new NewCourse("Physics", "002"));
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
        NewCourse newCourse = new NewCourse("PT", "003");
        ResultActions createCourseResponse = createNewCourse(newCourse);
        String uuid = getUuidFromResponse(createCourseResponse);

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
        createNewCourse(new NewCourse("PT", "003"));
    }

    @Test
    public void shouldDeleteExistingCourse() throws Exception {
        ResultActions createCourseResponse = createNewCourse(new NewCourse("PT", "003"));
        String uuid = getUuidFromResponse(createCourseResponse);

        mvc.perform(delete("/courses/{uuid}", uuid)).andExpect(status().isNoContent());
    }

    @Test
    public void shouldRegisterStudentToCourse() throws Exception {
        String courseUuid = getUuidFromResponse(createNewCourse(new NewCourse("PT", "003")));

        String studentUuid = getUuidFromResponse(createNewStudent(new NewStudent("Tom", "Cruise")));

        String requestBody = objectMapper.writeValueAsString(
            new RegisterStudentToCourse(studentUuid));

        mvc.perform(
               post("/courses/{uuid}/register", courseUuid).contentType(MediaType.APPLICATION_JSON)
                                                           .content(requestBody))
           .andExpect(status().isOk());
    }

    public static Stream<Arguments> byUuidMethods() {
        return Stream.of(Arguments.of(get("/courses/{uuid}", "suchCourseDoesNotExist")),
                         Arguments.of(delete("/courses/{uuid}", "suchCourseDoesNotExist")));
    }
}


