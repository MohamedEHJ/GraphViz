package fr.univavignon.ceri.model;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class DLPA {

    public String nom = "Directed Label Propagation Algorithm";
    public Integer communautes=0;

    /**
     * Function to get the neighbours of a node.
     *
     * @param n
     * @param G
     * @return
     */
    public ArrayList<Nodes> getVoisinsNoeud(Nodes n, Graph G){
        ArrayList<Edge> edgeList = G.getEdges();
        ArrayList<Nodes> voisins = new ArrayList<>();
        ArrayList<Edge> edgesVoisins = new ArrayList<>();

        for (Edge edge : edgeList){
            if(edge.src.getId()==n.getId() || edge.trg.getId()==n.getId()){
                edgesVoisins.add(edge);
            }
        }

        for (Edge edge : edgesVoisins){
            if(edge.src.getId()==n.getId() && !voisins.contains(edge.src)){
                voisins.add(edge.trg);
            }
            if(edge.trg.getId()==n.getId() && !voisins.contains(edge.trg)){
                voisins.add(edge.src);
            }
            if(edge.src.getId()==n.getId() && edge.trg.getId()==n.getId()){
                continue;
            }
        }

        return voisins;
    }

    /**
     *
     *
     *
     * @param n
     * @param G
     * @return
     */
    public ArrayList<Nodes> getVoisinsEntrantsNodes(Nodes n, Graph G) {
        ArrayList<Edge> edgeList = G.getEdges();
        ArrayList<Nodes> voisinsEntrants = new ArrayList<>();
        ArrayList<Edge> edgesVoisins = new ArrayList<>();

        for (Edge edge : edgeList){
            if(edge.src.getId()==n.getId() || edge.trg.getId()==n.getId()){
                edgesVoisins.add(edge);
            }
        }
        for (Edge edge : edgesVoisins){
            if(edge.trg.getId()==n.getId()){
                voisinsEntrants.add(edge.src);
            }
        }

        return voisinsEntrants;
    }

    /**
     *
     *
     *
     * @param n
     * @param G
     * @return
     */
    public ArrayList<Nodes> getVoisinsSortantNodes(Nodes n, Graph G) {
        ArrayList<Edge> edgeList = G.getEdges();
        ArrayList<Nodes> voisinsSortants = new ArrayList<>();
        ArrayList<Edge> edgesVoisins = new ArrayList<>();

        for (Edge edge : edgeList){
            if(edge.src.getId()==n.getId()){
                voisinsSortants.add(edge.trg);
            }
        }
        return voisinsSortants;
    }

    /**
     *
     *
     *
     * @param n
     * @param G
     * @return
     */
    public int getDegreNode(Nodes n, Graph G) {
        ArrayList<Edge> edgeList = G.getEdges();
        int d = 0;
        for (Edge edge : edgeList){
            if(edge.src.getId()==n.getId() || edge.trg.getId()==n.getId()){
                d++;
            }
        }

        return d;
    }

    /**
     *
     *
     *
     * @param n
     * @param G
     * @return
     */
    public int getDegreEntrantNode(Nodes n, Graph G) {
        ArrayList<Edge> edgeList = G.getEdges();
        int d = 0;
        for (Edge edge : edgeList){
            if(edge.trg.getId()==n.getId()){
                d++;
            }
        }

        return d;
    }

    /**
     *
     *
     *
     * @param n
     * @param G
     * @return
     */
    public int getDegreSortantNode(Nodes n, Graph G) {
        ArrayList<Edge> edgeList = G.getEdges();
        int d = 0;
        for (Edge edge : edgeList){
            if(edge.src.getId()==n.getId()){
                d++;
            }
        }

        return d;
    }


    /**
     *
     *
     *
     * @param etiquettes
     * @param nodes
     * @param voisinsEntrants
     * @param voisinsSortants
     * @param kTotal
     * @param kIn
     * @param kOut
     */

    public int Algo(ArrayList<Integer> etiquettes, ArrayList<Nodes> nodes, ArrayList<ArrayList<Nodes>> voisinsEntrants, ArrayList<ArrayList<Nodes>> voisinsSortants, ArrayList<Double> kTotal, ArrayList<Double> kIn, ArrayList<Double> kOut) {

        int stop = 0;
        double poids, pMax_in, pMax_out;
        ArrayList<Nodes>voisinMax;
        System.out.print("Etiquettes avant DLPA = "+etiquettes+"\n");

        //DEBUT ALGORITHME
        poids = 0.0;
        while(stop!=1){
            voisinMax = new ArrayList<>();
            Collections.shuffle(nodes);
            for(int i = 0; i < nodes.size(); i++) {
                for(int j = 0; j < voisinsEntrants.get(i).size(); j++) {
                    poids+= (1 - ((kIn.get(i)*kOut.get(Integer.valueOf(voisinsEntrants.get(i).get(j).getId())))/(kTotal.get(i)*kTotal.get(Integer.valueOf(voisinsEntrants.get(i).get(j).getId())))));
                }

                pMax_in = poids;
                //System.out.print("Poids entrant noeud "+i+" = "+pMax_in+"\n");
                poids = 0.0;

                for(int j = 0; j < voisinsSortants.get(i).size(); j++) {
                    poids+= (1 - ((kOut.get(i)*kIn.get(Integer.valueOf(voisinsSortants.get(i).get(j).getId())))/(kTotal.get(i)*kTotal.get(Integer.valueOf(voisinsSortants.get(i).get(j).getId())))));
                }

                pMax_out = poids;
                //System.out.print("Poids sortant noeud "+i+" = "+pMax_out+"\n");
                poids = 0.0;

                if(pMax_in >= pMax_out) {
                    System.out.print("Voisin max = noeud entrant "+voisinsEntrants.get(i).get(voisinsEntrants.get(i).size()-1).getId()+"\n");
                    etiquettes.set(i, etiquettes.get(Integer.valueOf((voisinsEntrants.get(i).get(voisinsEntrants.get(i).size()-1).getId()))));
                    voisinMax.add(voisinsEntrants.get(i).get(voisinsEntrants.get(i).size()-1));
                }
                else {
                    System.out.print("Voisin max = noeud sortant "+voisinsSortants.get(i).get(voisinsSortants.get(i).size()-1).getId()+"\n");
                    etiquettes.set(i, etiquettes.get(Integer.valueOf((voisinsSortants.get(i).get(voisinsSortants.get(i).size()-1).getId()))));
                    voisinMax.add(voisinsSortants.get(i).get(voisinsSortants.get(i).size()-1));
                }
            }

            stop = 1 ;

            for (int i = 0; i<voisinMax.size();i++){
                if(etiquettes.get(i)!=etiquettes.get(Integer.valueOf(voisinMax.get(i).getId()))){
                    stop = 0;
                    break;
                }
            }
        }

        System.out.print("Etiquettes après DLPA = "+etiquettes+"\n");

        ArrayList<Integer> commuArray = new ArrayList<>();
        for(int i=0;i<etiquettes.size();i++){
            if(!commuArray.contains(etiquettes.get(i))){
                commuArray.add(etiquettes.get(i));
            }
        }
        communautes=commuArray.size();
        System.out.print("Nombre de communautes = "+communautes+"\n");

        HashMap<Integer, Color> couleursCommunautes = new HashMap<>();
        for(int i=0;i<commuArray.size();i++){
            Random rand = new Random();
            float r = rand.nextFloat();
            float g = rand.nextFloat();
            float b = rand.nextFloat();
            Color randomColor = new Color(r,g,b,1);
            couleursCommunautes.put(commuArray.get(i), randomColor);
        }
        for(Nodes n : nodes){
            if(couleursCommunautes.containsKey(etiquettes.get(Integer.valueOf(n.getId())))){
                n.setColor(couleursCommunautes.get(etiquettes.get(Integer.valueOf(n.getId()))));
            }
        }

        return communautes;
    }
}
