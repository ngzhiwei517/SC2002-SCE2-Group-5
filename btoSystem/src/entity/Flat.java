package entity;


import javax.xml.crypto.dsig.spec.HMACParameterSpec;

public class Flat {
    public enum Type
    {
        TwoRoom,
        ThreeRoom,
    }

    private static int next_id = 0;
    private int id;
    private Project project;
    private Type type;
    private int units;
    private int price;

    public Flat(int id, Project project, String type, int units, int price) {
        this.id = id;
        if(id >= next_id) //ensure new generated ids are unique
        {
            next_id = id+1;
        }
        this.project = project;
        this.type = stringToType(type);
        this.units = units;
        this.price = price;
    }

    public Flat(String type, int units, int price) {
        this.id = next_id++;
        this.type = stringToType(type);
        this.units = units;
        this.price = price;
    }

    public Project getProject() { return project; }

    public void print()
    {
        System.out.println(this.getType() + ", " + this.getUnits() + ", " + this.getPrice());
    }

    public int getId() { return id;}

    public Type getType() { return type; }

    public int getUnits() { return units; }

    public int getPrice() { return price; }

    public Type stringToType(String strType)
    {
        switch (strType)
        {
            case "2-Room":
                return Type.TwoRoom;
            case "3-Room":
                return Type.ThreeRoom;
        }
        return null;
    }
}
