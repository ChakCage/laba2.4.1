import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Tower extends EnemyUnits {
    private static final Logger logger = Logger.getLogger(Tower.class.getName());

    private static final String ICON_UP = "⬆️";
    private String directionIcon;

    public Tower() {
        super("Вражеская Башня", 10, 0, 0, 0, 0, 0); // У башни нет атаки и движения
        this.directionIcon = ICON_UP; // Изначальное направление
        setCurrentX(3);
        setCurrentY(5);
    }

    public String getDirectionIcon() {
        return directionIcon;
    }

    public void updateDirection(List<Unit> playerUnits, Tower enemyTower) {
        if (playerUnits == null || playerUnits.isEmpty() || enemyTower == null) {
            return; // Нет юнитов игрока или информации о башне врага для определения направления
        }

        // Создаем массив для подсчета количества ваших воинов в каждом квадрате
        int[][] squares = new int[3][3];

        // Получаем координаты башни врага
        int enemyTowerX = enemyTower.getCurrentX();
        int enemyTowerY = enemyTower.getCurrentY();

        // Перебираем ваших воинов и считаем их в каждом квадрате
        for (Unit playerUnit : playerUnits) {
            int x = playerUnit.getCurrentX() - enemyTowerX + 1; // Смещаем относительно башни и добавляем 1, чтобы не получить отрицательные значения
            int y = playerUnit.getCurrentY() - enemyTowerY + 1; // Аналогично для y

            // Убедимся, что воин находится в пределах массива
            if (x >= 0 && x < 3 && y >= 0 && y < 3) {
                squares[x][y]++;
            } else {
                // Если воин находится за пределами массива, он будет отнесен к ближайшему краю
                if (x < 0) x = 0;
                if (x >= 3) x = 2;
                if (y < 0) y = 0;
                if (y >= 3) y = 2;

                squares[x][y]++;
            }
        }

        // Находим квадрат с максимальным количеством воинов
        int maxCount = 0;
        int maxSquareX = -1;
        int maxSquareY = -1;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (squares[i][j] > maxCount) {
                    maxCount = squares[i][j];
                    maxSquareX = i;
                    maxSquareY = j;
                }
            }
        }

        // Устанавливаем направление в квадрат с максимальным количеством воинов
        String newDirectionIcon = this.directionIcon;
        if (maxSquareX == 0) {
            if (maxSquareY == 0) {
                newDirectionIcon = "↖️"; // Верхний левый квадрат
            } else if (maxSquareY == 1) {
                newDirectionIcon = "⬆️"; // Верхний центральный квадрат
            } else {
                newDirectionIcon = "↗️"; // Верхний правый квадрат
            }
        } else if (maxSquareX == 1) {
            if (maxSquareY == 0) {
                newDirectionIcon = "⬅️"; // Левый центральный квадрат
            } else if (maxSquareY == 1) {
                newDirectionIcon = "○"; // Центральный квадрат (не двигать)
            } else {
                newDirectionIcon = "➡️"; // Правый центральный квадрат
            }
        } else {
            if (maxSquareY == 0) {
                newDirectionIcon = "↙️"; // Нижний левый квадрат
            } else if (maxSquareY == 1) {
                newDirectionIcon = "⬇️"; // Нижний центральный квадрат
            } else {
                newDirectionIcon = "↘️"; // Нижний правый квадрат
            }
        }

        if (!newDirectionIcon.equals(this.directionIcon)) {
            this.directionIcon = newDirectionIcon;
            logger.log(Level.INFO, "Башня стала смотреть в другую сторону: " + this.directionIcon);
        }
    }

    public void takeDamage(int damage) {
        // Логика для получения урона башней
        logger.log(Level.WARNING, "По башне ударили и сняли " + damage + "\n У башни осталось " + this.getHealth() + " ХП");
    }

    public void destroy() {
        // Логика для разрушения башни
        logger.log(Level.SEVERE, "Башня разрушена");
    }
}
