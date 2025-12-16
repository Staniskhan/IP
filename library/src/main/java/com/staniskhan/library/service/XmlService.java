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

@Service
public class XmlService {
    private static String XML_FILE;
    private static String XSD_FILE;

    public List<Book> loadBooksFromXml() {
        List<Book> books = new ArrayList<>();

        try {
            ClassLoader classLoader = getClass().getClassLoader();
            StringBuilder strb = new StringBuilder(classLoader.getResource("library.xml").getPath());
            strb.delete(strb.length() - 26, strb.length());
            XML_FILE = strb.toString();
            XML_FILE += "src/main/resources/library.xml";

            StringBuilder strb1 = new StringBuilder(classLoader.getResource("library.xsd").getPath());
            strb1.delete(strb1.length() - 26, strb1.length());
            XSD_FILE = strb1.toString();
            XSD_FILE += "src/main/resources/library.xsd";

            if (!validateXML()) {
                showErrorAndExit("XML файл не соответствует схеме!");
            }

            File file = new File(XML_FILE);
            if (!file.exists()) {
                System.out.println("XML файл не найден. Создаем пустой.");
                createEmptyXmlFile();
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

            System.out.println("Загружено " + books.size() + " книг из XML");
            return books;

        } catch (Exception e) {
            System.err.println("Ошибка при загрузке XML: " + e.getMessage());
            return books;
        }
    }

        private boolean validateXML() {
        try {
            SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
            Schema schema = factory.newSchema(new File(XSD_FILE));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(XML_FILE)));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void showErrorAndExit(String message) {
        System.out.println("ERROR!!! PROBLEM WITH THE DATA FILE!!!\n" + message);
        System.exit(1);
    }

    public void addBookToXml(Book book) {
        try {
            Document doc = loadXmlDocument();
            Element root = doc.getDocumentElement();

            NodeList books = root.getElementsByTagName("book");
            long maxId = 0;
            for (int i = 0; i < books.getLength(); i++) {
                Element bookElement = (Element) books.item(i);
                long id = Long.parseLong(bookElement.getAttribute("id"));
                if (id > maxId) maxId = id;
            }

            Element bookElement = doc.createElement("book");
            bookElement.setAttribute("id", String.valueOf(maxId + 1)); // Новый ID для XML
            bookElement.setAttribute("totalCopies", String.valueOf(book.getTotalCopies()));
            bookElement.setAttribute("availableCopies", String.valueOf(book.getAvailableCopies()));

            addTextElement(doc, bookElement, "title", book.getTitle());
            addTextElement(doc, bookElement, "author", book.getAuthor());
            addTextElement(doc, bookElement, "year", String.valueOf(book.getPublicationYear()));
            addTextElement(doc, bookElement, "price", book.getPrice().toString());
            addTextElement(doc, bookElement, "category", book.getCategory());

            root.appendChild(bookElement);
            saveXmlDocument(doc);

        } catch (Exception e) {
            throw new RuntimeException("Не удалось добавить книгу в XML", e);
        }
    }

    public void updatePriceInXml(Long bookId, BigDecimal newPrice) {
        try {
            Document doc = loadXmlDocument();
            NodeList books = doc.getElementsByTagName("book");

            for (int i = 0; i < books.getLength(); i++) {
                Element book = (Element) books.item(i);
                if (book.getAttribute("id").equals(bookId.toString())) {
                    NodeList priceNodes = book.getElementsByTagName("price");
                    if (priceNodes.getLength() > 0) {
                        priceNodes.item(0).setTextContent(newPrice.toString());
                        saveXmlDocument(doc);
                        System.out.println("Цена обновлена в XML для книги ID: " + bookId);
                    }
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка при обновлении цены в XML: " + e.getMessage());
            throw new RuntimeException("Не удалось обновить цену в XML", e);
        }
    }

    public void issueBookInXml(Long bookId) {
        try {
            Document doc = loadXmlDocument();
            NodeList books = doc.getElementsByTagName("book");

            for (int i = 0; i < books.getLength(); i++) {
                Element book = (Element) books.item(i);
                if (book.getAttribute("id").equals(bookId.toString())) {
                    int available = Integer.parseInt(book.getAttribute("availableCopies"));
                    if (available > 0) {
                        book.setAttribute("availableCopies", String.valueOf(available - 1));
                        saveXmlDocument(doc);
                        System.out.println("Книга выдана (XML): ID=" + bookId);
                    }
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка при выдаче книги в XML: " + e.getMessage());
            throw new RuntimeException("Не удалось обновить XML при выдаче книги", e);
        }
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

    private Document loadXmlDocument() throws Exception {
        File file = new File(XML_FILE);
        if (!file.exists()) {
            createEmptyXmlFile();
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(file);
    }

    private void saveXmlDocument(Document doc) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(XML_FILE));
        transformer.transform(source, result);
    }

    private void createEmptyXmlFile() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();

        Element root = doc.createElement("library");
        doc.appendChild(root);

        saveXmlDocument(doc);
        System.out.println("Создан пустой XML файл: " + XML_FILE);
    }
}