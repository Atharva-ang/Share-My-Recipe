# 🍳 Spring Boot Event-Driven Recipe Platform

A distributed microservices system built to demonstrate **Event-Driven Architecture** using Spring Boot, RabbitMQ, and Docker. 

This project decouples the **User-Facing API** (Producer) from the **Background Processing Worker** (Consumer) to ensure high scalability and responsiveness.

---

## 🏗 System Architecture

The system consists of two separate Spring Boot applications communicating asynchronously via a Message Broker. aND THIS IS FOR THE PR PURPOSE ONLY.

```mermaid
graph LR
    User[User/Postman] -- HTTP POST --> API[Recipe Platform API]
    API -- JDBC --> DB[(H2 Database)]
    API -- JSON Event --> RMQ((RabbitMQ))
    RMQ -- Consumes Message --> Worker[Recipe Worker Service]
    Worker -- Logs/Process --> Console[Console Output]
