import javax.swing.*;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Date;

public class ClienteTCP {
    public static void main(String[] args) {
        try {
            Socket cliente = new Socket("localHost",8080);
            ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());
            var input = entrada.readObject();
            JOptionPane.showMessageDialog(null,"Recebido do servidor: " + input);
            entrada.close();
            System.out.println("Conexão encerrada");
        }
        catch(Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}