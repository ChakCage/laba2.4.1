import java.util.Random;
import java.util.Scanner;
import java.text.DecimalFormat;
import java.io.*;
import java.util.*;
public class Buildings {
    public static class LeechHouse extends Building {
        public LeechHouse() {
            super("Дом лекаря", 20, 30); // Стоимость постройки
        }

        public int getHealthBonus() {
            return level; // Бонус к здоровью равен уровню здания
        }
    }

    public static class Forge extends Building {
        public Forge() {
            super("Кузница", 30, 40); // Стоимость постройки
        }

        public int getAttackBonus() {
            return level; // Бонус к атаке равен уровню здания
        }
    }

    public static class Armory extends Building {
        public Armory() {
            super("Арсенал", 40, 50); // Стоимость постройки
        }

        public int getDefenseBonus() {
            return level; // Бонус к защите равен уровню здания
        }
    }

    public static class Tavern extends Building {
        private int moveBonus;


        public Tavern() {
            super("Таверна", 25, 35); // Стоимость постройки
            moveBonus = 0;

        }

        public void setMoveBonus(int bonus) {
            moveBonus += bonus;
        }


        public int getMoveBonus() {
            return moveBonus;
        }


    }

    public static class Academy extends Building {
        private static final String FILE_NAME = "units.dat";

        public Academy() {
            super("Академия", 50, 60); // Стоимость постройки
            createUnitsFileWithDefaults(); // Создаем файл с дефолтными юнитами, если его нет
        }

