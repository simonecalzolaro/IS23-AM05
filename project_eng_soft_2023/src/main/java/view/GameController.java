package view;



import client.Matrix;
import client.Tile;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import myShelfieException.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static javafx.application.Platform.exit;

public class GameController extends GUIController {
    @FXML
    public Label scoreLabel;
    public Group bookshelfPlayer1, bookshelfPlayer2, bookshelfPlayer3;
    @FXML
    private  Button tile1Button, tile2Button, tile3Button;
    @FXML
    private Group showBookshelfGroup;
    @FXML
    private Group exitGroup;
    @FXML
    private Button enterColumnButton;
    @FXML
    private Button bookshelfButton;
    @FXML
    private Button column1Button, column2Button, column3Button, column4Button, column5Button;
    @FXML
    private Button enterTilesButton;
    @FXML
    private Group showBookshelf1Group, showBookshelf2Group, showBookshelf3Group;
    @FXML
    private Group showMyBookshelfGroup;
    @FXML
    private Group tile3, tile2, tile1;
    @FXML
    private ImageView tile3Image, tile2Image, tile1Image;
    @FXML
    private ImageView insert0x0, insert0x1, insert0x2, insert0x3, insert0x4, insert1x0, insert1x1, insert1x2, insert1x3, insert1x4, insert2x0, insert2x1, insert2x2, insert2x3, insert2x4, insert3x0, insert3x1, insert3x2, insert3x3, insert3x4, insert4x0, insert4x1, insert4x2, insert4x3, insert4x4, insert5x0, insert5x1, insert5x2, insert5x3, insert5x4;
    @FXML
    private Label stateLabel;
    private ImageView[][] insertBookshelf;
    private List<Button> columnButtons;
    @FXML
    private Group board3x0, board4x0, board3x1, board4x1, board5x1, board3x2, board4x2, board5x2, board6x2, board2x2, board1x3, board2x3, board2x4, board3x3, board4x3, board5x3, board6x3, board7x3, board8x3, board1x4, board0x4, board3x4, board4x4, board5x4, board6x4, board7x4, board8x4, board0x5, board1x5, board2x5, board3x5, board4x5, board5x5, board6x5, board7x5, board2x6, board3x6, board4x6, board5x6, board6x6, board3x7, board5x7, board4x8, board5x8, board4x7;

    private Group[][] boardGroups;
    @FXML
    private ImageView boardImage3x0, boardImage4x0, boardImage3x1, boardImage4x1, boardImage5x1, boardImage3x2, boardImage4x2, boardImage5x2, boardImage6x2, boardImage2x2, boardImage1x3, boardImage2x3, boardImage2x4, boardImage3x3, boardImage4x3, boardImage5x3, boardImage6x3, boardImage7x3, boardImage8x3, boardImage1x4, boardImage0x4, boardImage3x4, boardImage4x4, boardImage5x4, boardImage6x4, boardImage7x4, boardImage8x4, boardImage0x5, boardImage1x5, boardImage2x5, boardImage3x5, boardImage4x5, boardImage5x5, boardImage6x5, boardImage7x5, boardImage2x6, boardImage3x6, boardImage4x6, boardImage5x6, boardImage6x6, boardImage3x7, boardImage5x7, boardImage4x8, boardImage5x8, boardImage4x7;
    private ImageView[][]boardImages;
    @FXML
    private Button boardButton3x0, boardButton4x0, boardButton3x1, boardButton4x1, boardButton5x1, boardButton3x2, boardButton4x2, boardButton5x2, boardButton6x2, boardButton2x2, boardButton1x3, boardButton2x3, boardButton2x4, boardButton3x3, boardButton4x3, boardButton5x3, boardButton6x3, boardButton7x3, boardButton8x3, boardButton1x4, boardButton0x4, boardButton3x4, boardButton4x4, boardButton5x4, boardButton6x4, boardButton7x4, boardButton8x4, boardButton0x5, boardButton1x5, boardButton2x5, boardButton3x5, boardButton4x5, boardButton5x5, boardButton6x5, boardButton7x5, boardButton2x6, boardButton3x6, boardButton4x6, boardButton5x6, boardButton6x6, boardButton3x7, boardButton5x7, boardButton4x8, boardButton5x8, boardButton4x7;
    
