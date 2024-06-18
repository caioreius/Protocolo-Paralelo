/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package envio;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author caio e gabriel
 */
public class Buffertimer {
    int pacotes = 0;
    int cont = 0;
    Pacote[] pac;      
    
    public Buffertimer(int num) {
        this.pacotes  = num;
        pac = new Pacote[pacotes];
    }
    public void addPacote(int [] dados){
        pac[cont] = new Pacote(dados);
        cont++;
        if(cont==pacotes){
            cont = 0;
        }
    }
    
    public int [] getPacote(int x){
        return pac[x].getDado();
    }
    
}
