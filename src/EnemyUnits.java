public class EnemyUnits extends InfantryUnit{

    public EnemyUnits(String name, int health, int attack, int range, int defense, int movement, int attackCost) {
        super(name, health, attack, range, defense, movement, attackCost);
    }

    public static class SwordsmanEnemy extends EnemyUnits {

        private final int unitNumber = 1  ;

        private final String img = "⚔️";
        public SwordsmanEnemy() {
            super("Мечник враг", 50, 5, 1, 8, 3, 10);

        }

        @Override
        public String toString() {
            return img;
        }
        @Override
        public int getUnitNumber(){
            return unitNumber;
        }


    }

    public static class AxemanEnemy extends EnemyUnits  {
        private final int unitNumber = 2;

        private final String img = "⚒️";
        public AxemanEnemy() {
            super("Топорщик враг", 45, 9, 1, 3, 4, 20);
        }

        @Override
        public String toString() {
            return img;
        }
        @Override
        public int getUnitNumber(){
            return unitNumber;
        }
    }
    public static class ArcherEnemy extends EnemyUnits  {
        private final int unitNumber = 3;

        private final String img = "\uD83C\uDFD3";
        public ArcherEnemy() {
            super("Лучник(дл.) враг", 30, 6, 5, 8, 2, 15);
        }

        @Override
        public String toString() {
            return img;
        }
        @Override
        public int getUnitNumber(){
            return unitNumber;
        }
    }

    public static class CrossbowmanEnemy extends EnemyUnits  {
        private final int unitNumber = 4;

        private final String img = "\uD83C\uDFBE";
        public CrossbowmanEnemy() {
            super("Арбалетчик враг", 40, 7, 6, 3, 2, 23);
        }

        @Override
        public String toString() {
            return img;
        }
        @Override
        public int getUnitNumber(){
            return unitNumber;
        }
    }

    public static class KnightEnemy extends EnemyUnits  {
        private final int unitNumber = 5;

        private final String img = "\uD83E\uDDD9";
        public KnightEnemy() {
            super("Рыцарь враг", 30, 5, 1, 3, 6, 20);
        }

        @Override
        public String toString() {
            return img;
        }
        @Override
        public int getUnitNumber(){
            return unitNumber;
        }
    }

    public static class MountedArcherEnemy extends EnemyUnits  {
        private final int unitNumber = 6;

        private final String img = "\uD83D\uDC0E️";
        public MountedArcherEnemy() {
            super("Конный лучник враг", 25, 3, 3, 2, 5, 25);
        }

        @Override
        public String toString() {
            return img;
        }
        @Override
        public int getUnitNumber(){
            return unitNumber;
        }
    }
}