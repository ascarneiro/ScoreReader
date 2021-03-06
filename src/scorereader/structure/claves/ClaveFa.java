/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scorereader.structure.claves;

import java.util.HashMap;
import java.util.Map;
import scorereader.abc.ABC_NOTATION;
import scorereader.structure.Linha;

/**
 *
 * @author ascarneiro
 */
public class ClaveFa extends Pauta {

    private Map<String, Linha> linhas = new HashMap<String, Linha>();
    protected String[] pentagramaSol = new String[]{
        ABC_NOTATION._SOL,
        ABC_NOTATION._SI,
        ABC_NOTATION._RE,
        ABC_NOTATION.FA_,
        ABC_NOTATION.LA_
    };

    public ClaveFa(ClaveSol claveSol) {
        super(0);
    }

    
    
    public ClaveFa(int index) {
        super(index);
    }

    @Override
    public String getNomeNotaAcima(int index) {
        if (index == 1) {
            return ABC_NOTATION._LA;
        } else if (index == 2) {
            return ABC_NOTATION._DO;
        } else if (index == 3) {
            return ABC_NOTATION.MI_;
        } else if (index == 4) {
            return ABC_NOTATION.SOL_;
        } else if (index == 5) {
            return ABC_NOTATION.SI_;
        }
        return "ruido";

    }

    @Override
    public String getNomeNotaAbaixo(int index) {
        if (index == 1) {
            return ABC_NOTATION._FA;
        } else if (index == 2) {
            return ABC_NOTATION._LA;
        } else if (index == 3) {
            return ABC_NOTATION._DO;
        } else if (index == 4) {
            return ABC_NOTATION.MI_;
        } else if (index == 5) {
            return ABC_NOTATION.SOL_;
        }
        return "ruido";

    }

    @Override
    public String getNomeNota(int index) {
        return pentagramaSol[index - 1];
    }

    @Override
    public Map<String, Linha> getLinhas() {
        return linhas;
    }

    @Override
    public void addLinha(int numero, Linha linha) {
        if (linha.y < minimo) {
            minimo = linha.y;
        }

        if (linha.y > maximo) {
            maximo = linha.y;
        }
        linhas.put(Linha.idLinha[numero - 1], linha);
    }

    @Override
    public Linha getLinha(String numero) {
        return linhas.get(numero);
    }


}
