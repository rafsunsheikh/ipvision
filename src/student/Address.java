/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

/**
 *
 * @author MD Rafsun Sheikh
 */
import java.util.Scanner;

public class Address {
    private String village;
    private String postOffice;
    private String thana;
    private String district;
    private String zipCode;

    //all the get methods
    public void setVillage(String village){
        this.village = village;
    }
    public void setPostOffice(String postOffice){
        this.postOffice= postOffice;
    }
    public void setThana(String thana){
        this.thana = thana;
    }
    public void setDistrict(String district){
        this.district = district;
    }
    public void setZipCode(String zipCode){
        this.zipCode = zipCode;
    }

    //all the get methods
    public String getVillage(){
        return village;
    }
    public String getPostOffice(){
        return postOffice;
    }
    public String getThana(){
        return thana;
    }
    public String getDistrict(){
        return district;
    }
    public String getZipCode(){
        return zipCode;
    }

}
