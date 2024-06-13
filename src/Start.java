import java.util.Scanner;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.File;



public class Start {
    private static final Logger logger = Logger.getLogger(Start.class.getName());
    private static final FileHandler fileHandler;

    static {
        try {
            fileHandler = new FileHandler("Game_Log.txt");
            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL);
            logger.setUseParentHandlers(false);
        } catch (IOException e) {
            throw new ExceptionInInitializerError("Failed to initialize logger: " + e.getMessage());
        }
    }
    public static void StartGame(Scanner scanner) {
        boolean running = true;

        while (running) {
            System.out.println("Добро пожаловать в захватывающую MMO RPG Bauman Gates");
            System.out.println("Выберите действие:");
            System.out.println("1. Начать игру");
            System.out.println("2. Посмотреть описание игры и условные обозначения");
            System.out.println("3. Лор игры");
            System.out.println("4. Выбрать карту");
            System.out.println("5. Создание и редактирование карт");
            System.out.println("6. Меню города");
            System.out.println("7. Выйти из игры");
            System.out.println("-----------------------------------");
            System.out.print("Ваш выбор: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    startGame(scanner);
                    break;
                case 2:
                    showGameDescription(scanner);
                    break;
                case 3:
                    showGameLore(scanner);
                    break;
                case 4:
                    File[] mapFiles = new File(".").listFiles((dir, name) -> name.startsWith("map_") && name.endsWith(".txt"));
                    if (mapFiles != null && mapFiles.length > 0) {
                        System.out.println("Доступные карты:");
                        for (int i = 0; i < mapFiles.length; i++) {
                            System.out.println((i + 1) + ". " + mapFiles[i].getName());
                        }

                        System.out.print("Выберите номер карты: ");
                        int mapChoice = scanner.nextInt();

                        if (mapChoice > 0 && mapChoice <= mapFiles.length) {
                            String selectedMap = mapFiles[mapChoice - 1].getName();

                            Battlefield.loadMap(selectedMap); // Загружаем выбранную карту
                            ConfigManager.saveLastSelectedMap(selectedMap); // Сохраняем имя карты в файл конфигурации

                            System.out.println("Карта " + selectedMap + " выбрана.");
                        } else {
                            System.out.println("Некорректный выбор.");
                        }
                    } else {
                        System.out.println("Нет доступных карт.");
                    }
                    break;
                case 5:
                    EditingMenu editingMenu = new EditingMenu();
                    editingMenu.run(scanner);
                    break;
                case 6:
                    GameMenu.showCityMenu(scanner); // Меню города
                    break;
                case 7:
                    System.out.println("Выход из игры...");
                    running = false; // Завершаем цикл
                    scanner.close(); // Закрытие сканера
                    break;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }

    }


    private static void showGameDescription(Scanner scanner) {
        System.out.println("-----------------------------------");
        System.out.println("Описание игры и условные обозначения:");
        System.out.println("Добро пожаловать в захватывающую MMO RPG Bauman Gates!");
        System.out.println("Цель игры: победить противника, уничтожив всех его юнитов.");
        System.out.println("Ход игры: игрок и компьютер поочередно выполняют свои действия, перемещая юнитов и атакуя врагов.");
        System.out.println("Как играть: выберите своего юнита для перемещения или атаки, следуя инструкциям на экране.");
        System.out.println("-----------------------------------");
        System.out.println("Мои юниты:");
        System.out.println("🗡️ Мечник (Здоровье: 50, Защита: 8, Движение: 3, Урон: 5, Радиус атаки: 1, Цена: 10)");
        System.out.println("🪓 Топорщик (Здоровье: 45, Защита: 3, Движение: 4, Урон: 9, Радиус атаки: 1, Цена: 20)");
        System.out.println("🏹 Лучник (Здоровье: 30, Защита: 8, Движение: 2, Урон: 6, Радиус атаки: 5, Цена: 15)");
        System.out.println("🎯 Арбалетчик (Здоровье: 40, Защита: 3, Движение: 2, Урон: 7, Радиус атаки: 6, Цена: 23)");
        System.out.println("🛡️ Рыцарь (Здоровье: 30, Защита: 3, Движение: 6, Урон: 5, Радиус атаки: 1, Цена: 2)");
        System.out.println("🏇 Конный лучник (Здоровье: 25, Защита: 2, Движение: 5, Урон: 3, Радиус атаки: 3, Цена: 25)");
        System.out.println("-----------------------------------");
        System.out.println("Враги:");
        System.out.println("⚔️ Вражеский Мечник (Характеристики совпадают с юнитом Мечник)");
        System.out.println("⚒️ Вражеский Топорщик (Характеристики совпадают с юнитом Топорщик)");
        System.out.println("🏓 Вражеский Лучник (Характеристики совпадают с юнитом Тучник)");
        System.out.println("🎾 Вражеский Арбалетчик (Характеристики совпадают с юнитом Арбалетчик)");
        System.out.println("🧙 Вражеский Рыцарь (Характеристики совпадают с юнитом Рыцарь)");
        System.out.println("🐎 Вражеский Конный лучник (Характеристики совпадают с юнитом Конный лучник)");
        System.out.println("-----------------------------------");
        System.out.println("Введите '1', чтобы вернуться в меню.");
        System.out.print("Ваш выбор: ");
        String choice = scanner.nextLine();
        System.out.println("-----------------------------------");
        if (choice.equals("1")) {
            StartGame(scanner);
        } else {
            System.out.println("Неверный ввод. Возвращение в главное меню...");
            StartGame(scanner);
        }
    }

    private static void showGameLore(Scanner scanner) {
        System.out.println("-----------------------------------");
        System.out.println("Лор игры:");
        System.out.println("Мир Bauman Gates находится в состоянии бесконечной войны между двумя могущественными королевствами.");
        System.out.println("Жители этих земель привыкли к постоянным столкновениям и считают их частью обыденной жизни.");
        System.out.println("Каждое королевство стремится к господству над другим и готово прибегнуть к любым средствам, чтобы достичь своих целей.");
        System.out.println("Ваше королевство готово сразиться с врагами и принести победу своему народу!");
        System.out.println("-----------------------------------");
        System.out.println("Введите '1', чтобы вернуться в меню.");
        System.out.print("Ваш выбор: ");
        Integer choice = scanner.nextInt();
        System.out.println("-----------------------------------");
        if (choice.equals(1)) {
            StartGame(scanner);
        } else {
            System.out.println("Неверный ввод. Возвращение в главное меню...");
            StartGame(scanner);
        }

    }


    public static void startGame(Scanner scanner) {
        logger.info("Игра началась");
        Battlefield battlefield = new Battlefield(9);
        System.out.println("-----------------------------------");
        System.out.println("Загрузка игры...");
        System.out.println("-----------------------------------");

        PlayerProgress playerProgress = PlayerProgress.loadProgress("game_progress.dat"); // Загружаем прогресс
        if (playerProgress == null) {
            playerProgress = new PlayerProgress(); // Создаем новый, если нет сохраненного прогресса
        }

        City city = playerProgress.getCity(); // Получаем город игрока
        BattlePreparation battlePreparation = new BattlePreparation(); // Создаем объект для подготовки к бою

        System.out.println("Выберите метод начальной установки юнитов:");
        System.out.println("1. Начать игру с самостоятельным размещением юнитов");
        System.out.println("2. Выбрать пресет и начать игру");
        System.out.println("-----------------------------------");
        System.out.print("Ваш выбор: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        System.out.println("-----------------------------------");

        switch (choice) {
            case 1:
                SetUnit setUnit = new SetUnit(city, battlePreparation); // Передаем City и BattlePreparation
                setUnit.placeUnits(battlefield,scanner); // Размещаем юнитов
                Enemy enemy = new Enemy(battlefield);
                enemy.placeEnemyUnits(); // Размещаем врагов
                break;
            case 2:
                SetUnit.setupPresetUnits(battlefield, city, battlePreparation); // Используем готовый пресет
                break;
            default:
                System.out.println("Неверный выбор. Запустите игру заново.");
                return; // Завершаем метод, если выбор неверен
        }

        // Сохраняем прогресс после установки юнитов
        playerProgress.saveProgress("game_progress.dat");

        battlefield.display(); // Отображаем начальное поле

        // Цикл игры
        int playerTurnCounter = 0;
        while (!battlefield.isGameOver()) {
            System.out.println("Ход игрока:");

            playerTurnCounter++;

            int playerActionsLeft = 2; // Количество доступных действий для игрока в ходе

            while (playerActionsLeft > 0) {
                System.out.println("Выберите действие:");
                System.out.println("1. Переместить воина");
                System.out.println("2. Атаковать врага");
                System.out.println("3. Атаковать с катапульты");
                System.out.println("4. Атаковать башню");

                System.out.print("Введите номер действия: ");
                int action = scanner.nextInt();
                scanner.nextLine();

                switch (action) {
                    case 1:
                        Actions.moveUnit(battlefield,scanner);
                        break;
                    case 2:
                        Actions.attackEnemy(battlefield,scanner);
                        break;
                    case 3:
                        Actions.attackWithCatapult(battlefield,scanner);
                        break;
                    case 4:
                        Actions.attackTower(battlefield, scanner);
                        break;
                    default:
                        System.out.println("Неверный выбор. Попробуйте снова.");
                        continue; // Повтор действия
                }


                playerActionsLeft--;

                if (playerActionsLeft >= 1) {
                    battlefield.display(); // Показываем поле после действий
                }

                if (playerTurnCounter % 2 == 0) {
                    battlefield.resetCatapultUsage(); // Сброс использования катапульты
                }
            }

            System.out.println("Ход врага:");
            EnemyActions.performEnemyActions(battlefield); // Действия врага
            battlefield.display(); // Отображаем после действий врага
            System.out.println("-----------------------------------");
        }

        System.out.println("Игра окончена! Победил " + battlefield.getWinner());
        logger.info("Игра окончена! Победил " + battlefield.getWinner());
        playerProgress = PlayerProgress.loadProgress("game_progress.dat");
        if (playerProgress != null) {
            city = playerProgress.getCity();
            System.out.println("Обновленные ресурсы после игры: Камень = " + city.getResources().getStone() + ", Дерево = " + city.getResources().getWood());
        }
        fileHandler.close(); // Закрываем обработчик файла


    }

}

