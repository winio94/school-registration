package com.winio94.recruitment.schoolregistration;

import static com.winio94.recruitment.schoolregistration.TestUtils.anyUuidComparator;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.winio94.recruitment.schoolregistration.api.NewCourse;
import com.winio94.recruitment.schoolregistration.api.NewStudent;
import com.winio94.recruitment.schoolregistration.api.RegisterStudentToCourse;
import com.winio94.recruitment.schoolregistration.api.Student;
import java.util.Collections;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;

public class StudentsControllerTest extends AbstractControllerTest {

    //todo test for null/empty payloads & path variables

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
        String registrationRequestBody = toJsonString(new RegisterStudentToCourse(studentUuid));

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

    public static Stream<Arguments> byUuidMethods() {
        return Stream.of(Arguments.of(get("/students/{uuid}", "suchStudentDoesNotExist")),
                         Arguments.of(delete("/students/{uuid}", "suchStudentDoesNotExist")));
    }
}


