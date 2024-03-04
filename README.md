# JavaFX Screen Recorder Application
- The JavaFX Screen Recorder application allows users to record their screen activities with ease. This application utilizes JavaFX for the graphical user interface and JavaCV along with FFmpeg for screen recording capabilities..

## Features
- Start Recording: Initiate the screen recording process with a single click.
- Stop Recording: Terminate the screen recording session at any time.
- Recording Timer: Displays the duration of the ongoing screen recording.
- Automatic File Saving: Recorded videos are automatically saved to the user's Downloads folder.
- Adjustable Frame Rate and Video Quality: Users can customize frame rate and video quality settings for optimal recording results.
## Dependencies
- JavaFX: JavaFX is used to build the graphical user interface (GUI) for the calculator application.
- MySQL Connector: The MySQL Connector for Java is required for database connectivity and storing calculation history.
- FFmpeg: FFmpeg is a multimedia framework used to record, convert, and stream audio and video.

```
<dependency>
<groupId>com.github.kokorin.jaffree</groupId>
<artifactId>jaffree</artifactId>
<version>0.11.0</version>
</dependency>
```
- JavaCV Platform: Java interface to FFmpeg and OpenCV, providing multimedia functionality to Java applications.
```
  <dependency>
  <groupId>org.bytedeco</groupId>
  <artifactId>javacv-platform</artifactId>
  <version>1.5.5</version>
  </dependency>
```
## Installation and Usage
- Clone the Repository: Clone the repository to your local machine using the following command:


```https://github.com/akshay-7span/javafx-demo```

- Set Up Database: Ensure you have a MySQL database set up. Update the database connection parameters in the Calculator.java file:


```
private static final String DB_URL = "jdbc:mysql://localhost:3306/database name";
private static final String DB_USER = "databse-user-name";
private static final String DB_PASSWORD = "data-base-password";
```
- Set Up Dependencies: Ensure you have JavaFX, MySQL Connector Java, FFmpeg, and JavaCV Platform dependencies included in your project's build configuration.

- Run the Application: Open the project in IntelliJ IDEA or your preferred IDE. Run the ScreenRecorder.java file to launch the application.

- Start Recording: Click on the "Start Recording" button to begin the screen recording process.

- Stop Recording: To stop the recording, click on the "Stop Recording" button. The recorded video will be saved automatically to your Downloads folder.

- View Recording Duration: The duration of the ongoing recording is displayed in real-time on the application interface.

- Adjust Settings: Customize frame rate and video quality settings as per your preferences by modifying the relevant parameters in the code.

- Close Application: Ensure recording is stopped and the application is closed properly to avoid any potential issues.

