package dao;

import entity.Manager;
import entity.Officer;
import entity.User;

import java.util.HashMap;

public interface UserDAO {
    HashMap<String, User> get(); // keyed by NRIC/Username
    boolean read();
    boolean write();
    boolean add(User user);
    boolean remove(User user);
    public Manager getManager(int id);
    public Officer getOfficer(int id);
    public User getUser(int id);

}

