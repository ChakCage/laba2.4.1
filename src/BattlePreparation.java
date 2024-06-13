public class BattlePreparation {
    public void prepareForBattle(City city, InfantryUnit unit) {
        Buildings.LeechHouse leechHouse = (Buildings.LeechHouse) city.getBuilding(Buildings.LeechHouse.class);
        Buildings.Forge forge = (Buildings.Forge) city.getBuilding(Buildings.Forge.class);
        Buildings.Armory armory = (Buildings.Armory) city.getBuilding(Buildings.Armory.class);
        Buildings.Tavern tavern = (Buildings.Tavern) city.getBuilding(Buildings.Tavern.class);

        if (leechHouse != null) {
            int healthBonus = leechHouse.getHealthBonus(); // Бонус к здоровью
            unit.setHealth(unit.getHealth() + healthBonus);
        }

        if (forge != null) {
            int attackBonus = forge.getAttackBonus(); // Бонус к атаке
            unit.setAttack(unit.getAttack() + attackBonus);
        }

        if (armory != null) {
            int defenseBonus = armory.getDefenseBonus(); // Бонус к защите
            unit.setDefense(unit.getDefense() + defenseBonus);
        }

        if (tavern != null) {
            int moveBonus = tavern.getMoveBonus(); // Бонус к перемещению

            unit.setMovement( (unit.getMovement() + moveBonus));
        }

        Buildings.Academy academy = (Buildings.Academy) city.getBuilding(Buildings.Academy.class);
        if (academy != null) {
            // Логика работы академии, например, создание новых юнитов
        }

        Buildings.Market market = (Buildings.Market) city.getBuilding(Buildings.Market.class);
        if (market != null) {
            // Логика работы рынка, если она необходима для подготовки к бою
        }
    }
}
