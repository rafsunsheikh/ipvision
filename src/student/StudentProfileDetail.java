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
public class StudentProfileDetail {

    private String fullName;
    private String userName;
    private String password;
    private String phone;
    private String email;

    Address address = new Address();
    //UserExperience userExperience = new UserExperience();
    //UserDetails userDetails = new UserDetails();

    //All the set methods
    public void setFullName(String fullName){
        this.fullName = fullName;
    }
    public void setUserName(String userName){
        this.userName = userName;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public void setPhone(String phone){
        this.phone = phone;
    }
    public void setEmail(String email){
        this.email = email;
    }

    //all the get methods
    public String getFullName(){
        return fullName;
    }
    public String getUserName(){
        return userName;
    }
    public String getPassword(){
        return password;
    }
    public String getPhone(){
        return phone;
    }
    public String getEmail(){
        return email;
    }

}