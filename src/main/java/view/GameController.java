package view;
import client.Client;
import client.Matrix;
import client.Tile;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import myShelfieException.*;

import java.io.IOException;
import java.util.*;

/**
 *
 * Gui controller for the game scene
 */

public class GameController extends GUIController {
    Timer timer;
    private ImageView[][] insertBookshelf;
    private List<Button> columnButtons;

    private Group[][] boardGroups;
   private ImageView[][]boardImages;
    private Button[][] boardButtons;
    private ImageView[][] myBookshelf;
    private ImageView[][] showMyBookshelf;
    private ImageView[][] showBookshelf1;
    private ImageView[][] showBookshelf2;
     private ImageView[][] showBookshelf3;
    private ImageView[][] bookshelf1;
     private ImageView[][] bookshelf2;
    private ImageView[][] bookshelf3;


    private int selectedColumn=-1;

    private final TileImages tileImages=new TileImages();

    private final List<Integer> coord = new ArrayList<>();
    private final List<Integer> sortedCoord = new ArrayList<>();


    @Override
    public void setScene(GUI gui, Stage stage) {
        super.setScene(gui, stage);
        if(client.getModel().getCgc1()!=null){
            ((ImageView) root.lookup("#cgc1")).setImage(new Image(getCGCImage(client.getModel().getCgc1().ordinal())));
            ((ImageView) root.lookup("#cgc2")).setImage(new Image(getCGCImage(client.getModel().getCgc2().ordinal())));
            ((ImageView) root.lookup("#pgc")).setImage(new Image(getPGCImage(client.getModel().getPgcNum())));
        }
        initialize();
        setOtherBookshelf();
        gui.getChatController().setClient(client);
        gui.getChatController().initializeChat();
    }

    private Timer timerExc;
    private final TimerTask taskExc = new TimerTask() {
        @Override
        public void run() {
            Platform.runLater(()->{
                root.lookup("#exceptionLabel").setVisible(false);
                root.lookup("#exceptionLabel").setDisable(true);
            });

            if(timerExc!=null){
                timerExc.cancel();
                timerExc=null;
            }

        }
    };

    public void setClient(Client client){
        this.client=client;
        }

    //----------------------------------------------------------- Server vs client-------------------------------------

    /**
     * Method invoked by the GUI View to set GUI on StarTurn notify
     */
    public void startTurn(){
        ableBoardButton();
        setBoardButton();
        root.lookup("#enterTilesButton").setVisible(true);
        ((Label) root.lookup("#stateLabel")).setText("Choose tile!");
        setTimer();
    }


    /**
     * Method invoked by the GUI View to set the GUI on endGame notify
     * @throws IOException from FXMLLoader load
     */
    public void endGame(Map<String, Integer> results) throws IOException {
        FXMLLoader fxmlLoader1 = new FXMLLoader(GUIApplication.class.getResource("rank.fxml"));
        Parent root1= fxmlLoader1.load();
        Scene scene = new Scene(root1, 1250,650);
        RankController rankController=fxmlLoader1.getController();
        rankController.setRoot(root1);
        rankController.setClient(client);
        rankController.setScene(gui,stage);
        rankController.setRank(results);
        stage.setScene(scene);
    }

    /**
     * Method invoked by the GUI View to set the GUI on update notify
     */
    public void updateAll() {
        updateBoard();
        updateScore();
        updateMyBookshelf();
        updateOtherBookshelf();
        if(client.GameEnded()) root.lookup("#endGameToken").setVisible(false);
        if(((ImageView) root.lookup("#cgc1")).getImage()!=null){
            ((ImageView) root.lookup("#cgc1")).setImage(new Image(getCGCImage(client.getModel().getCgc1().ordinal())));
            ((ImageView) root.lookup("#cgc2")).setImage(new Image(getCGCImage(client.getModel().getCgc2().ordinal())));
            ((ImageView) root.lookup("#pgc")).setImage(new Image(getPGCImage(client.getModel().getPgcNum())));
        }
    }

    /**
     * Method invoked by the GUI View to end the turn.
     */
    public void endTurn(){
        coord.clear();
        sortedCoord.clear();
        root.lookup("#enterColumnButton").setDisable(true);
        root.lookup("#enterTilesButton").setDisable(true);
        root.lookup("#enterTilesButton").setVisible(false);
        ((Button) root.lookup("#bookshelfButton")).setOnAction(null);
        ((Label) root.lookup("#stateLabel")).setText("Not your turn!");
    }

    /**
     * Method invoked my the GUI View to show the stage if there is an active game to restore
     */
    public void showGame() {
        stage.show();
    }

    @Override
    public void showException(String exception){
        if(timerExc!=null){
            timerExc.cancel();
            timerExc=null;
        }

        ((Label) root.lookup("#exceptionLabel")).setText(exception);
        root.lookup("#exceptionLabel").setVisible(true);
        root.lookup("#exceptionLabel").setDisable(false);

        if(timerExc!=null){
            timerExc.cancel();
            timerExc=null;
        }
        timerExc = new Timer();
        timerExc.schedule(taskExc, 3000);

    }


//------------------------------------------------------------------------------Update Method----------------------------------------------
    private void updateScore() {
        ((Label) root.lookup("#scoreLabel")).setText(Integer.toString(client.getModel().getMyScore()));
    }

    private void updateMyBookshelf() {
        updateBookshelf(myBookshelf,client.getModel().getMyBookshelf());
       updateBookshelf(insertBookshelf, myBookshelf);
       updateBookshelf(showMyBookshelf, myBookshelf);
    }

    private void updateBookshelf(ImageView[][] bookshelf, Matrix reference){
        if(bookshelf!=null){
            int h=5;
            for(int i=0; i<6;i++){
                for(int j=0; j<5; j++){
                    if(bookshelf[i][j].getImage()==null){
                        Tile tile=reference.getTileByCoord(h, j);
                        if(tile==Tile.NOTAVAILABLE||tile==Tile.EMPTY||tile==null) {
                            bookshelf[i][j].setImage(null);
                        }
                        else {
                            Image image=new Image(tileImages.getImage(tile));
                            bookshelf[i][j].setImage(image);
                        }
                    }

                }
                h--;
            }
        }

    }

    private void updateBookshelf(ImageView[][] bookshelf, ImageView[][] reference){
        for(int i = 0; i<6; i++){
            for(int j =0; j<5; j++){
                bookshelf[i][j].setImage(reference[i][j].getImage());
            }
        }
    }

    private void updateOtherBookshelf() {
        int i = 0;
        for (Matrix m : client.getModel().getOtherPlayers().values()) {
            if(i<client.getModel().getOtherPlayers().size()){
                switch (i) {
                    case 0 -> {
                        updateBookshelf(bookshelf1, m);
                        updateBookshelf(showBookshelf1, bookshelf1);
                    }
                    case 1 -> {
                        updateBookshelf(bookshelf2, m);
                        updateBookshelf(showBookshelf2, bookshelf2);
                    }
                    case 2 -> {
                        updateBookshelf(bookshelf3, m);
                        updateBookshelf(showBookshelf3, bookshelf3);
                    }
                    default -> {
                        return;
                    }
                }
            }
            i++;


        }

    }

    private void updateBoard(){
        int checkRefill=0;
        for (int i = 0; i<9; i++) {
            for (int j = 0; j < 9; j++) {
                if (boardGroups[j][i]!=null&&boardImages[j][i].getImage()==null&&client.getModel().getBoard().getTileByCoord(i,j)!=Tile.NOTAVAILABLE&&client.getModel().getBoard().getTileByCoord(i,j)!=Tile.EMPTY) {
                    checkRefill=1;
                    break;
                }
            }
            if (checkRefill==1) break;
        }
        for (int i = 0; i<9; i++){
            for (int j = 0; j<9; j++){
                if(checkRefill==1&&boardGroups[j][i]!=null)  boardImages[j][i].setImage(null);
                if(boardGroups[j][i]!=null&&(client.getModel().getBoard().getTileByCoord(i,j)==Tile.EMPTY||boardImages[j][i].getImage()==null)){
                    Tile tile=client.getModel().getBoard().getTileByCoord(i,j);
                    if (tile==Tile.EMPTY||tile==Tile.NOTAVAILABLE) boardImages[j][i].setImage(null);
                    else {
                        String image = tileImages.getImage(tile);
                        if(image!=null) boardImages[j][i].setImage(new Image(image));
                    }
                }
            }
        }
    }

