import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.*;
public class SetUnit {
    private static City city; // Добавляем поле City
    private static BattlePreparation battlePreparation; // Добавляем BattlePreparation
    private static List<InfantryUnit> availableUnits; // Список доступных юнитов

    public SetUnit(City city, BattlePreparation battlePreparation) { // Конструктор для инициализации
        this.city = city;
        this.battlePreparation = battlePreparation;
        this.availableUnits = loadUnitsFromFile();
    }
    public static void setupPresetUnits(Battlefield battlefield, City city, BattlePreparation battlePreparation) {
        InfantryUnit knight = new Units.Knight();
        InfantryUnit axeman = new Units.Axeman();
        InfantryUnit crossbowman = new Units.Crossbowman();
        InfantryUnit swordsman = new Units.Swordsman();

        battlePreparation.prepareForBattle(city, knight);
        battlePreparation.prepareForBattle(city, axeman);
        battlePreparation.prepareForBattle(city, crossbowman);
        battlePreparation.prepareForBattle(city, swordsman);

        battlefield.placeUnit(0, 0, knight);
        battlefield.placeUnit(0, 2, axeman);
        battlefield.placeUnit(0, 4, crossbowman);
        battlefield.placeUnit(0, 6, swordsman);

        // Для вражеских юнитов (без подготовки к бою)
        battlefield.placeUnit(8, 0, new EnemyUnits.KnightEnemy());
        battlefield.placeUnit(8, 2, new EnemyUnits.AxemanEnemy());
        battlefield.placeUnit(8, 4, new EnemyUnits.CrossbowmanEnemy());
        battlefield.placeUnit(8, 6, new EnemyUnits.SwordsmanEnemy());
    }

    private List<InfantryUnit> loadUnitsFromFile() {
        List<InfantryUnit> units = new ArrayList<>();
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("units.dat"))) {
            while (true) {
                try {
                    InfantryUnit unit = (InfantryUnit) inputStream.readObject();
                    units.add(unit);
                } catch (EOFException eof) {
                    break; // Достигнут конец файла
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ошибка при загрузке юнитов: " + e.getMessage());
        }
        return units;
    }

    public void placeUnits(Battlefield battlefield,Scanner scanner) {
        int money = 100;// Начальное количество денег

        System.out.println("Ваш бюджет: " + money);

        System.out.println("Выберите юнитов для размещения (максимум 5):");
        int unitsCount = 0;
        int chet = 0;

        while (unitsCount < 5 && money > 0) {
            Buildings.Academy.showAllUnits();

            while (chet != 0) {
                System.out.println("Оставшиеся деньги: " + money);
                break;
            }

            chet++;
            System.out.print("Выберите номер юнита (0 для завершения выбора): ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 0) {
                break;
            }

            InfantryUnit selectedUnit = availableUnits.get(choice - 1);

            if (selectedUnit.getCost() <= money) {
                placeUnit(battlefield, selectedUnit,scanner); // Добавляем юнита на поле
                money -= selectedUnit.getCost();
                unitsCount++;
            } else {
                System.out.println("У вас недостаточно денег для выбора этого юнита.");
            }
        }
    }

    private static void placeUnit(Battlefield battlefield, InfantryUnit unit,Scanner scanner) {
        int x, y;
        do {
            System.out.println("Введите координаты для размещения " + unit.getName() + " на поле (x и y от 0 до " + (battlefield.getSize() - 1) + "): ");
            System.out.print("x: ");
            y = scanner.nextInt();
            System.out.print("y: ");
            x = scanner.nextInt();
            scanner.nextLine();
        } while (!battlefield.isValidPosition(x, y));

        unit.setCurrentX(x); // Устанавливаем координаты юнита
        unit.setCurrentY(y);
        battlePreparation.prepareForBattle(city, unit);
        battlefield.placeUnit(x, y, unit);
        battlefield.display();
    }
}



