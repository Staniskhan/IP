package com.staniskhan.library.service;

import java.io.File;
import java.math.BigDecimal;
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

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.staniskhan.library.model.Book;
import com.staniskhan.library.model.User;

@Service
public class XmlService {
    private static String XML_FILE; // Для книг
    private static String XSD_FILE; // Для книг
    private static String USERS_XML_FILE; // Для пользователей

    // === БЛОК ИНИЦИАЛИЗАЦИИ ПУТЕЙ ===
    public XmlService() {
        // Вычисляем пути при создании бина
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            String basePath = classLoader.getResource("").getPath();
            // Откатываемся от target/classes к корню проекта (примерно)
            // Примечание: В реальном продакшене пути могут отличаться, но для локального запуска оставляем вашу логику
            if (basePath.contains("/target/")) {
                basePath = basePath.substring(0, basePath.indexOf("/target/"));
            }

            XML_FILE = basePath + "/src/main/resources/library.xml";
            XSD_FILE = basePath + "/src/main/resources/library.xsd";
            USERS_XML_FILE = basePath + "/src/main/resources/users.xml";

        } catch (Exception e) {
            System.err.println("Ошибка вычисления путей к файлам: " + e.getMessage());
        }
    }

    // === МЕТОДЫ ДЛЯ ПОЛЬЗОВАТЕЛЕЙ (НОВЫЕ) ===

    public List<User> loadUsersFromXml() {
        List<User> users = new ArrayList<>();
        try {
            File file = new File(USERS_XML_FILE);
            if (!file.exists()) {
                createEmptyUsersXml();
                return users;
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("user");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    User user = new User();
                    user.setUsername(getElementText(element, "username"));
                    user.setPassword(getElementText(element, "password"));
                    user.setRole(getElementText(element, "role"));

                    // Чтение взятых книг
                    List<String> books = new ArrayList<>();
                    NodeList bookNodes = element.getElementsByTagName("borrowedBook");
                    for (int j = 0; j < bookNodes.getLength(); j++) {
                        books.add(bookNodes.item(j).getTextContent());
                    }
                    user.setBorrowedBooks(books);

                    users.add(user);
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка при загрузке пользователей: " + e.getMessage());
        }
        return users;
    }

    public void addUserToXml(User user) {
        try {
            Document doc = loadXmlDocument(USERS_XML_FILE);
            Element root = doc.getDocumentElement();

            Element userElement = doc.createElement("user");
            addTextElement(doc, userElement, "username", user.getUsername());
            addTextElement(doc, userElement, "password", user.getPassword());
            addTextElement(doc, userElement, "role", user.getRole());

            // Контейнер для книг (сначала пустой)
            Element booksElement = doc.createElement("borrowedBooks");
            userElement.appendChild(booksElement);

            root.appendChild(userElement);
            saveXmlDocument(doc, USERS_XML_FILE);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось добавить пользователя в XML", e);
        }
    }

    public void updateUserBorrowedBooksInXml(String username, List<String> borrowedBooks) {
        try {
            Document doc = loadXmlDocument(USERS_XML_FILE);
            NodeList users = doc.getElementsByTagName("user");

            for (int i = 0; i < users.getLength(); i++) {
                Element userElement = (Element) users.item(i);
                String currentUsername = getElementText(userElement, "username");

                if (currentUsername.equals(username)) {
                    // Нашли пользователя, удаляем старый список книг
                    NodeList oldBooksNodeList = userElement.getElementsByTagName("borrowedBooks");
                    if (oldBooksNodeList.getLength() > 0) {
                        userElement.removeChild(oldBooksNodeList.item(0));
                    }

                    // Создаем новый список
                    Element booksContainer = doc.createElement("borrowedBooks");
                    for (String title : borrowedBooks) {
                        addTextElement(doc, booksContainer, "borrowedBook", title);
                    }
                    userElement.appendChild(booksContainer);

                    saveXmlDocument(doc, USERS_XML_FILE);
                    break;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Не удалось обновить книги пользователя в XML", e);
        }
    }

    private void createEmptyUsersXml() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        Element root = doc.createElement("users");
        doc.appendChild(root);
        saveXmlDocument(doc, USERS_XML_FILE);
        System.out.println("Создан файл пользователей: " + USERS_XML_FILE);
    }

    // === СУЩЕСТВУЮЩИЕ МЕТОДЫ ДЛЯ КНИГ (БЕЗ ИЗМЕНЕНИЙ ЛОГИКИ, ТОЛЬКО ПУТИ) ===

    public List<Book> loadBooksFromXml() {
        // ... (код загрузки книг остается прежним, используй XML_FILE и XSD_FILE из полей класса)
        // Для краткости я не дублирую весь код книг, он остается как был,
        // но убедись, что переменная XML_FILE инициализирована в конструкторе.

        List<Book> books = new ArrayList<>();
        try {
            // Валидация и чтение (как было в твоем коде)
            if (!validateXML()) {
                System.out.println("XML файл книг не валиден или отсутствует.");
            }
            File file = new File(XML_FILE);
            if (!file.exists()) {
                createEmptyBookXml(); // Выделил создание в метод для порядка
                return books;
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("book");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    Book book = new Book();
                    book.setId(Long.parseLong(element.getAttribute("id")));
                    book.setTitle(getElementText(element, "title"));
                    book.setAuthor(getElementText(element, "author"));
                    book.setPublicationYear(Integer.parseInt(getElementText(element, "year")));
                    book.setPrice(new BigDecimal(getElementText(element, "price")));
                    book.setCategory(getElementText(element, "category"));
                    book.setTotalCopies(Integer.parseInt(element.getAttribute("amount")));
                    book.setAvailableCopies(Integer.parseInt(element.getAttribute("realAmount")));
                    books.add(book);
                }
            }
            return books;
        } catch (Exception e) {
            System.err.println("Ошибка загрузки книг: " + e.getMessage());
            return books;
        }
    }

    // ... Остальные методы для книг (addBookToXml, updatePriceInXml, issueBookInXml) остаются без изменений ...
    // ВАЖНО: Везде где использовался saveXmlDocument(doc), теперь используй saveXmlDocument(doc, XML_FILE)

    public void addBookToXml(Book book) {
        try {
            Document doc = loadXmlDocument(XML_FILE);
            Element root = doc.getDocumentElement();
            NodeList books = root.getElementsByTagName("book");
            long maxId = 0;
            for (int i = 0; i < books.getLength(); i++) {
                Element bookElement = (Element) books.item(i);
                long id = Long.parseLong(bookElement.getAttribute("id"));
                if (id > maxId) maxId = id;
            }
            Element bookElement = doc.createElement("book");
            bookElement.setAttribute("id", String.valueOf(maxId + 1));
            bookElement.setAttribute("amount", String.valueOf(book.getTotalCopies()));
            bookElement.setAttribute("realAmount", String.valueOf(book.getAvailableCopies()));
            addTextElement(doc, bookElement, "title", book.getTitle());
            addTextElement(doc, bookElement, "author", book.getAuthor());
            addTextElement(doc, bookElement, "year", String.valueOf(book.getPublicationYear()));
            addTextElement(doc, bookElement, "price", book.getPrice().toString());
            addTextElement(doc, bookElement, "category", book.getCategory());
            root.appendChild(bookElement);
            saveXmlDocument(doc, XML_FILE); // <-- ПУТЬ
        } catch (Exception e) { throw new RuntimeException(e); }
    }

    public void updatePriceInXml(Long bookId, BigDecimal newPrice) {
        try {
            Document doc = loadXmlDocument(XML_FILE);
            NodeList books = doc.getElementsByTagName("book");
            for (int i = 0; i < books.getLength(); i++) {
                Element book = (Element) books.item(i);
                if (book.getAttribute("id").equals(bookId.toString())) {
                    NodeList priceNodes = book.getElementsByTagName("price");
                    if (priceNodes.getLength() > 0) {
                        priceNodes.item(0).setTextContent(newPrice.toString());
                        saveXmlDocument(doc, XML_FILE);
                    }
                    break;
                }
            }
        } catch (Exception e) { throw new RuntimeException(e); }
    }

    public void issueBookInXml(Long bookId) {
        try {
            Document doc = loadXmlDocument(XML_FILE);
            NodeList books = doc.getElementsByTagName("book");
            for (int i = 0; i < books.getLength(); i++) {
                Element book = (Element) books.item(i);
                // Ищем книгу по ID
                if (book.getAttribute("id").equals(bookId.toString())) {

                    // ИСПРАВЛЕНИЕ: используем "realAmount" вместо "availableCopies"
                    String realAmountStr = book.getAttribute("realAmount");

                    // Защита от пустой строки (на случай старых данных)
                    int available = 0;
                    if (realAmountStr != null && !realAmountStr.isEmpty()) {
                        available = Integer.parseInt(realAmountStr);
                    }

                    if (available > 0) {
                        // Уменьшаем количество и сохраняем обратно в realAmount
                        book.setAttribute("realAmount", String.valueOf(available - 1));
                        saveXmlDocument(doc, XML_FILE);
                    }
                    break;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при выдаче книги в XML: " + e.getMessage(), e);
        }
    }

    // === ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ===

    private boolean validateXML() {
        try {
            SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
            Schema schema = factory.newSchema(new File(XSD_FILE));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(XML_FILE)));
            return true;
        } catch (Exception e) { return false; }
    }

    private String getElementText(Element parent, String tagName) {
        NodeList nodeList = parent.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return "";
    }

    private void addTextElement(Document doc, Element parent, String tagName, String text) {
        Element element = doc.createElement(tagName);
        element.appendChild(doc.createTextNode(text));
        parent.appendChild(element);
    }

    private Document loadXmlDocument(String filePath) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            if(filePath.equals(XML_FILE)) createEmptyBookXml();
            else createEmptyUsersXml();
        }
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(file);
    }

    private void saveXmlDocument(Document doc, String filePath) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(filePath));
        transformer.transform(source, result);
    }

    private void createEmptyBookXml() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        Element root = doc.createElement("library");
        doc.appendChild(root);
        saveXmlDocument(doc, XML_FILE);
    }
}