package client;

import java.rmi.RemoteException;
import java.util.Scanner;

public class ClientApp {


    private static Client client;
    //private static UI userInterface;


    public static void main(String[] args) {


        //--------------------------------- TUI o GUI ? ----------------------------------

        //------------------------------- RMI o Socket ? ----------------------------------


        System.out.println("What kind of connection do you want?");
        int select;
        Scanner scan = new Scanner(System.in);
        do{
            System.out.println("0 --> RMI \n1 --> Socket");
            select = scan.nextInt();
        }while(select != 0 && select != 1);

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

        try {
            client.startClient();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        //------------------------------ waiting room ----------------------------------

        //-------------------------------- game loop ----------------------------------

    }
}
