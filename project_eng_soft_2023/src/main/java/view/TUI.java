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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TUI extends View {
    List<Integer> coord = new ArrayList<>();
    List<Tile> chosenTiles = new ArrayList<>();

    @Override
    public int getNumOfPlayer() throws RemoteException {
        return client.enterNumberOfPlayers();
    }

    @Override
    public void updateBoard() {
        out.println("This is the updated board:\n");
        plotBoard();
        out.println("This is your Bookshelf:\n");
        plotShelf();
    }

    @Override
    public void endGame() {
        out.println("The game has ended!\n");
    }

    @Override
    public void isYourTurn() throws IOException, NotConnectedException, InvalidParametersException {

        try{
        if(client.isMyTurn()) {
            out.println("It's your turn, you have 2 min to complete your task!\n");

            updateBoard();
            String action;
            int request = 0;

            do {
                out.println("What do you want to do?\n");
                out.println("0 --> ShowPGC\n");
                out.println("1 --> ShowCGC\n");
                out.println("2 --> MakeYourMove\n");
                action = getInput();

                switch (action) {

                    case "0" -> {

                        out.println("This is your PersonalGoalCard:\n");
                        plotPGC();
                        request++;
                    }

                    case "1" -> {
                        if (getNumOfPlayer() == 3) {
                            out.println("This is the CommonGoalCard:\n");
                            out.println(client.getModel().getCgc1().getDescription());
                        } else {
                            out.println("These are the CommonGoalCards:\n");
                            out.println(client.getModel().getCgc1().getDescription());
                            out.println(client.getModel().getCgc2().getDescription());
                        }
                        request++;

                    }

                }
            }while(!action.equals("2") || request!=2);


            boolean goon = false;

            do {

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
                    client.askBoardTiles(chosenTiles, coord);
                    goon = true;
                    coord.clear();
                    chosenTiles.clear();

                } catch (InvalidChoiceException e) {
                    e.getMessage();
                }

            } while (!goon);

        }

        }catch(NotMyTurnException e){
            e.getMessage();
        }

    }

    @Override
    public void startGame() throws IOException, NotBoundException {
        init();
        out.println("What kind of connection do you want?\n");
        String connection;
        do{
            out.println("0 --> RMI \n1 --> Socket");
            connection = getInput();

            if(connection.equals("0")){
                client = new RMIClient();
            }

            if(connection.equals("1")){
                client = new SocketClient();
            }
        }while(!connection.equals("0") && !connection.equals("1"));

        client.initializeClient();

        boolean goon = false;
        do {
            out.println("Choose your nickname:\n");
            String nickname = getInput();
            try {
                client.askLogin(nickname);
                goon = true;
            } catch (LoginException e) {
                e.getMessage();
            }
        }while(!goon);
    }

    @Override
    public void endYourTurn() {
        out.println("Your turn is over!\n");
    }


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

    private final PrintStream out;
    private final BufferedReader reader;

    public TUI() {
        reader = new BufferedReader(new InputStreamReader(System.in));
        out = System.out;
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
            for(int col=0; col< 5; col++){

                if(row!=temp) {
                    temp++;
                    strShelf.append("\n");
                }

                switch (client.getModel().getMyBookshelf().getTileByCoord(row, col)){
                    case GREEN -> strShelf.append("|").append(ColorCLI.GREEN_BACKGROUND).append(row).append(";").append(col).append(ColorCLI.RESET);
                    case BLUE -> strShelf.append("|").append(ColorCLI.BLUE_BACKGROUND).append(row).append(";").append(col).append(ColorCLI.RESET);
                    case WHITE -> strShelf.append("|").append(ColorCLI.WHITE_BACKGROUND).append(row).append(";").append(col).append(ColorCLI.RESET);
                    case PINK -> strShelf.append("|").append(ColorCLI.PINK_BACKGROUND).append(row).append(";").append(col).append(ColorCLI.RESET);
                    case YELLOW -> strShelf.append("|").append(ColorCLI.YELLOW_BACKGROUND).append(row).append(";").append(col).append(ColorCLI.RESET);
                    case LIGHTBLUE -> strShelf.append("|").append(ColorCLI.LIGHTBLUE_BACKGROUND).append(row).append(";").append(col).append(ColorCLI.RESET);
                    case EMPTY, NOTAVAILABLE -> strShelf.append("|   ").append(ColorCLI.RESET);
                }

                if(col==4) strShelf.append("|");

            }
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
