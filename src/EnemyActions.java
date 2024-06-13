import java.util.List;

public class EnemyActions {
    public static void performEnemyActions(Battlefield battlefield) {
        List<Unit> enemyUnits = battlefield.getEnemyUnits();
        boolean playerInRange = isPlayerInRange(battlefield);
        Tower enemyTower = battlefield.getEnemyTower();

        // Обновляем направление башни
        enemyTower.updateDirection(battlefield.getPlayerUnits(), enemyTower);

        // Ограничиваем врага выполнением только двух действий за ход
        for (int i = 0; i < 2; i++) {
            if (playerInRange) {
                attackPlayerUnits(battlefield, enemyUnits);
            } else {
                moveEnemyUnits(battlefield, enemyUnits, enemyTower);
            }
        }
    }


    private static void attackPlayerUnits(Battlefield battlefield, List<Unit> enemyUnits) {
        List<Unit> playerUnits = battlefield.getPlayerUnits();
        for (Unit enemyUnit : enemyUnits) {
            if (!playerUnits.isEmpty()) {
                for (Unit playerUnit : playerUnits) {
                    boolean isPlayerInCatapultArea = battlefield.isUnitInCatapultArea(playerUnit.getCurrentX(), playerUnit.getCurrentY());
                    if (isPlayerInCatapultArea) {
                        attackCatapultArea(battlefield, enemyUnit, playerUnit);
                    } else {
                        boolean canAttackEnemy = enemyUnit.canAttackEnemy( playerUnit.getCurrentX(), playerUnit.getCurrentY());
                        if (canAttackEnemy) {
                            battlefield.attackEnemy(enemyUnit, playerUnit);
                            return;
                        }
                    }
                }
            }
        }
    }


    private static void attackCatapultArea(Battlefield battlefield, Unit enemyUnit, Unit playerUnit) {
        boolean attackOccurred = false; // Флаг, указывающий, произошла ли атака хотя бы один раз

        for (int i = 3; i <= 4; i++) {
            for (int j = 3; j <= 4; j++) {
                boolean canAttackEnemy = enemyUnit.canAttackEnemy( j, i);
                if (canAttackEnemy) {
                    attackOccurred = true; // Устанавливаем флаг в true, так как атака произошла

                }
            }
        }

        // Если атака произошла хотя бы один раз, применяем урон к воинам в зоне катапульты
        if (attackOccurred) {
            System.out.println("Вражеский юнит атакует катапульту!");
            for (Unit unit : battlefield.getPlayerUnits()) {
                if (battlefield.isUnitInCatapultArea(unit.getCurrentX(), unit.getCurrentY())) {
                    unit.receiveDamage(enemyUnit.getAttack());
                    System.out.println(enemyUnit.getName() + " атаковал " + unit.getName() + " и нанёс " + enemyUnit.getAttack() + " урона" );
                    System.out.println("Текущее состояние: \n" + enemyUnit.getName() + " - " +  unit.getHealth() + " ХП " + enemyUnit.getDefense() + " защиты" );
                    System.out.println(unit.getName() + " - " +  unit.getHealth() + " ХП " + unit.getDefense() + " защиты" );
                }
            }


        }
    }

    private static void moveEnemyUnits(Battlefield battlefield, List<Unit> enemyUnits, Tower enemyTower) {
        for (Unit enemyUnit : enemyUnits) {
            // Получаем текущие координаты вражеского юнита
            int currentX = enemyUnit.getCurrentX();
            int currentY = enemyUnit.getCurrentY();

            // Получаем доступные клетки для перемещения
            boolean[][] availableCells = battlefield.getAvailableMoveCells(currentX, currentY, enemyUnit.getMovement());

            // Определяем направление на основе стрелки башни
            int dx = 0, dy = 0;
            switch (enemyTower.getDirectionIcon()) {
                case " ⬆":
                    dy = -1; // Движение вверх
                    break;
                case " ⬇":
                    dy = 1; // Движение вниз
                    break;
                case " ⬅":
                    dx = -1; // Движение влево
                    break;
                case " ➡":
                    dx = 1; // Движение вправо
                    break;
                case " ↖":
                    dx = currentX == 0 ? 0 : -1; // Движение вверх-влево, если не достигнут левый край карты
                    dy = currentY == 0 ? 0 : -1; // Игнорируем движение вверх, если достигнут верхний край карты
                    break;
                case " ↗":
                    dx = currentX == 0 ? 0 : -1; // Движение вниз-влево, если не достигнут левый край карты
                    dy = currentY == 9 - 1 ? 0 : 1; // Игнорируем движение вниз, если достигнут нижний край карты
                    break;
                case " ↘":
                    dx = currentX == 9 - 1 ? 0 : 1; // Движение вниз-вправо, если не достигнут правый край карты
                    dy = currentY == 9 - 1 ? 0 : 1; // Игнорируем движение вниз, если достигнут нижний край карты
                    break;
                case " ↙":
                    dx = currentX == 9 - 1 ? 0 : 1; // Движение вверх-вправо, если не достигнут правый край карты
                    dy = currentY == 0 ? 0 : -1; // Игнорируем движение вверх, если достигнут верхний край карты
                    break;
            }

            // Проверяем, если игрок находится в радиусе 2 клеток, двигаемся к нему
            for (Unit playerUnit : battlefield.getPlayerUnits()) {
                int playerX = playerUnit.getCurrentX();
                int playerY = playerUnit.getCurrentY();
                if (Math.abs(playerX - currentX) <= 2 && Math.abs(playerY - currentY) <= 2) {
                    dx = Integer.compare(playerX, currentX);
                    dy = Integer.compare(playerY, currentY);
                    break;
                }
            }

            // Перемещаем врага в клетку, если клетка свободна
            int newX = currentX + dx;
            int newY = currentY + dy;

            // Проверяем, не находится ли клетка в области катапульты
            if (Battlefield.isValidPosition(newX, newY) && availableCells[newX][newY] && !isUnitAtPosition(battlefield, newX, newY)) {
                battlefield.moveUnit(enemyUnit, newX, newY);
            }
        }
    }







    private static boolean isUnitAtPosition(Battlefield battlefield, int x, int y) {
        List<Unit> unitsAtPosition = battlefield.getUnitsAtPosition(x, y);
        return !unitsAtPosition.isEmpty();
    }


    private static boolean isPlayerInRange(Battlefield battlefield) {
        List<Unit> playerUnits = battlefield.getPlayerUnits();
        List<Unit> enemyUnits = battlefield.getEnemyUnits();
        for (Unit enemyUnit : enemyUnits) {
            for (Unit playerUnit : playerUnits) {
                // Проверяем, находится ли игрок в пределах досягаемости вражеского юнита
                if (enemyUnit.canAttackEnemy(playerUnit.getCurrentX(), playerUnit.getCurrentY())) {
                    return true;
                }
                // Проверяем, находится ли игрок в катапультной зоне атаки вражеского юнита
                if (battlefield.isUnitInCatapultArea(playerUnit.getCurrentX(), playerUnit.getCurrentY())) {
                    for (int i = 3; i <= 4; i++) {
                        for (int j = 3; j <= 4; j++) {
                            if (enemyUnit.canAttackEnemy(j, i)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }






}
