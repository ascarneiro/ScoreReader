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
public class Elemento {

    public byte[] image;
    public String base64Image;
    public String tipo;

    public Elemento(byte[] image, String base64Image, String tipo) {

        this.image = image;
        this.base64Image = base64Image;
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    public byte[] getImage() {
        return image;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getBase64Image() {
        return base64Image;
    }

}
