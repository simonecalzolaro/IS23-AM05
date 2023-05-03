package controller;

import client.Client;
import myShelfieException.LoginException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class LobbyTest {
/*
    @Test
    void login_onePlayer() throws LoginException, IOException {

        Lobby lobby;
        ClientApp client;

        lobby=new Lobby();
        client=new ClientApp();
        GameHandler gh;

        gh=lobby.login("Mario", client, true );

        assertTrue(lobby.getGames().contains( gh ) );

        assertEquals(lobby.getGames().size(), 1);

    }

    @Test
    void login_twoPlayer() throws LoginException, IOException {

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

    }
*/

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