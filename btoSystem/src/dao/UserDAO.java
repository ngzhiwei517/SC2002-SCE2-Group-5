package dao;

import entity.Manager;
import entity.Officer;
import entity.User;
import interfaces.Reader;
import interfaces.Writer;

import java.util.HashMap;

public interface UserDAO extends Reader, Writer {
    HashMap<String, User> get(); // keyed by NRIC/Username
    boolean read();
    boolean write();
    boolean add(User user);
    boolean remove(User user);
    public Manager getManager(int id);
    public Officer getOfficer(int id);
    public User getUser(int id);

}

