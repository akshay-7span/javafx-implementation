
# JavaFX Web Crawler Application
- The JavaFX Web Crawler application enables users to crawl websites and extract various types of information such as links, SEO analysis, and HTML body content. This application is built using JavaFX for the user interface and HTMLUnit for web crawling functionalities.

## Features
- URL Input: Users can input the URL of the website they want to crawl.
  Search Links: Extracts all hyperlinks from the provided URL.
- Search SEO Analysis: Analyzes the SEO-related information of the webpage, including title, meta tags, and header tags.
- Search HTML Body: Retrieves the HTML body content of the webpage.
  Progress Indicator: Indicates the progress of the crawling process.
- Data Insertion: Inserts the crawled data into a MySQL database for storage and analysis.
## Dependencies
- JavaFX: JavaFX is used to build the graphical user interface (GUI) for the calculator application.
- MySQL Connector: The MySQL Connector for Java is required for database connectivity and storing calculation history.
- HTMLUnit: A "headless" browser for Java programs that simulates the behavior of a web browser to extract content from web pages.

```
<dependency>
      <groupId>net.sourceforge.htmlunit</groupId>
      <artifactId>htmlunit</artifactId>
      <version>2.70.0</version>
    </dependency>
```
## Installation and Usage
- Clone the Repository: Clone the repository to your local machine using the following command:


```https://github.com/akshay-7span/javafx-demo```

- Set Up Database: Ensure you have a MySQL database set up. Update the database connection parameters in the Calculator.java file:


```
private static final String DB_URL = "jdbc:mysql://localhost:3306/database name";
private static final String DB_USER = "databse-user-name";
private static final String DB_PASSWORD = "database-password";
```
- Run the Application: Open the project in your preferred IDE. Run the WebCrawlerAppUsingHTMLUnit.java file to launch the application.

- Enter URL: Input the URL of the website you want to crawl into the provided TextField.

- Crawl Actions: Click on the respective buttons (Search Links, Search SEO Analysis, Search HTML Body) to initiate the crawling process and extract the desired information.

- View Results: The extracted information will be displayed in the TextArea provided in the application interface.

- Data Storage: The crawled data can be stored in a MySQL database for further analysis or processing.