    //------------------------------------------------------------Client vs Server-----------------------------------

    @FXML
    private void enterTiles(ActionEvent actionEvent) {
        for (int i=0; i<9; i++){
            for (int j=0; j<9; j++){
                if(boardButtons[i][j]!=null){
                    if(boardButtons[i][j].getOnMouseEntered()==null){
                        coord.add(j);
                        coord.add(i);
                    }
                }
            }
        }
        try{
            client.askBoardTiles(coord);
        } catch (InvalidChoiceException | InvalidParametersException e) {
            showException("This move is invalid!");
        } catch (NotConnectedException e) {
            showException("You're not online");
        } catch (IOException e) {
            showException("IOException");
        } catch (NotMyTurnException e) {
            showException("It's not your turn!");
        }
        disableBoardButton();
        setTile();

        for(Button button:columnButtons){
            button.setDisable(true);
        }
        root.lookup("#enterTilesButton").setDisable(true);
        root.lookup("#enterTilesButton").setVisible(false);


        removeTiles();
        for(Button button:columnButtons){
            button.setDisable(false);
        }

        selectMyBookshelf(actionEvent);
        ((Button) root.lookup("#bookshelfButton")).setOnAction(this::selectMyBookshelf);
        if(timer!=null){
            timer.cancel();
            timer=null;
        }
        setTimer();
    }

    @FXML
    private void leaveGame(ActionEvent actionEvent){

        try {
            client.askLeaveGame();
            System.exit(0);
        } catch (LoginException e) {
            showException("An error occurred while trying to leave the game");
        } catch (IOException e) {
            showException("IOException");
        }
    }

    @FXML
    private void insertTile(ActionEvent actionEvent) {
        try {
            client.askInsertShelfTiles(selectedColumn, sortedCoord);
            for(int i=0; i<6; i++){
                myBookshelf[i][selectedColumn].setImage(insertBookshelf[i][selectedColumn].getImage());
            }
        } catch (InvalidChoiceException | InvalidLenghtException e) {
            showException("This move is invalid!");
        } catch (NotConnectedException e) {
            showException("You're not online!");
        } catch (IOException e) {
            showException("IOException");
        } catch (NotMyTurnException e) {
            showException("It's not your turn!");
        }

        endInsertTiles(actionEvent);
    }

    private void passTurn() {
        if(timer!=null){
            timer.cancel();
            timer=null;
        }

        updateBookshelf(myBookshelf, client.getModel().getMyBookshelf());
        disableBoardButton();
        coord.clear();
        sortedCoord.clear();
        root.lookup("#enterTilesButton").setDisable(true);
        root.lookup("#enterTilesButton").setVisible(false);
        removeTiles();
        updateBoard();
        endInsertTiles(new ActionEvent());
        ((Label) root.lookup("#stateLabel")).setText("Not you're turn!");
        try{
            client.askPassMyTurn();

        } catch (Exception e) {
            showException("An error occurred while trying to pass the turn!");
        }
    }






    //------------------------------------------------------------GUI setting methods----------------------------------------------------------------

    private void setTile() {
        for(int i=0; i<coord.size();i=i+2){
            switch (i){
                case 0-> ((ImageView) root.lookup("#tile1Image")).setImage(boardImages[coord.get(i+1)][coord.get(i)].getImage());
                case 2-> ((ImageView) root.lookup("#tile2Image")).setImage(boardImages[coord.get(i+1)][coord.get(i)].getImage());
                case 4-> ((ImageView) root.lookup("#tile3Image")).setImage(boardImages[coord.get(i+1)][coord.get(i)].getImage());
            }
        }

        root.lookup("#tile1").setVisible(true);

        if(((ImageView) root.lookup("#tile2Image")).getImage()!=null) {
            root.lookup("#tile2").setVisible(true);

        }
        if (((ImageView) root.lookup("#tile3Image")).getImage()!=null){
            root.lookup("#tile3").setVisible(true);

        }
    }

    private void endInsertTiles(ActionEvent actionEvent) {
        closeBookshelf(actionEvent);
        if (timer!=null){
            timer.cancel();
            timer=null;
        }

        root.lookup("#timeLabel").setVisible(false);

        ((ImageView) root.lookup("#tile1Image")).setImage(null);
        ((ImageView) root.lookup("#tile2Image")).setImage(null);
        ((ImageView) root.lookup("#tile3Image")).setImage(null);

        root.lookup("#tile1").setDisable(true);
        root.lookup("#tile2").setDisable(true);
        root.lookup("#tile3").setDisable(true);

        root.lookup("#tile1").setVisible(false);
        root.lookup("#tile2").setVisible(false);
        root.lookup("#tile3").setVisible(false);

        root.lookup("#tile1Button").setOnMouseExited(this::highlightTile);
        root.lookup("#tile2Button").setOnMouseExited(this::highlightTile);
        root.lookup("#tile3Button").setOnMouseExited(this::highlightTile);

        root.lookup("#tile1Button").setOnMouseEntered(this::highlightTile);
        root.lookup("#tile2Button").setOnMouseEntered(this::highlightTile);
        root.lookup("#tile3Button").setOnMouseEntered(this::highlightTile);

        root.lookup("#tile1Button").setOpacity(0);
        root.lookup("#tile2Button").setOpacity(0);
        root.lookup("#tile3Button").setOpacity(0);


        unselectColumn();
        for(Button button:columnButtons){
            button.setDisable(true);
        }

    }
    private void selectMyBookshelf(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        if(button.getOnMouseEntered()!=null) {
            ((Label) root.lookup("#stateLabel")).setText("Choose column");
            root.lookup("#showBookshelfGroup").setDisable(false);
            root.lookup("#showBookshelfGroup").setVisible(true);
            root.lookup("#showMyBookshelfGroup").setDisable(true);
            root.lookup("#showMyBookshelfGroup").setVisible(false);

            root.lookup("#bookshelfButton").setOnMouseExited(null);
            root.lookup("#bookshelfButton").setOnMouseEntered(null);
        } else {
            ((Label) root.lookup("#stateLabel")).setText("");
            closeBookshelf(actionEvent);
        }
    }

    @FXML
    private void closeBookshelf(ActionEvent actionEvent) {
        root.lookup("#showBookshelfGroup").setDisable(true);
        root.lookup("#showBookshelfGroup").setVisible(false);
        root.lookup("#bookshelfButton").setOnMouseExited(this::showMyBookshelf);
        root.lookup("#bookshelfButton").setOnMouseEntered(this::showMyBookshelf);
        root.lookup("#bookshelfButton").setOpacity(0);
        ((Label) root.lookup("#stateLabel")).setText("Click on the bookshelf \nto insert tiles");
    }

    @FXML
    private void showMyBookshelf(MouseEvent mouseEvent){
        Button button = (Button) mouseEvent.getSource();
        if(mouseEvent.getEventType().getName().equals("MOUSE_ENTERED")){
            button.setOpacity(0.5);
            root.lookup("#showMyBookshelfGroup").setDisable(false);
            root.lookup("#showMyBookshelfGroup").setVisible(true);

        } else {
            button.setOpacity(0);
            root.lookup("#showMyBookshelfGroup").setDisable(true);
            root.lookup("#showMyBookshelfGroup").setVisible(false);
        }
    }

