# Postcode Distance Calculator
This is a simple application to calculate the distance between two postcodes in the UK.

#### ![Java](https://img.shields.io/badge/Java-8-red)  ![Gradle](https://img.shields.io/badge/Gradle-7.5.5-lightgrey) ![Spring Framework](https://img.shields.io/badge/Spring_Framework-2.7.18-darkgreen) ![H2](https://img.shields.io/badge/Database-H2-blue)

## Getting Started
### Setting Up Environment
1. You will need to have Java 8 installed in your machine.
2. The Gradle Wrapper is included in the project, so you do not need to install Gradle.

### Start the application (via Gradle)
##### Simple start: ``` gradle :clean :bootRun --stacktrace ```
##### Details start: ``` gradle :clean :bootRun --stacktrace --scan -i ```

###### If the message below appeared, you may type 'no' to skip:
    Publishing a build scan to scans.gradle.com requires accepting the Gradle Terms of Service defined at https://gradle.com/terms-of-service. Do you accept these terms? [yes, no]

###### Once you have saw the message below, meaning the application is running and listening to port 8080:
    INFO  org.springframework.boot.web.embedded.tomcat.TomcatWebServer   : Tomcat started on port(s): 8080 (http) with context path ''
    INFO  com.postcode.calculator.PostcodeDistanceCalculatorApplication  : Started MyPostalCodeCalculatorApplication in x.xxx seconds (JVM running for 4.5)

### Run Application Test (Gradle)
##### Simple test: ``` gradle :clean :test --stacktrace ```
##### Details test: ``` gradle :clean :test --stacktrace --scan -i ```

### Connecting to H2 Database console
###### You may connect to the H2 Database console that looks like the screenshot below:
###### You may want to refer the credential [here](#H2-Database-Console) for the login
![H2 Console](_assets%2Fh2-console.png)

## Reference Documentation
### Postman Collection
###### You may download the Postman Collection below and import into your Postman Application to test the application:
[Postcode Distance Calculator.postman_collection.json](_assets%2FGeographical.postman_collection.json)

## Credentials
#### H2 Database Console
    URL: http://localhost:8080/calculator-db-console
    Username: admin
    Password: admin

#### Application Credentials
    Username: calculator_admin
    Password: admin123
