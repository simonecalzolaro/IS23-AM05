package controller;


import client.ClientHandler;
import model.Tile;
import myShelfieException.*;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class SocketLobby extends Lobby implements Runnable{

    Socket socketServer;
    static int count;

    Stream outServer;
    Stream inServer;

    GameHandler cp;

    static boolean isPaused;

    /**
     * constructor for the ServerApp
     *
     * @throws RemoteException
     */
    protected SocketLobby(Socket socket) throws IOException {

        this.socketServer = socket;
        System.out.println("----SocketServer ready----");

        try{
            outServer = new Stream(socketServer,0);
        } catch (InvalidParametersException e) {
            System.out.println("SocketClient --- InvalidParameterException occurred trying to create the output stream");
            System.out.println("---> Change it with a valid one");
            throw new RuntimeException();
        }

        try{
            inServer = new Stream(socketServer,1);
        } catch (InvalidParametersException e) {
            System.out.println("SocketClient --- InvalidParameterException occurred trying to create the input stream");
            System.out.println("---> Change it with a valid one");
            throw new RuntimeException();
        }
    }

    @Override
    public synchronized void startServer() throws RemoteException {
         isPaused = false;

        JSONObject request = new JSONObject();
        JSONObject response = new JSONObject();

        while(true){

            if(!request.equals(null)) request.clear();
            if(!response.equals(null)) response.clear();

            try{
                System.out.println("SocketLobby --- Waiting request from the client");
                request = inServer.read();
                System.out.println("SocketLobby --- New request received from the client");
            } catch (InvalidOperationException e) {
                System.out.println("SocketLobby --- InvalidOperationException occurred trying to read a new request");
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("SocketLobby --- IOException occurred trying to read a new request");
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                System.out.println("SocketLobby --- ClassNotFoundException occurred trying to read a new request");
                e.printStackTrace();
            }

            String Action = (String) request.get("Action");

            //Let's see if it is a feedback or a method
            switch (Action){

                case "Method":

                    String Method = (String) request.get("Method");



                    //METHOD -----------------------------------------------------------------------------------------
                    switch (Method){

                        case "login":

                            try{
                                TCPLogin(request);
                                response.put("Action","Feedback");
                                response.put("Feedback","OKLogin");

                                try {
                                    outServer.reset();
                                    outServer.write(response);
                                } catch (InvalidOperationException e) {
                                    System.out.println("SocketLobby --- InvalidOperationException occurred trying yo flush the feedback to the client");
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    System.out.println("SocketLobby --- IOException occurred trying to flush the feedback to the client");
                                    e.printStackTrace();
                                }

                            } catch (LoginException e) {

                                response.put("Action","Feedback");
                                response.put("Feedback","LoginException");
                                response.put("Param1",e);

                                try {
                                    outServer.reset();
                                    outServer.write(response);
                                } catch (InvalidOperationException e1) {
                                    System.out.println("SocketLobby --- InvalidOperationException occurred trying yo flush the feedback to the client");
                                    e.printStackTrace();
                                } catch (IOException e1) {
                                    System.out.println("SocketLobby --- IOException occurred trying to flush the feedback to the client");
                                    e.printStackTrace();
                                }

                            } catch (IOException e) {
                                response.put("Action","Feedback");
                                response.put("Feedback","IOException");
                                response.put("Param1",e);

                                try {
                                    outServer.reset();
                                    outServer.write(response);
                                } catch (InvalidOperationException e1) {
                                    System.out.println("SocketLobby --- InvalidOperationException occurred trying yo flush the feedback to the client");
                                    e.printStackTrace();
                                } catch (IOException e1) {
                                    System.out.println("SocketLobby --- IOException occurred trying to flush the feedback to the client");
                                    e.printStackTrace();
                                }
                            }

                            break;


                        case "checkFullWaitingRoom":

                            try{
                                TCPcheckFullWaitingRoom();
                                response.put("Action","Feedback");
                                response.put("Feedback","OKcheckFullWaitingRoom");

                                try{
                                    outServer.reset();
                                    outServer.write(response);
                                } catch (InvalidOperationException e) {
                                    System.out.println("SocketLobby --- InvalidOperationException occurred trying yo flush the feedback to the client");
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    System.out.println("SocketLobby --- IOException occurred trying to flush the feedback to the client");
                                    e.printStackTrace();
                                }


                            } catch (IOException e) {
                                System.out.println("SocketLobby --- IOException occurred trying to flush the feedback to the client");
                                response.put("Action","Feedback");
                                response.put("Feedback","IOException");
                                response.put("Param1",e);

                                try {
                                    outServer.reset();
                                    outServer.write(response);
                                } catch (InvalidOperationException e1) {
                                    System.out.println("SocketLobby --- InvalidOperationException occurred trying yo flush the feedback to the client");
                                    e.printStackTrace();
                                } catch (IOException e1) {
                                    System.out.println("SocketLobby --- IOException occurred trying to flush the feedback to the client");
                                    e.printStackTrace();
                                }

                            }

                            //STOPPO LO STREAM PER EVITARE DI AVERE UN ACCESSO CONCORRENTE ALLO STESSO STREAM INPUT DA PARTE
                            // DI SOCKETLOBBY E SOCKETCONTROLPLAYER
                            if (tempPlayers.size() < attendedPlayers) {
                                redLight();
                            }else{
                                greenLight();
                            }

                            while (isPaused){
                                try {
                                    wait();
                                } catch (InterruptedException e) {
                                    System.out.println("SocketLobby --- InterruptedException occurred trying to wait the process");
                                    throw new RuntimeException();
                                }
                            }


                            break;

                        case "leaveGame":

                            try{
                                boolean left = TCPLeaveGame(request);

                                response.put("Action","Feedback");
                                response.put("Feedback","OKleaveGame");
                                response.put("Param1",left);

                                try{
                                    outServer.reset();
                                    outServer.write(response);
                                } catch (InvalidOperationException e) {
                                    System.out.println("SocketLobby --- InvalidOperationException occurred trying yo flush the feedback to the client");
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    System.out.println("SocketLobby --- IOException occurred trying to flush the feedback to the client");
                                    e.printStackTrace();
                                }

                            } catch (LoginException e) {

                                response.put("Action","Feedback");
                                response.put("Feedback","LoginException");
                                response.put("Param1",e);

                                try {
                                    outServer.reset();
                                    outServer.write(response);
                                } catch (InvalidOperationException e1) {
                                    System.out.println("SocketLobby --- InvalidOperationException occurred trying yo flush the feedback to the client");
                                    e.printStackTrace();
                                } catch (IOException e1) {
                                    System.out.println("SocketLobby --- IOException occurred trying to flush the feedback to the client");
                                    e.printStackTrace();
                                }


                            }

                            break;

                        case "chooseBoardTiles":

                            try{
                                TCPchooseBoardTiles(request);

                                response.put("Action","Feedback");
                                response.put("Feedback","OKchooseBoardTiles");

                                try{
                                    outServer.reset();
                                    outServer.write(response);
                                } catch (InvalidOperationException e) {
                                    System.out.println("SocketLobby --- InvalidOperationException occurred trying yo flush the feedback to the client");
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    System.out.println("SocketLobby --- IOException occurred trying to flush the feedback to the client");
                                    e.printStackTrace();
                                }


                            } catch (InvalidChoiceException e) {

                                response.put("Action","Feedback");
                                response.put("Feedback","InvalidChoiceException");
                                response.put("Param1",e);

                                try {
                                    outServer.reset();
                                    outServer.write(response);
                                } catch (InvalidOperationException e1) {
                                    System.out.println("SocketLobby --- InvalidOperationException occurred trying yo flush the feedback to the client");
                                    e.printStackTrace();
                                } catch (IOException e1) {
                                    System.out.println("SocketLobby --- IOException occurred trying to flush the feedback to the client");
                                    e.printStackTrace();
                                }


                            } catch (NotConnectedException e) {

                                response.put("Action","Feedback");
                                response.put("Feedback","NotConnectedException");
                                response.put("Param1",e);

                                try {
                                    outServer.reset();
                                    outServer.write(response);
                                } catch (InvalidOperationException e1) {
                                    System.out.println("SocketLobby --- InvalidOperationException occurred trying yo flush the feedback to the client");
                                    e.printStackTrace();
                                } catch (IOException e1) {
                                    System.out.println("SocketLobby --- IOException occurred trying to flush the feedback to the client");
                                    e.printStackTrace();
                                }


                            } catch (InvalidParametersException e) {

                                response.put("Action","Feedback");
                                response.put("Feedback","InvalidParametersException");
                                response.put("Param1",e);

                                try {
                                    outServer.reset();
                                    outServer.write(response);
                                } catch (InvalidOperationException e1) {
                                    System.out.println("SocketLobby --- InvalidOperationException occurred trying yo flush the feedback to the client");
                                    e.printStackTrace();
                                } catch (IOException e1) {
                                    System.out.println("SocketLobby --- IOException occurred trying to flush the feedback to the client");
                                    e.printStackTrace();
                                }


                            } catch (NotMyTurnException e) {

                                response.put("Action","Feedback");
                                response.put("Feedback","NotMyTurnException");
                                response.put("Param1",e);

                                try {
                                    outServer.reset();
                                    outServer.write(response);
                                } catch (InvalidOperationException e1) {
                                    System.out.println("SocketLobby --- InvalidOperationException occurred trying yo flush the feedback to the client");
                                    e.printStackTrace();
                                } catch (IOException e1) {
                                    System.out.println("SocketLobby --- IOException occurred trying to flush the feedback to the client");
                                    e.printStackTrace();
                                }


                            }

                            break;


                        case "insertShelfTiles":

                            try{
                                TCPinsertShelfTiles(request);

                                response.put("Action","Feedback");
                                response.put("Feedback","OKinsertShelfTiles");

                                try{
                                    outServer.reset();
                                    outServer.write(response);
                                } catch (InvalidOperationException e) {
                                    System.out.println("SocketLobby --- InvalidOperationException occurred trying yo flush the feedback to the client");
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    System.out.println("SocketLobby --- IOException occurred trying to flush the feedback to the client");
                                    e.printStackTrace();
                                }



                            } catch (InvalidChoiceException e) {

                                response.put("Action","Feedback");
                                response.put("Feedback","InvalidChoiceException");
                                response.put("Param1",e);

                                try {
                                    outServer.reset();
                                    outServer.write(response);
                                } catch (InvalidOperationException e1) {
                                    System.out.println("SocketLobby --- InvalidOperationException occurred trying yo flush the feedback to the client");
                                    e.printStackTrace();
                                } catch (IOException e1) {
                                    System.out.println("SocketLobby --- IOException occurred trying to flush the feedback to the client");
                                    e.printStackTrace();
                                }


                            } catch (NotConnectedException e) {

                                response.put("Action","Feedback");
                                response.put("Feedback","NotConnectedException");
                                response.put("Param1",e);

                                try {
                                    outServer.reset();
                                    outServer.write(response);
                                } catch (InvalidOperationException e1) {
                                    System.out.println("SocketLobby --- InvalidOperationException occurred trying yo flush the feedback to the client");
                                    e.printStackTrace();
                                } catch (IOException e1) {
                                    System.out.println("SocketLobby --- IOException occurred trying to flush the feedback to the client");
                                    e.printStackTrace();
                                }


                            } catch (InvalidLenghtException e) {

                                response.put("Action","Feedback");
                                response.put("Feedback","InvalidLenghtException");
                                response.put("Param1",e);

                                try {
                                    outServer.reset();
                                    outServer.write(response);
                                } catch (InvalidOperationException e1) {
                                    System.out.println("SocketLobby --- InvalidOperationException occurred trying yo flush the feedback to the client");
                                    e.printStackTrace();
                                } catch (IOException e1) {
                                    System.out.println("SocketLobby --- IOException occurred trying to flush the feedback to the client");
                                    e.printStackTrace();
                                }


                            } catch (NotMyTurnException e) {

                                response.put("Action","Feedback");
                                response.put("Feedback","NotMyTurnException");
                                response.put("Param1",e);

                                try {
                                    outServer.reset();
                                    outServer.write(response);
                                } catch (InvalidOperationException e1) {
                                    System.out.println("SocketLobby --- InvalidOperationException occurred trying yo flush the feedback to the client");
                                    e.printStackTrace();
                                } catch (IOException e1) {
                                    System.out.println("SocketLobby --- IOException occurred trying to flush the feedback to the client");
                                    e.printStackTrace();
                                }

                            }

                            break;

                        case "getMyScore":

                            try {
                                int score = TCPgetMyScore();

                                response.put("Action","Feedback");
                                response.put("Feedback","OKgetMyScore");
                                response.put("Param1",score);

                                try{
                                    outServer.reset();
                                    outServer.write(response);
                                } catch (InvalidOperationException e) {
                                    System.out.println("SocketLobby --- InvalidOperationException occurred trying yo flush the feedback to the client");
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    System.out.println("SocketLobby --- IOException occurred trying to flush the feedback to the client");
                                    e.printStackTrace();
                                }


                            } catch (RemoteException e) {
                                response.put("Action","Feedback");
                                response.put("Feedback","RemoteException");
                                response.put("Param1",e);

                                try {
                                    outServer.reset();
                                    outServer.write(response);
                                } catch (InvalidOperationException e1) {
                                    System.out.println("SocketLobby --- InvalidOperationException occurred trying yo flush the feedback to the client");
                                    e.printStackTrace();
                                } catch (IOException e1) {
                                    System.out.println("SocketLobby --- IOException occurred trying to flush the feedback to the client");
                                    e.printStackTrace();
                                }
                            }


                    }

                    break;

                //FEEDBACK -------------------------------------------------------------------------------------------

                case "Feedback":

                    break;
            }
        }
    }

    //TCPLOGIN

    //synchronized static per evitare che più thread chiamino lo stesso metodo in contemporanea
    public void TCPLogin(JSONObject json) throws LoginException, IOException {

        String nick = (String) json.get("Param1");
        ArrayList<Stream> streams = new ArrayList<>();
        streams.add(outServer);
        streams.add(inServer);

        cp = login(nick,streams);


    }

    public void TCPcheckFullWaitingRoom() throws IOException {
        checkFullWaitingRoom();
    }

    public void TCPContinueGame(JSONObject json)  {


    }

    public boolean TCPLeaveGame(JSONObject json) throws LoginException, RemoteException {

        String nick = (String) json.get("Param1");
        return leaveGame(nick);

    }


    public boolean TCPchooseBoardTiles(JSONObject json) throws InvalidChoiceException, NotConnectedException, InvalidParametersException, RemoteException, NotMyTurnException {

        List<Tile> chosenTiles = (List<Tile>) json.get("Param1");
        List<Integer> coord = (List<Integer>) json.get("Param2");

        return cp.chooseBoardTiles(chosenTiles,coord);

    }


    public boolean TCPinsertShelfTiles(JSONObject json) throws InvalidChoiceException, NotConnectedException, InvalidLenghtException, RemoteException, NotMyTurnException {

        ArrayList<Tile> choosenTiles = (ArrayList<Tile>) json.get("Param1");
        int choosenColumns = (int) json.get("Param2");
        List<Integer> coord = (List<Integer>) json.get("Param3");

        return cp.insertShelfTiles(choosenTiles,choosenColumns,coord);


    }


    public int TCPgetMyScore() throws RemoteException {

        return cp.getMyScore();

    }


    public synchronized void redLight(){
        isPaused = true;
    }

    public synchronized void greenLight(){
        isPaused = false;
        notifyAll();
    }


    @Override
    public void run() {
        try {
            startServer();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }



}
