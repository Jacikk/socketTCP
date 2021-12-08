import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class ServidorTCP
{
    public static void main(String[] args) {
        try {

            ServerSocket servidor = new ServerSocket(8080);
            System.out.println("Servidor ouvindo a porta 8080");

            byte[] b = InetAddress.getByName("localhost").getAddress();
            System.out.println(b[0] + "." + b[1] + "." + b[2] + "." + b[3]);

            System.out.println("Endereço: " + InetAddress.getByName("localhost").getHostAddress() );

            while(true) {

                Socket cliente = servidor.accept();
                System.out.println("Cliente conectado: " + cliente.getInetAddress().getHostAddress());
                ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
                saida.flush();
                saida.writeObject("Deu tudo certo!");
                saida.close();
                cliente.close();
            }
        }
        catch(Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
        finally {
            System.out.println("Deu tudo certo!");
        }
    }
}
