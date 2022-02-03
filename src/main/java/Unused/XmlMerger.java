/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Unused;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 *
 * @author eiker
 */
public class XmlMerger {
    public static Document merge(String expression, File... files) throws Exception {
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();
        XPathExpression compiledExpression = xpath.compile(expression);
        return merge(compiledExpression, files);
    }

  private static Document merge(XPathExpression expression, File... files) throws Exception {
    DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
    docBuilderFactory.setIgnoringElementContentWhitespace(true);
    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
    Document base = docBuilder.parse(files[0]);

    Node results = (Node) expression.evaluate(base, XPathConstants.NODE);
    if (results == null) {
      throw new IOException(files[0] + ": expression does not evaluate to node");
    }

    for (int i = 1; i < files.length; i++) {
      Document merge = docBuilder.parse(files[i]);
      System.out.println(files[i].getAbsoluteFile());
      Node nextResults = (Node) expression.evaluate(merge, XPathConstants.NODE);
      while (nextResults.hasChildNodes()) {
        Node kid = nextResults.getFirstChild();
        nextResults.removeChild(kid);
        kid = base.importNode(kid, true);
        results.appendChild(kid);
      }
    }

    return base;
  }

  public static void print(Document doc) throws Exception {
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();
    DOMSource source = new DOMSource(doc);
    Result result = new StreamResult(System.out);
    transformer.transform(source, result);
  }
  
  public static void toFile(String path, Document doc) throws IOException, TransformerException{
    DOMSource source = new DOMSource(doc);
    FileWriter writer = new FileWriter(new File(path));
    StreamResult result = new StreamResult(writer);

    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();
    transformer.transform(source, result);
  }
}
