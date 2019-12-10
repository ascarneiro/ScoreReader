/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scorereader.abc;

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
import scorereader.ScoreReader;

import scorereader.structure.Figura;
import scorereader.structure.claves.Pauta;

public class Parser {

    private ScoreReader scoreReader = null;
    private static String abcFile = "";
    private static String fileSvg = "";
    private static String filePDF = "";
    private static String fileMidi = "";
    private static String fileXHTML = "";
    private static String fileMUSICXML = "";

    private static final int quebrarEmCompassos = 3;
    private static String PATHTOFILE = "C:\\Users\\ascarneiro\\Desktop\\TCC\\ScoreReader\\output\\";
    private String PATHTOTOOLS = "C:\\Users\\ascarneiro\\Desktop\\TCC\\ScoreReader\\tools\\";

    private String BAT_SVG = "C:\\Users\\ascarneiro\\Desktop\\TCC\\ScoreReader\\tools\\svg.bat";
    private String BAT_MIDI = "C:\\Users\\ascarneiro\\Desktop\\TCC\\ScoreReader\\tools\\midi.bat";
    private String BAT_MUSICXML = "C:\\Users\\ascarneiro\\Desktop\\TCC\\ScoreReader\\tools\\musicxml.bat";

    private StringBuilder score = new StringBuilder();

    public Parser(ScoreReader scoreReader) {
        this.scoreReader = scoreReader;
    }

