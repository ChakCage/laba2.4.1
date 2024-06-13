import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.List;
import java.util.Scanner;

public class JunitTest {
    @Nested
    class MapEditorTest {

        @Test
        void testCreateMap() {
            MapEditor mapEditor = new MapEditor();
            mapEditor.createMap(9);

            List<List<Field>> mapList = mapEditor.getMapList();
            assertNotNull(mapList);
            assertEquals(9, mapList.size());

            for (List<Field> row : mapList) {
                assertEquals(9, row.size());
                for (Field field : row) {
                    assertEquals("\uD83C\uDFD5️", field.getTerrainType());
                    assertEquals(1.0, field.getPenaltys());
                }
            }
        }

        @Test
        void testAddObstacle() {
            MapEditor mapEditor = new MapEditor();
            mapEditor.createMap(9);

            String obstacle = "\uD83C\uDF33"; // дерево
            mapEditor.addObstacle(5, 5, obstacle);

            Field field = mapEditor.getMapList().get(5).get(5);
            assertEquals(obstacle, field.getTerrainType());
            assertEquals(1.0, field.getPenaltys());
        }

        @Test
        void testEditMap() {
            MapEditor mapEditor = new MapEditor();
            mapEditor.createMap(9);

            String obstacle = "\uD83C\uDF33"; // дерево
            mapEditor.addObstacle(5, 5, obstacle);

            // Удаляем препятствие
            mapEditor.addObstacle(5, 5, "\uD83C\uDFD5️");

            Field field = mapEditor.getMapList().get(5).get(5);
            assertEquals("\uD83C\uDFD5️", field.getTerrainType());
            assertEquals(1.0, field.getPenaltys());
        }

        @Test
        void testLoadMapFromFile() {
            MapEditor mapEditor = new MapEditor();
            mapEditor.createMap(9);
            String filename = "testMap";

            mapEditor.saveMapToFile(filename);

            List<List<Field>> loadedMap = MapEditor.loadMapFromFile(filename);
            assertNotNull(loadedMap);
            assertEquals(9, loadedMap.size());

            for (List<Field> row : loadedMap) {
                assertEquals(9, row.size());
                for (Field field : row) {
                    assertEquals("\uD83C\uDFD5️", field.getTerrainType());
                    assertEquals(1.0, field.getPenaltys());
                }
            }
        }

    }

    @Nested
    class BuildingLevelUpTest {

        @Test
        void testLeechHouseLevelUp() {
            City city = new City();
            Buildings.LeechHouse leechHouse = new Buildings.LeechHouse();
            city.addBuilding(leechHouse);
            leechHouse.upgrade(city.getResources());
            assertEquals(2, leechHouse.getLevel());
        }

        @Test
        void testForgeLevelUp() {
            City city = new City();
            Buildings.Forge forge = new Buildings.Forge();
            city.addBuilding(forge);
            forge.upgrade(city.getResources());
            assertEquals(2, forge.getLevel());
        }

        @Test
        void testArmoryLevelUp() {
            City city = new City();
            Buildings.Armory armory = new Buildings.Armory();
            city.addBuilding(armory);
            armory.upgrade(city.getResources());
            assertEquals(2, armory.getLevel());
        }
    }

    @Nested
    class SpecialBuildingsTest {

        @Test
        void testAcademyCreation() {
            City city = new City();
            Buildings.Academy academy = new Buildings.Academy();
            city.addBuilding(academy);
            assertNotNull(academy);
            Resources resources = city.getResources();
            String input = "Тестовик\n1\n1\n1\n1\n1\n1\n1\n\uD83D\uDE43\nAEZAKMI";  // 1 для выбора обмена дерева на камень и 50 как количество дерева
            ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
            Scanner scanner = new Scanner(in);
            academy.createNewUnit(scanner, resources);
        }

        @Test
        void testMarketTrade() {
            City city = new City();
            Buildings.Market market = new Buildings.Market();
            city.addBuilding(market);
            Resources resources = city.getResources();

            // Задаем ввод для обмена дерева на камень
            String input = "1\n300\n";  // 1 для выбора обмена дерева на камень и 50 как количество дерева
            ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
            Scanner scanner = new Scanner(in);

            market.trade(resources, scanner);

            // Проверяем, что дерево было обменено на камень
            assertTrue(resources.getWood() < 300);
            assertTrue(resources.getStone() > 0);
        }

        @Test
        void testTavernMoveBonus() {
            Buildings.Tavern tavern = new Buildings.Tavern();
            tavern.setMoveBonus(5);
            assertEquals(5, tavern.getMoveBonus());
        }
    }

    @Nested
    class PlayerProgressTest {

        @Test
        void testSaveAndLoadProgress() {
            String filename = "test_save.dat";
            PlayerProgress progress = new PlayerProgress();
            City city = progress.getCity();
            Buildings.Forge forge = new Buildings.Forge();
            city.addBuilding(forge);
            progress.saveProgress(filename);

            PlayerProgress loadedProgress = PlayerProgress.loadProgress(filename);
            City loadedCity = loadedProgress.getCity();
            assertNotNull(loadedCity.getBuilding(Buildings.Forge.class));
            new File(filename).delete(); // Удаляем тестовый файл после проверки
        }
    }



}
