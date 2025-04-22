package dao;

import entity.Receipt;

import java.util.HashMap;
import interfaces.Reader;
import interfaces.Writer;

public interface ReceiptDAO extends Reader, Writer{
    HashMap<Integer, Receipt> get();
    boolean read();
    boolean write();
    boolean add(Receipt receipt);
    boolean remove(Receipt receipt);
}
