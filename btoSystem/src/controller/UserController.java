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
    User loggedUser = null;

    private Map<String, User> Users = new HashMap<>();;
    private final String applicantPath = "ApplicantList.csv";
    private final String officerPath = "OfficerList.csv";
    private final String managerPath = "ManagerList.csv";

    public void init()
    {
        try{readData();}
        catch (IOException e)
        {
            e.printStackTrace();
        }
        printUsersContent();
    }

    public User getLoggedUser()
    {
        return loggedUser;
    }

    public void printUsersContent()
    {

        File file = new File(applicantPath);
        System.out.println("Looking for: " + file.getAbsolutePath());
        System.out.println("Exists? " + file.exists());

        for (Map.Entry<String, User> entry : Users.entrySet()) {
            User user = entry.getValue();
            System.out.println("NRIC: " + entry.getKey());
            System.out.println("Name: " + user.getName());
            System.out.println("Age: " + user.getAge());
            System.out.println("Married: " + user.isMarried());

            // Check subclass
            if (user instanceof Officer) {
                System.out.println("Type: Officer");
            }
            else if (user instanceof Applicant) {
                System.out.println("Type: Applicant");
                // If Applicant has specific methods or fields, you can cast and print
                // Applicant applicant = (Applicant) user;
            } else if (user instanceof Manager) {
                System.out.println("Type: Manager");
            } else {
                System.out.println("Type: Unknown or base User");
            }

            System.out.println("-----");
        }
    }

    public boolean readData() throws IOException
    {
        //process applicants
        try (BufferedReader br = new BufferedReader(new FileReader(applicantPath))) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String name = values[0];
                String nric = values[1];
                int age = Integer.parseInt(values[2]);
                boolean isMarried = values[3].equals("Married");
                String password = values[4];
                User newUser = new Applicant(name, nric, age, isMarried, password);
                Users.put(nric, newUser);
            }
        }

        //process managers
        try (BufferedReader br = new BufferedReader(new FileReader(managerPath))) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String name = values[0];
                String nric = values[1];
                int age = Integer.parseInt(values[2]);
                boolean isMarried = values[3].equals("Married");
                String password = values[4];
                User newUser = new Manager(name, nric, age, isMarried, password);
                Users.put(nric, newUser);
            }
        }

        //process officers
        try (BufferedReader br = new BufferedReader(new FileReader(officerPath))) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String name = values[0];
                String nric = values[1];
                int age = Integer.parseInt(values[2]);
                boolean isMarried = values[3].equals("Married");
                String password = values[4];
                User newUser = new Officer(name, nric, age, isMarried, password);
                Users.put(nric, newUser);
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

    public Manager getManager(String name)
    {
        for(String key : Users.keySet())
        {
            if(Users.get(key).getName().equals(name) && Users.get(key) instanceof Manager)
            {
                return (Manager) Users.get(key);
            }
        }
        return null;
    }

    public Officer getOfficer(String name)
    {
        for(String key : Users.keySet())
        {
            if(Users.get(key).getName().equals(name) && Users.get(key) instanceof Officer)
            {
                return (Officer) Users.get(key);
            }
        }
        return null;
    }
}
