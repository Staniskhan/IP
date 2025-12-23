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
import javax.xml.xpath.*;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.staniskhan.library.model.Book;
import com.staniskhan.library.model.User;

@Service
public class XmlService {
    private static String XML_FILE;
    private static String XSD_FILE;
    private static String USERS_XML_FILE;

    public XmlService() {
        String baseDir = System.getProperty("user.dir");
        XML_FILE = baseDir + File.separator + "books.xml";
        XSD_FILE = baseDir + File.separator + "books.xsd";
        USERS_XML_FILE = baseDir + File.separator + "users.xml";
    }

    public List<Book> loadBooksFromXml() {
        List<Book> books = new ArrayList<>();
        try {
            Document doc = loadXmlDocument(XML_FILE);
            NodeList nodeList = doc.getElementsByTagName("book");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    books.add(parseBookElement(element));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return books;
    }

    public void addBookToXml(Book book) {
        try {
            Document doc = loadXmlDocument(XML_FILE);
            Element root = doc.getDocumentElement();

            Element bookElement = doc.createElement("book");

            long nextId = 1;
            NodeList books = doc.getElementsByTagName("book");
            if (books.getLength() > 0) {
                Element lastBook = (Element) books.item(books.getLength() - 1);
                nextId = Long.parseLong(lastBook.getAttribute("id")) + 1;
            }

            bookElement.setAttribute("id", String.valueOf(nextId));
            bookElement.setAttribute("amount", String.valueOf(book.getTotalCopies()));
            bookElement.setAttribute("realAmount", String.valueOf(book.getTotalCopies()));

            addChildElement(doc, bookElement, "title", book.getTitle());
            addChildElement(doc, bookElement, "author", book.getAuthor());
            addChildElement(doc, bookElement, "year", String.valueOf(book.getPublicationYear()));
            addChildElement(doc, bookElement, "price", book.getPrice().toString());
            addChildElement(doc, bookElement, "category", book.getCategory());

            root.appendChild(bookElement);
            saveXmlDocument(doc, XML_FILE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updatePriceInXml(Long bookId, BigDecimal newPrice) {
        try {
            Document doc = loadXmlDocument(XML_FILE);
            NodeList nodeList = doc.getElementsByTagName("book");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                if (Long.parseLong(element.getAttribute("id")) == bookId) {
                    element.getElementsByTagName("price").item(0).setTextContent(newPrice.toString());
                    break;
                }
            }
            saveXmlDocument(doc, XML_FILE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void issueBookInXml(Long bookId) {
        try {
            Document doc = loadXmlDocument(XML_FILE);
            NodeList nodeList = doc.getElementsByTagName("book");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                if (Long.parseLong(element.getAttribute("id")) == bookId) {
                    int realAmount = Integer.parseInt(element.getAttribute("realAmount"));
                    if (realAmount > 0) {
                        element.setAttribute("realAmount", String.valueOf(realAmount - 1));
                    }
                    break;
                }
            }
            saveXmlDocument(doc, XML_FILE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void returnBookInXml(Long bookId) {
        try {
            Document doc = loadXmlDocument(XML_FILE);
            NodeList nodeList = doc.getElementsByTagName("book");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                if (Long.parseLong(element.getAttribute("id")) == bookId) {
                    int realAmount = Integer.parseInt(element.getAttribute("realAmount"));
                    int amount = Integer.parseInt(element.getAttribute("amount"));
                    if (realAmount < amount) {
                        element.setAttribute("realAmount", String.valueOf(realAmount + 1));
                    }
                    break;
                }
            }
            saveXmlDocument(doc, XML_FILE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<User> loadUsersFromXml() {
        List<User> users = new ArrayList<>();
        try {
            Document doc = loadXmlDocument(USERS_XML_FILE);
            NodeList nodeList = doc.getElementsByTagName("user");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                User user = new User();
                user.setUsername(element.getElementsByTagName("username").item(0).getTextContent());
                user.setPassword(element.getElementsByTagName("password").item(0).getTextContent());
                user.setRole(element.getElementsByTagName("role").item(0).getTextContent());

                NodeList borrowedList = element.getElementsByTagName("borrowedBook");
                List<String> books = new ArrayList<>();
                for (int j = 0; j < borrowedList.getLength(); j++) {
                    books.add(borrowedList.item(j).getTextContent());
                }
                user.setBorrowedBooks(books);
                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    public void addUserToXml(User user) {
        try {
            Document doc = loadXmlDocument(USERS_XML_FILE);
            Element root = doc.getDocumentElement();

            NodeList existingUsers = doc.getElementsByTagName("username");
            for (int i = 0; i < existingUsers.getLength(); i++) {
                if (existingUsers.item(i).getTextContent().equals(user.getUsername())) {
                    return;
                }
            }

            Element userElement = doc.createElement("user");
            addChildElement(doc, userElement, "username", user.getUsername());
            addChildElement(doc, userElement, "password", user.getPassword());
            addChildElement(doc, userElement, "role", user.getRole());
            userElement.appendChild(doc.createElement("borrowedBooks"));

            root.appendChild(userElement);
            saveXmlDocument(doc, USERS_XML_FILE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateUserBorrowedBooksInXml(String username, List<String> borrowedBooks) {
        try {
            Document doc = loadXmlDocument(USERS_XML_FILE);
            NodeList users = doc.getElementsByTagName("user");

            for (int i = 0; i < users.getLength(); i++) {
                Element userEl = (Element) users.item(i);
                if (userEl.getElementsByTagName("username").item(0).getTextContent().equals(username)) {
                    Element borrowedBooksEl = (Element) userEl.getElementsByTagName("borrowedBooks").item(0);

                    while (borrowedBooksEl.hasChildNodes()) {
                        borrowedBooksEl.removeChild(borrowedBooksEl.getFirstChild());
                    }

                    for (String title : borrowedBooks) {
                        addChildElement(doc, borrowedBooksEl, "borrowedBook", title);
                    }
                    break;
                }
            }
            saveXmlDocument(doc, USERS_XML_FILE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Book> searchBooksByXPath(String expression) {
        List<Book> results = new ArrayList<>();
        try {
            Document doc = loadXmlDocument(XML_FILE);
            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xpath = xPathFactory.newXPath();

            NodeList nodes = (NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);

            for (int i = 0; i < nodes.getLength(); i++) {
                Element element = (Element) nodes.item(i);
                results.add(parseBookElement(element));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    private Book parseBookElement(Element e) {
        Book book = new Book();
        book.setId(Long.parseLong(e.getAttribute("id")));
        book.setTitle(e.getElementsByTagName("title").item(0).getTextContent());
        book.setAuthor(e.getElementsByTagName("author").item(0).getTextContent());
        book.setPublicationYear(Integer.parseInt(e.getElementsByTagName("year").item(0).getTextContent()));
        book.setCategory(e.getElementsByTagName("category").item(0).getTextContent());
        book.setPrice(new java.math.BigDecimal(e.getElementsByTagName("price").item(0).getTextContent()));
        book.setTotalCopies(Integer.parseInt(e.getAttribute("amount")));
        book.setAvailableCopies(Integer.parseInt(e.getAttribute("realAmount")));
        return book;
    }

    private void addChildElement(Document doc, Element parent, String name, String text) {
        Element element = doc.createElement(name);
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
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);
        doc.getDocumentElement().normalize();
        return doc;
    }

    private void saveXmlDocument(Document doc, String filePath) throws Exception {
        cleanEmptyNodes(doc.getDocumentElement());
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(filePath));
        transformer.transform(source, result);
    }

    private void cleanEmptyNodes(Node node) {
        NodeList childNodes = node.getChildNodes();
        for (int i = childNodes.getLength() - 1; i >= 0; i--) {
            Node child = childNodes.item(i);
            if (child.getNodeType() == Node.TEXT_NODE && child.getTextContent().trim().isEmpty()) {
                node.removeChild(child);
            } else if (child.getNodeType() == Node.ELEMENT_NODE) {
                cleanEmptyNodes(child);
            }
        }
    }

    private void createEmptyBookXml() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        Element root = doc.createElement("library");
        doc.appendChild(root);
        saveXmlDocument(doc, XML_FILE);
    }

    private void createEmptyUsersXml() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        Element root = doc.createElement("users");
        doc.appendChild(root);
        saveXmlDocument(doc, USERS_XML_FILE);
    }
}