    @FXML
    private void showOtherPlayerBookshelf(MouseEvent mouseEvent){
        Button button = (Button) mouseEvent.getSource();
        if(mouseEvent.getEventType().getName().equals("MOUSE_ENTERED")){
            button.setOpacity(0.5);
            switch (button.getId()){
                case("bookshelfPlayer1Button")->{
                    root.lookup("#showBookshelf1Group").setVisible(true);
                    root.lookup("#showBookshelf1Group").setDisable(false);
                }
                case("bookshelfPlayer2Button")->{
                    root.lookup("#showBookshelf2Group").setVisible(true);
                    root.lookup("#showBookshelf2Group").setDisable(false);
                }
                case("bookshelfPlayer3Button")->{
                    root.lookup("#showBookshelf3Group").setVisible(true);
                    root.lookup("#showBookshelf3Group").setDisable(false);
                }
            }

        } else {
            button.setOpacity(0);
            root.lookup("#showBookshelf1Group").setVisible(false);
            root.lookup("#showBookshelf1Group").setDisable(true);

            root.lookup("#showBookshelf2Group").setVisible(false);
            root.lookup("#showBookshelf2Group").setDisable(true);

            root.lookup("#showBookshelf3Group").setVisible(false);
            root.lookup("#showBookshelf3Group").setDisable(true);
        }
    }

    @FXML
    private void selectColumn(ActionEvent actionEvent){
        if(root.lookup("#tile1Button").getOnMouseEntered()==null|| root.lookup("#tile2Button").getOnMouseEntered()==null|| root.lookup("#tile3Button").getOnMouseEntered()==null) return;
        ((Label) root.lookup("#stateLabel")).setText("Insert tiles");
        Button button = (Button) actionEvent.getSource();
        if(button.getOnMouseEntered()!=null){
            int column=-1;
            switch (button.getId()){
                case("column1Button") -> column=0;
                case("column2Button") -> column=1;
                case("column3Button") -> column=2;
                case("column4Button") -> column=3;
                case("column5Button") -> column=4;
            }
            for(Button columnButton : columnButtons){
                if(!columnButton.equals(button)){
                    columnButton.setOnMouseExited(this::highlightTile);
                    columnButton.setOnMouseEntered(this::highlightTile);

                    columnButton.setOpacity(0);
                }
            }
            if(!checkSingleColumn(column)) {
                ((Label) root.lookup("#stateLabel")).setText("Not enough space!");
                root.lookup("#tile1").setDisable(true);
                root.lookup("#tile2").setDisable(true);
                root.lookup("#tile3").setDisable(true);
                return;
            }

            selectedColumn=column;

            button.setOnMouseExited(null);
            button.setOnMouseEntered(null);

            button.setOpacity(0.5);



            if(((ImageView) root.lookup("#tile3Image")).getImage()!=null) {
                root.lookup("#tile3").setDisable(false);
                root.lookup("#tile3Image").setDisable(false);
            }
            if(((ImageView) root.lookup("#tile2Image")).getImage()!=null){
                root.lookup("#tile2").setDisable(false);
                root.lookup("#tile2Image").setDisable(false);
            }
            root.lookup("#tile1").setDisable(false);
            root.lookup("#tile1Image").setDisable(false);

        } else {
            unselectColumn();
        }
    }

    private void unselectColumn(){
        Button button=null;
        if(selectedColumn<0||selectedColumn>4) return;
        switch (selectedColumn){
            case 0-> button=((Button) root.lookup("#column1Button"));
            case 1-> button=((Button) root.lookup("#column2Button"));
            case 2-> button=((Button) root.lookup("#column3Button"));
            case 3-> button=((Button) root.lookup("#column4Button"));
            case 4-> button=((Button) root.lookup("#column5Button"));
        }
        button.setOnMouseExited(this::highlightTile);
        button.setOnMouseEntered(this::highlightTile);
        button.setOpacity(0);
        ((Label) root.lookup("#stateLabel")).setText("Choose column");

        root.lookup("#tile3").setDisable(true);
        root.lookup("#tile2").setDisable(true);
        root.lookup("#tile1").setDisable(true);
        root.lookup("#tile1Image").setDisable(true);
        root.lookup("#tile2Image").setDisable(true);
        root.lookup("#tile3Image").setDisable(true);

        selectedColumn=-1;
    }
    private boolean checkSingleColumn(int column) {
        int space=0;
        for(int i=0; i<6; i++){
            if(insertBookshelf[i][column].getImage()==null){
               space++;
            }
        }
        int tile=0;
        if(((ImageView) root.lookup("#tile1Image")).getImage()!=null) tile++;
        if(((ImageView) root.lookup("#tile2Image")).getImage()!=null) tile++;
        if(((ImageView) root.lookup("#tile3Image")).getImage()!=null) tile++;

        return space >= tile;
    }

    @FXML
    private void putTile(ActionEvent actionEvent) {
        if(selectedColumn<0||selectedColumn>4) return;
       Button button = (Button) actionEvent.getSource();
        Image image;
        int tileNum=-1;
        switch(button.getId()) {
            case ("tile1Button")-> {
                image=((ImageView) root.lookup("#tile1Image")).getImage();
                tileNum=0;
            }
            case ("tile2Button")-> {
                image=((ImageView) root.lookup("#tile2Image")).getImage();
                tileNum=2;
            }
            case ("tile3Button")-> {
                image=((ImageView) root.lookup("#tile3Image")).getImage();
                tileNum=4;
            }
            default -> image=null;
        }
        if(button.getOnMouseEntered()!=null){
            if(tileNum!=-1){
                sortedCoord.add(coord.get(tileNum));
                sortedCoord.add(coord.get(tileNum+1));
            }

            for (int i=5; i>=0; i--){
                if(insertBookshelf[i][selectedColumn].getImage()==null){
                    insertBookshelf[i][selectedColumn].setImage(image);
                    break;
                }
            }
            button.setOpacity(0.5);
            button.setOnMouseEntered(null);
            button.setOnMouseExited(null);

            for(int i=0; i<5; i++){
                if(i!=selectedColumn) columnButtons.get(i).setDisable(true);
            }

            if(root.lookup("#tile1Button").getOnMouseEntered()==null&&(root.lookup("#tile3Button").getOnMouseEntered()==null||((ImageView) root.lookup("#tile3Image")).getImage()==null)&&(root.lookup("#tile2Button").getOnMouseEntered()==null||((ImageView) root.lookup("#tile2Image")).getImage()==null)){
                root.lookup("#enterColumnButton").setDisable(false);
            }
        } else {
            root.lookup("#enterColumnButton").setDisable(true);
            if(sortedCoord.size()!=0&&sortedCoord.size()!=1){
                sortedCoord.remove(sortedCoord.size()-1);
                sortedCoord.remove(sortedCoord.size()-1);
            }

            for (int i=0; i<6; i++){
                if(insertBookshelf[i][selectedColumn].getImage()!=null){
                    if(!insertBookshelf[i][selectedColumn].getImage().equals(image)) return;

                    insertBookshelf[i][selectedColumn].setImage(null);

                    button.setOpacity(0);
                    button.setOnMouseEntered(this::highlightTile);
                    button.setOnMouseExited(this::highlightTile);
                    if(root.lookup("#tile1Button").getOnMouseEntered()!=null&& root.lookup("#tile3Button").getOnMouseEntered()!=null&& root.lookup("#tile2Button").getOnMouseEntered()!=null){
                        for(int j=0; j<5; j++){
                            if(j!=selectedColumn) columnButtons.get(j).setDisable(false);
                        }
                    }

                    return;
                }
                }
            }
    }



    private void setBoardButton(){
        for (int i=0; i<9; i++){
            for (int j=0; j<9; j++){
                checkButton(i,j);
            }
        }
        root.lookup("#enterTilesButton").setDisable(true);

        for(int h=0; h<9; h++){
            for(int k=0; k<9; k++){
                if(boardButtons[h][k]!=null){
                    if(boardButtons[h][k].getOnMouseEntered()==null){
                        root.lookup("#enterTilesButton").setDisable(false);
                        setButtonsOnSelect(h, k);


                    }
                }
            }
        }
    }

