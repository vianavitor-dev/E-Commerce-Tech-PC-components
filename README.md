# 🖥️ E-Commerce Tech

### PC Components & Technology E-Commerce Backend

[![Java](https://img.shields.io/badge/Java-25-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.x-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
![JUnit5](https://img.shields.io/badge/JUnit5-%23f5f5f5.svg?style=for-the-badge&logo=junit5&logoColor=dc524a)
[![MySQL](https://img.shields.io/badge/MySQL-8.x-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
![JWT](https://img.shields.io/badge/json%20web%20tokens-%23000000.svg?style=for-the-badge&logo=jsonwebtokens&logoColor=white)
![Maven](https://img.shields.io/badge/apachemaven-%23C71A36.svg?style=for-the-badge&logo=apachemaven&logoColor=white)
---

## 📖 About

**E-Commerce Tech** is a backend application for an online store focused on **PC components and technology products**.

The project is being developed with **Java and Spring Boot**, with a focus on backend engineering concepts such as:

- REST API development
- Relational database modeling
- JPA/Hibernate
- Business rules
- Product and inventory management
- Component compatibility validation
- Layered architecture

The main goal is to simulate a backend system similar to one that could be used in a real-world e-commerce application.

---

## 🎯 Project Goals

- ✅️ **Implemented**
- ☑️ **Implemented partially**
- ❌️ **Not implemented**

| Done | Goal | Description |
|---| --- |---|
| ✅️ | E-Commerce | Build a complete backend for a technology-focused online store |
| ✅️ | Compatibility | Validate whether PC components can work together |
| ✅️ | Database | Model products and hardware specifications using a relational database |
| ✅️ | Security | Add authentication and authorization |
| ❌️ | Inventory | Manage product stock and SKUs |
| ☑️ | Orders | Handle carts, orders and order status |
| ✅️ | Testing | Add unit and integration tests |
| ❌️ | DevOps | Containerize and automate the application |
---

## 🛠️ Tech Stack

### Backend

| Technology | Purpose |
|---|---|
| Java | Main programming language |
| Spring Boot | Application framework |
| Maven | Dependency management |


### Database

| Technology | Purpose |
|---|---|
| MySQL | Relational database |

---

## How to run it

###  Prerequisites
- Java 25
- MySQL 8.0

### 1. Clone this repository
```bash
git clone https://github.com/vianavitor-dev/E-Commerce-Tech-PC-components.git
cd ecommerce-tech
```


### 2. Set up MySQL connection
modify the `application-dev.properties` to match the required connection settings of your database (JDBC URL, user and password)
```java
spring.datasource.url= // your JDBC URL connection, template: jdbc:<datatabase>://<host>:<port>/<database-name>
spring.datasource.username= // your database username (commonly: root)
spring.datasource.password= // your database user's password
```
you can use the [application-dev.properties](src/main/resources/application-dev.properties) of this repository as example if necessary


## 3. Run application
```bash
./mvnw spring-boot::run -Dspring-boot.run.profiles=dev
```

