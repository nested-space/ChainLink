package com.edenrump.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Link {
    private StringProperty name;
    private StringProperty description;
    private StringProperty url;

    public Link(String name, String description, String url) {
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
        this.url = new SimpleStringProperty(url);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public String getUrl() {
        return url.get();
    }

    public StringProperty urlProperty() {
        return url;
    }
}