        // Создание файла с дефолтными юнитами, если файл не существует
        public static void createUnitsFileWithDefaults() {
            File file = new File(FILE_NAME);
            if (!file.exists()) {
                try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file))) {
                    // Добавляем стандартных юнитов
                    List<InfantryUnit> defaultUnits = Arrays.asList(
                            new Units.Swordsman(),
                            new Units.Axeman(),
                            new Units.Archer(),
                            new Units.Crossbowman(),
                            new Units.Knight(),
                            new Units.MountedArcher()
                    );
                    for (InfantryUnit unit : defaultUnits) {
                        outputStream.writeObject(unit); // Записываем стандартных юнитов в файл
                    }
                    System.out.println("Файл с дефолтными юнитами успешно создан.");
                } catch (IOException e) {
                    System.out.println("Ошибка при создании файла с юнитами: " + e.getMessage());
                }
            }
        }

        public static void showAllUnits() {
            File file = new File(FILE_NAME);
            if (!file.exists()) {
                System.out.println("Файл с юнитами не найден. Создание дефолтных юнитов.");
                createUnitsFileWithDefaults(); // Создаем файл с дефолтными юнитами, если его нет
            }

            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
                System.out.println("Все существующие воины:");
                int number = 1;
                while (true) {
                    try {
                        InfantryUnit unit = (InfantryUnit) inputStream.readObject();
                        System.out.println(number + ". " + unit.toString() + unit.getName() + " (Здоровье: " + unit.getHealth() + ", Атака: " + unit.getAttack() + ", Дальность: " + unit.getRange() + ", Защита: " + unit.getDefense() + ", Движение: " + unit.getMovement() + ", Цена: " + unit.getCost() + ")");
                        number++;
                    } catch (EOFException eof) {
                        break; // Достигнут конец файла
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Ошибка при чтении существующих воинов: " + e.getMessage());
            }
        }

        public void createNewUnit(Scanner scanner, Resources resources) {
            System.out.println("Создание нового воина:");

            System.out.print("Имя воина: ");
            String name = scanner.next();
            scanner.nextLine();

            System.out.print("Здоровье (HP): ");
            int health = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Атака: ");
            int attack = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Дальность атаки: ");
            int range = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Защита: ");
            int defense = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Движение: ");
            int movement = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Цена: ");
            int cost = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Класс юнита (1-6): ");
            int unitNumber = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Значок (символ): ");
            String img = scanner.next();
            scanner.nextLine();

            // Расчет стоимости
            int woodCost = (health + attack + range) * 2; // 2 дерева за каждое очко
            int stoneCost = (defense + movement) * 2 + (50 - cost) * 2; // 2 камня за каждую единицу разницы с 50

            System.out.println("Стоимость создания нового юнита: " + woodCost + " дерева и " + stoneCost + " камня.");
            System.out.println("Хотите продолжить создание воина? (1 - Да, 0 - Нет)");

            String confirmInput = scanner.nextLine().trim().toUpperCase();
            boolean freeCreation = false;

            switch (confirmInput) {
                case "AEZAKMI": // Чит-код для бесплатного создания
                    freeCreation = true;
                    System.out.println("Чит-код активирован! Создание воина будет бесплатным.");
                    break;

                case "1": // Подтверждение с ресурсами
                    if (resources.getWood() >= woodCost && resources.getStone() >= stoneCost) {
                        resources.removeWood(woodCost);
                        resources.removeStone(stoneCost);
                    } else {
                        System.out.println("Недостаточно ресурсов для создания нового юнита.");
                        return;
                    }
                    break;

                case "0": // Отмена создания
                    System.out.println("Создание нового юнита отменено.");
                    return;

                default: // Если ввод некорректен
                    System.out.println("Неверный ввод. Попробуйте снова.");
                    return;
            }

            if (freeCreation || (resources.getWood() >= woodCost && resources.getStone() >= stoneCost)) {
                InfantryUnit newUnit = new InfantryUnit(name, health, attack, range, defense, movement, cost);
                newUnit.setimg(img);
                newUnit.setUnitNumber(unitNumber);

                try (AppendingObjectOutputStream outputStream = new AppendingObjectOutputStream(
                        new FileOutputStream("units.dat", true))) {
                    outputStream.writeObject(newUnit);
                    System.out.println("Новый воин создан и сохранен.");
                } catch (IOException e) {
                    System.out.println("Ошибка при сохранении нового юнита: " + e.getMessage());
                }
            }
        }


    }

    static class AppendingObjectOutputStream extends ObjectOutputStream {
        public AppendingObjectOutputStream(OutputStream out) throws IOException {
            super(out);
        }

        @Override
        protected void writeStreamHeader() throws IOException {
            reset(); // Сбрасываем заголовок, чтобы избежать перезаписи
        }
    }

    public static class Market extends Building {
        private final Random random = new Random();
        private final DecimalFormat df = new DecimalFormat("#.##"); // Два знака после запятой

        public Market() {
            super("Рынок", 10, 20); // Стоимость постройки
        }

        public void trade(Resources resources,Scanner scanner) {
            // Генерируем случайный курс от 0.2 до 1.0 и округляем до двух знаков после запятой
            double woodToStoneRate = 0.2 + random.nextDouble() * (5.0);
            double stoneToWoodRate = 0.2 + random.nextDouble() * (5.0);

            // Форматируем для отображения
            String woodToStoneStr = df.format(woodToStoneRate);
            String stoneToWoodStr = df.format(stoneToWoodRate);

            System.out.println("Добро пожаловать на рынок!");
            System.out.println("Курс обмена:");
            System.out.println("1 дерево за " + woodToStoneStr + " камня");
            System.out.println("1 камень за " + stoneToWoodStr + " дерева");


            System.out.println("Выберите опцию:");
            System.out.println("1. Обменять дерево на камень");
            System.out.println("2. Обменять камень на дерево");
            System.out.println("3. Выйти");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Сколько дерева вы хотите обменять?");
                    int woodAmount = scanner.nextInt();
                    if (resources.getWood() >= woodAmount) {
                        double stoneReceived = woodAmount * woodToStoneRate;
                        resources.removeWood(woodAmount);
                        resources.addStone((int) stoneReceived); // Округляем до целого числа
                        System.out.println("Вы обменяли " + woodAmount + " дерева на " + (int) stoneReceived + " камня.");
                    } else {
                        System.out.println("Недостаточно дерева для обмена.");
                    }
                    break;

                case 2:
                    System.out.println("Сколько камня вы хотите обменять?");
                    int stoneAmount = scanner.nextInt();
                    if (resources.getStone() >= stoneAmount) {
                        double woodReceived = stoneAmount * stoneToWoodRate;
                        resources.removeStone(stoneAmount);
                        resources.addWood((int) woodReceived); // Округляем до целого числа
                        System.out.println("Вы обменяли " + stoneAmount + " камня на " + (int) woodReceived + " дерева.");
                    } else {
                        System.out.println("Недостаточно камня для обмена.");
                    }
                    break;
                case 3:
                    System.out.println("Выход...");
                    break;
                default:
                    System.out.println("Неверный выбор.");
                    break;
            }
        }
    }

}
