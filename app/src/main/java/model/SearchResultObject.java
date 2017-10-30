package model;

/**
 * Created by tandat on 10/28/2017.
 */

public class SearchResultObject {
    private String name;
    private String adress;
    private  String phone;
    private String fax;


    public SearchResultObject(String name, String adress, String phone, String fax) {
        this.name = name;
        this.adress = adress;
        this.phone = phone;
        this.fax = fax;
    }

    public SearchResultObject() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }
}
