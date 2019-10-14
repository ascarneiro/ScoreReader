/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scorereader.structure;

import org.bytedeco.javacpp.opencv_core;

/**
 *
 * @author ascarneiro
 */
public class Crop {

    public String fileName;
    public int x;
    public int y;
    public int w;
    public int h;
    public opencv_core.IplImage image = null;

    public Crop(int x, int y, int w, int h, opencv_core.IplImage image) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.image = image;
    }

    public int getX() {
        return x;
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

    public opencv_core.IplImage getImage() {
        return image;
    }

}
