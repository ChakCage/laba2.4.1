import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class MapEditorTest {
    private MapEditor mapEditor;

    @BeforeEach
    void setUp() {
        mapEditor = new MapEditor();
        mapEditor.createMap(9);
    }

    @Test
    void testCreateMap() {
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
        String obstacle = "\uD83C\uDF33"; // дерево
        mapEditor.addObstacle(5, 5, obstacle);

        Field field = mapEditor.getMapList().get(5).get(5);
        assertEquals(obstacle, field.getTerrainType());
        assertEquals(1.0, field.getPenaltys());
    }

    @Test
    void testEditMap() {
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
