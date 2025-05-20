package nour.ebookplrmaker.service;

import org.springframework.stereotype.Service;
import java.io.File;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.R;
import org.docx4j.wml.Text;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

@Service
public class DocxService {
    public File convertToDocx(File file,String content) throws Exception{
        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
        ObjectFactory factory = new ObjectFactory();

        Document htmlDoc = Jsoup.parse(content);
        Element body = htmlDoc.body();

        for (Node node : body.childNodes()) {
            if (node instanceof Element) {
                Element elem = (Element) node;
                switch (elem.tagName()) {
                    case "p":
                        // Add paragraph text
                        wordMLPackage.getMainDocumentPart().addParagraphOfText(elem.text());
                        break;
                    case "ul":
                        // Add bullet list items
                        Elements listItems = elem.getElementsByTag("li");
                        for (Element li : listItems) {
                            P p = factory.createP();
                            R run = factory.createR();
                            Text text = factory.createText();
                            text.setValue("• " + li.text());  // simple bullet
                            run.getContent().add(text);
                            p.getContent().add(run);
                            wordMLPackage.getMainDocumentPart().getContent().add(p);
                        }
                        break;
                    // Add support for other tags here if needed
                    default:
                        // Ignore other tags for now
                        break;
                }
            }
        }

        // Save the DOCX to the file passed in the scope
        wordMLPackage.save(file);
        return file;
    }
}
