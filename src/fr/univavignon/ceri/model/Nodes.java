package fr.univavignon.ceri.model;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class Nodes {

    private int id;
    private String url;

    public static List<Nodes> nodesList = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void displayList() {
        for (int i = 0; i < nodesList.size(); i++) {
            System.out.print(nodesList.get(i));
        }
    }

    @Override
    public String toString() {
        return ("<id = " + id + " url = " + url + ">\n");
    }

    /**
     * Function to get all the node from a graphML file in an array edgeList.
     *
     * @param nList Node from DOM
     */
    public void nodeScrap(NodeList nList) {
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Nodes n = new Nodes();

            Node node = nList.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                //get all nodes info
                Element eElement = (Element) node;


                n.setId(Integer.parseInt(eElement.getElementsByTagName("data").item(1).getTextContent()));
                n.setUrl(eElement.getElementsByTagName("data").item(0).getTextContent());

                nodesList.add(n);
            }
        }
    }

}