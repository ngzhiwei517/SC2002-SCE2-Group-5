package controller;

import entity.AuditLog;
import interfaces.Reader;
import interfaces.Writer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AuditController  implements Reader, Writer {
    private HashMap<Integer, AuditLog> auditlogs = new HashMap<>();

    @Override
    public boolean readData() throws Exception {
        return false;
    }

    @Override
    public boolean writeData() throws Exception {
        return false;
    }


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
