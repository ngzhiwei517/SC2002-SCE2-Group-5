package dao;

import entity.Applicant;
import entity.Manager;
import entity.Officer;
import entity.User;

import java.io.*;
import java.util.HashMap;

public class UserCSVDAO implements UserDAO {
    private HashMap<String, User> users = new HashMap<>();
    private final String path = "ApplicantList.csv";

    @Override
    public HashMap<String, User> get() {
        return users;
    }

    @Override
    public boolean read() {
        //process applicants
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                //System.out.println(line); //TODO: REMOVE DEBUG LINE
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
                        users.put(nric, newUser);
                    }
                    break;
                    case "Officer":
                    {
                        User newUser = new Officer(id, name, nric, age, isMarried, password);
                        users.put(nric, newUser);
                    }
                    break;
                    case "Manager":
                    {
                        User newUser = new Manager(id, name, nric, age, isMarried, password);
                        users.put(nric, newUser);
                    }
                    break;
                }
            }
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean write() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))){
            bw.write("u.id,name,nric,age,marital_status,password,account_type");
            bw.newLine();
            for(String key : users.keySet()) {
                User user = users.get(key);
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
                //System.out.println("Writing"); //TODO: REMOVE DEBUG IDENTIFIER.
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean add(User user) {
        if(!users.containsKey(user.getNric())) {
            users.put(user.getName(), user);
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(User user) {
        return false; //unimplemented and not needed.
    }

    @Override
    public Manager getManager(int id)
    {
        for(String key : users.keySet())
        {
            if(users.get(key).getID() == id && users.get(key) instanceof Manager)
            {
                return (Manager) users.get(key);
            }
        }
        return null;
    }

    @Override
    public Officer getOfficer(int id)
    {
        for(String key : users.keySet())
        {
            if(users.get(key).getID() == id && users.get(key) instanceof Officer)
            {
                return (Officer) users.get(key);
            }
        }
        return null;
    }

    @Override
    public User getUser(int id)
    {
        for(User user: users.values())
        {
            if(user.getID() == id)
                return user;
        }
        return null;
    }
}
