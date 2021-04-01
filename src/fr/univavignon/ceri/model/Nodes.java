package fr.univavignon.ceri.model;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class Nodes {

    private String id;
    private String url;
    private float posX;
    private float posY;

    private float displacementX;
    private float displacementY;

    public float getDisplacementX() {
        return displacementX;
    }

    public void setDisplacementX(float displacementX) {
        this.displacementX = displacementX;
    }

    public float getDisplacementY() {
        return displacementY;
    }

    public void setDisplacementY(float displacementY) {
        this.displacementY = displacementY;
    }

    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

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
        List<Nodes> nodesList = new ArrayList<>();

        for (int temp = 0; temp < nList.getLength(); temp++) {
            Nodes n = new Nodes();

            Node node = nList.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                //get all nodes info
                Element eElement = (Element) node;


                n.setId((eElement.getElementsByTagName("data").item(1).getTextContent()));
                n.setUrl(eElement.getElementsByTagName("data").item(0).getTextContent());

                // Check for any n in the id of node, to avoid error while Integer.parseInt()
                n.setId(n.getId().replace("n", ""));

                nodesList.add(n);
            }
        }
        return nodesList;
    }

}