package view;

import client.Matrix;
import client.RMIClient;
import client.SocketClient;
import client.Tile;
import myShelfieException.*;

import java.io.IOException;
import java.io.PrintStream;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.*;

import static java.lang.Math.abs;

/**
 * it represents all the necessary methods to play with TUI
 */
public class TUI extends View {

    private boolean TurnTimer=false;
    private boolean gameEnded=false;
    boolean serverEndedGame=false;
    List<Integer> coord = new ArrayList<>();
    private final PrintStream out;
    private boolean insideChat=false;

    public TUI() {

        super();

        out = System.out;

    }

    Timer timer = new Timer();
    TimerTask task;
    //-------------------------------- @Override methods from View --------------------------------

    @Override
    public void getNumOfPlayer(){

        String input;
        int num;
        boolean goon=false;
        Scanner scan = new Scanner(System.in);
        ExecutorService executor = Executors.newSingleThreadExecutor();

        do {

            System.out.print("Enter the number of players: ");
            // This method reads the number provided using keyboard
            input = waitForInput(scan, executor);

            if(!Objects.equals(input, "")) {
                num = Integer.parseInt(input);
                if (num < 2 || num > 4) System.out.println("The number of players must be between 2 and 4");
            }else {
                num=0;
                goon=true;
                break;
            }

        } while (num < 2 || num > 4);

        if(!goon) {
            client.askSetNumberOfPlayers(num, client.getModel().getNickname());
        }else {
            try {
                client.askLeaveGame();
            }catch (LoginException e){
                out.println("LoginException occurred trying to leave the game (not getting num of players)!");
            }catch (IOException e){
                out.println("IOException occurred trying to leave the game (not getting num of players)!");
            }

            try {
                client.askLogin(client.getModel().getNickname());
            } catch (LoginException e) {
                out.println("LoginException occurred trying to log in (not getting num of players)!");
            } catch (IOException e) {
                out.println("IOException occurred trying to log in (not getting num of players)!");
            }
        }

        executor.shutdown();

    }

    @Override
    public void updateBoard() {
        out.println("This is the updated board:\n");
        plotBoard();
        out.println("This is your Bookshelf:\n");
        plotMatrixShelf(client.getModel().getMyBookshelf());

    }

    @Override
    public void endGame(Map<String, Integer> results) {
        TurnTimer = false;
        gameEnded=true;

        out.println("+---------------The game has ended!---------------+");

        out.println("               nickname     score       ");

        if(results.values().stream().toList().get(0)<=0){
            serverEndedGame=true;
        }

        for (Map.Entry<String, Integer> entry : results.entrySet()) {
            out.println("               "+ entry.getKey() + "      ->     " + abs(entry.getValue()));

        }
        out.println("+-------------------------------------------------+");

        if(!serverEndedGame) {
            out.println("Do you want to leave the game? y/n");
        }else{
            System.exit(0);
        }

    }

    @Override
    public void isYourTurn() {
        TurnTimer = false;

        if (client.isMyTurn()) {

            out.println("""      
                                    +-------------------------+
                                    |     IS YOUR TURN!!!     |
                                    +-------------------------+
                                                                         """);

            task = new TimerTask() {
                @Override
                public void run() {
                    System.out.println("Do a move! You are out of time!");
                    // Assegna 'true' alla variabile booleana quando il timer scade
                    // Esegui qui altre azioni desiderate
                    TurnTimer = true;
                }
            };

            int limitTime = 180;
            timer.schedule(task, limitTime*1000);
        }

    }

    @Override
    public void startGame() {

        boolean fileFound;



        //Verifico che ci sia un file da cui recuperare il tipo di connessione per poi creare il client
        //Se tale file non esiste procedo con la procedura standard di login


        //Devo vedere con Elena come le è più comodo eseguire questo controllo sulla GUI

        fileFound = checkForBackupFile();

        init();


        if (fileFound){

            backupLogin();

        }

        else{

            standardLogin();

        }

    }

