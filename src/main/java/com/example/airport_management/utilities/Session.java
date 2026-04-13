package com.example.airport_management.utilities;

public class Session {
    private static final Session instance = new Session();

    private boolean authenticated = false;
    private int user_id = 0;

    private Session() {}
    public static Session getInstance() {
        return instance;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }
    public void login(int user_id) {this.authenticated = true; this.user_id = user_id;}
    public void logout() {this.authenticated = false; this.user_id = 0;}
    public int get_user_id() {return this.user_id;}
}
