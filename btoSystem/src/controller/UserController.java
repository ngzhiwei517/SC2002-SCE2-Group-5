package controller;

import entity.*;
import interfaces.CSVReader;
import interfaces.CSVWriter;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserController implements CSVReader, CSVWriter {
    private static Map<String, User> Users = new HashMap<>();;
    private final String userPath = "ApplicantList.csv";

    public void init()
    {
        try{readData();}
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void exit()
    {
        writeData();
    }

    public boolean readData() throws IOException
    {
        //process applicants
        try (BufferedReader br = new BufferedReader(new FileReader(userPath))) {
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
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(userPath))){
            bw.write("u.id,name,nric,age,marital_status,password,account_type");
            bw.newLine();
            for(String key : Users.keySet()) {
                User user = Users.get(key);
                String userString = "";
                userString += user.getID() + ",";
                userString += user.getName() + ",";
                userString += user.getNric() + ",";
                userString += user.getAge() + ",";
                userString += (user.isMarried() ? "Married" : "Single") + ",";
                userString += user.getPassword() + ",";
                userString += user.getAccountType();
                bw.write(userString);
                bw.newLine();
                System.out.println("Writing"); //TODO: REMOVE DEBUG IDENTIFIER.
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public static User getUser(int id)
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

    public boolean resetPassword(String username, String currentPassword, String newPassword) {
        if(!Users.containsKey(username)) {
            return false;
        }
        User user = Users.get(username);
        if(user.changePassword(currentPassword, newPassword)){
            return true;
        }
        return false;
    }
}