    private Button[][] boardButtons;
    @FXML
    private ImageView bookshelf0x0, bookshelf1x0, bookshelf2x0, bookshelf3x0, bookshelf4x0, bookshelf4x1, bookshelf3x1, bookshelf2x1, bookshelf1x1, bookshelf0x1, bookshelf0x2, bookshelf1x2, bookshelf2x2, bookshelf3x2, bookshelf4x2, bookshelf0x3, bookshelf1x3, bookshelf2x3, bookshelf3x3, bookshelf4x3, bookshelf0x4, bookshelf1x4, bookshelf2x4, bookshelf3x4, bookshelf4x4, bookshelf5x0, bookshelf5x1, bookshelf5x2, bookshelf5x3, bookshelf5x4;
    private ImageView[][] myBookshelf;
    @FXML
    private ImageView bookshelf1_0x0, bookshelf1_1x0, bookshelf1_2x0, bookshelf1_3x0, bookshelf1_4x0, bookshelf1_4x1, bookshelf1_3x1, bookshelf1_2x1, bookshelf1_1x1, bookshelf1_0x1, bookshelf1_0x2, bookshelf1_1x2, bookshelf1_2x2, bookshelf1_3x2, bookshelf1_4x2, bookshelf1_0x3, bookshelf1_1x3, bookshelf1_2x3, bookshelf1_3x3, bookshelf1_4x3, bookshelf1_0x4, bookshelf1_1x4, bookshelf1_2x4, bookshelf1_3x4, bookshelf1_4x4, bookshelf1_5x0, bookshelf1_5x1, bookshelf1_5x2, bookshelf1_5x3, bookshelf1_5x4;
    private ImageView[][] bookshelf1;
    @FXML
    private ImageView bookshelf2_0x0, bookshelf2_1x0, bookshelf2_2x0, bookshelf2_3x0, bookshelf2_4x0, bookshelf2_4x1, bookshelf2_3x1, bookshelf2_2x1, bookshelf2_1x1, bookshelf2_0x1, bookshelf2_0x2, bookshelf2_1x2, bookshelf2_2x2, bookshelf2_3x2, bookshelf2_4x2, bookshelf2_0x3, bookshelf2_1x3, bookshelf2_2x3, bookshelf2_3x3, bookshelf2_4x3, bookshelf2_0x4, bookshelf2_1x4, bookshelf2_2x4, bookshelf2_3x4, bookshelf2_4x4, bookshelf2_5x0, bookshelf2_5x1, bookshelf2_5x2, bookshelf2_5x3, bookshelf2_5x4;
    private ImageView[][] bookshelf2;
    @FXML
    private ImageView bookshelf3_0x0, bookshelf3_1x0, bookshelf3_2x0, bookshelf3_3x0, bookshelf3_4x0, bookshelf3_4x1, bookshelf3_3x1, bookshelf3_2x1, bookshelf3_1x1, bookshelf3_0x1, bookshelf3_0x2, bookshelf3_1x2, bookshelf3_2x2, bookshelf3_3x2, bookshelf3_4x2, bookshelf3_0x3, bookshelf3_1x3, bookshelf3_2x3, bookshelf3_3x3, bookshelf3_4x3, bookshelf3_0x4, bookshelf3_1x4, bookshelf3_2x4, bookshelf3_3x4, bookshelf3_4x4, bookshelf3_5x0, bookshelf3_5x1, bookshelf3_5x2, bookshelf3_5x3, bookshelf3_5x4;
    private ImageView[][] bookshelf3;


    private int selectedColumn=-1;

    private final TileImages tileImages=new TileImages();

    private final List<Integer> coord = new ArrayList<>();
    @Override
    public void setScene(GUI gui, Stage stage) {
        super.setScene(gui, stage);
        gui.setGameController(this);
        initialize();
        }

    public void prova(ActionEvent actionEvent){

        for(int i=0; i<9; i++){
            for (int j=0; j<9; j++){
                if(boardImages[i][j]!=null){
                    boardImages[i][j].setImage(new Image(tileImages.getImage(Tile.PINK)));
                }
            }
        }

        insertBookshelf[5][0].setImage(new Image(tileImages.getImage(Tile.LIGHTBLUE)));
        insertBookshelf[4][0].setImage(new Image(tileImages.getImage(Tile.PINK)));
        insertBookshelf[3][0].setImage(new Image(tileImages.getImage(Tile.BLUE)));
        insertBookshelf[2][0].setImage(new Image(tileImages.getImage(Tile.LIGHTBLUE)));

        myBookshelf[5][0].setImage( insertBookshelf[5][0].getImage());
        myBookshelf[4][0].setImage(insertBookshelf[4][0].getImage());
        myBookshelf[3][0].setImage(insertBookshelf[3][0].getImage());
        myBookshelf[2][0].setImage(insertBookshelf[2][0].getImage());

        startTurn();
    }
    //----------------------------------------------------------- Server vs client-------------------------------------