    @Override
    public void standardLogin() {
        Scanner scan = new Scanner(System.in);
        String connection;

        out.println("What kind of connection do you want?\n");

        do {
            out.println("0 --> RMI\n1 --> Socket");
            connection = scan.next();


            if (connection.equals("0")) {
                try {

                    client.setView(this);
                    client = new RMIClient();


                } catch (RemoteException e) {
                    out.println("RemoteException occurred trying to initialize the client");
                    return;
                }catch (Exception e){
                    out.println("Exception occurred trying to initialize the client");
                    return;
                }
            }

            if (connection.equals("1")) {
                try {

                    client.setView(this);
                    client = new SocketClient();

                } catch (RemoteException e) {
                    out.println("RemoteException occurred trying to initialize the client");
                    return;
                }catch (Exception e){
                    out.println("Exception occurred trying to initialize the client");
                    return;
                }
            }
        } while (!connection.equals("0") && !connection.equals("1"));

        try {
            client.initializeClient();

        } catch (RemoteException e) {
            out.println("RemoteException occurred trying to initialize the client");

        } catch (NotBoundException e) {
            out.println("NotBoundException occurred trying to initialize the RMIClient");
        } catch (IOException e) {
            out.println("IOException occurred trying to initialize the SocketClient");
            out.println("---> Socket hasn't been created");
        }


        boolean goon = false;

        do {
            out.println("Choose your nickname:");
            String nickname = scan.next();
            try {
                client.getModel().setNickname(nickname);
                client.askLogin(nickname);

                goon = true;

            } catch (LoginException e) {
                out.println("LoginException occurred trying to log in!");

            } catch (IOException e) {
                out.println("IOException occurred trying to log in!");
            }
        } while (!goon);

        /*
        try {
            client.askCheckFullWaitingRoom();
        } catch (IOException e) {
            System.out.println("ClientApp --- IOException occurred in askCheckFullWaitingRoom()");
            throw new RuntimeException();
        }
         */

        //System.out.println(" Welcome "+ client.getModel().getNickname() +" you are now int the WAITING ROOM...");

        int conti=0;
        while (client.getModel().getPgcNum() == -1) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if(client.getModel().getPgcNum() == -1 && conti%20==0) System.out.println("...waiting for other players");
            conti++;
        }
    }

    @Override
    public void endYourTurn() {
        task.cancel();
        out.println("Your turn is over!\n");
    }

    @Override
    public void startPlay() {

        System.out.flush();
        System.out.println("+-----------------------------------+");
        System.out.println("|             let's go!!            |");
        System.out.println("|        The game has started !     |");
        System.out.println("|     is your turn? make a move =)  |");
        System.out.println("+-----------------------------------+");

        new Thread(this::commandListener).start();

    }

    @Override
    public void showException(String exception) {
        out.println(exception);
    }

    @Override
    public void plotNewMessage(String sender,String message) {
        StringBuilder strSender = new StringBuilder();
        ColorCLI color = null;

        Set<String> otherPlayers = client.getModel().getOtherPlayers().keySet();
        List<String> player = otherPlayers.stream().toList();

        if(sender.equals(player.get(0))){
            color = ColorCLI.GREEN;
        } else if (sender.equals(player.get(1))) {
            color = ColorCLI.BLUE;
        } else if (sender.equals(player.get(2))) {
            color = ColorCLI.PINK;
        }


        if(!client.isMyTurn() || insideChat) {
            strSender.append(color).append(sender).append(ColorCLI.RESET);
            out.println(strSender + ": " + message);
        }
    }

    @Override
    public void continueSession() {
        out.println("Your session can continue!");
    }

    //-------------------------------- other methods --------------------------------

    /**
     * represents every color to use in visual methods in TUI
     */
    private enum ColorCLI {
        RESET("\u001B[0m"),

        GREEN("\u001B[32m"),
        BLUE("\u001B[34m"),
        WHITE("\u001B[37m"),
        PINK("\u001B[35m"),
        YELLOW("\u001B[33m"),
        LIGHTBLUE("\u001B[36m"),

        BLACK_BACKGROUND("\u001B[40m"),
        GREEN_BACKGROUND("\u001B[42m"),
        BLUE_BACKGROUND("\u001B[44m"),
        WHITE_BACKGROUND("\u001B[47m"),
        PINK_BACKGROUND("\u001B[45m"),
        YELLOW_BACKGROUND("\u001B[43m"),
        LIGHTBLUE_BACKGROUND("\u001B[46m");

        private final String color;

        ColorCLI(String color) {
            this.color = color;
        }

        public String toString() {
            return color;
        }
    }

    private void init() {

        /*
        out.println("""
                ███      ███ ██    ██   ███████ ██   ██ ██████ ██     ██████ ██ ██████ \s
                ████    ████  ██  ██    ██      ██   ██ ██     ██     ██     ██ ██     \s
                ██ ██  ██ ██   ████     ███████ ███████ ██████ ██     ██████ ██ ██████ \s
                ██  ████  ██    ██           ██ ██   ██ ██     ██     ██     ██ ██     \s
                ██   ██   ██    ██      ███████ ██   ██ ██████ ██████ ██     ██ ██████ \s
                                                                                       \s
                                                                                       \s""");

         */
        out.println("""
                 
                 ███╗░░░███╗██╗░░░██╗░██████╗██╗░░██╗███████╗██╗░░░░░███████╗██╗███████╗
                 ████╗░████║╚██╗░██╔╝██╔════╝██║░░██║██╔════╝██║░░░░░██╔════╝██║██╔════╝
                 ██╔████╔██║░╚████╔╝░╚█████╗░███████║█████╗░░██║░░░░░█████╗░░██║█████╗░░
                 ██║╚██╔╝██║░░╚██╔╝░░░╚═══██╗██╔══██║██╔══╝░░██║░░░░░██╔══╝░░██║██╔══╝░░
                 ██║░╚═╝░██║░░░██║░░░██████╔╝██║░░██║███████╗███████╗██║░░░░░██║███████╗
                 ╚═╝░░░░░╚═╝░░░╚═╝░░░╚═════╝░╚═╝░░╚═╝╚══════╝╚══════╝╚═╝░░░░░╚═╝╚══════╝
                 
                 """);

        out.println("\nWelcome to My Shelfie Board Game!");
        out.println("\nCreators: Elena Caratti, Gabriele Clara Di Gioacchino, Mirko Calvi, Simone Calzolaro!");


    }

    private void initMenu(){
        out.println("+-----------------------------------------------------------+");
        out.println("|                          MENU:                            |");
        out.println("|                                                           |");
        out.println("|   /help: to show all the commands available               |");
        out.println("|   /pgc: to show your PersonalGoalCard                     |");
        out.println("|   /cgc: to show your CommonGoalCard                       |");
        out.println("|   /score: to get your score                               |");
        out.println("|   /others: to show other player bookshelf                 |");
        out.println("|   /myShelf: to show my bookshelf                          |");
        out.println("|   /board: to show the updated board                       |");
        out.println("|   /tiles: to choose and insert the tiles from the board   |");
        out.println("|   /chat: to write a message in the chat                   |");
        out.println("|   /showChat: to show the chat                             |");
        out.println("|   /q: to leave the game                                   |");
        out.println("|                                                           |");
        out.println("+-----------------------------------------------------------+");
    }

    private void plotBoard() {

        StringBuilder strBoard = new StringBuilder();
        int temp = 0;

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {

                if (row != temp) {
                    temp++;
                    strBoard.append("\n");
                }

                switch (client.getModel().getBoard().getTileByCoord(row, col)) {
                    case GREEN -> strBoard.append(ColorCLI.GREEN_BACKGROUND).append(row).append(";").append(col).append(" ").append(ColorCLI.RESET);
                    case BLUE -> strBoard.append(ColorCLI.BLUE_BACKGROUND).append(row).append(";").append(col).append(" ").append(ColorCLI.RESET);
                    case WHITE -> strBoard.append(ColorCLI.WHITE_BACKGROUND).append(row).append(";").append(col).append(" ").append(ColorCLI.RESET);
                    case PINK -> strBoard.append(ColorCLI.PINK_BACKGROUND).append(row).append(";").append(col).append(" ").append(ColorCLI.RESET);
                    case YELLOW -> strBoard.append(ColorCLI.YELLOW_BACKGROUND).append(row).append(";").append(col).append(" ").append(ColorCLI.RESET);
                    case LIGHTBLUE -> strBoard.append(ColorCLI.LIGHTBLUE_BACKGROUND).append(row).append(";").append(col).append(" ").append(ColorCLI.RESET);
                    case EMPTY, NOTAVAILABLE -> strBoard.append("    ").append(ColorCLI.RESET);
                }
            }
        }

        out.println(strBoard);

    }

    private void plotMatrixShelf(Matrix bookshelf) {

        StringBuilder strShelf = new StringBuilder();
        int temp = 5;

        for (int row = 5; row >= 0; row--) {
            for (int col = 0; col < 5; col++) {

                if (row != temp) {
                    temp--;
                    strShelf.append("\n");
                }

                switch (bookshelf.getTileByCoord(row, col)) {
                    case GREEN -> strShelf.append("|").append(ColorCLI.GREEN_BACKGROUND).append(row).append(";").append(col).append(ColorCLI.RESET);
                    case BLUE -> strShelf.append("|").append(ColorCLI.BLUE_BACKGROUND).append(row).append(";").append(col).append(ColorCLI.RESET);
                    case WHITE -> strShelf.append("|").append(ColorCLI.WHITE_BACKGROUND).append(row).append(";").append(col).append(ColorCLI.RESET);
                    case PINK -> strShelf.append("|").append(ColorCLI.PINK_BACKGROUND).append(row).append(";").append(col).append(ColorCLI.RESET);
                    case YELLOW -> strShelf.append("|").append(ColorCLI.YELLOW_BACKGROUND).append(row).append(";").append(col).append(ColorCLI.RESET);
                    case LIGHTBLUE -> strShelf.append("|").append(ColorCLI.LIGHTBLUE_BACKGROUND).append(row).append(";").append(col).append(ColorCLI.RESET);
                    case EMPTY, NOTAVAILABLE -> {
                        //System.out.println(row+","+col);
                        strShelf.append("|   ").append(ColorCLI.RESET);
                    }
                }

                if (col == 4) strShelf.append("|");
            }
        }

        out.println(strShelf);

    }

    private void plotOtherPlayersShelf() {

        for (Map.Entry<String, Matrix> entry : client.getModel().getOtherPlayers().entrySet()) {
            out.println(entry.getKey());
            out.println("\n");
            plotMatrixShelf(entry.getValue());
        }

    }

    private void plotPGC() {
        StringBuilder strPGC = new StringBuilder();

        int temp = 5;

        for (int row = 5; row >= 0; row--) {
            for (int col = 0; col < 5; col++) {

                if (row != temp) {
                    temp--;
                    strPGC.append("\n");
                }

                if (client.getModel().getPgc().getTileFromMap(Tile.GREEN)[0] == row && client.getModel().getPgc().getTileFromMap(Tile.GREEN)[1] == col) {
                    strPGC.append("|").append(ColorCLI.GREEN_BACKGROUND).append(row).append(";").append(col).append(ColorCLI.RESET);
                } else if (client.getModel().getPgc().getTileFromMap(Tile.BLUE)[0] == row && client.getModel().getPgc().getTileFromMap(Tile.BLUE)[1] == col) {
                    strPGC.append("|").append(ColorCLI.BLUE_BACKGROUND).append(row).append(";").append(col).append(ColorCLI.RESET);
                } else if (client.getModel().getPgc().getTileFromMap(Tile.WHITE)[0] == row && client.getModel().getPgc().getTileFromMap(Tile.WHITE)[1] == col) {
                    strPGC.append("|").append(ColorCLI.WHITE_BACKGROUND).append(row).append(";").append(col).append(ColorCLI.RESET);
                } else if (client.getModel().getPgc().getTileFromMap(Tile.PINK)[0] == row && client.getModel().getPgc().getTileFromMap(Tile.PINK)[1] == col) {
                    strPGC.append("|").append(ColorCLI.PINK_BACKGROUND).append(row).append(";").append(col).append(ColorCLI.RESET);
                } else if (client.getModel().getPgc().getTileFromMap(Tile.YELLOW)[0] == row && client.getModel().getPgc().getTileFromMap(Tile.YELLOW)[1] == col) {
                    strPGC.append("|").append(ColorCLI.YELLOW_BACKGROUND).append(row).append(";").append(col).append(ColorCLI.RESET);
                } else if (client.getModel().getPgc().getTileFromMap(Tile.LIGHTBLUE)[0] == row && client.getModel().getPgc().getTileFromMap(Tile.LIGHTBLUE)[1] == col) {
                    strPGC.append("|").append(ColorCLI.LIGHTBLUE_BACKGROUND).append(row).append(";").append(col).append(ColorCLI.RESET);
                } else {
                    strPGC.append("|   ").append(ColorCLI.RESET);
                }
                if (col == 4) strPGC.append("|");
            }
        }

        System.out.println(strPGC);

    }

    private void commandInsertTiles() {
        List<Integer> tile = new ArrayList<>();
        Scanner scan = new Scanner(System.in);
        boolean goon = false;
        boolean ok=false;

        do {

            int x,y;
            int round = 0;
            String select = "";
            out.println("Choose tiles from the board!");

            if(!client.isMyTurn()) return; //controllo ogni volta per disconnessioni

            while (!Objects.equals(select, "n")) {
                do {

                    out.println("Insert x:");

                    do {

                        if(!client.isMyTurn()) {
                            coord.clear();
                            return; //controllo ogni volta per disconnessioni
                        }

                        try {
                            x = Integer.parseInt(scan.next());
                            coord.add(x);
                            tile.add(x);
                            ok=true;
                        } catch (NumberFormatException e) {
                            out.println("NumberFormatException trying to choose the x tile!");
                            out.println("Insert x: ");
                            ok=false;
                        }
                    }while (!ok);

                    ok=false;
                    out.println("Insert y:");

                    do {

                        if(!client.isMyTurn()) {
                            coord.clear();
                            return; //controllo ogni volta per disconnessioni
                        }

                        try {
                            y = Integer.parseInt(scan.next());
                            coord.add(y);
                            tile.add(y);
                            ok=true;
                        } catch (NumberFormatException e) {
                            out.println("NumberFormatException trying to choose the y tile!");
                            out.println("Insert y: ");
                            ok=false;
                        }
                    }while (!ok);

                    round++;

                    if (checkTiles(coord)) {
                        out.println("There has been some error!\nChoose tiles from the board! (from the beginning)");
                        coord.clear();
                        round = 0;
                    }

                    if(!checkCatchable(tile)){
                        out.println("You cannot choose this tile! It has no free side!\nChoose tiles from the board! (from the beginning)");
                        coord.clear();
                        tile.clear();
                        ok=false;
                        round=0;
                    }

                    tile.clear();

                } while (checkTiles(coord) || !ok);

                if(getSpacesAvailableShelf()==(coord.size()/2)){
                    if( coord.size()/2 != 3 ) out.println("This is the max number of tiles you can get!");
                    break;
                }

                if (round == 3) break;

                do {
                    out.println("Would you like to continue? y/n");
                    select = scan.next();
                }while (!select.equals("y") && !select.equals("n"));

            }

            if(!client.isMyTurn()) {
                coord.clear();
                return; //controllo ogni volta per disconnessioni
            }

            try {
                client.askBoardTiles(coord);
                goon = true;

            } catch (InvalidChoiceException e) {
                out.println("InvalidChoiceException occurred trying to choose the tiles!");
                coord.clear();
            } catch (NotConnectedException e) {
                out.println("NotConnectedException occurred trying to choose the tiles!");
                coord.clear();
                return;
            } catch (NotMyTurnException e) {
                out.println("NotMyTurnException occurred trying to choose the tiles!");
                coord.clear();
                return;
            } catch (InvalidParametersException e) {
                out.println("InvalidParametersException occurred trying to choose the tiles!");
            } catch (IOException e) {
                out.println("IOException occurred trying to choose the tiles!");
                coord.clear();
                return;
            }

        } while (!goon);

        goon = false;
        ok=false;

        if(!client.isMyTurn()) {
            coord.clear();
            return; //controllo ogni volta per disconnessioni
        }

        do {
            out.println("Choose the column of your bookshelf for the tiles:");
            int chosenColumn=0;

            do {
                try {
                    chosenColumn = Integer.parseInt(scan.next());
                    ok=true;
                } catch (NumberFormatException e) {
                    out.println("NumberFormatException trying to choose the column of your bookshelf!");
                    out.println("Try again: ");
                }
                if(chosenColumn<0 || chosenColumn>4) {
                    ok=false;
                    out.println("You have chosen a column that doesn't exist!\nTry again: ");
                }else {

                    if (!checkColumn(chosenColumn)) {
                        ok = false;
                        out.println("You have chosen a full column OR you have chosen too many tiles for this column!\nTry again: ");
                    }
                }

            }while (!ok);

            if(!client.isMyTurn()) {
                coord.clear();
                return; //controllo ogni volta per disconnessioni
            }

            try {
                client.askInsertShelfTiles(chosenColumn, coord);
                goon = true;
                coord.clear();
            } catch (InvalidChoiceException e) {
                out.println("InvalidChoiceException occurred trying to insert tiles!");
            } catch (InvalidLenghtException e) {
                out.println("InvalidLengthException occurred trying to insert tiles!");
            } catch (NotMyTurnException e) {
                out.println("NotMyTurnException occurred trying to insert tiles!");
                coord.clear();
                return;
            } catch (NotConnectedException e) {
                out.println("NotConnectedException occurred trying to insert tiles!");
                coord.clear();
                return;
            } catch (IOException e) {
                out.println("IOException occurred trying to choose the tiles!");
                coord.clear();
                return;
            }

        } while (!goon);

    }

    private int getSpacesAvailableShelf(){
        int spaces=1;
        int temp;
        Matrix shelf;

        shelf = client.getModel().getMyBookshelf();

        for(int col=0;col<5;col++){
            for(int row=0;row<6;row++){
               if( shelf.getMatr()[row][col].equals(Tile.EMPTY) ) {
                   temp = 6-row;
                   if(temp>=spaces) spaces=temp;
               }
            }
        }

        return spaces;
    }

    private boolean checkTiles(List<Integer> tiles) {

        for(Integer t: tiles){
            if(t>=9 || t<0) {
                out.println("You're trying to get tiles that are not on the board!");
                return true;
            }
        }

        if (tiles.size() == 6) {
            if(client.getModel().getBoard().getTileByCoord(tiles.get(0), tiles.get(1)).equals(Tile.NOTAVAILABLE)
                    || client.getModel().getBoard().getTileByCoord(tiles.get(0), tiles.get(1)).equals(Tile.EMPTY)) return true;

            if(client.getModel().getBoard().getTileByCoord(tiles.get(2), tiles.get(3)).equals(Tile.NOTAVAILABLE)
                    || client.getModel().getBoard().getTileByCoord(tiles.get(2), tiles.get(3)).equals(Tile.EMPTY)) return true;

            if(client.getModel().getBoard().getTileByCoord(tiles.get(4), tiles.get(5)).equals(Tile.NOTAVAILABLE)
                    || client.getModel().getBoard().getTileByCoord(tiles.get(4), tiles.get(5)).equals(Tile.EMPTY)) return true;


            if(Objects.equals(tiles.get(0), tiles.get(2)) && Objects.equals(tiles.get(1), tiles.get(3))) return true;

            if(Objects.equals(tiles.get(2), tiles.get(4)) && Objects.equals(tiles.get(3), tiles.get(5))) return true;

            if(Objects.equals(tiles.get(0), tiles.get(4)) && Objects.equals(tiles.get(1), tiles.get(5))) return true;

            if (Objects.equals(tiles.get(0), tiles.get(2)) && Objects.equals(tiles.get(2), tiles.get(4))) return false;

            return !Objects.equals(tiles.get(1), tiles.get(3)) || !Objects.equals(tiles.get(3), tiles.get(5));
        } else if (tiles.size() == 4) {
            if(client.getModel().getBoard().getTileByCoord(tiles.get(0), tiles.get(1)).equals(Tile.NOTAVAILABLE)
                    || client.getModel().getBoard().getTileByCoord(tiles.get(0), tiles.get(1)).equals(Tile.EMPTY)) return true;

            if(client.getModel().getBoard().getTileByCoord(tiles.get(2), tiles.get(3)).equals(Tile.NOTAVAILABLE)
                    || client.getModel().getBoard().getTileByCoord(tiles.get(2), tiles.get(3)).equals(Tile.EMPTY)) return true;

            if(Objects.equals(tiles.get(0), tiles.get(2)) && Objects.equals(tiles.get(1), tiles.get(3))) return true;

            if (Objects.equals(tiles.get(0), tiles.get(2))) return false;

            return !Objects.equals(tiles.get(1), tiles.get(3));

        } else if (tiles.size() == 2) {
            return client.getModel().getBoard().getTileByCoord(tiles.get(0), tiles.get(1)).equals(Tile.NOTAVAILABLE)
                    || client.getModel().getBoard().getTileByCoord(tiles.get(0), tiles.get(1)).equals(Tile.EMPTY);
        }

        return true;
    }

    private boolean checkCatchable(List<Integer> tile){

        if(tile.get(0)>0 && tile.get(1)>0 && tile.get(0)<9 && tile.get(1)<9) {
            if (tile.get(0) != 8) {
                if (client.getModel().getBoard().getTileByCoord(tile.get(0) + 1, tile.get(1)).equals(Tile.NOTAVAILABLE)
                        || client.getModel().getBoard().getTileByCoord(tile.get(0) + 1, tile.get(1)).equals(Tile.EMPTY))
                    return true;
            }

            if (tile.get(0) != 0) {
                if (client.getModel().getBoard().getTileByCoord(tile.get(0) - 1, tile.get(1)).equals(Tile.NOTAVAILABLE)
                        || client.getModel().getBoard().getTileByCoord(tile.get(0) - 1, tile.get(1)).equals(Tile.EMPTY))
                    return true;
            }

            if (tile.get(1) != 8) {
                if (client.getModel().getBoard().getTileByCoord(tile.get(0), tile.get(1) + 1).equals(Tile.NOTAVAILABLE)
                        || client.getModel().getBoard().getTileByCoord(tile.get(0), tile.get(1) + 1).equals(Tile.EMPTY))
                    return true;
            }

            if (tile.get(1) != 0) {
                return client.getModel().getBoard().getTileByCoord(tile.get(0), tile.get(1) - 1).equals(Tile.NOTAVAILABLE)
                        || client.getModel().getBoard().getTileByCoord(tile.get(0), tile.get(1) - 1).equals(Tile.EMPTY);
            }
        }

        return false;
    }

    private boolean checkColumn(int column){

        if(column<0 || column>4) return false;

        if(coord.size()==2) return client.getModel().getMyBookshelf().getTileByCoord(5, column).equals(Tile.EMPTY);
        if(coord.size()==4) return client.getModel().getMyBookshelf().getTileByCoord(4, column).equals(Tile.EMPTY);
        if(coord.size()==6) return client.getModel().getMyBookshelf().getTileByCoord(3, column).equals(Tile.EMPTY);
        return false;
    }

    private static String waitForInput(Scanner scanner, ExecutorService executor) {
        try {
            // Avvia un'attività per leggere l'input dell'utente
            Future<String> future = executor.submit(scanner::nextLine);

            // Imposta il timeout
            return future.get(30, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            // Se si verifica un'eccezione, restituisce una stringa vuota
            return "";
        }
    }

    private void stopTurn(){
        if(TurnTimer){
            try{
                client.endYourTurn();
                client.askPassMyTurn();
            }catch (RemoteException e){
                out.println("RemoteException trying to end turn!");
            }
        }
    }

    private void printChat(){
        ColorCLI color = null;
        ArrayList<ArrayList<String>> chat = client.getModel().getMyChat().getConversation();

        Set<String> otherPlayers = client.getModel().getOtherPlayers().keySet();
        List<String> player = otherPlayers.stream().toList();

        for(ArrayList<String> message : chat){
            StringBuilder strSender = new StringBuilder();

            if(message.get(0).equals(player.get(0))){
                color = ColorCLI.GREEN;
            } else if (message.get(0).equals(player.get(1))) {
                color = ColorCLI.BLUE;
            } else if (message.get(0).equals(player.get(2))) {
                color = ColorCLI.PINK;
            }
            strSender.append(color).append(message.get(0)).append(ColorCLI.RESET);
            out.println(strSender + ": " + message.get(1));
        }
    }

    private void commandListener() {

        Scanner scan = new Scanner(System.in);
        String action;

        initMenu();

        while (!serverEndedGame) {

            out.println("Command:");

            action = scan.next();

            switch (action) {
                case "/help" -> {
                    System.out.println("/pgc: to show your PersonalGoalCard");
                    System.out.println("/cgc: to show your CommonGoalCard");
                    System.out.println("/score: to get your score");
                    System.out.println("/others: to show other player bookshelf");
                    System.out.println("/myShelf: to show my bookshelf");
                    System.out.println("/board: to show the updated board");
                    System.out.println("/tiles: to choose and insert the tiles from the board");
                    System.out.println("/chat: to write a message in the chat");
                    System.out.println("/showChat: to show your last message received");
                    System.out.println("/q: to leave the game");
                }

                case "/pgc" -> {
                    plotPGC();
                    stopTurn();
                }

                case "/cgc" -> {
                    out.println(client.getModel().getCgc1().getDescription());

                    if (client.getModel().getOtherPlayers().size() > 2) {
                        out.println(client.getModel().getCgc2().getDescription());
                    }
                    stopTurn();
                }

                case "/score" -> {
                    out.println("This is your score at the moment: ");
                    out.println(client.getModel().getMyScore());
                    stopTurn();
                }

                case "/others" -> {
                    plotOtherPlayersShelf();
                    stopTurn();
                }

                case "/myShelf" -> {
                    plotMatrixShelf(client.getModel().getMyBookshelf());
                    stopTurn();
                }

                case "/board" -> {
                    plotBoard();
                    stopTurn();
                }

                case "/tiles" -> {
                    if (client.isMyTurn()) {
                        commandInsertTiles();
                    } else {
                        out.println("You cannot do this!\nYou have to wait until it's your turn...");
                    }
                }

                case "/chat"->{
                    ArrayList<String> recipient = new ArrayList<>();
                    Set<String> otherPlayers = client.getModel().getOtherPlayers().keySet();
                    List<String> players = otherPlayers.stream().toList();
                    int num = players.size();

                    insideChat=true;
                    boolean sent=false;
                    boolean c=false;
                    out.println("type something and press 'enter'");
                    out.println("to leave the chat /qChat");
                    out.println("the chat is now broadcast!");
                    out.println("to send a message to a specific player, write at the end /player");
                    scan.nextLine();

                    do {
                        String mex = scan.nextLine();

                        if(mex.contains("/")) {
                            if(mex.equals("/qChat")) {
                                insideChat = false;
                                sent=true;
                                c = true;
                            }else if (mex.substring(mex.indexOf("/")+1).equals(players.get(0))){
                                recipient.add(players.get(0));
                                client.askPostMessage(mex.substring(0,mex.indexOf("/")), recipient);
                                sent=true;
                                recipient.clear();
                            } else if (num==2) {
                                if (mex.substring(mex.indexOf("/")+1).equals(players.get(1))){
                                    recipient.add(players.get(1));
                                    client.askPostMessage(mex.substring(0,mex.indexOf("/")), recipient);
                                    sent=true;
                                    recipient.clear();
                                }
                            } else if (num==3) {
                                if (mex.substring(mex.indexOf("/")+1).equals(players.get(1))){
                                    recipient.add(players.get(1));
                                    client.askPostMessage(mex.substring(0,mex.indexOf("/")), recipient);
                                    sent=true;
                                    recipient.clear();
                                }

                                if (mex.substring(mex.indexOf("/")+1).equals(players.get(2))) {
                                    recipient.add(players.get(2));
                                    client.askPostMessage(mex.substring(0,mex.indexOf("/")), recipient);
                                    sent=true;
                                    recipient.clear();
                                }
                            }
                        }

                        if(mex.equals("")){
                            out.println("You have tried sending an empty message");
                        }else if(!sent){
                            recipient.add(players.get(0));
                            if(num==2 || num==3) recipient.add(players.get(1));
                            if(num==3) recipient.add(players.get(2));
                            client.askPostMessage(mex, recipient);
                            recipient.clear();
                        }
                        sent=false;

                    }while (!c);
                    stopTurn();
                }

                case "/showChat"->{
                    out.println("These are your last messages: ");
                    printChat();
                    stopTurn();
                }

                case "/q" -> {
                    if(!gameEnded) {
                        try {
                            client.askLeaveGame();
                            serverEndedGame = true;
                        } catch (LoginException e) {
                            out.println("LoginException trying to leave the game!");
                        } catch (IOException e) {
                            out.println("IOException trying to leave the game!");
                        }
                    }else{
                        System.exit(0);
                    }
                }

                case "y" ->{
                    if(gameEnded){
                        try{
                            out.println("You are leaving the game...");
                            client.askLeaveGame();
                            out.println("See you soon!");
                            System.exit(0);
                        } catch (LoginException e) {
                            out.println("LoginException occurred trying to leave the game (game ended)!");
                        } catch (IOException e) {
                            out.println("IOException occurred trying to leave the game (game ended)!");
                        }

                    }
                }

                case "n" ->{
                    if(gameEnded) out.println("You are still in the game! Do whatever you want...");
                }

                case "/info" -> {
                    out.println("IS MY TURN:" + client.isMyTurn());
                    out.println("IS GAME ENDED:" + client.isGameEnded());
                    out.println("IS GAME STARTED" + client.isGameStarted());
                    out.println("GAME ID:" + client.getModel().getGameID());
                    out.println("CONNECTION TYPE:" + client.getModel().getConnectionType());
                    out.println("PING:" +client.getPingChecker());
                    out.println("SCORE:" + client.getModel().getMyScore());
                }
            }

        }
    }

}

