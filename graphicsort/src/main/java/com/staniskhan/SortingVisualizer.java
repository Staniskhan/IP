package com.staniskhan;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.application.Platform;
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
    private int delay = 50;

    private ArrayList<Integer> originalNumbers;
    private Pane bubbleSortPane;
    private Pane insertionSortPane;
    private Pane quickSortPane;
    private Button startButton;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setResizable(false);
        originalNumbers = readNumbersFromFile("input_files/input.txt");
        if (originalNumbers.isEmpty()) {
            System.out.println("Файл пуст или не найден");
            return;
        }

        Label bubbleLabel = new Label("Bubble sort");
        Label insertionLabel = new Label("Insertion sort");
        Label quickLabel = new Label("Quick sort");

        bubbleSortPane = createSortingPane();
        insertionSortPane = createSortingPane();
        quickSortPane = createSortingPane();

        updateBars(bubbleSortPane, originalNumbers);
        updateBars(insertionSortPane, originalNumbers);
        updateBars(quickSortPane, originalNumbers);

        VBox bubbleBox = new VBox(5, bubbleLabel, bubbleSortPane);
        VBox insertionBox = new VBox(5, insertionLabel, insertionSortPane);
        VBox quickBox = new VBox(5, quickLabel, quickSortPane);

        startButton = new Button("Запуск сортировки");
        startButton.setOnAction(e -> startSorting());

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
        
        ArrayList<Integer> bubbleNumbers = new ArrayList<>(originalNumbers);
        ArrayList<Integer> insertionNumbers = new ArrayList<>(originalNumbers);
        ArrayList<Integer> quickNumbers = new ArrayList<>(originalNumbers);

        updateBars(bubbleSortPane, bubbleNumbers);
        updateBars(insertionSortPane, insertionNumbers);
        updateBars(quickSortPane, quickNumbers);

        Thread b = new Thread(() -> {
            startBubbleSort(bubbleNumbers);
        });
        b.setDaemon(true);
        b.start();

        Thread i = new Thread(() -> {
        startInsertionSort(insertionNumbers);
        });
        i.setDaemon(true);
        i.start();

        Thread q = new Thread(() -> {
        startQuickSort(quickNumbers);
        });
        q.setDaemon(true);
        q.start();
    }

    private void updateBars(Pane pane, ArrayList<Integer> numbers) {
        Platform.runLater(() -> {
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
        });
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
                    updateBars(bubbleSortPane, numbers);
                    try
                    {
                        Thread.sleep(delay);
                    }
                    catch(InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void startInsertionSort(ArrayList<Integer> numbers) 
    {
        for (int i = numbers.size() - 2; i >= 0; i--)
        {
            int ind = i;
            for (int j = i + 1; j < numbers.size(); j++)
            {
                if (numbers.get(j) >= numbers.get(i))
                {
                    j = numbers.size();
                }
                else
                {
                    ind++;
                }
            }
            if (ind != i)
            {
                int el = numbers.get(i);
                for (int j = i; j < ind; j++)
                {
                    numbers.set(j, numbers.get(j + 1));
                }
                numbers.set(ind, el);
                updateBars(insertionSortPane, numbers);
                try
                {
                    Thread.sleep(delay);
                }
                catch(InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    private void startQuickSort(ArrayList<Integer> numbers) 
    {
        qs(numbers, 0, numbers.size() - 1);
    }

    private void qs(ArrayList<Integer> numbers, int low, int high)
    {
        if (low < high)
        {
            int pivot = partition(numbers, low, high);
            qs(numbers, low, pivot - 1);
            qs(numbers, pivot + 1, high);
        }
    }

    private int partition(ArrayList<Integer> numbers, int low, int high) 
    {
        int x = numbers.get(low);
        int pivot = low;
        for (int i = pivot + 1; i <= high; i++)
        {
            if (numbers.get(i) < x)
            {
                pivot++;
                int swap = numbers.get(pivot);
                numbers.set(pivot, numbers.get(i));
                numbers.set(i, swap);
                updateBars(quickSortPane, numbers);
                try
                {
                    Thread.sleep(delay);
                }
                catch(InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
        int swap = numbers.get(low);
        numbers.set(low, numbers.get(pivot));
        numbers.set(pivot, swap);
        updateBars(quickSortPane, numbers);
        try
        {
            Thread.sleep(delay);
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
        return pivot;
    }

    public static void main(String[] args) {
        launch(args);
    }
}