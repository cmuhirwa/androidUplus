package info.androidhive.uplus.models;

public class SingleItemModel {


    private String name;
    private String url;
    private String description, eventLocation;


    public SingleItemModel() {
    }

    public SingleItemModel(String name, String url, String eventLocation) {
        this.name = name;
        this.url = url;
        this.eventLocation = eventLocation;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
