**Simple CRUD REST service for Tasks**

Tech stack:
Java 17, Spring Boot WEB, Spring Boot JPA, DataBase - H2 (in memory mode)


<b>Task entity</b>:<br>
Task(id, title, description,userId)


<b>End-Points</b>:
1. POST /tasks — create a new Task
2. GET /tasks/{id} — get Task by ID.
3. PUT /tasks/{id} — update Task.
4. DELETE /tasks/{id} — delete Task by ID.
5. GET /tasks — get all Tasks.


**Usage:**
Build a jar file with command:<br>
`mvn clean install`

Launch jar file with command via terminal:

`${JAVA_HOME}\java.exe -jar ${work_dir}\target\task-service-0.0.1-SNAPSHOT.jar`

Java: 17
<br>Maven: 3