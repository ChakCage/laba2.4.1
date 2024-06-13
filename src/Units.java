
public class Units {
    public static class Swordsman extends InfantryUnit {
        private final int unitNumber = 1;



        private final String img = "\uD83D\uDDE1\uFE0F";
        public Swordsman() {
            super("Мечник", 50, 5, 1, 8, 3, 10);
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

    public static class Axeman extends InfantryUnit {
        private final int unitNumber = 2;



        private final String img = "\uD83E\uDE93";
        public Axeman() {
            super("Топорщик", 45, 9, 1, 3, 4, 20);
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
    public static class Archer extends InfantryUnit {
        private final int unitNumber = 3;

        private final String img = "\uD83C\uDFF9";
        public Archer() {
            super("Лучник(дл.)", 30, 6, 5, 8, 2, 15);
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

    public static class Crossbowman extends InfantryUnit {
        private final int unitNumber = 4;
        private final String img = "\uD83C\uDFAF";
        public Crossbowman() {
            super("Арбалетчик", 40, 7, 6, 3, 2, 23);
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

    public static class Knight extends InfantryUnit {
        private final int unitNumber = 5;
        private final String img = "\uD83D\uDEE1\uFE0F";
        public Knight() {
            super("Рыцарь", 30, 5, 1, 3, 6, 20);
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

    public static class MountedArcher extends InfantryUnit {
        private final int unitNumber = 6;
        private final String img = "\uD83C\uDFC7";
        public MountedArcher() {
            super("Конный лучник", 25, 3, 3, 2, 5, 25);
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
