/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scorereader;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import org.bytedeco.javacpp.opencv_core;
import scorereader.abc.Parser;
import scorereader.structure.Figura;
import scorereader.structure.Linha;
import scorereader.structure.claves.Clave;
import scorereader.structure.Nota;

/**
 *
 * @author ascarneiro
 */
public class ScoreReader {

    /**
     * @param args the command line arguments
     */
    public static int FATOR = 2;
    public static String CAMINHO_DEFAULT = "C:\\Users\\ascarneiro\\Desktop\\TCC\\ScoreReader\\repository\\";

    public static void main(String[] args) {

        try {

            //Carregar imagem
            JFileChooser jfc = new JFileChooser(CAMINHO_DEFAULT);
            if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                byte[] originalImage = Files.readAllBytes(jfc.getSelectedFile().toPath());

                //Remove linhas da pauta
                byte[] imageData = Utilities.removerLinhasDaPauta(originalImage);
                opencv_core.IplImage imagemSemLinhas = Utilities.bufferedImageToIplImage(imageData);
                opencv_core.IplImage imagemCinza = Utilities.binarizar(imagemSemLinhas.clone());
                opencv_core.IplImage imageCopy = imagemCinza.clone();

                ArrayList<Figura> figuras = Utilities.segmentar(imagemCinza, imageCopy);
                ArrayList<Clave> pautas = Utilities.obterInformacoesPautas(originalImage);
                ArrayList<Nota> notas = Utilities.detectarAlturaNotas(imagemSemLinhas);

//
                for (Figura figura : figuras) {
                    for (Nota nota : notas) {
                        int diff = Math.abs((int) (nota.x - figura.x));
                        if (diff < 5) {
                            figura.nota = nota;
                            break;
                        }
                    }
                }

                for (Clave clave : pautas) {
                    clave.calcularEspacos();//Pocessa espacos
                }

                //Reconhecer os elementos
                ArrayList<Figura> processados = new ArrayList<Figura>();
                for (Figura figura : figuras) {
                    if (figura.isNotaMusical()) {
                        for (Clave pauta : pautas) {

                            for (int i = 1; i <= pauta.getLinhas().size(); i++) {

                                Linha linha = pauta.getLinha(String.valueOf(i));
                                int diff = ((int) (figura.nota.y - linha.y));
                                int variacao = Math.abs(diff);
                                if (variacao < FATOR) {//Diferenca for de menos de 2 pixels
                                    figura.nota.nome = pauta.getNomeNota(linha.index);
                                    processados.add(figura);
                                } else {
                                    diff = ((int) (figura.nota.y - linha.yEspacoAbaixo));
                                    variacao = Math.abs(diff);
                                    if (variacao < FATOR) {
                                        figura.nota.nome = pauta.getNomeNotaAbaixo(linha.index);
                                        processados.add(figura);
                                    } else {
                                        diff = ((int) (figura.nota.y - linha.yEspacoAcima));
                                        variacao = Math.abs(diff);
                                        if (variacao < FATOR) {
                                            figura.nota.nome = pauta.getNomeNotaAcima(linha.index);
                                            processados.add(figura);
                                        }
                                    }
                                }
                            }

                        }
                    }

                }

                for (int i = 1; i < processados.size(); i++) {
                    Figura processado = processados.get(i);
                    System.out.println("Index: " + i + " Nota: " + processado.nota.nome);
                    if (processado.isNotaMusical()) {
                        ByteArrayInputStream bais = new ByteArrayInputStream(processado.getImage());
                        BufferedImage read = ImageIO.read(bais);
                        ImageIO.write(read, "PNG", new File(Utilities.DIR_DEBUG + "../notas//" + i + ".png"));
                    }

                }

                System.out.println(
                        "asdsad");
//
////            //Compilar os elementos
                Parser parser = new Parser();
                String[] scoreDir = parser.compile(jfc.getSelectedFile().getName(), processados);
//
                for (String dir : scoreDir) {
                    //Mostrar score
                    if (!dir.isEmpty()) {
                        Desktop.getDesktop().open(new File(dir));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
