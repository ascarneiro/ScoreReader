package scorereader.image;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.File;
import java.io.FileOutputStream;
import java.util.TreeMap;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileFilter;
import org.bytedeco.javacpp.Loader;
import static org.bytedeco.javacpp.helper.opencv_core.CV_RGB;
import static org.bytedeco.javacpp.helper.opencv_imgcodecs.cvLoadImage;
import static org.bytedeco.javacpp.helper.opencv_imgcodecs.cvSaveImage;
import org.bytedeco.javacpp.opencv_core.CvContour;
import org.bytedeco.javacpp.opencv_core.CvMemStorage;
import org.bytedeco.javacpp.opencv_core.CvPoint;
import org.bytedeco.javacpp.opencv_core.CvRect;
import org.bytedeco.javacpp.opencv_core.CvSeq;
import org.bytedeco.javacpp.opencv_core.IplImage;
import static org.bytedeco.javacpp.opencv_core.cvGet2D;
import static org.bytedeco.javacpp.opencv_core.cvGetSeqElem;
import static org.bytedeco.javacpp.opencv_imgproc.CV_ADAPTIVE_THRESH_MEAN_C;
import static org.bytedeco.javacpp.opencv_imgproc.CV_CHAIN_APPROX_NONE;
import static org.bytedeco.javacpp.opencv_imgproc.CV_RETR_CCOMP;
import static org.bytedeco.javacpp.opencv_imgproc.CV_THRESH_BINARY;
import static org.bytedeco.javacpp.opencv_imgproc.CV_THRESH_BINARY_INV;
import static org.bytedeco.javacpp.opencv_imgproc.cvAdaptiveThreshold;
import static org.bytedeco.javacpp.opencv_imgproc.cvBoundingRect;
import static org.bytedeco.javacpp.opencv_imgproc.cvDilate;
import static org.bytedeco.javacpp.opencv_imgproc.cvErode;
import static org.bytedeco.javacpp.opencv_imgproc.cvFindContours;
import static org.bytedeco.javacpp.opencv_imgproc.cvLine;
import static org.bytedeco.javacpp.opencv_imgproc.cvRectangleR;
import static org.bytedeco.javacpp.opencv_imgproc.cvThreshold;
import static scorereader.Utilities.IplImageToBufferedImage;

/**
 *
 * @author jonathan
 * @version 1.0
 *
 */
public class Prototipo extends JFrame {

    public static final int CV_LOAD_IMAGE_UNCHANGED = -1,
            CV_LOAD_IMAGE_GRAYSCALE = 0,
            CV_LOAD_IMAGE_COLOR = 1,
            CV_LOAD_IMAGE_ANYDEPTH = 2,
            CV_LOAD_IMAGE_ANYCOLOR = 4;

