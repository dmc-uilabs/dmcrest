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

    Notification() {
        this.id = -1;
        this.image = null;
        this.link = null;
        this.type = null;
        this.period = null;
        this.date = null;
        this.title = null;
        this.linkTitle = null;
    }
}