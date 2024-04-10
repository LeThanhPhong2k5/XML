package Thread;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

class MyThread extends Thread {
    private int threadNumber;

    public MyThread(int threadNumber) {
        this.threadNumber = threadNumber;
    }

    public void run() {
        // Tạo nội dung thông tin thread
        String id = "23NS075";
        String name = "Lê Thanh Phong";
        String address = "Hà Tĩnh";
        Date dateOfBirth = new Date(105,2,22);

        // Tính tuổi của học sinh
        Period agePeriod = calculateAge(dateOfBirth);
        int age = agePeriod.getYears();

        // Tính tổng các chữ số trong ngày sinh
        int sum = calculateSumOfDigits(dateOfBirth);

        // Kiểm tra xem tổng có phải là số nguyên tố không
        int isPrime = isPrime(sum) ? 1 : 0;

        // Ghi thông tin thread vào tệp XML
        writeToXML(id, name, address, dateOfBirth);

        // Cập nhật thông tin vào file kq.xml
        updateResultFile(age, sum, isPrime);
    }

    private synchronized void writeToXML(String id, String name, String address, Date dateOfBirth) {
        try {
            // Tạo một đối tượng DocumentBuilderFactory
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // Đọc tệp XML hiện có hoặc tạo một tệp mới nếu không tồn tại
            File xmlFile = new File("students.xml");
            Document doc;
            Element rootElement;
            if (xmlFile.exists()) {
                doc = docBuilder.parse(xmlFile);
                rootElement = doc.getDocumentElement();
            } else {
                doc = docBuilder.newDocument();
                rootElement = doc.createElement("students");
                doc.appendChild(rootElement);
            }

            // Tạo một phần tử mới cho thông tin của học sinh
            Element studentElement = doc.createElement("student");
            rootElement.appendChild(studentElement);

            // Thêm các phần tử con (id, name, address, dateOfBirth) vào phần tử học sinh
            Element idElement = doc.createElement("id");
            idElement.appendChild(doc.createTextNode(id));
            studentElement.appendChild(idElement);

            Element nameElement = doc.createElement("name");
            nameElement.appendChild(doc.createTextNode(name));
            studentElement.appendChild(nameElement);

            Element addressElement = doc.createElement("address");
            addressElement.appendChild(doc.createTextNode(address));
            studentElement.appendChild(addressElement);

            Element dateOfBirthElement = doc.createElement("dateOfBirth");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            dateOfBirthElement.appendChild(doc.createTextNode(dateFormat.format(dateOfBirth)));
            studentElement.appendChild(dateOfBirthElement);

            // Ghi tài liệu XML vào tệp
            FileOutputStream outputStream = new FileOutputStream(xmlFile);
            javax.xml.transform.TransformerFactory.newInstance().newTransformer().transform(
                    new javax.xml.transform.dom.DOMSource(doc),
                    new javax.xml.transform.stream.StreamResult(outputStream)
            );
            outputStream.close();
        } catch (ParserConfigurationException | IOException | SAXException | javax.xml.transform.TransformerException e) {
            e.printStackTrace();
        }
    }

    private void updateResultFile(int age, int sum, int isPrime) {
        try {
            // Tạo một đối tượng DocumentBuilderFactory
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // Đọc tệp XML hiện có hoặc tạo một tệp mới nếu không tồn tại
            File xmlFile = new File("kq.xml");
            Document doc;
            Element rootElement;
            if (xmlFile.exists()) {
                doc = docBuilder.parse(xmlFile);
                rootElement = doc.getDocumentElement();
            } else {
                doc = docBuilder.newDocument();
                rootElement = doc.createElement("kq");
                doc.appendChild(rootElement);
            }

            // Tạo hoặc cập nhật phần tử tuổi (age)
            Element ageElement = (Element) doc.getElementsByTagName("age").item(0);
            if (ageElement == null) {
                ageElement = doc.createElement("age");
                rootElement.appendChild(ageElement);
            }
            ageElement.setTextContent(String.valueOf(age));

            // Tạo hoặc cập nhật phần tử sum
            Element sumElement = (Element) doc.getElementsByTagName("sum").item(0);
            if (sumElement == null) {
                sumElement = doc.createElement("sum");
                rootElement.appendChild(sumElement);
            }
            sumElement.setTextContent(String.valueOf(sum));

            // Tạo hoặc cập nhật phần tử isPrime
            Element isPrimeElement = (Element) doc.getElementsByTagName("isPrime").item(0);
            if (isPrimeElement == null) {
                isPrimeElement = doc.createElement("isPrime");
                rootElement.appendChild(isPrimeElement);
            }
            isPrimeElement.setTextContent(String.valueOf(isPrime));

            // Ghi tài liệu XML vào tệp
            FileOutputStream outputStream = new FileOutputStream(xmlFile);
            javax.xml.transform.TransformerFactory.newInstance().newTransformer().transform(
                    new javax.xml.transform.dom.DOMSource(doc),
                    new javax.xml.transform.stream.StreamResult(outputStream)
            );
            outputStream.close();
        } catch (ParserConfigurationException | IOException | SAXException | javax.xml.transform.TransformerException e) {
            e.printStackTrace();
        }
    }


    private Period calculateAge(Date dateOfBirth) {
        LocalDate birthDate = LocalDate.of(dateOfBirth.getYear() + 1900, dateOfBirth.getMonth() + 1, dateOfBirth.getDate());
        LocalDate currentDate = LocalDate.now();
        return Period.between(birthDate, currentDate);
    }

    private int calculateSumOfDigits(Date dateOfBirth) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
        String dateString = dateFormat.format(dateOfBirth);
        int sum = 0;
        for (int i = 0; i < dateString.length(); i++) {
            sum += Character.getNumericValue(dateString.charAt(i));
        }
        return sum;
    }

    private boolean isPrime(int number) {
        if (number <= 1) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }
}

public class Main {
    public static void main(String[] args) {
        // Tạo và chạy 3 threads để ghi thông tin của học sinh vào tệp XML
        for (int i = 1; i <= 3; i++) {
            Thread thread1 = new MyThread(i);
            thread1.start();
        }
    }
}

