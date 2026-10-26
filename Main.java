import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Main {


    static ArrayList<Integer> requestIds = new ArrayList<>();
    static ArrayList<String> studentNames = new ArrayList<>();
    static ArrayList<String> bookTitles = new ArrayList<>();
    static ArrayList<Integer> pubYears = new ArrayList<>();
    static ArrayList<String> statuses = new ArrayList<>();

    static Queue<Integer> requestQueue = new LinkedList<>();

    static int nextRequestId = 1001;
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int choice;
        do {
            showMenu();
            choice = readInt("Enter choice: ", 0, 10);
            System.out.println();
            switch (choice) {
                case 1: registerRequest(); break;
                case 2: displayRecords(); break;
                case 3: searchRequest(); break;
                case 4: enqueueRequest(); break;
                case 5: processNext(); break;
                case 6: peekNext(); break;
                case 7: displayQueue(); break;
                case 8: checkQueueEmpty(); break;
                case 9: bubbleSortRecords(); break;
                case 10: insertionSortRecords(); break;
                case 0: System.out.println("Exiting program. Goodbye."); break;
            }
        } while (choice != 0);
        sc.close();
    }


    static void showMenu() {
        System.out.println("\n      LIBRARY BORROWING REQUEST SYSTEM      ");
        System.out.println(" 1. Register borrowing request");
        System.out.println(" 2. Display all records");
        System.out.println(" 3. Search request");
        System.out.println(" 4. Enqueue request (add to queue)");
        System.out.println(" 5. Process next request (dequeue)");
        System.out.println(" 6. Peek next request");
        System.out.println(" 7. Display queue");
        System.out.println(" 8. Check if queue is empty");
        System.out.println(" 9. Bubble Sort records (name / title)");
        System.out.println("10. Insertion Sort records (request no. / year)");
        System.out.println(" 0. Exit");
    }

    static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                int value = Integer.parseInt(line);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("Error: enter a number from " + min + " to " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Error: invalid input. Enter a whole number.");
            }
        }
    }

    static String readText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String text = sc.nextLine().trim();
            if (!text.isEmpty()) {
                return text;
            }
            System.out.println("Error: input cannot be empty.");
        }
    }

    static void registerRequest() {
        String name = readText("Student name: ");
        String title = readText("Book title: ");
        int year = readInt("Publication year (1000-2026): ", 1000, 2026);

        requestIds.add(nextRequestId);
        studentNames.add(name);
        bookTitles.add(title);
        pubYears.add(year);
        statuses.add("Registered");

        System.out.println("Request registered. Request No.: " + nextRequestId);
        nextRequestId++;
    }

    static void displayRecords() {
        if (requestIds.isEmpty()) {
            System.out.println("No records stored.");
            return;
        }
        System.out.printf("%-10s %-20s %-30s %-6s %-10s%n",
                "Request#", "Student", "Book Title", "Year", "Status");
        System.out.println("-------------------------------------------------------------------------------");
        for (int i = 0; i < requestIds.size(); i++) {
            printRecord(i);
        }
    }

    static void printRecord(int i) {
        System.out.printf("%-10d %-20s %-30s %-6d %-10s%n",
                requestIds.get(i), studentNames.get(i), bookTitles.get(i),
                pubYears.get(i), statuses.get(i));
    }

    static int findIndexById(int id) {
        for (int i = 0; i < requestIds.size(); i++) {
            if (requestIds.get(i) == id) {
                return i;
            }
        }
        return -1;
    }

    static void searchRequest() {
        if (requestIds.isEmpty()) {
            System.out.println("No records to search.");
            return;
        }
        System.out.println("Search by: 1. Request number  2. Student name");
        int option = readInt("Choice: ", 1, 2);
        boolean found = false;

        if (option == 1) {
            int id = readInt("Request number: ", 1, 999999);
            int idx = findIndexById(id);
            if (idx != -1) {
                System.out.println("Record found:");
                printRecord(idx);
                found = true;
            }
        } else {
            String keyword = readText("Student name (partial allowed): ").toLowerCase();
            for (int i = 0; i < studentNames.size(); i++) {
                if (studentNames.get(i).toLowerCase().contains(keyword)) {
                    if (!found) {
                        System.out.println("Matching records:");
                    }
                    printRecord(i);
                    found = true;
                }
            }
        }
        if (!found) {
            System.out.println("No matching record found.");
        }
    }

    // ===== Queue operations =====
    static void enqueueRequest() {
        if (requestIds.isEmpty()) {
            System.out.println("No records available. Register a request first.");
            return;
        }
        int id = readInt("Request number to enqueue: ", 1, 999999);
        int idx = findIndexById(id);
        if (idx == -1) {
            System.out.println("Request not found.");
            return;
        }
        if (!statuses.get(idx).equals("Registered")) {
            System.out.println("Request is already " + statuses.get(idx).toLowerCase() + ". Cannot enqueue.");
            return;
        }
        requestQueue.add(id);
        statuses.set(idx, "Queued");
        System.out.println("Request " + id + " added to the queue. Position: " + requestQueue.size());
    }

    static void processNext() {
        if (requestQueue.isEmpty()) {
            System.out.println("Queue is empty. No request to process.");
            return;
        }
        int id = requestQueue.poll();
        int idx = findIndexById(id);
        statuses.set(idx, "Processed");
        System.out.println("Processing request (first in, first out):");
        printRecord(idx);
    }

    static void peekNext() {
        if (requestQueue.isEmpty()) {
            System.out.println("Queue is empty. Nothing to peek.");
            return;
        }
        int idx = findIndexById(requestQueue.peek());
        System.out.println("Next request in line:");
        printRecord(idx);
    }

    static void displayQueue() {
        if (requestQueue.isEmpty()) {
            System.out.println("Queue is empty.");
            return;
        }
        System.out.println("Current queue (front to back):");
        int position = 1;
        for (int id : requestQueue) {
            int idx = findIndexById(id);
            System.out.println(position + ". Request " + id + " | " + studentNames.get(idx)
                    + " | " + bookTitles.get(idx));
            position++;
        }
    }

    static void checkQueueEmpty() {
        if (requestQueue.isEmpty()) {
            System.out.println("The queue is empty.");
        } else {
            System.out.println("The queue is not empty. Pending requests: " + requestQueue.size());
        }
    }

    // ===== Sorting =====
    // Swaps record i and j in ALL lists so they stay synchronized.
    static void swapRecords(int i, int j) {
        int tId = requestIds.get(i);
        requestIds.set(i, requestIds.get(j));
        requestIds.set(j, tId);

        String tName = studentNames.get(i);
        studentNames.set(i, studentNames.get(j));
        studentNames.set(j, tName);

        String tTitle = bookTitles.get(i);
        bookTitles.set(i, bookTitles.get(j));
        bookTitles.set(j, tTitle);

        int tYear = pubYears.get(i);
        pubYears.set(i, pubYears.get(j));
        pubYears.set(j, tYear);

        String tStatus = statuses.get(i);
        statuses.set(i, statuses.get(j));
        statuses.set(j, tStatus);
    }

    // Bubble Sort: alphabetical sort of text fields (student name or book title)
    static void bubbleSortRecords() {
        if (requestIds.size() < 2) {
            System.out.println("Need at least 2 records to sort.");
            return;
        }
        System.out.println("Bubble Sort by: 1. Student name  2. Book title");
        int field = readInt("Choice: ", 1, 2);
        ArrayList<String> key = (field == 1) ? studentNames : bookTitles;

        System.out.println("\nBEFORE SORTING:");
        displayRecords();

        int n = requestIds.size();
        for (int pass = 0; pass < n - 1; pass++) {
            boolean swapped = false;
            for (int j = 0; j < n - 1 - pass; j++) {
                if (key.get(j).compareToIgnoreCase(key.get(j + 1)) > 0) {
                    swapRecords(j, j + 1);
                    swapped = true;
                }
            }
            if (!swapped) {
                break;
            }
        }

        System.out.println("\nAFTER SORTING (A-Z):");
        displayRecords();
        System.out.println("\nNote: queue order is unchanged by sorting.");
    }

    // Insertion Sort: ascending sort of numeric fields (request number or year)
    static void insertionSortRecords() {
        if (requestIds.size() < 2) {
            System.out.println("Need at least 2 records to sort.");
            return;
        }
        System.out.println("Insertion Sort by: 1. Request number  2. Publication year");
        int field = readInt("Choice: ", 1, 2);
        ArrayList<Integer> key = (field == 1) ? requestIds : pubYears;

        System.out.println("\nBEFORE SORTING:");
        displayRecords();

        int n = requestIds.size();
        for (int i = 1; i < n; i++) {
            int j = i;
            while (j > 0 && key.get(j - 1) > key.get(j)) {
                swapRecords(j - 1, j);
                j--;
            }
        }

        System.out.println("\nAFTER SORTING (ascending):");
        displayRecords();
        System.out.println("\nNote: queue order is unchanged by sorting.");
    }
}
