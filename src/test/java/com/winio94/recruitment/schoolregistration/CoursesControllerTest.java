package com.winio94.recruitment.schoolregistration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.winio94.recruitment.schoolregistration.api.Course;
import com.winio94.recruitment.schoolregistration.api.NewCourse;
import com.winio94.recruitment.schoolregistration.api.NewStudent;
import com.winio94.recruitment.schoolregistration.api.Registration;
import com.winio94.recruitment.schoolregistration.api.Student;
import java.util.Collections;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;

public class CoursesControllerTest extends AbstractControllerTest {

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
           .andExpect(content().json(toJsonString(Course.from(newCourse, uuid))));
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
        createNewCourse(new NewCourse("Maths", "004"));
        createNewStudent(new NewStudent("John", "Doe"));
        NewStudent newStudent = new NewStudent("Tom", "Cruise");
        NewCourse newCourse = new NewCourse("PT", "003");
        String courseUuid = getUuidFromResponse(createNewCourse(newCourse));
        String studentUuid = getUuidFromResponse(createNewStudent(newStudent));
        String registrationRequestBody = toJsonString(new Registration(studentUuid));

        mvc.perform(
               post("/courses/{uuid}/register", courseUuid).contentType(MediaType.APPLICATION_JSON)
                                                           .content(registrationRequestBody))
           .andExpect(status().isOk());

        mvc.perform(get("/courses/{uuid}/students", courseUuid))
           .andExpect(status().isOk())
           .andExpect(content().json(
               toJsonString(Collections.singletonList(Student.from(newStudent, studentUuid)))));

        mvc.perform(get("/students/{uuid}/courses", studentUuid))
           .andExpect(status().isOk())
           .andExpect(content().json(
               toJsonString(Collections.singletonList(Course.from(newCourse, courseUuid)))));
    }

    @Test
    public void shouldFilterCourses() throws Exception {
        NewCourse courseWithoutAnyStudent = new NewCourse("Maths", "004");
        NewStudent newStudent = new NewStudent("Tom", "Cruise");
        NewCourse newCourse = new NewCourse("PT", "003");
        String courseWithoutAnyStudentUuid = getUuidFromResponse(
            createNewCourse(courseWithoutAnyStudent));
        String courseUuid = getUuidFromResponse(createNewCourse(newCourse));
        String studentUuid = getUuidFromResponse(createNewStudent(newStudent));
        String registrationRequestBody = toJsonString(new Registration(studentUuid));

        mvc.perform(
               post("/courses/{uuid}/register", courseUuid).contentType(MediaType.APPLICATION_JSON)
                                                           .content(registrationRequestBody))
           .andExpect(status().isOk());

        mvc.perform(get("/courses").param("student", studentUuid))
           .andExpect(status().isOk())
           .andExpect(content().json(
               toJsonString(Collections.singletonList(Course.from(newCourse, courseUuid)))));

        mvc.perform(get("/courses").param("withoutAnyStudent", "true"))
           .andExpect(status().isOk())
           .andExpect(content().json(toJsonString(Collections.singletonList(
               Course.from(courseWithoutAnyStudent, courseWithoutAnyStudentUuid)))));
    }

    @ParameterizedTest
    @MethodSource("invalidCourses")
    public void invalidCourseTest(NewCourse newCourse, String errorResponseBody) throws Exception {
        String requestBody = toJsonString(newCourse);

        mvc.perform(post("/courses").contentType(MediaType.APPLICATION_JSON).content(requestBody))
           .andExpect(status().isBadRequest())
           .andExpect(content().json(TestUtils.readFileAsString(errorResponseBody), true));
    }

    public static Stream<Arguments> invalidCourses() {
        return Stream.of(
            Arguments.of(new NewCourse(null, null), "response/error/missingNameAndCode.json"),
            Arguments.of(new NewCourse("Maths", null), "response/error/missingCode.json"),
            Arguments.of(new NewCourse(null, "101"), "response/error/missingName.json"),
            Arguments.of(new NewCourse("emptyCode", ""), "response/error/missingCode.json"),
            Arguments.of(new NewCourse("", "emptyName"), "response/error/missingName.json"),
            Arguments.of(new NewCourse("tooLongCode", StringUtils.repeat("a", 101)),
                         "response/error/tooLongCode.json"),
            Arguments.of(new NewCourse(StringUtils.repeat("a", 101), "some code"),
                         "response/error/tooLongName.json"));
    }


    public static Stream<Arguments> byUuidMethods() {
        return Stream.of(Arguments.of(get("/courses/{uuid}", "suchCourseDoesNotExist")),
                         Arguments.of(delete("/courses/{uuid}", "suchCourseDoesNotExist")));
    }
}


