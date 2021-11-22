public class SortEngine {

    static public boolean bubbleSort(String[][] directory) {
        long startTime = System.currentTimeMillis();
        boolean isSorted = false;
        while (!isSorted) {
            isSorted = true;
            for (int i = 0; i < directory.length - 1; i++) {
                String tempName = directory[i][0];
                String tempNumb = directory[i][1];
                if (tempName.compareTo(directory[i + 1][0]) > 0) {
                    directory[i][0] = directory[i + 1][0];
                    directory[i][1] = directory[i + 1][1];
                    directory[i + 1][0] = tempName;
                    directory[i + 1][1] = tempNumb;
                    isSorted = false;
                }
            }
            long endSort = System.currentTimeMillis();
            if (endSort - startTime > 20000) {
                return false;
            }
        }
        return true;
    }

    static public void quickSort(String[][] directory, int left, int right) {

        if (directory.length == 0 || left >= right) {
            return;
        }
        int middle = left + (right - left) / 2;
        String pivot = directory[middle][0];
        int i = left, j = right;
        while (i <= j) {
            while (directory[i][0].compareTo(pivot) < 0) {
                i++;
            }
            while (directory[j][0].compareTo(pivot) > 0) {
                j--;
            }
            if (i <= j) {
                String temp = directory[i][0];
                directory[i][0] = directory[j][0];
                directory[j][0] = temp;
                i++;
                j--;
            }
        }
        if (left < j) {
            quickSort(directory, left, j);
        }
        if (right > i) {
            quickSort(directory, i, right);
        }
    }
}
