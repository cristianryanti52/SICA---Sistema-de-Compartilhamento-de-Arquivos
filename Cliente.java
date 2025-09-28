import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Cliente {
    private static final String SERVIDOR = "127.0.0.1"; // IP do servidor
    private static final int PORTA = 12345;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVIDOR, PORTA);
            DataInputStream entrada = new DataInputStream(socket.getInputStream());
            DataOutputStream saida = new DataOutputStream(socket.getOutputStream());
            Scanner sc = new Scanner(System.in);

            System.out.println("Conectado ao servidor SiCA!");

            while (true) {
                System.out.println("\nEscolha uma opção:");
                System.out.println("1 - Enviar arquivo");
                System.out.println("2 - Listar arquivos no servidor");
                System.out.println("3 - Baixar arquivo");
                System.out.println("4 - Sair");
                System.out.print("Opção: ");
                int opcao = sc.nextInt();
                sc.nextLine(); // consumir quebra de linha

                switch (opcao) {
                    case 1:
                        System.out.print("Digite o caminho do arquivo: ");
                        String caminho = sc.nextLine();
                        enviarArquivo(saida, entrada, caminho);
                        break;
                    case 2:
                        saida.writeUTF("LIST");
                        System.out.println("Arquivos no servidor:\n" + entrada.readUTF());
                        break;
                    case 3:
                        System.out.print("Digite o nome do arquivo para download: ");
                        String nome = sc.nextLine();
                        saida.writeUTF("DOWNLOAD");
                        saida.writeUTF(nome);
                        receberArquivo(entrada, nome);
                        break;
                    case 4:
                        saida.writeUTF("EXIT");
                        socket.close();
                        System.out.println("Conexão encerrada.");
                        return;
                    default:
                        System.out.println("Opção inválida.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Envia arquivo para o servidor
    private static void enviarArquivo(DataOutputStream saida, DataInputStream entrada, String caminho) {
        try {
            File arquivo = new File(caminho);
            if (!arquivo.exists()) {
                System.out.println("Arquivo não encontrado!");
                return;
            }

            saida.writeUTF("UPLOAD");
            saida.writeUTF(arquivo.getName());
            saida.writeLong(arquivo.length());

            FileInputStream fis = new FileInputStream(arquivo);
            byte[] buffer = new byte[4096];
            int bytesLidos;
            while ((bytesLidos = fis.read(buffer)) > 0) {
                saida.write(buffer, 0, bytesLidos);
            }
            fis.close();

            System.out.println("Arquivo enviado com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao enviar arquivo: " + e.getMessage());
        }
    }

    // Recebe arquivo do servidor
    private static void receberArquivo(DataInputStream entrada, String nomeArquivo) {
        try {
            String resposta = entrada.readUTF();
            if (!resposta.equals("OK")) {
                System.out.println(resposta);
                return;
            }

            long tamanho = entrada.readLong();
            FileOutputStream fos = new FileOutputStream("download_" + nomeArquivo);
            byte[] buffer = new byte[4096];
            int bytesLidos;
            long total = 0;

            while (total < tamanho && (bytesLidos = entrada.read(buffer)) > 0) {
                fos.write(buffer, 0, bytesLidos);
                total += bytesLidos;
            }
            fos.close();

            System.out.println("Arquivo baixado como: download_" + nomeArquivo);
        } catch (IOException e) {
            System.out.println("Erro ao receber arquivo: " + e.getMessage());
        }
    }
}
