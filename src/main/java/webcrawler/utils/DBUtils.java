package webcrawler.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import webcrawler.tables.DataTables.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DBUtils {




    private static final String DB_URL = "jdbc:mysql://localhost:3306/demo";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";
    private static HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(DB_URL);
        config.setUsername(DB_USER);
        config.setPassword(DB_PASSWORD);
        dataSource = new HikariDataSource(config);
    }


/*    private static final String DB_URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
    private static final String DB_USER = "sa"; // Default H2 user
    private static final String DB_PASSWORD = ""; // Default H2 password*/

    public static void createTablesIfNotExist() {
        try {
            // Connect to the H2 in-memory database
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Create tables if they don't exist
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS crawled_data ("
                        + "id INT AUTO_INCREMENT PRIMARY KEY,"
                        + "page_name VARCHAR(255),"
                        + "page_url VARCHAR(255),"
                        + "link_url VARCHAR(255),"
                        + "link_type VARCHAR(50),"
                        + "status INT"
                        + ")");

                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS crawled_images ("
                        + "id INT AUTO_INCREMENT PRIMARY KEY,"
                        + "url VARCHAR(255) NOT NULL,"
                        + "page_name VARCHAR(255) NOT NULL,"
                        + "image_url VARCHAR(255) NOT NULL,"
                        + "alt_text VARCHAR(255),"
                        + "image_size VARCHAR(20) DEFAULT '0 KB'"
                        + ")");

                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS seo_analysis ("
                        + "id INT AUTO_INCREMENT PRIMARY KEY,"
                        + "url VARCHAR(255) NOT NULL,"
                        + "page_name VARCHAR(255),"
                        + "meta_tags TEXT,"
                        + "header_tags TEXT,"
                        + "content_type VARCHAR(255),"
                        + "load_time VARCHAR(10),"
                        + "repeated_words TEXT,"
                        + "indexed BOOLEAN"
                        + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertLinkData(String url, String linkText, String pageName, String status) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/demo", "root", "root")) {
            String sql = "INSERT INTO link_crawled_data (url, link, page_name, status) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, url);
                statement.setString(2, linkText);
                statement.setString(3, pageName);
                statement.setString(4, status);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertSEOData(String url, String title, String metaTags, String headerTags, String contentType, String pageLoadTime, String repeatedWords, boolean isIndexed, boolean blockedForCrawling) {
        String sql = "INSERT INTO seo_analysis (url, page_name, meta_tags, header_tags, content_type, load_time, repeated_words, indexed,bolcked_for_crawling) VALUES (?, ?, ?, ?, ?, ?, ?, ?,?)";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, url);
                statement.setString(2, title);
                statement.setString(3, metaTags);
                statement.setString(4, headerTags);
                statement.setString(5, contentType);
                statement.setString(6, pageLoadTime);
                statement.setString(7, repeatedWords);
                statement.setBoolean(8, isIndexed); // Store indexed status
                statement.setBoolean(9, blockedForCrawling);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertImageData(String pageName, String url, String imageUrl, String altText, String size) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO crawled_images (page_name, url, image_url, alt_text, image_size) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, pageName);
                statement.setString(2, url);
                statement.setString(3, imageUrl);
                statement.setString(4, altText);
                statement.setString(5, size);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<LinkDataForExcel> retrieveLinkDataFromDatabase() throws SQLException {
        List<LinkDataForExcel> links = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM crawled_data";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String pageName = resultSet.getString("page_name");
                    String pageUrl = resultSet.getString("page_url");
                    String linkUrl = resultSet.getString("link_url");
                    String linkType = resultSet.getString("link_type");
                    int status = resultSet.getInt("status");
                    links.add(new LinkDataForExcel(pageName, pageUrl, linkUrl, linkType, status));
                }
            }
        }
        return links;
    }


    public static List<SeoAnalysisData> retrieveSeoAnalysisDataFromDatabase() throws SQLException {
        List<SeoAnalysisData> seoDataList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM seo_analysis";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String url = resultSet.getString("url");
                    String pageName = resultSet.getString("page_name");
                    String metaTags = resultSet.getString("meta_tags");
                    String headerTags = resultSet.getString("header_tags");
                    String contentType = resultSet.getString("content_type");
                    String loadTime = resultSet.getString("load_time");
                    String repeatedWords = resultSet.getString("repeated_words");
                    seoDataList.add(new SeoAnalysisData(url, pageName, metaTags, headerTags, contentType, loadTime, repeatedWords));
                }
            }
        }
        return seoDataList;
    }


    public static List<CrawledImageData> retrieveCrawledImageDataFromDatabase() throws SQLException {
        List<CrawledImageData> imageDataList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM crawled_images";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String url = resultSet.getString("url");
                    String pageName = resultSet.getString("page_name");
                    String imageUrl = resultSet.getString("image_url");
                    String altText = resultSet.getString("alt_text");
                    String imageSize = resultSet.getString("image_size");
                    imageDataList.add(new CrawledImageData(url, pageName, imageUrl, altText, imageSize));
                }
            }
        }
        return imageDataList;
    }

    public static List<SEODataForCrawling> retrieveCrawledSeoDataFromDatabase() throws SQLException {
        List<SEODataForCrawling> seoDataForCrawlings = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT page_name, content_type,url,load_time  FROM seo_analysis";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        String pageName = resultSet.getString("page_name");
                        String contentType = resultSet.getString("content_type");
                        String pageUrl = resultSet.getString("url");
                        String loadTime = resultSet.getString("load_time") + "msc";

                        SEODataForCrawling seoData = new SEODataForCrawling(pageName, contentType, pageUrl, loadTime);
                        seoDataForCrawlings.add(seoData);
                    }
                }
            }
        }
        return seoDataForCrawlings;

    }


    // Method to fetch meta data from the database
    public static List<String> fetchMetaDataFromDatabase(String selectedUrl) throws SQLException {
        List<String> metaDataList = new ArrayList<>();
        String sql = "SELECT meta_tags FROM seo_analysis WHERE url = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, selectedUrl);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String metaData = resultSet.getString("meta_tags");
                    // Split the meta_tags by newline if multiple tags are stored in the database
                    String[] metaDataArray = metaData.split("\\r?\\n");
                    for (String tag : metaDataArray) {
                        // Trim any leading or trailing spaces and remove "meta:" prefix
                        String cleanedTag = tag.trim().replace("meta:", "");
                        metaDataList.add(cleanedTag);
                    }
                }
            }
        }
        return metaDataList;
    }


    public static List<String> fetchHeaderTagsFromDatabase(String selectedUrl) throws SQLException {
        List<String> headerTagsList = new ArrayList<>();
        String sql = "SELECT header_tags FROM seo_analysis WHERE url = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, selectedUrl);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String headerTags = resultSet.getString("header_tags");
                    // Split the header_tags by comma if multiple tags are stored in the database
                    String[] headerTagsArray = headerTags.split(",");
                    for (String tag : headerTagsArray) {
                        // Trim any leading or trailing spaces and remove "h1:" prefix
                        String cleanedTag = tag.trim().replace("h1:", "");
                        headerTagsList.add(cleanedTag);
                    }
                }
            }
        }
        return headerTagsList;
    }

    // Method to fetch repeatedWords data from the database based on the selected URL
    public static List<String> fetchRepeatedWordsFromDatabase(String selectedUrl) throws SQLException {
        List<String> repeatedWordsList = new ArrayList<>();
        String sql = "SELECT repeated_words FROM seo_analysis WHERE url = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, selectedUrl);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String repeatedWords = resultSet.getString("repeated_words");
                    repeatedWordsList.add(repeatedWords);
                }
            }
        }
        return repeatedWordsList;
    }

    public static List<ImageData> fetchImageDataFromDatabase(String url) throws SQLException {
        List<ImageData> imageDataList = new ArrayList<>();
        String sql = "SELECT page_name, image_url, image_size, alt_text FROM crawled_images WHERE url = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, url);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String pageName = resultSet.getString("page_name");
                    String imageUrl = resultSet.getString("image_url");
                    String imageSize = resultSet.getString("image_size");
                    String altText = resultSet.getString("alt_text");
                    ImageData imageData = new ImageData(url, pageName, imageUrl, imageSize, altText);
                    imageDataList.add(imageData);
                }
            }
        }
        return imageDataList;
    }

    public static void insertCrawledData(String pageName, String pageUrl, String linkUrl, String linkType, int status) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO crawled_data (page_name, page_url, link_url, link_type, status) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, pageName);
                statement.setString(2, pageUrl);
                statement.setString(3, linkUrl);
                statement.setString(4, linkType);
                statement.setInt(5, status);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<PageData> getDistinctPageData() {
        List<PageData> pageDataList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT DISTINCT page_name FROM crawled_data")) {

            while (resultSet.next()) {
                String pageName = resultSet.getString("page_name");
                PageData pageData = new PageData(pageName);
                pageDataList.add(pageData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pageDataList;
    }

    public static List<PageData> fetchDataForPageName(String pageName) throws SQLException {
        List<PageData> pageDataList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM crawled_data WHERE page_name = ?")) {

            statement.setString(1, pageName);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String fetchedPageName = resultSet.getString("page_name");
                    String linkUrl = resultSet.getString("link_url");
                    String linkType = resultSet.getString("link_type");
                    int status = resultSet.getInt("status");
                    PageData pageData = new PageData(fetchedPageName, status, linkUrl, linkType);
                    pageDataList.add(pageData);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throw the exception to handle it elsewhere if needed
        }
        return pageDataList;
    }
    public static void updateProgressBars(ProgressBar progressBar, Label countLabel, String statusPrefix) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT COUNT(*) FROM crawled_data WHERE status LIKE ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, statusPrefix + "%");
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int count = rs.getInt(1);
                        int totalCount = getTotalCount();
                        double progress = (double) count / totalCount;
                        progressBar.setProgress(progress);
                        countLabel.setText(Integer.toString(count)); // Set count label text
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getTotalCount() throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT COUNT(*) FROM crawled_data";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        }
        return 0;
    }

    public static void updateBlockedForCrawlingProgressBars(ProgressBar progressBar, Label countLabel) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT COUNT(*) FROM seo_analysis WHERE bolcked_for_crawling = 1";
            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    int totalCount = getTotalBlockedForCrawlingCount();
                    double progress = (double) count / totalCount;
                    progressBar.setProgress(progress);
                    countLabel.setText(Integer.toString(count)); // Set count label text
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getTotalBlockedForCrawlingCount() throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT COUNT(*) FROM seo_analysis WHERE bolcked_for_crawling = 1";
            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }
    public static ObservableList<PieChart.Data> fetchDataFromDatabaseForPieChart() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Query to count indexed and non-indexed URLs
            String query = "SELECT indexed, COUNT(*) AS count FROM seo_analysis GROUP BY indexed";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String indexed = resultSet.getBoolean("indexed") ? "Indexed Pages" : "Non-Indexed Pages";
                int count = resultSet.getInt("count");
                pieChartData.add(new PieChart.Data(indexed + ": " + count, count)); // Custom label with count
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pieChartData;
    }


}