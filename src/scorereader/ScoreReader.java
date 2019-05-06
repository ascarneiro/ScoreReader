/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scorereader;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import org.bytedeco.javacpp.opencv_core;
import scorereader.structure.Elemento;

/**
 *
 * @author ascarneiro
 */
public class ScoreReader {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {

            //Carregar imagem
            File fi = new File("C:\\Users\\ascarneiro\\Desktop\\TCC\\ScoreReader\\repository\\original.JPG");
            byte[] imageData = Files.readAllBytes(fi.toPath());

            //Remove linhas da pauta
            imageData = Utilities.removerLinhasDaPauta(imageData);
            opencv_core.IplImage imagemOriginal = Utilities.bufferedImageToIplImage(imageData);
            opencv_core.IplImage imagemCinza = Utilities.binarizar(imagemOriginal.clone());
            opencv_core.IplImage imageCopy = imagemCinza.clone();

            ArrayList<Elemento> segmentados = Utilities.segmentar(imagemCinza, imageCopy);

            //Reconhecer os elementos
            ArrayList<Elemento> processados = new ArrayList<Elemento>();
            int qt = 0;
            for (Elemento segmentado : segmentados) {
                if (qt > 3) {
                    break;
                }
                processados.add(Utilities.classificar(segmentado));
                System.out.println(processados.get(qt).getTipo());
                qt++;
            }
            /*
             //Compilar os elementos
             Parser parser = new Parser();
             String scoreDir = parser.compile(processados);

             //Mostrar score
             if (!scoreDir.isEmpty()) {
             Desktop.getDesktop().open(new File(scoreDir));
             }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
