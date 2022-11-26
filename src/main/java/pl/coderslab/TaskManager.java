package pl.coderslab;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TaskManager {
    static String fileName = "tasks.csv";
    static String[] allOptions = {"add", "remove", "list", "exit"};
    static String[][] tasks;

    public static void printOptions(String[] table) {
        System.out.println(ConsoleColors.CYAN);
        System.out.println("Please select an option: " + ConsoleColors.RED);
        for (String option : table) {
            System.out.println(option);
        }
    }

    public static void main(String[] args) {
        tasks = loadDataToTab(fileName);
        printOptions(allOptions);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();

            switch (input) {
                case "add":
                    addTask();
                    break;
                case "remove":
                    removeTask(tasks, getTheNumber());
                    System.out.println("Task deleted.");
                    break;
                case "list":
                    printList(tasks);
                    break;
                case "exit":
                    saveToFile(fileName, tasks);
                    System.out.println(ConsoleColors.RED + "Bye, bye.");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Please select a correct option.");
            }

            printOptions(allOptions);
        }
    }

    public static boolean isNumberGreaterEqualZero(String input) {
        if (NumberUtils.isParsable(input)) {
            return Integer.parseInt(input) >= 0;
        }
        return false;
    }

    public static int getTheNumber() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please select number of option you want to remove.");

        String n = scanner.nextLine();
        while (!isNumberGreaterEqualZero(n)) {
            System.out.println("Incorrect number. Choose 0 or greater");
            scanner.nextLine();
        }
        return Integer.parseInt(n);
    }

    public static void addTask() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please add task description");
        String description = scanner.nextLine();
        System.out.println("Please add task due date");
        String date = scanner.nextLine();
        System.out.println("Is your task important: true/false");
        String important = scanner.nextLine();
        tasks = Arrays.copyOf(tasks, tasks.length + 1);
        tasks[tasks.length - 1] = new String[3];
        tasks[tasks.length - 1][0] = description;
        tasks[tasks.length - 1][1] = date;
        tasks[tasks.length - 1][2] = important;
    }

    public static void removeTask(String[][] tab, int index) {
        try {
            if (index < tab.length) {
                tasks = ArrayUtils.remove(tab, index);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Element not exist.");
        }
    }

    public static void printList(String[][] tab) {
        for (int i = 0; i < tab.length; i++) {
            System.out.print(i + " : ");
            for (int j = 0; j < tab[i].length; j++) {
                System.out.print(tab[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static String[][] loadDataToTab(String fileName) {
        Path directory = Paths.get(fileName);
        if (!Files.exists(directory)) {
            System.out.println("File not exist.");
            System.exit(0);
        }
        String[][] table = null;
        try {
            List<String> strings = Files.readAllLines(directory);
            table = new String[strings.size()][strings.get(0).split(",").length];

            for (int i = 0; i < strings.size(); i++) {
                String[] splited = strings.get(i).split(",");
                for (int j = 0; j < splited.length; j++) {
                    table[i][j] = splited[j];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return table;
    }

    public static void saveToFile(String fileName, String[][] tab) {
        Path directory = Paths.get(fileName);
        String[] lines = new String[tasks.length];
        for (int i = 0; i < tab.length; i++) {
            lines[i] = String.join(",", tab[i]);
        }
        try {
            Files.write(directory, Arrays.asList(lines));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
