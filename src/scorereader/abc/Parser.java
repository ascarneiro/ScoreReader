/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scorereader.abc;

import static java.awt.SystemColor.text;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.fop.svg.PDFTranscoder;

import scorereader.structure.Figura;
import scorereader.structure.claves.Clave;

public class Parser {

    private static String abcFile = "";
    private static String fileSvg = "";
    private static String filePDF = "";
    private static String fileMidi = "";
    private static String fileXHTML = "";

    private static final int quebrarEmCompassos = 3;
    private static String PATHTOFILE = "C:\\Users\\ascarneiro\\Desktop\\TCC\\ScoreReader\\output\\";
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
            e.printStackTrace();
        }
        return PATHTOFILE + fileName + ".xhtml";
    }

    private String exportPDF(String file) {
        try {

            file = exportSVG(file);
            Transcoder transcoder = new PDFTranscoder();
            TranscoderInput transcoderInput = new TranscoderInput(new FileInputStream(new File(file)));

            String pdf = file.replace(".svg", "") + ".pdf";
            TranscoderOutput transcoderOutput = new TranscoderOutput(new FileOutputStream(new File(pdf)));
            transcoder.transcode(transcoderInput, transcoderOutput);
            transcoderOutput.getOutputStream().close();

            return pdf;
        } catch (Exception e) {
            e.printStackTrace();
            return " ";
        }

    }

    private String exportSVG(String file) {
        try {
            String fileName = file;
            try {
                String file1 = new String(PATHTOTOOLS);
                String file2 = new String(PATHTOFILE + file + ".abc");
                String file3 = new String(PATHTOFILE + file + ".svg");
                Process exec = Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", BAT_SVG, file1, file2, file3});
                while (exec.waitFor() != 0);
            } catch (Exception e) {
            }
            String path = PATHTOFILE + fileName + ".svg";

            String s = new String(Files.readAllBytes(Paths.get(path)));
            s = s.substring(s.indexOf("<svg"), s.indexOf("</svg>") + 6);
            try (PrintStream out = new PrintStream(new FileOutputStream(path))) {
                out.print(s);
            }

            return path;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
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
            e.printStackTrace();
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

    public static String getAbcFile() {
        return PATHTOFILE + "/" + abcFile + ".abc";
    }

    public static String getFileSvg() {
        return fileSvg;
    }

    public static String getFileMidi() {
        return fileMidi;
    }

    public static String getFilePDF() {
        return filePDF;
    }

    public static String getFileXHTML() {
        return fileXHTML;
    }

    public String[] compile(String filename, ArrayList<Clave> pautas) {
        StringBuilder score = make(filename, pautas);
        abcFile = makeABCFile(score);
        fileSvg = exportSVG(abcFile);
        fileXHTML = exportXHTML(abcFile);
        fileMidi = exportMIDI(abcFile);
        filePDF = exportPDF(abcFile);

        return new String[]{abcFile, fileSvg, fileMidi};
    }

    private StringBuilder make(String fileName, ArrayList<Clave> pautas) {
        score = new StringBuilder();
        score.append("X: 1").append(getTitulo(fileName)).append("\n");
        score.append(ABC_NOTATION.TITULO).append(" ").append(getTitulo(fileName)).append("\n");
        score.append(ABC_NOTATION.COMPOSITOR).append(" ").append(getCompositor()).append("\n");
        score.append(ABC_NOTATION.TONALIDADE).append(" ").append(getTonalidade()).append("\n");
        score.append(ABC_NOTATION.MEDIDA).append(" ").append(getMedida()).append("\n");
        score.append(getMusica(pautas));

        return score;
    }

    //Fixo por enquanto testar compilador
    private String getTitulo(String dsTitulo) {
        return dsTitulo.replace(".png", "").substring(0, 50);
    }

    private String getCompositor() {
        return "Trasncrypt by ScoreReader";
    }

    //Fixo por enquanto testar compilador
    private String getMedida() {
        return "4/4";
    }

    //Fixo por enquanto testar compilador
    private String getTonalidade() {
        return "D";
    }

    //Fixo por enquanto testar compilador
    private String getMusica(ArrayList<Clave> pautas) {
        StringBuilder abcCode = new StringBuilder();

        for (Clave pauta : pautas) {
            for (Figura elemento : pauta.getFigurasPauta()) {
                abcCode.append(elemento.getNota().nome).append(getTipo(elemento));
            }
        }

        abcCode.append(ABC_NOTATION.FINAL);

        return abcCode.toString();
    }

    private String getTipo(Figura elemento) {
        if ("Minima".equalsIgnoreCase(elemento.tipo)) {
            return ABC_NOTATION.MINIMA;
        } else if ("Seminima".equalsIgnoreCase(elemento.tipo)) {
            return ABC_NOTATION.SEMINIMA;
        } else if ("BarraCompasso".equalsIgnoreCase(elemento.tipo)) {
            return ABC_NOTATION.BARRA_COMPASSO;
        }
        return "Desconhecido";
    }

}
