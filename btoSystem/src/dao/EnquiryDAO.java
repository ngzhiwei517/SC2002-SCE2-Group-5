package dao;

import entity.Enquiry;

import java.util.HashMap;

public interface EnquiryDAO {
    void injectDAO(UserDAO userDAO, ProjectDAO projectDAO);
    HashMap<Integer, Enquiry> get();
    boolean read();
    boolean write();
    boolean add(Enquiry enquiry);
    boolean remove(Enquiry enquiry);
}