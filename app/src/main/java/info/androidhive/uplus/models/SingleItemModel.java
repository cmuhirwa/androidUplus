package info.androidhive.uplus.models;

public class SingleItemModel {


    private String name;
    private String url;
    private String description, eventLocation, sectionType, eventTicketStatus, eventTicketCode;


    public SingleItemModel() {
    }

    public SingleItemModel(String name, String url, String eventLocation, String sectionType, String eventTicketStatus, String eventTicketCode) {
        this.name = name;
        this.url = url;
        this.eventLocation = eventLocation;
        this.sectionType = sectionType;
        this.eventTicketStatus = eventTicketStatus;
        this.eventTicketCode = eventTicketCode;
    }

    public String getEventTicketStatus() {
        return eventTicketStatus;
    }

    public String getEventTicketCode() {
        return eventTicketCode;
    }

    public void setEventTicketStatus(String eventTicketStatus) {
        this.eventTicketStatus = eventTicketStatus;
    }

    public void setEventTicketCode(String eventTicketCode) {
        this.eventTicketCode = eventTicketCode;
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

    public String getSectionType(){ return sectionType;}
    public void setSectionType(String sectionType){
        this.sectionType = sectionType;
    }
}
