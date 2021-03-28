package fr.univavignon.ceri.model;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class Nodes {

    private String id;
    private String url;
    private int posX;
    private int posY;

    public static List<Nodes> nodesList = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }


    @Override
    public String toString() {
        return "<id = " + id + ", url = " + url + ", posX = " + posX + ", posY = " + posY + ">\n";
    }

    /**
     * Function to get all the node from a graphML file in an array edgeList.
     *
     * @param nList Node from DOM
     * @return
     */
    public static List<Nodes> nodeScrap(NodeList nList) {
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Nodes n = new Nodes();

            Node node = nList.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                //get all nodes info
                Element eElement = (Element) node;


                n.setId((eElement.getElementsByTagName("data").item(1).getTextContent()));
                n.setUrl(eElement.getElementsByTagName("data").item(0).getTextContent());

                nodesList.add(n);
            }
        }
        return nodesList;
    }

}