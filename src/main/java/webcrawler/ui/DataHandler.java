package webcrawler.ui;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import webcrawler.constant.Constants;
import webcrawler.crawller.Crawler;
import webcrawler.crawller.ImageCrawler;
import webcrawler.crawller.LinkCrawler;
import webcrawler.crawller.SEOAnalyzer;
import webcrawler.crawller.SEOCrawler;
import webcrawler.handler.EventHandlers;
import webcrawler.tables.DataTables;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static webcrawler.handler.EventHandlers.addBlockedForCrawlingProgressBar;
import static webcrawler.handler.EventHandlers.addProgressBar;
import static webcrawler.tables.TableCreation.createImageTable;
import static webcrawler.tables.TableCreation.createLinkDetailsTable;
import static webcrawler.tables.TableCreation.createSeoCrawlingTable;
import static webcrawler.ui.UIComponentFactory.backgroundFill;
import static webcrawler.ui.UIComponentFactory.crawlBudgetWasteLabel;
import static webcrawler.ui.UIComponentFactory.createBorderedVBox;
import static webcrawler.ui.UIComponentFactory.createLabelForTitle;
import static webcrawler.ui.UIComponentFactory.dropShadow;
import static webcrawler.ui.UIComponentFactory.gridPane;
import static webcrawler.ui.UIComponentFactory.hBox;
import static webcrawler.ui.UIComponentFactory.hBox1;
import static webcrawler.ui.UIComponentFactory.pieChart;
import static webcrawler.ui.UIComponentFactory.siteIndexabilityLabel;
import static webcrawler.ui.UIComponentFactory.startButton;
import static webcrawler.ui.UIComponentFactory.tabPane;
import static webcrawler.ui.UIComponentFactory.textField;
import static webcrawler.utils.CrawlerUtils.disableButtons;
import static webcrawler.utils.CrawlerUtils.displayGraph;
import static webcrawler.utils.CrawlerUtils.exportToExcel;
import static webcrawler.utils.DBUtils.fetchDataForPageName;
import static webcrawler.utils.DBUtils.fetchHeaderTagsFromDatabase;
import static webcrawler.utils.DBUtils.fetchImageDataFromDatabase;
import static webcrawler.utils.DBUtils.fetchMetaDataFromDatabase;
import static webcrawler.utils.DBUtils.fetchRepeatedWordsFromDatabase;
import static webcrawler.utils.DBUtils.getDistinctPageData;
import static webcrawler.utils.DBUtils.retrieveCrawledSeoDataFromDatabase;

public class DataHandler {

