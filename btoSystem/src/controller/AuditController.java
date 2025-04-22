package controller;

import entity.AuditLog;
import interfaces.Reader;
import interfaces.Writer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AuditController {
    private HashMap<Integer, AuditLog> auditlogs = new HashMap<>();

    public boolean init()
    {
        return true;
    }

    public boolean exit()
    {
        return true;
    }

    private boolean addLog()
    {
        return false;
    }


}
