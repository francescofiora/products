# Products
SpringBoot Rest Api tutorial with OpenApi 3.0 and PostgreSql.

## Topics covered
- Spring Boot Rest Api
- Swagger UI for visualizing APIs
- Error Handling
- Basic Authentication with roles
- Mapper for POJO<->DTO
- Actuator
- Logging
- Testing
    - Repositories using DataJpaTest
    - Services using Mockito
    - EndPoints using WebMvcTest
    - Pojos and Dtos using OpenPojo
    - End to End Test with TestRestTemplate
    - Integration test using TestContainers
- Eclipse support

# Getting Started
### Compile
    ./gradlew clean build

### Dependency-Check
    ./gradlew dependencyCheckAnalyze --info

### Pitest
    ./gradlew pitest

### Reports
    product-api/build/reports/checkstyle/main.html
    product-api/build/reports/checkstyle/test.html
    product-api/build/reports/tests/test/index.html
    product-api/build/reports/jacoco/test/html/index.html
    product-api/build/reports/pitest/index.html
    product-api/build/reports/dependency-check-report.html

    product-dto/build/reports/checkstyle/main.html
    product-dto/build/reports/checkstyle/test.html
    product-dto/build/reports/tests/test/index.html
    product-dto/build/reports/jacoco/test/html/index.html
    product-dto/build/reports/pitest/index.html
    product-dto/build/reports/dependency-check-report.html

### How to execute
    docker-compose -f docker_dev/docker-compose.yml up
    java -jar ./product-api/build/libs/product-api-1.0-SNAPSHOT.jar

**Links**
 - http://localhost:8081/swagger-ui.html (Api Doc)
 - https://localhost/ (phpPgAdmin)

## Security
 - Basic Authentication with roles
   - USER: (user/password)
   - ADMIN: (admin/password)

##Actuator
    curl -u user:password 'http://localhost:8081/actuator/info' -i -X GET
    curl -u user:password 'http://localhost:8081/actuator/health' -i -X GET

### Create docker image
    ./gradlew jibDockerBuild

### Integration test

###Publish client and dto jars
    ./gradlew publishToMavenLocal

###Run Integration tests
    cd product-integration-test
    ./gradlew clean build


# Technologies used
- [Gradle 7.4](https://gradle.org)
- [Java 17](https://openjdk.java.net/projects/jdk/17)
- [Spring Boot 3.1](https://spring.io/projects/spring-boot)
- [Spring Security](https://spring.io/projects/spring-security)
- [Swagger OpeApi 3.0](https://swagger.io/specification)
- [Mapstruct 1.5](https://mapstruct.org)
- [Lombok 1.18](https://projectlombok.org)
- [Spring Data JPA](https://projects.spring.io/spring-data-jpa)
- [org.postgresql 42.6](https://jdbc.postgresql.org)
- [HsqlDb](http://hsqldb.org)
- [LogBack 1.4](https://logback.qos.ch)
- [Mockito](https://site.mockito.org)
- [JUnit 5](https://junit.org/junit5)
- [OpenPojo 0.9](https://github.com/OpenPojo)
- [CheckStyle 8.44](https://checkstyle.sourceforge.io)
- [Owasp Dependency Check 7.4](https://owasp.org/www-project-dependency-check)
- [Jacoco 0.8](https://www.jacoco.org)
- [Pitest 1.7](https://pitest.org)
- [TestContainers 1.19](https://www.testcontainers.org)
