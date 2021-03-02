# central_de_erros
Codenation Java acceleration final challenge
Project presentation (in portuguese): https://www.youtube.com/watch?v=9pBdSP9gzP0

## Purpose

This RESTfull API is meant to gather all errors from an application in one place.
You can redirect errors from front-end, back-end, database etc to this app.

## Documentation

The application is hosted at https://matheuscentraldeerros.herokuapp.com/.

It's secured with Oauth2 has `password` scope. The client username is `codenation` and password `codenation123`.

You can create a new user with password using `POST /users` endpoint and then retrieve your access token at `POST /oauth/token`.

For endpoints documentation see [Central de Erros docs](https://matheuscentraldeerros.herokuapp.com/swagger-ui.html).

## Build and run

You'll need to have PostgreSql database installed in your machine.

To connect with your database, add these lines to `application.properties`:
```
spring.datasource.url=jdbc:postgresql://localhost:5432/<database name>
spring.datasource.username=<your username>
spring.datasource.password=<your password>
```
Go to the root of the project and run:

`./mvnw spring-boot:run` for Linux and Mac
`./mvnw.cmd spring-boot:run` on Windows

