# Task-management-system
An object-oriented Java application designed to manage employee tasks. Includes an automated task-duration estimation system, a project manager GUI, and object serialization for data persistence.

src/main/java/

├── DataModel/
│   ├── Employee.java
│   ├── Task.java          # sealed abstract class
│   ├── SimpleTask.java
│   └── ComplexTask.java   # composite pattern

├── BusinessLogic/
│   ├── TasksManagement.java
│   └── Utility.java

├── GUI/
│   └── View.java
└── Main.java

## Features

- Add employees and tasks (simple or complex)
- Assign tasks to employees
- Calculate employee work duration based on completed tasks
- Toggle task status between `Completed` / `Uncompleted`
- Filter and sort employees who worked more than 40 hours
- View per-employee breakdown of completed vs uncompleted tasks
- Data persistence via Java serialization (`.ser` files)

## Technologies

- Java 23
- Java Swing (GUI)
- Maven (build tool)
- Java Serialization (data persistence)

## How to Run

**Prerequisites:** Java 23+, Maven

```bash
mvn compile
mvn exec:java -Dexec.mainClass="Main"
```

Or open in IntelliJ IDEA and run `Main.java` directly.
