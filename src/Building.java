import java.io.Serializable;
public abstract class Building implements Serializable {
    protected int level; // Уровень здания
    protected String name; // Название здания
    protected int woodCost; // Стоимость в дереве для улучшения
    protected int stoneCost; // Стоимость в камне для улучшения

    public Building(String name, int woodCost, int stoneCost) {
        this.level = 1; // Все здания начинают с первого уровня
        this.name = name;
        this.woodCost = woodCost;
        this.stoneCost = stoneCost;
    }

    public String getName(){
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getStoneCost() {
        return (int) (stoneCost * (1 + (level-1) * 0.1)); // Стоимость постройки или улучшения в камне
    }

    public int getWoodCost() {
        return (int) (woodCost * (1 + (level-1) * 0.1)); // Стоимость постройки или улучшения в дереве
    }

    public void upgrade(Resources resources) {
        if (resources.getWood() >= woodCost && resources.getStone() >= stoneCost) {
            resources.removeWood(woodCost*(1+level/10));
            resources.removeStone(stoneCost*(1+level/10));
            level++;
            System.out.println("Здание " + name + " улучшено до уровня " + level);
        } else {
            System.out.println("Недостаточно ресурсов для улучшения.");
        }
    }

    @Override
    public String toString() {
        return name + " (Уровень: " + level + ")";
    }
}
