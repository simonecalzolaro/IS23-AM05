package client;


import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;

import myShelfieException.LoginException;

import view.GUIApplication;
import view.TUI;
import view.View;


import java.io.IOException;

import java.rmi.NotBoundException;

import java.util.Scanner;


public class ClientApp{


    private static Client client;
    //private static UI userInterface;
    private static View view;


    public static void main(String[] args) throws Exception {

        String nickName="";

        //--------------------------------- TUI o GUI ? ----------------------------------

        String select;
        Scanner scan = new Scanner(System.in);
        do{
            System.out.println("0 ---> TUI \n 1 ---> GUI?");

            select = scan.nextLine();

            if(!select.equals("0") && !select.equals("1")) System.out.println("ClientApp --- Invalid code --> Try again !");

        }while(!select.equals("0") && !select.equals("1"));

        switch (select){

            case "0":

                view = new TUI();
                try {
                    view.startGame();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (LoginException e) {
                    throw new RuntimeException(e);
                } catch (NotBoundException e) {
                    throw new RuntimeException(e);
                }
                break;

            case "1":
                new JFXPanel();
                Platform.runLater(()-> {
                    try {

                        GUIApplication guiApplication= new GUIApplication();
                        guiApplication.start(new Stage());

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

        }



        /*

        //------------------------------- RMI o Socket ? ----------------------------------

        System.out.println("What kind of connection do you want?");
        String select;
        Scanner scan = new Scanner(System.in);
        do{
            System.out.println("0 --> RMI \n1 --> Socket");
            select = scan.nextLine();

            if(!select.equals("0") && !select.equals("1")) System.out.println("ClientApp --- Invalid code --> Try again !");

        }while(!select.equals("0") && !select.equals("1"));

        //create the client
        switch (select){

            case "0":
                try{
                    client = new RMIClient();
                }catch (RemoteException e) {
                    System.out.println("ClientApp --- RemoteException occurred trying to initialize a new RMIClient");
                    e.printStackTrace();
                }
                break;

            case "1":
                try {
                    client= new SocketClient();
                } catch (RemoteException e) {
                    System.out.println("ClientApp --- RemoteException occurred trying to initialize a new SocketClient");
                    e.printStackTrace();
                }
                break;

        }


        try {
            client.initializeClient();
        } catch (RemoteException e) {
            System.out.println("ClientApp --- RemoteException occurred trying to initialize the client");
            e.printStackTrace();
        }catch (NotBoundException e){
            System.out.println("ClientApp --- NotBoundException occurred trying to initialize the RMIClient");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("ClientApp --- IOException occurred trying to initialize the SocketClient");
            System.out.println("---> Socket hasn't been created");
            e.printStackTrace();
        }

        boolean goOn = false;
        boolean ok = false;

        do{

            String num;
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));


            do {
                System.out.println("0 --> start a new game");
                System.out.println("1 --> continue a Game");
                num = scan.nextLine();

                if(!num.equals("0") &&  !num.equals("1")) System.out.println("ClientApp --- Invalid code --> Try again !");

            }while (!num.equals("0") &&  !num.equals("1"));

            switch (num) {

                //LOGIN
                case "0":

                    //insert nickname
                    do{
                        try{
                            System.out.println("nickname: ");
                            nickName = br.readLine();
                            ok = true;
                        }catch (IOException e){
                            System.out.println("ClientApp --- System.in exception --> Try again");
                            ok = false;
                        }
                    }while(!ok);


                    //login of the new player
                    try{
                        client.askLogin(nickName);
                        client.getModel().setNickname(nickName);
                        System.out.println("...successfully LOGIN...");
                    } catch (LoginException e) {
                        System.out.println("ClientApp --- LoginException occurred in askLogin()");
                        throw new RuntimeException();
                    } catch (IOException e) {
                        System.out.println("ClientApp --- IOException occurred in askLogin()");
                        throw new RuntimeException();
                    }

                    try {
                        client.askCheckFullWaitingRoom();
                    } catch (IOException e) {
                        System.out.println("ClientApp --- IOException occurred in askCheckFullWaitingRoom()");
                        throw new RuntimeException();
                    }


                    goOn = true;
                    break;

                //CONTINUE GAME
                case "1":
                    do{
                        try{
                            System.out.println("nickname: ");
                            nickName = br.readLine();
                            ok = true;
                        }catch (IOException e){
                            System.out.println("ClientApp --- System.in exception --> Try again");
                            ok = false;
                        }
                    }while(!ok);
                    //relogin of the new player
                    try {
                        client.askContinueGame();
                    } catch (LoginException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }


                    System.out.println("...reconnected successfully...");

                    goOn = true;
                    break;

                default:
                    goOn = false;
                    break;
            }
        }while(!goOn);

        //------------------------------ waiting room ----------------------------------


        //-------------------------------- game loop ----------------------------------

        while(!client.GameEnded()){
            /*
            try {
                sleep(10000);
                System.out.println("I'm "+nickName+" and I'm playing");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if(client.isMyTurn()){
                System.out.println("Choose a tiles: ");

            }
        }
         */


    }
}