    private boolean checkButton(int i, int j) {
        if (boardButtons[i][j]==null) return false;
        if(boardImages[i][j].getImage()==null) {
            boardButtons[i][j].setDisable(true);
            return false;
        }
        if(i-1<0||i+1>=9||j-1<0||j+1>=9){
            boardButtons[i][j].setDisable(false);
            return true;
        }
        if(boardButtons[i-1][j]==null||boardButtons[i+1][j]==null||boardButtons[i][j+1]==null||boardButtons[i][j-1]==null){
            boardButtons[i][j].setDisable(false);
            return true;
        }
        if(boardImages[i-1][j].getImage()==null||boardImages[i+1][j].getImage()==null||boardImages[i][j+1].getImage()==null||boardImages[i][j-1].getImage()==null){
            boardButtons[i][j].setDisable(false);
            return true;
        }
        boardButtons[i][j].setDisable(true);
        return false;
    }

    private void setButtonsOnSelect(int x, int y){
        List<Integer> list= new ArrayList<>();

        int space= checkColumnSpace();
        for (int i=0; i<9; i++){
            for (int j=0; j<9; j++){
                if(boardButtons[i][j]!=null&&boardButtons[i][j].getOnMouseEntered()!=null) {
                    switch (space) {
                        case (1)-> {
                            if (i != x || j != y) boardButtons[i][j].setDisable(true);
                        }
                        case (2)-> {
                            if ((i != x || (j != y - 1 && j != y && j != y + 1)) && (j != y || (i != x - 1 && i != x + 1))) boardButtons[i][j].setDisable(true);
                        }
                        default-> {
                            if ((i != x || (j != y - 2 && j != y - 1 && j != y && j != y + 1 && j != y + 2)) && (j != y || (i != x - 2 && i != x - 1 && i != x + 1 && i != x + 2))) boardButtons[i][j].setDisable(true);

                        }
                    }

                        if(i==x&&j==y+2&&j<7&&boardButtons[i][y + 1]!=null) {
                            if (boardButtons[i][y + 1].isDisable()) boardButtons[i][j].setDisable(true);
                        } else if (i==x&&j==y-2&&j>1&&boardButtons[i][y-1]!=null) {
                            if(boardButtons[i][y-1].isDisable())boardButtons[i][j].setDisable(true);
                        } else if (j==y&&i==x+2&&i<7&&boardButtons[i+1][j]!=null){
                            if(boardButtons[i+1][j].isDisable()) boardButtons[i][j].setDisable(true);
                        } else if (j==y&&i==x-2&&j<1&&boardButtons[i-1][j]!=null){
                            if(boardButtons[i-1][j].isDisable())boardButtons[i][j].setDisable(true);
                        }

                } else if (boardButtons[i][j] != null && boardButtons[i][j].getOnMouseEntered() == null) {
                    list.add(i);
                    list.add(j);
                }

            }
        }

        if(list.size()==4){
            if(Objects.equals(list.get(0), list.get(2)) && (list.get(1)==list.get(3)-2||list.get(1)==list.get(3)+2)){
                root.lookup("#enterTilesButton").setDisable(true);
            } else if ((list.get(0)==list.get(2)-2||list.get(0)==list.get(2)+2)) {
                root.lookup("#enterTilesButton").setDisable(true);
            }
        }
    }

    private int checkColumnSpace() {
        int max=0;
        for(int i=0; i<5; i++){
            int space=0;
            for(int j=0; j<6; j++){
                if (myBookshelf[j][i].getImage()==null) space++;
            }
            if (space>max) max=space;
            if (max>=3) return 3;
        }
        return max;
    }

