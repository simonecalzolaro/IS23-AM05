package client;

import myShelfieException.LoginException;
import view.GUI;
import view.GUIApplication;
import view.TUI;
import view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import java.util.Scanner;

/**
 * Client-side main class
 */
public class ClientApp {


    private static Client client;
    //private static UI userInterface;
    private static View view;


    public static void main(String[] args) throws NotBoundException, LoginException, IOException {

        System.out.println(System.getProperty("user.dir"));
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
                view.startGame();
                break;

            case "1":

                GUIApplication.main(args);
                break;

        }
    }
}
