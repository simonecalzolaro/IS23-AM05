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

public class TUI extends View {

    List<Integer> coord = new ArrayList<>();
    private final PrintStream out;

    public TUI() {

        super();

        out = System.out;
        startGame();
        new Thread(this::commandListener).start();

    }

    //-------------------------------- @Override methods from View --------------------------------

    @Override
    public void getNumOfPlayer() throws RemoteException {

        int num;
        Scanner scan = new Scanner(System.in);

        do {

            System.out.print("Enter the number of players: ");
            // This method reads the number provided using keyboard
            num = scan.nextInt();

            if (num < 2 || num > 4) System.out.println("The number of players must be between 2 and 4");

        } while (num < 2 || num > 4);

        client.askSetNumberOfPlayers(num, client.getModel().getNickname());


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

        out.println("+---------------The game has ended!---------------+");

    System.out.println("               nickname     score       ");

        for (Map.Entry<String, Integer> entry : results.entrySet()) {
            System.out.println("                 "+ entry.getKey() + "  ->  " + entry.getValue());
        }
        out.println("+-------------------------------------------------+");

    }

    @Override
    public void isYourTurn() {

        if (client.isMyTurn()) {

            out.println("""      
                                    +-------------------------+
                                    |     IS YOUR TURN!!!     |
                                    +-------------------------+
                                                                         """);


            //updateBoard();
            /*String action;
            int request = 0;

            do {
                out.println("+-----------------------------------+");
                out.println("|     What do you want to do?       |");
                out.println("|        0 --> ShowPGC              |");
                out.println("|        1 --> ShowCGC              |");
                out.println("|        2 --> GetMyScore           |");
                out.println("|        3 --> MakeYourMove         |");
                out.println("|        4 --> ShowOtherPlayerShelf |");
                out.println("|        5 --> leave the game       |");
                out.println("+-----------------------------------+");
                out.println("---> ");

                action = getInput();

                switch (action) {

                    case "0" -> {

                        out.println("This is your PersonalGoalCard:\n");
                        plotPGC();
                        request++;
                    }

                    case "1" -> {
                        if (client.getModel().getOtherPlayers().size() < 3) {
                            out.println("This is the CommonGoalCard:\n");
                            out.println(client.getModel().getCgc1().getDescription());
                        } else {
                            out.println("These are the CommonGoalCards:\n");
                            out.println(client.getModel().getCgc1().getDescription());
                            out.println(client.getModel().getCgc2().getDescription());
                        }
                        request++;
                    }

                    case "2" ->{
                        out.println(client.getModel().getMyScore());
                        request++;
                    }

                    case "4" ->{
                        plotOtherPlayersShelf();
                    }

                    case "5" -> {

                        try {
                            client.askLeaveGame();
                        } catch (LoginException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }while(!action.equals("3") && request!=3);

            boolean goon = false;

            do {

                //---da rendere a prova di scimmia
                //  ->puù scegliere più volte la stessa tile
                //  ->può scegliere tiles non adiacenti
                //  ->substring funziona male senza fflush(), io ti consiglierei di fare un loop del tipo
                //      Inserisci x:
                //      Inserisci y:
                //      Continuare? y/n
                //  ->scritto così è sempre costretto a pescarne 3

                int round = 0;
                String select;

                out.println("Choose tiles from the board!");

                do {
                    do{

                        out.println("Insert x:");

                        int x = Integer.parseInt(getInput());

                        out.println("Insert y:");

                        int y = Integer.parseInt(getInput());

                        coord.add(x);
                        coord.add(y);
                        chosenTiles.add(client.getModel().getBoard().getTileByCoord(x,y));

                        if (!checkTiles(coord)) {
                            out.println("There has been some error!\nChoose tiles from the board!");
                            coord.clear();
                            chosenTiles.clear();
                            round=0;
                        }

                    }while(!checkTiles(coord));

                    round++;

                    out.println("Would you like to continue? y/n");
                    select = getInput();

                }while (round!=3 && !Objects.equals(select, "n"));


                try {
                    client.askBoardTiles(coord);
                    goon = true;

                } catch (InvalidChoiceException e) {
                    e.getMessage();
                    coord.clear();
                    chosenTiles.clear();
                }catch (NotConnectedException e){
                    out.println("NotConnectedException occurred trying to choose the tiles!");
                    e.printStackTrace();
                }catch (NotMyTurnException e){
                    out.println("NotMyTurnException occurred trying to choose the tiles!");
                    e.printStackTrace();
                }catch (InvalidParametersException e){
                    out.println("InvalidParametersException occurred trying to choose the tiles!");
                    e.getMessage();
                }

            } while (!goon);

            goon=false;

            do{
                out.println("Choose the column of your bookshelf for the tiles:\n");
                int chosenColumn = Integer.parseInt(getInput());

                try {
                    client.askInsertShelfTiles( chosenColumn, coord);
                    goon = true;
                    chosenTiles.clear();
                    coord.clear();
                }catch (InvalidChoiceException e){
                    e.getMessage();
                } catch (InvalidLenghtException e) {
                    throw new RuntimeException(e);
                }catch (NotMyTurnException e){
                    out.println("NotMyTurnException occurred trying to insert tiles!");
                    e.printStackTrace();
                }catch (NotConnectedException e){
                    out.println("NotConnectedException occurred trying to insert tiles!");
                    e.printStackTrace();
                }

            }while (!goon);
*/
        }

    }

