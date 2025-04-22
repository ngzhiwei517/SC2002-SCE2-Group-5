package dao;

import entity.Enquiry;

import java.util.HashMap;
import interfaces.Reader;
import interfaces.Writer;

public interface EnquiryDAO extends Reader, Writer{
    void injectDAO(UserDAO userDAO, ProjectDAO projectDAO);
    HashMap<Integer, Enquiry> get();
    boolean read();
    boolean write();
    boolean add(Enquiry enquiry);
    boolean remove(Enquiry enquiry);
}