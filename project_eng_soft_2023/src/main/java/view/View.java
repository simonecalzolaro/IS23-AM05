package view;

public interface View {
    public int getNumOfPlayer();
    public void updateBoard();
    public void endGame();
    public void isYourTurn();
    public void startGame();

    public void endYourTurn();
}
