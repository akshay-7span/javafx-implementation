package webcrawler.newUI;

import javafx.scene.control.TableView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
                    tableView.getItems().add(new WebCrawlingApp.ImageData(pageUrl, pageName, imageUrl,"",""));
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
    public static void insertSEOData(String url, String title, String metaTags, String headerTags, String contentType) throws SQLException {
        String sql = "INSERT INTO seo_analysis (url, page_name, meta_tags, header_tags, content_type) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/demo", "root", "root")) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, url);
                statement.setString(2, title);
                statement.setString(3, metaTags);
                statement.setString(4, headerTags);
                statement.setString(5, contentType);
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
            String sql = "SELECT * FROM link_crawled_data";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String url = resultSet.getString("url");
                    String link = resultSet.getString("link");
                    String pageName = resultSet.getString("page_name");
                    String status = resultSet.getString("status");
                    links.add(new WebCrawlingApp.LinkDataForExcel(url, link, pageName, status));
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
                    seoDataList.add(new WebCrawlingApp.SeoAnalysisData(url, pageName, metaTags, headerTags, contentType));
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



    /*private static final String MAP_NAME_SEO = "seo_analysis_map";
    private static final String MAP_NAME_LINKS = "link_crawled_data_map";
    private static final String MAP_NAME_IMAGES = "crawled_images_map";

    private static HazelcastInstance hazelcastInstance;

    public static void initHazelcast() {
        hazelcastInstance = Hazelcast.newHazelcastInstance();
    }

    public static HazelcastInstance getHazelcastInstance() {
        if (hazelcastInstance == null) {
            initHazelcast();
        }
        return hazelcastInstance;
    }

    public static IMap<String, WebCrawlingApp.SEOData> getSEODataMap() {
        return hazelcastInstance.getMap(MAP_NAME_SEO);
    }

    public static IMap<String, WebCrawlingApp.LinkData> getLinkDataMap() {
        return hazelcastInstance.getMap(MAP_NAME_LINKS);
    }

    public static IMap<String, WebCrawlingApp.ImageData> getImageDataMap() {
        return hazelcastInstance.getMap(MAP_NAME_IMAGES);
    }

    // Fetch data for TableView1
    static void fetchDataForTableView1(TableView<WebCrawlingApp.SEOData> tableView) {
        IMap<String, WebCrawlingApp.SEOData> seoDataMap = getHazelcastInstance().getMap("seo_data_map");

        // Clear previous data
        tableView.getItems().clear();

        // Populate TableView with data fetched from Hazelcast
        for (Map.Entry<String, WebCrawlingApp.SEOData> entry : seoDataMap.entrySet()) {
            tableView.getItems().add(entry.getValue());
        }
    }

    // Fetch data for TableView2
    static void fetchDataForTableView2(TableView<WebCrawlingApp.SEOData> tableView) {
        IMap<String, WebCrawlingApp.SEOData> seoDataMap = getHazelcastInstance().getMap("seo_data_map");

        // Clear previous data
        tableView.getItems().clear();

        // Populate TableView with filtered data from Hazelcast
        for (Map.Entry<String, SEOData> entry : seoDataMap.entrySet()) {
            SEOData seoData = entry.getValue();
            if (seoData.getMeta() != null && !seoData.getMeta().isEmpty()) {
                tableView.getItems().add(seoData);
            }
        }
    }

    // Fetch data for TableView3
    static void fetchDataForTableView3(TableView<SEOData> tableView) {
        IMap<String, SEOData> seoDataMap = getHazelcastInstance().getMap("seo_data_map");

        // Clear previous data
        tableView.getItems().clear();

        // Populate TableView with data fetched from Hazelcast
        for (Map.Entry<String, SEOData> entry : seoDataMap.entrySet()) {
            SEOData seoData = entry.getValue();
            if (seoData.getH1() != null && !seoData.getH1().isEmpty()) {
                tableView.getItems().add(seoData);
            }
        }
    }

    // Fetch data for Links TableView
    static void fetchDataForLinksTableView(TableView<LinkData> tableView) {
        IMap<String, LinkData> linkDataMap = getHazelcastInstance().getMap("link_data_map");

        // Clear previous data
        tableView.getItems().clear();

        // Populate TableView with data fetched from Hazelcast
        for (Map.Entry<String, LinkData> entry : linkDataMap.entrySet()) {
            tableView.getItems().add(entry.getValue());
        }
    }

    // Fetch data for Links TableView filtered by status
    static void fetchDataForLinksTableViewForStatus(TableView<LinkData> tableView) {
        IMap<String, LinkData> linkDataMap = getHazelcastInstance().getMap("link_data_map");

        // Clear previous data
        tableView.getItems().clear();

        // Populate TableView with filtered data from Hazelcast
        for (Map.Entry<String, LinkData> entry : linkDataMap.entrySet()) {
            LinkData linkData = entry.getValue();
            if (linkData.getStatus() != null && !linkData.getStatus().isEmpty()) {
                tableView.getItems().add(linkData);
            }
        }
    }

    // Fetch data for Links TableView filtered by page name
    static void fetchDataForLinksTableViewForPageName(TableView<LinkData> tableView) {
        IMap<String, LinkData> linkDataMap = getHazelcastInstance().getMap("link_data_map");

        // Clear previous data
        tableView.getItems().clear();

        // Populate TableView with filtered data from Hazelcast
        for (Map.Entry<String, LinkData> entry : linkDataMap.entrySet()) {
            LinkData linkData = entry.getValue();
            if (linkData.getPageName() != null && !linkData.getPageName().isEmpty()) {
                tableView.getItems().add(linkData);
            }
        }
    }

    // Fetch data for Images TableView
    static void fetchDataForImagesTableView(TableView<ImageData> tableView) {
        IMap<String, ImageData> imageDataMap = getHazelcastInstance().getMap("image_data_map");

        // Clear previous data
        tableView.getItems().clear();

        // Populate TableView with data fetched from Hazelcast
        for (Map.Entry<String, ImageData> entry : imageDataMap.entrySet()) {
            tableView.getItems().add(entry.getValue());
        }
    }

    // Fetch data for Image Size TableView
    static void fetchDataForImageSizeTableView(TableView<ImageData> tableView) {
        IMap<String, ImageData> imageDataMap = getHazelcastInstance().getMap("image_data_map");

        // Clear previous data
        tableView.getItems().clear();

        // Populate TableView with data fetched from Hazelcast
        for (Map.Entry<String, ImageData> entry : imageDataMap.entrySet()) {
            tableView.getItems().add(entry.getValue());
        }
    }

    // Fetch data for Meta Text TableView
    static void fetchDataForMetaTextTableView(TableView<ImageData> tableView) {
        IMap<String, ImageData> imageDataMap = getHazelcastInstance().getMap("image_data_map");

        // Clear previous data
        tableView.getItems().clear();

        // Populate TableView with data fetched from Hazelcast
        for (Map.Entry<String, ImageData> entry : imageDataMap.entrySet()) {
            tableView.getItems().add(entry.getValue());
        }
    }

    // Insert Link data into Hazelcast
    public static void insertLinkData(String url, String linkText, String pageName, String status) {
        IMap<String, LinkData> linkDataMap = getHazelcastInstance().getMap("link_data_map");
        String key = generateUniqueKey();
        LinkData linkData = new LinkData(url, linkText, status);
        linkDataMap.put(key, linkData);
    }

    // Insert SEO data into Hazelcast
    public static void insertSEOData(String url, String title, String metaTags, String headerTags, String contentType) {
        IMap<String, SEOData> seoDataMap = getHazelcastInstance().getMap("seo_data_map");
        String key = generateUniqueKey();
        SEOData seoData = new SEOData(url, title, metaTags, headerTags, contentType);
        seoDataMap.put(key, seoData);
    }

    // Insert Image data into Hazelcast
    public static void insertImageData(String pageName, String url, String imageUrl, String altText, String size) {
        IMap<String, ImageData> imageDataMap = getHazelcastInstance().getMap("image_data_map");
        String key = generateUniqueKey();
        ImageData imageData = new ImageData(pageName, url, imageUrl, altText, size);
        imageDataMap.put(key, imageData);
    }

    // Retrieve Link data from Hazelcast
    public static List<LinkDataForExcel> retrieveLinkDataFromDatabase() {
        List<LinkDataForExcel> links = new ArrayList<>();
        IMap<String, LinkData> linkDataMap = getHazelcastInstance().getMap("link_data_map");
        for (Map.Entry<String, LinkData> entry : linkDataMap.entrySet()) {
            links.add(new LinkDataForExcel(entry.getValue().getUrl(), entry.getValue().getUrl(), entry.getValue().getPageName(), entry.getValue().getStatus()));
        }
        return links;
    }

    // Retrieve SEO data from Hazelcast
    public static List<WebCrawlingApp.SeoAnalysisData> retrieveSeoAnalysisDataFromDatabase() {
        List<WebCrawlingApp.SeoAnalysisData> seoDataList = new ArrayList<>();
        IMap<String, WebCrawlingApp.SEOData> seoDataMap = getHazelcastInstance().getMap("seo_data_map");
        for (Map.Entry<String, WebCrawlingApp.SEOData> entry : seoDataMap.entrySet()) {
            WebCrawlingApp.SEOData seoData = entry.getValue();
            seoDataList.add(new WebCrawlingApp.SeoAnalysisData(seoData.getPageUrl(), seoData.getPageName(), seoData.getMeta(), seoData.getH1(), seoData.getContentType()));
        }
        return seoDataList;
    }

    // Retrieve Image data from Hazelcast
    public static List<CrawledImageData> retrieveCrawledImageDataFromDatabase() {
        List<CrawledImageData> imageDataList = new ArrayList<>();
        IMap<String, ImageData> imageDataMap = getHazelcastInstance().getMap("image_data_map");
        for (Map.Entry<String, ImageData> entry : imageDataMap.entrySet()) {
            ImageData imageData = entry.getValue();
            imageDataList.add(new CrawledImageData(imageData.getPageUrl(), imageData.getPageName(), imageData.getImageUrl(), imageData.getAltText(), imageData.getImageSize()));
        }
        return imageDataList;
    }


    private static String generateUniqueKey() {
        // Generate a UUID (Universally Unique Identifier) as the key
        return UUID.randomUUID().toString();
    }*/
}

