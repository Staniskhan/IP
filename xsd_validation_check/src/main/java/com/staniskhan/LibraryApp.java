package com.staniskhan;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LibraryApp extends Application {
    private ObservableList<Book> books = FXCollections.observableArrayList();
    private List<IssuedBook> issuedBooks = new ArrayList<>();
    private String xmlPath;
    private String xsdPath;
    private BorderPane root;
    private MenuBar menuBar;

    @Override
    public void start(Stage primaryStage) {
        try {
            loadResources();
            
            if (!validateXML()) {
                showErrorAndExit("XML файл не соответствует схеме!");
                return;
            }
            
            loadBooksFromXML();
            
            root = new BorderPane();
            createMenuBar();
            
            Scene scene = new Scene(root, 1000, 700);
            
            primaryStage.setTitle("Библиотека");
            primaryStage.setScene(scene);
            primaryStage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAndExit("Ошибка при запуске приложения: " + e.getMessage());
        }
    }

    private void loadResources() {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            StringBuilder strb = new StringBuilder(classLoader.getResource("library.xml").getPath());
            strb.delete(strb.length() - 26, strb.length());
            xmlPath = strb.toString();
            xmlPath += "src/main/resources/library.xml";
            
            StringBuilder strb1 = new StringBuilder(classLoader.getResource("library.xsd").getPath());
            strb1.delete(strb1.length() - 26, strb1.length());
            xsdPath = strb1.toString();
            xsdPath += "src/main/resources/library.xsd";
        } catch (Exception e) {
            throw new RuntimeException("Не удалось загрузить ресурсы", e);
        }
    }

    private boolean validateXML() {
        try {
            SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
            Schema schema = factory.newSchema(new File(xsdPath));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(xmlPath)));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void showErrorAndExit(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Проблема с файлом данных");
        alert.setContentText(message);
        alert.showAndWait();
        System.exit(1);
    }

private void loadBooksFromXML() throws Exception {
    books.clear();
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    Document doc = dBuilder.parse(new File(xmlPath));
    doc.getDocumentElement().normalize();
    
    NodeList nodeList = doc.getElementsByTagName("book");
    
    for (int i = 0; i < nodeList.getLength(); i++) {
        Element element = (Element) nodeList.item(i);
        
        int id = Integer.parseInt(element.getAttribute("id"));
        String title = element.getElementsByTagName("title").item(0).getTextContent();
        String author = element.getElementsByTagName("author").item(0).getTextContent();
        int year = Integer.parseInt(element.getElementsByTagName("year").item(0).getTextContent());
        double price = Double.parseDouble(element.getElementsByTagName("price").item(0).getTextContent());
        String category = element.getElementsByTagName("category").item(0).getTextContent();
        int amount = Integer.parseInt(element.getAttribute("amount"));
        int realAmount = Integer.parseInt(element.getAttribute("realAmount"));
        
        books.add(new Book(id, title, author, year, price, category, amount, realAmount));
    }
}

