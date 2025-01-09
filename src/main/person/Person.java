package person;

public class Person {

    private String givenName;
    private String middleName;
    private String familyName;
    private int gender;
    private String homeAddress;
    private int phoneNumber;

    // Default constructor
    public Person() {
    }

    // Parameterized constructor
    public Person(String givenName, String middleName, String familyName, int gender, String homeAddress, int phoneNumber) {
        this.givenName = givenName;
        this.middleName = middleName;
        this.familyName = familyName;
        this.gender = gender;
        this.homeAddress = homeAddress;
        this.phoneNumber = phoneNumber;
    }

    // Getters and Setters
    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Person{" +
                "givenName='" + givenName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", familyName='" + familyName + '\'' +
                ", gender=" + gender +
                ", homeAddress='" + homeAddress + '\'' +
                ", phoneNumber=" + phoneNumber +
                '}';
    }
}
