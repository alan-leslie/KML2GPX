package Utils;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author al
 */
public class DomUtils {

    public static String getTagValue(String sTag, Node theNode) {
        String theValue = "";

        if (theNode.getNodeType() == Node.ELEMENT_NODE) {
            Element eElement = (Element) theNode;
            NodeList namedNodes = eElement.getElementsByTagName(sTag);

            if (namedNodes != null && namedNodes.getLength() > 0) {
                NodeList nlList = namedNodes.item(0).getChildNodes();
                Node nValue = (Node) nlList.item(0);
                theValue = nValue.getNodeValue();
            }
        }

        return theValue;
    }

    public static NodeList getXPathNodes(Node theNode, String theXPath) {
        XPath placemarkXpath = XPathFactory.newInstance().newXPath();
        NodeList theData = null;

        try {
            theData = (NodeList) placemarkXpath.evaluate(theXPath, theNode, XPathConstants.NODESET);
        } catch (XPathExpressionException ex) {
            Logger.getLogger(DomUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return theData;
    }
}
