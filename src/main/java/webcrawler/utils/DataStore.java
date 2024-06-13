package webcrawler.utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import webcrawler.tables.DataTables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DataStore {
    private static final Map<String, DataTables.SeoAnalysisData> seoAnalysisMap = new HashMap<>();
    private static final Map<String, List<DataTables.CrawledImageData>> crawledImagesMap = new HashMap<>();
    private static final Map<String, DataTables.LinkDataForExcel> crawledDataMap = new HashMap<>();

    public static void insertSEOData(String url, String title, String metaTags, String headerTags, String contentType, String pageLoadTime, String repeatedWords, boolean isIndexed, boolean blockedForCrawling) {
        DataTables.SeoAnalysisData seoData = new DataTables.SeoAnalysisData(url, title, metaTags, headerTags, contentType, pageLoadTime, repeatedWords, isIndexed, blockedForCrawling);
        seoAnalysisMap.put(url, seoData);
    }

    public static void insertImageData(String pageName, String url, String imageUrl, String altText, String size) {
        DataTables.CrawledImageData imageData = new DataTables.CrawledImageData(url, pageName, imageUrl, altText, size);
        crawledImagesMap.computeIfAbsent(url, k -> new ArrayList<>()).add(imageData);
    }
    public static void insertCrawledData(String pageName, String pageUrl, String linkUrl, String linkType, int status) {
        DataTables.LinkDataForExcel newData = new DataTables.LinkDataForExcel(pageName, pageUrl, linkUrl, linkType, status);
        crawledDataMap.put(CrawlerUtils.generateKey(pageName, linkUrl), newData);
    }

    public static List<DataTables.LinkDataForExcel> retrieveLinkDataFromDatabase() {
        return new ArrayList<>(crawledDataMap.values());
    }

    public static List<DataTables.SeoAnalysisData> retrieveSeoAnalysisDataFromDatabase() {
        return new ArrayList<>(seoAnalysisMap.values());
    }

    public static List<DataTables.CrawledImageData> retrieveCrawledImageDataFromDatabase() {
        List<DataTables.CrawledImageData> allImages = new ArrayList<>();
        for (List<DataTables.CrawledImageData> images : crawledImagesMap.values()) {
            allImages.addAll(images);
        }
        return allImages;
    }

    public static List<DataTables.SEODataForCrawling> retrieveCrawledSeoDataFromDatabase() {
        List<DataTables.SEODataForCrawling> seoDataForCrawlings = new ArrayList<>();
        for (DataTables.SeoAnalysisData seoData : seoAnalysisMap.values()) {
            DataTables.SEODataForCrawling data = new DataTables.SEODataForCrawling(seoData.getPageName(), seoData.getContentType(), seoData.getUrl(), seoData.getLoadTime() + "msc");
            seoDataForCrawlings.add(data);
        }
        return seoDataForCrawlings;
    }

    public static List<String> fetchMetaDataFromDatabase(String selectedUrl) {
        List<String> metaDataList = new ArrayList<>();
        DataTables.SeoAnalysisData seoData = seoAnalysisMap.get(selectedUrl);
        if (seoData != null) {
            String[] metaDataArray = seoData.getMetaTags().split("\\r?\\n");
            for (String tag : metaDataArray) {
                String cleanedTag = tag.trim().replace("meta:", "");
                metaDataList.add(cleanedTag);
            }
        }
        return metaDataList;
    }

    public static List<String> fetchHeaderTagsFromDatabase(String selectedUrl) {
        List<String> headerTagsList = new ArrayList<>();
        DataTables.SeoAnalysisData seoData = seoAnalysisMap.get(selectedUrl);
        if (seoData != null) {
            String[] headerTagsArray = seoData.getHeaderTags().split(",");
            for (String tag : headerTagsArray) {
                String cleanedTag = tag.trim().replace("h1:", "");
                headerTagsList.add(cleanedTag);
            }
        }
        return headerTagsList;
    }

    public static List<String> fetchRepeatedWordsFromDatabase(String selectedUrl) {
        List<String> repeatedWordsList = new ArrayList<>();
        DataTables.SeoAnalysisData seoData = seoAnalysisMap.get(selectedUrl);
        if (seoData != null) {
            repeatedWordsList.add(seoData.getRepeatedWords());
        }
        return repeatedWordsList;
    }

    public static List<DataTables.ImageData> fetchImageDataFromDatabase(String url) {
        List<DataTables.ImageData> imageDataList = new ArrayList<>();
        List<DataTables.CrawledImageData> images = crawledImagesMap.get(url);
        if (images != null) {
            for (DataTables.CrawledImageData image : images) {
                DataTables.ImageData data = new DataTables.ImageData(url, image.getPageName(), image.getImageUrl(), image.getImageSize(), image.getAltText());
                imageDataList.add(data);
            }
        }
        return imageDataList;
    }

    public static List<DataTables.PageData> getDistinctPageData() {
        Set<String> pageNames = new HashSet<>();
        for (DataTables.LinkDataForExcel linkData : crawledDataMap.values()) {
            pageNames.add(linkData.getPageName());
        }
        List<DataTables.PageData> pageDataList = new ArrayList<>();
        for (String pageName : pageNames) {
            pageDataList.add(new DataTables.PageData(pageName));
        }
        return pageDataList;
    }

    public static List<DataTables.PageData> fetchDataForPageName(String pageName) {
        List<DataTables.PageData> pageDataList = new ArrayList<>();
        for (DataTables.LinkDataForExcel linkData : crawledDataMap.values()) {
            if (linkData.getPageName().equals(pageName)) {
                pageDataList.add(new DataTables.PageData(pageName, linkData.getStatus(), linkData.getLinkUrl(), linkData.getLinkType()));
            }
        }
        return pageDataList;
    }


    public static void updateProgressBars(ProgressBar progressBar, Label countLabel, String statusPrefix) {
        long count = crawledDataMap.values().stream()
                .filter(data -> String.valueOf(data.getStatus()).startsWith(statusPrefix))
                .count();
        int totalCount = getTotalCount();
        double progress = (double) count / totalCount;
        progressBar.setProgress(progress);
        countLabel.setText(Long.toString(count));
    }

    public static int getTotalCount() {
        return crawledDataMap.size();
    }

    public static void updateBlockedForCrawlingProgressBars(ProgressBar progressBar, Label countLabel) {
        long count = seoAnalysisMap.values().stream().filter(DataTables.SeoAnalysisData::isBlockedForCrawling).count();
        int totalCount = getTotalBlockedForCrawlingCount();
        double progress = (double) count / totalCount;
        progressBar.setProgress(progress);
        countLabel.setText(Long.toString(count));
    }

    public static int getTotalBlockedForCrawlingCount() {
        return (int) seoAnalysisMap.values().stream().filter(DataTables.SeoAnalysisData::isBlockedForCrawling).count();
    }

    public static ObservableList<PieChart.Data> fetchDataFromDatabaseForPieChart() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        long indexedCount = seoAnalysisMap.values().stream().filter(DataTables.SeoAnalysisData::isIndexed).count();
        long nonIndexedCount = seoAnalysisMap.size() - indexedCount;
        pieChartData.add(new PieChart.Data("Indexed Pages: " + indexedCount, indexedCount));
        pieChartData.add(new PieChart.Data("Non-Indexed Pages: " + nonIndexedCount, nonIndexedCount));
        return pieChartData;
    }
}

