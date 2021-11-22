import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

public class SearchEngine {

    private final File book;
    private final File fullDirectory;
    private String[][] bookLinear;
    private String[][] bookJump;
    private String[][] bookBinary;
    private String[][] bookHash;

    SearchEngine(String fileName, String directoryName) {

        book = new File(fileName);
        fullDirectory = new File(directoryName);
    }

    // линейный поиск номеров телефона без сортировки директории
    public void linearSearch(String[] listFounded, String[][] directoryList) {

        bookLinear = new String[listFounded.length][2];
        int countFounded = 0;
        long start = System.currentTimeMillis(); // время начала поиска
        System.out.println("Start searching (linear search)...");
        for (int i = 0; i < listFounded.length; i++) {
            bookLinear[i][0] = listFounded[i];
            for (int j = 0; j < directoryList.length; j++) {
                if (bookLinear[i][0].equals(directoryList[j][0])) {
                    bookLinear[i][1] = directoryList[j][1];
                    countFounded++;
                    break;
                }
            }
        }
        long end = System.currentTimeMillis() - start; // время окончания поиска
        long min = end / 1000 / 60;
        long sec = end / 1000 % 60;
        long mil = end - sec * 1000;
        System.out.printf("Found %d / %d entries. Time taken %d min. %d sec. %d ms.\n\n",
                countFounded, listFounded.length, min, sec, mil);
    }

    // поиск прыжками и пузырьковая сортировка
    public void jumpSearch(String[] listFounded, String[][] directoryList) {

        long start = System.currentTimeMillis(); // время начала работы
        System.out.println("Start searching (bubble sort + jump search)...");
        boolean isBreak = false;
        int countFounded = 0;
        long endSort = 0;
        if (!SortEngine.bubbleSort(directoryList)) { // если работает слишком долго включаем линейный поиск
            endSort = System.currentTimeMillis() - start; // время окончания сортировки
            isBreak = true;
            for (int i = 0; i < listFounded.length; i++) {
                bookLinear[i][0] = listFounded[i];
                for (int j = 0; j < directoryList.length; j++) {
                    if (bookLinear[i][0].equals(directoryList[j][0])) {
                        bookLinear[i][1] = directoryList[j][1];
                        countFounded++;
                        break;
                    }
                }
            }
        } else {
            endSort = System.currentTimeMillis() - start; // время окончания сортировки
            // алгоритм поиска
            bookJump = new String[listFounded.length][2];
            int jumpStep = (int) Math.sqrt(directoryList.length); // шаг прыжка
            for (int i = 0; i < listFounded.length; i++) {  // проходим по всем элементам искомого массива
                int pervStep = 0;
                // определяем границу поиска
                // пока элемент по индексу меньше нужного
                while (directoryList[pervStep][0].compareTo(listFounded[i]) < 1) {
                    pervStep += jumpStep;
                    if (pervStep >= directoryList.length) {
                        pervStep = directoryList.length;
                        break;
                    }
                }
                // подключаем линейный поиск (от большего к меньшему)
                for (int j = pervStep; j > 0; j--) {
                    if (directoryList[j][0].equals(listFounded[i])) {
                        bookJump[i][0] = directoryList[j][0];
                        bookJump[i][1] = directoryList[j][1];
                        countFounded++;
                    }
                }
            }
        }
        long endSearch = System.currentTimeMillis() - start; // время окончания поиска
        long minAll = endSearch / 1000 / 60;
        long secAll = endSearch / 1000 % 60;
        long milAll = endSearch - secAll * 1000;
        System.out.printf("Found %d / %d entries. Time taken %d min. %d sec. %d ms.\n",
                countFounded, listFounded.length, minAll, secAll, milAll);
        long minSort = endSort / 1000 / 60;
        long secSort = endSort / 1000 % 60;
        long milSort = endSort - secSort * 1000;
        System.out.printf("Sorting time: %d min. %d sec. %d ms." + (isBreak ? " - STOPPED, moved to linear search\n" : "\n"),
                minSort, secSort, milSort);
        long minSearch = (endSearch - endSort) / 1000 / 60;
        long secSearch = (endSearch - endSort) / 1000 % 60;
        long milSearch = (endSearch - endSort) - secSearch * 1000;
        System.out.printf("Searching time: %d min. %d sec. %d ms.\n\n",
                minSearch, secSearch, milSearch);
    }

