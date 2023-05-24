package client;

import myShelfieException.*;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Map;
import model.Tile;

public class AsyncClientInput implements Runnable{


    SocketClient socketClient;

    private Stream outClient;
    private Stream inClient;

    public AsyncClientInput(SocketClient socketClient){
        this.socketClient = socketClient;
        setStreams();
    }

    private void setStreams(){
        outClient = socketClient.getOutputStream();
        inClient = socketClient.getInputStream();
    }


    private void startServer(){

        System.out.println("--- AsyncClientInput ready ---");

        JSONObject request = new JSONObject();
        JSONObject response = new JSONObject();

        while (true){

            if(!request.equals(null)) request.clear();
            if(!response.equals(null)) response.clear();

            try{
                System.out.println("AsyncClientInput --- Waiting request from the server");
                request = inClient.read();
                System.out.println("AsyncClientInput  --- New request received from the server");
            } catch (InvalidOperationException e) {
                System.out.println("AsyncClientInput  --- InvalidOperationException occurred trying to read a new request");
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("AsyncClientInput  --- IOException occurred trying to read a new request");
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                System.out.println("AsyncClientInput  --- ClassNotFoundException occurred trying to read a new request");
                e.printStackTrace();
            }

            String Action = (String) request.get("Action");



            //Action: Method or Feedback
            switch (Action){


                //METHOD ------------------------------------------------------------------------------

                case "Method":

                    String Method = (String) request.get("Method");

                    switch (Method){

                        case "askNumberOfPlayers":

                            try {
                                TCPaskNumberOfPlayers();
                                response.put("Action","Feedback");
                                //response.put("Feedback",num);

                                try {
                                    outClient.reset();
                                    outClient.write(response);
                                } catch (InvalidOperationException e) {
                                    System.out.println("AsyncClientInput --- InvalidOperationException occurred trying to flush the number of players");
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    System.out.println("AsyncClientInput --- IOException occurred trying to flush the number of players");
                                    e.printStackTrace();
                                }


                            } catch (RemoteException e) {
                                System.out.println("AsyncClientInput --- RemoteException occurred trying to call enterNumberOfPlayers()");
                                response.put("Action","Feedback");
                                response.put("Feedback","RemoteException");

                                try {
                                    outClient.reset();
                                    outClient.write(response);
                                } catch (InvalidOperationException e1) {
                                    System.out.println("AsyncClientInput --- InvalidOperationException occurred trying to flush the feedback");
                                    e.printStackTrace();
                                } catch (IOException e2) {
                                    System.out.println("AsyncClientInput --- IOException occurred trying to flush the number of players");
                                    e.printStackTrace();
                                }
                            }

                            break;

                        case "notifyStartPlaying":

                            try{
                                TCPnotifyStartPlaying(request);

                                response.put("Action","Feedback");
                                response.put("Feedback","OKnotifyStartPlaying");

                                try {
                                    outClient.reset();
                                    outClient.write(response);
                                } catch (InvalidOperationException e) {
                                    System.out.println("AsyncClientInput --- InvalidOperationException occurred trying to flush the number of players");
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    System.out.println("AsyncClientInput --- IOException occurred trying to flush the number of players");
                                    e.printStackTrace();
                                }

                            } catch (RemoteException e) {
                                System.out.println("AsyncClientInput --- RemoteException occurred trying to call notifyStartPlaying()");
                                response.put("Action","Feedback");
                                response.put("Feedback","RemoteException");

                                try {
                                    outClient.reset();
                                    outClient.write(response);
                                } catch (InvalidOperationException e1) {
                                    System.out.println("AsyncClientInput --- InvalidOperationException occurred trying to flush the feedback");
                                    e.printStackTrace();
                                } catch (IOException e2) {
                                    System.out.println("AsyncClientInput --- IOException occurred trying to flush the number of players");
                                    e.printStackTrace();
                                }

                            }

                            break;

                        case "notifyUpdateBoard":

                            try {
                                TCPnotifyUpdateBoard(request);

                                response.put("Action","Feedback");
                                response.put("Feedback","OKnotifyUpdateBoard");

                                try {
                                    outClient.reset();
                                    outClient.write(response);
                                } catch (InvalidOperationException e) {
                                    System.out.println("AsyncClientInput --- InvalidOperationException occurred trying to flush the number of players");
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    System.out.println("AsyncClientInput --- IOException occurred trying to flush the number of players");
                                    e.printStackTrace();
                                }
                            } catch (RemoteException e) {
                                System.out.println("AsyncClientInput --- RemoteException occurred trying to call notifyUpdateBoard()");
                                response.put("Action","Feedback");
                                response.put("Feedback","RemoteException");

                                try {
                                    outClient.reset();
                                    outClient.write(response);
                                } catch (InvalidOperationException e1) {
                                    System.out.println("AsyncClientInput --- InvalidOperationException occurred trying to flush the feedback");
                                    e.printStackTrace();
                                } catch (IOException e2) {
                                    System.out.println("AsyncClientInput --- IOException occurred trying to flush the number of players");
                                    e.printStackTrace();
                                }

                            }

                            break;


                        case "notifyStartYourTurn":

                            try{
                                TCPStartYourTurn();

                                response.put("Action","Feedback");
                                response.put("Feedback","OKnotifyStartYourTurn");

                                try {
                                    outClient.reset();
                                    outClient.write(response);
                                } catch (InvalidOperationException e) {
                                    System.out.println("AsyncClientInput --- InvalidOperationException occurred trying to flush the number of players");
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    System.out.println("AsyncClientInput --- IOException occurred trying to flush the number of players");
                                    e.printStackTrace();
                                }

                            } catch (RemoteException e) {
                                System.out.println("AsyncClientInput --- RemoteException occurred trying to call notifyUpdateBoard()");
                                response.put("Action","Feedback");
                                response.put("Feedback","RemoteException");

                                try {
                                    outClient.reset();
                                    outClient.write(response);
                                } catch (InvalidOperationException e1) {
                                    System.out.println("AsyncClientInput --- InvalidOperationException occurred trying to flush the feedback");
                                    e.printStackTrace();
                                } catch (IOException e2) {
                                    System.out.println("AsyncClientInput --- IOException occurred trying to flush the number of players");
                                    e.printStackTrace();
                                }
                            }

                            break;


                        case "nofifyEndYourTurn":

                            try{
                                TCPEndYourTurn();

                                response.put("Action","Feedback");
                                response.put("Feedback","OKnotifyEndYourTurn");

                                try {
                                    outClient.reset();
                                    outClient.write(response);
                                } catch (InvalidOperationException e) {
                                    System.out.println("AsyncClientInput --- InvalidOperationException occurred trying to flush the number of players");
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    System.out.println("AsyncClientInput --- IOException occurred trying to flush the number of players");
                                    e.printStackTrace();
                                }

                            } catch (RemoteException e) {
                                System.out.println("AsyncClientInput --- RemoteException occurred trying to call notifyUpdateBoard()");
                                response.put("Action","Feedback");
                                response.put("Feedback","RemoteException");

                                try {
                                    outClient.reset();
                                    outClient.write(response);
                                } catch (InvalidOperationException e1) {
                                    System.out.println("AsyncClientInput --- InvalidOperationException occurred trying to flush the feedback");
                                    e.printStackTrace();
                                } catch (IOException e2) {
                                    System.out.println("AsyncClientInput --- IOException occurred trying to flush the number of players");
                                    e.printStackTrace();
                                }
                            }

                            break;


                        case "notifyEndGame":

                            try{
                                TCPEndGame(request);

                                response.put("Action","Feedback");
                                response.put("Feedback","OKnotifyEndYourTurn");

                                try {
                                    outClient.reset();
                                    outClient.write(response);
                                } catch (InvalidOperationException e) {
                                    System.out.println("AsyncClientInput --- InvalidOperationException occurred trying to flush the number of players");
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    System.out.println("AsyncClientInput --- IOException occurred trying to flush the number of players");
                                    e.printStackTrace();
                                }

                            } catch (RemoteException e) {
                                System.out.println("AsyncClientInput --- RemoteException occurred trying to call notifyUpdateBoard()");
                                response.put("Action","Feedback");
                                response.put("Feedback","RemoteException");

                                try {
                                    outClient.reset();
                                    outClient.write(response);
                                } catch (InvalidOperationException e1) {
                                    System.out.println("AsyncClientInput --- InvalidOperationException occurred trying to flush the feedback");
                                    e.printStackTrace();
                                } catch (IOException e2) {
                                    System.out.println("AsyncClientInput --- IOException occurred trying to flush the number of players");
                                    e.printStackTrace();
                                }
                            }

                            break;


                    }

                    break;





                 //FEEDBACK ------------------------------------------------------------------------------------

                case "Feedback":

                    String Feedback = (String) request.get("Feedback");

                    switch (Feedback){

                        case "OKLogin":
                            socketClient.greenLight();
                            break;

                        case "OKcheckFullWaitingRoom":

                            socketClient.greenLight();
                            break;

                        case "OKleaveGame":

                            boolean left = (boolean) request.get("Param1");

                            if(left) System.out.println("AsyncClientInput --- Failed attempt to leave the game");
                            else System.out.println("AsyncClientInput --- Game left successfully !");

                            socketClient.left = left;

                            socketClient.greenLight();

                            break;


                        case "OKchooseBoardTiles":

                            socketClient.greenLight();

                            break;

                        case "OKinsertShelfTiles" :

                            socketClient.greenLight();
                            break;

                        case "OKgetMyScore" :
                            socketClient.myScore = (int) request.get("Param1");
                            socketClient.greenLight();
                            break;

                        case "IOException":

                            System.out.println("AsyncClientInput --- IOException thrown by the server");

                            IOException e = (IOException) request.get("Param1");

                            e.printStackTrace();

                            break;

                        case "LoginException":

                            System.out.println("AsyncClientInput --- LoginException thrown by the server");

                            LoginException e1 = (LoginException) request.get("Param1");

                            e1.printStackTrace();

                            break;

                        case "NotMyTurnException":

                            System.out.println("AsyncClientInput --- NotMyTurnException thrown by the server");

                            NotMyTurnException e2 = (NotMyTurnException) request.get("Param1");

                            e2.printStackTrace();


                            break;

                        case "InvalidParametersException":

                            System.out.println("AsyncClientInput --- InvalidParametersException thrown by the server");

                            InvalidParametersException e3 = (InvalidParametersException) request.get("Param1");

                            e3.printStackTrace();


                            break;

                        case "NotConnectedException":

                            System.out.println("AsyncClientInput --- NotConnectedException thrown by the server");

                            NotConnectedException e4 = (NotConnectedException) request.get("Param1");

                            e4.printStackTrace();


                            break;



                        case "InvalidChoiceException":

                            System.out.println("AsyncClientInput --- InvalidChoiceException thrown by the server");

                            InvalidChoiceException e5 = (InvalidChoiceException) request.get("Param1");

                            e5.printStackTrace();


                            break;


                        case "InvalidLenghtException":

                            System.out.println("AsyncClientInput --- InvalidLenghtException thrown by the server");

                            InvalidLenghtException e6 = (InvalidLenghtException) request.get("Param1");

                            e6.printStackTrace();


                            break;



                    }


                    break;

            }

        }

    }


