//Librerias utilizadas para el socket
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

//Librerias utilizadas para JavaFX
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

/**
 * Clase que implenta una aplicación de chat desde la perspectiva de un cliente utilizado JavaFX, la cual se conecta a un servidor por medio de sockets
 * */
public class Client3 extends Application{
    /**
     * Este es el metodo main(principal) el cual es el encargado de iniciar la aplicación
     * @param args Los argumentos de la línea de comandos (no se utilizan).
     * @throws IOException Si ocurre un error cuando se inicia la aplicación.
     * */
    public static void main(String[] args) throws IOException{
        launch(args);
    }

/** 
 * Método el cual es usado para configurar y mostrar la interfaz gráfica de la apliación
*/
    public void start(Stage primaryStage)throws Exception{
        
        Group layout = new Group();
        TextField text = new TextField();
        Button button = new Button();
        TextArea textArea = new TextArea();
        textArea.setPrefWidth(700);//Determinar el ancho
        textArea.setPrefHeight(480);//Derterminar la altura
        text.setPrefWidth(500);
        text.setPrefHeight(20);
        button.setText("Enviar");





        //Configuración de sockets y entrada/salida
        final BufferedReader in;  //Se declara la entrada 
        final PrintWriter out;    //Se declara la salida
        final Scanner sc = new Scanner(System.in); // Sirve para obtener la informacón que se encuentra en la terminal la cual fue escrita por el teclado
        //Captura del nombre de usuario
        System.out.println("Indique el nombre de usuario con el que se desea ingresar");
        String username = sc.nextLine();//Obtiene el usuario escrito
        Socket clientSocket = new Socket("localhost", 1234);
        
        


        try {
            //En esta sección se inicializa la entrada/salida
            out = new PrintWriter(clientSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out.write(username);//Envia el username escrito
            out.println();
            out.flush();
            
            //Este Thread es el que permite enviar los mensajes al servidor
            Thread sender = new Thread(new Runnable() {
                public void run() {                          
                    button.setOnAction(e -> 
            {
                String msg;    
                while((clientSocket.isConnected())){
                        textArea.setEditable(true);//Se habilita el Text Area para ser modificada
                        msg = text.getText();
                        out.println(username + ":" + msg);//Se envia el mensaje escrito
                        out.flush();
                        String finalMSG = msg;
                        //Debido a que en el thread no se pueden hacer cambios en las interfaces graficas entonces se agrega el Platform para poder lograrlo
                        Platform.runLater(()->{
                            textArea.setText(textArea.getText() + "\n" + username + ":" + finalMSG);
                        });
                        text.clear();
                        textArea.setEditable(false); //Se deshabilita el Text Area para que no sea editable
                        break;

                    }
                    } );
                    
                }
            });
            sender.start();  

            //Este Thread es el que permite recibir los mensajes del servidor he imprimirlos en la interfaz grafica
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String msgFromGroupChat;

                    while((clientSocket.isConnected())){
                    try {
                        textArea.setEditable(true);
                        msgFromGroupChat = in.readLine();//Se lee el mensaje recibido
                        System.out.println(msgFromGroupChat);//Se imprime el mensaje recibido
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
        primaryStage.setTitle("Usuario :" +username);//Se le da un nombre a la ventana que se abre de la interfaz
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
        layout.getChildren().add(r);//Se añaden los elementos a la interfaz
        layout.getChildren().add(button);
        layout.getChildren().add(text);
        layout.getChildren().add(textArea);
        
        Scene scene = new Scene(layout,800,600);
        scene.setFill(Color.web("#e4f4fd"));
        textArea.setStyle("-fx-control-inner-background: #fff6ed;");
        primaryStage.setScene(scene);
        primaryStage.show();//Se muestra la interfaz grafica
        textArea.setEditable(false);

    }
}
