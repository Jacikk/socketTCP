import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadConexao implements Runnable {

    private final Socket socket;
    private boolean conectado;

    public ThreadConexao(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        conectado = true;

        System.out.println(socket.getInetAddress());
        while (conectado) {
            try {

                RequisicaoHTTP requisicao = RequisicaoHTTP.lerRequisicao(socket.getInputStream());

                if (requisicao.isManterViva()) {
                    socket.setKeepAlive(true);
                    socket.setSoTimeout(requisicao.getTempoLimite());
                } else {

                    socket.setSoTimeout(300);
                }

                if (requisicao.getRecurso().equals("/")) {
                    requisicao.setRecurso("index.html");
                }

                File arquivo = new File(requisicao.getRecurso().replaceFirst("/", ""));

                RespostaHTTP resposta;

                if (arquivo.exists()) {
                    resposta = new RespostaHTTP(requisicao.getProtocolo(), 200, "OK");
                } else {
                    resposta = new RespostaHTTP(requisicao.getProtocolo(), 404, "Not Found");
                    arquivo = new File("404.html");
                }

                resposta.setConteudoResposta(Files.readAllBytes(arquivo.toPath()));

                String dataFormatada = Util.formatarDataGMT(new Date());
                //cabeçalho padrão da resposta HTTP/1.1
                resposta.setCabecalho("Location", "http://localhost:8000/");
                resposta.setCabecalho("Date", dataFormatada);
                resposta.setCabecalho("Server", "MeuServidor/1.0");
                resposta.setCabecalho("Content-Type", "text/html");
                resposta.setCabecalho("Content-Length", resposta.getTamanhoResposta());

                resposta.setSaida(socket.getOutputStream());
                resposta.enviar();
            } catch (IOException ex) {

                if (ex instanceof SocketTimeoutException) {
                    try {
                        conectado = false;
                        socket.close();
                    } catch (IOException ex1) {
                        Logger.getLogger(ThreadConexao.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                }
            }

        }
    }

}
