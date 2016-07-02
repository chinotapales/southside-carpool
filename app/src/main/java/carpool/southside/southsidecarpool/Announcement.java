package carpool.southside.southsidecarpool;

public class Announcement{
    public static final String TABLE_NAME = "Annoucnement";
    public static final String COL_ID = "announcementID";
    public static final String COL_NAME = "annoucnementName";
    public static final String COL_DATE = "annoucnementDate";
    public static final String COL_MESSAGE = "announcementMessage";
    private int announcementID;
    private String announcementName;
    private String announcementDate;
    private String announcementMessage;
    public Announcement(){
    }
    public Announcement(int announcementID, String announcementName, String announcementDate, String announcementMessage){
        this.announcementID = announcementID;
        this.announcementName = announcementName;
        this.announcementDate = announcementDate;
        this.announcementMessage = announcementMessage;
    }
    public Announcement(String announcementName, String announcementDate, String announcementMessage){
        this.announcementName = announcementName;
        this.announcementDate = announcementDate;
        this.announcementMessage = announcementMessage;
    }
    public int getAnnouncementID(){
        return announcementID;
    }
    public void setAnnouncementID(int announcementID){
        this.announcementID = announcementID;
    }
    public String getAnnouncementName(){
        return announcementName;
    }
    public void setAnnouncementName(String announcementName){
        this.announcementName = announcementName;
    }
    public String getAnnouncementDate(){
        return announcementDate;
    }
    public void setAnnouncementDate(String announcementDate){
        this.announcementDate = announcementDate;
    }
    public String getAnnouncementMessage(){
        return announcementMessage;
    }
    public void setAnnouncementMessage(String announcementMessage){
        this.announcementMessage = announcementMessage;
    }


    public static String getTableName() {
        return TABLE_NAME;
    }

    public static String getColId() {
        return COL_ID;
    }

    public static String getColName() {
        return COL_NAME;
    }

    public static String getColDate() {
        return COL_DATE;
    }

    public static String getColMessage() {
        return COL_MESSAGE;
    }
}