    public void TCPaskNumberOfPlayers() throws RemoteException {

         socketClient.enterNumberOfPlayers();

    }

    public void TCPnotifyStartPlaying(JSONObject json) throws RemoteException {

        int pgcNum = (int) json.get("Param1");
        Map<Tile,Integer[]> map = (Map<Tile, Integer[]>) json.get("Param2");
        int cgcNum1 = (int) json.get("Param3");
        int cgcNum2 = (int) json.get("Param4");

        //socketClient.startPlaying(pgcNum,map,cgcNum1,cgcNum2, 10); //---SimoSocket
    }

    public void TCPnotifyUpdateBoard(JSONObject json) throws RemoteException {

        Tile[][] board = (Tile[][]) json.get("Param1");
        Tile[][] bookshelf = (Tile[][]) json.get("Param2");
        Map<String,Tile[][]> map = (Map) json.get("Param3");

        socketClient.updateBoard(board,bookshelf,map, 10); //---SimoSocket il 10 è messo a caso

    }

    public void TCPStartYourTurn() throws RemoteException {
        socketClient.startYourTurn();
    }

    public void TCPEndYourTurn() throws RemoteException {
        socketClient.endYourTurn();
    }


    public void TCPEndGame(JSONObject json) throws RemoteException {

        Map<Integer,String> result = (Map<Integer, String>) json.get("Param1");

        socketClient.theGameEnd(result);
    }



    @Override
    public void run() {
        startServer();
    }
}
