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

    public int index = 0;
    public double y = 0;

    public Linha(int numero, double y) {
        this.index = numero;
        this.y = y;
    }

    public int getIndex() {
        return index;
    }

    public double getY() {
        return y;
    }

}
