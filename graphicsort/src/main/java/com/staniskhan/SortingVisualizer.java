package com.staniskhan;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class SortingVisualizer extends Application {

    private double width = 900;
    private double height = 500;
    private double spacing = 10;
    private double delay = 100;

    private ArrayList<Integer> originalNumbers;
    private Pane bubbleSortPane;
    private Pane insertionSortPane;
    private Pane quickSortPane;
    private Button startButton;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setResizable(false);
        // Чтение чисел из файла
        originalNumbers = readNumbersFromFile("input.txt");
        if (originalNumbers.isEmpty()) {
            System.out.println("Файл пуст или не найден");
            return;
        }

        // Создание заголовков
        Label bubbleLabel = new Label("Bubble sort");
        Label insertionLabel = new Label("Insertion sort");
        Label quickLabel = new Label("Quick sort");

        // Создание панелей для визуализации
        bubbleSortPane = createSortingPane();
        insertionSortPane = createSortingPane();
        quickSortPane = createSortingPane();

        // Инициализация столбцов
        updateBars(bubbleSortPane, originalNumbers);
        updateBars(insertionSortPane, originalNumbers);
        updateBars(quickSortPane, originalNumbers);

        // Создание контейнеров для каждой сортировки
        VBox bubbleBox = new VBox(5, bubbleLabel, bubbleSortPane);
        VBox insertionBox = new VBox(5, insertionLabel, insertionSortPane);
        VBox quickBox = new VBox(5, quickLabel, quickSortPane);

        // Кнопка запуска
        startButton = new Button("Запуск сортировки");
        startButton.setOnAction(e -> startSorting());

        // Основной layout
        HBox sortingPanes = new HBox(spacing);
        sortingPanes.getChildren().addAll(bubbleBox, insertionBox, quickBox);

        VBox root = new VBox(10);
        root.getChildren().addAll(startButton, sortingPanes);

        Scene scene = new Scene(root, width, height);
        primaryStage.setTitle("Визуализация сортировок");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private ArrayList<Integer> readNumbersFromFile(String filename) {
        ArrayList<Integer> numbers = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                numbers.add(Integer.parseInt(line.trim()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return numbers;
    }

    private Pane createSortingPane() {
        Pane pane = new Pane();
        pane.setStyle("-fx-border-color: black; -fx-border-width: 1;");
        pane.setPrefSize((width - 2 * spacing) / 3, height - 100);
        return pane;
    }

    private void startSorting() {
        startButton.setDisable(true);
        
        // Создаем копии данных для каждой сортировки
        ArrayList<Integer> bubbleNumbers = new ArrayList<>(originalNumbers);
        ArrayList<Integer> insertionNumbers = new ArrayList<>(originalNumbers);
        ArrayList<Integer> quickNumbers = new ArrayList<>(originalNumbers);

        // Переинициализируем столбцы
        updateBars(bubbleSortPane, bubbleNumbers);
        updateBars(insertionSortPane, insertionNumbers);
        updateBars(quickSortPane, quickNumbers);

        // Запускаем сортировки
        // Runnable bubbleSort = ()->{
        //     startBubbleSort(bubbleSortPane, bubbleNumbers);
        // };
        // Thread b = new Thread(bubbleSort);
        // b.start();

        // Runnable insertionSort = ()->{
        //     startInsertionSort(insertionSortPane, insertionNumbers);
        // };
        // Thread i = new Thread(insertionSort);
        // i.start();

        // Runnable quickSort = ()->{
        //     startQuickSort(quickSortPane, quickNumbers);
        // };

        // Thread q = new Thread(quickSort);

        // q.start();
        
        startBubbleSort(bubbleNumbers);
        // startInsertionSort(insertionSortPane, insertionNumbers);
        // startQuickSort(quickSortPane, quickNumbers);
    }

    private void updateBars(Pane pane, ArrayList<Integer> numbers) {
        pane.getChildren().clear();
        double barWidth = pane.getPrefWidth() / numbers.size();
        double maxValue = numbers.stream().max(Integer::compareTo).orElse(1);

        for (int i = 0; i < numbers.size(); i++) {
            double barHeight = (numbers.get(i) / maxValue) * (pane.getPrefHeight() - 10);
            Rectangle bar = new Rectangle(
                i * barWidth,
                pane.getPrefHeight() - barHeight,
                barWidth - 1,
                barHeight
            );
            bar.setFill(Color.BLUE);
            pane.getChildren().add(bar);
        }
    }

    private void startBubbleSort(ArrayList<Integer> numbers) {
        for (int i = 0; i < numbers.size() - 1; i++)
        {
            for (int j = 1; j < numbers.size() - i; j++)
            {
                if (numbers.get(j) < numbers.get(j-1))
                {
                    int swap = numbers.get(j);
                    numbers.set(j, numbers.get(j-1));
                    numbers.set(j - 1, swap);
                    try
                    {
                        Thread.sleep(300);
                    }
                    catch(InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    updateBars(bubbleSortPane, numbers);
                }
            }
        }
    }

    // private void startInsertionSort(Pane pane, List<Integer> numbers) {
        

    // }

    // private void startQuickSort(Pane pane, List<Integer> numbers) {

    // }

    // private int partition(List<Integer> numbers, int low, int high) {

    // }

    public static void main(String[] args) {
        launch(args);
    }
}