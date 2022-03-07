<h2>School registration system</h2>

Design and implement simple school registration system

- Assuming you already have a list of students
- Assuming you already have a list of courses
- (REQ1) A student can register to multiple courses
- (REQ2) A course can have multiple students enrolled in it.
- (REQ3) A course has 50 students maximum
- (REQ4) A student can register to 5 course maximum

Provide the following REST API:

- (TASK1) Create students CRUD
- (TASK2) Create courses CRUD
- (TASK3) Create API for students to register to courses
- (TASK4) Create abilities for user to view all relationships between students and courses

+ (TASK5) Filter all students with a specific course
+ (TASK6) Filter all courses for a specific student
+ (TASK7) Filter all courses without any students
+ (TASK8) Filter all students without any courses

TODO tasks
* missing validation 
  * missing path variables
  * uuid (path variables/query params/registration request) schema validation
* validation of uniqueness
    * unique student
    * unique course
    * unique registration for given student and course
* automatic deregistration from a course when student/course is deleted, or constraint violation
  error

-----------------------------------------------------------------------

<h3>Building project</h3>

* To build and install locally, skipping all tests: `mvn clean install -DskipTests=true`
* To run unit tests only: `mvn clean verify`

-----------------------------------------------------------------------

<h3>Postman setup</h3>
TODO

-----------------------------------------------------------------------

<h3>Code formatting</h3>
TODO

-----------------------------------------------------------------------

<h3>REST API documentation</h3>
TODO

-----------------------------------------------------------------------

Build status TODO
https://docs.github.com/en/actions/monitoring-and-troubleshooting-workflows/adding-a-workflow-status-badge