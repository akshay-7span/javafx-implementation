package webcrawler.tables;

public class DataTables {
    public static class WebPageData {
        private String url;
        private final int cssPercentage;
        private final int jsPercentage;
        private final int htmlPercentage;

        public WebPageData(String url, int cssPercentage, int jsPercentage, int htmlPercentage) {
            this.url = url;
            this.cssPercentage = cssPercentage;
            this.jsPercentage = jsPercentage;
            this.htmlPercentage = htmlPercentage;
        }

        public int getCssPercentage() {
            return cssPercentage;
        }

        public int getJsPercentage() {
            return jsPercentage;
        }

        public int getHtmlPercentage() {
            return htmlPercentage;
        }
    }

    public static class SEOData {
        private final String pageName;
        private final String contentType;
        private final String pageUrl;
        private final String meta;
        private final String H1;

        public SEOData(String pageName, String contentType, String pageUrl, String meta, String h1) {
            this.pageName = pageName;
            this.contentType = contentType;
            this.pageUrl = pageUrl;
            this.meta = meta;
            H1 = h1;
        }


        public String getPageName() {
            return pageName;
        }

        public String getContentType() {
            return contentType;
        }

        public String getPageUrl() {
            return pageUrl;
        }

        public String getMeta() {
            return meta;
        }

        public String getH1() {
            return H1;
        }
    }

    public static class LinkData {
        private final String pageName;
        private final String url;
        private final String status;

        public LinkData(String pageName, String url, String status) {
            this.pageName = pageName;
            this.url = url;
            this.status = status;
        }

        // Getter methods
        public String getPageName() {
            return pageName;
        }

        public String getUrl() {
            return url;
        }

        public String getStatus() {
            return status;
        }
    }

    public static class ImageData {
        private final String pageUrl;
        private final String pageName;
        private final String imageUrl;
        private final String imageSize;
        private final String altText;

        public ImageData(String pageUrl, String pageName, String imageUrl, String imageSize, String altText) {
            this.pageUrl = pageUrl;
            this.pageName = pageName;
            this.imageUrl = imageUrl;
            this.imageSize = imageSize;
            this.altText = altText;
        }

        public String getPageUrl() {
            return pageUrl;
        }

        public String getPageName() {
            return pageName;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public String getImageSize() {
            return imageSize;
        }

        public String getAltText() {
            return altText;
        }
    }

    public static class LinkDataForExcel {
        private final String pageName;
        private final String pageUrl;
        private final String linkUrl;
        private final String linkType;
        private final int status;

        public LinkDataForExcel(String pageName, String pageUrl, String linkUrl, String linkType, int status) {
            this.pageName = pageName;
            this.pageUrl = pageUrl;
            this.linkUrl = linkUrl;
            this.linkType = linkType;
            this.status = status;
        }

        public String getPageName() {
            return pageName;
        }

        public String getPageUrl() {
            return pageUrl;
        }

        public String getLinkUrl() {
            return linkUrl;
        }

        public String getLinkType() {
            return linkType;
        }

        public int getStatus() {
            return status;
        }
    }

    public static class SeoAnalysisData {
        private final String url;
        private final String pageName;
        private final String metaTags;
        private final String headerTags;
        private final String contentType;
        private final String loadTime;
        private final String repeatedWords;

        public SeoAnalysisData(String url, String pageName, String metaTags, String headerTags, String contentType, String loadTime, String repeatedWords) {
            this.url = url;
            this.pageName = pageName;
            this.metaTags = metaTags;
            this.headerTags = headerTags;
            this.contentType = contentType;
            this.loadTime = loadTime;
            this.repeatedWords = repeatedWords;
        }

        public String getUrl() {
            return url;
        }

        public String getPageName() {
            return pageName;
        }

        public String getMetaTags() {
            return metaTags;
        }

        public String getHeaderTags() {
            return headerTags;
        }

        public String getContentType() {
            return contentType;
        }

        public String getLoadTime() {
            return loadTime;
        }

        public String getRepeatedWords() {
            return repeatedWords;
        }
    }


    public static class CrawledImageData {
        private final String url;
        private final String pageName;
        private final String imageUrl;
        private final String altText;
        private final String imageSize;
        // Add more fields as needed for image data

        public CrawledImageData(String url, String pageName, String imageUrl, String altText, String imageSize) {
            this.url = url;
            this.pageName = pageName;
            this.imageUrl = imageUrl;
            this.altText = altText;
            this.imageSize = imageSize;
        }

        public String getUrl() {
            return url;
        }

        public String getPageName() {
            return pageName;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public String getAltText() {
            return altText;
        }

        public String getImageSize() {
            return imageSize;
        }

    }

    public static class SEODataForCrawling {
        private final String pageName;
        private final String contentType;
        private final String pageUrl;
        private final String loadTime;

        public SEODataForCrawling(String pageName, String contentType, String pageUrl, String loadTime) {
            this.pageName = pageName;
            this.contentType = contentType;
            this.pageUrl = pageUrl;
            this.loadTime = loadTime;
        }

        public String getPageName() {
            return pageName;
        }

        public String getContentType() {
            return contentType;
        }

        public String getPageUrl() {
            return pageUrl;
        }

        public String getLoadTime() {
            return loadTime;
        }
    }

    public static class PageData {
        private String pageName;
        private int status;
        private String linkUrl;
        private String linkType;

        public PageData(String pageName, int status, String linkUrl, String linkType) {
            this.pageName = pageName;
            this.status = status;
            this.linkUrl = linkUrl;
            this.linkType = linkType;

        }

        public PageData(String pageName) {
            this.pageName = pageName;
        }

        // Getter methods for the properties
        public String getPageName() {
            return pageName;
        }

        public int getStatus() {
            return status;
        }

        public String getLinkUrl() {
            return linkUrl;
        }

        public String getLinkType() {
            return linkType;
        }

    }
}