    //private static IplImage imagemSK; //caso reconstruir idft em imagem colorida
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Prototipo().setVisible(true);
            }
        });
    }

    /**
     * O processo é executado na constução
     */
    public Prototipo() {

        boolean debug = false; //grava imagens intermediarias da construção

        //opcoes de tela
        setTitle("Prototipo");
        setName("Svm test");
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        JFileChooser arquivo = new JFileChooser("C:\\Users\\ascarneiro\\Desktop\\TCC\\ScoreReader\\repository\\Casos\\Recortados monografia\\");//arquivo de imagem de entrada
        arquivo.setFileFilter(new FiltroArquivoDescArq());
        if (arquivo.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            System.exit(0);
        }

        int contador = 1;//para gravar sequencia imagem
        IplImage imagemOriginal = carregaImagem(arquivo.getSelectedFile());
        IplImage imagemCinza = binarizar(arquivo.getSelectedFile());

        gravaEstadoImagem(imagemOriginal, contador++, debug);
        gravaEstadoImagem(imagemCinza, contador++, debug);

        imagemCinza = retiraLinhasdePauta(imagemCinza);
        gravaEstadoImagem(imagemCinza, contador++, debug);
        imagemCinza = morfologiaMatematica(imagemCinza);
        gravaEstadoImagem(imagemCinza, contador++, debug);

        IplImage imagemBB = imagemCinza.clone();
        //deixar umagemBB branca for (int a = 0; a < imagemBB.width(); a++) {for (int b = 0; b < imagemBB.height(); b++) {cvSet2D(imagemBB, b, a, CvScalar.WHITE);}}

        /**
         *
         * inicio da segmentação
         *
         */
        //inverte cor
        cvAdaptiveThreshold(imagemCinza, imagemCinza, 255,
                CV_ADAPTIVE_THRESH_MEAN_C, CV_THRESH_BINARY_INV, 11, 5);

        /**
         * inicio estocar contornos
         */
        CvMemStorage storage = CvMemStorage.create();
        CvSeq contours = new CvContour();
        cvFindContours(imagemCinza, storage, contours, Loader.sizeof(CvContour.class),
                CV_RETR_CCOMP, CV_CHAIN_APPROX_NONE, new CvPoint(0, 0));
        /**
         * fim estocar contornos
         */
        TreeMap<Integer, String> Listaformas = new TreeMap();

        /**
         * Para cada forma encontrada
         */
        for (; contours != null; contours = contours.h_next()) {
            CvRect r = cvBoundingRect(contours, 0);
            if ((r.height() * r.width()) > 10) {//se a area do contorno for maior que a area minima
                desenhaBB(imagemBB, cvBoundingRect(contours, 0));
                gravaEstadoImagem(imagemBB, contador++, debug);

                /**
                 * inicia lista pontos do contorno
                 */
                int[] pontoX = new int[contours.total()];
                int[] pontoY = new int[contours.total()];
                for (int i = 0; i < contours.total(); i++) {
                    CvPoint p = new CvPoint(cvGetSeqElem(contours, i));
                    pontoX[i] = p.x();
                    pontoY[i] = p.y();
                    // marcar contornos com vermelho
                    //cvLine(imagemOriginal, p, p, opencv_core.CvScalar.RED, 1, 4, 0);
                }
                /**
                 * fim lista pontos do contorno
                 */
                double[] descritores = descritores(pontoX, pontoY, 6);

                //ordena as formas
                int indice = pontoX[0];
                while (Listaformas.containsKey(indice)) {
                    indice++;
                }

                Listaformas.put(indice, identificaForma(descritores));
                if (debug) {//escreve os valores dos descritores encontrados
                    System.out.println("n=" + indice
                            + "	" + descritores[0]
                            + "	" + descritores[1]
                            + "	" + descritores[2]
                            + "	" + descritores[3]
                            + "	" + descritores[4]
                            + "	" + descritores[5]);
                }
            }
        }

        gravaEstadoImagem(imagemBB, contador++, debug);
        gravaArquivoReconhecimento(arquivo.getSelectedFile(), Listaformas);
        exibeResultado(imagemOriginal, imagemBB, Listaformas);
    }

    /**
     *
     * @param arquivo - referencia do arquivo a ser carregada / partitura
     * @return imagem carregada todas cores
     */
    private IplImage carregaImagem(File arquivo) {
        return cvLoadImage(arquivo.getAbsolutePath(), CV_LOAD_IMAGE_ANYCOLOR);
    }

    /**
     *
     * @param arquivo - referencia do arquivo a ser carregada / partitura
     * @return - imagem em tom de cinza com limiar
     */
    private IplImage binarizar(File arquivo) {
        //cvThreshold(cinza, cinza, 25, 255, CV_THRESH_OTSU); optativo
        IplImage load = cvLoadImage(arquivo.getAbsolutePath(), CV_LOAD_IMAGE_GRAYSCALE);
        cvThreshold(load, load, 20, 255, CV_THRESH_BINARY);
        return load;
    }

    /**
     *
     * @param imagem - imagem binarizada
     * @return - imagem sem as linhas horizontais
     *
     * ocorrem duas buscas 1 - maior quantidade 2 - pintar de branco as
     * similares
     */
    private IplImage retiraLinhasdePauta(IplImage imagem) {
        int[] hist = new int[imagem.cvSize().height()];
        int maior = 0;
        for (int a = 0; a < imagem.cvSize().height(); a++) {
            for (int b = 0; b < imagem.cvSize().width(); b++) {
                if (cvGet2D(imagem, a, b).val(0) <= 10) {
                    hist[a]++;
                }
            }
            if (maior <= hist[a]) {
                maior = hist[a];
            }
        }
        for (int i = 0; i < hist.length; i++) {
            if (hist[i] >= (maior * 0.8)) {
                CvPoint pt1 = new CvPoint(0, i);
                CvPoint pt2 = new CvPoint(imagem.width(), i);
                cvLine(imagem, pt1, pt2, CV_RGB(255, 255, 255), 1, 4, 0);
            }
        }
        return imagem;
    }

    /**
     *
     * @param imagem - imagem sem linhas de pauta e fragmentada
     * @return - simbolos segmentados
     */
    private IplImage morfologiaMatematica(IplImage imagem) {
        cvErode(imagem, imagem, null, 3);
        cvDilate(imagem, imagem, null, 2);
        return imagem;
    }

    /**
     *
     * @param imagem - qualquer imagem
     * @param bb - coordenadas de um retangulo - grava o retangulo na imagem (um
     * pouco maior)
     */
    private void desenhaBB(IplImage imagem, CvRect bb) {
        bb.x(bb.x() - 5);
        bb.y(bb.y() - 5);
        bb.width(bb.width() + 10);
        bb.height(bb.height() + 10);
        cvRectangleR(imagem, bb, CV_RGB(0, 255, 0), 1, 8, 0);
    }

    /**
     *
     * @param x lista das coordenadas x
     * @param y lista das coordenadas y
     * @param quantidade de descritores a ser gerada
     * @return lista contendo os descritores gerados
     */
    private static double[] descritores(int[] x, int[] y, int quantidade) {
        //DecimalFormat df = new DecimalFormat("0.00000");

        int U = x.length;  //quantidade de descritores
        int K = x.length; //tamanho da lista pontos

        double[] fReal = new double[U];
        double[] fImag = new double[U];

        for (int u = 0; u < U; ++u) {//para cada descritor
            for (int k = 0; k < K; ++k) {//percorre cada ponto do contorno
                double theta = (2 * Math.PI * u * k) / K; //T = theta
                fReal[u] += x[k] * Math.cos(theta) + y[k] * Math.sin(theta); //parte real
                fImag[u] += y[k] * Math.cos(theta) - x[k] * Math.sin(theta); //parte imaginaria descrita por i
            }
        }// fim encontra dft's 

        //////////// normalizar 
        //% invariant translacao matlab -> f([1]) = 0        
        fReal[0] = 0;
        fImag[0] = 0;
        //fim invariacao translacao

        //invariancia rotação
        double[] normalizado = new double[fReal.length];
        for (int u = 1; u < fReal.length; u++) {
            normalizado[u] = Math.sqrt(Math.pow(fReal[u], 2) + Math.pow(fImag[u], 2));
        }//fim invariancia

        /*
         //invariant escala. em matlab -> f = f / abs(f(2))
         //|Z| = Math.sqrt(a² + b²)
         //double sI = Math.sqrt(fReal[1] * fReal[1] + fImag[1] * fImag[1]);//sI = segundo item
         double sI = Math.abs(normalizado[1]);

         for (int i = 0; i < fReal.length; i++) {
         normalizado[i] = normalizado[i] / sI;
         } //fim invariancia escala
         */
        //DecimalFormat df = new DecimalFormat("0.00000");for (int i = 0; i < normalizado.length; i++) {System.out.print(df.format(normalizado[i]) + ";");}System.out.println(normalizado.length);

        /*
         //resize sem n 0 e 1
         {double[] temp = new double[normalizado.length - 2];
         System.arraycopy(normalizado, 2, temp, 0, temp.length);
         normalizado = temp;
         }
         //resize
         normalizado = novoTamanho(normalizado, quantidade);//seleciona N-elementos centrais
         //fim resize
         */
        {
            double[] temp = new double[6];
            int indice = 1; //pula o primeiro = 0 invariancia translação
            temp[0] = normalizado[indice];
            temp[1] = normalizado[++indice];
            temp[2] = normalizado[++indice];
            indice = normalizado.length;
            temp[3] = normalizado[--indice];
            temp[4] = normalizado[--indice];
            temp[5] = normalizado[--indice];
            normalizado = temp;
        }

        /**
         * restaura a dft nao utilizado, mas seria proximo a isso
         *
         * double[] Xnovo = new double[quantidade]; double[] Ynovo = new
         * double[quantidade]; for (int k = 0; k < quantidade; ++k) { for (int u
         * = 0; u < quantidade; ++u) { double theta = (2 * Math.PI * u * k) / U;
         * Xnovo[k] += fReal[u] * Math.cos(theta) - fImag[u] * Math.sin(theta);
         * Ynovo[k] += fReal[u] * Math.sin(theta) + fImag[u] * Math.cos(theta);
         * } Xnovo[k] = Xnovo[k] / U; Ynovo[k] = Ynovo[k] / U; }
         *
         * // plot em imagem em cor System.out.println("x novo y novo"); for
         * (int i = 1; i < Xnovo.length; i++) {//CvPoint pt1 = new CvPoint((int)
         * Xnovo[i - 1], (int) Ynovo[i - 1]);//CvPoint pt2 = new CvPoint((int)
         * Xnovo[i], (int) Ynovo[i]); CvPoint pt1 = new CvPoint((int) Xnovo[i -
         * 1], (int) Ynovo[i - 1]); CvPoint pt2 = new CvPoint((int) Xnovo[i],
         * (int) Ynovo[i]); cvLine(imagemSK, pt1, pt2, CV_RGB(255, 0, 0), 1, 4,
         * 0); gravaEstadoImagem(imagemSK, 150+i); } gravaEstadoImagem(imagemSK,
         * 150); // fim restaurar
         */
        return normalizado;
    }

    /**
     *
     * @param normalizado lista com descritores
     * @param n quantidade <= quantidade de normalizado @return lista de n e
     * lementos de normalizado - exclui os centrais
     */
    private static double[] novoTamanho(double[] normalizado, int n) {
        normalizado = flip(normalizado);
        double[] temp = new double[n];
        int inicio = (int) (Math.ceil((normalizado.length - n) / 2));

        if (((normalizado.length % 2 == 0) && (n % 2 != 0)) || ((normalizado.length % 2 != 0) && ((n % 2 == 0)))) {
            inicio += 1;
        }
        System.arraycopy(normalizado, inicio, temp, 0, n);
        temp = iflip(temp);
        return temp;
    }

    /**
     *
     * @param a lista de descritores ou double
     * @return inverte lista em Metade A e Metade B ex: flip('ABCD') = 'CDAB'
     */
    private static double[] flip(double[] a) {
        double[] temp = new double[a.length];
        int metade = (int) (Math.floor(a.length) / 2);
        int meio = 1; // 1 caso quantidade impar

        if ((a.length % 2) == 0) {
            meio = 0;
        }
        System.arraycopy(a, metade + meio, temp, 0, metade);
        System.arraycopy(a, 0, temp, metade, metade + meio);
        return temp;
    }

    /**
     *
     * @param a lista de descritores ou double
     * @return inverte lista em Metade A e Metade B ex: flip('ABCD') = 'CDAB' se
     * faz necessário quando tiver uma quantidade impar. senao o centro para na
     * posição 0
     */
    private static double[] iflip(double[] a) {
        double[] temp = new double[a.length];
        int metade = (int) (Math.floor(a.length) / 2);
        int meio = 1; // 1 caso quantidade impar

        if ((a.length % 2) == 0) {
            meio = 0;
        }
        System.arraycopy(a, metade, temp, 0, metade + meio);
        System.arraycopy(a, 0, temp, metade + meio, metade);
        return temp;
    }

    /**
     *
     * @param imagem - imagem para gravar no diretorio do projeto
     * @param contadorGravarImagem - contador para nao sobrescrever e manter a
     * ordem
     * @param debug - flag para gerar ou nao
     */
    private static void gravaEstadoImagem(IplImage imagem, int contadorGravarImagem, boolean debug) {
        if (debug) {
            cvSaveImage("Resultado " + contadorGravarImagem + ".png", imagem);
        }
    }

    /**
     *
     * @param file - para saber onde esta o arquivo
     * @param listaFormas - lista das formas encontradas, ja identificadas e em
     * ordem
     */
    private void gravaArquivoReconhecimento(File file, TreeMap<Integer, String> listaFormas) {
        try {
            try (FileOutputStream fos = new FileOutputStream(
                    new File(file.getParent() + "\\reconhecimento.txt"), false)) {
                String formas = listaFormas.values().toString();
                fos.write(formas.getBytes());
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    /**
     *
     * @param imagemOriginal - sem modificação
     * @param imagemBB - contendo a segmentação
     * @param Formas - resultado da classificação individual
     */
    private void exibeResultado(IplImage imagemOriginal, IplImage imagemBB, TreeMap<Integer, String> Formas) {
        Container container = getContentPane();
        JLabel label1 = new JLabel(new ImageIcon(IplImageToBufferedImage(imagemOriginal)));
        JLabel label2 = new JLabel(new ImageIcon(IplImageToBufferedImage(imagemBB)));
        String formas = Formas.values().toString();
        JTextArea area = new JTextArea(formas);
        area.setRows(2);

        JScrollPane tela = new JScrollPane();

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(1, 3));
        panel.add(label1, BorderLayout.NORTH);
        panel.add(label2, BorderLayout.CENTER);
        panel.add(area, BorderLayout.SOUTH);

        //adiciona o panel no container  
        tela.setViewportView(panel);
        container.add(tela, BorderLayout.CENTER);
        //container.setContentPane(tela);  
        pack();
        setVisible(true);
    }

    /**
     *
     * @param descritores encontrados/gerados
     * @return retorna o rotulo ou nome associado com a faixa
     */
    private String identificaForma(double[] descritores) {
        if (entre(//Clave de Sol
                entre(descritores[0], 10900, 10910),
                entre(descritores[1], 1360, 1370),
                entre(descritores[2], 1890, 1900),
                entre(descritores[3], 17095, 17100),
                entre(descritores[4], 3515, 3520),
                entre(descritores[5], 2390, 2400))) {
            return "Clave de sol";
        }
        if (entre(//seminima
                entre(descritores[0], 2730, 3700),
                entre(descritores[1], 610, 1770),
                entre(descritores[2], 40, 400),
                entre(descritores[3], 3540, 5100),
                entre(descritores[4], 750, 1000),
                entre(descritores[5], 520, 1000))) {
            return "Seminima";
        }
        if (entre(//Clave de do
                entre(descritores[0], 2100, 2110),
                entre(descritores[1], 30, 40),
                entre(descritores[2], 1280, 1310),
                entre(descritores[3], 10350, 10370),
                entre(descritores[4], 2400, 2500),
                entre(descritores[5], 1900, 2000))) {
            return "Clave de Do";
        }

        if (entre( // p 1/1			
                entre(descritores[0], 1130, 1250),
                entre(descritores[1], 200, 270),
                entre(descritores[2], 40, 110),
                entre(descritores[3], 2200, 2370),
                entre(descritores[4], 140, 220),
                entre(descritores[5], 200, 300))) {
            return "Pausa 1/1";
        }
        if (entre( // pausa 1/2			
                entre(descritores[0], 550, 600),
                entre(descritores[1], 320, 370),
                entre(descritores[2], 40, 50),
                entre(descritores[3], 1190, 1250),
                entre(descritores[4], 230, 270),
                entre(descritores[5], 200, 220))) {
            return "Pausa 1/2";
        }
        if (entre( // colcheia 1			
                entre(descritores[0], 1800, 2450),
                entre(descritores[1], 2730, 2950),
                entre(descritores[2], 750, 950),
                entre(descritores[3], 4750, 5270),
                entre(descritores[4], 2130, 2270),
                entre(descritores[5], 1860, 2030))) {
            return "Colcheia";
        }
        if (entre( // barra compasso			
                entre(descritores[0], 2800, 3000),
                entre(descritores[1], 0, 10),
                entre(descritores[2], 250, 300),
                entre(descritores[3], 3200, 3400),
                entre(descritores[4], 0, 10),
                entre(descritores[5], 350, 450))) {
            return "Barra Compasso";
        }
        if (entre( // semibreve			
                entre(descritores[0], 300, 350),
                entre(descritores[1], 0, 30),
                entre(descritores[2], 5, 30),
                entre(descritores[3], 1500, 1580),
                entre(descritores[4], 0, 30),
                entre(descritores[5], 130, 150))) {
            return "Semibreve";
        }
        if (entre( // clave fá			
                entre(descritores[0], 1250, 1260),
                entre(descritores[1], 2530, 2570),
                entre(descritores[2], 1530, 1550),
                entre(descritores[3], 5350, 5400),
                entre(descritores[4], 3020, 3030),
                entre(descritores[5], 1120, 1140))) {
            return "Clave de fa";
        }
        if (entre( // formula 44			
                entre(descritores[0], 3300, 3350),
                entre(descritores[1], 150, 190),
                entre(descritores[2], 220, 290),
                entre(descritores[3], 6820, 6920),
                entre(descritores[4], 250, 290),
                entre(descritores[5], 1000, 1100))) {
            return "Formula 44";
        }
        if (entre( // semicolcheia 1			
                entre(descritores[0], 1820, 2120),
                entre(descritores[1], 2680, 2840),
                entre(descritores[2], 290, 370),
                entre(descritores[3], 5550, 5850),
                entre(descritores[4], 1650, 1810),
                entre(descritores[5], 1660, 1800))) {
            return "Semicolcheia";
        }
        if (entre( // colcheia 2			
                entre(descritores[0], 2290, 2425),
                entre(descritores[1], 305, 350),
                entre(descritores[2], 315, 340),
                entre(descritores[3], 4725, 4825),
                entre(descritores[4], 165, 270),
                entre(descritores[5], 500, 535))) {
            return "Colcheia";
        }
        if (entre( // p14			
                entre(descritores[0], 1450, 1515),
                entre(descritores[1], 500, 540),
                entre(descritores[2], 530, 570),
                entre(descritores[3], 2630, 2700),
                entre(descritores[4], 810, 860),
                entre(descritores[5], 290, 330))) {
            return "Pausa 1/4";
        }
        if (entre( // formula 24			
                entre(descritores[0], 4200, 4230),
                entre(descritores[1], 200, 250),
                entre(descritores[2], 450, 500),
                entre(descritores[3], 7500, 7550),
                entre(descritores[4], 920, 950),
                entre(descritores[5], 700, 720))) {
            return "Formula 24";
        }
        if (entre( // semicolcheia 2			
                entre(descritores[0], 2270, 2330),
                entre(descritores[1], 305, 340),
                entre(descritores[2], 280, 300),
                entre(descritores[3], 4800, 4905),
                entre(descritores[4], 215, 270),
                entre(descritores[5], 530, 555))) {
            return "Semicolcheia";
        }
        if (entre( // fusa2			
                entre(descritores[0], 2265, 2380),
                entre(descritores[1], 305, 350),
                entre(descritores[2], 260, 390),
                entre(descritores[3], 4715, 4875),
                entre(descritores[4], 225, 250),
                entre(descritores[5], 525, 550))) {
            return "Fusa";
        }
        if (entre( // fusa1			
                entre(descritores[0], 2360, 2370),
                entre(descritores[1], 2520, 2530),
                entre(descritores[2], 550, 560),
                entre(descritores[3], 6550, 6570),
                entre(descritores[4], 1180, 1190),
                entre(descritores[5], 1040, 1050))) {
            return "Fusa";
        }
        if (entre( // p10			
                entre(descritores[0], 350, 390),
                entre(descritores[1], 0, 5),
                entre(descritores[2], 50, 80),
                entre(descritores[3], 1050, 1100),
                entre(descritores[4], 0, 5),
                entre(descritores[5], 100, 110))) {
            return "Pausa 2/1";
        }
        if (entre( // minima			
                entre(descritores[0], 2900, 3590),
                entre(descritores[1], 700, 770),
                entre(descritores[2], 50, 100),
                entre(descritores[3], 3800, 4450),
                entre(descritores[4], 850, 950),
                entre(descritores[5], 750, 900))) {
            return "Minima";
        }
        if (entre( // ponto			
                entre(descritores[0], 0, 3),
                entre(descritores[1], 0, 5),
                entre(descritores[2], 0, 10),
                entre(descritores[3], 100, 150),
                entre(descritores[4], 0, 10),
                entre(descritores[5], 0, 5))) {
            return "Ponto";
        }
        if (entre( // abreviacao 2			
                entre(descritores[0], 5650, 9100),
                entre(descritores[1], 8150, 9150),
                entre(descritores[2], 2550, 3450),
                entre(descritores[3], 9300, 13100),
                entre(descritores[4], 9600, 12500),
                entre(descritores[5], 1200, 1950))) {
            return "Abreviacao 2s";
        }
        if (entre( // abreviacao 4			
                entre(descritores[0], 32900, 33500),
                entre(descritores[1], 9100, 12500),
                entre(descritores[2], 1200, 3550),
                entre(descritores[3], 45900, 50000),
                entre(descritores[4], 22000, 23000),
                entre(descritores[5], 12600, 13000))) {
            return "Abreviacao 4s";
        }
        return "Nao reconhecida";
    }

    /**
     *
     * @param x valor comparado
     * @param a valor minimo
     * @param b valor máximo
     * @return se x esta no intervalo
     */
    private boolean entre(double x, double a, double b) {
        if ((a <= x) && (x <= b)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     *
     * @param b0 descritor 1
     * @param b1 descritor 2
     * @param b2 descritor 3
     * @param b3 descritor 4
     * @param b4 descritor 5
     * @param b5 descritor 6
     * @return se esta nas faixas
     */
    private boolean entre(boolean b0, boolean b1, boolean b2, boolean b3, boolean b4, boolean b5) {
        return (b0 && b1 && b2 && b3 && b4 && b5);

    }
}

/**
 *
 * @author jonathan apenas filtro para imagem jpg e png
 */
class FiltroArquivoDescArq extends FileFilter {

    String[] extensions;
    String description;

    public FiltroArquivoDescArq() {
        extensions = new String[]{"jpg", "png"};
        description = "Arquivo de Imagem";
    }

    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        for (String extension : extensions) {
            if (f.getName().toLowerCase().endsWith(extension)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
