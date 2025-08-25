# Project Setup Guide

Welcome! This project helps households and workers connect for local services.  
To run this project successfully, please follow the setup steps below.

---

## Prerequisites

- **Java Development Kit (JDK) 8 or above**  
  Make sure Java is installed on your machine. [Download JDK](https://adoptopenjdk.net/)
- **MySQL or MariaDB Server**  
  Required for storing all project data.

---

## Dependencies

1. **JavaFX**  
   Used for building the project’s graphical user interface.  
   - Download: [JavaFX Downloads](https://gluonhq.com/products/javafx/)
   - Add JavaFX .jar files to your project's libraries/module path.
   - For Maven/Gradle users, see [OpenJFX Docs](https://openjfx.io/openjfx-docs/).

2. **MySQL Connector/J**  
   Enables Java to connect to MySQL/MariaDB databases.  
   - Download: [MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/)
   - Add the `mysql-connector-java-x.x.x.jar` to your project’s libraries.

3. **javax.mail**  
   Used for sending emails (e.g., notifications, confirmations).
   - Download: [javax.mail (Jakarta Mail)](https://javaee.github.io/javamail/)
   - Add the `.jar` file to your project’s libraries.

4. **Project Database**  
   The project uses a pre-configured sample database.  
   - File: [`doer.sql`](./doer.sql)
   - This file contains all database tables, sample data, views, triggers, and stored procedures needed for the app to function.

---

## Setup Instructions

### 1. Clone the Repository

```sh
git clone https://github.com/Doer-Project/Doer.git
cd Doer
```

### 2. Import the Database

1. Open MySQL Workbench, phpMyAdmin, or your command line.
2. Create a new database, e.g., `doer`.
3. Import the provided `doer.sql` file:
   ```sh
   mysql -u youruser -p doer < doer.sql
   ```
4. Update your Java application's DB connection string as needed.

### 3. Install Java Dependencies

- Ensure JavaFX, MySQL Connector/J, and javax.mail jars are added to your IDE/project.
- For manual projects, add them to your classpath.
- For Maven/Gradle, include dependencies as shown below:

#### Example Maven dependencies

```xml
<dependencies>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-controls</artifactId>
        <version>20.0.0</version>
    </dependency>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.3.0</version>
    </dependency>
    <dependency>
        <groupId>com.sun.mail</groupId>
        <artifactId>javax.mail</artifactId>
        <version>1.6.2</version>
    </dependency>
</dependencies>
```

---

## Troubleshooting

- **Database Connection Issues:**  
  Double-check your DB username, password, and port. The default port is usually `3306`.
- **JavaFX Errors:**  
  Ensure the JavaFX jars are on your module path, not just the classpath.
- **Missing Libraries:**  
  Download and add all required jars as per above instructions.

---

## Further Reading

- [JavaFX Documentation](https://openjfx.io/)
- [MySQL Connector/J Docs](https://dev.mysql.com/doc/connector-j/8.0/en/)
- [Jakarta Mail (javax.mail) Docs](https://javaee.github.io/javamail/)

---

**If you’re new to Java projects, don’t hesitate to ask for help or search the documentation links above!**