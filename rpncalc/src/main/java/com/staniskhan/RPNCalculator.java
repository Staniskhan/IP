package com.staniskhan;

import java.util.Stack;
import java.util.StringTokenizer;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RPNCalculator extends Application {

    private TextField inputField;
    private TextArea outputArea;
    private TextArea rpnArea;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("RPN Calculator");

        // Create UI components
        inputField = new TextField();
        inputField.setPromptText("Enter expression here...");
        inputField.setStyle("-fx-font-size: 16;");

        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setStyle("-fx-font-size: 14; -fx-text-fill: blue;");
        outputArea.setPrefHeight(60);
        outputArea.setPromptText("Result will be shown here...");

        rpnArea = new TextArea();
        rpnArea.setEditable(false);
        rpnArea.setStyle("-fx-font-size: 14; -fx-text-fill: green;");
        rpnArea.setPrefHeight(60);
        rpnArea.setPromptText("RPN notation will be shown here...");

        // Create number buttons
        Button[] numberButtons = new Button[10];
        for (int i = 0; i < 10; i++) {
            numberButtons[i] = new Button(String.valueOf(i));
            final int num = i;
            numberButtons[i].setOnAction(e -> inputField.appendText(String.valueOf(num)));
            numberButtons[i].setPrefSize(50, 50);
            numberButtons[i].setStyle("-fx-font-size: 16;");
        }

        // Create operator buttons
        Button plusBtn = createOperatorButton("+");
        Button minusBtn = createOperatorButton("-");
        Button multiplyBtn = createOperatorButton("*");
        Button divideBtn = createOperatorButton("/");
        Button leftParenBtn = createOperatorButton("(");
        Button rightParenBtn = createOperatorButton(")");
        Button decimalBtn = createOperatorButton(".");
        
        Button clearBtn = new Button("C");
        clearBtn.setStyle("-fx-font-size: 16; -fx-background-color: #ff6666;");
        clearBtn.setPrefSize(50, 50);
        clearBtn.setOnAction(e -> {
            inputField.clear();
            outputArea.clear();
            rpnArea.clear();
        });

        Button backspaceBtn = new Button("⌫");
        backspaceBtn.setStyle("-fx-font-size: 16; -fx-background-color: #ffaa66;");
        backspaceBtn.setPrefSize(50, 50);
        backspaceBtn.setOnAction(e -> {
            String text = inputField.getText();
            if (!text.isEmpty()) {
                inputField.setText(text.substring(0, text.length() - 1));
            }
        });

        Button equalsBtn = new Button("=");
        equalsBtn.setStyle("-fx-font-size: 16; -fx-background-color: #66ff66;");
        equalsBtn.setPrefSize(50, 50);
        equalsBtn.setOnAction(e -> calculate());

        // Create number pad
        GridPane numberPad = new GridPane();
        numberPad.setHgap(5);
        numberPad.setVgap(5);
        numberPad.setPadding(new Insets(10));
        
        // Add numbers 1-9
        for (int i = 1; i <= 9; i++) {
            numberPad.add(numberButtons[i], (i - 1) % 3, (i - 1) / 3);
        }
        
        // Add 0 and other buttons in the last row
        numberPad.add(numberButtons[0], 0, 3);
        numberPad.add(decimalBtn, 1, 3);
        numberPad.add(equalsBtn, 2, 3);

        // Create operator panel
        VBox operatorPanel = new VBox(5);
        operatorPanel.setPadding(new Insets(10));
        operatorPanel.getChildren().addAll(plusBtn, minusBtn, multiplyBtn, divideBtn, 
                                         leftParenBtn, rightParenBtn, clearBtn, backspaceBtn);

        // Create input/output areas
        VBox ioPanel = new VBox(10);
        ioPanel.setPadding(new Insets(10));
        ioPanel.getChildren().addAll(inputField, outputArea, rpnArea);

        // Main layout
        HBox mainPanel = new HBox(10);
        mainPanel.setPadding(new Insets(10));
        mainPanel.getChildren().addAll(numberPad, operatorPanel);

        BorderPane root = new BorderPane();
        root.setTop(ioPanel);
        root.setCenter(mainPanel);

        // Handle Enter key in input field
        inputField.setOnAction(e -> calculate());

        Scene scene = new Scene(root, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Button createOperatorButton(String operator) {
        Button btn = new Button(operator);
        btn.setPrefSize(50, 50);
        btn.setStyle("-fx-font-size: 16;");
        btn.setOnAction(e -> inputField.appendText(operator));
        return btn;
    }

    private void calculate() {
        String expression = inputField.getText().trim();
        if (expression.isEmpty()) {
            outputArea.setText("ERROR: Empty expression");
            outputArea.setStyle("-fx-text-fill: red;");
            return;
        }

        try {
            String rpn = convertToRPN(expression);
            rpnArea.setText("RPN: " + rpn);
            
            double result = evaluateRPN(rpn);
            outputArea.setText("Result: " + result);
            outputArea.setStyle("-fx-text-fill: blue;");
            
        } catch (Exception e) {
            outputArea.setText("ERROR: " + e.getMessage());
            outputArea.setStyle("-fx-text-fill: red;");
            rpnArea.clear();
        }
    }

    private String convertToRPN(String expression) throws Exception {
        StringBuilder output = new StringBuilder();
        Stack<Character> stack = new Stack<>();
        boolean lastWasOperator = true; // For unary operators
        
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            
            if (Character.isWhitespace(c)) {
                continue;
            }
            
            if (Character.isDigit(c) || c == '.') {
                // Read the entire number
                while (i < expression.length() && 
                       (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    output.append(expression.charAt(i));
                    i++;
                }
                i--; // Adjust position
                output.append(' ');
                lastWasOperator = false;
                
            } else if (c == '(') {
                if (!lastWasOperator && !stack.isEmpty() && 
                    stack.peek() != '(' && stack.peek() != '[' && stack.peek() != '{') {
                    throw new Exception("Operator absence before '(' at position " + (i + 1));
                }
                stack.push(c);
                lastWasOperator = true;
                
            } else if (c == ')') {
                if (lastWasOperator && !stack.isEmpty() && stack.peek() == '(') {
                    throw new Exception("Empty brackets at position " + (i + 1));
                }
                
                while (!stack.isEmpty() && stack.peek() != '(') {
                    if (stack.peek() == '[' || stack.peek() == '{') {
                        throw new Exception("Mismatched brackets at position " + (i + 1));
                    }
                    output.append(stack.pop()).append(' ');
                }
                
                if (stack.isEmpty()) {
                    throw new Exception("Missing opening bracket for ')' at position " + (i + 1));
                }
                stack.pop(); // Remove '('
                lastWasOperator = false;
                
            } else if (isOperator(c)) {
                // Handle unary operators
                if (lastWasOperator && (c == '+' || c == '-')) {
                    output.append("0 ");
                } else if (lastWasOperator) {
                    throw new Exception("Missing left operand for '" + c + "' at position " + (i + 1));
                }
                
                while (!stack.isEmpty() && getPrecedence(stack.peek()) >= getPrecedence(c)) {
                    output.append(stack.pop()).append(' ');
                }
                stack.push(c);
                lastWasOperator = true;
            } else {
                throw new Exception("Invalid character '" + c + "' at position " + (i + 1));
            }
        }
        
        // Pop remaining operators from stack
        while (!stack.isEmpty()) {
            char op = stack.pop();
            if (op == '(') {
                throw new Exception("Unclosed bracket");
            }
            output.append(op).append(' ');
        }
        
        return output.toString().trim();
    }
    
    private double evaluateRPN(String rpn) throws Exception {
        Stack<Double> stack = new Stack<>();
        StringTokenizer tokenizer = new StringTokenizer(rpn);
        
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            
            if (isNumber(token)) {
                stack.push(Double.parseDouble(token));
            } else if (isOperator(token.charAt(0))) {
                if (stack.size() < 2) {
                    throw new Exception("Insufficient operands for operator " + token);
                }
                
                double b = stack.pop();
                double a = stack.pop();
                double result;
                
                switch (token.charAt(0)) {
                    case '+': result = a + b; break;
                    case '-': result = a - b; break;
                    case '*': result = a * b; break;
                    case '/': 
                        if (b == 0) throw new Exception("Division by zero");
                        result = a / b; 
                        break;
                    default: throw new Exception("Unknown operator: " + token);
                }
                stack.push(result);
            }
        }
        
        if (stack.size() != 1) {
            throw new Exception("Invalid expression");
        }
        
        return stack.pop();
    }
    
    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }
    
    private boolean isNumber(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    private int getPrecedence(char op) {
        switch (op) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                return 0;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}