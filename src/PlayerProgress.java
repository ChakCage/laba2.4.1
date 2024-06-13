import java.io.*;
import java.util.*;

public class PlayerProgress implements Serializable {
    private City city;

    public PlayerProgress() {
        this.city = new City(); // Новый город при начале игры
    }

    public City getCity() {
        return city;
    }

    public void saveProgress(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(this); // Сохранение прогресса
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PlayerProgress loadProgress(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (PlayerProgress) ois.readObject(); // Загрузка прогресса
        } catch (IOException | ClassNotFoundException e) {
            return new PlayerProgress(); // Если ошибка, возвращаем новый прогресс
        }
    }
}
