public class Main {

    public static void main(String[] args) {
        String fileName = "src\\main\\resources\\find.txt";
        String directoryName = "src\\main\\resources\\directory.txt";
        SearchEngine searchEngine = new SearchEngine(fileName, directoryName);

        String[] book = new String[500]; // 500 100
        String[][] directory = new String[1014130][2]; // 1014130 1000

        searchEngine.loadList(book, directory); // выгрузка списков
        searchEngine.linearSearch(book, directory); // линейный поиск
        searchEngine.jumpSearch(book, directory); // поиск прыжками
        searchEngine.binarySearch(book, directory); // бинарный поиск
        searchEngine.instantSearch(book, directory); // hash поиск
    }
}
