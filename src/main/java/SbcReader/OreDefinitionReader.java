/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SbcReader;

import OreDefinitionReader.ComplexMaterialGroup;
import OreDefinitionReader.ComplexMaterialRule;
import OreDefinitionReader.OreDefinition;
import OreDefinitionReader.OreDefinitionSingle;
import PlanetMappings.VoxelLayer;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
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
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import static Util.Constants.*;
import VoxelMaterial.VoxelMaterial;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Node;

/**
 *
 * @author eiker
 */
public class OreDefinitionReader {

    private final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();    
    private final String path;
    private final XPath xPath;
    private final Map < String, VoxelMaterial > voxelMaterials;
    
    public OreDefinitionReader(String path, Map < String, VoxelMaterial > voxelMaterials){ 
        this.path = path;  
        this.xPath = XPathFactory.newInstance().newXPath();
        this.voxelMaterials = voxelMaterials;
    }
    
    public OreDefinition getOreDefinition(){
        File f = new File(path);
        if(f.isFile())
            return getOreDefinition(f);
        else {
            OreDefinition oreDef = new OreDefinition();
            String[] pathnames = f.list();      

            for(String pathname : pathnames){
                OreDefinition tempOreDef = OreDefinitionReader.this.getOreDefinition(path + "\\" + pathname);
                if(tempOreDef != null)
                    oreDef.combine(tempOreDef);
            }
            return oreDef;
        }
    }
    
    private OreDefinition getOreDefinition(String path){
        File f = new File(path);
        if(f.isFile()){
            if(!path.contains("Prefabs") && FilenameUtils.getExtension(path).equals("sbc")){
                return getOreDefinition(f);
            }
            else{
                return null;
            }
        }
        else {
            OreDefinition oreDef = new OreDefinition();
            String[] pathnames = f.list();      

            for(String pathname : pathnames){
                OreDefinition tempOreDef = OreDefinitionReader.this.getOreDefinition(path + "\\" + pathname);
                if(tempOreDef != null)
                    oreDef.combine(tempOreDef);
            }
            return oreDef;
        }
    }
    
