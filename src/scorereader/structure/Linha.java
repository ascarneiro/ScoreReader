/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scorereader.structure;

/**
 *
 * @author ascarneiro
 */
public class Linha {

    public static String[] idLinha = new String[]{"5", "4", "3", "2", "1"};
    public int index = 0;
    public double y = 0;
    public double yEspacoAcima = 0;
    public double yEspacoAbaixo = 0;

    public Linha(int index, double y) {
        this.index = Integer.valueOf(idLinha[index - 1]);
        this.y = y;
    }

    public int getIndex() {
        return index;
    }

    public double getY() {
        return y;
    }

}
