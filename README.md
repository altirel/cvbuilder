# CV Builder
This is the project that helps employees to manage and consume **CV** and 
**Cover Letter** templates.

## Tech stack
- Java 17
- MongoDB
- GroupDocs

## How to launch
There are three simple ways how to launch the application.

### 1. IDE
- Specify required parameters in `application.yml` file.
- Add new `Spring Boot configuration` and select JDK and  main file `CvbuilderApplication.java`. 


### 2. Docker
- Open terminal and enter `docker compose up -d`

### 3. CLI
- Open CLI and enter following command to clean and build application `mvn clean package`.   
- Enter `java -jar /target/cvbuilder-0.0.1-SNAPSHOT.jar` to launch an application.