private void saveBooksToXML() throws Exception {
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    Document doc = dBuilder.newDocument();
    
    Element rootElement = doc.createElement("library");
    doc.appendChild(rootElement);
    
    java.text.DecimalFormatSymbols dfs = new java.text.DecimalFormatSymbols(java.util.Locale.US);
    java.text.DecimalFormat df = new java.text.DecimalFormat("0.00", dfs);
    
    for (Book book : books) {
        Element bookElement = doc.createElement("book");
        bookElement.setAttribute("id", String.valueOf(book.getId()));
        bookElement.setAttribute("amount", String.valueOf(book.getAmount()));
        bookElement.setAttribute("realAmount", String.valueOf(book.getRealAmount()));
        
        addElement(doc, bookElement, "title", book.getTitle());
        addElement(doc, bookElement, "author", book.getAuthor());
        addElement(doc, bookElement, "year", String.valueOf(book.getYear()));
        addElement(doc, bookElement, "price", df.format(book.getPrice()));
        addElement(doc, bookElement, "category", book.getCategory());
        
        rootElement.appendChild(bookElement);
    }
    
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    DOMSource source = new DOMSource(doc);
    StreamResult result = new StreamResult(new File(xmlPath));
    transformer.transform(source, result);
}
    
    private void addElement(Document doc, Element parent, String name, String value) {
        Element element = doc.createElement(name);
        element.appendChild(doc.createTextNode(value));
        parent.appendChild(element);
    }

    private void createMenuBar() {
        menuBar = new MenuBar();
        
        Menu menu = new Menu("Меню");
        
        MenuItem showAll = new MenuItem("Вывод всех сведений о книгах в виде таблицы");
        showAll.setOnAction(e -> showAllBooks());
        
        MenuItem addNew = new MenuItem("Добавление новой книги");
        addNew.setOnAction(e -> showAddBookForm());
        
        MenuItem search = new MenuItem("Поиск");
        search.setOnAction(e -> showSearch());
        
        MenuItem reprice = new MenuItem("Переоценка");
        reprice.setOnAction(e -> showReprice());
        
        MenuItem issue = new MenuItem("Выдача книг");
        issue.setOnAction(e -> showIssueBooks());
        
        MenuItem returnBook = new MenuItem("Вернуть книгу");
        returnBook.setOnAction(e -> showReturnBooks());
        
        MenuItem showIssued = new MenuItem("Выданные книги");
        showIssued.setOnAction(e -> showIssuedBooks());
        
        menu.getItems().addAll(showAll, addNew, search, reprice, issue, returnBook, showIssued);
        menuBar.getMenus().add(menu);
        
        root.setTop(menuBar);
    }

    private void showAllBooks() {
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        
        TableView<Book> table = createBookTable();
        table.setItems(books);
        
        ScrollPane scrollPane = new ScrollPane(table);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        
        content.getChildren().add(scrollPane);
        root.setCenter(content);
    }

    private TableView<Book> createBookTable() {
        TableView<Book> table = new TableView<>();
        
        TableColumn<Book, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        
        TableColumn<Book, String> titleCol = new TableColumn<>("Название");
        titleCol.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        
        TableColumn<Book, String> authorCol = new TableColumn<>("Автор");
        authorCol.setCellValueFactory(cellData -> cellData.getValue().authorProperty());
        
        TableColumn<Book, Integer> yearCol = new TableColumn<>("Год");
        yearCol.setCellValueFactory(cellData -> cellData.getValue().yearProperty().asObject());
        
        TableColumn<Book, Double> priceCol = new TableColumn<>("Цена");
        priceCol.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        
        TableColumn<Book, String> categoryCol = new TableColumn<>("Категория");
        categoryCol.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());
        
        TableColumn<Book, Integer> amountCol = new TableColumn<>("Количество");
        amountCol.setCellValueFactory(cellData -> cellData.getValue().amountProperty().asObject());
        
        TableColumn<Book, Integer> realAmountCol = new TableColumn<>("Доступно");
        realAmountCol.setCellValueFactory(cellData -> cellData.getValue().realAmountProperty().asObject());
        
        table.getColumns().addAll(idCol, titleCol, authorCol, yearCol, priceCol, categoryCol, amountCol, realAmountCol);
        
        return table;
    }

    private void showAddBookForm() {
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        
        TextField titleField = new TextField();
        TextField authorField = new TextField();
        TextField yearField = new TextField();
        TextField priceField = new TextField();
        ComboBox<String> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll("programming", "computer-science", "fiction", "non-fiction", "science");
        TextField amountField = new TextField();
        TextField realAmountField = new TextField();
        
        Button addButton = new Button("Добавить");
        addButton.setOnAction(e -> {
            boolean valid = true;
            
            if (titleField.getText().isEmpty()) {
                titleField.setPromptText("Это поле обязательно для заполнения");
                titleField.setStyle("-fx-prompt-text-fill: red;");
                valid = false;
            }
            if (authorField.getText().isEmpty()) {
                authorField.setPromptText("Это поле обязательно для заполнения");
                authorField.setStyle("-fx-prompt-text-fill: red;");
                valid = false;
            }
            if (yearField.getText().isEmpty()) {
                yearField.setPromptText("Это поле обязательно для заполнения");
                yearField.setStyle("-fx-prompt-text-fill: red;");
                valid = false;
            }
            if (priceField.getText().isEmpty()) {
                priceField.setPromptText("Это поле обязательно для заполнения");
                priceField.setStyle("-fx-prompt-text-fill: red;");
                valid = false;
            }
            if (categoryBox.getValue() == null) {
                categoryBox.setPromptText("Это поле обязательно для заполнения");
                valid = false;
            }
            if (amountField.getText().isEmpty()) {
                amountField.setPromptText("Это поле обязательно для заполнения");
                amountField.setStyle("-fx-prompt-text-fill: red;");
                valid = false;
            }
            if (realAmountField.getText().isEmpty()) {
                realAmountField.setPromptText("Это поле обязательно для заполнения");
                realAmountField.setStyle("-fx-prompt-text-fill: red;");
                valid = false;
            }
            
            if (valid) {
                try {
                    int newId = books.stream().mapToInt(Book::getId).max().orElse(0) + 1;
                    Book newBook = new Book(
                        newId,
                        titleField.getText(),
                        authorField.getText(),
                        Integer.parseInt(yearField.getText()),
                        Double.parseDouble(priceField.getText()),
                        categoryBox.getValue(),
                        Integer.parseInt(amountField.getText()),
                        Integer.parseInt(realAmountField.getText())
                    );
                    
                    if (newBook.getRealAmount() <= newBook.getAmount()) {
                        books.add(newBook);
                        saveBooksToXML();
                        
                        titleField.clear();
                        authorField.clear();
                        yearField.clear();
                        priceField.clear();
                        categoryBox.getSelectionModel().clearSelection();
                        amountField.clear();
                        realAmountField.clear();
                        
                        showAlert("Успех", "Книга добавлена успешно!");
                    } else {
                        showAlert("Ошибка", "realAmount не может быть больше amount!");
                    }
                } catch (Exception ex) {
                    showAlert("Ошибка", "Некорректные данные: " + ex.getMessage());
                }
            }
        });
        
        content.getChildren().addAll(
            new Label("Название:"), titleField,
            new Label("Автор:"), authorField,
            new Label("Год:"), yearField,
            new Label("Цена:"), priceField,
            new Label("Категория:"), categoryBox,
            new Label("Общее количество:"), amountField,
            new Label("Доступно:"), realAmountField,
            addButton
        );
        
        root.setCenter(content);
    }

    private void showSearch() {
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        
        HBox searchBox = new HBox(10);
        TextField titleSearch = new TextField();
        titleSearch.setPromptText("Название");
        TextField authorSearch = new TextField();
        authorSearch.setPromptText("Автор");
        TextField categorySearch = new TextField();
        categorySearch.setPromptText("Категория");
        Button searchButton = new Button("Найти");
        
        TableView<Book> table = createBookTable();
        
        searchButton.setOnAction(e -> {
            String title = titleSearch.getText().toLowerCase();
            String author = authorSearch.getText().toLowerCase();
            String category = categorySearch.getText().toLowerCase();
            
            ObservableList<Book> filtered = FXCollections.observableArrayList();
            for (Book book : books) {
                boolean match = true;
                if (!title.isEmpty() && !book.getTitle().toLowerCase().contains(title)) match = false;
                if (!author.isEmpty() && !book.getAuthor().toLowerCase().contains(author)) match = false;
                if (!category.isEmpty() && !book.getCategory().toLowerCase().contains(category)) match = false;
                if (match) filtered.add(book);
            }
            table.setItems(filtered);
        });
        
        searchBox.getChildren().addAll(titleSearch, authorSearch, categorySearch, searchButton);
        content.getChildren().addAll(searchBox, table);
        root.setCenter(content);
    }

    private void showReprice() {
        SplitPane splitPane = new SplitPane();
        
        VBox leftPanel = new VBox(10);
        leftPanel.setPadding(new Insets(10));
        
        ListView<Book> bookList = new ListView<>();
        bookList.setItems(books);
        bookList.setCellFactory(param -> new ListCell<Book>() {
            @Override
            protected void updateItem(Book item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getTitle() + " - " + item.getAuthor());
                }
            }
        });
        
        TextField priceField = new TextField();
        priceField.setPromptText("Новая цена");
        priceField.setDisable(true);
        
        Button repriceButton = new Button("Переоценить");
        repriceButton.setDisable(true);
        
        VBox rightPanel = new VBox(10);
        rightPanel.setPadding(new Insets(10));
        Label infoLabel = new Label("Выберите книгу");
        
        bookList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                priceField.setDisable(false);
                repriceButton.setDisable(false);
                infoLabel.setText(
                    "ID: " + newVal.getId() + "\n" +
                    "Название: " + newVal.getTitle() + "\n" +
                    "Автор: " + newVal.getAuthor() + "\n" +
                    "Год: " + newVal.getYear() + "\n" +
                    "Цена: " + newVal.getPrice() + "\n" +
                    "Категория: " + newVal.getCategory() + "\n" +
                    "Количество: " + newVal.getAmount() + "\n" +
                    "Доступно: " + newVal.getRealAmount()
                );
                priceField.setText(String.valueOf(newVal.getPrice()));
            } else {
                priceField.setDisable(true);
                repriceButton.setDisable(true);
                infoLabel.setText("Выберите книгу");
            }
        });
        
        repriceButton.setOnAction(e -> {
            Book selected = bookList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                try {
                    double newPrice = Double.parseDouble(priceField.getText());
                    if (newPrice > 0) {
                        selected.setPrice(newPrice);
                        try
                        {
                            saveBooksToXML();
                        }
                        catch (Exception e1)
                        {
                            e1.printStackTrace();
                        }
                        infoLabel.setText(
                            "ID: " + selected.getId() + "\n" +
                            "Название: " + selected.getTitle() + "\n" +
                            "Автор: " + selected.getAuthor() + "\n" +
                            "Год: " + selected.getYear() + "\n" +
                            "Цена: " + selected.getPrice() + "\n" +
                            "Категория: " + selected.getCategory() + "\n" +
                            "Количество: " + selected.getAmount() + "\n" +
                            "Доступно: " + selected.getRealAmount()
                        );
                        showAlert("Успех", "Цена изменена успешно!");
                    } else {
                        showAlert("Ошибка", "Цена должна быть больше 0!");
                    }
                } catch (NumberFormatException ex) {
                    showAlert("Ошибка", "Некорректная цена!");
                }
            }
        });
        
        leftPanel.getChildren().addAll(new Label("Список книг:"), bookList, priceField, repriceButton);
        rightPanel.getChildren().add(infoLabel);
        
        splitPane.getItems().addAll(leftPanel, rightPanel);
        splitPane.setDividerPositions(0.5);
        
        root.setCenter(splitPane);
    }

    private void showIssueBooks() {
        SplitPane splitPane = new SplitPane();
        
        VBox leftPanel = new VBox(10);
        leftPanel.setPadding(new Insets(10));
        
        ListView<Book> bookList = new ListView<>();
        ObservableList<Book> availableBooks = FXCollections.observableArrayList();
        for (Book book : books) {
            if (book.getRealAmount() > 0) {
                availableBooks.add(book);
            }
        }
        bookList.setItems(availableBooks);
        bookList.setCellFactory(param -> new ListCell<Book>() {
            @Override
            protected void updateItem(Book item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getTitle() + " - " + item.getAuthor());
                }
            }
        });
        
        Button issueButton = new Button("Выписать книгу");
        issueButton.setDisable(true);
        
        VBox rightPanel = new VBox(10);
        rightPanel.setPadding(new Insets(10));
        Label infoLabel = new Label("Выберите книгу");
        
        bookList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                issueButton.setDisable(newVal.getRealAmount() == 0);
                infoLabel.setText(
                    "ID: " + newVal.getId() + "\n" +
                    "Название: " + newVal.getTitle() + "\n" +
                    "Автор: " + newVal.getAuthor() + "\n" +
                    "Год: " + newVal.getYear() + "\n" +
                    "Цена: " + newVal.getPrice() + "\n" +
                    "Категория: " + newVal.getCategory() + "\n" +
                    "Количество: " + newVal.getAmount() + "\n" +
                    "Доступно: " + newVal.getRealAmount()
                );
            } else {
                issueButton.setDisable(true);
                infoLabel.setText("Выберите книгу");
            }
        });
        
        issueButton.setOnAction(e -> {
            Book selected = bookList.getSelectionModel().getSelectedItem();
            if (selected != null && selected.getRealAmount() > 0) {
                selected.setRealAmount(selected.getRealAmount() - 1);
                
                IssuedBook issued = findIssuedBook(selected.getId());
                if (issued == null) {
                    issuedBooks.add(new IssuedBook(selected.getId(), 1));
                } else {
                    issued.incrementIssuedAmount();
                }
                
                try
                {
                    saveBooksToXML();
                }
                catch (Exception e1)
                {
                    e1.printStackTrace();
                }
                
                infoLabel.setText(
                    "ID: " + selected.getId() + "\n" +
                    "Название: " + selected.getTitle() + "\n" +
                    "Автор: " + selected.getAuthor() + "\n" +
                    "Год: " + selected.getYear() + "\n" +
                    "Цена: " + selected.getPrice() + "\n" +
                    "Категория: " + selected.getCategory() + "\n" +
                    "Количество: " + selected.getAmount() + "\n" +
                    "Доступно: " + selected.getRealAmount()
                );
                
                availableBooks.clear();
                for (Book book : books) {
                    if (book.getRealAmount() > 0) {
                        availableBooks.add(book);
                    }
                }
                
                if (selected.getRealAmount() == 0) {
                    issueButton.setDisable(true);
                }
                
                showAlert("Успех", "Книга выдана успешно!");
            }
        });
        
        leftPanel.getChildren().addAll(new Label("Доступные книги:"), bookList, issueButton);
        rightPanel.getChildren().add(infoLabel);
        
        splitPane.getItems().addAll(leftPanel, rightPanel);
        splitPane.setDividerPositions(0.5);
        
        root.setCenter(splitPane);
    }

    private void showReturnBooks() {
        SplitPane splitPane = new SplitPane();
        
        VBox leftPanel = new VBox(10);
        leftPanel.setPadding(new Insets(10));
        
        ListView<Book> bookList = new ListView<>();
        ObservableList<Book> issuedBookList = FXCollections.observableArrayList();
        for (IssuedBook issued : issuedBooks) {
            if (issued.getIssuedAmount() > 0) {
                Book book = findBookById(issued.getBookId());
                if (book != null) {
                    issuedBookList.add(book);
                }
            }
        }
        bookList.setItems(issuedBookList);
        bookList.setCellFactory(param -> new ListCell<Book>() {
            @Override
            protected void updateItem(Book item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getTitle() + " - " + item.getAuthor());
                }
            }
        });
        
        Button returnButton = new Button("Вернуть книгу");
        returnButton.setDisable(true);
        
        VBox rightPanel = new VBox(10);
        rightPanel.setPadding(new Insets(10));
        Label infoLabel = new Label("Выберите книгу");
        
        bookList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                IssuedBook issued = findIssuedBook(newVal.getId());
                if (issued != null && issued.getIssuedAmount() > 0) {
                    returnButton.setDisable(false);
                    infoLabel.setText(
                        "ID: " + newVal.getId() + "\n" +
                        "Название: " + newVal.getTitle() + "\n" +
                        "Автор: " + newVal.getAuthor() + "\n" +
                        "Год: " + newVal.getYear() + "\n" +
                        "Цена: " + newVal.getPrice() + "\n" +
                        "Категория: " + newVal.getCategory() + "\n" +
                        "Количество у читателя: " + issued.getIssuedAmount()
                    );
                } else {
                    returnButton.setDisable(true);
                    infoLabel.setText("Выберите книгу");
                }
            } else {
                returnButton.setDisable(true);
                infoLabel.setText("Выберите книгу");
            }
        });
        
        returnButton.setOnAction(e -> {
            Book selected = bookList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                IssuedBook issued = findIssuedBook(selected.getId());
                if (issued != null && issued.getIssuedAmount() > 0) {
                    issued.decrementIssuedAmount();
                    selected.setRealAmount(selected.getRealAmount() + 1);
                    
                    try
                    {
                        saveBooksToXML();
                    }
                    catch (Exception e1)
                    {
                        e1.printStackTrace();
                    }
                    
                    infoLabel.setText(
                        "ID: " + selected.getId() + "\n" +
                        "Название: " + selected.getTitle() + "\n" +
                        "Автор: " + selected.getAuthor() + "\n" +
                        "Год: " + selected.getYear() + "\n" +
                        "Цена: " + selected.getPrice() + "\n" +
                        "Категория: " + selected.getCategory() + "\n" +
                        "Количество у читателя: " + issued.getIssuedAmount()
                    );
                    
                    if (issued.getIssuedAmount() == 0) {
                        issuedBookList.remove(selected);
                        returnButton.setDisable(true);
                    }
                    
                    showAlert("Успех", "Книга возвращена успешно!");
                }
            }
        });
        
        leftPanel.getChildren().addAll(new Label("Выданные книги:"), bookList, returnButton);
        rightPanel.getChildren().add(infoLabel);
        
        splitPane.getItems().addAll(leftPanel, rightPanel);
        splitPane.setDividerPositions(0.5);
        
        root.setCenter(splitPane);
    }

    private void showIssuedBooks() {
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        
        if (issuedBooks.isEmpty() || issuedBooks.stream().allMatch(ib -> ib.getIssuedAmount() == 0)) {
            Label noBooksLabel = new Label("У Вас нет выписанных книг");
            noBooksLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: gray;");
            content.getChildren().add(noBooksLabel);
        } else {
            TableView<Book> table = createIssuedBooksTable();
            content.getChildren().add(table);
        }
        
        root.setCenter(content);
    }

    private TableView<Book> createIssuedBooksTable() {
        TableView<Book> table = new TableView<>();
        ObservableList<Book> issuedBookList = FXCollections.observableArrayList();
        
        for (IssuedBook issued : issuedBooks) {
            if (issued.getIssuedAmount() > 0) {
                Book book = findBookById(issued.getBookId());
                if (book != null) {
                    issuedBookList.add(book);
                }
            }
        }
        
        TableColumn<Book, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        
        TableColumn<Book, String> titleCol = new TableColumn<>("Название");
        titleCol.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        
        TableColumn<Book, String> authorCol = new TableColumn<>("Автор");
        authorCol.setCellValueFactory(cellData -> cellData.getValue().authorProperty());
        
        TableColumn<Book, Integer> issuedAmountCol = new TableColumn<>("Количество у читателя");
        issuedAmountCol.setCellValueFactory(cellData -> {
            Book book = cellData.getValue();
            IssuedBook issued = findIssuedBook(book.getId());
            return new SimpleIntegerProperty(issued != null ? issued.getIssuedAmount() : 0).asObject();
        });
        
        table.getColumns().addAll(idCol, titleCol, authorCol, issuedAmountCol);
        table.setItems(issuedBookList);
        
        return table;
    }

    private Book findBookById(int id) {
        return books.stream().filter(b -> b.getId() == id).findFirst().orElse(null);
    }

    private IssuedBook findIssuedBook(int bookId) {
        return issuedBooks.stream().filter(ib -> ib.getBookId() == bookId).findFirst().orElse(null);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}