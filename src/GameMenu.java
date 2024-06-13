import java.util.List;
import java.util.Scanner;

public class GameMenu {
    public static void showCityMenu(Scanner scanner) {
        PlayerProgress playerProgress = PlayerProgress.loadProgress("game_progress.dat");

        if (playerProgress == null) {
            System.out.println("Ошибка: Не удалось загрузить прогресс. Пожалуйста, попробуйте снова.");
            return;
        }
        City city = playerProgress.getCity();
        Resources resources = city.getResources();
        Buildings.Academy academy = (Buildings.Academy) city.getBuilding(Buildings.Academy.class);
        if (academy != null) {
            // Создаем файл с дефолтными юнитами, если он не существует
            Buildings.Academy.createUnitsFileWithDefaults();
        }


        while (true) {
            System.out.println("-----------------------------------");
            System.out.println("Меню города:");
            System.out.println("1. Построить здание");
            System.out.println("2. Улучшить здание");
            System.out.println("3. Показать здания");
            System.out.println("4. Вернуться в главное меню");

            if (city.getBuilding(Buildings.Academy.class) != null) {
                System.out.println("5. Зайти в Академию ");
            }
            // Проверяем наличие рынка в городе
            if (city.getBuilding(Buildings.Market.class) != null) {
                System.out.println("6. Зайти на рынок" ); // Добавляем опцию для рынка
            }

            System.out.println("Текущие ресурсы: Камень = " + resources.getStone() + ", Дерево = " + resources.getWood());
            System.out.println("-----------------------------------");

            System.out.print("Ваш выбор: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("-----------------------------------");
                    buildBuilding(city, scanner, playerProgress); // Передаем `playerProgress` для сохранения
                    System.out.println("-----------------------------------");
                    break;
                case 2:
                    System.out.println("-----------------------------------");
                    upgradeBuilding(city, scanner, playerProgress); // Передаем `playerProgress` для сохранения
                    System.out.println("-----------------------------------");
                    break;
                case 3:
                    System.out.println("-----------------------------------");
                    city.displayBuildings(); // Отображаем все здания
                    System.out.println("-----------------------------------");
                    break;
                case 4:
                    playerProgress.saveProgress("game_progress.dat");
                    System.out.println("-----------------------------------");
                    return; // Возвращаемся в главное меню
                case 5:
                    System.out.println("-----------------------------------");
                    academy = (Buildings.Academy) city.getBuilding(Buildings.Academy.class);
                    if (academy != null) {
                        System.out.println("1. Просмотреть существующих воинов");
                        System.out.println("2. Создать нового воина");

                        int academyChoice = scanner.nextInt();
                        scanner.nextLine();

                        switch (academyChoice) {
                            case 1:
                                Buildings.Academy.showAllUnits(); // Показать всех воинов
                                break;
                            case 2:
                                academy.createNewUnit(scanner,resources); // Создать нового воина
                                break;
                            default:
                                System.out.println("Неверный выбор. Попробуйте снова.");
                                System.out.println("-----------------------------------");
                        }
                    }
                    break;
                case 6:
                    System.out.println("-----------------------------------");
                    // Проверяем, построен ли рынок, перед вызовом метода trade
                    Buildings.Market market = (Buildings.Market) city.getBuilding(Buildings.Market.class);
                    if (market != null) {
                        market.trade(resources,scanner); // Вызов метода `trade`
                    } else {
                        System.out.println("Рынок еще не построен.");
                        System.out.println("-----------------------------------");
                    }
                    break;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
                    System.out.println("-----------------------------------");
                    break;
            }
        }
    }
    private static void buildBuilding(City city, Scanner scanner, PlayerProgress playerProgress) {
        Resources resources = city.getResources(); // Получаем текущие ресурсы

        Buildings.LeechHouse leechHouse = new Buildings.LeechHouse();
        Buildings.Forge forge = new Buildings.Forge();
        Buildings.Armory armory = new Buildings.Armory();
        Buildings.Tavern tavern = new Buildings.Tavern();
        Buildings.Academy academy = new Buildings.Academy();
        Buildings.Market market = new Buildings.Market();

        System.out.println("Выберите здание для постройки:");
        System.out.println("1. Дом лекаря (стоимость " + leechHouse.getStoneCost() + " камня и " + leechHouse.getWoodCost() + " дерева)");
        System.out.println("2. Кузница (стоимость " + forge.getStoneCost() + " камня и " + forge.getWoodCost() + " дерева)");
        System.out.println("3. Арсенал (стоимость " + armory.getStoneCost() + " камня и " + armory.getWoodCost() + " дерева)");
        System.out.println("4. Таверна (стоимость " + tavern.getStoneCost() + " камня и " + tavern.getWoodCost() + " дерева)");
        System.out.println("5. Академия (стоимость " + academy.getStoneCost() + " камня и " + academy.getWoodCost() + " дерева)");
        System.out.println("6. Рынок (стоимость " + market.getStoneCost() + " камня и " + market.getWoodCost() + " дерева)");
        System.out.println("Текущие ресурсы: Камень = " + resources.getStone() + ", Дерево = " + resources.getWood());

        int choice = scanner.nextInt();
        scanner.nextLine();

        Building newBuilding;
        switch (choice) {
            case 1:
                newBuilding = new Buildings.LeechHouse();
                break;
            case 2:
                newBuilding = new Buildings.Forge();
                break;
            case 3:
                newBuilding = new Buildings.Armory();
                break;
            case 4:
                newBuilding = new Buildings.Tavern();
                break;
            case 5:
                newBuilding = new Buildings.Academy();
                break;
            case 6:
                newBuilding = new Buildings.Market();
                break;
            default:
                System.out.println("Неверный выбор. Попробуйте снова.");
                return;
        }

        // Проверяем, хватает ли ресурсов для постройки
        if (city.getBuilding(newBuilding.getClass()) == null) { // Проверяем, существует ли здание
            if (resources.consume(newBuilding.getStoneCost(), newBuilding.getWoodCost())) {
                city.addBuilding(newBuilding); // Добавляем здание, если ресурсов достаточно
                System.out.println("Построено здание: " + newBuilding);
                playerProgress.saveProgress("game_progress.dat"); // Сохраняем прогресс после постройки
            } else {
                System.out.println("Недостаточно ресурсов для постройки " + newBuilding.getName());
            }
        } else {
            System.out.println("Здание уже построено: " + newBuilding.getName());
        }
    }

