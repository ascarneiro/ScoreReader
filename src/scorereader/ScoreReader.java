/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scorereader;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import javax.imageio.ImageIO;
import scorereader.abc.Parser;
import scorereader.server.Server;
import scorereader.structure.Elemento;
import scorereader.structure.JSONParser;

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
            File fi = new File("C:\\tcc\\ScoreReader\\fragment\\bill.JPG");
            byte[] imageData = Files.readAllBytes(fi.toPath());

            //Segmentar imagem
            ArrayList<Elemento> preProcessados = segmentar(imageData);

            //Reconhecer os elementos
            ArrayList<Elemento> processados = new ArrayList<Elemento>();
            for (Elemento elemento : preProcessados) {
                String encoded = Base64.getEncoder().encodeToString(elemento.getImage());
                BufferedImage image = ImageIO.read(fi);
                HashMap<String, Object> params = new HashMap<>();
                params.put("width", image.getWidth());
                params.put("height", image.getHeight());
                params.put("image64", encoded);

                String retorno = Server.callServerPython("analiseImage", params);
                processados.add(JSONParser.jsonToElement(retorno));

            }
            //Compilar os elementos
            Parser parser = new Parser();
            String scoreDir = parser.compile(processados);

            //Mostrar score
            if (!scoreDir.isEmpty()) {
                Desktop.getDesktop().open(new File(scoreDir));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<Elemento> segmentar(byte[] imageData) {
        //Chamar rotina do Mauricenz segmentacao
        return new ArrayList<>();
    }

}
