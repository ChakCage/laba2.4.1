import java.util.Objects;
import java.io.Serializable;

public class Field implements Serializable {
    private Object occupant; // Объект, находящийся на поле: InfantryUnit или EnemyUnit
    private final String terrainType; // Тип местности: '*', '#', '@', '!'
    private final double penalty; // Штраф за прохождение поля

    public Field(String terrainType, double penalty) {
        this.terrainType = terrainType;
        this.penalty = penalty;
    }

    @Override
    public String toString() {
        return String.valueOf(terrainType);
    }

    public String getTerrainType() {
        return terrainType;
    }


    public void setOccupant(Object occupant) {
        this.occupant = occupant;
    }

    public Object getOccupant() {
        return occupant;
    }

    public boolean hasUnit() {
        return occupant != null;
    }

    public Unit getUnit() {
        if (hasUnit()) {
            return (Unit) occupant;
        } else {
            return null;
        }
    }

    public double getPenaltys() {
        return penalty;
    }


    public double getPenalty(InfantryUnit unit) {
        switch (unit.getUnitNumber()) {
            case 1:
            case 2:
                if (Objects.equals(terrainType, "♨️")) {
                    return penalty * 1.5;
                } else if (Objects.equals(terrainType, "\uD83C\uDF33")) {
                    return penalty * 1.2;
                } else if (Objects.equals(terrainType, "\uD83D\uDDFB")) {
                    return penalty * 1.5;
                }
                break;
            case 3:
            case 4:
                if (Objects.equals(terrainType, "♨️")) {
                    return penalty * 1.8;
                } else if (Objects.equals(terrainType, "\uD83C\uDF33")) {
                    return penalty * 1.0;
                } else if (Objects.equals(terrainType, "\uD83D\uDDFB")) {
                    return penalty * 2.2;
                }
                break;
            case 5:
            case 6:
                if (Objects.equals(terrainType, "♨️")) {
                    return penalty * 2.2;
                } else if (Objects.equals(terrainType, "\uD83C\uDF33")) {
                    return penalty * 1.5;
                } else if (Objects.equals(terrainType, "\uD83D\uDDFB")) {
                    return penalty * 1.2;
                }
                break;
        }
        return penalty;
    }
}
