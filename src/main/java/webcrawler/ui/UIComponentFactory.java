package webcrawler.ui;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import webcrawler.constant.Constants;

import static webcrawler.ui.DataHandler.exportButton;
import static webcrawler.ui.DataHandler.progressBar;
import static webcrawler.utils.CrawlerUtils.createPieChart;
import static webcrawler.utils.DBUtils.fetchDataFromDatabaseForPieChart;

public class UIComponentFactory {

    public static Button startButton(){
        // Creating button
        Button startButton = new Button(Constants.START_BUTTON);
        startButton.setStyle(Constants.BUTTON_STYLE);
        return startButton;
    }
    public static VBox createBorderedVBox(double width, double height) {
        VBox vbox = new VBox();
        vbox.setPrefSize(width, height);
        vbox.getStyleClass().add(Constants.VBOX);
        return vbox;
    }

    public static TextField textField (){
        // Creating text area
        TextField urlTextField = new TextField();
        urlTextField.setPromptText(Constants.ENTER_URL); // Displaying the text as prompt in the text field

        // Setting up the width of the text field
        urlTextField.setPrefWidth(400);
        return urlTextField;
    }

    public static HBox hBox(TextField textField ,Button startButton){
        HBox hbox = new HBox(10); // spacing = 10
        hbox.getChildren().addAll(textField, startButton, exportButton, progressBar);
        hbox.setAlignment(Pos.CENTER);
        return hbox;
    }
    public static Background backgroundFill(){
        // Create BackgroundFill for VBox backgrounds
        BackgroundFill vboxBackgroundFill = new BackgroundFill(Color.web("#FAFAFA"), new CornerRadii(10), null);
        return new Background(vboxBackgroundFill);
    }

    public static PieChart pieChart(){
        ObservableList<PieChart.Data> pieChartData = fetchDataFromDatabaseForPieChart();

        // Create the pie chart
        PieChart pieChart = createPieChart(pieChartData);
        pieChart.setPrefSize(250,200);
        pieChart.setLabelsVisible(false);
        return pieChart;
    }
    public static Label createLabelForTitle(){
        // Create a label for the title
        Label titleLabel = new Label(Constants.TITLE_LABEL_TEXT);
        titleLabel.setStyle(Constants.TITLE_LABEL_STYLE);
        return titleLabel;
    }
    public static Label siteIndexabilityLabel(){
        // Create a label for Site Indexability
        Label siteIndexabilityLabel = new Label(Constants.SITE_INDEXABILITY_LABEL_TEXT);
        siteIndexabilityLabel.setStyle(Constants.LABEL_STYLE);
        siteIndexabilityLabel.setPadding(new Insets(5));
        return siteIndexabilityLabel;
    }

    public static Label crawlBudgetWasteLabel(){
        // Create a label for Crawl Budget Waste
        Label crawlBudgetWasteLabel = new Label(Constants.CRAWL_BUDGET_WASTE_LABEL_TEXT);
        crawlBudgetWasteLabel.setStyle(Constants.LABEL_STYLE);
        crawlBudgetWasteLabel.setPadding(new Insets(5));
        return crawlBudgetWasteLabel;
    }
    public static HBox hBox1(){
        // Create an HBox for the two VBox elements to be displayed side by side
        HBox hbox1 = new HBox(10); // 10 is the spacing between the VBox elements
        hbox1.setPadding(new Insets(10));
        return hbox1;
    }

    public static TabPane tabPane(){
        // Create the TabPane
        TabPane tabPane = new TabPane();
        tabPane.setPrefSize(600, 300);

        // Disable closing of tabs
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Create tabs and their respective content
        Tab metaTab = new Tab(Constants.META_TAB_TEXT);
        Tab h1Tab = new Tab(Constants.H1_TAB_TEXT);
        Tab imageTab = new Tab(Constants.IMAGE_TAB_TEXT);
        Tab wordCountTab = new Tab(Constants.WORD_COUNT_TAB_TEXT);
        Tab links = new Tab(Constants.LINKS_TAB_TEXT);

        // Add content to tabs (you can add your specific content here)
        // For demonstration, adding a label with the tab name
        metaTab.setContent(new Label(Constants.META_TAB_CONSTANT));
        h1Tab.setContent(new Label(Constants.H1_TAB_CONSTANT));
        imageTab.setContent(new Label(Constants.IMAGE_TAB_CONSTANT));
        wordCountTab.setContent(new Label(Constants.WORD_COUNT_CONSTANT));
        links.setContent(new Label(Constants.LINKS_TAB_CONSTANT));

        // Add tabs to the TabPane
        tabPane.getTabs().addAll(metaTab, h1Tab, imageTab, wordCountTab, links);

        return tabPane;
    }

    public static DropShadow dropShadow(){
        // Applying drop shadow effect
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.rgb(0, 0, 0, 0.2));
        dropShadow.setRadius(10);
        dropShadow.setOffsetX(0);
        dropShadow.setOffsetY(0);

        return dropShadow;
    }

    public static GridPane gridPane(HBox hbox, VBox leftTopVBox, VBox rightTopVBox, VBox leftBottomVBox, VBox rightBottomVBox){
        // Creating a GridPane as the root node
        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setHgap(5);
        root.setVgap(5);
        root.setPadding(new Insets(10));

        // Adding components to the GridPane
        root.add(hbox, 0, 0, 2, 1); // Spanning across two columns
        root.add(leftTopVBox, 0, 1);
        root.add(rightTopVBox, 1, 1);
        root.add(leftBottomVBox, 0, 2);
        root.add(rightBottomVBox, 1, 2);

        return root;
    }
}