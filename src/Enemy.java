
public class Enemy {
    private Battlefield battlefield;

    public Enemy(Battlefield battlefield) {
        this.battlefield = battlefield;
    }

    public void placeEnemyUnits() {
        // Места, где будут находиться вражеские воины (статичные пока что)
        int[][] enemyUnitPositions = {
                {1, 7}
        };

        // Создаем и размещаем вражеских воинов на поле
        for (int i = 0; i < enemyUnitPositions.length; i++) {
            int x = enemyUnitPositions[i][0];
            int y = enemyUnitPositions[i][1];
            EnemyUnits enemyUnit = new EnemyUnits.SwordsmanEnemy(); // Пока создадим только мечника врага, но можно добавить и других
            battlefield.placeUnit(y, x, enemyUnit);
        }
    }


}

