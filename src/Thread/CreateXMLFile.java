package Thread;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

public class CreateXMLFile {
    public static void main(String[] args) {
        try {
            // Tạo một đối tượng DocumentBuilderFactory
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // Tạo một tài liệu XML mới
            Document doc = docBuilder.newDocument();

            // Tạo phần tử gốc
            Element rootElement = doc.createElement("data");
            doc.appendChild(rootElement);

            // Tạo các phần tử con và thêm chúng vào phần tử gốc
            Element child1 = doc.createElement("child1");
            Text child1Text = doc.createTextNode("Content for child1");
            child1.appendChild(child1Text);
            rootElement.appendChild(child1);

            Element child2 = doc.createElement("child2");
            child2.setAttribute("attribute", "value");
            rootElement.appendChild(child2);

            // Lưu tài liệu XML vào tệp
            File xmlFile = new File("kq.xml");
            FileOutputStream outputStream = new FileOutputStream(xmlFile);
            javax.xml.transform.TransformerFactory.newInstance().newTransformer().transform(
                    new javax.xml.transform.dom.DOMSource(doc),
                    new javax.xml.transform.stream.StreamResult(outputStream)
            );
            outputStream.close();

            System.out.println("XML file created successfully.");

        } catch (ParserConfigurationException | FileNotFoundException | javax.xml.transform.TransformerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

