package controller;

import entity.Applicant;
import entity.Manager;
import entity.Officer;
import entity.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserController {
    private static User loggedUser = null;

    private Map<String, User> Users = new HashMap<>();;
    private final String applicantPath = "ApplicantList.csv";

    public void init()
    {
        try{readData();}
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static User getLoggedUser()
    {
        return loggedUser;
    }

    public static void clearLoggedUser() { loggedUser = null; }

    public boolean readData() throws IOException
    {
        //process applicants
        try (BufferedReader br = new BufferedReader(new FileReader(applicantPath))) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                String[] values = line.split(",");
                int id = Integer.parseInt(values[0]);
                String name = values[1];
                String nric = values[2];
                int age = Integer.parseInt(values[3]);
                boolean isMarried = values[4].equals("Married");
                String password = values[5];
                String type = values[6];
                switch(type)
                {
                    case "Applicant":
                    {
                        User newUser = new Applicant(id, name, nric, age, isMarried, password);
                        Users.put(nric, newUser);
                    }
                    break;
                    case "Officer":
                    {
                        User newUser = new Officer(id, name, nric, age, isMarried, password);
                        Users.put(nric, newUser);
                    }
                    break;
                    case "Manager":
                    {
                        User newUser = new Manager(id, name, nric, age, isMarried, password);
                        Users.put(nric, newUser);
                    }
                    break;
                }
            }
        }
        return true;
    }

    public boolean writeData()
    {
        return false;
    }

    public boolean login(String username, String password)
    {
        if (Users.containsKey(username)) {
            if(Users.get(username).verifyPassword(password))
            {
                loggedUser = Users.get(username);
                return true;
            }
        }
        return false;
    }

    public User getUser(int id)
    {
        for(User user: Users.values())
        {
            if(user.getID() == id)
                return user;
        }
        return null;
    }

    public Manager getManager(int id)
    {
        for(String key : Users.keySet())
        {
            if(Users.get(key).getID() == id && Users.get(key) instanceof Manager)
            {
                return (Manager) Users.get(key);
            }
        }
        return null;
    }

    public Officer getOfficer(int id)
    {
        for(String key : Users.keySet())
        {
            if(Users.get(key).getID() == id && Users.get(key) instanceof Officer)
            {
                return (Officer) Users.get(key);
            }
        }
        return null;
    }
}
