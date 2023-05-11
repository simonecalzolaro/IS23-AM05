package view;

public class TUI implements View{

    @Override
    public int getNumOfPlayer() {
        return 0;
    }

    @Override
    public void updateBoard() {

    }

    @Override
    public void endGame() {

    }

    @Override
    public void isYourTurn() {

    }

    @Override
    public void startGame() {

    }

    @Override
    public void endYourTurn() {

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


}
