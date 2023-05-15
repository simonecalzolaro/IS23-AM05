package view;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class GameController extends GUIController {
    @FXML
    private Group board3x0;
    @FXML
    private Group board4x0;
    @FXML
    private Group board3x1;
    @FXML
    private Group board4x1;
    @FXML
    private Group board5x1;
    @FXML
    private Group board3x2;
    @FXML
    private Group board4x2;
    @FXML
    private Group board5x2;
    @FXML
    private Group board6x2;
    @FXML
    private Group board2x2;
    @FXML
    private Group board1x3;
    @FXML
    private Group board2x3;
    @FXML
    private Group board2x4;
    @FXML
    private Group board3x3;
    @FXML
    private Group board4x3;
    @FXML
    private Group board5x3;
    @FXML
    private Group board6x3;
    @FXML
    private Group board7x3;
    @FXML
    private Group board8x3;
    @FXML
    private Group board1x4;
    @FXML
    private Group board0x4;
    @FXML
    private Group board3x4;
    @FXML
    private Group board4x4;
    @FXML
    private Group board5x4;
    @FXML
    private Group board6x4;
    @FXML
    private Group board7x4;
    @FXML
    private Group board8x4;
    @FXML
    private Group board0x5;
    @FXML
    private Group board1x5;
    @FXML
    private Group board2x5;
    @FXML
    private Group board3x5;
    @FXML
    private Group board4x5;
    @FXML
    private Group board5x5;
    @FXML
    private Group board6x5;
    @FXML
    private Group board7x5;
    @FXML
    private Group board2x6;
    @FXML
    private Group board3x6;
    @FXML
    private Group board4x6;
    @FXML
    private Group board5x6;
    @FXML
    private Group board6x6;
    @FXML
    private Group board3x7;
    @FXML
    private Group board5x7;
    @FXML
    private Group board4x8;
    @FXML
    private Group board5x8;
    @FXML
    private Group board4x7;

    private Group[][] boardGroups;
    @FXML
    private ImageView boardImage3x0;
    @FXML
    private ImageView boardImage4x0;
    @FXML
    private ImageView boardImage3x1;
    @FXML
    private ImageView boardImage4x1;
    @FXML
    private ImageView boardImage5x1;
    @FXML
    private ImageView boardImage3x2;
    @FXML
    private ImageView boardImage4x2;
    @FXML
    private ImageView boardImage5x2;
    @FXML
    private ImageView boardImage6x2;
    @FXML
    private ImageView boardImage2x2;
    @FXML
    private ImageView boardImage1x3;
    @FXML
    private ImageView boardImage2x3;
    @FXML
    private ImageView boardImage2x4;
    @FXML
    private ImageView boardImage3x3;
    @FXML
    private ImageView boardImage4x3;
    @FXML
    private ImageView boardImage5x3;
    @FXML
    private ImageView boardImage6x3;
    @FXML
    private ImageView boardImage7x3;
    @FXML
    private ImageView boardImage8x3;
    @FXML
    private ImageView boardImage1x4;
    @FXML
    private ImageView boardImage0x4;
    @FXML
    private ImageView boardImage3x4;
    @FXML
    private ImageView boardImage4x4;
    @FXML
    private ImageView boardImage5x4;
    @FXML
    private ImageView boardImage6x4;
    @FXML
    private ImageView boardImage7x4;
    @FXML
    private ImageView boardImage8x4;
    @FXML
    private ImageView boardImage0x5;
    @FXML
    private ImageView boardImage1x5;
    @FXML
    private ImageView boardImage2x5;
    @FXML
    private ImageView boardImage3x5;
    @FXML
    private ImageView boardImage4x5;
    @FXML
    private ImageView boardImage5x5;
    @FXML
    private ImageView boardImage6x5;
    @FXML
    private ImageView boardImage7x5;
    @FXML
    private ImageView boardImage2x6;
    @FXML
    private ImageView boardImage3x6;
    @FXML
    private ImageView boardImage4x6;
    @FXML
    private ImageView boardImage5x6;
    @FXML
    private ImageView boardImage6x6;
    @FXML
    private ImageView boardImage3x7;
    @FXML
    private ImageView boardImage5x7;
    @FXML
    private ImageView boardImage4x8;
    @FXML
    private ImageView boardImage5x8;
    @FXML
    private ImageView boardImage4x7;

    @FXML
    private Button boardButton3x0;
    @FXML
    private Button boardButton4x0;
    @FXML
    private Button boardButton3x1;
    @FXML
    private Button boardButton4x1;
    @FXML
    private Button boardButton5x1;
    @FXML
    private Button boardButton3x2;
    @FXML
    private Button boardButton4x2;
    @FXML
    private Button boardButton5x2;
    @FXML
    private Button boardButton6x2;
    @FXML
    private Button boardButton2x2;
    @FXML
    private Button boardButton1x3;
    @FXML
    private Button boardButton2x3;
    @FXML
    private Button boardButton2x4;
    @FXML
    private Button boardButton3x3;
    @FXML
    private Button boardButton4x3;
    @FXML
    private Button boardButton5x3;
    @FXML
    private Button boardButton6x3;
    @FXML
    private Button boardButton7x3;
    @FXML
    private Button boardButton8x3;
    @FXML
    private Button boardButton1x4;
    @FXML
    private Button boardButton0x4;
    @FXML
    private Button boardButton3x4;
    @FXML
    private Button boardButton4x4;
    @FXML
    private Button boardButton5x4;
    @FXML
    private Button boardButton6x4;
    @FXML
    private Button boardButton7x4;
    @FXML
    private Button boardButton8x4;
    @FXML
    private Button boardButton0x5;
    @FXML
    private Button boardButton1x5;
    @FXML
    private Button boardButton2x5;
    @FXML
    private Button boardButton3x5;
    @FXML
    private Button boardButton4x5;
    @FXML
    private Button boardButton5x5;
    @FXML
    private Button boardButton6x5;
    @FXML
    private Button boardButton7x5;
    @FXML
    private Button boardButton2x6;
    @FXML
    private Button boardButton3x6;
    @FXML
    private Button boardButton4x6;
    @FXML
    private Button boardButton5x6;
    @FXML
    private Button boardButton6x6;
    @FXML
    private Button boardButton3x7;
    @FXML
    private Button boardButton5x7;
    @FXML
    private Button boardButton4x8;
    @FXML
    private Button boardButton5x8;
    @FXML
    private Button boardButton4x7;
    
    private Button[][] boardButtons;
    @FXML
    private ImageView bookshelf0x0;
    @FXML
    private ImageView bookshelf1x0;
    @FXML
    private ImageView bookshelf2x0;
    @FXML
    private ImageView bookshelf3x0;
    @FXML
    private ImageView bookshelf4x0;
    @FXML
    private ImageView bookshelf4x1;
    @FXML
    private ImageView bookshelf3x1;
    @FXML
    private ImageView bookshelf2x1;
    @FXML
    private ImageView bookshelf1x1;
    @FXML
    private ImageView bookshelf0x1;
    @FXML
    private ImageView bookshelf0x2;
    @FXML
    private ImageView bookshelf1x2;
    @FXML
    private ImageView bookshelf2x2;
    @FXML
    private ImageView bookshelf3x2;
    @FXML
    private ImageView bookshelf4x2;
    @FXML

    private ImageView bookshelf0x3;
    @FXML
    private ImageView bookshelf1x3;
    @FXML
    private ImageView bookshelf2x3;
    @FXML
    private ImageView bookshelf3x3;
    @FXML
    private ImageView bookshelf4x3;
    @FXML
    private ImageView bookshelf0x4;
    @FXML
    private ImageView bookshelf1x4;
    @FXML
    private ImageView bookshelf2x4;
    @FXML
    private ImageView bookshelf3x4;
    @FXML
    private ImageView bookshelf4x4;
    @FXML
    private ImageView bookshelf0x5;
    @FXML
    private ImageView bookshelf1x5;
    @FXML
    private ImageView bookshelf2x5;
    @FXML
    private ImageView bookshelf3x5;
    @FXML
    private ImageView bookshelf4x5;

    private ImageView[][] myBookshelf;



    @Override
    public void setScene(GUI gui, Stage stage) {
        super.setScene(gui, stage);
        gui.setGameController(this);
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

        ImageView[][] boardImages = new ImageView[][]{{null, null, null, null, boardImage0x4, boardImage0x5, null, null, null},
                {null, null, null, boardImage1x3, boardImage1x4, boardImage1x5, null, null, null},
                {null, null, boardImage2x2, boardImage2x3, boardImage2x4, boardImage2x5, boardImage2x6, null, null},
                {boardImage3x0, boardImage3x1, boardImage3x2, boardImage3x3, boardImage3x4, boardImage3x5, boardImage3x6, boardImage3x7, null},
                {boardImage4x0, boardImage4x1, boardImage4x2, boardImage4x3, boardImage4x4, boardImage4x5, boardImage4x6, boardImage4x7, boardImage4x8},
                {null, boardImage5x1, boardImage5x2, boardImage5x3, boardImage5x4, boardImage5x5, boardImage5x6, boardImage5x7, boardImage5x8},
                {null, null, boardImage6x2, boardImage6x3, boardImage6x4, boardImage6x5, boardImage6x6, null, null},
                {null, null, null, boardImage7x3, boardImage7x4, boardImage7x5, null, null, null},
                {null, null, null, boardImage8x3, boardImage8x4, null, null, null, null}
        };
        myBookshelf=new ImageView[][] {        {bookshelf0x0, bookshelf1x0, bookshelf2x0, bookshelf3x0, bookshelf4x0},
                {bookshelf0x1, bookshelf1x1,bookshelf2x1, bookshelf3x1, bookshelf4x1},
                {bookshelf0x2, bookshelf1x2,bookshelf2x2, bookshelf3x2, bookshelf4x2},
                {bookshelf0x3, bookshelf1x3,bookshelf2x3, bookshelf3x3, bookshelf4x3},
                {bookshelf0x4, bookshelf1x4,bookshelf2x4, bookshelf3x4, bookshelf4x4},
                {bookshelf0x5, bookshelf1x5,bookshelf2x5, bookshelf3x5, bookshelf4x5},


        };
        }

    public void prova(MouseEvent actionEvent) {
        board4x0.setVisible(true);
        board4x0.setDisable(false);
        boardGroups[3][0].setVisible(true);
        boardGroups[3][0].setDisable(false);

    }


    public void selectTile(ActionEvent actionEvent) {
    }

    private void unselectTile() {
    }

    public void showMyBookshef(ActionEvent actionEvent) {
    }

    public void ableTile(){
    }


}
