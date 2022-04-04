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

-----------------------------------------------------------------------

<h4>Running tests</h4>

* To run unit tests, execute following maven command from project root
  directory : `mvn clean verify`

<h4>Building project</h4>

* To build project, execute following maven command from project root
  directory : `mvn clean install`
* To build project without running tests, execute following maven command from project root
  directory : `mvn clean install -DskipTests=true`

<h4>Running application</h4>
<h5>Maven</h5>

* To run application locally from generated sources using maven, execute following maven command
  from project root directory :
  `mvn spring-boot:run -Dspring-boot.run.profiles=dev`
    * REST API will be accessible on port 8080
    * Application will store data in the in-memory H2 database

<h5>Docker</h5>

To run application locally, execute following docker-compose command from project root directory : `docker-compose up -d`

* REST API will be accessible on port 6868 (running as a container)
* Application will store data in MySQL database (running as a separate container)

Checking logs

* Run `docker ps`
    * Find Container ID for running application `school-registration-app`
* Run `docker logs <CONTAINER_ID>`

-----------------------------------------------------------------------

<h3>Postman setup</h3>
All existing REST APIs can be executed through Postman REST API client

* Postman collection can be
  found [here](./infrastructure/postman/school_registration_system.postman_collection.json)
* Postman environment for application built via maven command can be
  found [here](./infrastructure/postman/LOCAL.postman_environment.json)
* Postman environment for application built docker compose command can be
  found [here](./infrastructure/postman/LOCAL_DOCKER.postman_environment.json)

-----------------------------------------------------------------------

<h3>REST API documentation</h3>

* API contract can be opened via https://editor.swagger.io/#. File can be found [here](./infrastructure/contract/openapi.yaml)
* API documentation can also be accessed via web browser [here](http://localhost:8080/swagger-ui/index.html), provided application is up and running. 

  -----------------------------------------------------------------------

TODO tasks

* API for removing registration for a given student (currently there is no such API, and once
  student registers to some course, neither student nor course cannot be deleted)
* Proper error handling for database integrity errors
    * removing student/course while there is an existing registration for them
    * attempting to register student to given course for second time
* Setting up CI/CD (e.g. Github actions)
* migrate to latest Java
