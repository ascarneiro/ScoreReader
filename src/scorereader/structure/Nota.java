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
public class Nota {

    public int ordem = 0;
    public double x = 0;
    public double y = 0;
    public double raio = 0;
    public String nome = "indeterminada";

    public Nota(int ordem, double x, double y, double raio) {
        this.ordem = ordem;
        this.x = x;
        this.y = y;
        this.raio = raio;
    }

    
    
}
