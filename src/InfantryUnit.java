import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.Serializable;

public class InfantryUnit implements Unit,Serializable {
    private static final Logger logger = Logger.getLogger(InfantryUnit.class.getName());
    private boolean isDead;
    private String name;
    private int health;
    private int attack;
    private int range;
    private int defense;
    private int movement;
    private int Cost;
    protected int unitNumber;
    private  int currentX; // Текущая координата X
    private  int currentY;// Текущая координата Y
    private String img;

    @Override
    public String toString() {
        return img;
    }

    public void setUnitNumber(int unitNumber) {
        this.unitNumber = unitNumber;
    }

    public InfantryUnit(String name, int health, int attack, int range, int defense, int movement, int Cost) {
        this.name = name;
        this.health = health;
        this.attack = attack;
        this.range = range;
        this.defense = defense;
        this.movement = movement;
        this.Cost = Cost;
        this.currentX = -1;
        this.currentY = -1;
        this.isDead = false;// По умолчанию в начале игры юнит живой
        logger.setLevel(Level.INFO);
    }







    // Метод для получения уклонения от атаки катапультой




    // Метод, проверяющий, мертв ли юнит
    public boolean isDead() {
        return isDead;
    }


    // Метод для "убийства" юнита
    public void killUnit() {
        PlayerProgress playerProgress = PlayerProgress.loadProgress("game_progress.dat");
        City city = playerProgress.getCity();
        Resources resources = city.getResources();
        isDead = true;
        System.out.println("Юнит " + this.name + " был убит.");

        if (this instanceof EnemyUnits) { // Проверка на принадлежность к классу врага
            resources.addWood(10); // Добавляем 10 дерева
            resources.addStone(10); // Добавляем 10 камня
            System.out.println("Вы получили 10 дерева и 10 камня за убийство врага.");
            try {
                playerProgress.saveProgress("game_progress.dat");
                System.out.println("Прогресс сохранен после убийства врага.");
            } catch (Exception e) {
                System.out.println("Ошибка при сохранении прогресса: " + e.getMessage());
            }
        }
    }

    // Getters and setters
    public String getName() {
        return name;
    }




    private static final FileHandler fileHandler;

