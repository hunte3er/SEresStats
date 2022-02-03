/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SbcReader;

import Blueprints.Blueprint;
import Blueprints.Ingredient;
import Blueprints.Item;
import Util.BlacklistCheck;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author eiker
 */
public class BlueprintReader {
    private final String path;
    private final DocumentBuilderFactory dbf;
    
    public BlueprintReader(String path){
        this.path = path;
        this.dbf = DocumentBuilderFactory.newInstance();
    }
    
    public List<Blueprint> getBlueprints(){
        File f = new File(path);
        if(f.isFile())
            return getBlueprints(f);
        else {
            List<Blueprint> blueprints = new ArrayList();
            String[] pathnames = f.list();      

            for(String pathname : pathnames){
                List<Blueprint> tempBlueprints = getBlueprints(path + "\\" + pathname);
                if(tempBlueprints != null)
                    blueprints.addAll(tempBlueprints);
            }
            return blueprints;
        }
    }
    
    private List<Blueprint> getBlueprints(String path){        
        File f = new File(path);
        if(f.isFile()){
            if(!path.contains("Prefabs") && FilenameUtils.getExtension(path).equals("sbc")){
                return getBlueprints(f);
            }
            else{
                return null;
            }
        }
        else {
            List<Blueprint> blueprints = new ArrayList();
            String[] pathnames = f.list();      

            for(String pathname : pathnames){
                List<Blueprint> tempBlueprints = getBlueprints(path + "\\" + pathname);
                if(tempBlueprints != null)
                    blueprints.addAll(tempBlueprints);
            }
            return blueprints;
        }
    }
    
    public List<Blueprint> getBlueprints(File f){        
        List<Blueprint> result = new ArrayList();
        try {            
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(f);
            
            doc.getDocumentElement().normalize();
            
            XPath xPath = XPathFactory.newInstance().newXPath();
            String expression = "//Definitions/Blueprints/Blueprint";
            NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
            
            if(nodeList.item(0) != null && !nodeList.item(0).getFirstChild().getNodeName().equals("Id") && !nodeList.item(0).getFirstChild().hasAttributes() ){
                
                for(int i = 0; i < nodeList.getLength(); i++){
                    expression = "./Id/SubtypeId";
                    NodeList nList = (NodeList) xPath.compile(expression).evaluate(nodeList.item(i), XPathConstants.NODESET);
                    
                    if(nList.item(0) == null)
                        continue;
                    
                    List<Ingredient> input = new ArrayList();
                    List<Ingredient> output = new ArrayList();
                    
                    expression = "./Prerequisites/Item";
                    NodeList oList = (NodeList) xPath.compile(expression).evaluate(nodeList.item(i), XPathConstants.NODESET);
                    
                    for(int j = 0; j < oList.getLength(); j++){
                        NamedNodeMap att = oList.item(j).getAttributes();
                        String subtypeId = att.getNamedItem("SubtypeId").getNodeValue();
                        String type = att.getNamedItem("TypeId").getNodeValue();
                        double amount = Double.parseDouble(att.getNamedItem("Amount").getNodeValue());
                        input.add(new Ingredient(new Item(subtypeId, type), amount));
                    }
                    
                    expression = "./Results/Item | ./Result";
                    NodeList pList = (NodeList) xPath.compile(expression).evaluate(nodeList.item(i), XPathConstants.NODESET);
                    
                    for(int j = 0; j < pList.getLength(); j++){
                        NamedNodeMap att = pList.item(j).getAttributes();
                        String subtypeId = att.getNamedItem("SubtypeId").getNodeValue();
                        String type = att.getNamedItem("TypeId").getNodeValue();
                        double amount = Double.parseDouble(att.getNamedItem("Amount").getNodeValue());
                        output.add(new Ingredient(new Item(subtypeId, type), amount));
                    }
                    
                    String subtypeId = nList.item(0).getTextContent();
                    if(BlacklistCheck.checkBlueprint(subtypeId))
                        result.add(new Blueprint(subtypeId, input, output));
                }
            }
        } catch (ParserConfigurationException | XPathExpressionException | SAXException | IOException ex) {
            Logger.getLogger(BlueprintReader.class.getName()).log(Level.SEVERE, null, ex);
        }        
        return result;
    }
}
