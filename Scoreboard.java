import java.util.*;
import java.io.*;

public class Scoreboard {

    static ArrayList<String> names = new ArrayList<>();
    static ArrayList<Integer> scores = new ArrayList<>();
    static final String FILE_NAME = "scoreboard.txt";
    static Scanner kb = new Scanner(System.in);

    public static void main(String[] args) {
        loadFromFile();

        int choice;
        do {
            System.out.println("\nSelect an option:");
            System.out.println("1. Add Player");
            System.out.println("2. Delete Player");
            System.out.println("3. View Players");
            System.out.println("4. Display Scoreboard");
            System.out.println("5. Exit");
            System.out.print("Enter your choice [1-5]: ");

            while (!kb.hasNextInt()) {
                System.out.print("Invalid input. Enter a number [1-5]: ");
                kb.next();
            }
            choice = kb.nextInt();

            switch (choice) {
                case 1 -> addPlayer();
                case 2 -> deletePlayer();
                case 3 -> viewPlayers();
                case 4 -> displayScoreboard();
                case 5 -> saveToFile();
                default -> System.out.println("Invalid choice. Try again.");
            }

        } while (choice != 5);

        System.out.println("Goodbye!");
    }

    static void addPlayer() {
        kb.nextLine(); // consume newline
        System.out.print("Enter player name: ");
        String name = kb.nextLine();
        System.out.print("Enter player score: ");
        while (!kb.hasNextInt()) {
            System.out.print("Invalid score. Enter a number: ");
            kb.next();
        }
        int score = kb.nextInt();
        names.add(name);
        scores.add(score);
        System.out.println("Player added.");
    }

    static void deletePlayer() {
        if (names.isEmpty()) {
            System.out.println("No players to delete.");
            return;
        }
        viewPlayers();
        System.out.printf("Enter index to delete [0 - %d]: ", names.size() - 1);
        while (!kb.hasNextInt()) {
            System.out.print("Invalid input. Enter a valid index: ");
            kb.next();
        }
        int index = kb.nextInt();
        if (index < 0 || index >= names.size()) {
            System.out.println("Invalid index.");
            return;
        }
        System.out.printf("Deleted %s with score %d.%n", names.remove(index), scores.remove(index));
    }

    static void viewPlayers() {
        if (names.isEmpty()) {
            System.out.println("No players to display.");
            return;
        }
        System.out.println("\nPlayer List:");
        for (int i = 0; i < names.size(); i++) {
            System.out.printf("[%d] %s - %d%n", i, names.get(i), scores.get(i));
        }
    }

    static void displayScoreboard() {
        if (names.isEmpty()) {
            System.out.println("No players to display.");
            return;
        }

        // Sort by score descending
        for (int i = 0; i < scores.size() - 1; i++) {
            for (int j = i + 1; j < scores.size(); j++) {
                if (scores.get(j) > scores.get(i)) {
                    Collections.swap(scores, i, j);
                    Collections.swap(names, i, j);
                }
            }
        }

        int maxScore = scores.get(0);
        int maxNameLength = names.stream().mapToInt(String::length).max().orElse(0);

        System.out.println("\nScoreboard:");
        for (int i = 0; i < names.size(); i++) {
            String paddedName = String.format("%-" + maxNameLength + "s", names.get(i));
            int stars = (maxScore == 0) ? 0 : (scores.get(i) * 50) / maxScore;
            System.out.printf("%s | %s%n", paddedName, "*".repeat(stars));
        }
    }

    static void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (int i = 0; i < names.size(); i++) {
                writer.println(names.get(i) + "," + scores.get(i));
            }
            System.out.println("Data saved to file.");
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    static void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String[] parts = fileScanner.nextLine().split(",");
                if (parts.length == 2) {
                    names.add(parts[0]);
                    scores.add(Integer.parseInt(parts[1]));
                }
            }
            System.out.println("Loaded previous data.");
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}