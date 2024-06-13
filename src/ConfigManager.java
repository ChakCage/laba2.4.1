import java.io.IOException;
import java.io.*;

public class ConfigManager {
    private static final String CONFIG_FILE = "config.txt";

    // Метод загрузки последней выбранной карты
    public static String loadLastSelectedMap() {
        File configFile = new File(CONFIG_FILE);
        if (!configFile.exists()) {
            createDefaultConfig(); // Если файла нет, создаем его
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(CONFIG_FILE))) {
            return (String) ois.readObject(); // Десериализация имени карты
        } catch (IOException | ClassNotFoundException e) {
            return "map_default.txt"; // Если ошибка, возвращаем дефолтную карту
        }
    }

    // Метод сохранения имени последней выбранной карты
    public static void saveLastSelectedMap(String mapName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CONFIG_FILE))) {
            oos.writeObject(mapName); // Сериализация имени карты
            System.out.println("Последняя выбранная карта сохранена: " + mapName);
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении последней выбранной карты.");
            e.printStackTrace();
        }
    }

    // Метод создания файла конфигурации с дефолтной картой
    private static void createDefaultConfig() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CONFIG_FILE))) {
            oos.writeObject("map_default.txt"); // Сохранение дефолтной карты
            System.out.println("Файл конфигурации создан с дефолтной картой.");
        } catch (IOException e) {
            System.out.println("Ошибка при создании файла конфигурации.");
            e.printStackTrace();
        }
    }
}

