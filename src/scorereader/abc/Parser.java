/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scorereader.abc;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import scorereader.structure.Figura;

/**
 *
 * @author ascarneiro
 */
public class Parser {

    private String PATHTOFILE = "C:\\Users\\ascarneiro\\Desktop\\TCC\\ScoreReader\\output\\";
    private String PATHTOTOOLS = "C:\\Users\\ascarneiro\\Desktop\\TCC\\ScoreReader\\tools\\";

    private String BAT_SVG = "C:\\Users\\ascarneiro\\Desktop\\TCC\\ScoreReader\\tools\\svg.bat";
    private String BAT_MIDI = "C:\\Users\\ascarneiro\\Desktop\\TCC\\ScoreReader\\tools\\midi.bat";

    private StringBuilder score = new StringBuilder();

    private String exportXHTML(String file) {
        String fileName = file;
        try {
            String file1 = new String(PATHTOTOOLS);
            String file2 = new String(PATHTOFILE + file + ".abc");
            String file3 = new String(PATHTOFILE + file + ".xhtml");
            Process exec = Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", BAT_SVG, file1, file2, file3});
            while (exec.waitFor() != 0);
        } catch (Exception e) {
        }
        return PATHTOFILE + fileName + ".xhtml";
    }

    private String exportPDF(StringBuilder score) {
        return "";
    }

    private String exportMIDI(String file) {
        String fileName = file;
        try {
            fileName = file.replace(".abc", ".midi");
            String file1 = new String(PATHTOTOOLS);
            String file2 = new String(PATHTOFILE + file + ".abc");
            String file3 = new String(PATHTOFILE + file + ".midi");
            Process exec = Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", BAT_MIDI, file1, file2, file3});
            while (exec.waitFor() != 0);
        } catch (Exception e) {
        }
        return PATHTOFILE + fileName + ".midi";
    }

    private String makeABCFile(StringBuilder score) {

        String fileName = System.currentTimeMillis() + "";
        try {
            FileWriter writer = new FileWriter(PATHTOFILE + fileName + ".abc");
            BufferedWriter bw = new BufferedWriter(writer);
            bw.write(score.toString());
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fileName;
    }

    public String[] compile(ArrayList<Figura> elementos) {
        StringBuilder score = make(elementos);
        String abcFile = makeABCFile(score);
        String fileSvg = exportXHTML(abcFile);
        String fileMidi = exportMIDI(abcFile);

        return new String[]{fileSvg, fileMidi};
    }

    private StringBuilder make(ArrayList<Figura> elementos) {
        score = new StringBuilder();
        score.append("X: 1").append(getTitulo(elementos)).append("\n");
        score.append(NomeNota.TITULO).append(" ").append(getTitulo(elementos)).append("\n");
        score.append(NomeNota.COMPOSITOR).append(" ").append(getCompositor(elementos)).append("\n");
        score.append(NomeNota.TONALIDADE).append(" ").append(getTonalidade(elementos)).append("\n");
        score.append(NomeNota.MEDIDA).append(" ").append(getMedida(elementos)).append("\n");
        score.append(getMusica(elementos));

        return score;
    }

    //Fixo por enquanto testar compilador
    private String getTitulo(ArrayList<Figura> elementos) {
        return "Twinkle Twinkle, Litlle Star";
    }

    private String getCompositor(ArrayList<Figura> elementos) {
        return "Trasncrypt by ScoreReader";
    }

    //Fixo por enquanto testar compilador
    private String getMedida(ArrayList<Figura> elementos) {
        return "4/4";
    }

    //Fixo por enquanto testar compilador
    private String getTonalidade(ArrayList<Figura> elementos) {
        return "D";
    }

    //Fixo por enquanto testar compilador
    private String getMusica(ArrayList<Figura> elementos) {
        return "D2D2A2A2|B2B2A4|G2G2F2F2|";
    }
}
