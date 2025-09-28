import java.io.*;
import java.net.*;
import java.util.*;

public class Servidor {
    // Porta do servidor
    private static final int PORTA = 12345;
    // Diretório onde os arquivos serão armazenados
    private static final String DIRETORIO = "pasta_servidor";

    public static void main(String[] args) {
        try {
            // Cria diretório se não existir
            File dir = new File(DIRETORIO);
            if (!dir.exists()) {
                dir.mkdir();
            }

            // Inicia o servidor TCP
            ServerSocket serverSocket = new ServerSocket(PORTA);
            System.out.println("Servidor iniciado na porta " + PORTA);

            while (true) {
                // Aceita conexão de um cliente
                Socket socket = serverSocket.accept();
                System.out.println("Cliente conectado: " + socket.getInetAddress());

                // Cria uma thread para tratar cada cliente
                new Thread(new TrataCliente(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Classe interna para tratar cada cliente conectado
    static class TrataCliente implements Runnable {
        private Socket socket;

        public TrataCliente(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                DataInputStream entrada = new DataInputStream(socket.getInputStream());
                DataOutputStream saida = new DataOutputStream(socket.getOutputStream());

                while (true) {
                    // Lê comando do cliente
                    String comando = entrada.readUTF();

                    switch (comando) {
                        case "UPLOAD":
                            receberArquivo(entrada);
                            break;
                        case "LIST":
                            listarArquivos(saida);
                            break;
                        case "DOWNLOAD":
                            enviarArquivo(entrada, saida);
                            break;
                        case "EXIT":
                            System.out.println("Cliente desconectado.");
                            socket.close();
                            return;
                        default:
                            saida.writeUTF("Comando inválido.");
                    }
                }
            } catch (IOException e) {
                System.out.println("Conexão encerrada.");
            }
        }

        // Recebe um arquivo do cliente
        private void receberArquivo(DataInputStream entrada) throws IOException {
            String nomeArquivo = entrada.readUTF();
            long tamanho = entrada.readLong();

            FileOutputStream fos = new FileOutputStream(DIRETORIO + "/" + nomeArquivo);
            byte[] buffer = new byte[4096];
            int bytesLidos;
            long total = 0;

            while (total < tamanho && (bytesLidos = entrada.read(buffer)) > 0) {
                fos.write(buffer, 0, bytesLidos);
                total += bytesLidos;
            }
            fos.close();

            System.out.println("Arquivo recebido: " + nomeArquivo);
        }

        // Lista arquivos disponíveis no servidor
        private void listarArquivos(DataOutputStream saida) throws IOException {
            File dir = new File(DIRETORIO);
            File[] arquivos = dir.listFiles();
            if (arquivos != null) {
                StringBuilder lista = new StringBuilder();
                for (File f : arquivos) {
                    lista.append(f.getName()).append("\n");
                }
                saida.writeUTF(lista.toString());
            } else {
                saida.writeUTF("Nenhum arquivo disponível.");
            }
        }

        // Envia um arquivo para o cliente
        private void enviarArquivo(DataInputStream entrada, DataOutputStream saida) throws IOException {
            String nomeArquivo = entrada.readUTF();
            File arquivo = new File(DIRETORIO + "/" + nomeArquivo);

            if (!arquivo.exists()) {
                saida.writeUTF("Arquivo não encontrado.");
                return;
            }

            saida.writeUTF("OK");
            saida.writeLong(arquivo.length());

            FileInputStream fis = new FileInputStream(arquivo);
            byte[] buffer = new byte[4096];
            int bytesLidos;
            while ((bytesLidos = fis.read(buffer)) > 0) {
                saida.write(buffer, 0, bytesLidos);
            }
            fis.close();

            System.out.println("Arquivo enviado: " + nomeArquivo);
        }
    }
}
