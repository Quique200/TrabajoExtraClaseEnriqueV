import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.io.OutputStreamWriter;

/** 
 * Esta clase es la encargada de manejar la comunicación con un cliente en un chat.
*/
public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;

    /**
     * Este constructor es el encargado de que se inicialicen los flujos de entrada/salida, así como obtener el nombre de usuario del cliente que se conecto al servidor.
     *
     * @param socket El socket de la conexión con el cliente.
     */
    public ClientHandler(Socket socket){
        try{
            this.socket = socket;
            this.bufferedWriter =new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); 
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            this.clientUsername = bufferedReader.readLine(); 
            clientHandlers.add(this); 
            broadcastMessage("SERVER: "+clientUsername+" ha entrado al chat!");
        
        } catch (IOException e){
            closeEverything(socket,bufferedReader,bufferedWriter);
        }
    }

    @Override
    public void run(){
        String messageFromClient;

        while (socket.isConnected()){
            try{
                messageFromClient = bufferedReader.readLine();
                broadcastMessage(messageFromClient);
            }catch(IOException e){
                closeEverything(socket,bufferedReader,bufferedWriter);
                break;
            }
        }

    }
    /**
     * Este método es utilizado para transmitir un mensaje a todos los clientes conectados por ende este mensaje sería uno de broadcast.
     *
     * @param messageToSend Es el mensaje que se va a enviar a todos los clientes.
     */
    public void broadcastMessage(String messageToSend){
        for(ClientHandler clientHandler: clientHandlers){
            try{
                if (!clientHandler.clientUsername.equals(clientUsername)){
                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            }catch(IOException e) {
                closeEverything(socket,bufferedReader,bufferedWriter);
            }
        }
    }
    /**
     * Método para el manejador de cliente, al igual que el avisar a los demás clientes que un usuario se desconecto del chat.
     */
    public void removeClientHandler(){
        clientHandlers.remove(this);
        broadcastMessage("SERVER"+clientUsername+" has left the chat!");
    }
    
    /**
     * Este método es utilizado para cerrar los flujos de entrada/salida y el socket.
     *
     * @param socket          El socket a cerrar.
     * @param bufferedReader  El lector de flujo de entrada a cerrar.
     * @param bufferedWriter  El escritor de flujo de salida a cerrar.
     */
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        removeClientHandler();
        try{
            if (bufferedReader != null){
                bufferedReader.close();
            }
            if (bufferedWriter != null){
                bufferedWriter.close();
            }
            if (socket != null){
                socket.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }

    }
    
}
