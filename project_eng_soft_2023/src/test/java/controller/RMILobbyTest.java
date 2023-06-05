package controller;

import client.ClientHandler;
import client.RMIClient;
import myShelfieException.LoginException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class RMILobbyTest {

    static ServerApp serverApp;

    @BeforeAll
    static void startTest() throws AlreadyBoundException, RemoteException {

        serverApp=new ServerApp();
        serverApp.lobby.initializeServer();
        ServerApp.startRMIServer();

    }

    public Lobby newRMILobbyGiver(){

        serverApp.lobby.initializeServer();

        return serverApp.lobby;

    }

    @Test
    void login1Player() throws IOException, LoginException {

        Lobby l=newRMILobbyGiver();

        ClientHandler ch=new RMIClient();
        GameHandler gh= l.login("anna", ch );

        assertEquals(1, l.getWaitingRoom().size());
        assertTrue(l.getWaitingRoom().contains(gh));

    }

    @Test
    void login2Player2() throws IOException, LoginException {

        Lobby l=newRMILobbyGiver();

        ClientHandler ch1=new RMIClient();
        ClientHandler ch2=new RMIClient();


        GameHandler gh1= l.login("mirko", ch1 );
        GameHandler gh2= l.login("mario", ch2 );

        assertTrue(l.getWaitingRoom().size() == 2);
        assertTrue(l.getWaitingRoom().contains(gh1));
        assertTrue(l.getWaitingRoom().contains(gh2));

    }

    @Test
    void loginSameNick() throws IOException, LoginException {

        Lobby l=newRMILobbyGiver();

        ClientHandler ch1=new RMIClient();
        ClientHandler ch2=new RMIClient();


        final GameHandler gh1= l.login("mirko", ch1 );
        GameHandler gh2;
        assertThrows(LoginException.class , ()-> { l.login("mirko", ch2 );});

        assertEquals(1, l.getWaitingRoom().size());
        assertTrue(l.getWaitingRoom().contains(gh1));

    }


    @Test
    void continueGame() {



    }

    @Test
    void leaveGame() {



    }

    @Test
    void setNumberOfPlayersNeg() throws IOException, LoginException {

        Lobby l=newRMILobbyGiver();

        ClientHandler ch1=new RMIClient();
        ClientHandler ch2=new RMIClient();


        GameHandler gh1= l.login("mirko", ch1 );
        GameHandler gh2= l.login("mario", ch2 );


        assertTrue(l.getWaitingRoom().contains(gh1));
        assertTrue(l.getWaitingRoom().contains(gh2));

        assertThrows(IllegalArgumentException.class , ()-> { l.setNumberOfPlayers(-10, "mirko");});

    }

    @Test
    void setNumberOfPlayersBig() throws IOException, LoginException {

        Lobby l=newRMILobbyGiver();

        ClientHandler ch1=new RMIClient();
        ClientHandler ch2=new RMIClient();


        GameHandler gh1= l.login("mirko", ch1 );
        GameHandler gh2= l.login("mario", ch2 );


        assertTrue(l.getWaitingRoom().contains(gh1));
        assertTrue(l.getWaitingRoom().contains(gh2));

        assertThrows(IllegalArgumentException.class , ()-> { l.setNumberOfPlayers(4, "cesare");});


    }

    @Test
    void simulateLoginAndGameCreation() throws IOException, LoginException, InterruptedException {

        Lobby l=newRMILobbyGiver();

        ClientHandler ch1=new RMIClient();
        ClientHandler ch2=new RMIClient();


        GameHandler gh1= l.login("mirko", ch1 );
        GameHandler gh2= l.login("mario", ch2 );

        l.setNumberOfPlayers(2, "mirko");

        Thread.sleep(3000);

        assertEquals(1, l.getGames().size());


    }


    @Test
    void getAttendedPlayersAndWaitingRoom() throws IOException, LoginException {


        Lobby l=newRMILobbyGiver();

        assertEquals(0, l.getWaitingRoom().size());
        assertEquals(-1, l.getAttendedPlayers());

        ClientHandler ch1=new RMIClient();
        ClientHandler ch2=new RMIClient();
        ClientHandler ch3=new RMIClient();
        ClientHandler ch4=new RMIClient();
        ClientHandler ch5=new RMIClient();


        GameHandler gh1= l.login("mirko", ch1 );
        GameHandler gh2= l.login("mario", ch2 );
        GameHandler gh3= l.login("maria", ch3 );
        GameHandler gh4= l.login("dario", ch4 );
        GameHandler gh5= l.login("marco", ch5 );

        assertEquals(-2, l.getAttendedPlayers());
        assertEquals(5, l.getWaitingRoom().size());

    }

    @Test
    void getClients() {
    }

    @Test
    void removeFromWaitingRoom() {
    }

    @Test
    void quitGameIDandNotify() {
    }
}