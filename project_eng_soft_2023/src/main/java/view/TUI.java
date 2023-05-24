package view;

import client.RMIClient;
import client.SocketClient;
import model.Tile;
import myShelfieException.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.*;

public class TUI extends View {

    List<Integer> coord ;
    ArrayList<client.Tile> chosenTiles ;
    private final PrintStream out;
    private final BufferedReader reader;

    public TUI() {

        super();

        reader = new BufferedReader(new InputStreamReader(System.in));
        out = System.out;

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

            if(num<2 || num >4) System.out.println("The number of players must be between 2 and 4");

        }while(num<2 || num >4);

        client.askSetNumberOfPlayers(num, client.getModel().getNickname());

    }

    @Override
    public void updateBoard() {

        out.println("This is the updated board:\n");
        plotBoard();
        out.println("This is your Bookshelf:\n");
        plotShelf();

    }

    @Override
    public void endGame(Map< Integer, String> results) {

        for(Integer key: results.keySet()){
            System.out.println(key + " ->   "+ results.get(key));
        }

        out.println("The game has ended!\n");

    }

    @Override
    public void isYourTurn() throws IOException{

        if(client.isMyTurn()) {
            out.println("It's your turn, you have 2 min to complete your task!\n");

            updateBoard();
            String action;
            int request = 0;

            do {
                out.println("+-----------------------------------+");
                out.println("|     What do you want to do?       |");
                out.println("|        0 --> ShowPGC              |");
                out.println("|        1 --> ShowCGC              |");
                out.println("|        2 --> GetMyScore           |");
                out.println("|        3 --> MakeYourMove         |");
                out.println("|        4 --> leave the game       |");
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
                        /*
                        try {
                            //out.println(client.askGetMyScore());
                        }catch (IOException e){
                            out.println("IOException occurred trying to get the score!");
                            e.printStackTrace();
                        }

                         */
                    }

                    case "4" -> {

                        /*
                        try {
                            out.println(client.askLeaveGame());
                        } catch (LoginException e) {
                            throw new RuntimeException(e);
                        }

                         */
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

                out.println("Choose tiles from the board: (x,y)\n");
                String tile1 = getInput();
                coord.add(Integer.valueOf(tile1.substring(2)));
                coord.add(Integer.valueOf(tile1.substring(4)));
                chosenTiles.add(client.getModel().getBoard().getTileByCoord(coord.get(0), coord.get(1)));

                String tile2 = getInput();
                coord.add(Integer.valueOf(tile2.substring(2)));
                coord.add(Integer.valueOf(tile2.substring(4)));
                chosenTiles.add(client.getModel().getBoard().getTileByCoord(coord.get(2), coord.get(3)));

                String tile3 = getInput();
                coord.add(Integer.valueOf(tile3.substring(2)));
                coord.add(Integer.valueOf(tile3.substring(4)));
                chosenTiles.add(client.getModel().getBoard().getTileByCoord(coord.get(4), coord.get(5)));

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

        }

    }

    @Override
    public void startGame() throws IOException{

        String connection;
        String num;

        init();
        out.println("What kind of connection do you want?\n");

        do{
            out.println("0 --> RMI \n1 --> Socket");
            connection = getInput();

            if(connection.equals("0")){
                try {
                    client = new RMIClient(this);
                }catch (RemoteException e){
                    out.println("RemoteException occurred trying to initialize the client");
                    e.printStackTrace();
                }
            }

            if(connection.equals("1")){
                try {
                    client = new SocketClient(this);
                }catch (RemoteException e){
                    out.println("RemoteException occurred trying to initialize the client");
                    e.printStackTrace();
                }
            }
        }while(!connection.equals("0") && !connection.equals("1"));

        try{
            client.initializeClient();
        }catch (RemoteException e){
            out.println("RemoteException occurred trying to initialize the client");
            e.printStackTrace();
        }catch (NotBoundException e){
            out.println("NotBoundException occurred trying to initialize the RMIClient");
            e.printStackTrace();
        }catch (IOException e){
            out.println("IOException occurred trying to initialize the SocketClient");
            out.println("---> Socket hasn't been created");
            e.printStackTrace();
        }

        do {
            System.out.println("0 --> start a new game");
            System.out.println("1 --> continue a Game");
            num = getInput();

            if(!num.equals("0") &&  !num.equals("1")) System.out.println("ClientApp --- Invalid code --> Try again !");

        }while (!num.equals("0") &&  !num.equals("1"));

        boolean goon = false;
        switch (num){
            case "0":
                do {
                     out.println("Choose your nickname:\n");
                    String nickname = getInput();
                    try {
                        client.askLogin(nickname);
                        client.getModel().setNickname(nickname);
                        goon = true;
                    } catch (LoginException e) {
                        e.getMessage();
                    }
                }while(!goon);

                break;

            case "1":

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

        while(client.getModel().getPgcNum()==-1){
            System.out.println("...waiting for other players");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
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


    //-------------------------------- other methods --------------------------------

    private enum ColorCLI{
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

        ColorCLI(String color){
            this.color = color;
        }

        public String toString(){
            return color;
        }
    }


    public String getInput() throws IOException {
        return reader.readLine();
    }

    public boolean init() {
        out.println("""
                ███      ███ ██    ██   ███████ ██   ██ ██████ ██     ██████ ██ ██████ \s
                ████    ████  ██  ██    ██      ██   ██ ██     ██     ██     ██ ██     \s
                ██ ██  ██ ██   ████     ███████ ███████ ██████ ██     ██████ ██ ██████ \s
                ██  ████  ██    ██           ██ ██   ██ ██     ██     ██     ██ ██     \s
                ██   ██   ██    ██      ███████ ██   ██ ██████ ██████ ██     ██ ██████ \s
                                                                                       \s
                                                                                       \s""");

        out.println("\nWelcome to My Shelfie Board Game!");
        out.println("\nCreators: Elena Caratti, Gabriele Clara Di Gioacchino, Mirko Calvi, Simone Calzolaro!");

        return true;
    }

    public boolean plotBoard() {

            StringBuilder strBoard = new StringBuilder();
            int temp=0;

            for(int row=0; row< 9; row++){
                for(int col=0; col< 9; col++){

                    if(row!=temp) {
                        temp++;
                        strBoard.append("\n");
                    }

                    switch (client.getModel().getBoard().getTileByCoord(row, col)){
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

            return true;
    }

    public boolean plotShelf() {
        StringBuilder strShelf = new StringBuilder();
        int temp=0;

        for(int row=0; row<6; row++){
            strShelf.append("\n");
            for(int col=0; col< 5; col++){

                switch (client.getModel().getMyBookshelf().getTileByCoord(row, col)){

                    case GREEN -> strShelf.append("|").append(ColorCLI.GREEN_BACKGROUND).append(row).append(";").append(col).append(ColorCLI.RESET);
                    case BLUE -> strShelf.append("|").append(ColorCLI.BLUE_BACKGROUND).append(row).append(";").append(col).append(ColorCLI.RESET);
                    case WHITE -> strShelf.append("|").append(ColorCLI.WHITE_BACKGROUND).append(row).append(";").append(col).append(ColorCLI.RESET);
                    case PINK -> strShelf.append("|").append(ColorCLI.PINK_BACKGROUND).append(row).append(";").append(col).append(ColorCLI.RESET);
                    case YELLOW -> strShelf.append("|").append(ColorCLI.YELLOW_BACKGROUND).append(row).append(";").append(col).append(ColorCLI.RESET);
                    case LIGHTBLUE -> strShelf.append("|").append(ColorCLI.LIGHTBLUE_BACKGROUND).append(row).append(";").append(col).append(ColorCLI.RESET);
                    case EMPTY, NOTAVAILABLE -> strShelf.append("|   ").append(ColorCLI.RESET);

                }
            }
            strShelf.append("|");
        }

        out.println(strShelf.toString());

        return true;
    }

    public boolean plotPGC() {
        StringBuilder strPGC = new StringBuilder();

        int temp=0;

        for(int row=0; row<6; row++){
            for(int col=0; col<5; col++){

                if(row!=temp) {
                    temp++;
                    strPGC.append("\n");
                }

                if(Arrays.equals(client.getModel().getPgc().getMap().get(Tile.GREEN), new Integer[]{row, col})){
                    strPGC.append("|").append(ColorCLI.GREEN_BACKGROUND).append(" ").append(ColorCLI.RESET);
                }else if(Arrays.equals(client.getModel().getPgc().getMap().get(Tile.BLUE), new Integer[]{row, col})){
                    strPGC.append("|").append(ColorCLI.BLUE_BACKGROUND).append(" ").append(ColorCLI.RESET);
                } else if (Arrays.equals(client.getModel().getPgc().getMap().get(Tile.WHITE), new Integer[]{row, col})) {
                    strPGC.append("|").append(ColorCLI.WHITE_BACKGROUND).append(" ").append(ColorCLI.RESET);
                }else if(Arrays.equals(client.getModel().getPgc().getMap().get(Tile.PINK), new Integer[]{row, col})){
                    strPGC.append("|").append(ColorCLI.PINK_BACKGROUND).append(" ").append(ColorCLI.RESET);
                }else if(Arrays.equals(client.getModel().getPgc().getMap().get(Tile.YELLOW), new Integer[]{row, col})){
                    strPGC.append("|").append(ColorCLI.YELLOW_BACKGROUND).append(" ").append(ColorCLI.RESET);
                }else if(Arrays.equals(client.getModel().getPgc().getMap().get(Tile.LIGHTBLUE), new Integer[]{row, col})){
                    strPGC.append("|").append(ColorCLI.LIGHTBLUE_BACKGROUND).append(" ").append(ColorCLI.RESET);
                }else{
                    strPGC.append("| ").append(ColorCLI.RESET);
                }
                if(col==4) strPGC.append("|");
            }
        }

        System.out.println(strPGC.toString());

        return true;
    }
}