    public static final int NUM_THREADS = 10; // Number of threads for concurrent crawling tasks
    public static final ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);

    public static final ProgressBar progressBar = new ProgressBar();

    public static Button exportButton = new Button();

    public static GridPane root() {

        TextField textField = textField();

        Button startButton = startButton();

        exportButton = new Button(Constants.EXPORT_BUTTON);
        exportButton.setStyle(Constants.BUTTON_STYLE);

        progressBar.setVisible(false);
        progressBar.setStyle(Constants.PROGRESS_BAR_STYLE); // Set width, height, and color


        HBox hbox = hBox(textField, startButton);

        Background vboxBackground = backgroundFill();

        // Apply CSS to the HBox
        hbox.getStyleClass().add(Constants.HBOX);

        // Creating VBoxs
        VBox leftTopVBox = createBorderedVBox(1000, 300);
        VBox leftBottomVBox = createBorderedVBox(1000, 300);
        VBox rightTopVBox = createBorderedVBox(800, 550);
        VBox rightBottomVBox = createBorderedVBox(800, 550);

        // Applying background color and effects
        leftTopVBox.setStyle(Constants.VBOXS_STYLE);
        leftBottomVBox.setStyle(Constants.VBOXS_STYLE);
        rightTopVBox.setStyle(Constants.VBOXS_STYLE);
        rightBottomVBox.setStyle(Constants.VBOXS_STYLE);
        PieChart pieChart = pieChart();

        Label titleLabel = createLabelForTitle();

        Label siteIndexabilityLabel = siteIndexabilityLabel();

        Label crawlBudgetWasteLabel = crawlBudgetWasteLabel();

        VBox vBoxForFirstRightTop = createBorderedVBox(400, 300);

        vBoxForFirstRightTop.getChildren().addAll(siteIndexabilityLabel, pieChart);
        VBox vBoxForFirstSecondTop = createBorderedVBox(400, 300);
        vBoxForFirstSecondTop.getChildren().add(crawlBudgetWasteLabel);

        vBoxForFirstRightTop.setStyle(Constants.BORDERED_VBOX_STYLE);
        vBoxForFirstSecondTop.setStyle(Constants.BORDERED_VBOX_STYLE);

        // Create an HBox for the two VBox elements to be displayed side by side
        HBox hBox1 = hBox1();
        hBox1.getChildren().addAll(vBoxForFirstRightTop, vBoxForFirstSecondTop);

        // Add the HBox to the rightTopVBox
        rightTopVBox.getChildren().addAll(titleLabel, hBox1);


        TabPane tabPane = tabPane();

        DropShadow dropShadow = dropShadow();

        tabPane.setEffect(dropShadow);

        // Add the TabPane to the leftBottomVBox
        leftBottomVBox.getChildren().add(tabPane);

        // Applying CSS to the VBoxes
        leftTopVBox.setBackground(vboxBackground);
        leftTopVBox.setEffect(dropShadow);

        leftBottomVBox.setBackground(vboxBackground);
        leftBottomVBox.setEffect(dropShadow);

        rightTopVBox.setBackground(vboxBackground);
        rightTopVBox.setEffect(dropShadow);

        rightBottomVBox.setBackground(vboxBackground);
        rightBottomVBox.setEffect(dropShadow);

        // Example of applying background color and effects to the HBox
        hbox.setBackground(vboxBackground);
        hbox.setEffect(dropShadow);

        // Add the TableView to the leftTopVBox
        leftTopVBox.getChildren().add(createSeoCrawlingTable());



        GridPane root = gridPane(hbox, leftTopVBox, rightTopVBox, leftBottomVBox, rightBottomVBox);

        // Set TabPane to fill height and width
        tabPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
        tabPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
        tabPane.setMaxHeight(Double.MAX_VALUE);
        tabPane.setMaxWidth(Double.MAX_VALUE);


        TableView<String> tableViewMeta = new TableView<>();
        TableColumn<String, String> metaTagsColumn = new TableColumn<>(Constants.META_TAGS_COLUMN_TITLE);
        metaTagsColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
        tableViewMeta.getColumns().add(metaTagsColumn);
        tableViewMeta.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableView<String> tableViewH1 = new TableView<>();
        TableColumn<String, String> headerTagsCol = new TableColumn<>(Constants.HEADER_TAGS_COLUMN_TITLE);
        headerTagsCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));
        tableViewH1.getColumns().add(headerTagsCol);
        tableViewH1.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableView<String> tableViewRepeatedWords = new TableView<>();
        TableColumn<String, String> repeatedWordsCol = new TableColumn<>(Constants.REPEATED_WORDS_COLUMN_TITLE);
        repeatedWordsCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));
        tableViewRepeatedWords.getColumns().add(repeatedWordsCol);
        tableViewRepeatedWords.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        EventHandlers.setStartButtonAction(startButton, textField, progressBar, vBoxForFirstSecondTop);
        EventHandlers.setSeoCrawlingTableClickAction(createSeoCrawlingTable(), progressBar, tableViewMeta, tableViewH1, tableViewRepeatedWords, rightBottomVBox);
        EventHandlers.setTabPaneSelectionListener(tabPane, progressBar, createLinkDetailsTable());
        EventHandlers.setLinkDetailsTableSelectionListener(createLinkDetailsTable());
        EventHandlers.setExportButtonAction(exportButton);

        return root;
    }
}
