package model;

import java.util.ArrayList;

public class Coda {
    ArrayList<Coordinate> queue = new ArrayList<Coordinate>();

    public void enqueue(Coordinate o){
        queue.add(o);
    }

    public void dequeue(){
        if(queue.size() > 0){
            queue.remove(0);
        }
    }

    public int size(){
        return queue.size();
    }

    public Coordinate head(){
        if(queue.size() > 0){
            return queue.get(0);
        }else return null;
    }

    public Coordinate tail(){
        if(queue.size() > 0){
            return queue.get(queue.size()-1);
        }else return null;
    }

    public ArrayList<Coordinate> getCoda(){
        return queue;
    }




}