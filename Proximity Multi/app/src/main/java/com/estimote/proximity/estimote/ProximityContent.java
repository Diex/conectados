package com.estimote.proximity.estimote;

//
// Running into any issues? Drop us an email to: contact@estimote.com
//

public class ProximityContent {

    private String title;
    private String subtitle;
    private String color;

    ProximityContent(String title, String subtitle, String color) {
        this.title = title;
        this.subtitle = subtitle;
        this.color = color;
    }

    String getTitle() {
        return title;
    }

    String getSubtitle() {
        return subtitle;
    }

    String getColor() { return color; }
}
