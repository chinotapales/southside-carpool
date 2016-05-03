package carpool.southside.southsidecarpool;

public class Person{
    public static final String TABLE_NAME = "People";
    public static final String COL_ID = "personID";
    public static final String COL_NAME = "personName";
    public static final String COL_NUMBER = "personNumber";
    public static final String COL_COLLEGE = "personCollege";
    public static final String COL_PROVIDER_FAVORITED = "isFavorited";
    public static final String COL_RIDER_FAVORITED = "isRiderFavorited";
    private int personID, isFavorited, isRiderFavorited;
    private String personName;
    private String personNumber;
    private String personCollege;
    public Person(String personName, String personNumber, String personCollege, int isFavorited, int isRiderFavorited){
        this.personName = personName;
        this.personNumber = personNumber;
        this.personCollege = personCollege;
        this.isFavorited = isFavorited;
        this.isRiderFavorited = isRiderFavorited;
    }
    public Person(int personID, String personName, String personNumber, String personCollege, int isFavorited, int isRiderFavorited){
        this.personID = personID;
        this.personName = personName;
        this.personNumber = personNumber;
        this.personCollege = personCollege;
        this.isFavorited = isFavorited;
        this.isRiderFavorited = isRiderFavorited;
    }
    public int getPersonID(){
        return personID;
    }
    public void setPersonID(int personID){
        this.personID = personID;
    }
    public String getPersonName(){
        return personName;
    }
    public void setPersonName(String personName){
        this.personName = personName;
    }
    public String getPersonNumber(){
        return personNumber;
    }
    public void setPersonNumber(String personNumber){
        this.personNumber = personNumber;
    }
    public String getPersonCollege(){
        return personCollege;
    }
    public void setPersonCollege(String personCollege){
        this.personCollege = personCollege;
    }
    public int getIsFavorited(){
        return isFavorited;
    }
    public void setIsFavorited(int isFavorited){
        this.isFavorited = isFavorited;
    }
    public int getIsRiderFavorited(){
        return isRiderFavorited;
    }
    public void setIsRiderFavorited(int isRiderFavorited){
        this.isRiderFavorited = isRiderFavorited;
    }
}
