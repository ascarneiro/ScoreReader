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
public class Armadura {

    private BufferedImage image;
    private String clave;
    private String[] bemois;
    private String[] sustenidos;
    private String tempo;

    public Armadura(String clave, String[] bemois, String[] sustenidos, String tempo) {
        this.clave = clave;
        this.bemois = bemois;
        this.sustenidos = sustenidos;
        this.tempo = tempo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String[] getBemois() {
        return bemois;
    }

    public void setBemois(String[] bemois) {
        this.bemois = bemois;
    }

    public String[] getSustenidos() {
        return sustenidos;
    }

    public void setSustenidos(String[] sustenidos) {
        this.sustenidos = sustenidos;
    }

    public String getTempo() {
        return tempo;
    }

    public void setTempo(String tempo) {
        this.tempo = tempo;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }

}
