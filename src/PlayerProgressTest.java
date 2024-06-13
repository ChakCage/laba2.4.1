import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;

public class PlayerProgressTest {

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