    @Override
    public void startGame() {
        Scanner scan = new Scanner(System.in);
        String connection;
        String num;

        init();
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
                    e.printStackTrace();
                }
            }

            if (connection.equals("1")) {
                try {

                    client.setView(this);
                    client = new SocketClient();

                } catch (RemoteException e) {
                    out.println("RemoteException occurred trying to initialize the client");
                    e.printStackTrace();
                }
            }
        } while (!connection.equals("0") && !connection.equals("1"));

        try {
            client.initializeClient();
        } catch (RemoteException e) {
            out.println("RemoteException occurred trying to initialize the client");
            e.printStackTrace();
        } catch (NotBoundException e) {
            out.println("NotBoundException occurred trying to initialize the RMIClient");
            e.printStackTrace();
        } catch (IOException e) {
            out.println("IOException occurred trying to initialize the SocketClient");
            out.println("---> Socket hasn't been created");
            e.printStackTrace();
        }

        do {
            System.out.println("0 --> start a new game");
            System.out.println("1 --> continue a Game");
            num = scan.next();

            if (!num.equals("0") && !num.equals("1")) System.out.println("ClientApp --- Invalid code --> Try again !");

        } while (!num.equals("0") && !num.equals("1"));

        boolean goon = false;
        switch (num) {
            case "0":
                do {
                    out.println("Choose your nickname:");
                    String nickname = scan.next();
                    try {

                        client.getModel().setNickname(nickname);
                        client.askLogin(nickname);

                        goon = true;
                    } catch (LoginException e) {
                        e.getMessage();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } while (!goon);

                break;

            case "1":

                do {
                    try {
                        client.askContinueGame();
                        goon=true;
                    } catch (LoginException e) {
                        e.getMessage();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }while (!goon);

                break;


        }
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
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if(client.getModel().getPgcNum() == -1 && conti%20==0) System.out.println("...waiting for other players");
            conti++;
        }
    }

    @Override
    public void endYourTurn() {
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

    }

    @Override
    public void showException(String exception) {
        out.println(exception);
    }

    @Override
    public void plotNewMessage(String sender,String message) {
        out.println(sender+":"+message);
    }


    //-------------------------------- other methods --------------------------------

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

    public void init() {

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

    public void initMenu(){
        out.println("+-----------------------------------------------------------+");
        out.println("|                          MENU:                            |");
        out.println("|                                                           |");
        out.println("|   /help : to show all the commands available              |");
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

    public void plotBoard() {

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

        out.println(strBoard.toString());

    }

    public void plotMatrixShelf(Matrix bookshelf) {

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

        out.println(strShelf.toString());

    }

    public void plotOtherPlayersShelf() {

        for (Map.Entry<String, Matrix> entry : client.getModel().getOtherPlayers().entrySet()) {
            out.println(entry.getKey());
            out.println("\n");
            plotMatrixShelf(entry.getValue());
        }

    }

    public void plotPGC() {
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

        System.out.println(strPGC.toString());

    }

    public void commandInsertTiles() {
        Scanner scan = new Scanner(System.in);
        boolean goon = false;
        boolean ok=false;

        do {

            //---da rendere a prova di scimmia
            int x,y;
            int round = 0;
            String select = "";
            out.println("Choose tiles from the board!");

            while (!Objects.equals(select, "n")) {
                do {

                    out.println("Insert x:");

                    do {
                        try {
                            x = Integer.parseInt(scan.next());
                            coord.add(x);
                            ok=true;
                        } catch (NumberFormatException e) {
                            out.println("NumberFormatException trying to choose the x tile!");
                            out.println("Insert x: ");
                        }
                    }while (!ok);

                    ok=false;
                    out.println("Insert y:");

                    do {
                        try {
                            y = Integer.parseInt(scan.next());
                            coord.add(y);
                            ok=true;
                        } catch (NumberFormatException e) {
                            out.println("NumberFormatException trying to choose the y tile!");
                            out.println("Insert y: ");
                        }
                    }while (!ok);

                    round++;

                    if (!checkTiles(coord)) {
                        out.println("There has been some error!\nChoose tiles from the board!");
                        coord.clear();
                        round = 0;
                    }

                } while (!checkTiles(coord));

                if (round == 3) break;

                out.println("Would you like to continue? y/n");
                select = scan.next();

            }


            try {
                client.askBoardTiles(coord);
                goon = true;

            } catch (InvalidChoiceException e) {
                e.getMessage();
                coord.clear();
            } catch (NotConnectedException e) {
                out.println("NotConnectedException occurred trying to choose the tiles!");
                e.printStackTrace();
            } catch (NotMyTurnException e) {
                out.println("NotMyTurnException occurred trying to choose the tiles!");
                e.printStackTrace();
            } catch (InvalidParametersException e) {
                out.println("InvalidParametersException occurred trying to choose the tiles!");
            } catch (IOException e) {
                out.println("IOException occurred trying to choose the tiles!");
            }

        } while (!goon);

        goon = false;
        ok=false;

        do {
            out.println("Choose the column of your bookshelf for the tiles:\n");
            int chosenColumn=0;

            do {
                try {
                    chosenColumn = Integer.parseInt(scan.next());
                    ok=true;
                } catch (NumberFormatException e) {
                    out.println("NumberFormatException trying to choose the column of your bookshelf!");
                    out.println("Try again: ");
                }
            }while (!ok);

            try {
                client.askInsertShelfTiles(chosenColumn, coord);
                goon = true;
                coord.clear();
            } catch (InvalidChoiceException e) {
                e.getMessage();
            } catch (InvalidLenghtException e) {
                throw new RuntimeException(e);
            } catch (NotMyTurnException e) {
                out.println("NotMyTurnException occurred trying to insert tiles!");
                e.printStackTrace();
            } catch (NotConnectedException e) {
                out.println("NotConnectedException occurred trying to insert tiles!");
                e.printStackTrace();
            } catch (IOException e) {
                out.println("IOException occurred trying to choose the tiles!");
            }

        } while (!goon);
    }

    public boolean checkTiles(List<Integer> tiles) {

        if (tiles.size() == 6) {
            if(Objects.equals(tiles.get(0), tiles.get(2)) && Objects.equals(tiles.get(1), tiles.get(3))) return false;

            if(Objects.equals(tiles.get(2), tiles.get(4)) && Objects.equals(tiles.get(3), tiles.get(5))) return false;

            if(Objects.equals(tiles.get(0), tiles.get(4)) && Objects.equals(tiles.get(1), tiles.get(5))) return false;

            if (Objects.equals(tiles.get(0), tiles.get(2)) && Objects.equals(tiles.get(2), tiles.get(4))) return true;

            if (Objects.equals(tiles.get(1), tiles.get(3)) && Objects.equals(tiles.get(3), tiles.get(5))) return true;

            return false;
        } else if (tiles.size() == 4) {
            if(Objects.equals(tiles.get(0), tiles.get(2)) && Objects.equals(tiles.get(1), tiles.get(3))) return false;

            if (Objects.equals(tiles.get(0), tiles.get(2))) return true;

            if (Objects.equals(tiles.get(1), tiles.get(3))) return true;

            return false;
        } else if (tiles.size() == 2) {
            return true;
        }

        return false;
    }

    public void commandListener() {

        Scanner scan = new Scanner(System.in);
        String action;
        boolean flag = true;

        initMenu();

        while (flag) {
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
                    System.out.println("/showChat: to show the chat");
                    System.out.println("/q: to leave the game");
                }

                case "/pgc" -> {
                    plotPGC();
                }

                case "/cgc" -> {
                    out.println(client.getModel().getCgc1().getDescription());

                    if (client.getModel().getOtherPlayers().size() > 2) {
                        out.println(client.getModel().getCgc2().getDescription());
                    }
                }

                case "/score" -> {
                    out.println("This is your score at the moment: ");
                    out.println(client.getModel().getMyScore());
                }

                case "/others" -> {
                    plotOtherPlayersShelf();
                }

                case "/myShelf" -> {
                    plotMatrixShelf(client.getModel().getMyBookshelf());
                }

                case "/board" -> {
                    plotBoard();
                }

                case "/tiles" -> {
                    if (client.isMyTurn()) {
                        commandInsertTiles();
                    } else {
                        out.println("You cannot do this!\nYou have to wait until it's your turn...");
                    }
                }

                case "/chat"->{
                    out.println("type something and press 'enter'");
                    String mex=scan.next();
                    client.askPostMessage(mex, new ArrayList(client.getModel().getOtherPlayers().keySet()));
                }

                case "/showChat"->{
                    out.println("");
                }

                case "/q" -> {
                    try {
                        client.askLeaveGame();
                        flag = false;
                    } catch (LoginException e) {
                        e.getMessage();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

}

