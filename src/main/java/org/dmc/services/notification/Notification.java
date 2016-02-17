package org.dmc.services.notification;

public class Notification {
    private final int id;
    private final String image;
    private final String link;
    private final String type;
    private final String period;
    private final String date;
    private final String title;
    private final String linkTitle;

    Notification(int id, String image, String link, String type,
                 String period, String date, String title, String linkTitle) {
        this.id = id;
        this.image = image;
        this.link = link;
        this.type = type;
        this.period = period;
        this.date = date;
        this.title = title;
        this.linkTitle = linkTitle;
    }
    
    public int getId() {
        return id;
    }
    
    public String getImage() {
        return image;
    }
    
    public String getLink() {
        return link;
    }
    
    public String getType() {
        return type;
    }
    
    public String getPeriod() {
        return period;
    }
    
    public String getDate() {
        return date;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getLinkTitle() {
        return linkTitle;
    }
}