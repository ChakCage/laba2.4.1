import java.io.*;
import java.util.*;

public class MapEditor implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<List<Field>> mapList;
    private Map<String, Double> availableObstacles;
    private static final String OBSTACLES_FILE = "obstacles.txt";
    private static final String UNIQUE_OBSTACLES_FILE = "unique_obstacles.txt";
    private static String currentMapFileName;


    public MapEditor() {
        this.mapList = new ArrayList<>();
        this.availableObstacles = new HashMap<>();

        // Инициализируем стандартные препятствия и их штрафы
        loadObstacles();
    }
    public void setCurrentMapFileName(String mapFileName) {
        this.currentMapFileName = mapFileName;
    }

    public List<List<Field>> getMapList() {
        return mapList;
    }

    private static class AppendingObjectOutputStream extends ObjectOutputStream {
        public AppendingObjectOutputStream(OutputStream out) throws IOException {
            super(out);
        }

        @Override
        protected void writeStreamHeader() throws IOException {
            reset(); // Избегаем повторного заголовка
        }
    }

    private void loadObstacles() {
        // Загрузка общих препятствий
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(OBSTACLES_FILE))) {
            availableObstacles = (Map<String, Double>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            availableObstacles.put("\uD83C\uDF33", 1.0); // Дерево
            availableObstacles.put("♨️", 1.0); // Болото
            availableObstacles.put("\uD83D\uDDFB", 1.0); // Холм
            saveCommonObstacles(); // Сохраняем стандартные препятствия, если файл не найден
        }

        // Загрузка уникальных препятствий
        File uniqueObstaclesFile = new File(UNIQUE_OBSTACLES_FILE);
        if (uniqueObstaclesFile.exists() && uniqueObstaclesFile.length() > 0) {
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(uniqueObstaclesFile))) {
                while (true) {
                    try {
                        Object obj = inputStream.readObject();
                        if (obj instanceof Map) {
                            Map<String, Object> uniqueObstacle = (Map<String, Object>) obj;
                            String owner = (String) uniqueObstacle.get("Owner");
                            String symbol = (String) uniqueObstacle.get("Symbol");
                            Double penalty = (Double) uniqueObstacle.get("Penalty");
                            if (owner != null && owner.equals(currentMapFileName) && symbol != null && penalty != null) {
                                availableObstacles.put(symbol, penalty);
                            }
                        }
                    } catch (EOFException eofException) {
                        break; // Конец файла
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }






    private void saveCommonObstacles() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(OBSTACLES_FILE))) {
            outputStream.writeObject(availableObstacles);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void appendUniqueObstacle(Map<String, Object> uniqueObstacle) {
        ensureFileHasHeader();
        try (AppendingObjectOutputStream outputStream = new AppendingObjectOutputStream(
                new FileOutputStream(UNIQUE_OBSTACLES_FILE, true))) {
            outputStream.writeObject(uniqueObstacle);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveUniqueObstacles() {
        // Сохранение уникальных препятствий в файл unique_obstacles.txt
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(UNIQUE_OBSTACLES_FILE))) {
            outputStream.writeObject(availableObstacles); // Обеспечить перезапись файла
        } catch (IOException e) {
            e.printStackTrace();
        }
    }









    // Метод для создания карты с указанным размером
    public void createMap(int size) {
        for (int i = 0; i < size; i++) {
            List<Field> row = new ArrayList<>();
            for (int j = 0; j < size; j++) {
                row.add(new Field("\uD83C\uDFD5️", 1.0)); // Используем класс Field с дефолтным символом и штрафом
            }
            mapList.add(row);
        }
    }

    // Метод для отображения карты
    public void displayMap() {
        for (List<Field> row : mapList) {
            for (Field field : row) {
                System.out.print(field.getTerrainType() + " "); // Используем метод Field для вывода символа
            }
            System.out.println();
        }
    }


    // Метод для добавления препятствия на карту по указанным координатам
    public void addObstacle(int y, int x, String obstacle) {
        if (isValidPosition(x, y)) {
            double penalty = availableObstacles.getOrDefault(obstacle, 1.0); // Получаем штраф из карты препятствий
            mapList.get(x).set(y, new Field(obstacle, penalty)); // Создаем новое поле Field
        } else {
            System.out.println("Недопустимые координаты.");
        }
    }


    // Метод для добавления нового типа препятствия и его штрафа
    public void addCustomObstacle(String obstacleSymbol, Double penalty, boolean addToAllMaps) {
        if (addToAllMaps) {
            availableObstacles.put(obstacleSymbol, penalty); // Добавляем к общим препятствиям
            saveCommonObstacles(); // Сохраняем общий список препятствий
        } else {
            Map<String, Object> uniqueObstacle = new HashMap<>();
            uniqueObstacle.put("Symbol", obstacleSymbol);   // Символ препятствия
            uniqueObstacle.put("Penalty", penalty);         // Штраф
            if (currentMapFileName != null) {
                uniqueObstacle.put("Owner", currentMapFileName); // Владелец
            } else {
                System.out.println("Предупреждение: имя текущей карты не установлено. Добавление без владельца.");
            }

            appendUniqueObstacle(uniqueObstacle); // Дозапись уникального препятствия
            availableObstacles.put(obstacleSymbol, penalty); // Добавляем в общий список
        }
    }









    // Метод для удаления препятствия с карты по указанным координатам
    // Метод для удаления препятствия с поля
    public void removeObstacle(Scanner scanner) {
        System.out.println("Список препятствий на карте:");
        int count = 1;
        for (int i = 0; i < mapList.size(); i++) {
            for (int j = 0; j < mapList.get(i).size(); j++) {
                Field field = mapList.get(i).get(j);
                String obstacle = field.getTerrainType(); // Получаем символ препятствия
                if (!obstacle.equals("\uD83C\uDFD5️")) {
                    Double penalty = field.getPenaltys(); // Получаем штраф препятствия
                    System.out.println(count++ + ". Препятствие: " + obstacle + ", Штраф: " + penalty + ", Координаты: (" + j + ", " + i + ")");
                }
            }
        }

        System.out.print("Выберите номер препятствия для удаления: ");
        int obstacleChoice = scanner.nextInt();
        scanner.nextLine();

        int currentCount = 1;
        for (int i = 0; i < mapList.size(); i++) {
            for (int j = 0; j < mapList.get(i).size(); j++) {
                Field field = mapList.get(i).get(j);
                if (!field.getTerrainType().equals("\uD83C\uDFD5️")) {
                    if (currentCount == obstacleChoice) {
                        mapList.get(i).set(j, new Field("\uD83C\uDFD5️", 1.0)); // Сброс поля в значение по умолчанию
                        System.out.println("Препятствие успешно удалено.");
                        return;
                    }
                    currentCount++;
                }
            }
        }

        System.out.println("Некорректный выбор препятствия.");
    }



    // Метод для сохранения объекта MapEditor в файл
    public void saveMapToFile(String filename) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(formatFilename(filename)))) {
            outputStream.writeObject(mapList);
            System.out.println("Карта сохранена в файл " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMapList(List<List<Field>> mapList) {
        this.mapList = mapList;
    }

    // Метод для загрузки карты из файла
    public  static List<List<Field>> loadMapFromFile(String filename) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(formatFilename(filename)))) {
            return (List<List<Field>>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Метод для проверки допустимости позиции на карте
    private boolean isValidPosition(int x, int y) {
        return x >= 0 && x < mapList.size() && y >= 0 && y < mapList.get(0).size();
    }

    // Метод для удаления карты
    public void deleteMap(Scanner scanner) {
        if (mapList.isEmpty()) {
            System.out.println("Список карт пуст.");
            return;
        }

        System.out.println("Список карт для удаления:");
        for (int i = 0; i < mapList.size(); i++) {
            System.out.println((i + 1) + ". Карта " + (i + 1));
        }


        System.out.print("Выберите номер карты для удаления: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice < 1 || choice > mapList.size()) {
            System.out.println("Некорректный выбор карты.");
            return;
        }

        mapList.remove(choice - 1);
        System.out.println("Карта " + choice + " успешно удалена.");
    }



    // Метод для редактирования загруженной карты
    public void editMapMenu(Scanner scanner, String filename) {
        while (true) {
            System.out.println("Редактирование карты:");
            System.out.println("1. Показать карту");
            System.out.println("2. Поставить препятствие на поле");
            System.out.println("3. Создать новое препятствие");
            System.out.println("4. Удалить препятствие с карты");
            System.out.println("5. Показать доступные препятствия");
            System.out.println("6. Выйти из редактирования и сохранить изменения");

            System.out.print("Выберите действие: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    displayMap();
                    break;
                case 2:
                    addObstacleMenu(scanner);
                    break;
                case 3:
                    addCustomObstacleMenu(scanner);
                    break;
                case 4:
                    System.out.println("Меню удаления препятствия:");
                    System.out.println("1. Удалить препятствие с текущей карты");
                    System.out.println("2. Удалить препятствие из списка препятствий");
                    System.out.print("Выберите действие: ");
                    int removeChoice = scanner.nextInt();
                    switch (removeChoice) {
                        case 1:
                            removeObstacle(scanner);
                            break;
                        case 2:
                            removeObstacleMenu(scanner);
                            break;
                        default:
                            System.out.println("Некорректный выбор.");
                            break;
                    }
                    break;
                case 5:
                    displayAvailableObstacles();
                    break;
                case 6:
                    System.out.println("Сохранение изменений...");
                    saveMapToFile(filename); // Сохраняем изменения в файл
                    System.out.println("Изменения сохранены.");
                    System.out.println("Выход из редактирования.");
                    return;
                default:
                    System.out.println("Неверный выбор. Пожалуйста, попробуйте еще раз.");
                    break;
            }
        }
    }

    // Метод для добавления препятствия через меню
    public void addObstacleMenu(Scanner scanner) {
        System.out.print("Введите координаты X и Y для препятствия: ");
        int x = scanner.nextInt();
        int y = scanner.nextInt();

        System.out.println("Доступные препятствия:");
        int count = 1;
        for (Map.Entry<String, Double> entry : availableObstacles.entrySet()) {
            System.out.println(count++ + ". " + entry.getKey() + " (Штраф: " + entry.getValue() + ")");
        }

        System.out.print("Выберите номер препятствия: ");
        int obstacleChoice = scanner.nextInt();

        if (obstacleChoice >= 1 && obstacleChoice <= availableObstacles.size()) {
            String selectedObstacle = "";
            count = 1;
            for (Map.Entry<String, Double> entry : availableObstacles.entrySet()) {
                if (count++ == obstacleChoice) {
                    selectedObstacle = entry.getKey();
                    break;
                }
            }
            addObstacle(x, y, selectedObstacle);
        } else {
            System.out.println("Неправильный выбор.");
        }
    }



    // Метод для добавления пользовательского препятствия через меню
    public void addCustomObstacleMenu(Scanner scanner) {
        System.out.print("Введите символ нового препятствия: ");
        String obstacleSymbol = scanner.next();
        System.out.print("Введите штраф за препятствие: ");
        double penalty = scanner.nextDouble();

        System.out.println("Добавить препятствие на все карты или только на текущую карту?");
        System.out.println("1. На все карты");
        System.out.println("2. Только на текущую карту");
        System.out.print("Выберите вариант: ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                addCustomObstacle(obstacleSymbol, penalty, true);
                break;
            case 2:
                addCustomObstacle(obstacleSymbol, penalty, false);
                break;
            default:
                System.out.println("Неправильный выбор.");
                break;
        }
    }




    // Метод для удаления препятствия через меню
    public void removeObstacleMenu(Scanner scanner) {
        System.out.println("Список препятствий на карте:");
        int count = 1;
        for (Map.Entry<String, Double> entry : availableObstacles.entrySet()) {
            System.out.println(count++ + ". Препятствие: " + entry.getKey() + ", Штраф: " + entry.getValue());
        }
        System.out.print("Выберите номер препятствия для удаления: ");
        int obstacleChoice = scanner.nextInt();
        if (obstacleChoice >= 1 && obstacleChoice <= availableObstacles.size()) {
            String[] obstacles = availableObstacles.keySet().toArray(new String[0]);
            String obstacleToRemove = obstacles[obstacleChoice - 1];

            // Удаляем препятствие только с текущей карты из списка доступных для карт
            availableObstacles.remove(obstacleToRemove);


            // Удаляем препятствие из файла с препятствиями
            removeObstacleFromFile(obstacleToRemove);

            System.out.println("Препятствие успешно удалено.");
        } else {
            System.out.println("Некорректный выбор препятствия.");
        }
    }

    // Метод для удаления препятствия из файла с препятствиями


    private void removeObstacleFromFile(String obstacleToRemove) {
        try {
            // Удаление из файла общих препятств
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(OBSTACLES_FILE))) {
                Map<String, Double> commonObstacles = (Map<String, Double>) inputStream.readObject();
                commonObstacles.remove(obstacleToRemove);
                saveCommonObstacles();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            // Удаление из файла уникальных препятств
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(UNIQUE_OBSTACLES_FILE))) {
                Map<String, Double> uniqueObstacles = (Map<String, Double>) inputStream.readObject();
                uniqueObstacles.remove(obstacleToRemove);
                saveUniqueObstacles();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    // Метод для отображения доступных препятствий
    public void displayAvailableObstacles() {
        System.out.println("Доступные препятствия:");
        int count = 1;

        // Чтение файла с общими препятствиями, с проверкой существования файла
        File obstaclesFile = new File(OBSTACLES_FILE);
        if (obstaclesFile.exists() && obstaclesFile.length() > 0) {
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(obstaclesFile))) {
                Map<String, Double> obstaclePenalties = (Map<String, Double>) inputStream.readObject();
                for (Map.Entry<String, Double> entry : obstaclePenalties.entrySet()) {
                    String obstacle = entry.getKey();
                    System.out.println(count++ + ". " + obstacle + " (Штраф: " + entry.getValue() + ", Общее препятствие)");
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Общие препятствия не найдены.");
        }

        // Чтение файла с уникальными препятствиями, с проверкой существования файла
        File uniqueObstaclesFile = new File(UNIQUE_OBSTACLES_FILE);
        if (uniqueObstaclesFile.exists() && uniqueObstaclesFile.length() > 0) {
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(uniqueObstaclesFile))) {
                Object obj;
                while (true) {
                    try {
                        obj = inputStream.readObject();
                        if (obj == null) {
                            continue;
                        }

                        if (obj instanceof Map) {
                            Map<String, Object> uniqueObstacle = (Map<String, Object>) obj;
                            String obstacle = (String) uniqueObstacle.get("Symbol");
                            String owner = (String) uniqueObstacle.get("Owner");
                            Double penalty = (Double) uniqueObstacle.get("Penalty");

                            if (obstacle != null && penalty != null && owner != null && owner.equals(currentMapFileName)) {
                                System.out.println(count++ + ". " + obstacle + " (Штраф: " + penalty + ", Уникальное препятствие этой карты)");
                            }
                        }
                    } catch (EOFException eofException) {
                        break; // Достигнут конец файла
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Файл с уникальными препятствиями не найден.");
        }
    }



    private void ensureFileHasHeader() {
        File uniqueObstaclesFile = new File(UNIQUE_OBSTACLES_FILE);
        if (!uniqueObstaclesFile.exists() || uniqueObstaclesFile.length() == 0) {
            try (ObjectOutputStream outputStream = new ObjectOutputStream(
                    new FileOutputStream(UNIQUE_OBSTACLES_FILE))) {
                // Создаем пустой заголовок, чтобы избежать StreamCorruptedException
                outputStream.writeObject(new HashMap<String, Object>());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }





    // Метод для отображения существующих карт
    public void displayExistingMaps() {
        System.out.println("Доступные карты для редактирования:");
        File directory = new File(".");
        File[] files = directory.listFiles((dir, name) -> name.startsWith("map_") && name.endsWith(".txt"));
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                System.out.println((i + 1) + ". " + files[i].getName());
            }
        }
    }

    // Метод для форматирования имени файла
    private static String formatFilename(String filename) {
        if (!filename.endsWith(".txt")) {
            filename += ".txt";
        }
        return filename;
    }

    public void displayAvailableObstaclesMain(Scanner scanner) {
        System.out.println("1. Отобразить только препятствия доступные для постановки");
        System.out.println("2. Отобразить препятствия всех карт (общие и уникальные)");

        System.out.print("Выберите опцию: ");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                displayCommonObstacles();
                break;
            case 2:
                displayAllObstacles();
                break;
            default:
                System.out.println("Некорректный выбор.");
                break;
        }
    }

    private void displayCommonObstacles() {
        System.out.println("Общие препятствия:");
        int count = 1;

        // Отображение общих препятствий из файла obstacles
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(OBSTACLES_FILE))) {
            // Проверяем, что прочитанный объект - карта
            Object obj = inputStream.readObject();
            if (obj instanceof Map) {
                Map<String, Double> commonObstacles = (Map<String, Double>) obj;
                // Итерируем по общим препятствиям и отображаем их
                for (Map.Entry<String, Double> entry : commonObstacles.entrySet()) {
                    System.out.println(count++ + ". " + entry.getKey() + " (Штраф: " + entry.getValue() + ", Общее препятствие)");
                }
            } else {
                System.out.println("Не удалось прочитать общие препятствия.");
            }
        } catch (EOFException eofException) {
            System.out.println("Файл общих препятствий пуст или достигнут конец файла.");
        } catch (FileNotFoundException e) {
            System.out.println("Файл общих препятствий не найден.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Отображение уникальных препятствий с Owner = NULL
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(UNIQUE_OBSTACLES_FILE))) {
            Object obj;

            while (true) {
                try {
                    obj = inputStream.readObject(); // Читаем объект
                    if (obj == null) {
                        continue; // Пропускаем пустые объекты
                    }

                    if (obj instanceof Map) { // Проверяем, что объект - карта
                        Map<String, Object> uniqueObstacle = (Map<String, Object>) obj;
                        String owner = (String) uniqueObstacle.get("Owner");

                        if (owner == null) { // Если владелец - NULL, это уникальное препятствие
                            String obstacle = (String) uniqueObstacle.get("Symbol");
                            Double penalty = (Double) uniqueObstacle.get("Penalty");
                            if (obstacle != null && penalty != null) {
                                System.out.println(count++ + ". " + obstacle + " (Штраф: " + penalty + ", Уникальное препятствие этой карты)");
                            }
                        }
                    }
                } catch (EOFException eofException) {
                    break; // Достигнут конец файла, выходим из цикла
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл уникальных препятствий не найден.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }




    private void displayAllObstacles() {
        System.out.println("Все препятствия:");
        displayCommonObstacles(); // Отображаем общие препятствия
        int count = availableObstacles.size() + 1; // Инициализируем с учетом общего количества препятствий

        // Отображаем уникальные препятствия с указанием их принадлежности к картам
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(UNIQUE_OBSTACLES_FILE))) {
            Object obj;

            // Считываем уникальные препятствия из файла
            while (true) { // Используем цикл while(true), чтобы избежать ошибок
                try {
                    obj = inputStream.readObject(); // Читаем объект
                    if (obj == null) {
                        continue; // Пропускаем пустые объекты
                    }

                    if (obj instanceof Map) { // Проверяем, что объект - карта
                        Map<String, Object> uniqueObstacle = (Map<String, Object>) obj;
                        String obstacle = (String) uniqueObstacle.get("Symbol");
                        String owner = (String) uniqueObstacle.get("Owner");
                        Double penalty = (Double) uniqueObstacle.get("Penalty");

                        if (obstacle != null && penalty != null) { // Убедимся, что препятствие и штраф не null
                            if (owner != null) { // Проверяем, есть ли владелец
                                System.out.println(count++ + ". " + obstacle + " (Штраф: " + penalty + ", Уникальное препятствие, уникально для " + owner + ")");
                            }
                        }
                    }
                } catch (EOFException eofException) {
                    break; // Достигнут конец файла, выходим из цикла
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл уникальных препятствий не найден."); // Обработка отсутствия файла
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace(); // Обработка ошибок ввода-вывода или ошибки класса
        }
    }


    public void updateUniqueObstaclesOwner(String mapName) {
        List<Map<String, Object>> updatedObstacles = new ArrayList<>();
        File file = new File(UNIQUE_OBSTACLES_FILE);

        // Читаем существующие уникальные препятствия, если файл существует
        if (file.exists() && file.length() > 0) {
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
                while (true) {
                    try {
                        Object obj = inputStream.readObject(); // Корректное считывание данных
                        if (obj instanceof Map) {
                            Map<String, Object> uniqueObstacle = (Map<String, Object>) obj;
                            if (uniqueObstacle.get("Owner") == null) {
                                uniqueObstacle.put("Owner", mapName);
                            }
                            updatedObstacles.add(uniqueObstacle);
                        }
                    } catch (EOFException eofException) {
                        break; // Конец файла
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        // Перезапись файла с обновленными уникальными препятствиями
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(UNIQUE_OBSTACLES_FILE))) {
            for (Map<String, Object> obstacle : updatedObstacles) {
                outputStream.writeObject(obstacle);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }






}
