package fr.univavignon.ceri.model;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class Edge implements Comparable<Edge> {
    private int source;
    private int target;
    private Nodes src;
    private Nodes trg;
    private int poids;
    private int posX;
    private int posY;

    public void setSrc(Nodes src) {
        this.src = src;
    }

    public void setTrg(Nodes trg) {
        this.trg = trg;
    }

    public int getSource() {
        return source;
    }

    public int getTarget() {
        return target;
    }

    public int getPoids() {
        return poids;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public void setPoids(int poids) {
        this.poids = poids;
    }

    @Override
    public String toString() {
        return "<source: " + src.getUrl() + " destination: " + trg.getUrl() + " poids: " + poids + ">\n";
    }

    /**
     * Function to get all the edge from a graphML file in an array edgeList.
     *
     * @param eList Node from DOM
     * @return
     */
    public static List<Edge> edgeScrap(NodeList eList) {
        List<Edge> edgeList = new ArrayList<>();

        for (int temp = 0; temp < eList.getLength(); temp++) {
            Edge e = new Edge();

            Node node = eList.item(temp);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                //Print each employee's detail
                Element eElement = (Element) node;

                e.setSource(Integer.parseInt(eElement.getAttribute("source").substring(1)));
                e.setTarget(Integer.parseInt(eElement.getAttribute("target").substring(1)));
                e.setPoids((int) Float.parseFloat(eElement.getElementsByTagName("data").item(0).getTextContent()));

                edgeList.add(e);
            }
        }
        return edgeList;
    }

    @Override
    public int compareTo(Edge compareSource) {
        int cs = ((Edge) compareSource).getSource();
        /* For Ascending order*/
        return cs - this.source;

        /* For Descending order do like this */
        //return compareage-this.studentage;
    }

}