    public void startTurn(){
        ableBoardButton();
        setBoardButton();
        enterTilesButton.setVisible(true);
        stateLabel.setText("Choose tile!");
    }

    public void update(){
        updateBoard();
        updateOtherBookshelf();
    }

    public void endGame(){
        //TODO show final ranking
    }

    //------------------------------------------------------------Client vs Server-----------------------------------
    public void enterTiles(ActionEvent actionEvent) {
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
        /*try{
            client.askBoardTiles(coord);
        } catch (InvalidChoiceException e) {
            throw new RuntimeException(e);
        } catch (NotConnectedException e) {
            throw new RuntimeException(e);
        } catch (InvalidParametersException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NotMyTurnException e) {
            throw new RuntimeException(e);
        }*/
        disableBoardButton();
        setTile();
        removeTiles();
        for(Button button:columnButtons){
            button.setDisable(true);
        }
        enterTilesButton.setDisable(true);
        enterTilesButton.setVisible(false);
        for(Button button:columnButtons){
            button.setDisable(false);
        }

        selectMyBookshelf(actionEvent);
        bookshelfButton.setOnAction(this::selectMyBookshelf);
    }

    private void setTile() {
        for(int i=0; i<coord.size();i=i+2){
            switch (i){
                case 0-> tile1Image.setImage(boardImages[coord.get(i+1)][coord.get(i)].getImage());
                case 2-> tile2Image.setImage(boardImages[coord.get(i+1)][coord.get(i)].getImage());
                case 4-> tile3Image.setImage(boardImages[coord.get(i+1)][coord.get(i)].getImage());
            }
        }

        tile1.setVisible(true);

        if(tile2Image.getImage()!=null) {
            tile2.setVisible(true);

        }
        if (tile3Image.getImage()!=null){
            tile3.setVisible(true);

        }
    }

