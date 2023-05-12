package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;

public class ClientApp {


    private static Client client;
    //private static UI userInterface;


    public static void main(String[] args) {

        String nickName="";

        //--------------------------------- TUI o GUI ? ----------------------------------

        //------------------------------- RMI o Socket ? ----------------------------------

        System.out.println("What kind of connection do you want?");
        int select;
        Scanner scan = new Scanner(System.in);
        do{
            System.out.println("0 --> RMI \n1 --> Socket");
            select = scan.nextInt();
        }while(select != 0 && select != 1);

        //create the client
        switch (select){

            case 0:
                try{
                    client = new RMIClient();
                }catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                break;

            case 1:
                try {
                    client= new SocketClient();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                break;

        }

        client.startClient();


        do {

            int num;
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));


            do {
                System.out.println("0 --> start a new game");
                System.out.println("1 --> continue a Game");
                num = scan.nextInt();

            }while (num!=0 &&  num!=1);

            try {

                switch (num) {

                    case 0:
                        System.out.println("nickname: ");
                        nickName = br.readLine();
                        //login of the new player
                        client.askLogin(nickName);
                        client.getModel().setNickname(nickName);
                        System.out.println("...successfully LOGIN...");
                        break;

                    case 1:
                        System.out.println("nickname: ");
                        nickName = br.readLine();
                        //relogin of the new player
                        client.askContinueGame();
                        System.out.println("...reconnected successfully...");
                        break;
                }

            } catch (Exception e) {

                System.out.println(e.getMessage());
                e.printStackTrace();
                System.out.println("try again...");
                nickName="";

            }

        }while(nickName.equals(""));

        //------------------------------ waiting room ----------------------------------


        //-------------------------------- game loop ----------------------------------

        while(!client.GameEnded()){

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if(client.isMyTurn()){
                System.out.println("Choose a tiles: ");

            }



        }




    }
}
