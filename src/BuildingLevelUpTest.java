import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BuildingLevelUpTest {
    private City city;

    @BeforeEach
    void setUp() {
        city = new City();
    }

    @Test
    void testLeechHouseLevelUp() {
        Buildings.LeechHouse leechHouse = new Buildings.LeechHouse();
        city.addBuilding(leechHouse);
        leechHouse.upgrade(city.getResources());
        assertEquals(2, leechHouse.getLevel());
    }

    @Test
    void testForgeLevelUp() {
        Buildings.Forge forge = new Buildings.Forge();
        city.addBuilding(forge);
        forge.upgrade(city.getResources());
        assertEquals(2, forge.getLevel());
    }

    @Test
    void testArmoryLevelUp() {
        Buildings.Armory armory = new Buildings.Armory();
        city.addBuilding(armory);
        armory.upgrade(city.getResources());
        assertEquals(2, armory.getLevel());
    }
}
