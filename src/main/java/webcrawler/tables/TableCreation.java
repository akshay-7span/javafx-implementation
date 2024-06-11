package webcrawler.tables;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import webcrawler.tables.DataTables.*;

public class TableCreation {

    public static TableView<SEODataForCrawling> createSeoCrawlingTable() {
        TableView<SEODataForCrawling> tableViewSEOCrawlingDetails = new TableView<>();
        TableColumn<SEODataForCrawling, String> pageNameCol = new TableColumn<>("Page Name");
        TableColumn<SEODataForCrawling, String> contentTypeCol = new TableColumn<>("Content Type");
        TableColumn<SEODataForCrawling, String> pageUrlCol = new TableColumn<>("Page URL");
        TableColumn<SEODataForCrawling, String> pageLoadTime = new TableColumn<>("Page Load Time");

        pageNameCol.setCellValueFactory(new PropertyValueFactory<>("pageName"));
        contentTypeCol.setCellValueFactory(new PropertyValueFactory<>("contentType"));
        pageUrlCol.setCellValueFactory(new PropertyValueFactory<>("pageUrl"));
        pageLoadTime.setCellValueFactory(new PropertyValueFactory<>("loadTime"));

        tableViewSEOCrawlingDetails.getColumns().addAll(pageNameCol, contentTypeCol, pageUrlCol, pageLoadTime);

        return tableViewSEOCrawlingDetails;
    }

    public static TableView<PageData> createLinksTable() {
        TableView<PageData> tableView = new TableView<>();
        TableColumn<PageData, String> pageNameLinks = new TableColumn<>("Page Name");

        pageNameLinks.setCellValueFactory(new PropertyValueFactory<>("pageName"));
        tableView.getColumns().add(pageNameLinks);

        return tableView;
    }

    public static TableView<PageData> createLinkDetailsTable() {
        TableView<PageData> linkTableView = new TableView<>();
        TableColumn<PageData, String> linkUrlCol = new TableColumn<>("Link URL");
        TableColumn<PageData, String> linkTypeCol = new TableColumn<>("Link Type");
        TableColumn<PageData, Integer> statusCol = new TableColumn<>("Status");

        linkUrlCol.setCellValueFactory(new PropertyValueFactory<>("linkUrl"));
        linkTypeCol.setCellValueFactory(new PropertyValueFactory<>("linkType"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        linkTableView.getColumns().addAll(linkUrlCol, linkTypeCol, statusCol);

        return linkTableView;
    }

    public static TableView<String> createMetaTable() {
        TableView<String> tableViewMeta = new TableView<>();
        TableColumn<String, String> metaTagsColumn = new TableColumn<>("Meta Tags");

        metaTagsColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
        tableViewMeta.getColumns().add(metaTagsColumn);
        tableViewMeta.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        return tableViewMeta;
    }

    public static TableView<String> createH1Table() {
        TableView<String> tableViewH1 = new TableView<>();
        TableColumn<String, String> headerTagsCol = new TableColumn<>("Header Tags");

        headerTagsCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));
        tableViewH1.getColumns().add(headerTagsCol);
        headerTagsCol.prefWidthProperty().bind(tableViewH1.widthProperty().multiply(0.9));
        headerTagsCol.setResizable(true);

        return tableViewH1;
    }

    public static TableView<String> createRepeatedWordsTable() {
        TableView<String> tableViewRepeatedWords = new TableView<>();
        TableColumn<String, String> repeatedWordsCol = new TableColumn<>("Repeated Words");

        repeatedWordsCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));
        tableViewRepeatedWords.getColumns().add(repeatedWordsCol);
        repeatedWordsCol.prefWidthProperty().bind(tableViewRepeatedWords.widthProperty().multiply(0.9));
        repeatedWordsCol.setResizable(true);

        return tableViewRepeatedWords;
    }

    public static TableView<String> createLoadTimeTable() {
        TableView<String> tableViewLoadTime = new TableView<>();
        TableColumn<String, String> loadTimeCol = new TableColumn<>("Load Time");

        loadTimeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));
        tableViewLoadTime.getColumns().add(loadTimeCol);
        loadTimeCol.prefWidthProperty().bind(tableViewLoadTime.widthProperty().multiply(0.9));
        loadTimeCol.setResizable(true);

        return tableViewLoadTime;
    }

    public static TableView<ImageData> createImageTable() {
        TableView<ImageData> imageTable = new TableView<>();
        TableColumn<ImageData, String> pageUrl = new TableColumn<>("Page URL");
        TableColumn<ImageData, String> pageNameForImage = new TableColumn<>("Page Name");
        TableColumn<ImageData, String> imageUrl = new TableColumn<>("Image URL");
        TableColumn<ImageData, String> imageSizes = new TableColumn<>("Image Size");
        TableColumn<ImageData, String> altTextCol = new TableColumn<>("Alt Text");

        pageUrl.setCellValueFactory(new PropertyValueFactory<>("pageUrl"));
        pageNameForImage.setCellValueFactory(new PropertyValueFactory<>("pageName"));
        imageUrl.setCellValueFactory(new PropertyValueFactory<>("imageUrl"));
        imageSizes.setCellValueFactory(new PropertyValueFactory<>("imageSize"));
        altTextCol.setCellValueFactory(new PropertyValueFactory<>("altText"));

        imageTable.getColumns().addAll(pageUrl, pageNameForImage, imageUrl, imageSizes, altTextCol);

        return imageTable;
    }
}
