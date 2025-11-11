package com.staniskhan;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

    private Map<String, List<Gift>> congratulatorGifts = new HashMap<>();
    private List<String> congratulators = new ArrayList<>();
    private List<Gift> allGifts = new ArrayList<>();
    private List<CartItem> cartItems = new ArrayList<>();
    
    private CheckBox[] congratulatorCheckboxes;
    private ComboBox<Gift> giftComboBox;
    private RadioButton concertYesRadio;
    private RadioButton concertNoRadio;
    private CheckBox regularCustomerCheckbox;
    private Label totalCostLabel;
    private TextArea cartTextArea;
    private ScrollPane cartScrollPane;
    
    private final double CONCERT_COST = 5000.0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        loadDataFromFile();
        
        primaryStage.setTitle("Вычисление затрат на награждение победителей олимпиады");
        
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(15);
        grid.setHgap(15);
        
        Label congratulatorLabel = new Label("a. Выберите Поздравителя (можно несколько):");
        VBox congratulatorBox = createCongratulatorSelection();
        grid.add(congratulatorLabel, 0, 0);
        grid.add(congratulatorBox, 1, 0);
        
        Label giftLabel = new Label("b. Выберите Подарок:");
        giftComboBox = new ComboBox<>();
        giftComboBox.setPrefWidth(200);
        grid.add(giftLabel, 0, 1);
        grid.add(giftComboBox, 1, 1);
        
        Label concertLabel = new Label("c. Нужен ли Концерт?");
        ToggleGroup concertGroup = new ToggleGroup();
        concertYesRadio = new RadioButton("Да");
        concertNoRadio = new RadioButton("Нет");
        concertYesRadio.setToggleGroup(concertGroup);
        concertNoRadio.setToggleGroup(concertGroup);
        concertNoRadio.setSelected(true);
        
        VBox concertBox = new VBox(5);
        concertBox.getChildren().addAll(concertYesRadio, concertNoRadio);
        grid.add(concertLabel, 0, 2);
        grid.add(concertBox, 1, 2);
        
        Label customerLabel = new Label("d. Постоянный клиент?");
        regularCustomerCheckbox = new CheckBox("Да");
        grid.add(customerLabel, 0, 3);
        grid.add(regularCustomerCheckbox, 1, 3);
        
        Button addButton = new Button("Добавить в корзину");
        Button clearButton = new Button("Очистить корзину");
        
        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(addButton, clearButton);
        grid.add(buttonBox, 1, 4);
        
        Label cartLabel = new Label("Корзина:");
        
        cartTextArea = new TextArea();
        cartTextArea.setEditable(false);
        cartTextArea.setWrapText(true);
        cartTextArea.setPrefRowCount(8);
        cartTextArea.setText("Корзина пуста");
        
        cartScrollPane = new ScrollPane(cartTextArea);
        cartScrollPane.setFitToWidth(true);
        cartScrollPane.setPrefHeight(200);
        
        Label totalLabel = new Label("Общая стоимость:");
        totalCostLabel = new Label("0.0 руб.");
        
        VBox cartBox = new VBox(10);
        cartBox.getChildren().addAll(cartLabel, cartScrollPane, totalLabel, totalCostLabel);
        grid.add(cartBox, 1, 5);
        
        addButton.setOnAction(e -> addToCart());
        clearButton.setOnAction(e -> clearCart());
        
        addCongratulatorListeners();
        
        updateGiftComboBox();
        
        Scene scene = new Scene(grid, 700, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private VBox createCongratulatorSelection() {
        VBox box = new VBox(5);
        congratulatorCheckboxes = new CheckBox[congratulators.size()];
        
        for (int i = 0; i < congratulators.size(); i++) {
            CheckBox checkBox = new CheckBox(congratulators.get(i));
            congratulatorCheckboxes[i] = checkBox;
            box.getChildren().add(checkBox);
        }
        
        return box;
    }
    
    private void addCongratulatorListeners() {
        for (CheckBox checkBox : congratulatorCheckboxes) {
            checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                updateGiftComboBox();
            });
        }
    }
    
    private void updateGiftComboBox() {
        List<Gift> availableGifts = new ArrayList<>();
        
        for (CheckBox checkBox : congratulatorCheckboxes) {
            if (checkBox.isSelected()) {
                List<Gift> gifts = congratulatorGifts.get(checkBox.getText());
                if (gifts != null) {
                    availableGifts.addAll(gifts);
                }
            }
        }
        
        if (availableGifts.isEmpty()) {
            availableGifts.addAll(allGifts);
        }
        
        ObservableList<Gift> observableGifts = FXCollections.observableArrayList(availableGifts);
        giftComboBox.setItems(observableGifts);
        if (!availableGifts.isEmpty()) {
            giftComboBox.setValue(availableGifts.get(0));
        }
    }
    
    private void addToCart() {
        List<String> selectedCongratulators = new ArrayList<>();
        for (CheckBox checkBox : congratulatorCheckboxes) {
            if (checkBox.isSelected()) {
                selectedCongratulators.add(checkBox.getText());
            }
        }
        
        if (selectedCongratulators.isEmpty()) {
            showAlert("Ошибка", "Пожалуйста, выберите хотя бы одного поздравителя");
            return;
        }
        
        Gift selectedGift = giftComboBox.getValue();
        if (selectedGift == null) {
            showAlert("Ошибка", "Пожалуйста, выберите подарок");
            return;
        }
        
        CartItem item = new CartItem();
        item.setCongratulators(selectedCongratulators);
        item.setGift(selectedGift);
        item.setConcert(concertYesRadio.isSelected());
        item.setRegularCustomer(regularCustomerCheckbox.isSelected());
        
        cartItems.add(item);
        
        updateCartDisplay();
    }
    
    private void clearCart() {
        cartItems.clear();
        updateCartDisplay();
    }
    
    private void updateCartDisplay() {
        if (cartItems.isEmpty()) {
            cartTextArea.setText("Корзина пуста");
            totalCostLabel.setText("0.0 руб.");
            return;
        }
        
        StringBuilder cartText = new StringBuilder();
        double totalCost = 0.0;
        
        for (int i = 0; i < cartItems.size(); i++) {
            CartItem item = cartItems.get(i);
            double itemCost = calculateItemCost(item);
            totalCost += itemCost;
            
            cartText.append("Позиция ").append(i + 1).append(":\n");
            cartText.append("  Поздравители: ").append(String.join(", ", item.getCongratulators())).append("\n");
            cartText.append("  Подарок: ").append(item.getGift().getName())
                   .append(" (").append(item.getGift().getCost()).append(" руб.)\n");
            cartText.append("  Концерт: ").append(item.hasConcert() ? "Да" : "Нет");
            if (item.hasConcert()) {
                cartText.append(" (").append(CONCERT_COST).append(" руб.)");
            }
            cartText.append("\n");
            cartText.append("  Постоянный клиент: ").append(item.isRegularCustomer() ? "Да" : "Нет").append("\n");
            cartText.append("  Стоимость позиции: ").append(String.format("%.2f", itemCost)).append(" руб.\n");
            cartText.append("---\n");
        }
        
        cartText.append("\nИТОГО: ").append(String.format("%.2f", totalCost)).append(" руб.");
        
        cartTextArea.setText(cartText.toString());
        totalCostLabel.setText(String.format("%.2f руб.", totalCost));
        
        cartTextArea.positionCaret(0);
    }
    
    private double calculateItemCost(CartItem item) {
        double cost = item.getGift().getCost();
        
        if (item.hasConcert()) {
            cost += CONCERT_COST;
        }
        
        if (item.isRegularCustomer()) {
            cost *= 0.9; // 10% скидка
        }
        
        return cost;
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void loadDataFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            boolean readingGifts = false;
            String currentCongratulator = null;
            
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                
                if (line.isEmpty()) {
                    readingGifts = true;
                    continue;
                }
                
                if (!readingGifts) {
                    congratulators.add(line);
                    congratulatorGifts.put(line, new ArrayList<>());
                } else {
                    if (congratulators.contains(line)) {
                        currentCongratulator = line;
                    } else if (currentCongratulator != null) {
                        String[] parts = line.split(":");
                        if (parts.length == 2) {
                            String giftName = parts[0].trim();
                            try {
                                double cost = Double.parseDouble(parts[1].trim());
                                Gift gift = new Gift(giftName, cost);
                                congratulatorGifts.get(currentCongratulator).add(gift);
                                allGifts.add(gift);
                            } catch (NumberFormatException e) {
                                System.err.println("Ошибка парсинга стоимости: " + line);
                            }
                        }
                    }
                }
            }
            
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
            fillWithTestData();
        }
        
        if (congratulators.isEmpty()) {
            fillWithTestData();
        }
    }
    
    private void fillWithTestData() {
        congratulators.add("Дед Мороз");
        congratulators.add("Снегурочка");
        congratulators.add("Клоун");
        
        List<Gift> dedMorozGifts = new ArrayList<>();
        dedMorozGifts.add(new Gift("Шоколадка", 100.0));
        dedMorozGifts.add(new Gift("Кукла", 500.0));
        dedMorozGifts.add(new Gift("Конструктор", 800.0));
        
        List<Gift> snegurochkaGifts = new ArrayList<>();
        snegurochkaGifts.add(new Gift("Книга", 150.0));
        snegurochkaGifts.add(new Gift("Мягкая игрушка", 400.0));
        snegurochkaGifts.add(new Gift("Набор красок", 600.0));
        
        List<Gift> clounGifts = new ArrayList<>();
        clounGifts.add(new Gift("Воздушный шар", 50.0));
        clounGifts.add(new Gift("Мыльные пузыри", 200.0));
        clounGifts.add(new Gift("Хлопушка", 300.0));
        
        congratulatorGifts.put("Дед Мороз", dedMorozGifts);
        congratulatorGifts.put("Снегурочка", snegurochkaGifts);
        congratulatorGifts.put("Клоун", clounGifts);
        
        allGifts.addAll(dedMorozGifts);
        allGifts.addAll(snegurochkaGifts);
        allGifts.addAll(clounGifts);
    }
    
    private static class Gift {
        private String name;
        private double cost;
        
        public Gift(String name, double cost) {
            this.name = name;
            this.cost = cost;
        }
        
        public String getName() { return name; }
        public double getCost() { return cost; }
        
        @Override
        public String toString() {
            return name + " - " + cost + " руб.";
        }
    }
    
    private static class CartItem {
        private List<String> congratulators;
        private Gift gift;
        private boolean hasConcert;
        private boolean isRegularCustomer;
        
        public List<String> getCongratulators() { return congratulators; }
        public void setCongratulators(List<String> congratulators) { this.congratulators = congratulators; }
        
        public Gift getGift() { return gift; }
        public void setGift(Gift gift) { this.gift = gift; }
        
        public boolean hasConcert() { return hasConcert; }
        public void setConcert(boolean hasConcert) { this.hasConcert = hasConcert; }
        
        public boolean isRegularCustomer() { return isRegularCustomer; }
        public void setRegularCustomer(boolean regularCustomer) { isRegularCustomer = regularCustomer; }
    }
}