    private OreDefinition getOreDefinition(File f){ 
        OreDefinition result = new OreDefinition();
        try {
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(f);
            
            doc.getDocumentElement().normalize();
            
            String expression = XPATH_PLANET_GENERATOR_DEF;
            NodeList nodeList = (NodeList) this.xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
            
            if(nodeList.item(0) != null && !nodeList.item(0).getFirstChild().getNodeName().equals("Id") && !nodeList.item(0).getFirstChild().hasAttributes() ){
                
            for(int i = 0; i < nodeList.getLength(); i++){
                expression = XPATH_SUBTYPE_ID;
                NodeList subNodeList = (NodeList) this.xPath.compile(expression).evaluate(nodeList.item(i), XPathConstants.NODESET);
                
                if(subNodeList.item(0) == null)
                    continue;
                
                String subtypeId = subNodeList.item(0).getTextContent();
                
                String tempPath = "";
                if(f.getParent().toLowerCase().endsWith("data")){
                    tempPath = f.getParent();
                }
                else{
                    String[] tempArr = f.getParent().split("\\\\");
                    List<String> tempL = new ArrayList(java.util.Arrays.asList(tempArr));
                    int dataIndex = tempL.indexOf("Data");
                    tempL.subList(dataIndex + 1, tempL.size()).clear();
                    tempArr = tempL.toArray(new String[0]);
                    tempPath = String.join("\\", tempArr);
                }
                
                File tempF = new File(tempPath + PATH_SEPERATOR + IMAGE_FOLDER);
                String[] directories = tempF.list((File current, String name) -> new File(current, name).isDirectory());
                
                if(!Arrays.asList(EXCLUDES).contains(subtypeId) && Arrays.asList(directories).contains(subtypeId)){                    
                    expression = "./HillParams";
                    subNodeList = (NodeList) this.xPath.compile(expression).evaluate(nodeList.item(i), XPathConstants.NODESET);
                    
                    NamedNodeMap hillParams = subNodeList.item(0).getAttributes();
                    float hillMin = Float.parseFloat(hillParams.getNamedItem("Min").getNodeValue());
                    float hillMax = Float.parseFloat(hillParams.getNamedItem("Max").getNodeValue());
                    
                    result.addOreDefinitionPlanet(subtypeId);
                    result.getOreDefinitionPlanet(subtypeId).setHillMin(hillMin);
                    result.getOreDefinitionPlanet(subtypeId).setHillMax(hillMax);
                    result.getOreDefinitionPlanet(subtypeId).setDefaultSubsurfaceMaterial(getDefaultSubsurfaceMaterial(nodeList.item(0)));
                    result.getOreDefinitionPlanet(subtypeId).setDefaultSurfaceMaterial(getDefaultSurfaceMaterial(nodeList.item(0)));
                    
                    result.getOreDefinitionPlanet(subtypeId).addOreDefinitions(this.getOreDefinitions(nodeList.item(i), XPATH_ORE)); 
                    result.getOreDefinitionPlanet(subtypeId).addMaterialDefinitions(this.getOreDefinitions(nodeList.item(i), XPATH_MATERIAL)); 
                    result.getOreDefinitionPlanet(subtypeId).addComplexMaterialDefinitions(this.getComplexMaterialGroups(nodeList.item(i)));
                }
            }
           }
        } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException ex) {
            Logger.getLogger(OreDefinitionReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }   
    
    private  Map<String, OreDefinitionSingle> getOreDefinitions(Node node, String xPathStr){
        Map<String, OreDefinitionSingle> result = new HashMap();
        try {
            NodeList nodeList = (NodeList) this.xPath.compile(xPathStr).evaluate(node, XPathConstants.NODESET);
            
            for(int j = 0; j < nodeList.getLength(); j++){
                NamedNodeMap oreTags = nodeList.item(j).getAttributes();
                
                String type = "";
                int start = 0;
                int depth = 0;
                
                if(xPathStr.equals(XPATH_ORE)){
                    type = oreTags.getNamedItem("Type").getNodeValue();
                    start = Integer.parseInt(oreTags.getNamedItem("Start").getNodeValue());
                    depth = Integer.parseInt(oreTags.getNamedItem("Depth").getNodeValue());
                }
                else if (xPathStr.equals(XPATH_MATERIAL)){
                    type = oreTags.getNamedItem("Material").getNodeValue();
                    depth = Integer.parseInt(oreTags.getNamedItem("MaxDepth").getNodeValue());
                }
                
                int value = Integer.parseInt(oreTags.getNamedItem("Value").getNodeValue());
                
                result.put(type, new OreDefinitionSingle(value, start, depth, type));
            }
            
        } catch (XPathExpressionException ex) {
            Logger.getLogger(OreDefinitionReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    private  VoxelMaterial getDefaultSubsurfaceMaterial(Node node){
        VoxelMaterial result = null;
        try {
            NodeList nodeList = (NodeList) this.xPath.compile("./DefaultSubSurfaceMaterial").evaluate(node, XPathConstants.NODESET);
            if(nodeList.item(0) != null){
                NamedNodeMap tags = nodeList.item(0).getAttributes();
                result = this.voxelMaterials.get(tags.getNamedItem("Material").getNodeValue());  
            }
            else
                result = this.voxelMaterials.get("Stone");
        } catch (XPathExpressionException ex) {
            Logger.getLogger(OreDefinitionReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    private  VoxelLayer getDefaultSurfaceMaterial(Node node){
        VoxelLayer result = null;
        try {
            NodeList nodeList = (NodeList) this.xPath.compile("./DefaultSurfaceMaterial").evaluate(node, XPathConstants.NODESET);
            NamedNodeMap tags = nodeList.item(0).getAttributes();
            String material = tags.getNamedItem("Material").getNodeValue();    
            int depth = Integer.parseInt(tags.getNamedItem("MaxDepth").getNodeValue()); 
            result = new VoxelLayer(0, depth, this.voxelMaterials.get(material));
        } catch (XPathExpressionException ex) {
            Logger.getLogger(OreDefinitionReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    private Map<Integer, ComplexMaterialGroup> getComplexMaterialGroups(Node node){
        Map<Integer, ComplexMaterialGroup> result = new HashMap();
        try {
            String expression = XPATH_COMPLEX;
            NodeList groupList = (NodeList) this.xPath.compile(expression).evaluate(node, XPathConstants.NODESET);
            
            for(int j = 0; j < groupList.getLength(); j++){
                Node materialGroup = groupList.item(j);
                NamedNodeMap tagList = materialGroup.getAttributes();
                
                String name = tagList.getNamedItem("Name").getNodeValue();
                int value = Integer.parseInt(tagList.getNamedItem("Value").getNodeValue());
            
                ComplexMaterialGroup compMatGrp = new ComplexMaterialGroup(name, value);
                List<ComplexMaterialRule> compMats = new ArrayList();
               
                NodeList rules = (NodeList) this.xPath.compile("./Rule").evaluate(materialGroup, XPathConstants.NODESET);
                Map<VoxelMaterial, Integer> IdValues = new HashMap();
                
                for(int m = 0; m < rules.getLength(); m++){
                    NodeList subNodeList = (NodeList) this.xPath.compile("./Layers/Layer").evaluate(rules.item(m), XPathConstants.NODESET);

                    for(int k = 0; k < subNodeList.getLength(); k++){
                        NamedNodeMap oreTag = subNodeList.item(k).getAttributes();
                        IdValues.put(this.voxelMaterials.get(oreTag.getNamedItem("Material").getNodeValue()), Integer.parseInt(oreTag.getNamedItem("Depth").getNodeValue()));                    
                    }

                    subNodeList = (NodeList) this.xPath.compile("./Height").evaluate(rules.item(m), XPathConstants.NODESET);
                    tagList = subNodeList.item(0).getAttributes();
                    double heightMin = Double.parseDouble(tagList.getNamedItem("Min").getNodeValue());
                    double heightMax = Double.parseDouble(tagList.getNamedItem("Max").getNodeValue());

                    subNodeList = (NodeList) this.xPath.compile("./Latitude").evaluate(rules.item(m), XPathConstants.NODESET);
                    tagList = subNodeList.item(0).getAttributes();
                    int latMin = Integer.parseInt(tagList.getNamedItem("Min").getNodeValue());
                    int latMax = Integer.parseInt(tagList.getNamedItem("Max").getNodeValue());

                    subNodeList = (NodeList) this.xPath.compile("./Slope").evaluate(rules.item(m), XPathConstants.NODESET);
                    tagList = subNodeList.item(0).getAttributes();
                    int slopeMin = Integer.parseInt(tagList.getNamedItem("Min").getNodeValue());
                    int slopeMax = Integer.parseInt(tagList.getNamedItem("Max").getNodeValue());

                    compMats.add(new ComplexMaterialRule(IdValues, heightMin, heightMax, latMin, latMax, slopeMin, slopeMax));
                }                
                compMatGrp.addComplexMaterials(compMats);
                result.put(value, compMatGrp);
            }
            
        } catch (XPathExpressionException ex) {
            Logger.getLogger(OreDefinitionReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
