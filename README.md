fs-professional-development-rest
================================

This is my simple RESTful todos app to show my skills.

**This is a smiple todos app**

To run
 * You need a postgres database. Set the connection details in the pom.
 * Run command ```mvn compile flyway:migrate``` to setup the database tables.
 * Run command ```mvn clean tomcat7:run-war``` to start the service.
 * Check http://localhost:8080/todo/ for web pages.
 * Check http://localhost:8080/todo/api.html for api documentation.
