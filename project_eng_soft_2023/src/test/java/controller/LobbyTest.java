package controller;

import client.Client;
import client.RMIClient;
import myShelfieException.LoginException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.rmi.AlreadyBoundException;


class LobbyTest {

    @Test
    void login_5Players() throws LoginException, IOException, AlreadyBoundException {

        Lobby lobby;
        Client c1;
        Client c2;
        Client c3;
        Client c4;
        Client c5;

        lobby=new RMILobby();
        lobby.startServer();

      /*  c1=new RMIClient();
        c2=new RMIClient();
        c3=new RMIClient();
        c4=new RMIClient();
        c5=new RMIClient();


        lobby.login("mario", c1);
        lobby.login("elio", c2);
*/
    }

    @Test
    void login_twoPlayer() throws LoginException, IOException {

        /*
        Lobby lobby;
        ClientApp client;

        lobby=new Lobby();
        client=new ClientApp();
        GameHandler gh, gh1;

        gh=lobby.login("Mario", client, true );

        assertTrue(lobby.getGames().contains( gh ) );

        assertEquals(lobby.getGames().size(), 1);

        gh1=lobby.login("Dario", client, true );

        assertTrue(lobby.getGames().contains( gh1 ) );

        assertEquals(lobby.getGames().size(), 2);

         */


    }


    @Test
    void continueGame() {
    }

    @Test
    void leaveGame() {
    }

    @Test
    void askNumberOfPlayers() {
    }

    @Test
    void notifyStartPlaying() {
    }
}