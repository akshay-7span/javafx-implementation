package webcrawler.crawller;

import javafx.scene.control.ProgressBar;

public interface Crawler {
    void crawl(String url, ProgressBar progressBar);
}