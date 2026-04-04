package com.example.airport_management;

public class Session {
    private static final Session instance = new Session();

    private boolean authenticated = false;

    private Session() {}
    public static Session getInstance() {
        return instance;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }
    public void login() {this.authenticated = true;}
    public void logout() {this.authenticated = false;}
}
