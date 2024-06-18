/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package envio;

/**
 *
 * @author flavio
 */
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class EnviaDados extends Thread {

    private final int portaLocalEnvio = 2000;
    private final int portaDestino = 2001;
    private final int portaLocalRecebimento = 2003;
    Semaphore sem;
    private final String funcao;
    static boolean fim;
    static int numSequencia = 0;// num sequencia dos pacotes
    static int numEsperado = 0;// num sequencia que chega do ACK
    static int num = 0;
    static int contBuffer = 0;
    static int teste = 0;
    static int x = 0;
    static boolean reenvio = false;

    static Buffertimer b;

    public EnviaDados(Semaphore sem, String funcao, boolean fim) {
        super(funcao);
        this.sem = sem;
        this.funcao = funcao;
        this.fim = fim;///////
        this.num = sem.availablePermits();
        b = new Buffertimer(num);
    }

    public String getFuncao() {
        return funcao;
    }

    private void enviaPct(int[] dados) {
        //converte int[] para byte[]
        ByteBuffer byteBuffer = ByteBuffer.allocate(dados.length * 4);
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        intBuffer.put(dados);

        byte[] buffer = byteBuffer.array();

        try {
            //System.out.println("Semaforo: "+sem.availablePermits());
            sem.acquire();
            //System.out.println("Semaforo: "+sem.availablePermits());

            InetAddress address = InetAddress.getByName("localhost");
            try (DatagramSocket datagramSocket = new DatagramSocket(portaLocalEnvio)) {
                DatagramPacket packet = new DatagramPacket(
                        buffer, buffer.length, address, portaDestino);

                datagramSocket.send(packet);
            }

            System.out.println("Envio feito.");
        } catch (SocketException ex) {
            Logger.getLogger(EnviaDados.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(EnviaDados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {

        switch (this.getFuncao()) {
            case "envia":
                //variavel onde os dados lidos serao gravados
                int[] dados = new int[350];
                //contador, para gerar pacotes com 1400 Bytes de tamanho
                //como cada int ocupa 4 Bytes, estamos lendo blocos com 350
                //int's por vez.
                int cont = 0;

                try (FileInputStream fileInput = new FileInputStream("entrada");) {
                    int lido;
                    while ((lido = fileInput.read()) != -1) {
                        dados[cont] = lido;
                        cont++;
                        if (cont == 349) {
                            //envia pacotes a cada 350 int's lidos.
                            //ou seja, 1400 Bytes.
                            dados[349] = numSequencia;
                            numSequencia++;///////////////
                            enviaPct(dados);
                            b.addPacote(dados);
                            dados = new int[350];
                            cont = 0;
                            //////
                        }
                    }

                    //ultimo pacote eh preenchido com
                    //-1 ate o fim, indicando que acabou
                    //o envio dos dados.
                    for (int i = cont; i < 349; i++) {
                        dados[i] = -1;
                    }
                    dados[349] = numSequencia;
                    numSequencia++;/////////////////
                    enviaPct(dados);
                    b.addPacote(dados);
                    ////////
                } catch (IOException e) {
                    System.out.println("Error message: " + e.getMessage());
                }
                break;
            case "ack":
                try {
                    DatagramSocket serverSocket = new DatagramSocket(portaLocalRecebimento);
                    byte[] receiveData = new byte[4];
                    int retorno = 0;

                    while (retorno != -1) {
                        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                        serverSocket.receive(receivePacket);
                        byte[] tmp = receivePacket.getData();
                        retorno = ((tmp[0] & 0xff) << 24) + ((tmp[1] & 0xff) << 16) + ((tmp[2] & 0xff) << 8) + ((tmp[3] & 0xff));
                        teste = retorno;
                        if (retorno != numEsperado) {
                            if (retorno == -1) {
                                System.out.println("Ultimo Ack Recebido");
                            } else {
                                System.out.println("ACK errado");
                                reenvio = true;
                            }
                        } else {
                            System.out.println("Ack recebido " + retorno + ".");
                            sem.release();
                            numEsperado++;
                        }

                    }
                    fim = true;
                } catch (IOException e) {
                    System.out.println("Excecao: " + e.getMessage());
                }
                break;
            case "timer": {
                int x =0;
                try {
                    while (!fim) {
                        Thread.sleep(50);
                        if (teste != -1) {
                            if (reenvio) {
                                x = 0;
                                for (int i = 0; i < num; i++) {
                                    int[] dadosreenvio = new int[350];
                                    dadosreenvio = b.getPacote((numEsperado+x)%num);
                                    //converte int[] para byte[]
                                    ByteBuffer byteBuffer = ByteBuffer.allocate(dadosreenvio.length * 4);
                                    IntBuffer intBuffer = byteBuffer.asIntBuffer();
                                    intBuffer.put(dadosreenvio);

                                    byte[] buffer = byteBuffer.array();

                                    try {

                                        InetAddress address = InetAddress.getByName("localhost");
                                        try (DatagramSocket datagramSocket = new DatagramSocket(portaLocalEnvio)) {
                                            DatagramPacket packet = new DatagramPacket(
                                                    buffer, buffer.length, address, portaDestino);

                                            datagramSocket.send(packet);
                                        }

                                        System.out.println("Envio feito.");
                                        x++;
                                       
                                    } catch (SocketException ex) {
                                        Logger.getLogger(EnviaDados.class.getName()).log(Level.SEVERE, null, ex);
                                    } catch (IOException ex) {
                                        Logger.getLogger(EnviaDados.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            }
                        } else {
                            break;
                        }
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(EnviaDados.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            break;
            default:
                break;
        }

    }
}