    static {
        try {
            fileHandler = new FileHandler("Attack_Log.txt");
            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL);
            logger.setUseParentHandlers(false);
        } catch (IOException e) {
            throw new ExceptionInInitializerError("Failed to initialize logger: " + e.getMessage());
        }
    }



    // Метод для получения урона юнитом
    public void receiveDamage(int damage) {
        // Вычисляем, сколько урона проходит защиту
        int damageAfterDefense = Math.max(damage - this.defense, 0);

        // Уменьшаем защиту на величину нанесенного урона
        this.defense = Math.max(this.defense - damage, 0);

        // Уменьшаем здоровье на величину урона, превышающего защиту
        this.health = Math.max(this.health - damageAfterDefense, 0);

        // Если здоровье стало меньше или равно 0, "убиваем" юнита
        if (this.health <= 0) {
            this.health = 0; // Чтобы здоровье не стало отрицательным
            this.killUnit();
        }
        logger.log(Level.INFO, "Юнит {0} получил {1} урона", new Object[]{this.name, damage});
    }




    public void setName(String name) {
        this.name = name;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getAttack() {
        return attack;
    }

    @Override
    public void setimg(String img) {
        this.img = img;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getMovement() {
        return movement;
    }

    public void setMovement(int movement) {
        this.movement = movement;
    }

    public int getCost() {
        return Cost;
    }

    public void setCost(int Cost) {
        this.Cost = Cost;
    }

    public int getUnitNumber() {
        return unitNumber;
    }

    public void setCurrentX(int currentX) {
        this.currentX = currentX;
    }

    public void setCurrentY(int currentY) {
        this.currentY = currentY;
    }

    public int getCurrentX() {
        return currentX;
    }

    public int getCurrentY() {
        return currentY;
    }



    public boolean canMove(int newX, int newY) {
        // Проверяем, что новая позиция находится в пределах поля
        if (!Battlefield.isValidPosition(newX, newY)) {
            return false;
        }

        // Проверяем, равны ли текущие координаты новым координатам
        if (currentX == newX && currentY == newY) {
            return true; // Возвращаем true, если текущие координаты равны новым
        }

        double totalPenalty = 0.0; // Сумма шагов с учетом штрафов

        // Вычисляем расстояние до новой позиции
        int distanceX = Math.abs(newX - currentX);
        int distanceY = Math.abs(newY - currentY);



        if (currentX != newX && currentY != newY) { // Движение по диагонали
            int steps = Math.abs(newX - currentX);
            for (int i = 1; i <= steps; i++) {
                int x = currentX + i * (newX - currentX) / steps;
                int y = currentY + i * (newY - currentY) / steps;
                Field field = Battlefield.getFieldAtPosition(x, y);
                if (i % 2 == 0) { // Каждая вторая клетка по диагонали
                    totalPenalty += 2.0; // Устанавливаем штраф 2
                } else {
                    totalPenalty += field.getPenalty(this);
                }
            }
        } else { // Движение по вертикали или горизонтали
            int steps = Math.max(distanceX, distanceY);
            for (int i = 1; i <= steps; i++) {
                int x = currentX + (newX - currentX) * i / steps;
                int y = currentY + (newY - currentY) * i / steps;
                Field field = Battlefield.getFieldAtPosition(x, y);
                totalPenalty += field.getPenalty(this);
            }
        }

        // Проверяем, что количество очков движения после учёта штрафов достаточно для перемещения
        System.out.println("Пройденное расстояние = " + totalPenalty);
        return totalPenalty <= movement;
    }




    public boolean canAttackEnemy(int enemyX, int enemyY) {
        double distance = 0.0; // Сумма шагов с учетом штрафов

        // Вычисляем расстояние до координат врага
        int distanceX = Math.abs(enemyX - currentX);
        int distanceY = Math.abs(enemyY - currentY);



        if (currentX != enemyX && currentY != enemyY) { // Движение по диагонали
            int steps = Math.abs(enemyX - currentX);
            int stepsY = Math.abs(enemyY - currentY);
            steps = Math.max(steps, stepsY); // Выбираем максимальное количество шагов

            for (int i = 1; i <= steps; i++) {
                if (i % 2 == 0) { // Каждая вторая клетка по диагонали
                    distance += 2.0; // Устанавливаем штраф 2
                } else {
                    distance += 1;
                }
            }
        } else { // Движение по вертикали или горизонтали
            int steps = Math.max(distanceX, distanceY);
            for (int i = 1; i <= steps; i++) {
                distance += 1;
            }
        }

        // Проверяем, что количество очков движения после учёта штрафов достаточно для перемещения


        return distance <= range; // Может атаковать, если расстояние до врага меньше или равно его дальности атаки
    }


    @Override
    public void attackEnemy(Unit targetUnit) {
        // Проверяем, что цель атаки существует
        if (targetUnit == null) {
            System.out.println("Невозможно атаковать пустую цель.");
            return;
        }



        // Вычисляем урон атаки
        int damage = this.getAttack();

        // Наносим урон цели атаки
        targetUnit.receiveDamage(damage);

        int attackerHealthAfterAttack = this.health;
        int attackerDefenseAfterAttack = this.defense;
        int targetHealthAfterAttack = targetUnit.getHealth();
        int targetDefenseAfterAttack = targetUnit.getDefense();

        logger.log(Level.INFO,
                "Юнит {0} атаковал юнита {1} и нанес {2} урона. \n" +
                        "Текущее состояние: " +
                        "{0} - {3} ХП, {4} Защиты; " +
                        "{1} - {5} ХП, {6} Защиты",
                new Object[]{this.name, targetUnit.getName(), damage,
                        attackerHealthAfterAttack, attackerDefenseAfterAttack,
                        targetHealthAfterAttack, targetDefenseAfterAttack});

        System.out.println("Юнит " + this.name + " атаковал юнита " + targetUnit.getName() + " и нанес " + damage + " урона. \n" +
                "Текущее состояние: \n" +
                this.name + " - " + attackerHealthAfterAttack + " ХП, " + attackerDefenseAfterAttack + " Защиты; \n"  +
                targetUnit.getName() + " - " + targetHealthAfterAttack + " ХП, " + targetDefenseAfterAttack + " Защиты;");

    }



}
