# Backend
This folder contains backend part of the CV Builder project.

## Tech stack
- Java 17
- MongoDB
- PDFBox
- MinIO

## Prerequisites
You must have Java 17 JDK and JRE installed on your machine. There are a lot of options where you can get it.

## How to launch
There are three simple ways how to launch the application.

### 1. IDE
- Specify required parameters in `application.yml` file.
- Add new `Spring Boot configuration` and select JDK and  main file `CvbuilderApplication.java`. 

### 2. CLI
- Open CLI and run `mvn clean package` to build application.   
- Run `java -jar /target/cvbuilder-0.0.1-SNAPSHOT.jar` to launch an application.
> [!WARNING]
> You may encounter a problem if you are trying to run the project on a Windows machine. By default, the Windows command line does not support running third-party applications such as mvn. Use any search engine to find the way how to add mvn to Path.
