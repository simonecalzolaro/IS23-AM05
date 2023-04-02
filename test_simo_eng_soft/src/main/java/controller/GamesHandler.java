package controller;

import java.util.ArrayList;
import java.util.Scanner;

public class GamesHandler {
    ArrayList<Game> activeGames = new ArrayList<>();

    public void startPlaying(ControlPlayer player){

        if(!activeGames.isEmpty()){
            int lastIndex = activeGames.size()-1;
            if(activeGames.get(lastIndex).getConnectedPlayers() < activeGames.get(lastIndex).getExpectedPlayers()){
                activeGames.get(lastIndex).addPlayer(player);
            }
            else{
                int numInt=-1;

                while(numInt == -1){
                    Scanner scanner = new Scanner(System.in);
                    System.out.println("\nInsert the expected number of players:");
                    String numString = scanner.nextLine();

                    switch (numString){
                        case "2":
                            numInt = 2;
                            break;

                        case "3":
                            numInt = 3;
                            break;

                        case "4":
                            numInt = 4;
                            break;

                        default:
                            System.out.println("\nInvalid number of players. Try again\n");
                            numInt = -1;

                    }
                }

                activeGames.add(new Game(player,numInt));
                int runnableIndex = activeGames.size()-1;

                activeGames.get(runnableIndex).start();


            }
        }
        else{
            int numInt=-1;

            while(numInt == -1){
                Scanner scanner = new Scanner(System.in);
                System.out.println("\nInsert the expected number of players:");
                String numString = scanner.nextLine();

                switch (numString){
                    case "2":
                        numInt = 2;
                        break;

                    case "3":
                        numInt = 3;
                        break;

                    case "4":
                        numInt = 4;
                        break;

                    default:
                        System.out.println("\nInvalid number of players. Try again\n");
                        numInt = -1;

                }
            }

            activeGames.add(new Game(player,numInt));
            int runnableIndex = activeGames.size()-1;

            activeGames.get(runnableIndex).start();



        }
    }
}
