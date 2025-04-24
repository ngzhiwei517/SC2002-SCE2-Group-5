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
    Manager getManager(int id);
    Officer getOfficer(int id);
    User getUser(int id);
}

