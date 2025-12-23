package com.staniskhan;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.StringTokenizer;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RPNCalculator extends Application {

    private TextField inputField;
    private TextField varXField;
    private TextArea outputArea;
    private TextArea rpnArea;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("RPN Calculator with Variables");

        inputField = new TextField();
        inputField.setPromptText("Введите выражение (напр. x + 5)...");
        inputField.setStyle("-fx-font-size: 16;");

        Label varLabel = new Label("Значение x:");
        varXField = new TextField("0");
        varXField.setPrefWidth(100);
        HBox varBox = new HBox(10, varLabel, varXField);
        varBox.setPadding(new Insets(5, 0, 5, 0));

        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setStyle("-fx-font-size: 14; -fx-text-fill: blue;");
        outputArea.setPrefHeight(60);

        rpnArea = new TextArea();
        rpnArea.setEditable(false);
        rpnArea.setStyle("-fx-font-size: 14; -fx-text-fill: green;");
        rpnArea.setPrefHeight(60);

        GridPane numberPad = createNumberPad();
        
        Button xBtn = new Button("x");
        xBtn.setPrefSize(50, 50);
        xBtn.setStyle("-fx-font-size: 16; -fx-background-color: #add8e6;");
        xBtn.setOnAction(e -> inputField.appendText("x"));

        VBox operatorPanel = createOperatorPanel();
        operatorPanel.getChildren().add(0, xBtn); 

        VBox ioPanel = new VBox(10);
        ioPanel.setPadding(new Insets(10));
        ioPanel.getChildren().addAll(inputField, varBox, outputArea, rpnArea);

        HBox mainPanel = new HBox(10);
        mainPanel.setPadding(new Insets(10));
        mainPanel.getChildren().addAll(numberPad, operatorPanel);

        BorderPane root = new BorderPane();
        root.setTop(ioPanel);
        root.setCenter(mainPanel);

        inputField.setOnAction(e -> calculate());

        Scene scene = new Scene(root, 500, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void calculate() {
        String expression = inputField.getText().trim();
        if (expression.isEmpty()) return;

        try {
            double xValue = Double.parseDouble(varXField.getText().replace(",", "."));
            Map<String, Double> variables = new HashMap<>();
            variables.put("x", xValue);

            String rpn = convertToRPN(expression);
            rpnArea.setText("RPN: " + rpn);
            
            double result = evaluateRPN(rpn, variables);
            outputArea.setText("Результат: " + result);
            outputArea.setStyle("-fx-text-fill: blue;");
            
        } catch (NumberFormatException e) {
            outputArea.setText("ОШИБКА: Неверное значение x");
            outputArea.setStyle("-fx-text-fill: red;");
        } catch (Exception e) {
            outputArea.setText("ОШИБКА: " + e.getMessage());
            outputArea.setStyle("-fx-text-fill: red;");
        }
    }

    private String convertToRPN(String expression) throws Exception {
        StringBuilder output = new StringBuilder();
        Stack<Character> stack = new Stack<>();
        boolean lastWasOperator = true;
        
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (Character.isWhitespace(c)) continue;
            
            if (Character.isDigit(c) || c == '.' || Character.isLetter(c)) {
                if (Character.isLetter(c)) {
                    output.append(c).append(' ');
                    lastWasOperator = false;
                } else {
                    while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                        output.append(expression.charAt(i));
                        i++;
                    }
                    i--; 
                    output.append(' ');
                    lastWasOperator = false;
                }
            } else if (c == '(') {
                stack.push(c);
                lastWasOperator = true;
            } else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    output.append(stack.pop()).append(' ');
                }
                if (stack.isEmpty()) throw new Exception("Пропущена скобка");
                stack.pop();
                lastWasOperator = false;
            } else if (isOperator(c)) {
                if (lastWasOperator && (c == '+' || c == '-')) output.append("0 ");
                while (!stack.isEmpty() && getPrecedence(stack.peek()) >= getPrecedence(c)) {
                    output.append(stack.pop()).append(' ');
                }
                stack.push(c);
                lastWasOperator = true;
            }
        }
        while (!stack.isEmpty()) output.append(stack.pop()).append(' ');
        return output.toString().trim();
    }
    
    private double evaluateRPN(String rpn, Map<String, Double> vars) throws Exception {
        Stack<Double> stack = new Stack<>();
        StringTokenizer tokenizer = new StringTokenizer(rpn);
        
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            
            if (isNumber(token)) {
                stack.push(Double.parseDouble(token));
            } else if (vars.containsKey(token)) {
                stack.push(vars.get(token));
            } else if (isOperator(token.charAt(0))) {
                if (stack.size() < 2) throw new Exception("Недостаточно операндов");
                double b = stack.pop();
                double a = stack.pop();
                stack.push(applyOp(token.charAt(0), a, b));
            }
        }
        return stack.pop();
    }

    private GridPane createNumberPad() {
        GridPane grid = new GridPane();
        grid.setHgap(5); grid.setVgap(5); grid.setPadding(new Insets(10));
        
        for (int i = 1; i <= 9; i++) {
            Button btn = new Button(String.valueOf(i));
            btn.setPrefSize(50, 50);
            int finalI = i;
            btn.setOnAction(e -> inputField.appendText(String.valueOf(finalI)));
            grid.add(btn, (i - 1) % 3, (i - 1) / 3);
        }
        Button zero = new Button("0"); zero.setPrefSize(50, 50);
        zero.setOnAction(e -> inputField.appendText("0"));
        
        Button dot = new Button("."); dot.setPrefSize(50, 50);
        dot.setOnAction(e -> inputField.appendText("."));

        Button eq = new Button("="); eq.setPrefSize(50, 50);
        eq.setStyle("-fx-background-color: #66ff66;");
        eq.setOnAction(e -> calculate());

        grid.add(zero, 0, 3); grid.add(dot, 1, 3); grid.add(eq, 2, 3);
        return grid;
    }

    private VBox createOperatorPanel() {
        VBox vbox = new VBox(5);
        String[] ops = {"+", "-", "*", "/", "(", ")"};
        for (String op : ops) {
            Button b = new Button(op);
            b.setPrefSize(50, 50);
            b.setOnAction(e -> inputField.appendText(op));
            vbox.getChildren().add(b);
        }
        Button clear = new Button("C");
        clear.setStyle("-fx-background-color: #ff6666;");
        clear.setPrefSize(50, 50);
        clear.setOnAction(e -> { inputField.clear(); outputArea.clear(); rpnArea.clear(); });
        vbox.getChildren().add(clear);
        return vbox;
    }

    private double applyOp(char op, double a, double b) throws Exception {
        switch (op) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/': if (b == 0) throw new Exception("Деление на ноль"); return a / b;
        }
        return 0;
    }

    private boolean isOperator(char c) { return "+-*/".indexOf(c) != -1; }
    
    private boolean isNumber(String str) {
        try { Double.parseDouble(str); return true; } catch (Exception e) { return false; }
    }
    
    private int getPrecedence(char op) {
        if (op == '+' || op == '-') return 1;
        if (op == '*' || op == '/') return 2;
        return 0;
    }

    public static void main(String[] args) { launch(args); }
}