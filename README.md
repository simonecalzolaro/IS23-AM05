
# MyShelfie IS23-AM05

Software engineering final project 2023 Politecnico di Milano
Digital version of the Cranio Games's board game MyShelfie

![BANNER_MYSHELFIE](https://github.com/simonecalzolaro/IS23-AM05/assets/92320303/51df4b8a-bb53-4980-8dc7-6b203583e031)


# Project description
The entire project is written in Java. Users can choose to play with a graphical interface (GUI) or with the command line (TUI),
they can also choose the way the client communicates with the server between Java RMI connection or a Socket TCP connection.
The game is client-side resilient since the user is able to re-join the game (if still active on the server) in case of disconnections caused by client-side crash or network disconnections, but it's not server-side persistent so it doesn't handle server-side crashes. The player assumes that the server never fails.
Players can share messages to each other through a chat.
Server is able to run multiple different games.
# How to install and run the project

-    Ensure that java is installed on your device

-    Clone the repository into your file system

-    Open the terminal and navigate to the repository, then go into the ' out ' directory inside the project directory

-    If your device is going to run as a server type:

         java -jar server.jar 

-    If your device is going to run as a client type:
  
          java -jar client.jar

-    You can run your device as both client and server by performing both the previous steps on two different terminal windows




-    Clients and server are able to communicate only if they are connected to the same network 

-    Player must set the server's IP address manually before to play by performing these steps:
    
     Open the terminal and type:
        - Windows users:
        
                ipconfig 
        
        - MacOS/Linux users: 
            
                 ifconfig 

     Look for the IP address of the network interface 
           that you are using for playing an copy interface
        
     Open the project directory and go into the ' out ' 
           folder, the open the file called ' header.json ' 
           and paste the address into the ' hostname ' field


           


# How to use the project

When the software starts the terminal prompts the choice between GUI and TUI, make the choice and then the graphic/command-line will be shown. Now you can choose the connection between RMI and socket and after this you have to insert your nickname in order to log into a new/existing game, if you're starting a new game you have to choose the number of players.


- If you're using the CLI type ' /help ' on the terminal and it will show all the command that the user can perform
        
- If you're using the GUI you can manually select the tiles from the board, then you have to click the ' enter ' button, choose the columns on your shelf and then click on the tiles in the order that you want them to be inserted
        
 Note: You cannot change your mind after the tiles have been chosen from the board
        

# Credits

Creators of the project Simone Calzolaro, Gabriele Clara Di Gioacchino, Elena Caratti, Mirko Calvi
