package com.winio94.recruitment.schoolregistration;

import static com.winio94.recruitment.schoolregistration.TestUtils.anyUuidComparator;
import static com.winio94.recruitment.schoolregistration.TestUtils.randomPersonalId;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        createNewStudent(new NewStudent("John", "Doe", "111"));
        createNewStudent(new NewStudent("Karen", "Simson", "222"));
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
        NewStudent newStudent = new NewStudent("Tom", "Cruise", randomPersonalId());
        ResultActions createStudentResponse = createNewStudent(newStudent);
        String uuid = getUuidFromResponse(createStudentResponse);

        mvc.perform(get("/students/{uuid}", uuid))
           .andExpect(status().isOk())
           .andExpect(content().json(toJsonString(Student.from(newStudent, uuid))));
    }

    @ParameterizedTest
    @MethodSource("byUuidMethods")
    public void shouldReturnNotFoundErrorIfStudentDoesNotExist(RequestBuilder requestBuilder) throws Exception {
        mvc.perform(requestBuilder)
           .andExpect(status().isNotFound());
    }

    @Test
    public void shouldCreateNewStudent() throws Exception {
        createNewStudent(new NewStudent("Monica", "Bellucci", randomPersonalId()));
    }

    @Test
    public void shouldNotAllowToCreateNewStudentWithSamePersonalId() throws Exception {
        String personalId = randomPersonalId();
        createNewStudent(new NewStudent("Monica", "Bellucci", personalId));

        String secondStudent = toJsonString(new NewStudent("Johny", "Depp", personalId));
        mvc.perform(post("/students").contentType(MediaType.APPLICATION_JSON)
                                     .content(secondStudent))
           .andExpect(status().isBadRequest())
           .andExpect(content().json(TestUtils.readFileAsString("response/error/duplicatePersonalId.json")));
    }

    @Test
    public void shouldDeleteExistingStudent() throws Exception {
        ResultActions createStudentResponse = createNewStudent(new NewStudent("Tom",
                                                                              "Cruise",
                                                                              randomPersonalId()));
        String uuid = getUuidFromResponse(createStudentResponse);

        mvc.perform(delete("/students/{uuid}", uuid))
           .andExpect(status().isNoContent());
    }

    @Test
    public void shouldFilterStudents() throws Exception {
        NewStudent studentNotRegisteredToAnyCourse = new NewStudent("John", "Doe", randomPersonalId());
        NewStudent newStudent = new NewStudent("Tom", "Cruise", randomPersonalId());
        NewCourse newCourse = new NewCourse("PT", "003");
        String studentNotRegisteredToAnyCourseUuid = getUuidFromResponse(createNewStudent(
            studentNotRegisteredToAnyCourse));
        String courseUuid = getUuidFromResponse(createNewCourse(newCourse));
        String studentUuid = getUuidFromResponse(createNewStudent(newStudent));
        String registrationRequestBody = toJsonString(new Registration(studentUuid));

        mvc.perform(post("/courses/{uuid}/register", courseUuid).contentType(MediaType.APPLICATION_JSON)
                                                                .content(registrationRequestBody))
           .andExpect(status().isOk());

        mvc.perform(get("/students").param("course", courseUuid))
           .andExpect(status().isOk())
           .andExpect(content().json(toJsonString(Collections.singletonList(Student.from(newStudent,
                                                                                         studentUuid)))));

        mvc.perform(get("/students").param("notRegisteredToAnyCourse", "true"))
           .andExpect(status().isOk())
           .andExpect(content().json(toJsonString(Collections.singletonList(Student.from(
               studentNotRegisteredToAnyCourse,
               studentNotRegisteredToAnyCourseUuid)))));
    }

    @ParameterizedTest
    @MethodSource("invalidStudents")
    public void invalidRequestBodyTest(NewStudent newStudent, String errorResponseBody) throws Exception {
        String requestBody = toJsonString(newStudent);

        mvc.perform(post("/students").contentType(MediaType.APPLICATION_JSON)
                                     .content(requestBody))
           .andExpect(status().isBadRequest())
           .andExpect(content().json(TestUtils.readFileAsString(errorResponseBody), false));
    }

    @ParameterizedTest
    @MethodSource("withInvalidUuidParam")
    public void shouldReturnBadRequestErrorWhenUuidParamHasInvalidFormat(RequestBuilder requestBuilder,
                                                                         String paramName) throws Exception {

        mvc.perform(requestBuilder)
           .andExpect(status().isBadRequest())
           .andExpect(content().json(TestUtils.readFileAsString("response/error/invalidUuidParam.json")
                                              .replaceAll("<PARAM_NAME>", paramName), true));
    }

    public static Stream<Arguments> invalidStudents() {
        return Stream.of(Arguments.of(new NewStudent(null, null, randomPersonalId()),
                                      "response/error/missingFirstNameAndLastName.json"),
                         Arguments.of(new NewStudent("Monica", null, randomPersonalId()),
                                      "response/error/missingLastName.json"),
                         Arguments.of(new NewStudent(null, "Bellucci", randomPersonalId()),
                                      "response/error/missingFirstName.json"),
                         Arguments.of(new NewStudent("emptyLastName", "", randomPersonalId()),
                                      "response/error/missingLastName.json"),
                         Arguments.of(new NewStudent("", "emptyFirstName", randomPersonalId()),
                                      "response/error/missingFirstName.json"),
                         Arguments.of(new NewStudent("tooLongLastName",
                                                     StringUtils.repeat("a", 101),
                                                     randomPersonalId()), "response/error/tooLongLastName.json"),
                         Arguments.of(new NewStudent(StringUtils.repeat("a", 101),
                                                     "tooLongFirstName",
                                                     randomPersonalId()), "response/error/tooLongFirstName.json"));
    }

    public static Stream<Arguments> byUuidMethods() {
        return Stream.of(Arguments.of(get("/students/{uuid}",
                                          UUID.randomUUID()
                                              .toString())),
                         Arguments.of(delete("/students/{uuid}",
                                             UUID.randomUUID()
                                                 .toString())));
    }

    public static Stream<Arguments> withInvalidUuidParam() {
        String invalidUuid = "invalidUuid";

        Stream<Arguments> invalidPathParams = Stream.of(Arguments.of(get("/students/{uuid}", invalidUuid), "uuid"),
                                                        Arguments.of(get("/students/{uuid}/courses", invalidUuid),
                                                                     "uuid"),
                                                        Arguments.of(delete("/students/{uuid}", invalidUuid),
                                                                     "uuid"));

        Stream<Arguments> invalidQueryParams = Stream.of(Arguments.of(get("/students").param("course",
                                                                                             invalidUuid),
                                                                      "courseUuid"));

        return Stream.concat(invalidPathParams, invalidQueryParams);
    }
}


