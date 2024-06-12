package webcrawler.constant;

public class Constants {
    public static final String APP_TITLE = "Web Crawling";
    public static final String ENTER_URL = "Enter URL";
    public static final String START_BUTTON = "Start Crawling";
    public static final String EXPORT_BUTTON = "Export Results";
    public static final String BUTTON_STYLE = "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 20;";
    public static final String PROGRESS_BAR_STYLE = "-fx-pref-width: 200px; -fx-pref-height: 20px; -fx-background-color: #d3d3d3; -fx-accent: #00ff00;";
    public static String VBOX = "vbox";
    public static String HBOX = "hbox";
    public static final String TITLE_LABEL_TEXT = "Crawlabillity";
    public static final String TITLE_LABEL_STYLE = "-fx-font-size: 18px; -fx-font-weight: bold;";
    public static final String SITE_INDEXABILITY_LABEL_TEXT = "Site Indexability";
    public static final String CRAWL_BUDGET_WASTE_LABEL_TEXT = "Crawl Budget Waste";
    public static final String LABEL_STYLE = "-fx-font-size: 14px; -fx-font-weight: bold;";
    public static final String META_TAB_TEXT = "Meta";
    public static final String H1_TAB_TEXT = "H1";
    public static final String IMAGE_TAB_TEXT = "Image";
    public static final String WORD_COUNT_TAB_TEXT = "Repeated Word Count";
    public static final String LINKS_TAB_TEXT = "Crawl Links";

    public static final String META_TAB_CONSTANT = "Meta Content";
    public static final String H1_TAB_CONSTANT = "H1 Content";
    public static final String IMAGE_TAB_CONSTANT = "Image Content";
    public static final String WORD_COUNT_CONSTANT = "Repeated Word Count Content";
    public static final String LINKS_TAB_CONSTANT = "Crawl Links Content";

    public static final String STYLESHEET_PATH = "/styles.css";
    public static final int SCENE_WIDTH = 1000;
    public static final int SCENE_HEIGHT = 700;

    // Styles for VBoxes
    public static final String VBOXS_STYLE = "-fx-background-color: #dcedc8; -fx-background-radius: 10; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0); -fx-padding: 5;";
    public static final String BORDERED_VBOX_STYLE = "-fx-border-color: white; -fx-border-width: 2px; " +
            "-fx-border-radius: 10px; -fx-background-radius: 10px; -fx-background-color: #dcedc8;";



    // Column Titles
    public static final String META_TAGS_COLUMN_TITLE = "Meta Tags";
    public static final String HEADER_TAGS_COLUMN_TITLE = "Header Tags";
    public static final String REPEATED_WORDS_COLUMN_TITLE = "Repeated Words";

    // POI Logger constant
    public static final String POI_LOGGER_PROPERTY = "org.apache.poi.util.POILogger";
    public static final String POI_LOGGER_VALUE = "org.apache.poi.util.SimpleLogger";

    // Excel file related constants
    public static final String EXCEL_FILE_TITLE = "web_crawling_details.xlsx";
    public static final String EXCEL_FILE_DESCRIPTION = "Excel files (*.xlsx)";
    public static final String EXCEL_FILE_EXTENSION = "*.xlsx";

    // Colors for PieChart
    public static final String HTML_COLOR = "#FFA726"; // Orange
    public static final String CSS_COLOR = "#4CAF50"; // Green
    public static final String JS_COLOR = "#03A9F4"; // Blue

    // HTTP Methods
    public static final String HTTP_METHOD_GET = "GET";
    public static final String HTTP_METHOD_HEAD = "HEAD";

    // Error messages
    public static final String PAGE_LOAD_TIME_ERROR = "unable to fetch page load time";

    // Titles
    public static final String CONTENT_BREAKDOWN_TITLE = "Content Breakdown";

    // Default user directory
    public static final String DEFAULT_USER_DIRECTORY = "/Downloads";

    public static String HTML = "HTML";
    public static String CSS = "CSS";
    public static String JAVA_SCRIPT = "JavaScript";

    public static String SAVE_EXCEL_FILE= "Save Excel File";
    public static String USER_HOME= "user.home";

    public static String CSS_SELECTOR = "link[rel=stylesheet]";
    public static String JS_SELECTOR = "script[src]";
    public static String ALL_HTML_SELECTOR = "html *";

    public static String LINK_DATA="Link Data";
    public static String SEO_ANALYSIS="SEO Analysis";
    public static String CRAWLED_IMAGES="Crawled Images";

    public static String BASE_URL = "https://www.google.com/search?q=site:";


}
