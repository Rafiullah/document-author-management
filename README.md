# Document and Author Management Web Application

## Project Overview
This project is a simple Document and Author Management System that allows users to create, update, and manage documents, authors, and their relationships.

### Features
Through the system a user can perform:
- Create, Read, Update, and Delete (CRUD) operations on documents
- Create, Read, Update, and Delete (CRUD) operations on author

## Technologies
- **Java** 
- **Spring Boot** 
- **Maven** 
- **Spring Data JPA**
- **Spring Security**
- **Postgresql** 
- **Kafka** 
- **JUnit**
- **Mockito**

## Getting Started

To get started with this project, follow these steps:

### 1. Clone the Repository

First, clone the repository to your local machine and change to the directory:

```bash
git clone https://github.com/Rafiullah/document-author-management.git
cd document-author-management 
```
### 2. Set Up Dependencies with Docker Compose
   This project uses Docker Compose to set up the required services, including Kafka, Zookeeper, and PostgreSQL. Make sure you have Docker installed on your machine.

#### Start Services
Run the following command to start the necessary services:
```bash
docker-compose up
```
This will launch Kafka, Zookeeper, and PostgreSQL as defined in the docker-compose.yml file. The services will be available at the following addresses:

- **PostgreSQL**: localhost:5433
- **Kafka**: localhost:9092
- **Zookeeper**: localhost:2181

### 3. Build and Run the Application
   Since the project itself is not included in the Docker Compose setup, you'll need to build and run the application manually.

#### Build the Application
Navigate to the directory where your application code is located and build the project using Maven:
```bash 
mvn clean install
```
#### Run the Application
After building the project, you can run the application through following command:
```bash
./mvnw spring-boot:run
```
### 4. Authenticate and Obtain a JWT Token
   Once the application is running, you need to authenticate to obtain a JWT token for accessing protected API endpoints.

#### 1. Authenticate

Send a POST request to the authentication endpoint along with credentials defined below:

- **URL**: (http://localhost:8090/authenticate)[http://localhost:8090/authenticate]
- **Method**: POST
- **Content-Type**: application/json
- **Body** (raw JSON):
```json
{
"username": "admin",
"password": "admin"
}
```
You can use tools like `curl`, `Postman`, or any HTTP client to make this request. Here’s an example using `curl`:

```bash
curl -X POST http://localhost:8090/authenticate -H "Content-Type: application/json" -d '{"username":"admin", "password":"admin"}'
```
#### 2. Receive JWT Token

Upon successful authentication, you will receive a JWT token in the response. The response will look something like this:

```json
{
"token": "your_jwt_token_here"
}
```
#### Use the JWT Token

Include the JWT token in the `Authorization` header of your requests to access protected API endpoints. The header should look like this:

```makefile
Authorization: Bearer your_jwt_token_here
```
For example, using `curl`:

```bash
curl -X GET http://localhost:8090/your-protected-endpoint -H "Authorization: Bearer your_jwt_token_here"
```

## API Documentation
API Documentation can be accessed through: </br>
[http://localhost:8090/swagger-ui/index.html](http://localhost:8090/swagger-ui/index.html)