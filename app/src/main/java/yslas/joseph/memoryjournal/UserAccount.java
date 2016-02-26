package yslas.joseph.memoryjournal;

import android.util.Log;

/**
 * Created by JYsla_000 on 2/12/2016.
 * This is the object for the user account
 * it will hold all the account info from the database into an object
 */
public class UserAccount {
    private String email;
    private String uName;
    private String password;
    private String security_Q1;
    private String securityA1;
    private String security_Q2;
    private String securityA2;

    public UserAccount(String email, String uName, String password, String security_Q1,
                       String security_Q2, String securityA1, String securityA2)
    {
        this.email = email;
        this.uName = uName;
        this.password = password;
        this.security_Q1 = security_Q1;
        this.security_Q2 = security_Q2;
        this.securityA1 = securityA1;
        this.securityA2 = securityA2;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail()
    {
        return email;
    }

    public void setUName(String uName) {
        this.uName = uName;
    }
    public String getUName() {
        return uName;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }

    public void setSecurity_Q1(String security_Q1) {
        this.security_Q1 = security_Q1;
    }
    public String getSecurity_Q1() {
        return security_Q1;
    }

    public void setSecurityA1(String securityA1) {
        this.securityA1 = securityA1;
    }
    public String getSecurityA1() {
        return securityA1;
    }

    public void setSecurity_Q2(String security_Q2) {
        this.security_Q2 = security_Q2;
    }
    public String getSecurity_Q2() {
        return security_Q2;
    }

    public void setSecurityA2(String securityA2) {
        this.securityA2 = securityA2;
    }
    public String getSecurityA2() {
        return securityA2;
    }

    public void testAccount()
    {
        Log.d("account", email + " " + uName + " " + password + " " +security_Q1 + " "
        + security_Q2 + " " + securityA1 + " " + securityA2);
    }
}
