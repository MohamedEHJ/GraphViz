package fr.univavignon.ceri.model;

import javax.management.ListenerNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PageRank {
    private Map<String, ArrayList<Integer>> listeES;
    private Map<String, ArrayList<Float>> listeW;
    private Map<String, ArrayList<Float>> listePR;
    public int iteration = 1;

    public PageRank() {
        setListeES(new HashMap<String, ArrayList<Integer>>());
        setListeW(new HashMap<String, ArrayList<Float>>());
        setListePR(new HashMap<String, ArrayList<Float>>());
    }
    public void fillListeES(ArrayList<Edge> a, ArrayList<Nodes> b) {
        for(Nodes nod : b ){
            ArrayList tmp = new ArrayList<Integer>();
            tmp.add(0);
            tmp.add(0);
            getListeES().put(nod.getUrl(), tmp);
        }

        for(Edge edg : a){
            for(String i : getListeES().keySet()){
                if(i.equals(edg.getSrc().getUrl())){
                    getListeES().get(i).set(0,getListeES().get(i).get(0) + 1);
                }
                if(i.equals(edg.getTrg().getUrl())){
                    getListeES().get(i).set(1,getListeES().get(i).get(1) + 1);
                }
            }
        }
        System.out.println(getListeES().toString());
        w(b,a);
        pr(a,b);
    }

    public void w(ArrayList<Nodes> c, ArrayList<Edge> a) {
        for(Nodes j : c){
            ArrayList tmp = new ArrayList<Float>();
            tmp.add(0.0F);
            tmp.add(0.0F);
            getListeW().put(j.getUrl(), tmp);
        }
        int somme = 0;
        String tmp = "";
        for(Edge i : a){
            if(tmp.equals("")){
                tmp = i.getSrc().getUrl();
            }
            if(!i.getSrc().getUrl().equals(tmp)){
                getListeW().get(tmp).set(0, (float)getListeES().get(tmp).get(0)/(float) somme);
                somme = 0;
            }
            somme += getListeES().get(tmp).get(1);
            tmp = i.getSrc().getUrl();
        }
        somme = 0;
        for(Nodes i : c){
            for(Edge j : a) {
                if(j.getTrg().getUrl().equals(i.getUrl())){
                    somme += getListeES().get(j.getSrc().getUrl()).get(1); //nb sorties du source
                }
            }
            getListeW().get(i.getUrl()).set(1, (float)getListeES().get(i.getUrl()).get(1)/(float)somme);
            somme = 0;
        }

        System.out.println(getListeW().toString());
    }

    public void pr(ArrayList<Edge> a, ArrayList<Nodes> b){
        for(Nodes i : b){
            ArrayList tmp = new ArrayList<Float>();
            tmp.add(1.0F/(float)b.size());
            //tmp.add(1.0F);
            getListePR().put(i.getUrl(), tmp);
        }

        float sum = 0.0F;
        for(int cpt = 0; cpt < iteration; cpt++) {
            for (Nodes i : b) {
                for (Edge j : a) {
                    if (j.getTrg().getUrl().equals(i.getUrl())) {
                        int size = getListePR().get(j.getTrg().getUrl()).size();
                        //PR(v)*Win*Wout
                        sum += getListePR().get(j.getSrc().getUrl()).get(size - 1) * getListeW().get(j.getSrc().getUrl()).get(0) * getListeW().get(j.getSrc().getUrl()).get(1);

                    }
                }
                float d = 0.85F;
                sum = sum * d + (1.0F - d);
                getListePR().get(i.getUrl()).add(sum);
                sum = 0.0F;
            }
        }
        //System.out.println(getListePR().toString());
    }

    public ArrayList<String> sortPR(){
        HashMap<String, ArrayList<Float>> listePRClone = new HashMap<String, ArrayList<Float>>(getListePR());
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
            System.out.println(cpt + " : " + i + " - " + getListePR().get(i).get(iteration));
        }
        return sortedLinks;
    }
    //GETTERS SETTERS

    public Map<String, ArrayList<Integer>> getListeES() {
        return listeES;
    }

    public void setListeES(Map<String, ArrayList<Integer>> listeES) {
        this.listeES = listeES;
    }

    public Map<String, ArrayList<Float>> getListeW() {
        return listeW;
    }

    public void setListeW(Map<String, ArrayList<Float>> listeW) {
        this.listeW = listeW;
    }

    public Map<String, ArrayList<Float>> getListePR() {
        return listePR;
    }

    public void setListePR(Map<String, ArrayList<Float>> listePR) {
        this.listePR = listePR;
    }
}
