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

public class App extends Application{
    public static void main(String[] args){
        launch(args);
    }


    public void start(Stage primaryStage)throws Exception{
        Group layout = new Group();
        TextField text = new TextField();
        Button button;
        button = new Button();
        final Label label = new Label("Hola Mundos");
        button.setText("Enviar");
        final Socket clientSocket; // socket used by client to send and recieve data from server
        final BufferedReader in;   // object to read data from socket
        final PrintWriter out;     // object to write data into socket
        final Scanner sc = new Scanner(text.getText()); // object to read data from user's keybord
        final int val;
        
        


        try {
            clientSocket = new Socket("127.0.0.1",5000);
            out = new PrintWriter(clientSocket.getOutputStream());
            //String msg;
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                //Thread sender = new Thread(new Runnable() {
                //String msg;
                //@Override;
                //public void run() {                          
                    button.setOnAction(e -> 
            {
                String msg;    
                while(true){
                        System.out.println(text.getText());
                        //System.out.println(sc.nextLine());
                        msg = text.getText();
                        out.println(msg);
                        out.flush();
                        //Button boton;
                        //boton = new Button();
                        //boton.setText(text.getText());
                        label.setText(label.getText() + "\n" + "Usted :"+text.getText());
                        text.clear();
                        //layout.getChildren().add(boton);
                        break;

                    }
                    } );
                    
                //}
            //});
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
                            label.setText(label.getText() + "\n" + "Server :"+ msg);
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

        label.setLayoutX(400);
        label.setLayoutY(250);

        r.setFill(Color.BLACK);
        layout.getChildren().add(r);
        layout.getChildren().add(button);
        layout.getChildren().add(text);
        layout.getChildren().add(label);
        
        Scene scene = new Scene(layout,800,600);
        primaryStage.setScene(scene);
        primaryStage.show();




    }
}
