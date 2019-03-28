/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scorereader.structure;

import java.awt.image.BufferedImage;

/**
 *
 * @author ascarneiro
 */
public class Figura {

    public Figura(int tipo, BufferedImage image) {
        this.tipo = tipo;
        this.image = image;
    }

    private int tipo;
    private BufferedImage image;

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getTipo() {
        return tipo;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }

}
