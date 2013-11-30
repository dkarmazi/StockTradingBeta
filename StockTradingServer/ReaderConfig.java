
package StockTradingServer;

import StockTradingCommon.Enumeration;

/**
 *
 * @author Hirosh Wickramasuriya <hirosh@gwmail.gwu.edu>
 */
public class ReaderConfig 
{
    public static ReaderXml readerXml = new ReaderXml(Enumeration.XmlFile.CONFIGURATION);
    
    
    private static String dbNodeName = "database";
    private static String encryptionNodeName = "encryption";
    
    public static String getDbHostIp()
    {
        return readerXml.getValue(dbNodeName, "hostIP");
    }
    
    public static String getDbPort()
    {
        return readerXml.getValue(dbNodeName, "port");
    }
    
    public static String getDbSchema()
    {
        return readerXml.getValue(dbNodeName, "schema");
    }
    
    public static String getDbUser()
    {
        return readerXml.getValue(dbNodeName, "user");
    }
    
    public static String getDbPassword()
    {
        return readerXml.getValue(dbNodeName, "password");
    }
    
    public static String getEncryptionIV()
    {
        return readerXml.getValue(encryptionNodeName, "iv");
    }
    
    public static String getEncryptionKey()
    {
        return readerXml.getValue(encryptionNodeName, "key");
    }
}
