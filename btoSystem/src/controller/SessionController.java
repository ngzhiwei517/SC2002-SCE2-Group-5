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

    public static boolean login(String username, String password)
    {
        User user = UserController.getUser(username);
        if (user != null) {
            if(user.verifyPassword(password))
            {
                loggedUser = user;
                return true;
            }
        }
        return false;
    }
}
