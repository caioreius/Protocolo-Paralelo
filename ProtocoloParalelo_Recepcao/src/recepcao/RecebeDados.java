/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recepcao;

/**
 *
 * @author flavio
 */
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RecebeDados extends Thread {

    private final int portaLocalReceber = 2001;
    private final int portaLocalEnviar = 2002;
    private final int portaDestino = 2003;
    static int numSequencia = 0;//num de sequencia q chegou
    static int num = 0;// num de sequancia
    static float numero;

    private void enviaAck(boolean fim,boolean errado) {

        try {
            InetAddress address = InetAddress.getByName("localhost");
            try (DatagramSocket datagramSocket = new DatagramSocket(portaLocalEnviar)) {
                int[] sendString = new int[1];//criacao do vetor para envio de confirmacao de ack
                sendString[0] = numSequencia;//na unica posicao coloco o numsequencia para confirmacao
                if (fim) {
                    sendString[0] = -1;
                }
                if(errado){
                    sendString[0] = -2;
                }
                
                ByteBuffer byteBuffer = ByteBuffer.allocate(sendString.length * 4);
                IntBuffer intBuffer = byteBuffer.asIntBuffer();
                intBuffer.put(sendString);
                byte[] sendData = byteBuffer.array();

                DatagramPacket packet = new DatagramPacket(
                        sendData, sendData.length, address, portaDestino);

                datagramSocket.send(packet);
            }
        } catch (SocketException ex) {
            Logger.getLogger(RecebeDados.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RecebeDados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {

        try {
            DatagramSocket serverSocket = new DatagramSocket(portaLocalReceber);
            byte[] receiveData = new byte[1400];
            try (FileOutputStream fileOutput = new FileOutputStream("saida")) {
                boolean fim = false;
                while (!fim) {
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    serverSocket.receive(receivePacket);
                    System.out.println("dado recebido");

                    byte[] tmp = receivePacket.getData();

                    numero = (float) Math.random();
                    if (numero > 0.5) {
                        numSequencia = ((tmp[1396] & 0xff) << 24) + ((tmp[1397] & 0xff) << 16) + ((tmp[1398] & 0xff) << 8) + ((tmp[1399] & 0xff)); //retiro o numero de sequencia do pacote que chegou
                        if (numSequencia == num) {
                            for (int i = 0; i < tmp.length - 4; i = i + 4) {////////
                                int dados = ((tmp[i] & 0xff) << 24) + ((tmp[i + 1] & 0xff) << 16) + ((tmp[i + 2] & 0xff) << 8) + ((tmp[i + 3] & 0xff));

                                if (dados == -1) {
                                    fim = true;
                                    break;
                                }

                                fileOutput.write(dados);
                            }
                            enviaAck(fim, false);//////////////
                            num++;
                        } else {
                            System.out.println("descartado");///////
                        }
                    }else{
                        enviaAck(fim,true);
                        System.out.println("perda do pacote");
                    }
                    
                }
            }
        } catch (IOException e) {
            System.out.println("Excecao: " + e.getMessage());
        }
    }
}
