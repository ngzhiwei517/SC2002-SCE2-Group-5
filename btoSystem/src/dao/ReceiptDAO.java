package dao;

import entity.Receipt;

import java.util.HashMap;

public interface ReceiptDAO {
    HashMap<Integer, Receipt> get();
    boolean read();
    boolean write();
    boolean add(Receipt receipt);
    boolean remove(Receipt receipt);
}
