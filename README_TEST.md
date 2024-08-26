## Running Tests in SELENIUM_WEBDRIVER_PROJECT 

To run the tests, follow these steps:

1. **Open a terminal** in the root directory of the project.
2. **Run the following command** to execute all tests:
   ```bash
   mvn test
3. **Output example** 
3.1. All tests passes succesfully:
    ```bash
[INFO] Results:
[INFO]
[INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS

3.2. Any test failed:
    ```bash
[ERROR] Failures: 
[ERROR]   SeleniumTestngTest.selectDropdownOptionTest:104 Option 2 should be selected. expected [false] but found [true]
[INFO]
[ERROR] Tests run: 7, Failures: 1, Errors: 0, Skipped: 0
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD FAILURE





