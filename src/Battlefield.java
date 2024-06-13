import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.IOException;
import java.io.File;
import java.io.*;




public class Battlefield {
    private static final Logger logger = Logger.getLogger(Battlefield.class.getName());
    private FileHandler fileHandler;
    private static int size;
    private boolean catapultUsed;
    private double evasionFromCatapult = 0.0;
    public double calculateEvasionFromCatapult(InfantryUnit unit) {
        // Используем switch для установки разных значений уклонения в зависимости от unitNumber
        switch (unit.getUnitNumber()) {
            case 1:
            case 2:
                return evasionFromCatapult + 0.1; // Уклонение для unitNumber 1 и 2
            case 3:
            case 4:
                return evasionFromCatapult + 0.2; // Уклонение для unitNumber 3 и 4
            case 5:
            case 6:
                return evasionFromCatapult + 0.5; // Уклонение для unitNumber 5 и 6
            default:
                return 0.0;
        }
    }

    private Tower enemyTower;



    public Tower getEnemyTower() {
        return enemyTower;
    }

    private static List<List<Field>> grid;
    private List<List<Field>> catapultArea; // Дополнительное поле для катапульты
    private static final String DEFAULT_MAP_FILE = "map_default.txt";
    String lastMap = ConfigManager.loadLastSelectedMap();

