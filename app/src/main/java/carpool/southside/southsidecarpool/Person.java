package carpool.southside.southsidecarpool;

public class Person{
    private int personID;
    private String personName;
    private String personNumber;
    private String personCollege;
    /*
    private String personBatch;
    private String personVillage;
     */
    public Person(String personName, String personNumber, String personCollege){
        this.personName = personName;
        this.personNumber = personNumber;
        this.personCollege = personCollege;
    }
    public Person(int personID, String personName, String personNumber, String personCollege){
        this.personID = personID;
        this.personName = personName;
        this.personNumber = personNumber;
        this.personCollege = personCollege;
    }
    //If Batch and Village are Implemented
    /*
    public Person(String personName, String personNumber, String personCollege, String personBatch, String personVillage){
        this.personName = personName;
        this.personNumber = personNumber;
        this.personCollege = personCollege;
        this.personBatch = personBatch;
        this.personVillage = personVillage;
    }
    public Person(int personID, String personName, String personNumber, String personCollege, String personBatch, String personVillage){
        this.personID = personID;
        this.personName = personName;
        this.personNumber = personNumber;
        this.personCollege = personCollege;
        this.personBatch = personBatch;
        this.personVillage = personVillage;
    }
    */
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
    /*
    public String getPersonBatch(){
        return personBatch;
    }
    public void setPersonBatch(String personBatch){
        this.personBatch = personBatch;
    }
    public String getPersonVillage(){
        return personVillage;
    }
    public void setPersonVillage(String personVillage){
        this.personVillage = personVillage;
    }
    */
}
