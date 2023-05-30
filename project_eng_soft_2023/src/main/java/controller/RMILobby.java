package controller;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedHashMap;
import java.util.Map;

public class RMILobby extends Lobby{


    String hostname;
    int PORT;


    /**
     * constructor for the ServerApp
     *
     * @throws RemoteException
     */
    public RMILobby() throws RemoteException {
        super();
    }


    @Override
    public void startServer() throws RemoteException, AlreadyBoundException {

        getServerSettings();

        Registry registry = null;
        ClientServerHandler stub = null;

        stub = (ClientServerHandler) UnicastRemoteObject.exportObject(this, PORT);
        registry = LocateRegistry.createRegistry(PORT);
        registry.bind("ServerAppService",stub);

        System.out.println("----RMIServer ready----");

    }


    public void getServerSettings() {

        Long PORT_pre;

        try{
            Object o = new JSONParser().parse(new FileReader("src/main/config/header.json"));
            JSONObject j =(JSONObject) o;
            Map arg = new LinkedHashMap();
            arg = (Map) j.get("serverSettings");

            hostname = (String) arg.get("hostname");
            PORT_pre = (Long) arg.get("RMIPORT");

            PORT = PORT_pre.intValue();

        } catch (FileNotFoundException e) {
            System.out.println("RMIClient --- FileNotFoundException occurred trying to retrieve server's information from header.json");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("RMIClient --- IOException occurred trying to retrieve server's information from header.json");
            e.printStackTrace();
        } catch (ParseException e) {
            System.out.println("RMIClient --- ParseException occurred trying to retrieve server's information from header.json");
            e.printStackTrace();
        }
    }

}
