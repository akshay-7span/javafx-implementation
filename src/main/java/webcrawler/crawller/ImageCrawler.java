package webcrawler.crawller;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressBar;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import static webcrawler.utils.CrawlerUtils.*;
import static webcrawler.utils.DBUtils.insertImageData;


public class ImageCrawler implements Crawler {

    private final Set<String> visitedUrls = new HashSet<>();

    public ImageCrawler() {
    }

    @Override
    public void crawl(String url, ProgressBar progressBar) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                progressBar.setVisible(true);
                try (final WebClient webClient = createWebClient()) {
                    HtmlPage mainPage = webClient.getPage(url);
                    webClient.waitForBackgroundJavaScript(10000);
                    crawlImagesHelper(url, mainPage, webClient);

                    List<String> links = extractLinks(mainPage);

                    for (String link : links) {
                        try {
                            HtmlPage linkedPage = webClient.getPage(link);
                            webClient.waitForBackgroundJavaScript(10000);
                            crawlImagesHelper(linkedPage.getUrl().toString(), linkedPage, webClient);
                        } catch (Exception e) {
                            System.err.println("Error processing URL: " + link + " - " + e.getMessage());
                        }
                    }
                    decrementActiveTasks(progressBar);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        };
        new Thread(task).start();
    }

    private void crawlImagesHelper(String pageUrl, HtmlPage page, WebClient webClient) {
        if (visitedUrls.contains(pageUrl)) {
            return;
        }
        visitedUrls.add(pageUrl);

        List<HtmlImage> images = page.getByXPath("//img");
        webClient.waitForBackgroundJavaScript(10000);

        Platform.runLater(() -> {
            for (HtmlImage image : images) {
                String imageUrl = image.getAttribute("src");
                String imageAltText = image.getAttribute("alt");
                String pageName = extractPageName(pageUrl);
                try {
                    int imageSize = getImageSize(imageUrl);
                    String imageSizeStr = String.format("%.2f", (double) imageSize / 1024) + " KB";
                    insertImageData(pageName, pageUrl, imageUrl, imageAltText, imageSizeStr);
                } catch (IOException e) {
                    System.err.println("Error saving image data to the database: " + e.getMessage());
                }
            }
        });
    }

}

