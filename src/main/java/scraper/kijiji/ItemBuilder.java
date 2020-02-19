package scraper.kijiji;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Objects;

public class ItemBuilder {
    private static final String URL_BASE = "https://www.kijiji.ca";
    private static final String ATTRIBUTE_ID = "data-listing-id";
    private static final String ATTRIBUTE_IMAGE = "image";
    private static final String ATTRIBUTE_IMAGE_SRC = "data-src";
    private static final String ATTRIBUTE_IMAGE_NAME = "alt";
    private static final String ATTRIBUTE_PRICE = "price";
    private static final String ATTRIBUTE_TITLE = "title";
    private static final String ATTRIBUTE_LOCATION = "location";
    private static final String ATTRIBUTE_DATE = "date-posted";
    private static final String ATTRIBUTE_DESCRIPTION = "description";

    private Element element;
    private KijijiItem item;


    ItemBuilder(){

    }
    public ItemBuilder setElement(Element element){
        this.element=element;
        return this;

    }
    public KijijiItem build() {
        KijijiItem kijijiItem = new KijijiItem();
        kijijiItem.setId(getId());
        kijijiItem.setUrl(getUrl());
        kijijiItem.setImageUrl(getImageUrl());
        kijijiItem.setImageName(getImageName());
        kijijiItem.setPrice(getPrice());
        kijijiItem.setTitle(getTitle());
        kijijiItem.setDate(getDate());
        kijijiItem.setLocation(getLocation());
        kijijiItem.setDescription(getDescription());

        return kijijiItem;
    }


    public String getUrl() {
        return URL_BASE+element.getElementsByClass(ATTRIBUTE_TITLE).get(0).child(0).attr("href").trim();
    }

    public String getId() {
        return element.attr(ATTRIBUTE_ID).trim();
    }

    public String getImageUrl() {
        Elements elements = element.getElementsByClass(ATTRIBUTE_IMAGE);
        if(elements.isEmpty())
            return "";
        return elements.get(0).child(0).attr(ATTRIBUTE_IMAGE_SRC).trim();
    }

    public String getImageName() {
        Elements elements = element.getElementsByClass(ATTRIBUTE_IMAGE);
        if(elements.isEmpty())
            return "";
        return elements.get(0).child(0).attr(ATTRIBUTE_IMAGE_NAME).trim();
    }

    public String getPrice() {
        Elements elements = element.getElementsByClass(ATTRIBUTE_PRICE);
        if(elements.isEmpty())
            return "";
        return elements.get(0).text().trim();
    }

    public String getTitle() {
        Elements elements = element.getElementsByClass(ATTRIBUTE_TITLE);
        if(elements.isEmpty())
            return "";
        return elements.get(0).child(0).text().trim();
    }

    public String getDate() {
        Elements elements = element.getElementsByClass(ATTRIBUTE_DATE);
        if(elements.isEmpty())
            return "";
        return elements.get(0).text().trim();
    }

    public String getLocation() {
        Elements elements = element.getElementsByClass(ATTRIBUTE_LOCATION);
        if(elements.isEmpty())
            return "";
        return elements.get(0).childNode(0).outerHtml().trim();
    }

    public String getDescription() {
        Elements elements = element.getElementsByClass(ATTRIBUTE_DESCRIPTION);
        if(elements.isEmpty())
            return "";
        return elements.get(0).text().trim();
    }
}
