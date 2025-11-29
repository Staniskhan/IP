package com.staniskhan;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Comparator;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class studentTable extends Application {

    private TableView<Student> table = new TableView<>();
    private ObservableList<Student> studentList = FXCollections.observableArrayList();
    private boolean ascendingSort = true;

    public static void main(String[] args) {
        fileGeneratorForTable.generateFile("input_files/input.txt", 10, 7, 14);
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Student Table Application");

        readDataFromFile("input_files/input.txt");

        TableColumn<Student, String> nameColumn = new TableColumn<>("ФИО");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        nameColumn.setPrefWidth(200);

        TableColumn<Student, Integer> courseColumn = new TableColumn<>("Курс");
        courseColumn.setCellValueFactory(new PropertyValueFactory<>("course"));
        courseColumn.setPrefWidth(80);

        TableColumn<Student, Integer> semesterColumn = new TableColumn<>("Семестр");
        semesterColumn.setCellValueFactory(new PropertyValueFactory<>("semester"));
        semesterColumn.setPrefWidth(80);

        TableColumn<Student, String> groupColumn = new TableColumn<>("Группа");
        groupColumn.setCellValueFactory(new PropertyValueFactory<>("group"));
        groupColumn.setPrefWidth(100);

        TableColumn<Student, Double> gradeColumn = new TableColumn<>("Средний балл");
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("averageGrade"));
        gradeColumn.setPrefWidth(120);

        table.getColumns().addAll(nameColumn, courseColumn, semesterColumn, groupColumn, gradeColumn);
        table.setItems(studentList);

        setupSorting(nameColumn, Comparator.comparing(Student::getFullName));
        setupSorting(courseColumn, Comparator.comparing(Student::getCourse));
        setupSorting(semesterColumn, Comparator.comparing(Student::getSemester));
        setupSorting(groupColumn, Comparator.comparing(Student::getGroup));
        setupSorting(gradeColumn, Comparator.comparing(Student::getAverageGrade));

        Label fileLabel = new Label("Имя файла:");
        TextField fileTextField = new TextField();
        fileTextField.setPromptText("output.txt");
        Button saveButton = new Button("Сохранить в файл");
        
        saveButton.setOnAction(e -> {
            String filename = fileTextField.getText().trim();
            if (filename.isEmpty()) {
                filename = "output_files/output.txt";
            }
            saveDataToFile(filename);
        });

        HBox fileBox = new HBox(10, fileLabel, fileTextField, saveButton);
        fileBox.setPadding(new Insets(10));

        VBox root = new VBox(10, table, fileBox);
        root.setPadding(new Insets(10));

        primaryStage.setScene(new Scene(root, 600, 500));
        primaryStage.show();
    }

    private void setupSorting(TableColumn<Student, ?> column, Comparator<Student> comparator) {
        column.setSortable(true);
        column.setOnEditStart(event -> {
            if (ascendingSort) {
                studentList.sort(comparator);
            } else {
                studentList.sort(comparator.reversed());
            }
            ascendingSort = !ascendingSort;
            table.refresh();
        });
    }

    private void readDataFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            int studentCount = Integer.parseInt(reader.readLine().trim());
            
            for (int i = 0; i < studentCount; i++) {
                String fullName = reader.readLine();
                int course = Integer.parseInt(reader.readLine());
                int semester = Integer.parseInt(reader.readLine());
                int group = Integer.parseInt(reader.readLine());
                double AVG = Double.parseDouble(reader.readLine());
                
                studentList.add(new Student(fullName, course, semester, group, AVG));
            }
        } catch (IOException e) {
            showAlert("Ошибка чтения файла", "Не удалось прочитать файл: " + filename);
        } catch (NumberFormatException e) {
            showAlert("Ошибка формата", "Некорректный формат данных в файле");
        } catch (Exception e) {
            showAlert("Ошибка", "Произошла ошибка при чтении файла");
        }
    }

    private void saveDataToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println(studentList.size());
            
            for (Student student : studentList) {
                writer.println(student.getFullName());
                writer.println(student.getCourse());
                writer.println(student.getSemester());
                writer.println(student.getGroup());
                writer.println(student.getAverageGrade());
            }
            
        } catch (IOException e) {
            showAlert("Ошибка записи", "Не удалось записать данные в файл: " + filename);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class Student {
        private String name;
        private int course;
        private int semester;
        private int group;
        private double AVG;

        public Student(String name, int course, int semester, int group, double AVG) 
        {
            this.name = name;
            this.course = course;
            this.semester = semester;
            this.group = group;
            this.AVG = AVG;
        }

        public String getFullName() { return name; }
        public int getCourse() { return course; }
        public int getSemester() { return semester; }
        public int getGroup() { return group; }
        public double getAverageGrade() { return AVG; }
    }


   
}