    public Battlefield(int size) {
        this.size = size;
        this.grid = new ArrayList<>(size);
        this.catapultArea = new ArrayList<>();
        File mapFile = new File(DEFAULT_MAP_FILE);
        if (mapFile.exists()) {
            // Если файл существует, загрузим его и восстановим поле
            loadMap(lastMap);
        } else {// Если файла нет, создаем начальное поле и сохраняем в файл
            initializeGrid();
            saveGridToFile();
        }
        enemyTower = new Tower();
        initializeCatapultArea();
        try {
            fileHandler = new FileHandler("Move_Log.txt");
            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL);
            logger.setUseParentHandlers(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.setLevel(Level.INFO);
    }

    private void initializeGrid() {
        for (int i = 0; i < size; i++) {
            List<Field> row = new ArrayList<>(size);
            for (int j = 0; j < size; j++) {
                row.add(new Field("\uD83C\uDFD5️", 1.0)); // Инициализация пустой клетки
            }
            grid.add(row);
        }
        // Добавление полей с заданными координатами
        grid.get(1).set(3, new Field("♨️", 1.0)); // Болото
        grid.get(1).set(4, new Field("♨️", 1.0)); // Болото
        grid.get(4).set(4, new Field("\uD83C\uDF33", 1.0)); // Дерево
        grid.get(5).set(6, new Field("\uD83D\uDDFB", 1.0)); // Холм
    }
    private static void saveGridToFile() {
        try (FileOutputStream fos = new FileOutputStream(DEFAULT_MAP_FILE);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(grid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadMap(String mapFileName) {
        File mapFile = new File(mapFileName); // Преобразуем строку в объект File
        try (FileInputStream fis = new FileInputStream(mapFile);
             ObjectInputStream ois = new ObjectInputStream(fis)) { // Исправьте ObjectOutputStream на ObjectInputStream
            grid = (List<List<Field>>) ois.readObject(); // Загружаем карту
            System.out.println("Карта " + mapFileName + " успешно загружена на поле боя."); // Используем имя файла как строку
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ошибка при загрузке карты " + mapFileName); // Исправление вывода ошибок
            e.printStackTrace();
        }
    }


    public static boolean isValidPosition(int x, int y) {
        return x >= 0 && x < size && y >= 0 && y < size;
    }

    public List<Unit> getUnitsAtPosition(int x, int y) {
        List<Unit> units = new ArrayList<>();
        if (isValidPosition(x, y)) {
            Field field = grid.get(x).get(y);
            if (field.hasUnit()) {
                units.add(field.getUnit());
            }
        }
        return units;
    }



    public void placeUnit(int x, int y, InfantryUnit unit) {
        if (isValidPosition(x, y)) {
            Field field = grid.get(x).get(y);
            if (!field.hasUnit()) {
                field.setOccupant(unit);
                unit.setCurrentX(x);
                unit.setCurrentY(y);
            } else {
                System.out.println("Воин уже находится на этой клетке.");
            }
        } else {
            System.out.println("Недопустимые координаты для размещения воина.");
        }
    }

    public int getSize() {
        return size;
    }

    public void display() {
        System.out.println("-----------------------------------");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Field field = grid.get(i).get(j);
                // Проверяем, находится ли текущая клетка в области катапульты
                boolean isCatapultArea = i >= 3 && i <= 4 && j >= 3 && j <= 4;
                // Проверяем, находится ли текущая клетка в области башни
                boolean isTowerArea = i >= 5 && i <= 6 && j >= 3 && j <= 4;
                // Если клетка в области катапульты и нет юнита, выводим катапульту, иначе выводим содержимое клетки
                if (isCatapultArea && !field.hasUnit()) {
                    System.out.print("\uD83D\uDD2B "); // Катапульта
                } else if (isTowerArea && !field.hasUnit()) {
                    System.out.print(enemyTower.getDirectionIcon() + " "); // Башня
                } else if (field.getOccupant() != null) {
                    System.out.print(field.getOccupant().toString() + " ");
                } else {
                    System.out.print(field.getTerrainType() + " ");
                }
            }
            System.out.println();
        }
        System.out.println("-----------------------------------");
    }





    public static Field getFieldAtPosition(int x, int y) {
        if (isValidPosition(x, y)) {
            return grid.get(x).get(y);
        } else {
            System.out.println("Недопустимые координаты.");
            return null;
        }
    }


    public boolean[][] getAvailableMoveCells(int x, int y, int movement) {
        boolean[][] availableCells = new boolean[getSize()][getSize()];

        // Проверяем каждую клетку на возможность перемещения
        for (int i = 0; i < getSize(); i++) {
            for (int j = 0; j < getSize(); j++) {
                // Вычисляем расстояние между текущей клеткой и клеткой (x, y)
                int distance = Math.abs(x - i) + Math.abs(y - j);
                // Проверяем, что расстояние не превышает значение movement
                if (distance <= movement) {
                    // Устанавливаем значение true для доступной клетки
                    availableCells[i][j] = true;
                }
            }
        }

        return availableCells;
    }


    public List<Unit> getEnemyUnits() {
        List<Unit> enemyUnits = new ArrayList<>();
        for (List<Field> row : grid) {
            for (Field field : row) {
                if (field.hasUnit() && field.getUnit() instanceof EnemyUnits) {
                    enemyUnits.add(field.getUnit());
                }
            }
        }
        return enemyUnits;
    }

    // Метод для получения всех союзных юнитов на поле
    public List<Unit> getPlayerUnits() {
        List<Unit> playerUnits = new ArrayList<>();
        for (List<Field> row : grid) {
            for (Field field : row) {
                if (field.hasUnit() && !(field.getUnit() instanceof EnemyUnits)) {
                    playerUnits.add(field.getUnit());

                }
            }
        }

        return playerUnits;

    }

    public void moveUnit(Unit unit, int newX, int newY) {
        // Проверяем, жив ли юнит
        if (!unit.isDead()) {
            // Удаляем воина с его текущих координат
            Field currentField = getFieldAtPosition(unit.getCurrentX(), unit.getCurrentY());
            currentField.setOccupant(null);

            // Устанавливаем новые координаты воину
            unit.setCurrentX(newX);
            unit.setCurrentY(newY);

            // Получаем поле, на котором окажется воин после перемещения
            Field newField = getFieldAtPosition(newX, newY);

            // Если новое поле - препятствие, восстанавливаем на нем препятствие
            newField.setOccupant(unit);
            System.out.println("Юнит перемещен на новые координаты (" + newY + ", " + newX + ").");
            logger.log(Level.INFO, "Юнит {0} перемещен на новые координаты ({1}, {2}).",
                    new Object[]{unit.getName(), newY, newX});
        }
    }



    public static boolean attackEnemy(Unit attacker, Unit targetUnit) {
        // Получаем координаты атакующего и цели
        int targetX = targetUnit.getCurrentX();
        int targetY = targetUnit.getCurrentY();

        // Проверяем, находится ли цель в зоне досягаемости атаки атакующего
        if (!attacker.canAttackEnemy(targetX, targetY)) {
            System.out.println("Цель находится вне зоны досягаемости.");
            return false;
        }


        attacker.attackEnemy(targetUnit);

        // Если цель была уничтожена, удаляем ее из поля
        if (targetUnit.isDead()) {
            // Получаем поле, на котором находится цель
            Field targetField = getFieldAtPosition(targetX, targetY);
            // Удаляем цель из поля
            targetField.setOccupant(null);
        }

        return true;
    }

    private void initializeCatapultArea() {
        // Инициализация поля для катапульты как одного целого
        catapultArea.add(Arrays.asList(new Field("\uD83D\uDD2B", 1.0), new Field("\uD83D\uDD2B", 1.0)));
        catapultArea.add(Arrays.asList(new Field("\uD83D\uDD2B", 1.0), new Field("\uD83D\uDD2B", 1.0)));
    }
    // Метод для проверки, находится ли воин в катапультной области
    public boolean isUnitInCatapultArea(int x, int y) {
        return x >= 3 && x <= 4 && y >= 3 && y <= 4;
    }

    // Метод для проверки, можно ли атаковать с катапульты
    public void resetCatapultUsage() {
        catapultUsed = false;
    }


    public void attackFromCatapult(int targetX, int targetY, Battlefield battlefield) {
        if (!catapultUsed) {
            logger.log(Level.INFO,
                    "Катапульта атакует область вокруг клетки ({0}, {1})",
                    new Object[]{targetX, targetY});

            for (int i = targetY - 1; i <= targetY + 1; i++) {
                for (int j = targetX - 1; j <= targetX + 1; j++) {
                    Field targetField = battlefield.getFieldAtPosition(i, j);
                    if (targetField != null && targetField.getOccupant() instanceof Unit) {
                        InfantryUnit targetUnit = (InfantryUnit) targetField.getOccupant();
                        // Проверяем уклонение от атаки катапульты
                        double evasionChance = calculateEvasionFromCatapult(targetUnit);
                        System.out.println("Шанс уклонение = " + evasionChance);
                        if (Math.random() > evasionChance) { // Если уклонение не сработало
                            // Наносим урон воину
                            int damage = 20;
                            targetUnit.receiveDamage(damage);

                            // Логгирование атаки с катапульты
                            int targetHealthAfterAttack = targetUnit.getHealth();
                            int targetDefenseAfterAttack = targetUnit.getDefense();

                            logger.log(Level.INFO,
                                    "Катапульта атаковала юнита {0} и нанесла {1} урона. \n" +
                                            "Текущее состояние: {0} - {2} ХП, {3} Защиты;",
                                    new Object[]{targetUnit.getName(), damage,
                                            targetUnit.getName(), targetHealthAfterAttack, targetDefenseAfterAttack});

                            System.out.println("Катапульта атаковала юнита " + targetUnit.getName() + " и нанесла " + damage + " урона. \n" +
                                    "Текущее состояние: \n" +
                                    targetUnit.getName() + " - " + targetHealthAfterAttack + " ХП, " + targetDefenseAfterAttack + " Защиты;");
                        } else {
                            System.out.println("Юнит " + targetUnit.getName() + " мастерски уклонился от атаки катапульты.");
                        }
                    }

                    if (targetField != null && !targetField.hasUnit()) { // Проверяем, что на клетке нет юнита
                        // Устанавливаем огонь на клетку, если там нет юнита
                        battlefield.grid.get(i).set(j, new Field("🔥", 2.0));
                    }
                }
            }

            catapultUsed = true;
        } else {
            System.out.println("Катапульта перезарежается, её можно использовать в следующем ходу.");
        }
    }



    public Unit getUnitInCatapult() {
        // Проверяем каждую клетку в области катапульты
        for (int i = 3; i <= 4; i++) {
            for (int j = 3; j <= 4; j++) {
                Field field = grid.get(i).get(j);
                // Если на клетке есть юнит, возвращаем его
                if (field.hasUnit()) {
                    return field.getUnit();
                }
            }
        }
        // Если ни один юнит не найден, возвращаем null
        return null;
    }


    public boolean arePlayerUnitsAlive() {
        List<Unit> playerUnits = getPlayerUnits();
        return !playerUnits.isEmpty();
    }

    public boolean areEnemyUnitsAlive() {
        List<Unit> enemyUnits = getEnemyUnits();
        return !enemyUnits.isEmpty();
    }
    public boolean isTowerDestroyed() {
        return enemyTower.getHealth() <= 0;
    }

    public String getWinner() {
        if (!arePlayerUnitsAlive()) {
            return "компьютер!";
        } else if (!areEnemyUnitsAlive() || isTowerDestroyed()) {
            return "игрок!";
        } else {
            return null; // Если игра не завершена
        }
    }

    public boolean attackTower(Unit attacker,Tower tower) {

        // Проверка, находится ли атакующий в радиусе досягаемости башни
        if (attacker.canAttackEnemy(tower.getCurrentX(), tower.getCurrentY()) ||
                attacker.canAttackEnemy(tower.getCurrentX(), tower.getCurrentY() + 1) ||
                attacker.canAttackEnemy(tower.getCurrentX() + 1, tower.getCurrentY()) ||
                attacker.canAttackEnemy(tower.getCurrentX() + 1, tower.getCurrentY() + 1)) {
            // Наносим урон башне
            tower.receiveDamage(attacker.getAttack());
            tower.takeDamage(attacker.getAttack());
            return true;
        }

        return false;
    }



    public boolean isGameOver() {
        if (isTowerDestroyed()){
            enemyTower.destroy();
        }
        return !arePlayerUnitsAlive() || !areEnemyUnitsAlive() || isTowerDestroyed();
    }

}
