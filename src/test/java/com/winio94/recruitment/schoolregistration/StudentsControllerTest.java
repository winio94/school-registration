package com.winio94.recruitment.schoolregistration;

import static com.winio94.recruitment.schoolregistration.TestUtils.anyUuidComparator;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.winio94.recruitment.schoolregistration.api.NewCourse;
import com.winio94.recruitment.schoolregistration.api.NewStudent;
import com.winio94.recruitment.schoolregistration.api.Registration;
import com.winio94.recruitment.schoolregistration.api.Student;
import java.util.Collections;
import java.util.UUID;
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

public class StudentsControllerTest extends AbstractControllerTest {

    @Test
    public void shouldReturnListOfStudents() throws Exception {
        createNewStudent(new NewStudent("John", "Doe"));
        createNewStudent(new NewStudent("Karen", "Simson"));
        String expectedResponse = TestUtils.readFileAsString("response/getAllStudents.json");

        String response = mvc.perform(get("/students"))
                             .andExpect(status().isOk())
                             .andReturn()
                             .getResponse()
                             .getContentAsString();

        JSONAssert.assertEquals(expectedResponse, response, anyUuidComparator);
    }

    @Test
    public void shouldReturnStudentByUuid() throws Exception {
        NewStudent newStudent = new NewStudent("Tom", "Cruise");
        ResultActions createStudentResponse = createNewStudent(newStudent);
        String uuid = getUuidFromResponse(createStudentResponse);

        mvc.perform(get("/students/{uuid}", uuid))
           .andExpect(status().isOk())
           .andExpect(content().json(toJsonString(Student.from(newStudent, uuid))));
    }

    @ParameterizedTest
    @MethodSource("byUuidMethods")
    public void shouldReturnNotFoundErrorIfStudentDoesNotExist(RequestBuilder requestBuilder)
        throws Exception {
        mvc.perform(requestBuilder).andExpect(status().isNotFound());
    }

    @Test
    public void shouldCreateNewStudent() throws Exception {
        createNewStudent(new NewStudent("Monica", "Bellucci"));
    }

    @Test
    public void shouldDeleteExistingStudent() throws Exception {
        ResultActions createStudentResponse = createNewStudent(new NewStudent("Tom", "Cruise"));
        String uuid = getUuidFromResponse(createStudentResponse);

        mvc.perform(delete("/students/{uuid}", uuid)).andExpect(status().isNoContent());
    }

    @Test
    public void shouldFilterStudents() throws Exception {
        NewStudent studentNotRegisteredToAnyCourse = new NewStudent("John", "Doe");
        NewStudent newStudent = new NewStudent("Tom", "Cruise");
        NewCourse newCourse = new NewCourse("PT", "003");
        String studentNotRegisteredToAnyCourseUuid = getUuidFromResponse(
            createNewStudent(studentNotRegisteredToAnyCourse));
        String courseUuid = getUuidFromResponse(createNewCourse(newCourse));
        String studentUuid = getUuidFromResponse(createNewStudent(newStudent));
        String registrationRequestBody = toJsonString(new Registration(studentUuid));

        mvc.perform(
               post("/courses/{uuid}/register", courseUuid).contentType(MediaType.APPLICATION_JSON)
                                                           .content(registrationRequestBody))
           .andExpect(status().isOk());

        mvc.perform(get("/students").param("course", courseUuid))
           .andExpect(status().isOk())
           .andExpect(content().json(
               toJsonString(Collections.singletonList(Student.from(newStudent, studentUuid)))));

        mvc.perform(get("/students").param("notRegisteredToAnyCourse", "true"))
           .andExpect(status().isOk())
           .andExpect(content().json(toJsonString(Collections.singletonList(
               Student.from(studentNotRegisteredToAnyCourse,
                            studentNotRegisteredToAnyCourseUuid)))));
    }

    @ParameterizedTest
    @MethodSource("invalidStudents")
    public void invalidRequestBodyTest(NewStudent newStudent, String errorResponseBody)
        throws Exception {
        String requestBody = toJsonString(newStudent);

        mvc.perform(post("/students").contentType(MediaType.APPLICATION_JSON).content(requestBody))
           .andExpect(status().isBadRequest())
           .andExpect(content().json(TestUtils.readFileAsString(errorResponseBody), true));
    }

    @ParameterizedTest
    @MethodSource("withInvalidUuidParam")
    public void shouldReturnBadRequestErrorWhenUuidParamHasInvalidFormat(
        RequestBuilder requestBuilder, String paramName) throws Exception {

        mvc.perform(requestBuilder)
           .andExpect(status().isBadRequest())
           .andExpect(content().json(
               TestUtils.readFileAsString("response/error/invalidUuidParam.json")
                        .replaceAll("<PARAM_NAME>", paramName), true));
    }

    public static Stream<Arguments> invalidStudents() {
        return Stream.of(Arguments.of(new NewStudent(null, null),
                                      "response/error/missingFirstNameAndLastName.json"),
                         Arguments.of(new NewStudent("Monica", null),
                                      "response/error/missingLastName.json"),
                         Arguments.of(new NewStudent(null, "Bellucci"),
                                      "response/error/missingFirstName.json"),
                         Arguments.of(new NewStudent("emptyLastName", ""),
                                      "response/error/missingLastName.json"),
                         Arguments.of(new NewStudent("", "emptyFirstName"),
                                      "response/error/missingFirstName.json"), Arguments.of(
                new NewStudent("tooLongLastName", StringUtils.repeat("a", 101)),
                "response/error/tooLongLastName.json"), Arguments.of(
                new NewStudent(StringUtils.repeat("a", 101), "tooLongFirstName"),
                "response/error/tooLongFirstName.json"));
    }

    public static Stream<Arguments> byUuidMethods() {
        return Stream.of(Arguments.of(get("/students/{uuid}", "suchStudentDoesNotExist")),
                         Arguments.of(delete("/students/{uuid}", "suchStudentDoesNotExist")));
    }

    public static Stream<Arguments> withInvalidUuidParam() {
        String invalidUuid = "invalidUuid";

        Stream<Arguments> invalidPathParams = Stream.of(
            Arguments.of(get("/students/{uuid}", invalidUuid), "uuid"),
            Arguments.of(get("/students/{uuid}/courses", invalidUuid), "uuid"),
            Arguments.of(delete("/students/{uuid}", invalidUuid), "uuid"));

        Stream<Arguments> invalidQueryParams = Stream.of(
            Arguments.of(get("/students").param("course", invalidUuid), "courseUuid"));

        return Stream.concat(invalidPathParams, invalidQueryParams);
    }
}


