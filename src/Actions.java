import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Actions {
    public static void moveUnit(Battlefield battlefield,Scanner scanner) {

        System.out.println("Выберите юнита для перемещения (введите порядковый номер):");


        // Отображаем только воинов, которыми управляет игрок
        List<Unit> playerUnits = battlefield.getPlayerUnits();
        for (int i = 0; i < playerUnits.size(); i++) {
            System.out.println((i + 1) + ". " + playerUnits.get(i).getName() + " (" + playerUnits.get(i).getCurrentY() + ";" + playerUnits.get(i).getCurrentX() + ")");
        }
        System.out.print("Ваш выбор: ");
        // Выбор порядкового номера юнита для перемещения
        int unitIndex = scanner.nextInt();
        scanner.nextLine();

        // Получение юнита по его порядковому номеру
        if (unitIndex >= 1 && unitIndex <= playerUnits.size()) {
            Unit unit = playerUnits.get(unitIndex - 1);
            System.out.println("-----------------------------------");
            System.out.println("Введите новые координаты для перемещения (x и y):");
            System.out.print("x: ");
            int newY = scanner.nextInt();
            System.out.print("y: ");
            int newX = scanner.nextInt();

            // Проверяем, что новые координаты в пределах допустимого поля
            if (!battlefield.isValidPosition(newX, newY) || !unit.canMove(newX, newY)) {
                System.out.println("Недопустимые координаты для перемещения. Попробуйте снова.");
                return;
            }

            // Перемещаем юнита на новые координаты
            battlefield.moveUnit(unit, newX, newY);
        } else {
            System.out.println("Неверный выбор юнита. Попробуйте снова.");
        }
    }


    public static void attackEnemy(Battlefield battlefield, Scanner scanner) {

        System.out.println("-----------------------------------");
        System.out.println("Выберите воина для атаки (введите порядковый номер)");

        // Отображаем только союзных воинов
        List<Unit> playerUnits = battlefield.getPlayerUnits();
        for (int i = 0; i < playerUnits.size(); i++) {
            System.out.println((i + 1) + ". " + playerUnits.get(i).getName() + " (" + playerUnits.get(i).getCurrentY() + ", " + playerUnits.get(i).getCurrentX() + ")");
        }

        System.out.print("Ваш выбор: ");

        // Выбор порядкового номера союзного воина для атаки
        int friendlyUnitIndex = scanner.nextInt();
        System.out.println("-----------------------------------");

        // Получение союзного воина по его порядковому номеру
        if (friendlyUnitIndex >= 1 && friendlyUnitIndex <= playerUnits.size()) {
            Unit friendlyUnit = playerUnits.get(friendlyUnitIndex - 1);

            System.out.println("Выберите вражеского воина, которого хотите атаковать (введите порядковый номер)");
            // Отображаем вражеских воинов
            List<Unit> enemyUnits = battlefield.getEnemyUnits();
            for (int i = 0; i < enemyUnits.size(); i++) {
                System.out.println((i + 1) + ". " + enemyUnits.get(i).getName() + " (" + enemyUnits.get(i).getCurrentY() + ", " + enemyUnits.get(i).getCurrentX() + ")");
            }

            // Выбор порядкового номера врага для атаки

            System.out.print("Ваш выбор: ");
            int enemyUnitIndex = scanner.nextInt();
            System.out.println("-----------------------------------");
            // Получение врага по его порядковому номеру
            if (enemyUnitIndex >= 1 && enemyUnitIndex <= enemyUnits.size()) {
                Unit enemyUnit = enemyUnits.get(enemyUnitIndex - 1);

                // Выполняем атаку выбранного союзного воина на выбранного врага
                boolean isAttackSuccessful = battlefield.attackEnemy(friendlyUnit, enemyUnit);

                if (isAttackSuccessful) {
                    System.out.println("Атака успешна!");
                } else {
                    System.out.println("Не удалось выполнить атаку.");
                }
            } else {
                System.out.println("Неверный выбор врага для атаки. Попробуйте снова.");
            }
        } else {
            System.out.println("Неверный выбор союзного воина для атаки. Попробуйте снова.");
        }



    }

    public static void attackWithCatapult(Battlefield battlefield,Scanner scanner) {


        Unit unitInCatapult = battlefield.getUnitInCatapult(); // Получаем юнита, который находится в катапультe

        if (unitInCatapult != null) {
            System.out.println("Юнит " + unitInCatapult.getName() + " находится в катапульте.");

            System.out.println("Введите координаты для атаки:");
            System.out.print("Введите координату X: ");
            int targetX = scanner.nextInt();
            scanner.nextLine(); // Очистка буфера

            System.out.print("Введите координату Y: ");
            int targetY = scanner.nextInt();
            scanner.nextLine(); // Очистка буфера
            battlefield.attackFromCatapult(targetX, targetY, battlefield);
        } else {
            System.out.println("Катапульта пуста. Атака невозможна.");
        }
    }


    public static void attackTower(Battlefield battlefield, Scanner scanner) {
        Tower tower = battlefield.getEnemyTower();
        System.out.println("Имя башни = " + tower.getName() + " координаты башни = (" + tower.getCurrentX() + ", " + tower.getCurrentY() + ")" + " Хп башни = " + tower.getHealth());
        System.out.println("-----------------------------------");
        System.out.println("Выберите воина для атаки башни (введите порядковый номер)");

        // Отображаем только союзных воинов
        List<Unit> playerUnits = battlefield.getPlayerUnits();
        for (int i = 0; i < playerUnits.size(); i++) {
            System.out.println((i + 1) + ". " + playerUnits.get(i).getName() + " (" + playerUnits.get(i).getCurrentY() + ", " + playerUnits.get(i).getCurrentX() + ")");
        }

        System.out.print("Ваш выбор: ");
        // Выбор порядкового номера союзного воина для атаки башни
        int friendlyUnitIndex = scanner.nextInt();
        System.out.println("-----------------------------------");

        // Получение союзного воина по его порядковому номеру
        if (friendlyUnitIndex >= 1 && friendlyUnitIndex <= playerUnits.size()) {
            Unit friendlyUnit = playerUnits.get(friendlyUnitIndex - 1);

            // Проверка возможности атаки башни
            boolean isAttackSuccessful = battlefield.attackTower(friendlyUnit, tower);

            if (isAttackSuccessful) {
                System.out.println("Атака на башню успешна! У башни осталось " + tower.getHealth() + " ХП");
            } else {
                System.out.println("Не удалось выполнить атаку на башню.");
            }
        } else {
            System.out.println("Неверный выбор союзного воина для атаки башни. Попробуйте снова.");
        }
    }





}