    @FXML
    private void selectTile(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        if(button.getOnMouseEntered()!=null){
            button.setOnMouseExited(null);
            button.setOnMouseEntered(null);
            button.setOpacity(0.5);
            root.lookup("#enterTilesButton").setDisable(false);

            for (int i=0; i<9; i++){
                for (int j=0; j<9; j++){
                    if(button.equals(boardButtons[i][j])){
                        setButtonsOnSelect(i,j);
                    }
                }

            }

        } else {
            button.setOpacity(0);
            button.setOnMouseExited(this::highlightTile);
            button.setOnMouseEntered(this::highlightTile);
            setBoardButton();
        }
    }
    private void removeTiles() {
        for (int i=0; i<9; i++){
            for (int j=0; j<9; j++){
                if(boardButtons[i][j]!=null){
                    if(boardButtons[i][j].getOnMouseEntered()==null){
                        boardButtons[i][j].setOpacity(0);
                        boardButtons[i][j].setOnMouseExited(this::highlightTile);
                        boardButtons[i][j].setOnMouseEntered(this::highlightTile);
                        boardImages[i][j].setImage(null);
                    }
                }
            }
        }
    }
    private void ableBoardButton(){
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                if(boardGroups[i][j]!=null){
                    boardGroups[i][j].setDisable(false);
                    boardButtons[i][j].setDisable(false);
                }
            }
        }
    }
    private void disableBoardButton(){
        for (int i=0; i<9; i++){
            for (int j=0; j<9; j++){
                if(boardButtons[i][j]!=null){
                    boardButtons[i][j].setOnMouseExited(this::highlightTile);
                    boardButtons[i][j].setOnMouseExited(this::highlightTile);
                    boardButtons[i][j].setOpacity(0);
                    boardButtons[i][j].setDisable(true);
                }
            }
        }
    }
    //------------------------------------------------------------Button methods-----------------------------------------------------------------
    @FXML
    private void highlightTile(MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        if(mouseEvent.getEventType().getName().equals("MOUSE_ENTERED")){
            button.setOpacity(0.5);
        } else {
            button.setOpacity(0);
        }
    }


    @FXML
    private void showExitGroup(ActionEvent actionEvent) {
        root.lookup("#exitGroup").setVisible(true);
        root.lookup("#exitGroup").setDisable(false);
    }

    @FXML
    private void closeExit(ActionEvent actionEvent) {
        root.lookup("#exitGroup").setVisible(false);
        root.lookup("#exitGroup").setDisable(true);
    }
    //----------------------------------------------------------Initialization methods---------------------------------
   private void setOtherBookshelf(){
        switch (client.getModel().getNumOtherPlayers()){
                case 1->{
                    root.lookup("#bookshelfPlayer3").setDisable(true);
                    root.lookup("#bookshelfPlayer3").setVisible(false);

                    root.lookup("#bookshelfPlayer2").setDisable(true);
                    root.lookup("#bookshelfPlayer2").setVisible(false);
                }

                case 2->{
                    root.lookup("#bookshelfPlayer3").setDisable(true);
                    root.lookup("#bookshelfPlayer3").setVisible(false);
                }
            }
   }

    private void setTimer(){
        timer = new Timer();
        root.lookup("#timeLabel").setVisible(true);
        ((Label) root.lookup("#timeLabel")).setText("59");
        Task task= new Task() ;
        timer.schedule(task, 1000, 1000);
    }


    private void initialize() {


        boardGroups=new Group[][] {  {null,      null,       null,       null,      (Group) root.lookup("#board0x4"),  (Group)root.lookup("#board0x5"),   null,       null,       null},
                {null,      null,       null,      (Group)root.lookup("#board1x3"),  (Group)root.lookup("#board1x4"),  (Group)root.lookup("#board1x5"),   null,       null,       null},
                {null,      null,      (Group) root.lookup("#board2x2"),  (Group)root.lookup("#board2x3"),  (Group)root.lookup("#board2x4"),  (Group)root.lookup("#board2x5"),  (Group)root.lookup("#board2x6"),   null,       null},
                {(Group)root.lookup("#board3x0"), (Group)root.lookup("#board3x1"), (Group)root.lookup("#board3x2"),  (Group)root.lookup("#board3x3"),  (Group)root.lookup("#board3x4"),  (Group)root.lookup("#board3x5"),  (Group)root.lookup("#board3x6"),  (Group)root.lookup("#board3x7"),   null},
                {(Group)root.lookup("#board4x0"), (Group)root.lookup("#board4x1"),  (Group)root.lookup("#board4x2"),  (Group)root.lookup("#board4x3"),  (Group)root.lookup("#board4x4"),  (Group)root.lookup("#board4x5"),  (Group)root.lookup("#board4x6"),  (Group)root.lookup("#board4x7"),  (Group)root.lookup("#board4x8")},
                {null,     (Group)root.lookup("#board5x1"),  (Group)root.lookup("#board5x2"),  (Group)root.lookup("#board5x3"),  (Group)root.lookup("#board5x4"),  (Group)root.lookup("#board5x5"),  (Group)root.lookup("#board5x6"),  (Group)root.lookup("#board5x7"),  (Group)root.lookup("#board5x8")},
                {null,      null,      (Group)root.lookup("#board6x2"),  (Group)root.lookup("#board6x3"),  (Group)root.lookup("#board6x4"),  (Group)root.lookup("#board6x5"),  (Group)root.lookup("#board6x6"),   null,       null},
                {null,      null,       null,      (Group)root.lookup("#board7x3"),  (Group)root.lookup("#board7x4"),  (Group)root.lookup("#board7x5"),   null,       null,       null},
                {null,      null,       null,      (Group)root.lookup("#board8x3"),  (Group)root.lookup("#board8x4"),   null,       null,       null,       null}
        };

        boardButtons= new Button[][]{  {null,            null,             null,           null,           (Button) root.lookup("#boardButton0x4"), (Button) root.lookup("#boardButton0x5"), null,           null,           null},
                {null,            null,             null,           (Button) root.lookup("#boardButton1x3"), (Button) root.lookup("#boardButton1x4"), (Button) root.lookup("#boardButton1x5"), null,           null,           null},
                {null,            null,             (Button) root.lookup("#boardButton2x2"), (Button) root.lookup("#boardButton2x3"), (Button) root.lookup("#boardButton2x4"), (Button) root.lookup("#boardButton2x5"), (Button) root.lookup("#boardButton2x6"), null,           null},
                {(Button) root.lookup("#boardButton3x0"),  (Button) root.lookup("#boardButton3x1"),   (Button) root.lookup("#boardButton3x2"), (Button) root.lookup("#boardButton3x3"), (Button) root.lookup("#boardButton3x4"), (Button) root.lookup("#boardButton3x5"), (Button) root.lookup("#boardButton3x6"), (Button) root.lookup("#boardButton3x7"), null},
                {(Button) root.lookup("#boardButton4x0"),  (Button) root.lookup("#boardButton4x1"),   (Button) root.lookup("#boardButton4x2"), (Button) root.lookup("#boardButton4x3"), (Button) root.lookup("#boardButton4x4"), (Button) root.lookup("#boardButton4x5"), (Button) root.lookup("#boardButton4x6"), (Button) root.lookup("#boardButton4x7"), (Button) root.lookup("#boardButton4x8")},
                {null,            (Button) root.lookup("#boardButton5x1"),   (Button) root.lookup("#boardButton5x2"), (Button) root.lookup("#boardButton5x3"), (Button) root.lookup("#boardButton5x4"), (Button) root.lookup("#boardButton5x5"), (Button) root.lookup("#boardButton5x6"), (Button) root.lookup("#boardButton5x7"), (Button) root.lookup("#boardButton5x8")},
                {null,            null,             (Button) root.lookup("#boardButton6x2"), (Button) root.lookup("#boardButton6x3"), (Button) root.lookup("#boardButton6x4"), (Button) root.lookup("#boardButton6x5"), (Button) root.lookup("#boardButton6x6"), null,           null},
                {null,            null,             null,           (Button) root.lookup("#boardButton7x3"), (Button) root.lookup("#boardButton7x4"), (Button) root.lookup("#boardButton7x5"), null,           null,           null},
                {null,            null,             null,           (Button) root.lookup("#boardButton8x3"), (Button) root.lookup("#boardButton8x4"), null,           null,           null,           null}
        };

        boardImages = new ImageView[][]{  {null,        null,       null,       null,       (ImageView) root.lookup("#boardImage0x4"),      (ImageView) root.lookup("#boardImage0x5"),      null,       null,       null},
                {null,            null,             null,           (ImageView) root.lookup("#boardImage1x3"), (ImageView) root.lookup("#boardImage1x4"), (ImageView) root.lookup("#boardImage1x5"),    null,           null,           null},
                {null,            null,             (ImageView) root.lookup("#boardImage2x2"),  (ImageView) root.lookup("#boardImage2x3"), (ImageView) root.lookup("#boardImage2x4"), (ImageView) root.lookup("#boardImage2x5"),    (ImageView) root.lookup("#boardImage2x6"),  null,           null},
                {(ImageView) root.lookup("#boardImage3x0"),   (ImageView) root.lookup("#boardImage3x1"),    (ImageView) root.lookup("#boardImage3x2"),  (ImageView) root.lookup("#boardImage3x3"), (ImageView) root.lookup("#boardImage3x4"), (ImageView) root.lookup("#boardImage3x5"),    (ImageView) root.lookup("#boardImage3x6"),  (ImageView) root.lookup("#boardImage3x7"),  null},
                {(ImageView) root.lookup("#boardImage4x0"),   (ImageView) root.lookup("#boardImage4x1"),    (ImageView) root.lookup("#boardImage4x2"),  (ImageView) root.lookup("#boardImage4x3"), (ImageView) root.lookup("#boardImage4x4"), (ImageView) root.lookup("#boardImage4x5"),    (ImageView) root.lookup("#boardImage4x6"),  (ImageView) root.lookup("#boardImage4x7"), (ImageView) root.lookup("#boardImage4x8")},
                {null,            (ImageView) root.lookup("#boardImage5x1"),    (ImageView) root.lookup("#boardImage5x2"),  (ImageView) root.lookup("#boardImage5x3"), (ImageView) root.lookup("#boardImage5x4"), (ImageView) root.lookup("#boardImage5x5"),    (ImageView) root.lookup("#boardImage5x6"),  (ImageView) root.lookup("#boardImage5x7"), (ImageView) root.lookup("#boardImage5x8")},
                {null,            null,             (ImageView) root.lookup("#boardImage6x2"),  (ImageView) root.lookup("#boardImage6x3"), (ImageView) root.lookup("#boardImage6x4"), (ImageView) root.lookup("#boardImage6x5"),    (ImageView) root.lookup("#boardImage6x6"),  null,           null},
                {null,            null,             null,           (ImageView) root.lookup("#boardImage7x3"), (ImageView) root.lookup("#boardImage7x4"), (ImageView) root.lookup("#boardImage7x5"),    null,           null,           null},
                {null,            null,             null,           (ImageView) root.lookup("#boardImage8x3"), (ImageView) root.lookup("#boardImage8x4"), null,             null,           null,           null}
        };
        myBookshelf=new ImageView[][] {        {(ImageView) root.lookup("#bookshelf0x0"), (ImageView) root.lookup("#bookshelf0x1"), (ImageView) root.lookup("#bookshelf0x2"), (ImageView) root.lookup("#bookshelf0x3"), (ImageView) root.lookup("#bookshelf0x4")},
                {(ImageView) root.lookup("#bookshelf1x0"), (ImageView) root.lookup("#bookshelf1x1"),(ImageView) root.lookup("#bookshelf1x2"), (ImageView) root.lookup("#bookshelf1x3"), (ImageView) root.lookup("#bookshelf1x4")},
                {(ImageView) root.lookup("#bookshelf2x0"), (ImageView) root.lookup("#bookshelf2x1"),(ImageView) root.lookup("#bookshelf2x2"), (ImageView) root.lookup("#bookshelf2x3"), (ImageView) root.lookup("#bookshelf2x4")},
                {(ImageView) root.lookup("#bookshelf3x0"), (ImageView) root.lookup("#bookshelf3x1"),(ImageView) root.lookup("#bookshelf3x2"), (ImageView) root.lookup("#bookshelf3x3"), (ImageView) root.lookup("#bookshelf3x4")},
                {(ImageView) root.lookup("#bookshelf4x0"), (ImageView) root.lookup("#bookshelf4x1"),(ImageView) root.lookup("#bookshelf4x2"), (ImageView) root.lookup("#bookshelf4x3"), (ImageView) root.lookup("#bookshelf4x4")},
                {(ImageView) root.lookup("#bookshelf5x0"), (ImageView) root.lookup("#bookshelf5x1"),(ImageView) root.lookup("#bookshelf5x2"), (ImageView) root.lookup("#bookshelf5x3"), (ImageView) root.lookup("#bookshelf5x4")},
        };

        insertBookshelf= new ImageView[][]{        {(ImageView) root.lookup("#insert0x0"), (ImageView) root.lookup("#insert0x1"), (ImageView) root.lookup("#insert0x2"), (ImageView) root.lookup("#insert0x3"), (ImageView) root.lookup("#insert0x4")},
                {(ImageView) root.lookup("#insert1x0"), (ImageView) root.lookup("#insert1x1"),(ImageView) root.lookup("#insert1x2"), (ImageView) root.lookup("#insert1x3"), (ImageView) root.lookup("#insert1x4")},
                {(ImageView) root.lookup("#insert2x0"), (ImageView) root.lookup("#insert2x1"),(ImageView) root.lookup("#insert2x2"), (ImageView) root.lookup("#insert2x3"), (ImageView) root.lookup("#insert2x4")},
                {(ImageView) root.lookup("#insert3x0"), (ImageView) root.lookup("#insert3x1"),(ImageView) root.lookup("#insert3x2"), (ImageView) root.lookup("#insert3x3"), (ImageView) root.lookup("#insert3x4")},
                {(ImageView) root.lookup("#insert4x0"), (ImageView) root.lookup("#insert4x1"),(ImageView) root.lookup("#insert4x2"), (ImageView) root.lookup("#insert4x3"), (ImageView) root.lookup("#insert4x4")},
                {(ImageView) root.lookup("#insert5x0"), (ImageView) root.lookup("#insert5x1"),(ImageView) root.lookup("#insert5x2"), (ImageView) root.lookup("#insert5x3"), (ImageView) root.lookup("#insert5x4")},
        };

        bookshelf1=new ImageView[][] {        {(ImageView) root.lookup("#bookshelf1_0x0"), (ImageView) root.lookup("#bookshelf1_0x1"), (ImageView) root.lookup("#bookshelf1_0x2"), (ImageView) root.lookup("#bookshelf1_0x3"), (ImageView) root.lookup("#bookshelf1_0x4")},
                {(ImageView) root.lookup("#bookshelf1_1x0"), (ImageView) root.lookup("#bookshelf1_1x1"),(ImageView) root.lookup("#bookshelf1_1x2"), (ImageView) root.lookup("#bookshelf1_1x3"), (ImageView) root.lookup("#bookshelf1_1x4")},
                {(ImageView) root.lookup("#bookshelf1_2x0"), (ImageView) root.lookup("#bookshelf1_2x1"),(ImageView) root.lookup("#bookshelf1_2x2"), (ImageView) root.lookup("#bookshelf1_2x3"), (ImageView) root.lookup("#bookshelf1_2x4")},
                {(ImageView) root.lookup("#bookshelf1_3x0"), (ImageView) root.lookup("#bookshelf1_3x1"),(ImageView) root.lookup("#bookshelf1_3x2"), (ImageView) root.lookup("#bookshelf1_3x3"), (ImageView) root.lookup("#bookshelf1_3x4")},
                {(ImageView) root.lookup("#bookshelf1_4x0"), (ImageView) root.lookup("#bookshelf1_4x1"),(ImageView) root.lookup("#bookshelf1_4x2"), (ImageView) root.lookup("#bookshelf1_4x3"), (ImageView) root.lookup("#bookshelf1_4x4")},
                {(ImageView) root.lookup("#bookshelf1_5x0"), (ImageView) root.lookup("#bookshelf1_5x1"),(ImageView) root.lookup("#bookshelf1_5x2"), (ImageView) root.lookup("#bookshelf1_5x3"), (ImageView) root.lookup("#bookshelf1_5x4")},
        };

        bookshelf2=new ImageView[][] {        {(ImageView) root.lookup("#bookshelf2_0x0"), (ImageView) root.lookup("#bookshelf2_0x1"), (ImageView) root.lookup("#bookshelf2_0x2"), (ImageView) root.lookup("#bookshelf2_0x3"), (ImageView) root.lookup("#bookshelf2_0x4")},
                {(ImageView) root.lookup("#bookshelf2_1x0"), (ImageView) root.lookup("#bookshelf2_1x1"),(ImageView) root.lookup("#bookshelf2_1x2"), (ImageView) root.lookup("#bookshelf2_1x3"), (ImageView) root.lookup("#bookshelf2_1x4")},
                {(ImageView) root.lookup("#bookshelf2_2x0"), (ImageView) root.lookup("#bookshelf2_2x1"),(ImageView) root.lookup("#bookshelf2_2x2"), (ImageView) root.lookup("#bookshelf2_2x3"), (ImageView) root.lookup("#bookshelf2_2x4")},
                {(ImageView) root.lookup("#bookshelf2_3x0"), (ImageView) root.lookup("#bookshelf2_3x1"),(ImageView) root.lookup("#bookshelf2_3x2"), (ImageView) root.lookup("#bookshelf2_3x3"), (ImageView) root.lookup("#bookshelf2_3x4")},
                {(ImageView) root.lookup("#bookshelf2_4x0"), (ImageView) root.lookup("#bookshelf2_4x1"),(ImageView) root.lookup("#bookshelf2_4x2"), (ImageView) root.lookup("#bookshelf2_4x3"), (ImageView) root.lookup("#bookshelf2_4x4")},
                {(ImageView) root.lookup("#bookshelf2_5x0"), (ImageView) root.lookup("#bookshelf2_5x1"),(ImageView) root.lookup("#bookshelf2_5x2"), (ImageView) root.lookup("#bookshelf2_5x3"), (ImageView) root.lookup("#bookshelf2_5x4")},
        };
        bookshelf3=new ImageView[][] {        {(ImageView) root.lookup("#bookshelf3_0x0"), (ImageView) root.lookup("#bookshelf3_0x1"), (ImageView) root.lookup("#bookshelf3_0x2"), (ImageView) root.lookup("#bookshelf3_0x3"), (ImageView) root.lookup("#bookshelf3_0x4")},
                {(ImageView) root.lookup("#bookshelf3_1x0"), (ImageView) root.lookup("#bookshelf3_1x1"),(ImageView) root.lookup("#bookshelf3_1x2"), (ImageView) root.lookup("#bookshelf3_1x3"), (ImageView) root.lookup("#bookshelf3_1x4")},
                {(ImageView) root.lookup("#bookshelf3_2x0"), (ImageView) root.lookup("#bookshelf3_2x1"),(ImageView) root.lookup("#bookshelf3_2x2"), (ImageView) root.lookup("#bookshelf3_2x3"), (ImageView) root.lookup("#bookshelf3_2x4")},
                {(ImageView) root.lookup("#bookshelf3_3x0"), (ImageView) root.lookup("#bookshelf3_3x1"),(ImageView) root.lookup("#bookshelf3_3x2"), (ImageView) root.lookup("#bookshelf3_3x3"), (ImageView) root.lookup("#bookshelf3_3x4")},
                {(ImageView) root.lookup("#bookshelf3_4x0"), (ImageView) root.lookup("#bookshelf3_4x1"),(ImageView) root.lookup("#bookshelf3_4x2"), (ImageView) root.lookup("#bookshelf3_4x3"), (ImageView) root.lookup("#bookshelf3_4x4")},
                {(ImageView) root.lookup("#bookshelf3_5x0"), (ImageView) root.lookup("#bookshelf3_5x1"),(ImageView) root.lookup("#bookshelf3_5x2"), (ImageView) root.lookup("#bookshelf3_5x3"), (ImageView) root.lookup("#bookshelf3_5x4")},
        };

        showMyBookshelf=new ImageView[][] {        {(ImageView) root.lookup("#showMyBookshelf_0x0"), (ImageView) root.lookup("#showMyBookshelf_0x1"), (ImageView) root.lookup("#showMyBookshelf_0x2"), (ImageView) root.lookup("#showMyBookshelf_0x3"), (ImageView) root.lookup("#showMyBookshelf_0x4")},
                {(ImageView) root.lookup("#showMyBookshelf_1x0"), (ImageView) root.lookup("#showMyBookshelf_1x1"),(ImageView) root.lookup("#showMyBookshelf_1x2"), (ImageView) root.lookup("#showMyBookshelf_1x3"), (ImageView) root.lookup("#showMyBookshelf_1x4")},
                {(ImageView) root.lookup("#showMyBookshelf_2x0"), (ImageView) root.lookup("#showMyBookshelf_2x1"),(ImageView) root.lookup("#showMyBookshelf_2x2"), (ImageView) root.lookup("#showMyBookshelf_2x3"), (ImageView) root.lookup("#showMyBookshelf_2x4")},
                {(ImageView) root.lookup("#showMyBookshelf_3x0"), (ImageView) root.lookup("#showMyBookshelf_3x1"),(ImageView) root.lookup("#showMyBookshelf_3x2"), (ImageView) root.lookup("#showMyBookshelf_3x3"), (ImageView) root.lookup("#showMyBookshelf_3x4")},
                {(ImageView) root.lookup("#showMyBookshelf_4x0"), (ImageView) root.lookup("#showMyBookshelf_4x1"),(ImageView) root.lookup("#showMyBookshelf_4x2"), (ImageView) root.lookup("#showMyBookshelf_4x3"), (ImageView) root.lookup("#showMyBookshelf_4x4")},
                {(ImageView) root.lookup("#showMyBookshelf_5x0"), (ImageView) root.lookup("#showMyBookshelf_5x1"),(ImageView) root.lookup("#showMyBookshelf_5x2"), (ImageView) root.lookup("#showMyBookshelf_5x3"), (ImageView) root.lookup("#showMyBookshelf_5x4")},
        };

        showBookshelf1=new ImageView[][] {        {(ImageView) root.lookup("#showBookshelf1_0x0"), (ImageView) root.lookup("#showBookshelf1_0x1"), (ImageView) root.lookup("#showBookshelf1_0x2"), (ImageView) root.lookup("#showBookshelf1_0x3"), (ImageView) root.lookup("#showBookshelf1_0x4")},
                {(ImageView) root.lookup("#showBookshelf1_1x0"), (ImageView) root.lookup("#showBookshelf1_1x1"),(ImageView) root.lookup("#showBookshelf1_1x2"), (ImageView) root.lookup("#showBookshelf1_1x3"), (ImageView) root.lookup("#showBookshelf1_1x4")},
                {(ImageView) root.lookup("#showBookshelf1_2x0"), (ImageView) root.lookup("#showBookshelf1_2x1"),(ImageView) root.lookup("#showBookshelf1_2x2"), (ImageView) root.lookup("#showBookshelf1_2x3"), (ImageView) root.lookup("#showBookshelf1_2x4")},
                {(ImageView) root.lookup("#showBookshelf1_3x0"), (ImageView) root.lookup("#showBookshelf1_3x1"),(ImageView) root.lookup("#showBookshelf1_3x2"), (ImageView) root.lookup("#showBookshelf1_3x3"), (ImageView) root.lookup("#showBookshelf1_3x4")},
                {(ImageView) root.lookup("#showBookshelf1_4x0"), (ImageView) root.lookup("#showBookshelf1_4x1"),(ImageView) root.lookup("#showBookshelf1_4x2"), (ImageView) root.lookup("#showBookshelf1_4x3"), (ImageView) root.lookup("#showBookshelf1_4x4")},
                {(ImageView) root.lookup("#showBookshelf1_5x0"), (ImageView) root.lookup("#showBookshelf1_5x1"),(ImageView) root.lookup("#showBookshelf1_5x2"), (ImageView) root.lookup("#showBookshelf1_5x3"), (ImageView) root.lookup("#showBookshelf1_5x4")},
        };

        showBookshelf2=new ImageView[][] {        {(ImageView) root.lookup("#showBookshelf2_0x0"), (ImageView) root.lookup("#showBookshelf2_0x1"), (ImageView) root.lookup("#showBookshelf2_0x2"), (ImageView) root.lookup("#showBookshelf2_0x3"), (ImageView) root.lookup("#showBookshelf2_0x4")},
                {(ImageView) root.lookup("#showBookshelf2_1x0"), (ImageView) root.lookup("#showBookshelf2_1x1"),(ImageView) root.lookup("#showBookshelf2_1x2"), (ImageView) root.lookup("#showBookshelf2_1x3"), (ImageView) root.lookup("#showBookshelf2_1x4")},
                {(ImageView) root.lookup("#showBookshelf2_2x0"), (ImageView) root.lookup("#showBookshelf2_2x1"),(ImageView) root.lookup("#showBookshelf2_2x2"), (ImageView) root.lookup("#showBookshelf2_2x3"), (ImageView) root.lookup("#showBookshelf2_2x4")},
                {(ImageView) root.lookup("#showBookshelf2_3x0"), (ImageView) root.lookup("#showBookshelf2_3x1"),(ImageView) root.lookup("#showBookshelf2_3x2"), (ImageView) root.lookup("#showBookshelf2_3x3"), (ImageView) root.lookup("#showBookshelf2_3x4")},
                {(ImageView) root.lookup("#showBookshelf2_4x0"), (ImageView) root.lookup("#showBookshelf2_4x1"),(ImageView) root.lookup("#showBookshelf2_4x2"), (ImageView) root.lookup("#showBookshelf2_4x3"), (ImageView) root.lookup("#showBookshelf2_4x4")},
                {(ImageView) root.lookup("#showBookshelf2_5x0"), (ImageView) root.lookup("#showBookshelf2_5x1"),(ImageView) root.lookup("#showBookshelf2_5x2"), (ImageView) root.lookup("#showBookshelf2_5x3"), (ImageView) root.lookup("#showBookshelf2_5x4")},
        };

        showBookshelf3=new ImageView[][] {        {(ImageView) root.lookup("#showBookshelf3_0x0"), (ImageView) root.lookup("#showBookshelf3_0x1"), (ImageView) root.lookup("#showBookshelf3_0x2"), (ImageView) root.lookup("#showBookshelf3_0x3"), (ImageView) root.lookup("#showBookshelf3_0x4")},
                {(ImageView) root.lookup("#showBookshelf3_1x0"), (ImageView) root.lookup("#showBookshelf3_1x1"),(ImageView) root.lookup("#showBookshelf3_1x2"), (ImageView) root.lookup("#showBookshelf3_1x3"), (ImageView) root.lookup("#showBookshelf3_1x4")},
                {(ImageView) root.lookup("#showBookshelf3_2x0"), (ImageView) root.lookup("#showBookshelf3_2x1"),(ImageView) root.lookup("#showBookshelf3_2x2"), (ImageView) root.lookup("#showBookshelf3_2x3"), (ImageView) root.lookup("#showBookshelf3_2x4")},
                {(ImageView) root.lookup("#showBookshelf3_3x0"), (ImageView) root.lookup("#showBookshelf3_3x1"),(ImageView) root.lookup("#showBookshelf3_3x2"), (ImageView) root.lookup("#showBookshelf3_3x3"), (ImageView) root.lookup("#showBookshelf3_3x4")},
                {(ImageView) root.lookup("#showBookshelf3_4x0"), (ImageView) root.lookup("#showBookshelf3_4x1"),(ImageView) root.lookup("#showBookshelf3_4x2"), (ImageView) root.lookup("#showBookshelf3_4x3"), (ImageView) root.lookup("#showBookshelf3_4x4")},
                {(ImageView) root.lookup("#showBookshelf3_5x0"), (ImageView) root.lookup("#showBookshelf3_5x1"),(ImageView) root.lookup("#showBookshelf3_5x2"), (ImageView) root.lookup("#showBookshelf3_5x3"), (ImageView) root.lookup("#showBookshelf3_5x4")},
        };

        columnButtons= new ArrayList<>();
        columnButtons.add((Button) root.lookup("#column1Button"));
        columnButtons.add((Button) root.lookup("#column2Button"));
        columnButtons.add((Button)root.lookup("#column3Button"));
        columnButtons.add((Button) root.lookup("#column4Button"));
        columnButtons.add((Button) root.lookup("#column5Button"));
    }

    public void setRoot(Parent root) {
        this.root=root;
    }


    //--------------------------------------------------------Private class--------------------------------------------


   private class Task extends TimerTask{
       @Override
       public void run() {
               int n=Integer.parseInt(((Label) root.lookup("#timeLabel")).getText())-1;
               if (n==-1){
                   Platform.runLater(GameController.this::passTurn);
               }else{
                   Platform.runLater(()-> ((Label) root.lookup("#timeLabel")).setText(Integer.toString(n)));
               }
       }
   }
    private static class TileImages {
        List<String> greenTiles;
        List<String> whiteTiles;
        List<String> blueTiles;
        List<String> lightblueTiles;
        List<String> pinkTiles;
        List<String> yellowTiles;

        public TileImages() {
            greenTiles=new ArrayList<>();
            greenTiles.add(getClass().getResource("/view/17_MyShelfie_BGA/item_tiles/Gatti1.1.png").toString());
            greenTiles.add(getClass().getResource("/view/17_MyShelfie_BGA/item_tiles/Gatti1.2.png").toString());
            greenTiles.add(getClass().getResource("/view/17_MyShelfie_BGA/item_tiles/Gatti1.3.png").toString());

            whiteTiles=new ArrayList<>();
            whiteTiles.add(getClass().getResource("/view/17_MyShelfie_BGA/item_tiles/Libri1.1.png").toString());
            whiteTiles.add(getClass().getResource("/view/17_MyShelfie_BGA/item_tiles/Libri1.2.png").toString());
            whiteTiles.add(getClass().getResource("/view/17_MyShelfie_BGA/item_tiles/Libri1.3.png").toString());

            blueTiles=new ArrayList<>();
            blueTiles.add(getClass().getResource("/view/17_MyShelfie_BGA/item_tiles/Cornici1.1.png").toString());
            blueTiles.add(getClass().getResource("/view/17_MyShelfie_BGA/item_tiles/Cornici1.2.png").toString());
            blueTiles.add(getClass().getResource("/view/17_MyShelfie_BGA/item_tiles/Cornici1.3.png").toString());

            lightblueTiles=new ArrayList<>();
            lightblueTiles.add(getClass().getResource("/view/17_MyShelfie_BGA/item_tiles/Trofei1.1.png").toString());
            lightblueTiles.add(getClass().getResource("/view/17_MyShelfie_BGA/item_tiles/Trofei1.2.png").toString());
            lightblueTiles.add(getClass().getResource("/view/17_MyShelfie_BGA/item_tiles/Trofei1.3.png").toString());

            pinkTiles=new ArrayList<>();
            pinkTiles.add(getClass().getResource("/view/17_MyShelfie_BGA/item_tiles/Piante1.1.png").toString());
            pinkTiles.add(getClass().getResource("/view/17_MyShelfie_BGA/item_tiles/Piante1.2.png").toString());
            pinkTiles.add(getClass().getResource("/view/17_MyShelfie_BGA/item_tiles/Piante1.3.png").toString());

            yellowTiles=new ArrayList<>();
            yellowTiles.add(getClass().getResource("/view/17_MyShelfie_BGA/item_tiles/Giochi1.1.png").toString());
            yellowTiles.add(getClass().getResource("/view/17_MyShelfie_BGA/item_tiles/Giochi1.2.png").toString());
            yellowTiles.add(getClass().getResource("/view/17_MyShelfie_BGA/item_tiles/Giochi1.3.png").toString());

        }

        public String getImage(Tile tile){
            switch (tile){
                case GREEN -> {
                    greenTiles.add(greenTiles.get(0));
                    greenTiles.remove(0);
                    return greenTiles.get(2);
                }
                case WHITE -> {
                    whiteTiles.add(whiteTiles.get(0));
                    whiteTiles.remove(0);
                    return whiteTiles.get(2);
                }
                case YELLOW -> {
                    yellowTiles.add(yellowTiles.get(0));
                    yellowTiles.remove(0);
                    return yellowTiles.get(2);
                }
                case BLUE -> {
                    blueTiles.add(blueTiles.get(0));
                    blueTiles.remove(0);
                    return blueTiles.get(2);
                }
                case LIGHTBLUE -> {
                    lightblueTiles.add(lightblueTiles.get(0));
                    lightblueTiles.remove(0);
                    return lightblueTiles.get(2);
                }
                case PINK -> {
                    pinkTiles.add(pinkTiles.get(0));
                    pinkTiles.remove(0);
                    return pinkTiles.get(2);
                }
            }
            return null;
        }
    }

  private String getPGCImage(int i){
        switch (i){
            case 1 -> {
                return getClass().getResource("/view/17_MyShelfie_BGA/personal_goal_cards/Personal_Goals.png").toString();
            }
            case 2 -> {
                return getClass().getResource("/view/17_MyShelfie_BGA/personal_goal_cards/Personal_Goals2.png").toString();
            }
            case 3 -> {
                return getClass().getResource("/view/17_MyShelfie_BGA/personal_goal_cards/Personal_Goals3.png").toString();
            }
            case 4 -> {
                return getClass().getResource("/view/17_MyShelfie_BGA/personal_goal_cards/Personal_Goals4.png").toString();
            }
            case 5 -> {
                return getClass().getResource("/view/17_MyShelfie_BGA/personal_goal_cards/Personal_Goals5.png").toString();
            }
            case 6 -> {
                return getClass().getResource("/view/17_MyShelfie_BGA/personal_goal_cards/Personal_Goals6.png").toString();
            }
            case 7 -> {
                return getClass().getResource("/view/17_MyShelfie_BGA/personal_goal_cards/Personal_Goals7.png").toString();
            }
            case 8 -> {
                return getClass().getResource("/view/17_MyShelfie_BGA/personal_goal_cards/Personal_Goals8.png").toString();
            }
            case 9 -> {
                return getClass().getResource("/view/17_MyShelfie_BGA/personal_goal_cards/Personal_Goals9.png").toString();
            }
            case 10-> {
                return getClass().getResource("/view/17_MyShelfie_BGA/personal_goal_cards/Personal_Goals10.png").toString();
            }
            case 11 -> {
                return getClass().getResource("/view/17_MyShelfie_BGA/personal_goal_cards/Personal_Goals11.png").toString();
            }
            case 12 -> {
                return getClass().getResource("/view/17_MyShelfie_BGA/personal_goal_cards/Personal_Goals12.png").toString();
            }
        }
        return null;
  }

  public String getCGCImage(int i){
        switch (i){
            case 4->{
                return getClass().getResource("/view/17_MyShelfie_BGA/common_goal_cards/1.jpg").toString();
            }
            case 9->{
                return getClass().getResource("/view/17_MyShelfie_BGA/common_goal_cards/2.jpg").toString();
            }
            case 3->{
                return getClass().getResource("/view/17_MyShelfie_BGA/common_goal_cards/3.jpg").toString();
            }
            case 1->{
                return getClass().getResource("/view/17_MyShelfie_BGA/common_goal_cards/4.jpg").toString();
            }
            case 0->{
                return getClass().getResource("/view/17_MyShelfie_BGA/common_goal_cards/4.jpg").toString();
            }
            case 5->{
                return getClass().getResource("/view/17_MyShelfie_BGA/common_goal_cards/5.jpg").toString();
            }
            case 10->{
                return getClass().getResource("/view/17_MyShelfie_BGA/common_goal_cards/6.jpg").toString();
            }
            case 8->{
                return getClass().getResource("/view/17_MyShelfie_BGA/common_goal_cards/7.jpg").toString();
            }
            case 2->{
                return getClass().getResource("/view/17_MyShelfie_BGA/common_goal_cards/8.jpg").toString();
            }
            case 6->{
                return getClass().getResource("/view/17_MyShelfie_BGA/common_goal_cards/9.jpg").toString();
            }
            case 11->{
                return getClass().getResource("/view/17_MyShelfie_BGA/common_goal_cards/10.jpg").toString();
            }
            case 7->{
                return getClass().getResource("/view/17_MyShelfie_BGA/common_goal_cards/11.jpg").toString();
            }
            case 12->{
                return getClass().getResource("/view/17_MyShelfie_BGA/common_goal_cards/12.jpg").toString();
            }
        }
        return null;
  }

}