    public void leaveGame(ActionEvent actionEvent){

        try {
            client.askLeaveGame();
            exit();
        } catch (LoginException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertTile(ActionEvent actionEvent) {
        /*try {
            client.askInsertShelfTiles(selectedColumn, coord);

        } catch (InvalidChoiceException e) {
            throw new RuntimeException(e);
        } catch (NotConnectedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InvalidLenghtException e) {
            throw new RuntimeException(e);
        } catch (NotMyTurnException e) {
            throw new RuntimeException(e);
        }*/

        closeBookshelf(actionEvent);

        for(int i=0; i<6; i++){
                myBookshelf[i][selectedColumn].setImage(insertBookshelf[i][selectedColumn].getImage());
        }


        tile1Image.setImage(null);
        tile2Image.setImage(null);
        tile3Image.setImage(null);

        tile1.setDisable(true);
        tile2.setDisable(true);
        tile3.setDisable(true);

        tile1.setVisible(false);
        tile2.setVisible(false);
        tile3.setVisible(false);

        tile1Button.setOnMouseExited(this::highlightTile);
        tile2Button.setOnMouseExited(this::highlightTile);
        tile3Button.setOnMouseExited(this::highlightTile);

        tile1Button.setOnMouseEntered(this::highlightTile);
        tile2Button.setOnMouseEntered(this::highlightTile);
        tile3Button.setOnMouseEntered(this::highlightTile);

        tile1Button.setOpacity(0);
        tile2Button.setOpacity(0);
        tile3Button.setOpacity(0);


        unselectColumn();
        for(Button button:columnButtons){
            button.setDisable(true);
        }

    }

    public void endTurn(){
        coord.clear();
        enterColumnButton.setDisable(true);
        bookshelfButton.setOnAction(null);
        stateLabel.setText("Not your turn!");
        Integer score = null;
        try{
            score= client.askGetMyScore();
        } catch (IOException e) {

        }

        scoreLabel.setText(Integer.toString(score));
    }


    //------------------------------------------------------------Bookshelf methods----------------------------------------------------------------


    public void selectMyBookshelf(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        if(button.getOnMouseEntered()!=null) {
            stateLabel.setText("Choose column");
            showBookshelfGroup.setDisable(false);
            showBookshelfGroup.setVisible(true);
            showMyBookshelfGroup.setDisable(true);
            showMyBookshelfGroup.setVisible(false);

            bookshelfButton.setOnMouseExited(null);
            bookshelfButton.setOnMouseEntered(null);
        } else {
            stateLabel.setText("");
            closeBookshelf(actionEvent);
        }
    }

    public void closeBookshelf(ActionEvent actionEvent) {
        showBookshelfGroup.setDisable(true);
        showBookshelfGroup.setVisible(false);
        bookshelfButton.setOnMouseExited(this::showMyBookshelf);
        bookshelfButton.setOnMouseEntered(this::showMyBookshelf);
        bookshelfButton.setOpacity(0);
    }

    public void showMyBookshelf(MouseEvent mouseEvent){
        Button button = (Button) mouseEvent.getSource();
        if(mouseEvent.getEventType().getName().equals("MOUSE_ENTERED")){
            button.setOpacity(0.5);
            showMyBookshelfGroup.setDisable(false);
            showMyBookshelfGroup.setVisible(true);

        } else {
            button.setOpacity(0);
            showMyBookshelfGroup.setDisable(true);
            showMyBookshelfGroup.setVisible(false);
        }
    }

    public void showOtherPlayerBookshelf(MouseEvent mouseEvent){
        Button button = (Button) mouseEvent.getSource();
        if(mouseEvent.getEventType().getName().equals("MOUSE_ENTERED")){
            button.setOpacity(0.5);
            switch (button.getId()){
                case("bookshelfPlayer1Button")->{
                    showBookshelf1Group.setVisible(true);
                    showBookshelf1Group.setDisable(false);
                }
                case("bookshelfPlayer2Button")->{
                    showBookshelf2Group.setVisible(true);
                    showBookshelf2Group.setDisable(false);
                }
                case("bookshelfPlayer3Button")->{
                    showBookshelf3Group.setVisible(true);
                    showBookshelf3Group.setDisable(false);
                }
            }

        } else {
            button.setOpacity(0);
            showBookshelf1Group.setVisible(false);
            showBookshelf1Group.setDisable(true);

            showBookshelf2Group.setVisible(false);
            showBookshelf2Group.setDisable(true);

            showBookshelf3Group.setVisible(false);
            showBookshelf3Group.setDisable(true);
        }
    }

    public void selectColumn(ActionEvent actionEvent){
        if(tile1Button.getOnMouseEntered()==null||tile2Button.getOnMouseEntered()==null||tile3Button.getOnMouseEntered()==null) return;
        stateLabel.setText("Insert tiles");
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
            if(!checkColumn(column)) {
                stateLabel.setText("Not enough space!");
                return;
            }

            selectedColumn=column;

            button.setOnMouseExited(null);
            button.setOnMouseEntered(null);

            button.setOpacity(0.5);



            if(tile3Image.getImage()!=null) {
                tile3.setDisable(false);
                tile3Image.setDisable(false);
            }
            if(tile2Image.getImage()!=null){
                tile2.setDisable(false);
                tile2Image.setDisable(false);
            }
            tile1.setDisable(false);
            tile1Image.setDisable(false);

        } else {
            unselectColumn();
        }
    }

    public void unselectColumn(){
        Button button=null;
        if(selectedColumn<0||selectedColumn>4) return;
        switch (selectedColumn){
            case 0-> button=column1Button;
            case 1-> button=column2Button;
            case 2-> button=column3Button;
            case 3-> button=column4Button;
            case 4-> button=column5Button;
        }
        button.setOnMouseExited(this::highlightTile);
        button.setOnMouseEntered(this::highlightTile);
        button.setOpacity(0);
        stateLabel.setText("Choose column");

        tile3.setDisable(true);
        tile2.setDisable(true);
        tile1.setDisable(true);
        tile1Image.setDisable(true);
        tile2Image.setDisable(true);
        tile3Image.setDisable(true);

        selectedColumn=-1;
    }
    private boolean checkColumn(int column) {
        int space=0;
        for(int i=0; i<6; i++){
            if(insertBookshelf[i][column].getImage()==null){
               space++;
            }
        }
        int tile=0;
        if(tile1Image.getImage()!=null) tile++;
        if(tile2Image.getImage()!=null) tile++;
        if(tile2Image.getImage()!=null) tile++;

        return space >= tile;
    }

    public void putTile(ActionEvent actionEvent) {
        if(selectedColumn<0||selectedColumn>4) return;
        Button button = (Button) actionEvent.getSource();
        Image image;
        switch(button.getId()) {
            case ("tile1Button")-> image=tile1Image.getImage();
            case ("tile2Button")-> image=tile2Image.getImage();
            case ("tile3Button")-> image=tile3Image.getImage();
            default -> image=null;
        }
        if(button.getOnMouseEntered()!=null){

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

            if(tile1Button.getOnMouseEntered()==null&&(tile3Button.getOnMouseEntered()==null||tile3Image.getImage()==null)&&(tile2Button.getOnMouseEntered()==null||tile2Image.getImage()==null)){
                enterColumnButton.setDisable(false);
            }
        } else {

            for (int i=0; i<6; i++){
                if(insertBookshelf[i][selectedColumn].getImage()!=null){
                    if(!insertBookshelf[i][selectedColumn].getImage().equals(image)) return;
                    enterColumnButton.setDisable(true);
                    insertBookshelf[i][selectedColumn].setImage(null);

                    button.setOpacity(0);
                    button.setOnMouseEntered(this::highlightTile);
                    button.setOnMouseExited(this::highlightTile);
                    if(tile1Button.getOnMouseEntered()!=null&&tile3Button.getOnMouseEntered()!=null&&tile2Button.getOnMouseEntered()!=null){
                        for(int j=0; j<5; j++){
                            if(j!=selectedColumn) columnButtons.get(j).setDisable(false);
                        }
                    }

                    return;
                }
                }
            }
    }

    private void updateOtherBookshelf() {
        int i=0;
        for (Matrix m:client.getModel().getOtherPlayers().values()){
            //TODO update other player bookshelf
        }
    }

    //--------------------------------------------------------------Board methods-----------------------------------------------

    public void updateBoard(){
        for (int i = 0; i<9; i++){
            for (int j = 0; j<9; j++){
                if(boardGroups[j][i]!=null){
                    Tile tile=client.getModel().getBoard().getTileByCoord(i,j);
                    if (tile==Tile.EMPTY) boardImages[j][i].setImage(null);
                    else {
                        String image = tileImages.getImage(tile);
                        boardImages[j][i].setImage(new Image(image));
                    }
                }
            }
        }
    }
    public void setBoardButton(){
        for (int i=0; i<9; i++){
            for (int j=0; j<9; j++){
                checkButton(i,j);
            }
        }
        enterTilesButton.setDisable(true);
        for(int h=0; h<9; h++){
            for(int k=0; k<9; k++){
                if(boardButtons[h][k]!=null){
                    if(boardButtons[h][k].getOnMouseEntered()==null){
                        setButtonsOnSelect(h, k);
                        enterTilesButton.setDisable(false);
                    }
                }
            }
        }
    }

    private void checkButton(int i, int j) {
        if (boardButtons[i][j]==null) return;
        if(boardImages[i][j].getImage()==null) {
            boardButtons[i][j].setDisable(true);
            return;
        }
        if(i-1<0||i+1>=9||j-1<0||j+1>=9){
            boardButtons[i][j].setDisable(false);
            return;
        }
        if(boardButtons[i-1][j]==null||boardButtons[i+1][j]==null||boardButtons[i][j+1]==null||boardButtons[i][j-1]==null){
            boardButtons[i][j].setDisable(false);
            return;
        }
        if(boardImages[i-1][j].getImage()==null||boardImages[i+1][j].getImage()==null||boardImages[i][j+1].getImage()==null||boardImages[i][j-1].getImage()==null){
            boardButtons[i][j].setDisable(false);
            return;
        }
        boardButtons[i][j].setDisable(true);
    }

    public void setButtonsOnSelect(int x, int y){
        for (int i=0; i<9; i++){
            for (int j=0; j<9; j++){
                if((i!=x||(j!=y-2&&j!=y-1&&j!=y&&j!=y+1&&j!=y+2))&&(j!=y||(i!=x-2&&i!=x-1&&i!=x+1&&i!=x+2))&&boardButtons[i][j]!=null&&boardButtons[i][j].getOnMouseEntered()!=null){
                    boardButtons[i][j].setDisable(true);
                }
            }
        }
    }

    public void selectTile(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        if(button.getOnMouseEntered()!=null){
            button.setOnMouseExited(null);
            button.setOnMouseEntered(null);
            button.setOpacity(0.5);
            enterTilesButton.setDisable(false);
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
                        boardImages[i][j].setImage(null);
                        boardButtons[i][j].setOnMouseExited(this::highlightTile);
                        boardButtons[i][j].setOnMouseEntered(this::highlightTile);
                    }
                }
            }
        }
    }
    public void ableBoardButton(){
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                if(boardGroups[i][j]!=null){
                    boardGroups[i][j].setDisable(false);
                    boardButtons[i][j].setDisable(false);
                }
            }
        }
    }
    public void disableBoardButton(){
        for (int i=0; i<9; i++){
            for (int j=0; j<9; j++){
                if(boardButtons[i][j]!=null){
                    boardButtons[i][j].setDisable(true);
                }
            }
        }
    }
    //------------------------------------------------------------Button methods-----------------------------------------------------------------
    public void highlightTile(MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        if(mouseEvent.getEventType().getName().equals("MOUSE_ENTERED")){
            button.setOpacity(0.5);
        } else {
            button.setOpacity(0);
        }
    }

    //-------------------------------------------------------------Group methods-------------------------------------


    public void showExitGroup(ActionEvent actionEvent) {
        exitGroup.setVisible(true);
        exitGroup.setDisable(false);
    }

    public void closeExit(ActionEvent actionEvent) {
        exitGroup.setVisible(false);
        exitGroup.setDisable(true);
    }
    //----------------------------------------------------------Inizialization methods---------------------------------
   private void setOtherBookshelf(){
        switch (client.getModel().getOtherPlayers().size()){
            case 1->{
                bookshelfPlayer3.setDisable(true);
                bookshelfPlayer3.setVisible(false);

                bookshelfPlayer2.setDisable(true);
                bookshelfPlayer2.setVisible(false);
            }

            case 2->{
                bookshelfPlayer3.setDisable(true);
                bookshelfPlayer3.setVisible(false);
            }
        }
   }

    private void initialize() {
        boardGroups=new Group[][] {  {null,      null,       null,       null,       board0x4,   board0x5,   null,       null,       null},
                {null,      null,       null,       board1x3,   board1x4,   board1x5,   null,       null,       null},
                {null,      null,       board2x2,   board2x3,   board2x4,   board2x5,   board2x6,   null,       null},
                {board3x0,  board3x1,   board3x2,   board3x3,   board3x4,   board3x5,   board3x6,   board3x7,   null},
                {board4x0,  board4x1,   board4x2,   board4x3,   board4x4,   board4x5,   board4x6,   board4x7,   board4x8},
                {null,      board5x1,   board5x2,   board5x3,   board5x4,   board5x5,   board5x6,   board5x7,   board5x8},
                {null,      null,       board6x2,   board6x3,   board6x4,   board6x5,   board6x6,   null,       null},
                {null,      null,       null,       board7x3,   board7x4,   board7x5,   null,       null,       null},
                {null,      null,       null,       board8x3,   board8x4,   null,       null,       null,       null}
        };

        boardButtons= new Button[][]{  {null,            null,             null,           null,           boardButton0x4, boardButton0x5, null,           null,           null},
                {null,            null,             null,           boardButton1x3, boardButton1x4, boardButton1x5, null,           null,           null},
                {null,            null,             boardButton2x2, boardButton2x3, boardButton2x4, boardButton2x5, boardButton2x6, null,           null},
                {boardButton3x0,  boardButton3x1,   boardButton3x2, boardButton3x3, boardButton3x4, boardButton3x5, boardButton3x6, boardButton3x7, null},
                {boardButton4x0,  boardButton4x1,   boardButton4x2, boardButton4x3, boardButton4x4, boardButton4x5, boardButton4x6, boardButton4x7, boardButton4x8},
                {null,            boardButton5x1,   boardButton5x2, boardButton5x3, boardButton5x4, boardButton5x5, boardButton5x6, boardButton5x7, boardButton5x8},
                {null,            null,             boardButton6x2, boardButton6x3, boardButton6x4, boardButton6x5, boardButton6x6, null,           null},
                {null,            null,             null,           boardButton7x3, boardButton7x4, boardButton7x5, null,           null,           null},
                {null,            null,             null,           boardButton8x3, boardButton8x4, null,           null,           null,           null}
        };

        boardImages = new ImageView[][]{  {null,        null,       null,       null,       boardImage0x4,      boardImage0x5,      null,       null,       null},
                {null,            null,             null,           boardImage1x3, boardImage1x4, boardImage1x5,    null,           null,           null},
                {null,            null,             boardImage2x2,  boardImage2x3, boardImage2x4, boardImage2x5,    boardImage2x6,  null,           null},
                {boardImage3x0,   boardImage3x1,    boardImage3x2,  boardImage3x3, boardImage3x4, boardImage3x5,    boardImage3x6,  boardImage3x7,  null},
                {boardImage4x0,   boardImage4x1,    boardImage4x2,  boardImage4x3, boardImage4x4, boardImage4x5,    boardImage4x6,  boardImage4x7, boardImage4x8},
                {null,            boardImage5x1,    boardImage5x2,  boardImage5x3, boardImage5x4, boardImage5x5,    boardImage5x6,  boardImage5x7, boardImage5x8},
                {null,            null,             boardImage6x2,  boardImage6x3, boardImage6x4, boardImage6x5,    boardImage6x6,  null,           null},
                {null,            null,             null,           boardImage7x3, boardImage7x4, boardImage7x5,    null,           null,           null},
                {null,            null,             null,           boardImage8x3, boardImage8x4, null,             null,           null,           null}
        };
        myBookshelf=new ImageView[][] {        {bookshelf0x0, bookshelf0x1, bookshelf0x2, bookshelf0x3, bookshelf0x4},
                {bookshelf1x0, bookshelf1x1,bookshelf1x2, bookshelf1x3, bookshelf1x4},
                {bookshelf2x0, bookshelf2x1,bookshelf2x2, bookshelf2x3, bookshelf2x4},
                {bookshelf3x0, bookshelf3x1,bookshelf3x2, bookshelf3x3, bookshelf3x4},
                {bookshelf4x0, bookshelf4x1,bookshelf4x2, bookshelf4x3, bookshelf4x4},
                {bookshelf5x0, bookshelf5x1,bookshelf5x2, bookshelf5x3, bookshelf5x4},
        };

        insertBookshelf= new ImageView[][]{        {insert0x0, insert0x1, insert0x2, insert0x3, insert0x4},
                {insert1x0, insert1x1,insert1x2, insert1x3, insert1x4},
                {insert2x0, insert2x1,insert2x2, insert2x3, insert2x4},
                {insert3x0, insert3x1,insert3x2, insert3x3, insert3x4},
                {insert4x0, insert4x1,insert4x2, insert4x3, insert4x4},
                {insert5x0, insert5x1,insert5x2, insert5x3, insert5x4},
        };

        bookshelf1=new ImageView[][] {        {bookshelf1_0x0, bookshelf1_0x1, bookshelf1_0x2, bookshelf1_0x3, bookshelf1_0x4},
                {bookshelf1_1x0, bookshelf1_1x1,bookshelf1_1x2, bookshelf1_1x3, bookshelf1_1x4},
                {bookshelf1_2x0, bookshelf1_2x1,bookshelf1_2x2, bookshelf1_2x3, bookshelf1_2x4},
                {bookshelf1_3x0, bookshelf1_3x1,bookshelf1_3x2, bookshelf1_3x3, bookshelf1_3x4},
                {bookshelf1_4x0, bookshelf1_4x1,bookshelf1_4x2, bookshelf1_4x3, bookshelf1_4x4},
                {bookshelf1_5x0, bookshelf1_5x1,bookshelf1_5x2, bookshelf1_5x3, bookshelf1_5x4},
        };

        bookshelf2=new ImageView[][] {        {bookshelf2_0x0, bookshelf2_0x1, bookshelf2_0x2, bookshelf2_0x3, bookshelf2_0x4},
                {bookshelf2_1x0, bookshelf2_1x1,bookshelf2_1x2, bookshelf2_1x3, bookshelf2_1x4},
                {bookshelf2_2x0, bookshelf2_2x1,bookshelf2_2x2, bookshelf2_2x3, bookshelf2_2x4},
                {bookshelf2_3x0, bookshelf2_3x1,bookshelf2_3x2, bookshelf2_3x3, bookshelf2_3x4},
                {bookshelf2_4x0, bookshelf2_4x1,bookshelf2_4x2, bookshelf2_4x3, bookshelf2_4x4},
                {bookshelf2_5x0, bookshelf2_5x1,bookshelf2_5x2, bookshelf2_5x3, bookshelf2_5x4},
        };
        bookshelf3=new ImageView[][] {        {bookshelf3_0x0, bookshelf3_0x1, bookshelf3_0x2, bookshelf3_0x3, bookshelf3_0x4},
                {bookshelf3_1x0, bookshelf3_1x1,bookshelf3_1x2, bookshelf3_1x3, bookshelf3_1x4},
                {bookshelf3_2x0, bookshelf3_2x1,bookshelf3_2x2, bookshelf3_2x3, bookshelf3_2x4},
                {bookshelf3_3x0, bookshelf3_3x1,bookshelf3_3x2, bookshelf3_3x3, bookshelf3_3x4},
                {bookshelf3_4x0, bookshelf3_4x1,bookshelf3_4x2, bookshelf3_4x3, bookshelf3_4x4},
                {bookshelf3_5x0, bookshelf3_5x1,bookshelf3_5x2, bookshelf3_5x3, bookshelf3_5x4},
        };

        columnButtons= new ArrayList<>();
        columnButtons.add(column1Button);
        columnButtons.add(column2Button);
        columnButtons.add(column3Button);
        columnButtons.add(column4Button);
        columnButtons.add(column5Button);
    }

    //--------------------------------------------------------Private class--------------------------------------------
    private static class TileImages {
        List<String> greenTiles;
        List<String> whiteTiles;
        List<String> blueTiles;
        List<String> lightblueTiles;
        List<String> pinkTiles;
        List<String> yellowTiles;

        public TileImages() {
            greenTiles=new ArrayList<>();
            greenTiles.add("file:/C:/Users/Utente/IS23-AM05/project_eng_soft_2023/target/classes/view/17_MyShelfie_BGA/item_tiles/Gatti1.1.png");
            greenTiles.add("file:/C:/Users/Utente/IS23-AM05/project_eng_soft_2023/target/classes/view/17_MyShelfie_BGA/item_tiles/Gatti1.1.png");
            greenTiles.add("file:/C:/Users/Utente/IS23-AM05/project_eng_soft_2023/target/classes/view/17_MyShelfie_BGA/item_tiles/Gatti1.1.png");

            whiteTiles=new ArrayList<>();
            whiteTiles.add("file:/C:/Users/Utente/IS23-AM05/project_eng_soft_2023/target/classes/view/17_MyShelfie_BGA/item_tiles/Libri1.1.png");
            whiteTiles.add("file:/C:/Users/Utente/IS23-AM05/project_eng_soft_2023/target/classes/view/17_MyShelfie_BGA/item_tiles/Libri1.2.png");
            whiteTiles.add("file:/C:/Users/Utente/IS23-AM05/project_eng_soft_2023/target/classes/view/17_MyShelfie_BGA/item_tiles/Libri1.3.png");

            blueTiles=new ArrayList<>();
            blueTiles.add("file:/C:/Users/Utente/IS23-AM05/project_eng_soft_2023/target/classes/view/17_MyShelfie_BGA/item_tiles/Cornici1.1.png");
            blueTiles.add("file:/C:/Users/Utente/IS23-AM05/project_eng_soft_2023/target/classes/view/17_MyShelfie_BGA/item_tiles/Cornici1.2.png");
            blueTiles.add("file:/C:/Users/Utente/IS23-AM05/project_eng_soft_2023/target/classes/view/17_MyShelfie_BGA/item_tiles/Cornici1.3.png");

            lightblueTiles=new ArrayList<>();
            lightblueTiles.add("file:/C:/Users/Utente/IS23-AM05/project_eng_soft_2023/target/classes/view/17_MyShelfie_BGA/item_tiles/Trofei1.1.png");
            lightblueTiles.add("file:/C:/Users/Utente/IS23-AM05/project_eng_soft_2023/target/classes/view/17_MyShelfie_BGA/item_tiles/Trofei1.2.png");
            lightblueTiles.add("file:/C:/Users/Utente/IS23-AM05/project_eng_soft_2023/target/classes/view/17_MyShelfie_BGA/item_tiles/Trofei1.3.png");

            pinkTiles=new ArrayList<>();
            pinkTiles.add("file:/C:/Users/Utente/IS23-AM05/project_eng_soft_2023/target/classes/view/17_MyShelfie_BGA/item_tiles/Piante1.1.png");
            pinkTiles.add("file:/C:/Users/Utente/IS23-AM05/project_eng_soft_2023/target/classes/view/17_MyShelfie_BGA/item_tiles/Piante1.2.png");
            pinkTiles.add("file:/C:/Users/Utente/IS23-AM05/project_eng_soft_2023/target/classes/view/17_MyShelfie_BGA/item_tiles/Piante1.3.png");

            yellowTiles=new ArrayList<>();
            yellowTiles.add("file:/C:/Users/Utente/IS23-AM05/project_eng_soft_2023/target/classes/view/17_MyShelfie_BGA/item_tiles/Giochi1.1.png");
            yellowTiles.add("file:/C:/Users/Utente/IS23-AM05/project_eng_soft_2023/target/classes/view/17_MyShelfie_BGA/item_tiles/Giochi1.2.png");
            yellowTiles.add("file:/C:/Users/Utente/IS23-AM05/project_eng_soft_2023/target/classes/view/17_MyShelfie_BGA/item_tiles/Giochi1.3.png");

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

}
