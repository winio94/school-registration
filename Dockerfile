FROM maven:3.8.2-jdk-8
WORKDIR .
COPY . .
RUN mvn -Dmaven.test.skip package
CMD mvn spring-boot:run