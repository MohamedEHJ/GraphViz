package fr.univavignon.ceri.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PageRank {
    public ArrayList<Nodes> nodes;
    public ArrayList<Edge> edges;
    public Map<Edge, Double> wIn;
    public Map<Edge, Double> wOut;
    public Map<String, ArrayList<Integer>> listeES;
    public Map<String, ArrayList<Double>> listePR;
    public int iteration = 15;
    public double d = 0.85;
    public int tailleMax = 20;
    public int tailleMin = 5;

    public PageRank(ArrayList<Edge> a, ArrayList<Nodes> b) {
        this.listeES = new HashMap<>();
        this.edges = a;
        this.nodes = b;
        wIn = new HashMap<>();
        wOut = new HashMap<>();
        listePR = new HashMap<>();
        fillListeES();
        wIn();
        wOut();
        wpr();
    }

    /**
        Function that get the number of input and output of each edges in a hashMap.

     **/
    public void fillListeES() {
        for(Nodes nod : nodes ){
            ArrayList tmp = new ArrayList<Integer>();
            tmp.add(0);
            tmp.add(0);
            getListeES().put(nod.getUrl(), tmp);
        }

        for(Edge edg : edges){
            for(String i : getListeES().keySet()){
                if(i.equals(edg.getSrc().getUrl())){
                    getListeES().get(i).set(1,getListeES().get(i).get(1) + 1);
                }
                if(i.equals(edg.getTrg().getUrl())){
                    getListeES().get(i).set(0,getListeES().get(i).get(0) + 1);
                }
            }
        }
        //System.out.println(getListeES().toString());

    }

    /**
     * Function that put the weightIN of each nodes in a HashMap
     */
    public void wIn(){
        int somme;
        for(Edge i : edges){//A
            somme = getListeES().get(i.getTrg().getUrl()).get(0);
            for(Edge j : edges) {//p1
                if(!i.equals(j)) {
                    if (j.getSrc().getUrl().equals(i.getTrg().getUrl())) {
                        somme += getListeES().get(j.getTrg().getUrl()).get(0);
                    }
                }
            }
            wIn.put(i, (double) getListeES().get(i.getTrg().getUrl()).get(0) / somme);
        }

       /* for(Edge i : wIn.keySet()){
            System.out.println(i + " : " +wIn.get(i));
        }*/
    }
    /**
     * Function that put the weightOut of each nodes in a HashMap
     */
    public void wOut(){
        int somme = 0;
        for(Edge i : edges){
            somme = getListeES().get(i.getTrg().getUrl()).get(0);
            for(Edge j : edges){
                if(j.getSrc().getUrl().equals(i.getTrg().getUrl())){
                    somme += getListeES().get(j.getSrc().getUrl()).get(1);
                }
            }
            wOut.put(i, (double)getListeES().get(i.getTrg().getUrl()).get(1)/somme );
        }

        /*for(Edge i : wOut.keySet()){
            System.out.println(i + " : " +wOut.get(i));
        }*/
    }

    /**
     * Function that give you the weightIn between the two url you give in parameter
     * @param v source url
     * @param u goto url
     * @return double wIn
     */
    public double getWin(String v, String u){
        for(Edge i : edges){
            if(i.getSrc().getUrl().equals(v)){
                if(i.getTrg().getUrl().equals(u)){
                    return wIn.get(i);
                }
            }
        }
        return 1.0;
    }

    /**
     * Function that give you the weightOut between the two url you give in parameter
     * @param v source url
     * @param u go to url
     * @return double wOut
     */
    public double getWout(String v, String u){
        for(Edge i : edges){
            if(i.getSrc().getUrl().equals(v)){
                if(i.getTrg().getUrl().equals(u)){
                    return wOut.get(i);
                }
            }
        }
        return 1.0;
    }

    /**
     * Function that put in a HashMap the weighted PageRank of each Edge.
     */
    public void wpr() {
        for(Nodes i : nodes){
            ArrayList<Double> tmp = new ArrayList<>();
            //tmp.add((double)1/nodes.size());
            tmp.add((double)1);
            listePR.put(i.getUrl(), tmp);
        }

        double somme = 0;
        int size = 0;
        for(int k = 0; k < iteration; k++) {
            for (Nodes i : nodes) {
                size = listePR.get(i.getUrl()).size() - 1;
                for (Edge j : edges) {
                    if (j.getTrg().getUrl().equals(i.getUrl())) {
                        somme += listePR.get(j.getSrc().getUrl()).get(size) * getWin(j.getTrg().getUrl(),j.getSrc().getUrl()) * getWout(j.getTrg().getUrl(),j.getSrc().getUrl());
                    }
                }
                listePR.get(i.getUrl()).add(somme * d + (1 - d));
                somme = 0;
            }
            /*for (String i : listePR.keySet()) {
                System.out.println(i + " : " + listePR.get(i).toString());
            }*/
        }
    }

    /**
     * Function that return an ArrayList of String. This Arraylist contains each URL, sorted from the higher wPageRank
     * to the lowest.
     * @return ArrayList<String>
     */
    public ArrayList<String> sortPR(){
        HashMap<String, ArrayList<Double>> listePRClone = new HashMap<String, ArrayList<Double>>(listePR);
        ArrayList<String> sortedLinks = new ArrayList<>();
        String tmp = "";
        int size;
        while(listePRClone.size() != 0) {
            size = listePRClone.size();
            for (String i : listePRClone.keySet()) {
                int SIZE = listePRClone.get(i).size() - 1;
                if (tmp.equals("")) {
                    tmp = i;
                }
                if (listePRClone.get(tmp).get(SIZE) < listePRClone.get(i).get(SIZE)) {
                    tmp = i;
                }
            }
            sortedLinks.add(tmp);
            listePRClone.remove(tmp);
            tmp = "";
        }
        int cpt = 0;
        for(String i : sortedLinks){
            cpt++;
//            System.out.println(cpt + " : " + i + " - " + listePR.get(i).get(iteration));

        }
        generateSize(sortedLinks);
        return sortedLinks;
    }

    /**
     * Function that add a size to each edge. The bigger the wPageRank, the bigger the size.
     * @param a List of url sorted, bigger wPR first.
     */
    public void generateSize(ArrayList<String> a) {
        for(int i = 0, size = a.size(); i < size; i++){
            for(Nodes j : nodes){
                if(j.getUrl().equals(a.get(i))){
                    /*double tmp = (double)i * (tailleMax-tailleMin) /size;
                    j.setTaille(tailleMin + tmp);*/
                    j.setTaille(listePR.get(j.getUrl()).get(iteration) * 15);
                    break;
                }
            }
        }
        System.out.println(nodes.toString());
    }
    //GETTERS SETTERS

    public Map<String, ArrayList<Integer>> getListeES() {
        return listeES;
    }

}
