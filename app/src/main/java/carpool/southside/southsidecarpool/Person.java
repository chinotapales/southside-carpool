package carpool.southside.southsidecarpool;

public class Person{
    public static final String TABLE_NAME = "People";
    public static final String COL_ID = "personID";
    public static final String COL_NAME = "personName";
    public static final String COL_NUMBER = "personNumber";
    public static final String COL_COLLEGE = "personCollege";
    public static final String COL_FAVORITED = "isFavorited";
    private int personID, isFavorited;
    private String personName;
    private String personNumber;
    private String personCollege;
    public Person(String personName, String personNumber, String personCollege, int isFavorited){
        this.personName = personName;
        this.personNumber = personNumber;
        this.personCollege = personCollege;
        this.isFavorited = isFavorited;
    }
    public Person(int personID, String personName, String personNumber, String personCollege, int isFavorited){
        this.personID = personID;
        this.personName = personName;
        this.personNumber = personNumber;
        this.personCollege = personCollege;
        this.isFavorited = isFavorited;

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
}
