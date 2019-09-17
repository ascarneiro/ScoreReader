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
import scorereader.abc.NomeNota;
import scorereader.abc.Parser;
import scorereader.structure.Figura;
import scorereader.structure.Linha;
import scorereader.structure.Pauta;
import scorereader.structure.Nota;

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
            File fi = new File("C:\\Users\\ascarneiro\\Desktop\\TCC\\ScoreReader\\repository\\OneBar.png");
            byte[] originalImage = Files.readAllBytes(fi.toPath());

            //Remove linhas da pauta
            byte[] imageData = Utilities.removerLinhasDaPauta(originalImage);
            opencv_core.IplImage imagemSemLinhas = Utilities.bufferedImageToIplImage(imageData);
            opencv_core.IplImage imagemCinza = Utilities.binarizar(imagemSemLinhas.clone());
            opencv_core.IplImage imageCopy = imagemCinza.clone();

            ArrayList<Figura> figuras = Utilities.segmentar(imagemCinza, imageCopy);
            ArrayList<Pauta> pautas = Utilities.obterInformacoesPautas(originalImage);
            ArrayList<Nota> notas = Utilities.detectarAlturaNotas(imagemSemLinhas);

            //Reconhecer os elementos
            ArrayList<Figura> processados = new ArrayList<Figura>();
//
//            for (Pauta pauta : pautas) {
//                int size = pauta.getLinhas().size();
//                for (int i = size; i > 0; i--) {
//                    Linha linha = pauta.getLinha(String.valueOf(i));
//                    for (Nota nota : notas) {
//
//                        int diff = (int) (nota.y - linha.y);
//                        diff = Math.abs(diff);
//                        if (diff >= 0 && diff <= 4) {
//                            diff = (int) (figura.y - figura.y);
//                            diff = Math.abs(diff);
//                            if (diff >= 0 && diff <= 13) {
//                                String nome = NomeNota._DO;
//                                if (linha.index == 1) {
//
//                                    nome = NomeNota._FA;
//                                } else if (linha.index == 2) {
//                                    nome = NomeNota._RE;
//                                } else if (linha.index == 3) {
//                                    nome = NomeNota._SI;
//                                } else if (linha.index == 4) {
//                                    nome = NomeNota._SOL;
//                                } else if (linha.index == 5) {
//                                    nome = NomeNota._MI;
//
//                                }
//                                nota.nome = nome;
//                                figura.setNota(nota);
//                                processados.add(figura);
//                            }
//
//                        }
//                    }
//
//                }
//            }
//
////            //Compilar os elementos
            Parser parser = new Parser();
//            String[] scoreDir = parser.compile(processados);
//
//            for (String dir : scoreDir) {
//                //Mostrar score
//                if (!dir.isEmpty()) {
//                    Desktop.getDesktop().open(new File(dir));
//                }
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
