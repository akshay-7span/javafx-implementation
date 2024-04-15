package webcrawler.newUI;

import javafx.scene.control.TableView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import webcrawler.newUI.WebCrawlingApp.*;

public class DatabaseCall {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/demo";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    // Inside fetchDataForTableView1 method
    static void fetchDataForTableView1(TableView<WebCrawlingApp.SEOData> tableView) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT page_name, content_type, url FROM seo_analysis";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                // Clear previous data
                tableView.getItems().clear();

                // Populate TableView with data fetched from the database
                while (resultSet.next()) {
                    String pageName = resultSet.getString("page_name");
                    String contentType = resultSet.getString("content_type");
                    String pageUrl = resultSet.getString("url");

                    // Add data to TableView
                    tableView.getItems().add(new WebCrawlingApp.SEOData(pageName, contentType, pageUrl, "", ""));
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Print stack trace for debugging
            // Handle exceptions
        }
    }

    static void fetchDataForTableView2(TableView<WebCrawlingApp.SEOData> tableView) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT page_name, content_type, url, meta_tags FROM seo_analysis WHERE meta_tags IS NOT NULL";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                // Clear previous data
                tableView.getItems().clear();

                // Populate TableView with data fetched from the database
                while (resultSet.next()) {
                    String pageName = resultSet.getString("page_name");
                    String contentType = resultSet.getString("content_type");
                    String pageUrl = resultSet.getString("url");
                    String meta = resultSet.getString("meta_tags");

                    // Remove "meta:" prefix and colon from meta tags
                    meta = meta.replaceAll("meta:\\s*", "").replaceAll(":", "");

                    tableView.getItems().add(new WebCrawlingApp.SEOData(pageName, contentType, pageUrl, meta, ""));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions
        }
    }


    static void fetchDataForTableView3(TableView<WebCrawlingApp.SEOData> tableView) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT page_name, url, header_tags FROM seo_analysis";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                // Clear previous data
                tableView.getItems().clear();

                // Populate TableView with data fetched from the database
                while (resultSet.next()) {
                    String pageName = resultSet.getString("page_name");
                    String pageUrl = resultSet.getString("url");
                    String H1 = resultSet.getString("header_tags");

                    H1 = H1.replaceAll("h1:\\s*", "").replaceAll(":", "");

                    tableView.getItems().add(new WebCrawlingApp.SEOData(pageName, "", pageUrl, "", H1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions
        }
    }

    static void fetchDataForLinksTableView(TableView<WebCrawlingApp.LinkData> tableView) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT link, url, status FROM link_crawled_data";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                // Clear previous data
                tableView.getItems().clear();

                // Populate TableView with data fetched from the database
                while (resultSet.next()) {
                    String pageName = resultSet.getString("link");
                    String url = resultSet.getString("url");
                    String status = resultSet.getString("status");

                    // Add data to TableView
                    tableView.getItems().add(new WebCrawlingApp.LinkData(pageName, url, status));
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Print stack trace for debugging
            // Handle exceptions
        }
    }

    static void fetchDataForLinksTableViewForStatus(TableView<WebCrawlingApp.LinkData> tableViewLinks) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT link, status FROM link_crawled_data";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                // Clear previous data
                tableViewLinks.getItems().clear();

                // Populate TableView with data fetched from the database
                while (resultSet.next()) {
                    String pageName = resultSet.getString("link");
                    String status = resultSet.getString("status");

                    // Add data to TableView
                    tableViewLinks.getItems().add(new WebCrawlingApp.LinkData(pageName, "", status));
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Print stack trace for debugging
            // Handle exceptions
        }
    }

    static void fetchDataForLinksTableViewForPageName(TableView<WebCrawlingApp.LinkData> tableViewLinks) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT link, url FROM link_crawled_data";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                // Clear previous data
                tableViewLinks.getItems().clear();

                // Populate TableView with data fetched from the database
                while (resultSet.next()) {
                    String pageName = resultSet.getString("link");
                    String url = resultSet.getString("url");

                    // Add data to TableView
                    tableViewLinks.getItems().add(new WebCrawlingApp.LinkData(pageName, url, ""));
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Print stack trace for debugging
            // Handle exceptions
        }
    }

    static void fetchDataForImagesTableView(TableView<WebCrawlingApp.ImageData> tableView) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT url, page_name, image_url FROM crawled_images";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                // Clear previous data
                tableView.getItems().clear();

                // Populate TableView with data fetched from the database
                while (resultSet.next()) {
                    String pageUrl = resultSet.getString("url");
                    String pageName = resultSet.getString("page_name");
                    String imageUrl = resultSet.getString("image_url");

                    // Add data to TableView
                    tableView.getItems().add(new WebCrawlingApp.ImageData(pageUrl, pageName, imageUrl, "", ""));
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Print stack trace for debugging
            // Handle exceptions
        }
    }

    static void fetchDataForImageSizeTableView(TableView<WebCrawlingApp.ImageData> tableView) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT url, page_name, image_url, image_size FROM crawled_images";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                // Clear previous data
                tableView.getItems().clear();

                // Populate TableView with data fetched from the database
                while (resultSet.next()) {
                    String pageUrl = resultSet.getString("url");
                    String pageName = resultSet.getString("page_name");
                    String imageUrl = resultSet.getString("image_url");
                    String imageSize = resultSet.getString("image_size");

                    tableView.getItems().add(new WebCrawlingApp.ImageData(pageUrl, pageName, imageUrl, imageSize, ""));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions
        }
    }

    static void fetchDataForMetaTextTableView(TableView<WebCrawlingApp.ImageData> tableView) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT url, page_name, image_url, alt_text FROM crawled_images";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                // Clear previous data
                tableView.getItems().clear();

                // Populate TableView with data fetched from the database
                while (resultSet.next()) {
                    String pageUrl = resultSet.getString("url");
                    String pageName = resultSet.getString("page_name");
                    String imageUrl = resultSet.getString("image_url");
                    String altText = resultSet.getString("alt_text");

                    tableView.getItems().add(new WebCrawlingApp.ImageData(pageUrl, pageName, imageUrl, "", altText));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions
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

    public static void insertSEOData(String url, String title, String metaTags, String headerTags, String contentType, String pageLoadTime, String repeatedWords) throws SQLException {
        String sql = "INSERT INTO seo_analysis (url, page_name, meta_tags, header_tags, content_type,load_time, repeated_words) VALUES (?, ?, ?, ?, ?,?,?)";
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/demo", "root", "root")) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, url);
                statement.setString(2, title);
                statement.setString(3, metaTags);
                statement.setString(4, headerTags);
                statement.setString(5, contentType);
                statement.setString(6, pageLoadTime);
                statement.setString(7, repeatedWords);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void insertImageData(String pageName, String url, String imageUrl, String altText, String size) throws SQLException {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/demo", "root", "root")) {
            String sql = "INSERT INTO crawled_images (page_name, url, image_url, alt_text, image_size) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, pageName);
                statement.setString(2, url);
                statement.setString(3, imageUrl);
                statement.setString(4, altText);
                statement.setString(5, size);
                statement.executeUpdate();
            }
        }
    }

    public static List<WebCrawlingApp.LinkDataForExcel> retrieveLinkDataFromDatabase() throws SQLException {
        List<WebCrawlingApp.LinkDataForExcel> links = new ArrayList<>();
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
                    links.add(new WebCrawlingApp.LinkDataForExcel(pageName, pageUrl, linkUrl, linkType, status));
                }
            }
        }
        return links;
    }


    public static List<WebCrawlingApp.SeoAnalysisData> retrieveSeoAnalysisDataFromDatabase() throws SQLException {
        List<WebCrawlingApp.SeoAnalysisData> seoDataList = new ArrayList<>();
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
                    seoDataList.add(new WebCrawlingApp.SeoAnalysisData(url, pageName, metaTags, headerTags, contentType, loadTime, repeatedWords));
                }
            }
        }
        return seoDataList;
    }


    public static List<WebCrawlingApp.CrawledImageData> retrieveCrawledImageDataFromDatabase() throws SQLException {
        List<WebCrawlingApp.CrawledImageData> imageDataList = new ArrayList<>();
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
                    imageDataList.add(new WebCrawlingApp.CrawledImageData(url, pageName, imageUrl, altText, imageSize));
                }
            }
        }
        return imageDataList;
    }

    public static List<WebCrawlingApp.SEODataForCrawling> retrieveCrawledSeoDataFromDatabase() throws SQLException {
        List<WebCrawlingApp.SEODataForCrawling> seoDataForCrawlings = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT page_name, content_type, url FROM seo_analysis";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        String pageName = resultSet.getString("page_name");
                        String contentType = resultSet.getString("content_type");
                        String pageUrl = resultSet.getString("url");

                        WebCrawlingApp.SEODataForCrawling seoData = new WebCrawlingApp.SEODataForCrawling(pageName, contentType, pageUrl);
                        seoDataForCrawlings.add(seoData);
                    }
                }
            }
        }
        return seoDataForCrawlings;

    }

    public static List<WebCrawlingApp.SEODataForCrawling> fetchDataFromDatabase(String selectedUrl) throws SQLException {
        List<WebCrawlingApp.SEODataForCrawling> seoDataForCrawlings = new ArrayList<>();
        String sql = "SELECT page_name, url, content_type FROM seo_analysis WHERE url = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            // Set the URL parameter value
            statement.setString(1, selectedUrl);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String pageName = resultSet.getString("page_name");
                    String contentType = resultSet.getString("content_type");
                    String pageUrl = resultSet.getString("url");

                    WebCrawlingApp.SEODataForCrawling seoData = new WebCrawlingApp.SEODataForCrawling(pageName, contentType, pageUrl);
                    seoDataForCrawlings.add(seoData);
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
                    metaDataList.add(metaData);
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
                    headerTagsList.addAll(Arrays.asList(headerTagsArray));
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

    // Method to fetch loadTime data from the database based on the selected URL
    public static List<String> fetchLoadTimeFromDatabase(String selectedUrl) throws SQLException {
        List<String> loadTimeList = new ArrayList<>();
        String sql = "SELECT load_time FROM seo_analysis WHERE url = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, selectedUrl);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String loadTime = resultSet.getString("load_time");
                    // Append "msc" to the load time value
                    loadTime += "msc";
                    loadTimeList.add(loadTime);
                }
            }
        }
        return loadTimeList;
    }

    public static List<WebCrawlingApp.ImageData> fetchImageDataFromDatabase(String url) throws SQLException {
        List<WebCrawlingApp.ImageData> imageDataList = new ArrayList<>();
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
}