package webcrawler.utils;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import webcrawler.tables.DataTables.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import static webcrawler.utils.DBUtils.*;

public class CrawlerUtils {

    public static CountDownLatch latch = new CountDownLatch(3);
    public static String extractDomain(String url) {
        try {
            URL u = new URL(url);
            return u.getHost();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static boolean isSameDomain(String url, String domain) {
        return url.contains(domain);
    }

    public static String getAbsoluteUrl(String baseUrl, String relativeUrl) {
        try {
            URL base = new URL(baseUrl);
            URL absolute = new URL(base, relativeUrl);
            return absolute.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static int getLinkStatus(String link) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(link).openConnection();
            connection.setRequestMethod("HEAD");
            connection.connect();
            int status = connection.getResponseCode();
            connection.disconnect();
            return status;
        } catch (IOException e) {
            e.printStackTrace();
            return 0; // Return an invalid status code for connection errors
        }
    }

    public static synchronized void decrementActiveTasks(ProgressBar progressBar) {
        latch.countDown(); // Decrease the latch count
        if (latch.getCount() == 0) {
            // Update progress bar visibility on the JavaFX application thread
            Platform.runLater(() -> progressBar.setVisible(false));
        }
    }


    public static boolean hasContentToAnalyze(HtmlPage page) {
        return !page.getTitleText().isEmpty() ||
                !page.getHead().getElementsByTagName("meta").isEmpty() ||
                !page.getByXPath("//h1 ").isEmpty();
    }

    private final Set<String> visitedUrls = new HashSet<>();


    public static String extractPageName(String pageUrl) {
        try {
            URL urlObj = new URL(pageUrl);
            String path = urlObj.getPath();
            // Remove any leading and trailing slashes
            path = path.replaceAll("^/|/$", "");
            // Extract the last segment of the path as the page name
            String[] segments = path.split("/");
            String pageName = segments[segments.length - 1];
            // Remove file extension, if any
            int dotIndex = pageName.lastIndexOf('.');
            if (dotIndex != -1) {
                pageName = pageName.substring(0, dotIndex);
            }
            return pageName;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "Unknown"; // Return a default page name if extraction fails
        }
    }

    public static List<String> extractLinks(HtmlPage page) {
        List<String> links = new ArrayList<>();
        DomNodeList<DomElement> anchorTags = page.getElementsByTagName("a");
        String baseUrl = page.getBaseURI(); // Get the base URL of the page
        for (DomElement anchorTag : anchorTags) {
            String link = anchorTag.getAttribute("href");
            if (!link.isEmpty()) {
                // Check if the link starts with "http://" or "https://"
                if (!link.startsWith("http://") && !link.startsWith("https://")) {
                    // If not, prepend the base URL
                    link = baseUrl + link;
                }
                // Filter out telephone links
                if (!isTelephoneLink(link)) {
                    links.add(link);
                }
            }
        }
        return links;
    }

    public static int getImageSize(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("HEAD");
        return conn.getContentLength();
    }

    public static boolean isTelephoneLink(String link) {
        return link.startsWith("tel:");
    }

    public static void exportToExcel(Button exportButton) throws SQLException {
        System.setProperty("org.apache.poi.util.POILogger", "org.apache.poi.util.SimpleLogger");

        Workbook workbook = new XSSFWorkbook();

        // Export Link Data
        Sheet linkSheet = workbook.createSheet("Link Data");
        int linkStartRow = createTitleRow(linkSheet, "Link Data", LinkDataForExcel.class);
        List<LinkDataForExcel> linkDataList = retrieveLinkDataFromDatabase();
        populateData(linkSheet, linkDataList, linkStartRow);

        // Export SEO Analysis
        Sheet seoSheet = workbook.createSheet("SEO Analysis");
        int seoStartRow = createTitleRow(seoSheet, "SEO Analysis", SeoAnalysisData.class);
        List<SeoAnalysisData> seoDataList = retrieveSeoAnalysisDataFromDatabase();
        populateData(seoSheet, seoDataList, seoStartRow);

        // Export Crawled Images
        Sheet imageSheet = workbook.createSheet("Crawled Images");
        int imageStartRow = createTitleRow(imageSheet, "Crawled Images", CrawledImageData.class);
        List<CrawledImageData> imageDataList = retrieveCrawledImageDataFromDatabase();
        populateData(imageSheet, imageDataList, imageStartRow);


        // Autosize columns
        for (Sheet sheet : workbook) {
            for (int i = 0; i < 6; i++) {
                sheet.autoSizeColumn(i);
            }
        }

        // Show save file dialog
        File selectedFile = showSaveFileDialog(exportButton);
        if (selectedFile != null) {
            String filePath = selectedFile.getAbsolutePath();

            // Write the workbook content to the Excel file
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception
            } finally {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Add this method to your class
    private static File showSaveFileDialog(Button exportButton) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Excel File");
        fileChooser.setInitialFileName("web_crawling_details.xlsx");

        // Set the default directory
        String userHomeFolder = System.getProperty("user.home");
        File defaultDirectory = new File(userHomeFolder + "/Downloads");
        fileChooser.setInitialDirectory(defaultDirectory);

        // Set the file extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel files (*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        return fileChooser.showSaveDialog(exportButton.getScene().getWindow());
    }

    private static int createTitleRow(Sheet sheet, String title, Class<?> dataType) {
        CellStyle titleStyle = sheet.getWorkbook().createCellStyle();
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue(title);
        titleCell.setCellStyle(titleStyle);

        String[] headers;
        if (dataType == LinkDataForExcel.class) {
            // If the data type is LinkDataForExcel, include the additional "Link Type" column
            headers = new String[]{"Page Name", "Page Url", "Link", "Link Type", "Status"};
        } else if (dataType == SeoAnalysisData.class) {
            headers = new String[]{"URL", "Page Name", "Meta Tags", "Header Tags", "Content Type", "Load Time", "Repeated Words"};
        } else if (dataType == CrawledImageData.class) {
            headers = new String[]{"URL", "Page Name", "Image URL", "Alt Text", "Image Size"};
        } else {
            headers = new String[]{};
        }

        Row headerRow = sheet.createRow(1); // Row for headers
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        // Merge cells for title
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headers.length - 1));

        return 2; // Start from row 2 for data
    }

    private static void populateData(Sheet sheet, List<? extends Object> dataList, int startRow) {
        int currentRow = startRow;
        for (Object data : dataList) {
            Row row = sheet.createRow(currentRow++);
            // Populate data based on the type
            if (data instanceof LinkDataForExcel linkData) {
                row.createCell(0).setCellValue(linkData.getPageName());
                row.createCell(1).setCellValue(linkData.getPageUrl());
                row.createCell(2).setCellValue(linkData.getLinkUrl());
                row.createCell(3).setCellValue(linkData.getLinkType());
                row.createCell(4).setCellValue(linkData.getStatus());
            } else if (data instanceof SeoAnalysisData seoData) {
                row.createCell(0).setCellValue(seoData.getUrl());
                row.createCell(1).setCellValue(seoData.getPageName());
                row.createCell(2).setCellValue(seoData.getMetaTags());
                row.createCell(3).setCellValue(seoData.getHeaderTags());
                row.createCell(4).setCellValue(seoData.getContentType());
                row.createCell(5).setCellValue(seoData.getLoadTime());
                row.createCell(6).setCellValue(seoData.getRepeatedWords());
            } else if (data instanceof CrawledImageData imageData) {
                row.createCell(0).setCellValue(imageData.getUrl());
                row.createCell(1).setCellValue(imageData.getPageName());
                row.createCell(2).setCellValue(imageData.getImageUrl());
                row.createCell(3).setCellValue(imageData.getAltText());
                row.createCell(4).setCellValue(imageData.getImageSize());
            }
        }
    }


    public static WebClient createWebClient() {
        WebClient webClient = new WebClient(com.gargoylesoftware.htmlunit.BrowserVersion.CHROME);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        return webClient;
    }

    private VBox createBorderedVBox(double width, double height) {
        VBox vbox = new VBox();
        vbox.setPrefSize(width, height);
        vbox.getStyleClass().add("vbox");
        return vbox;
    }

    public static void disableButtons(Button... buttons) {
        for (Button button : buttons) {
            button.setDisable(true);
        }
    }

    public static String getPageLoadTime(String url) {
        // Start time
        long startTime = System.currentTimeMillis();

        // Fetch the webpage content using Jsoup
        try {
            Jsoup.connect(url).get();

            // Stop time
            long endTime = System.currentTimeMillis();

            // Calculate page load time
            return String.valueOf(endTime - startTime);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "unable to fetch page load time"; // Return -1 if unable to fetch page load time
    }

    public static String countRepeatedWords(String url) {
        Map<String, Integer> wordCount = new HashMap<>();
        try {
            Document doc = Jsoup.connect(url).get();
            String text = doc.body().text(); // Extract text from the body of the HTML document

            // Split the text into words using whitespace as delimiter
            String[] words = text.split("\\s+");

            // Count occurrences of each word
            for (String word : words) {
                wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
            }

            // Filter out non-repeated words
            Map<String, Integer> repeatedWords = new HashMap<>();
            for (Map.Entry<String, Integer> entry : wordCount.entrySet()) {
                if (entry.getValue() > 1) {
                    repeatedWords.put(entry.getKey(), entry.getValue());
                }
            }
            return mapToString(repeatedWords);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method to convert map to string
    private static String mapToString(Map<String, Integer> map) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            stringBuilder.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        return stringBuilder.toString();
    }

    public static VBox displayGraph(String pageUrl) {
        // Fetch webpage content and analyze it
        WebPageData data = analyzeWebPage(pageUrl);

        // Create PieChart
        assert data != null;
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("HTML", data.getHtmlPercentage()),
                new PieChart.Data("CSS", data.getCssPercentage()),
                new PieChart.Data("JavaScript", data.getJsPercentage()));

        // Set names for the slices with percentages
        for (PieChart.Data slice : pieChartData) {
            String nameWithPercentage = slice.getName() + " (" + slice.getPieValue() + "%)";
            slice.setName(nameWithPercentage);

            // Set color for each slice based on content type
            switch (slice.getName()) {
                case "HTML" -> slice.getNode().setStyle("-fx-pie-color: #FFA726;"); // Orange
                case "CSS" -> slice.getNode().setStyle("-fx-pie-color: #4CAF50;"); // Green
                case "JavaScript" -> slice.getNode().setStyle("-fx-pie-color: #03A9F4;"); // Blue
            }
        }

        final PieChart chart = new PieChart(pieChartData);
        chart.setTitle("Content Breakdown");

        // Hide slice percentages
        chart.setLabelsVisible(false);

        // Create VBox and add PieChart
        VBox graphBox = new VBox(chart);
        graphBox.getStyleClass().add("graph-pane"); // Add style class

        VBox.setVgrow(chart, Priority.ALWAYS);

        return graphBox;
    }

    private static WebPageData analyzeWebPage(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            Elements cssElements = doc.select("link[rel=stylesheet]");
            Elements jsElements = doc.select("script[src]");
            Elements htmlElements = doc.select("html *");

            int totalElements = cssElements.size() + jsElements.size() + htmlElements.size();
            int cssCount = cssElements.size();
            int jsCount = jsElements.size();

            // Calculate percentages
            int cssPercentage = (int) Math.ceil((cssCount * 100.0) / totalElements);
            int jsPercentage = (int) Math.ceil((jsCount * 100.0) / totalElements);
            int htmlPercentage = 100 - cssPercentage - jsPercentage;

            // Adjust CSS percentage to ensure total is 100%
            cssPercentage = 100 - jsPercentage - htmlPercentage;

            return new WebPageData(url, cssPercentage, jsPercentage, htmlPercentage);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    public static boolean isURLIndexed(String url) {
        try (WebClient webClient = new WebClient(BrowserVersion.CHROME)) {
            webClient.getOptions().setJavaScriptEnabled(false);
            HtmlPage page = webClient.getPage("https://www.google.com/search?q=site:" + url);
            return !page.asXml().contains("did not match any documents");
        } catch (Exception ex) {
            System.out.println("An error occurred while checking indexed pages.");
            return false;
        }
    }
    // Method to check if crawling is blocked for a given URL
    public static boolean isCrawlingBlocked(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        int responseCode = connection.getResponseCode();
        return responseCode == HttpURLConnection.HTTP_FORBIDDEN; // Check if response is Forbidden (403)
    }
    public static PieChart createPieChart(ObservableList<PieChart.Data> pieChartData) {
        return new PieChart(pieChartData);
    }

}
