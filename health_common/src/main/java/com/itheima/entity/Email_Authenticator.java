package com.itheima.entity;

import javax.mail.Authenticator;

public class Email_Authenticator extends Authenticator {
    String memberName = null;
    String password = null;

    public Email_Authenticator() {
    }

    public Email_Authenticator(String memberName, String password) {
        this.memberName = memberName;
        this.password = password;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
