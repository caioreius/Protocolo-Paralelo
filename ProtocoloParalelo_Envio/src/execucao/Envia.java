/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package execucao;

import envio.EnviaDados;
import java.util.Timer;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Envia {
	public static void main(String[] args) {
		Semaphore sem = new Semaphore(1);
                boolean fim = false;
		
		EnviaDados e = new EnviaDados(sem, "envia",fim);
		EnviaDados r = new EnviaDados(sem, "ack",fim);
                EnviaDados t = new EnviaDados(sem, "timer",fim);
		
                
		r.start();
		e.start();
		t.start();
                
		try {
			e.join();
			System.out.println("Fim envia");
			r.join();
			System.out.println("Fim recebe Ack");
                        t.join();
			System.out.println("Fim timer");
		} catch (InterruptedException ex) {
			Logger.getLogger(Envia.class.getName()).log(Level.SEVERE, null, ex);
		}

	}
}
