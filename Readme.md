[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/8T067B4s)


In this project, you will perform control-flow testing on a fault-seeded Java program, [**Printtokens.java**](./src/Printtokens.java). Your tasks include drawing **control flow graphs** to derive test cases, writing **JUnit test methods**, and identifying and fixing any faults that are discovered. You are required to achieve **edge coverage** for individual methods (**unit testing**) and maximize **code coverage** for the main method (**program-level testing**).


[Project slides](./project.fall.2024.pdf)

The project can be broadly divided into 4 major steps: 
- Prepare test cases
- Write JUnit test methods
- Execute JUnit tests
- Fix faults
<hr>

## Test Case Preparation

### Create Control Flow Graph
   - Draw a control flow graph for each method
   - Example shown in [Video](https://youtu.be/lj7HY7ENU8Y)

### Design Test Cases 
   - Create test paths
   - Identity inputs and expected outputs
   - Example Given in [Video](https://youtu.be/xfANxegQJnQ)
   - Useful tool [**Oracle**](./oracle/Printtokens_Oracle.jar): provides the expected outputs for the given inputs. To open it, jre 8 is required.

## JUnit Test Method Writing and Executing and Fault Fixing

### Recommendations

- [Eclipse IDE](https://riyagoel192.medium.com/how-to-download-eclipse-java-ide-on-windows-52608032d6d9) 
- [Git](https://www.howtogeek.com/832083/how-to-install-git-on-windows/)
- [Java 8](https://www.java.com/en/download/manual.jsp)

Note: you can use other IDEs like Visual Studio Code instead of Eclipse. 

### Write and execute JUnit Test Methods and fix the found faults. 

1. Launch Git Bash and navigate to the directory where you want to work on your project. 

2. Clone (download) your project.
- Generate a personal access token ([help](https://docs.github.com/en/enterprise-server@3.9/authentication/keeping-your-account-and-data-secure/managing-your-personal-access-tokens))
 
- Clone the project repository in Git Bash:
```
git clone https://<your GitHub access token>@github.com/cse4321/<your repository name>
E.g.: git clone https://ghp_zvmpaRAjpE8UPuxjCXf5Dg3lW11GpC2b@github.com/cse4321/PrinttokensTesting.git
```

3. Explore how to write test methods with JUnit in Eclipse IDE. 
- Launch Eclipse IDE and import the project (File -> Open Projects from File System -> select the project folder -> Finish)
- Configure the required JUnit-required jar files
- Study how to write test methods in Branch **eg_junit_tests** first

    - Switch to Branch eg_junit_tests in Git Bash 
    ```
    git checkout eg_junit_tests 
    ```
    - Study and explore how to write test methods in Eclipse IDE in Branch **eg_junit_tests** (resources:[Video](https://youtu.be/DuAqP8IRcbY) and [System Rules](https://stefanbirkner.github.io/system-rules/))

    - Switch to back to Branch **main** in Git Bash 
  ```
  git checkout main 
  ```
  
4. Write and execute your JUnit test methods in Branch **main** and fix found faults.


5. Commit and push your work with Git Bash
    - Set up user info (if not already configured)
    ```
    git config user.name "your name"
    git config user.email "your email" 
    ```
   - Stage all modified files
    ```
    git add .
    ``` 
   - Commit the changes
    ```
    git commit -m "commit message"
    ``` 
   - push the changes to the project repository in GitHub
    ```
    git push origin main
    ``` 

### Submission
- Check all the deliverables in the [project slides](./project.fall.2024.pdf).
- Put all the deliverables in Branch **main** in a folder "deliverables".
- Commit the changes before the deadline. 

Note: for the code coverage reports, they must be **HTML** files. In addition, two coverage reports must be given: **one for the unit tests** of individual methods and **one for the program-level tests** of the main method.

<hr>

### Configure jar files
There are three jar files required to write JUnit test methods.


1. **Right-click** on the project name in the left pane of the IDE.
2. **Select** "*Build Path*" -> "*Configure Build Path*" to open the Java Build Path window.
3. In the "*Libraries*" tab, **select** "*Classpath*" and then **click** the "*Add External JARs*" button on the right.
4. **Navigate** to the folder that contains the jar files, **select** all of the jar files, and then click the "*Open*" button.
5. Now that all the jar files are added, **click** the "*Apply and Close*" button at the bottom to finish the configuration.



