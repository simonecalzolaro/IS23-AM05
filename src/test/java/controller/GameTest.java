package controller;

import client.ClientHandler;
import client.RMIClient;
import model.Board;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    public void mapSorter() throws IOException {

        Game g=new Game();

        Map<String , Integer> mapToSort= new HashMap<>();
        mapToSort.put("rino", 1);
        mapToSort.put("tino", 10);
        mapToSort.put("aldo", 0);
        mapToSort.put("gio", 5);
        mapToSort.put("jack", 0);
        mapToSort.put("gio1", 6);
        mapToSort.put("lino", 9);

        Map<String , Integer> sortedMap=g.sortMapByValue(mapToSort);
        ArrayList<Integer> sortedValues= new ArrayList<>(sortedMap.values());

        ArrayList<Integer> numb= new ArrayList<>(mapToSort.values());
        Collections.sort(numb, Comparator.reverseOrder());

        for(int i=0; i< numb.size(); i++ ){

            assertEquals(sortedValues.get(i), numb.get(i) );


        }
    }


    @Test
    public void removePlayer() throws IOException {

        ArrayList<ControlPlayer> pls= new ArrayList<>();
        ControlPlayer mario=new RMIControlPlayer("mario", new RMIClient());
        pls.add(mario);
        pls.add(new RMIControlPlayer("marino", new RMIClient()));
        pls.add(new RMIControlPlayer("mariano", new RMIClient()));


        Game g=new Game(pls, new Board());

        assertEquals(g.getPlayers().size(), 3);

        g.removePlayer(mario);

        assertEquals(g.getPlayers().size(), 2);

    }


}