package com.example.lenovo.login.remote;

/**
 * Created by Lenovo on 16/08/2018.
 */

public class ApiUtils {

    public static final String BASE_URL = "http://www.anagata.co.id/apifwdata/";

    public static UserService getUserService() {
        return RetrofitClient.getClient(BASE_URL).create(UserService.class);
    }
}