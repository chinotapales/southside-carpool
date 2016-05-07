package carpool.southside.southsidecarpool;

public class Shift{
    public static final String TABLE_NAME = "Shift";
    public static final String COL_ID = "shiftID";
    public static final String COL_DAY = "shiftDay";
    public static final String COL_TYPE = "shiftType";
    public static final String COL_TIME = "shiftTime";
    public static final String COL_PROVIDER = "shiftProvider";
    private int shiftID;
    private String shiftDay;
    private String shiftType;
    private String shiftTime;
    private String shiftProvider;
    public Shift(){
    }
    public Shift(String shiftDay, String shiftType, String shiftTime, String shiftProvider){
        this.shiftDay = shiftDay;
        this.shiftType = shiftType;
        this.shiftTime = shiftTime;
        this.shiftProvider = shiftProvider;
    }
    public Shift(int shiftID, String shiftDay, String shiftType, String shiftTime, String shiftProvider){
        this.shiftID = shiftID;
        this.shiftDay = shiftDay;
        this.shiftType = shiftType;
        this.shiftTime = shiftTime;
        this.shiftProvider = shiftProvider;
    }
    public int getShiftID(){
        return shiftID;
    }
    public void setShiftID(int shiftID){
        this.shiftID = shiftID;
    }
    public String getShiftDay(){
        return shiftDay;
    }
    public void setShiftDay(String shiftDay){
        this.shiftDay = shiftDay;
    }
    public String getShiftType(){
        return shiftType;
    }
    public void setShiftType(String shiftType){
        this.shiftType = shiftType;
    }
    public String getShiftTime(){
        return shiftTime;
    }
    public void setShiftTime(String shiftTime){
        this.shiftTime = shiftTime;
    }
    public String getShiftProvider(){
        return shiftProvider;
    }
    public void setShiftProvider(String shiftProvider){
        this.shiftProvider = shiftProvider;
    }
}
