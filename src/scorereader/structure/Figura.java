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
public class Figura {

    public int x = 0;
    public int y = 0;

    public int w = 0;
    public int h = 0;
    public byte[] image;
    public String base64Image;
    public String tipo;
    public boolean note = false;
    public Nota nota = null;

    public Figura(int x, int y, int w, int h, byte[] image, String base64Image, String tipo) {

        this.image = image;
        this.base64Image = base64Image;
        this.tipo = tipo;
        this.x = x;
        this.y = y;
        this.h = h;
        this.w = w;
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

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setNote(boolean note) {
        this.note = note;
    }

    public boolean isNote() {
        return note;
    }

    public int getY() {
        return y;
    }

    public int getH() {
        return h;
    }

    public int getW() {
        return w;
    }

    public int getX() {
        return x;
    }

    public void setNota(Nota nota) {
        this.nota = nota;
    }

    public Nota getNota() {
        return nota;
    }

}
