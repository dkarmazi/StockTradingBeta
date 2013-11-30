
package StockTradingServer;

import java.io.File;
import org.w3c.dom.Document;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import StockTradingCommon.Enumeration;
/**
 *
 * @author Hirosh Wickramasuriya <hirosh@gwmail.gwu.edu>
 */

// This class read xml file 
public class  ReaderXml 
{
    private String  configFile = null;
    private Document doc = null;
    
    public ReaderXml(int fileType)
    {  
        try
        {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            
            if (fileType == Enumeration.XmlFile.CONFIGURATION)
            {
                configFile = getClass().getResource(".").getFile().toString() + ".."
                                    + System.getProperty("file.separator") + "config" 
                                    + System.getProperty("file.separator") + "config.xml";
                
                doc = docBuilder.parse(new File(configFile));
            }
                    
            // normalize text representation
            doc.getDocumentElement().normalize();

        }
        catch (SAXParseException ex)
        {
            System.out.println("XML Parsing Error " 
                    + ", line" + ex.getLineNumber() 
                    + ", uri" + ex.getSystemId());
            System.out.println(" " + ex.getMessage());
        }
        catch (SAXException ex)
        {
            ex.printStackTrace();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
            
    }
    
    
    private Node getFirstLevelNode(String nodeName)
    {
        Node node = null;
        if (doc != null)
        {
            NodeList nodeList = doc.getElementsByTagName(nodeName);
            if (nodeList.getLength() >0)
            {
                node = nodeList.item(0);
            }
        }
        return node;
    }
        
    private String getValue(Node parent, String nodeName) 
    {
        String nodeValue = null;
        
        NodeList nodeList = parent.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) 
        {
            if (nodeName.equals(nodeList.item(i).getNodeName()))
            {
                nodeValue = nodeList.item(i).getTextContent();
                break;
            }
        }
        return nodeValue;
    }
    
    public String getValue(String parent, String nodeName)
    {
        Node node = getFirstLevelNode(parent);
        return getValue(node, nodeName);
    }
    
}
