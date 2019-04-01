/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scorereader.image;

import org.opencv.core.Core;

/**
 *
 * @author ascarneiro
 */
public class OtherMain {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        Prototipo.retiraLinhasdePauta2();
    }
}
