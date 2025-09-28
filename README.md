# 📂 SiCA - Sistema de Compartilhamento de Arquivos

Este projeto é uma implementação simples de um **Sistema de Compartilhamento de Arquivos (SiCA)** utilizando **Java** e **sockets TCP**.  
Ele permite a comunicação entre **Cliente** e **Servidor** para realizar operações básicas:

- ✅ Enviar arquivos do cliente para o servidor (UPLOAD)  
- ✅ Listar os arquivos disponíveis no servidor (LIST)  
- ✅ Baixar arquivos do servidor para o cliente (DOWNLOAD)  
- ✅ Encerrar a conexão de forma segura (EXIT)  

---

## 🚀 Tecnologias utilizadas
- Linguagem: **Java 11+**  
- Comunicação: **Sockets TCP**

---

## 📂 Estrutura de Pastas

sica/
├── Cliente.java
├── Servidor.java
└── pasta_servidor/ # Diretório onde os arquivos do servidor ficam armazenados

yaml
Copiar código

---

## ⚙️ Como executar

### 1️⃣ Compilar os arquivos
Abra o terminal dentro da pasta do projeto e compile os códigos:
Abra o terminal dentro da pasta do projeto e compile os códigos:
```bash
javac Servidor.java Cliente.java
```
2️⃣ Iniciar o servidor

Execute o servidor:
```bash
java Servidor
```
O servidor será iniciado na porta 12345 e criará (se não existir) a pasta pasta_servidor/ para armazenar os arquivos enviados.

3️⃣ Iniciar o cliente

Em outro terminal (ou outra máquina), execute:
```bash
java Cliente
```
O cliente se conectará ao servidor e exibirá um menu de opções.

📜 Funcionalidades do Cliente

Após conectar-se ao servidor, o cliente pode escolher entre:

Enviar arquivo – Envia um arquivo local para o servidor.

Listar arquivos – Mostra todos os arquivos disponíveis no servidor.

Baixar arquivo – Baixa um arquivo existente do servidor para a máquina do cliente.

O arquivo será salvo com o prefixo download_.

Sair – Encerra a conexão com o servidor.

🖥️ Exemplo de uso
Cliente

Escolha uma opção:

1 - Enviar arquivo

2 - Listar arquivos no servidor

3 - Baixar arquivo

4 - Sair

Opção: 1

Digite o caminho do arquivo: exemplo.txt

Arquivo enviado com sucesso!

Servidor

Servidor iniciado na porta 12345

Cliente conectado: /127.0.0.1

Arquivo recebido: exemplo.txt

🔮 Melhorias futuras

Autenticação de usuários.

Suporte para múltiplos downloads simultâneos.

Interface gráfica para o cliente.

👨‍💻 Desenvolvido como exercício prático de Redes de Computadores.

