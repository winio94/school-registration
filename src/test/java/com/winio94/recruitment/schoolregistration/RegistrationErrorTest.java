package com.winio94.recruitment.schoolregistration;

import static com.winio94.recruitment.schoolregistration.TestUtils.randomPersonalId;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.winio94.recruitment.schoolregistration.api.NewCourse;
import com.winio94.recruitment.schoolregistration.api.NewStudent;
import com.winio94.recruitment.schoolregistration.api.Registration;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = {"MAX_NUMBER_OF_STUDENTS_PER_COURSE=1", "MAX_NUMBER_OF_COURSES_PER_STUDENT=1"})
public class RegistrationErrorTest extends AbstractControllerTest {

    @Test
    void shouldReturnErrorWhenNumberOfStudentsExceededForGivenCourse() throws Exception {
        NewStudent student1 = new NewStudent("Tom", "Cruise", randomPersonalId());
        NewStudent student2 = new NewStudent("Monica", "Bellucci", randomPersonalId());
        NewCourse newCourse = new NewCourse("PT", "003");
        String courseUuid = getUuidFromResponse(createNewCourse(newCourse));
        String student1Uuid = getUuidFromResponse(createNewStudent(student1));
        String student2Uuid = getUuidFromResponse(createNewStudent(student2));

        mvc.perform(post("/courses/{uuid}/register", courseUuid).contentType(MediaType.APPLICATION_JSON)
                                                                .content(toJsonString(new Registration(student1Uuid))))
           .andExpect(status().isOk());

        mvc.perform(post("/courses/{uuid}/register", courseUuid).contentType(MediaType.APPLICATION_JSON)
                                                                .content(toJsonString(new Registration(student2Uuid))))
           .andExpect(status().isBadRequest())
           .andExpect(content().json(TestUtils.readFileAsString("response/error/tooManyStudentsForCourse.json")));
    }

    @Test
    void shouldReturnErrorWhenNumberOfCoursesExceededForGivenStudent() throws Exception {
        NewStudent newStudent = new NewStudent("Tom", "Cruise", randomPersonalId());
        NewCourse course1 = new NewCourse("PT", "003");
        NewCourse course2 = new NewCourse("Maths", "004");
        String course1Uuid = getUuidFromResponse(createNewCourse(course1));
        String course2Uuid = getUuidFromResponse(createNewCourse(course2));
        String studentUuid = getUuidFromResponse(createNewStudent(newStudent));

        mvc.perform(post("/courses/{uuid}/register", course1Uuid).contentType(MediaType.APPLICATION_JSON)
                                                                 .content(toJsonString(new Registration(studentUuid))))
           .andExpect(status().isOk());

        mvc.perform(post("/courses/{uuid}/register", course2Uuid).contentType(MediaType.APPLICATION_JSON)
                                                                 .content(toJsonString(new Registration(studentUuid))))
           .andExpect(status().isBadRequest())
           .andExpect(content().json(TestUtils.readFileAsString("response/error/tooManyCoursesForStudent.json")));
    }

}