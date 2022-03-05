package com.winio94.recruitment.schoolregistration;

import static com.winio94.recruitment.schoolregistration.TestUtils.anyUuidComparator;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.winio94.recruitment.schoolregistration.api.CreateNewStudent;
import com.winio94.recruitment.schoolregistration.api.Student;
import com.winio94.recruitment.schoolregistration.service.StudentsService;
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
public class StudentsControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private StudentsService studentsService;

    @BeforeEach
    void beforeEach() {
        studentsService.getAll().forEach(student -> studentsService.delete(student.getUuid()));
    }

    @Test
    public void shouldReturnListOfStudents() throws Exception {
        createNewStudent(new CreateNewStudent("John", "Doe"));
        createNewStudent(new CreateNewStudent("Karen", "Simson"));
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
        CreateNewStudent newStudent = new CreateNewStudent("Tom", "Cruise");
        ResultActions createStudentResponse = createNewStudent(newStudent);
        String uuid = TestUtils.getUuidFromResponse(createStudentResponse);

        mvc.perform(get("/students/{uuid}", uuid))
           .andExpect(status().isOk())
           .andExpect(content().json(
               new ObjectMapper().writeValueAsString(Student.from(newStudent, uuid))));
    }

    @ParameterizedTest
    @MethodSource("byUuidMethods")
    public void shouldReturnNotFoundErrorIfStudentDoesNotExist(RequestBuilder requestBuilder)
        throws Exception {
        mvc.perform(requestBuilder).andExpect(status().isNotFound());
    }

    @Test
    public void shouldCreateNewStudent() throws Exception {
        createNewStudent(new CreateNewStudent("Monica", "Bellucci"));
    }

    @Test
    public void shouldDeleteExistingStudent() throws Exception {
        ResultActions createStudentResponse = createNewStudent(
            new CreateNewStudent("Tom", "Cruise"));
        String uuid = TestUtils.getUuidFromResponse(createStudentResponse);

        mvc.perform(delete("/students/{uuid}", uuid)).andExpect(status().isNoContent());
    }

    private ResultActions createNewStudent(CreateNewStudent newStudent) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(newStudent);

        return mvc.perform(
                      post("/students").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                  .andExpect(status().isCreated())
                  .andExpect(content().json(requestBody, false))
                  .andExpect(jsonPath("$.uuid", Matchers.not(Matchers.empty())));
    }

    public static Stream<Arguments> byUuidMethods() {
        return Stream.of(Arguments.of(get("/students/{uuid}", "suchStudentDoesNotExist")),
                         Arguments.of(delete("/students/{uuid}", "suchStudentDoesNotExist")));
    }
}


