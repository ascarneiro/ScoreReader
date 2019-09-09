/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scorereader.structure;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ascarneiro
 */
public class Pauta {

    public int index = 0;
    private Map<String, Linha> linhas = new HashMap<String, Linha>();

    public Pauta() {
    }

    public Pauta(int index) {
        this.index = index;
    }

    public int getOrdem() {
        return index;
    }

    public void addLinha(String numero, Linha linha) {
        linhas.put(numero, linha);
    }

    public Linha getLinha(String numero) {
        return linhas.get(numero);
    }

    public Map<String, Linha> getLinhas() {
        return linhas;
    }

    public int getIndex() {
        return index;
    }

    
    
}
