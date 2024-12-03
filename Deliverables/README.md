
# Printtokens Testing Instructions

This README provides instructions for executing the unit tests for the `Printtokens` program. Both the source code (`Printtokens.java`) and the test file (`testPrintTokens.java`) are located in the `src/Package` directory.

---

## Prerequisites

Ensure the following are installed on your system:

1. **Java Development Kit (JDK)**:
   - Version 8 or higher.
   - [Download JDK](https://www.oracle.com/java/technologies/javase-downloads.html).

2. **Visual Studio Code (VS Code)**:
   - [Download VS Code](https://code.visualstudio.com/).

3. **Java Extensions for VS Code**:
   - Install the following extensions:
     - [Java Extension Pack](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack).
     - [Java Test Runner](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-test).

---

## Running the Unit Tests

### 1. Open the Project in VS Code

1. Launch VS Code.
2. Open the project folder by navigating to:
   ```
   File > Open Folder > Select the project folder.
   ```

### 2. Install Required Extensions

1. Open the Extensions view in VS Code by clicking the square icon in the left sidebar or pressing `Ctrl+Shift+X` (`Cmd+Shift+X` on macOS).
2. Search for `Java Test Runner` and click `Install`.
3. Ensure that both the `Java Extension Pack` and `Java Test Runner` extensions are installed and enabled.

### 3. Run the Tests Using the Java Test Runner

1. Navigate to the `src/Package` folder.
2. Open the test file `testPrintTokens.java`.
3. Look for the `Run Test` and `Debug Test` icons next to each test method or at the class level.
4. Click the `Run Test` icon to execute all the tests or individual ones.

### 4. Run the Tests from the Terminal

To run the tests manually from the terminal:

1. Open the terminal in VS Code (`Ctrl+` or `Cmd+\``).
2. Navigate to the `src` folder:
   ```bash
   cd src
   ```
3. Compile the code and test files:
   ```bash
   javac -d ../bin -cp . Package/Printtokens.java Package/testPrintTokens.java
   ```
4. Execute the tests:
   ```bash
   java -cp ../bin org.junit.runner.JUnitCore Package.testPrintTokens
   ```

5. If the tests pass, you’ll see:
   ```
   OK (x tests)
   ```

6. If any test fails, the output will detail the failure, including the test name and the assertion that failed.

## Notes

- **Debugging Tests**:
  - Add breakpoints by clicking the margin next to the lines in the `testPrintTokens.java` file.
  - Right-click the test in the Test Explorer or file and select `Debug Test`.

- **JUnit Version**:
  - This project uses JUnit 4. Ensure the `junit.jar` library is included in the project dependencies.

---

## Troubleshooting

1. **Tests Not Running in Test Runner**:
   - Ensure the `Java Test Runner` extension is installed.
   - Open the Command Palette (`Ctrl+Shift+P` or `Cmd+Shift+P` on macOS) and run:
     ```
     Java: Clean Java Language Server Workspace
     ```

2. **Class Not Found Error**:
   - Ensure the terminal is in the `src` folder and the `bin` directory exists after compilation.

3. **Errors Related to JUnit**:
   - Ensure JUnit is correctly included. You can download the JUnit JAR and include it in the classpath.