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
public class Espaco {

    public int index = 0;
    public double y = 0;
  

    public Espaco(int numero, double y) {
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
