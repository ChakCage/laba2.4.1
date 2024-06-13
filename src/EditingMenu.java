import java.io.File;
import java.util.List;
import java.util.Scanner;

public class EditingMenu {
    private MapEditor editor;

    public EditingMenu() {
        this.editor = new MapEditor();

    }

    public void run(Scanner scanner) {
        while (true) {
            System.out.println("Меню редактирования карты:");
            System.out.println("1. Показать карту");
            System.out.println("2. Поставить препятствие на поле");
            System.out.println("3. Создать новое препятствие");
            System.out.println("4. Меню удаления препятствий");
            System.out.println("5. Удалить карту");
            System.out.println("6. Сохранить карту в файл");
            System.out.println("7. Редактировать существующую карту");
            System.out.println("8. Создать новую карту");
            System.out.println("9. Просмотреть доступные препятствия");
            System.out.println("10. Выйти");

            System.out.print("Выберите действие: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    editor.displayMap();
                    break;
                case 2:
                    editor.addObstacleMenu(scanner);
                    break;
                case 3:
                    editor.addCustomObstacleMenu(scanner);
                    break;
                case 4:
                    System.out.println("Меню удаления препятствия:");
                    System.out.println("1. Удалить препятствие с текущей карты");
                    System.out.println("2. Удалить препятствие из списка препятствий");
                    System.out.print("Выберите действие: ");
                    int removeChoice = scanner.nextInt();
                    scanner.nextLine();
                    switch (removeChoice) {
                        case 1:
                            editor.removeObstacle(scanner);
                            break;
                        case 2:
                            editor.removeObstacleMenu(scanner);
                            break;
                        default:
                            System.out.println("Некорректный выбор.");
                            break;
                    }
                    break;
                case 5:
                    editor.deleteMap(scanner);
                    break;
                case 6:
                    System.out.print("Введите имя файла для сохранения редактора карт: ");
                    String editorFilename = scanner.next();
                    String NameMap = "map_" + editorFilename + ".txt";
                    editor.updateUniqueObstaclesOwner(NameMap);
                    editor.saveMapToFile(NameMap);
                    break;
                case 7:
                    editor.displayExistingMaps();
                    System.out.print("Выберите номер карты для редактирования: ");
                    int mapChoice = scanner.nextInt();

                    File directory = new File(".");
                    File[] files = directory.listFiles((dir, name) -> name.startsWith("map_") && name.endsWith(".txt"));
                    if (files != null && mapChoice > 0 && mapChoice <= files.length) {
                        String mapName = files[mapChoice - 1].getName();
                        editor.setCurrentMapFileName(mapName);

                        List<List<Field>> loadedMap = MapEditor.loadMapFromFile(mapName);
                        if (loadedMap != null) {
                            System.out.println("Карта успешно загружена.");

                            // Создаем новый редактор и передаем загруженную карту
                            MapEditor newEditor = new MapEditor();
                            newEditor.setMapList(loadedMap);  // Устанавливаем карту в новый редактор

                            newEditor.editMapMenu(scanner, mapName);  // Передаем имя файла и новый редактор для редактирования
                        } else {
                            System.out.println("Не удалось загрузить карту.");
                        }
                    } else {
                        System.out.println("Некорректный выбор карты.");
                    }
                    break;


                case 8:
                    int size = 9;

                    editor.createMap(size);
                    editor.setCurrentMapFileName(null);

                    break;
                case 9:
                    displayAvailableObstacles(scanner);
                    break;
                case 10:
                    System.out.println("Выход из редактора карт.");

                    return;
                default:
                    System.out.println("Неверный выбор. Пожалуйста, попробуйте еще раз.");
                    break;
            }
        }
    }

    private void displayAvailableObstacles(Scanner scanner) {
        editor.displayAvailableObstaclesMain(scanner);
    }


}
