import java.io.Serializable;
public class Resources implements Serializable{
    private int wood;
    private int stone;

    public Resources() {
        this.wood = 300; // Начальное количество дерева
        this.stone = 300; // Начальное количество камня
    }

    public int getWood() {
        return wood;
    }

    public void addWood(int amount) {
        wood += amount;
    }

    public void removeWood(int amount) {
        wood -= amount;
    }

    public int getStone() {
        return stone;
    }

    public void addStone(int amount) {
        stone += amount;
    }

    public void removeStone(int amount) {
        stone -= amount;
    }

    public boolean consume(int stoneCost, int woodCost) {
        if (stone >= stoneCost && wood >= woodCost) {
            stone -= stoneCost;
            wood -= woodCost;
            return true;
        } else {
            return false;
        }
    }
}
