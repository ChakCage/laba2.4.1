import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class City implements Serializable {
    private final List<Building> buildings; // Список всех зданий в городе
    private final Resources resources; // Ресурсы города

    public City() {
        buildings = new ArrayList<>();
        resources = new Resources(); // Начальное количество ресурсов
    }

    public void addBuilding(Building building) {

        buildings.add(building);
        System.out.println("Здание добавлено: " + building);
    }

    public Building getBuilding(Class<? extends Building> buildingType) {
        for (Building building : buildings) {
            if (buildingType.isInstance(building)) {
                return building;
            }
        }
        return null;
    }



    public List<Building> getBuildings() {
        return new ArrayList<>(buildings); // Возвращаем копию списка зданий
    }

    public Resources getResources() {
        return resources;
    }

    public void displayBuildings() {
        if (buildings.size() > 0) {
            int number = 1;
            System.out.println("Здания в городе:");
            for (Building building : buildings) {
                System.out.println(number + "." + building);
                number++;
            }
        } else {
            System.out.println("-----------------------------------");
            System.out.println("Город пуст");
            System.out.println("-----------------------------------");
        }
    }
}
