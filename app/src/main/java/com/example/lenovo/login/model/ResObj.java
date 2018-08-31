package com.example.lenovo.login.model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by Lenovo on 16/08/2018.
 */

public class ResObj {
    private String success;
    private String message;
    private List<Login> login;


    public ResObj(String success, String message, List<Login>login) {
        this.success = success;
        this.message = message;
        this.login = login;
    }

    public String getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<Login> getLogin() {
        return login;
    }

    public class Login{
        private String username;
        private String bank;
        private String branch;

        public Login(String username, String bank, String branch) {
            this.username = username;
            this.bank = bank;
            this.branch = branch;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getBank() {
            return bank;
        }

        public void setBank(String bank) {
            this.bank = bank;
        }

        public String getBranch() {
            return branch;
        }

        public void setBranch(String branch) {
            this.branch = branch;
        }
    }
}
