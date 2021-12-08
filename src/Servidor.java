import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Servidor {

    public static void main(String[] args) throws IOException {

        ServerSocket servidor = new ServerSocket(8000);
        ExecutorService pool = Executors.newFixedThreadPool(20);
        System.out.println("Servidor ouvindo localHost:8000");
        while (true) {

            pool.execute(new ThreadConexao(servidor.accept()));
        }
    }
}
