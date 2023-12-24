# CV Builder
Are you looking for job and tired of building your CV and CL for every 
single vacancy and position? CV Builder simplifies this process.  

This only require a few easy steps:
1. Upload your own CV/CL template or select one of provided;
2. Specify values for required fields;
3. Choose format to save (`.pdf`/`.docx`).  

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
- Open CLI and run `mvn clean package` to build application.
- Open terminal and run following command `docker compose up -d`.

### 3. CLI
- Open CLI and run `mvn clean package` to build application.   
- Run `java -jar /target/cvbuilder-0.0.1-SNAPSHOT.jar` to launch an application.
