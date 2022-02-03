/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SbcReader;

import Blueprints.Item;
import VoxelMaterial.VoxelMaterial;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
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
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author eiker
 */
public class VoxelMaterialReader {
    private final String path;

    public VoxelMaterialReader(String path) {
        this.path = path;
    }

    public Map < String, VoxelMaterial > getVoxelMaterials() {
        File f = new File(path);
        if (f.isFile())
            return getVoxelMaterials(f);
        else {
            Map < String, VoxelMaterial > oreDef = new HashMap();
            String[] pathnames = f.list();

            for (String pathname: pathnames) {
                Map < String, VoxelMaterial > tempOreDef = getVoxelMaterials(path + "\\" + pathname);
                if (tempOreDef != null)
                    oreDef.putAll(tempOreDef);
            }
            return oreDef;
        }
    }

    private Map < String, VoxelMaterial > getVoxelMaterials(String path) {
        File f = new File(path);
               
        if(f.isFile()){
            if(!path.contains("Prefabs") && FilenameUtils.getExtension(path).equals("sbc")){
                return getVoxelMaterials(f);
            }
            else{
                return null;
            }
        }   
        else {
            Map < String, VoxelMaterial > oreDef = new HashMap();
            String[] pathnames = f.list();

            for (String pathname: pathnames) {
                Map < String, VoxelMaterial > tempOreDef = getVoxelMaterials(path + "\\" + pathname);
                if (tempOreDef != null)
                    oreDef.putAll(tempOreDef);
            }
            return oreDef;
        }
    }

    public Map < String, VoxelMaterial > getVoxelMaterials(File f) {
        Map < String, VoxelMaterial > result = new TreeMap();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(f);

            doc.getDocumentElement().normalize();

            XPath xPath = XPathFactory.newInstance().newXPath();
            String expression = "//Definitions/VoxelMaterials/VoxelMaterial";
            NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);

            if (nodeList.item(0) != null && !nodeList.item(0).getFirstChild().getNodeName().equals("Id") && !nodeList.item(0).getFirstChild().hasAttributes()) {

                for (int i = 0; i < nodeList.getLength(); i++) {
                    expression = "./Id/SubtypeId";
                    NodeList nList = (NodeList) xPath.compile(expression).evaluate(nodeList.item(i), XPathConstants.NODESET);

                    if (nList.item(0) == null)
                        continue;

                    expression = "./CanBeHarvested";
                    NodeList aList = (NodeList) xPath.compile(expression).evaluate(nodeList.item(i), XPathConstants.NODESET);
                    if (!"true".equals(aList.item(0).getTextContent().toLowerCase()))
                        continue;

                    expression = "./MinedOre";
                    NodeList oList = (NodeList) xPath.compile(expression).evaluate(nodeList.item(i), XPathConstants.NODESET);
                    String minedOreSubtypeId = oList.item(0).getTextContent();

                    expression = "./MinedOreRatio";
                    NodeList pList = (NodeList) xPath.compile(expression).evaluate(nodeList.item(i), XPathConstants.NODESET);
                    double minedOreRatio = Double.parseDouble(pList.item(0).getTextContent());
                                        
                    expression = "./AsteroidGeneratorSpawnProbabilityMultiplier";
                    NodeList qList = (NodeList) xPath.compile(expression).evaluate(nodeList.item(i), XPathConstants.NODESET);
                    expression = "./SpawnsInAsteroids";
                    NodeList rList = (NodeList) xPath.compile(expression).evaluate(nodeList.item(i), XPathConstants.NODESET);
                    double asteroidProbability = 1d;
                    if(rList.item(0) != null)
                        if(!Boolean.parseBoolean(rList.item(0).getTextContent()))
                            asteroidProbability = 0d;
                    if(asteroidProbability == 1 && qList.item(0) != null)
                        asteroidProbability = Double.parseDouble(qList.item(0).getTextContent());
                    
                    String subtypeId = nList.item(0).getTextContent();

                    result.put(subtypeId, new VoxelMaterial(subtypeId, new Item(minedOreSubtypeId, "Ore"), minedOreRatio, asteroidProbability));
                }
            }
        } catch (ParserConfigurationException | XPathExpressionException | SAXException | IOException ex) {
            Logger.getLogger(VoxelMaterialReader.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }
}