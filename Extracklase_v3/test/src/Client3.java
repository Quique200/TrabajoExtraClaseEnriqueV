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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;

public class Client3 extends Application{
    public static void main(String[] args){
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
        final Scanner sc = new Scanner(text.getText()); // object to read data from user's keybord

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
                while((clientSocket.isConnected)){
                        textArea.setEditable(true);
                        msg = text.getText();
                        out.println(username + ":" + msg);
                        out.flush();
                        out.flush();
                        //Button boton;
                        //boton = new Button();
                        //boton.setText(text.getText());
                        text.clear();
                        //layout.getChildren().add(boton);
                        break;

                    }
                    } );
                    
                }
            });
        //sender.start();  
              //      button.setOnAction(e -> 
            //{    
        //sender.start();
          //     } );
            Thread receiver = new Thread(new Runnable() {
                String msg;
                @Override
                public void run() {
                    try {
                        msg = in.readLine();
                        while(msg!=null){
                            System.out.println("Server : "+msg);
                            msg = in.readLine();
                        }
                        System.out.println("Server out of service");
                        out.close();
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            receiver .start();
    }catch (IOException e){
        e.printStackTrace();
        }
        primaryStage.setTitle("Title of the Window");
        //Button button;
        //button = new Button();
        //button.setText("Enviar");


        //Group layout = new Group();
        button.setLayoutX(670);
        button.setLayoutY(550);
        Rectangle r = new Rectangle(0, 0, 450, 400);
        r.setLayoutX(300);
        r.setLayoutY(180);

        text.setLayoutX(500);
        text.setLayoutY(550);


        r.setFill(Color.BLACK);
        layout.getChildren().add(r);
        layout.getChildren().add(button);
        layout.getChildren().add(text);
        
        Scene scene = new Scene(layout,800,600);
        primaryStage.setScene(scene);
        primaryStage.show();




    }
}
