package entity;



public class Flat {
    public enum Type
    {
        TwoRoom,
        ThreeRoom,
    }

    private Type type;
    private int units;
    private int price;

    public Flat(String type, int units, int price) {
        this.type = stringToType(type);
        this.units = units;
        this.price = price;
    }

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