    // бинарный поиск и быстрая сортировка
    public void binarySearch(String[] listFounded, String[][] directoryList) {

        long start = System.currentTimeMillis(); // время начала работы
        System.out.println("Start searching (quick sort + binary search)...");
        SortEngine.quickSort(directoryList, 0, directoryList.length - 1);
        long endSort = System.currentTimeMillis() - start; // время окончания сортировки
        // алгоритм поиска
        bookBinary = new String[listFounded.length][2];
        int countFounded = 0;
        for (int i = 0; i < listFounded.length; i++) {
            int left = 0;
            int right = directoryList.length - 1;
            while (left <= right) {
                int middleIndex = (left + right) / 2;
                if (listFounded[i].equals(directoryList[middleIndex][0])) {
                    bookBinary[i][0] = directoryList[middleIndex][0];
                    bookBinary[i][1] = directoryList[middleIndex][1];
                    countFounded++;
                    break;
                } else if (listFounded[i].compareTo(directoryList[middleIndex][0]) > 0) {
                    left = middleIndex + 1;
                } else {
                    right = middleIndex - 1;
                }
            }
        }
        long endSearch = System.currentTimeMillis() - start; // время окончания поиска
        long minAll = endSearch / 1000 / 60;
        long secAll = endSearch / 1000 % 60;
        long milAll = endSearch - secAll * 1000;
        System.out.printf("Found %d / %d entries. Time taken %d min. %d sec. %d ms.\n",
                countFounded, listFounded.length, minAll, secAll, milAll);
        long minSort = endSort / 1000 / 60;
        long secSort = endSort / 1000 % 60;
        long milSort = endSort - secSort * 1000;
        System.out.printf("Sorting time: %d min. %d sec. %d ms.\n",
                minSort, secSort, milSort);
        long minSearch = (endSearch - endSort) / 1000 / 60;
        long secSearch = (endSearch - endSort) / 1000 % 60;
        long milSearch = (endSearch - endSort) - secSearch * 1000;
        System.out.printf("Searching time: %d min. %d sec. %d ms.\n\n",
                minSearch, secSearch, milSearch);
    }

    // поиск с помощью HashMap
    public void instantSearch(String[] listFounded, String[][] directoryList) {

        bookHash = new String[listFounded.length][2];
        int countFounded = 0;
        long start = System.currentTimeMillis(); // время начала поиска
        HashMap<String, String> hashDirectory = new HashMap<>();
        for (String[] strings : directoryList) {
            hashDirectory.put(strings[0], strings[1]);
        }
        long endCreate = System.currentTimeMillis() - start; // время окончания создания структуры
        System.out.println("Start searching (hash table)...");
        for (int i = 0; i < listFounded.length; i++) {
            bookHash[i][0] = listFounded[i];
            if (hashDirectory.containsKey(bookHash[i][0])) {
                bookHash[i][1] = hashDirectory.get(bookHash[i][0]);
                countFounded++;
            }
        }
        long endSearch = System.currentTimeMillis() - start; // время окончания поиска
        long minAll = endSearch / 1000 / 60;
        long secAll = endSearch / 1000 % 60;
        long milAll = endSearch - secAll * 1000;
        System.out.printf("Found %d / %d entries. Time taken %d min. %d sec. %d ms.\n",
                countFounded, listFounded.length, minAll, secAll, milAll);
        long minSort = endCreate / 1000 / 60;
        long secSort = endCreate / 1000 % 60;
        long milSort = endCreate - secSort * 1000;
        System.out.printf("Creating time: %d min. %d sec. %d ms.\n",
                minSort, secSort, milSort);
        long minSearch = (endSearch - endCreate) / 1000 / 60;
        long secSearch = (endSearch - endCreate) / 1000 % 60;
        long milSearch = (endSearch - endCreate) - secSearch * 1000;
        System.out.printf("Searching time: %d min. %d sec. %d ms.\n\n",
                minSearch, secSearch, milSearch);
    }

    // выгрузка данных имен из файла в массив
    public void loadList(String[] listFounded, String[][] directoryList) {
        int count = 0;
        try (Scanner scanner = new Scanner(book)) {
            while (scanner.hasNext()) {
                String name = scanner.nextLine();
                listFounded[count] = " " + name;
                count++;
            }
            loadDirectory(directoryList);
        } catch (Exception e) {
            System.out.println("Error loading book file");
        }
    }

    // выгрузка из файла полного списка номер-имя
    public void loadDirectory(String[][] directoryList) {
        try (Scanner scanner = new Scanner(fullDirectory)) {
            int count = 0;
            while (scanner.hasNext()) {
                String phoneNumber = scanner.next();
                String name = scanner.nextLine();
                directoryList[count][0] = name;
                directoryList[count][1] = phoneNumber;
                count++;
            }
        } catch (Exception e) {
            System.out.println("Error loading directory file");
        }
    }
}