    private static void upgradeBuilding(City city, Scanner scanner, PlayerProgress playerProgress) {
        city.displayBuildings();

        Resources resources = city.getResources(); // Получаем ресурсы
        System.out.println("Текущие ресурсы: Камень = " + resources.getStone() + ", Дерево = " + resources.getWood());

        System.out.println("Выберите здание для улучшения (введите номер):");
        int buildingChoice = scanner.nextInt();

        List<Building> buildings = city.getBuildings(); // Получаем все здания
        if (buildingChoice >= 1 && buildingChoice <= buildings.size()) {
            Building buildingToUpgrade = buildings.get(buildingChoice - 1);

            int stoneCost = buildingToUpgrade.getStoneCost(); // Стоимость улучшения в камне
            int woodCost = buildingToUpgrade.getWoodCost(); // Стоимость улучшения в дереве

            System.out.println("Стоимость улучшения " + buildingToUpgrade.getName() + " составит " + stoneCost + " камня и " + woodCost + " дерева.");

            System.out.println("Хотите продолжить улучшение? (1 - Да, 0 - Нет)");
            int confirm = scanner.nextInt(); // Подтверждение улучшения
            if (confirm == 1) {
                if (resources.getStone() >= stoneCost && resources.getWood() >= woodCost) {
                    buildingToUpgrade.upgrade(resources); // Улучшаем здание
                    System.out.println("Здание улучшено: " + buildingToUpgrade);
                    playerProgress.saveProgress("game_progress.dat"); // Сохраняем прогресс после улучшения
                } else {
                    System.out.println("Недостаточно ресурсов для улучшения " + buildingToUpgrade.getName());
                }
            } else {
                System.out.println("Улучшение отменено.");
            }
        } else {
            System.out.println("Некорректный выбор. Попробуйте снова.");
        }
    }

}
