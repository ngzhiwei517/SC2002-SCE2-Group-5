package entity;


import javax.xml.crypto.dsig.spec.HMACParameterSpec;

public class Flat {
    public enum Type
    {
        TwoRoom,
        ThreeRoom,
    }

    private static int next_id = 0;
    private final int id;
    private Project project;
    private Type type;
    private float price;
    private int units;


    public Flat(int id, Project project, String type, float price, int units) {
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

    public Flat(Type type, float price, int units) {
        this.id = next_id++;
        this.type = type;
        this.units = units;
        this.price = price;
    }

    public void print()
    {
        System.out.println(this.getType() + ", " + this.getPrice() + ", " + this.getUnits());
    }

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

    public void deleteSelf(){
        if(project.deleteFlat(this)) {
            project = null; }
    }

    public int getId() { return id;}
    public Type getType() { return type; }
    public int getUnits() { return units; }
    public float getPrice() { return price; }
    public Project getProject() { return project; }
    public String getStringType() {
        if(type == Type.TwoRoom)
            return "2-Room";
        else if(type == Type.ThreeRoom)
            return "3-Room";
        return null;
    }

    public void setType(Type type) { this.type = type; }
    public void setPrice(float price) { this.price = price; }
    public void setUnits(int units) { this.units = units; }
    public void setProject(Project project) { this.project = project; }

    public void book()
    {
        units--;
    }

    public void withdraw()
    {
        units++;
    }

}