    private String exportXHTML(String file) {
        String fileName = file;
        try {
            String file1 = new String(PATHTOTOOLS);
            String file2 = new String(PATHTOFILE + file + ".abc");
            String file3 = new String(PATHTOFILE + file + ".xhtml");
            Process exec = Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", BAT_SVG, file1, file2, file3});
            while (exec.waitFor() != 0);
            scoreReader.log_console("Arquivo XHTML gerado com sucesso....");
        } catch (Exception e) {
            e.printStackTrace();
            scoreReader.log_console("Falha ao gerar arquivo XHTML ....");
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
            scoreReader.log_console("Arquivo PDF gerado com sucesso....");
            return pdf;
        } catch (Exception e) {
            scoreReader.log_console("Falha ao gerar arquivo PDF ....");
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
                scoreReader.log_console("Arquivo SVG gerado com sucesso....");
            } catch (Exception e) {
                scoreReader.log_console("Falha ao gerar arquivo SVG ....");
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

    private String exportMUSICXML(String file) {
        String fileName = file;
        try {
            fileName = file.replace(".abc", ".musicxml");
            String file1 = new String(PATHTOTOOLS);
            String file2 = new String(PATHTOFILE + file + ".abc");
            String file3 = new String(PATHTOFILE);
            Process exec = Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", BAT_MUSICXML, file1, file2, file3});
            while (exec.waitFor() != 0);
            scoreReader.log_console("Arquivo MUSICXML gerado com sucesso....");
        } catch (Exception e) {
            scoreReader.log_console("Falha ao gerar arquivo MUSICXML ....");
            e.printStackTrace();
        }

        String musicXMLFile = PATHTOFILE + fileName + ".xml";
        File f = new File(musicXMLFile);
        if (f.exists()) {
            f.renameTo(new File(musicXMLFile.replace(".xml", ".musicxml")));
        }
        return musicXMLFile.replace(".xml", ".musicxml");
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
            scoreReader.log_console("Arquivo MIDI gerado com sucesso....");
        } catch (Exception e) {
            scoreReader.log_console("Falha ao gerar arquivo MIDI ....");
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
            scoreReader.log_console("Arquivo ABC Notation gerado com sucesso....");
        } catch (Exception e) {
            scoreReader.log_console("Falha ao gerar arquivo ABC Notation ....");
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

    public static String getFileMUSICXML() {
        return fileMUSICXML;
    }

    public String[] compile(String filename, ArrayList<Pauta> pautas) {
        StringBuilder score = make(filename, pautas);
        abcFile = makeABCFile(score);
        fileSvg = exportSVG(abcFile);
        fileXHTML = exportXHTML(abcFile);
        fileMidi = exportMIDI(abcFile);
        filePDF = exportPDF(abcFile);
        fileMUSICXML = exportMUSICXML(abcFile);

        return new String[]{abcFile, fileSvg, fileMidi, fileMUSICXML};
    }

    private StringBuilder make(String fileName, ArrayList<Pauta> pautas) {
        score = new StringBuilder();
        score.append("X: 1").append(getTitulo(fileName)).append("\n");
        score.append(ABC_NOTATION.TITULO).append(" ").append(getTitulo(fileName)).append("\n");
        score.append(ABC_NOTATION.COMPOSITOR).append(" ").append(getCompositor());
        score.append(getElementos(pautas));

        return score;
    }

    //Fixo por enquanto testar compilador
    private String getTitulo(String dsTitulo) {
        return "Exemplo ScoreReader";
    }

    private String getCompositor() {
        return "Transcrito por prototipo Score Reader Alan Soares Carneiro";
    }

    private String getElementos(ArrayList<Pauta> pautas) {
        StringBuilder abcCode = new StringBuilder();
        int qtQuebra = 0;
        for (Pauta pauta : pautas) {
            boolean inicioNotasFimClave = false;
            String armaduraClave = getElementoArmaduraClave(pauta);
            abcCode.append(armaduraClave);

            for (Figura elemento : pauta.getFigurasPauta()) {
                if (elemento.isNotaMusical()) {
                    abcCode.append(elemento.getNota().nome).append(getTipoNota(elemento));
                    inicioNotasFimClave = true;
                } else if (inicioNotasFimClave) {

                    String figura = getTipoPausa(elemento);
                    if (figura.isEmpty()) {
                        figura = getTipoFigura(elemento);
                        if ("Semibreve".equalsIgnoreCase(figura)
                                || "Minima".equalsIgnoreCase(figura)
                                || "Seminima".equalsIgnoreCase(figura)
                                || "Colcheia".equalsIgnoreCase(figura)) {
                            //Caso nao identificar o nome da nota mas for nota musical usa padrao G (Sol)
                            figura = "G".concat(figura);
                        }
                    }
                    abcCode.append(figura);
                    qtQuebra++;
                }

            }
        }
        abcCode.append(ABC_NOTATION.FINAL);
        return abcCode.toString();
    }

    private String getTipoPausa(Figura elemento) {
        if ("PausaSemibreve".equalsIgnoreCase(elemento.tipo)) {
            return ABC_NOTATION.PAUSA_SEMIBREVE;
        } else if ("PausaMinima".equalsIgnoreCase(elemento.tipo)) {
            return ABC_NOTATION.PAUSA_MINIMA;
        } else if ("PausaSeminima".equalsIgnoreCase(elemento.tipo)) {
            return ABC_NOTATION.PAUSA_SEMINIMA;
        } else if ("PausaColcheia".equalsIgnoreCase(elemento.tipo)) {
            return ABC_NOTATION.PAUSA_COLCHEIA;
        } else if ("PausaSemiColcheia".equalsIgnoreCase(elemento.tipo)) {
            return ABC_NOTATION.PAUSA_SEMICOLCHEIA;
        }
        return "";
    }

    private String getElementoArmaduraClave(Pauta pauta) {
        StringBuilder armadura = new StringBuilder();
        int qtSustenidos = 0;
        int qtBemois = 0;
        String tempo = "4/4";
        String clave = "C";
        String tonalidade = "C";

        for (Figura figura : pauta.getFigurasPauta()) {
            //Quando encontrar a primeira nota musical cai fora pois acabou a armadura
            if (figura.isNotaMusical()) {
                break;
            }
            if ("ClaveSol".equalsIgnoreCase(figura.tipo)) {
                clave = ABC_NOTATION.CLAVE_SOL;
            } else if ("ClaveFa".equalsIgnoreCase(figura.tipo)) {
                clave = ABC_NOTATION.CLAVE_FA;
            } else if ("ClaveDo".equalsIgnoreCase(figura.tipo)) {
                clave = ABC_NOTATION.CLAVE_DO;
            } else if ("Sustenido".equalsIgnoreCase(figura.tipo)) {
                qtSustenidos++;
            } else if ("Bemol".equalsIgnoreCase(figura.tipo)) {
                qtBemois++;
            } else if ("MarcacaoTempo".equalsIgnoreCase(figura.tipo)) {
                //Ver regra tempo, fracao e caracter C
                tempo = "4/4"; //Seguir nomenclatura fracoes
            }
        }

        if (qtSustenidos > qtBemois) {
            tonalidade = ABC_NOTATION.TONALIDADE_SUSTENIDO[qtSustenidos];
        } else {
            tonalidade = ABC_NOTATION.TONALIDADE_BEMOL[qtBemois];
        }

        armadura.append("\n").append(ABC_NOTATION.TONALIDADE_E_CLAVE).append(tonalidade).append(" ").append(clave);
        armadura.append("\n").append(ABC_NOTATION.MEDIDA).append(tempo).append("\n");

        return armadura.toString();
    }

    private String getTipoFigura(Figura elemento) {
        if ("BarraCompasso".equalsIgnoreCase(elemento.tipo)) {
            return ABC_NOTATION.BARRA_COMPASSO;
        } else if ("Sustenido".equalsIgnoreCase(elemento.tipo)) {
            return ABC_NOTATION.SUSTENIDO;
        } else if ("Bemol".equalsIgnoreCase(elemento.tipo)) {
            return ABC_NOTATION.BEMOL;
        } else if ("Bequadro".equalsIgnoreCase(elemento.tipo)) {
            return ABC_NOTATION.BEQUADRO;

        }
        return "";
    }

    private String getTipoNota(Figura elemento) {
        if ("Semibreve".equalsIgnoreCase(elemento.tipo)) {
            return ABC_NOTATION.SEMIBREVE;
        } else if ("Minima".equalsIgnoreCase(elemento.tipo)) {
            return ABC_NOTATION.MINIMA;
        } else if ("Seminima".equalsIgnoreCase(elemento.tipo)) {
            return ABC_NOTATION.SEMINIMA;
        } else if ("Colcheia".equalsIgnoreCase(elemento.tipo)) {
            return ABC_NOTATION.COLCHEIA;
        } else if ("SemiColcheia".equalsIgnoreCase(elemento.tipo)) {
            return ABC_NOTATION.SEMICOLCHEIA;
        }
        return "";
    }

}
