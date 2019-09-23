/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scorereader.structure.claves;

import java.util.Map;
import scorereader.structure.Linha;

/**
 *
 * @author ascarneiro
 */
public abstract class Clave {

    public int index = 0;

    private Clave() {
    }

    public Clave(int index) {
        this.index = index;

    }

    public int getOrdem() {
        return index;
    }

    public void calcularEspacos() {
        for (int i = 1; i <= getLinhas().size(); i++) {

            Linha l = getLinhas().get(String.valueOf(i));
            Linha lb = getLinhas().get(String.valueOf(i + 1));
            Linha la = getLinhas().get(String.valueOf(i - 1));

            if (lb != null) {
                double diff = Math.abs((l.y - lb.y) / 2);
                l.yEspacoAbaixo = l.y + diff;
            }
            if (la != null) {
                double diff = Math.abs((l.y - la.y) / 2);
                l.yEspacoAcima = l.y - diff;
            }
            getLinhas().put(String.valueOf(i), l);
        }
    }

    public int getIndex() {
        return index;
    }

    /**
     *
     * Obrigatorios implementar
     */
    public abstract Linha getLinha(String numero);

    public abstract Map<String, Linha> getLinhas();

    public abstract String getNomeNota(int index);

    public abstract String getNomeNotaAcima(int index);

    public abstract String getNomeNotaAbaixo(int index);

    public abstract void addLinha(int numero, Linha linha);

}
