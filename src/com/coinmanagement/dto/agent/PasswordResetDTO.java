/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coinmanagement.dto.agent;

import java.sql.Timestamp;
import com.coinmanagement.dto.RingBit.PayloadDTO;
import com.coinmanagement.dto.RingBit.RingBitPreOrderDTO;
import java.util.List;

/**
 *
 * @author sagar
 */
public class PasswordResetDTO {

    private String userId;
    private String password;
    Timestamp created;
    

    public String getUserId() {
        return userId;
    }

    public void setuserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
