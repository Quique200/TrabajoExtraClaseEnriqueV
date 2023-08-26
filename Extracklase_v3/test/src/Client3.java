import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import javafx.application.Platform;

public class Client3 extends Application{
    public static void main(String[] args) throws IOException{
        launch(args);
    }


    public void start(Stage primaryStage)throws Exception{
        
        Group layout = new Group();
        TextField text = new TextField();
        Button button = new Button();
        TextArea textArea = new TextArea();
        textArea.setPrefWidth(700);
        textArea.setPrefHeight(480);
        text.setPrefWidth(500);
        text.setPrefHeight(20);
        button.setText("Enviar");






        final BufferedReader in;   // object to read data from socket
        final PrintWriter out;     // object to write data into socket
        final Scanner sc = new Scanner(System.in); // object to read data from user's keybord

        System.out.println("Indique el nombre de usuario con el que se desea ingresar");
        String username = sc.nextLine();
        Socket clientSocket = new Socket("localhost", 1234);
        
        


        try {
            out = new PrintWriter(clientSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out.write(username);
            out.println();
            out.flush();
            Thread sender = new Thread(new Runnable() {
                String msg;
                public void run() {                          
                    button.setOnAction(e -> 
            {
                String msg;    
                while((clientSocket.isConnected())){
                        textArea.setEditable(true);
                        msg = text.getText();
                        out.println(username + ":" + msg);
                        out.flush();
                        String finalMSG = msg;
                        Platform.runLater(()->{
                            textArea.setText(textArea.getText() + "\n" + username + ":" + finalMSG);
                        });
                        text.clear();
                        textArea.setEditable(false);
                        break;

                    }
                    } );
                    
                }
            });
            sender.start();  

            new Thread(new Runnable() {
                @Override
                public void run() {
                    String msgFromGroupChat;

                    while((clientSocket.isConnected())){
                    try {
                        textArea.setEditable(true);
                        msgFromGroupChat = in.readLine();
                        System.out.println(msgFromGroupChat);
                        String finalMsg = msgFromGroupChat;
                        Platform.runLater(()->{
                            textArea.setText(textArea.getText() + "\n" + " " + finalMsg);
                        });
                        textArea.setEditable(false);
                    } catch (IOException E) {
                        E.printStackTrace();
                    }
                }
                }
            }).start();
    }catch (IOException e){
        e.printStackTrace();
        }
        primaryStage.setTitle("Usuario :" +username);
        button.setLayoutX(640);
        button.setLayoutY(540);
        
        Rectangle r = new Rectangle(0, 0, 670, 400);
        r.setLayoutX(50);
        r.setLayoutY(170);

        text.setLayoutX(100);
        text.setLayoutY(540);

        textArea.setLayoutX(50);
        textArea.setLayoutY(50);


        r.setFill(Color.web("#c9e9fc"));
        layout.getChildren().add(r);
        layout.getChildren().add(button);
        layout.getChildren().add(text);
        layout.getChildren().add(textArea);
        
        Scene scene = new Scene(layout,800,600);
        scene.setFill(Color.web("#e4f4fd"));
        textArea.setStyle("-fx-control-inner-background: #fff6ed;");
        primaryStage.setScene(scene);
        primaryStage.show();
        textArea.setEditable(false);

    }
}
