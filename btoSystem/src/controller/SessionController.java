package controller;

import entity.User;

public class SessionController {

    private static User loggedUser = null;

    public static User getLoggedUser()
    {
        return loggedUser;
    }
    public static void clearLoggedUser() { loggedUser = null; }

    public static void logOut(){ clearLoggedUser();}
}
