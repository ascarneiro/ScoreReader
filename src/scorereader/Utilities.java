/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scorereader;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import static org.bytedeco.javacpp.helper.opencv_imgcodecs.cvLoadImage;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import org.bytedeco.javacpp.Loader;
import static org.bytedeco.javacpp.helper.opencv_core.CV_RGB;
import static org.bytedeco.javacpp.helper.opencv_imgcodecs.cvSaveImage;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_imgproc;
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
import static org.bytedeco.javacpp.opencv_imgproc.cvRectangleR;
import static org.bytedeco.javacpp.opencv_imgproc.cvThreshold;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter.ToIplImage;
import static scorereader.image.Prototipo.CV_LOAD_IMAGE_ANYCOLOR;
import scorereader.server.Server;
import scorereader.structure.Crop;
import scorereader.structure.Figura;
import scorereader.structure.Linha;
import scorereader.structure.claves.Clave;
import scorereader.structure.Nota;
import scorereader.structure.claves.ClaveSol;

/**
 *
 * @author ascarneiro
 */
public class Utilities {

    public static boolean DEBUG_IMAGES = false;
    public static boolean DEBUG_VALUES = false;
    public static String DIR_DEBUG = "C:\\Users\\ascarneiro\\Desktop\\TCC\\ScoreReader\\debug\\";

    /**
     * Desenha Bound Box sob os elementos identificados em uma pauta
     *
     * @param imagem
     * @return
     */
    public static opencv_core.IplImage drawBoundBoxes(opencv_core.IplImage imagemCinza, opencv_core.IplImage imagemBB) {
        opencv_core.CvMemStorage storage = opencv_core.CvMemStorage.create();
        opencv_core.CvSeq contours = new opencv_core.CvContour();
        cvFindContours(imagemCinza, storage, contours, Loader.sizeof(opencv_core.CvContour.class),
                CV_RETR_CCOMP, CV_CHAIN_APPROX_NONE, new opencv_core.CvPoint(0, 0));

        int index = 0;
        for (; contours != null; contours = contours.h_next()) {
            opencv_core.CvRect r = cvBoundingRect(contours, 0);
            if ((r.height() * r.width()) > 10) { //se a area do contorno for maior que a area minima
                int add = -100;
                if (index % 2 == 0) {
                    add = -100;
                }
                opencv_core.CvRect bb = cvBoundingRect(contours, 0);

                opencv_core.Mat m = new opencv_core.Mat(contours);
                opencv_core.Moments p = opencv_imgproc.moments(m, false);
                int meioX = (int) ((p.m10() / p.m00()));
                int meioY = (int) ((p.m01() / p.m00()));

                bb.x(bb.x());
                bb.y(bb.y());
                bb.width(bb.width());
                bb.height(bb.height());
//                if (DEBUG_VALUES) {
                // cvRectangleR(imagemBB, bb, CV_RGB(0, 255, 0), 1, 8, 0);
//                }

//                if (DEBUG_VALUES) {
                // Adding Text
                opencv_core.CvScalar c = CV_RGB(0, 0, 0);
                opencv_imgproc.cvPutText(
                        imagemBB, // Matrix obj of the image
                        "(" + meioX + " , " + meioY + ")", // Text to be added
                        new opencv_core.CvPoint(meioX, meioY + add), // point
                        opencv_imgproc.cvFont(1), // front face
                        c // Scalar object for color
                );

                opencv_core.CvScalar rgb = CV_RGB(195, 10, 0);
                opencv_imgproc.cvCircle(imagemBB, new opencv_core.CvPoint(meioX, meioY), 20, rgb);
//                }

            }
            index++;
        }
        return imagemBB;
    }

    /**
     * Recorta os elements identificados na pauta com uma bound box
     *
     * @param imagem
     * @param bb
     * @return
     */
    public static ArrayList<Crop> cropElements(opencv_core.IplImage imagemCinza, opencv_core.IplImage imagemBB) {

        ArrayList<Crop> elements = new ArrayList<Crop>();

        opencv_core.CvMemStorage storage = opencv_core.CvMemStorage.create();
        opencv_core.CvSeq contours = new opencv_core.CvContour();

        cvFindContours(imagemCinza, storage, contours, Loader.sizeof(opencv_core.CvContour.class),
                CV_RETR_CCOMP, CV_CHAIN_APPROX_NONE, new opencv_core.CvPoint(0, 0));

        List<opencv_core.CvSeq> contourList = new ArrayList<>();
        for (; contours != null; contours = contours.h_next()) {
            contourList.add(contours);
        }

        contourList = sortLeftToRightTopToBottom(contourList);

        for (opencv_core.CvSeq c : contourList) {
            opencv_core.CvRect r = cvBoundingRect(c, 0);
            if ((r.height() * r.width()) > 10) { //se a area do contorno for maior que a area minima
                opencv_core.CvRect bb = cvBoundingRect(c, 0);

                opencv_core.Mat m = new opencv_core.Mat(c);
                opencv_core.Moments p = opencv_imgproc.moments(m, false);
                int meioX = (int) ((p.m10() / p.m00()));
                int meioY = (int) ((p.m01() / p.m00()));

                bb.x(bb.x());
                bb.y(bb.y());
                bb.width(bb.width());
                bb.height(bb.height());

                cvRectangleR(imagemBB, bb, CV_RGB(0, 255, 0), 1, 8, 0);

                opencv_core.Rect rectCrop = new opencv_core.Rect(bb.x(), bb.y(), bb.width(), bb.height());
                opencv_core.Mat croppedImage = new opencv_core.Mat(new opencv_core.Mat(imagemBB), rectCrop);
                opencv_core.IplImage crop = new opencv_core.IplImage(croppedImage);

                Crop cr = new Crop(meioX, meioY, bb.width(), bb.height(), crop);
                String s = DIR_DEBUG + "crop" + System.currentTimeMillis() + ".png";
                cr.fileName = s;

                cvSaveImage(s, crop);
                elements.add(cr);

            }
        }
        return elements;
    }

    private static List<opencv_core.CvSeq> sortLeftToRightTopToBottom(List<opencv_core.CvSeq> contourList) {
        //sort by y coordinates using the topleft point of every contour's bounding box
        Collections.sort(contourList, (opencv_core.CvSeq o1, opencv_core.CvSeq o2) -> {
            opencv_core.CvRect rect1 = cvBoundingRect(o1, 0);
            opencv_core.CvRect rect2 = cvBoundingRect(o2, 0);

            int result = Double.compare(rect1.y(), rect2.y());
            return result;
        });

        //sort by x coordinates
        Collections.sort(contourList, (opencv_core.CvSeq o1, opencv_core.CvSeq o2) -> {
            opencv_core.CvRect rect1 = cvBoundingRect(o1, 0);
            opencv_core.CvRect rect2 = cvBoundingRect(o2, 0);
            int result = 0;
            double total = rect1.y() / rect2.y();
            if (total >= 0.9 && total <= 1.4) {
                result = Double.compare(rect1.x(), rect2.x());
            }
            return result;
        });
        return contourList;
    }

    public static opencv_core.IplImage binarizar(opencv_core.IplImage imagem) {
        //cvThreshold(cinza, cinza, 25, 255, CV_THRESH_OTSU); optativo
        cvThreshold(imagem, imagem, 20, 255, CV_THRESH_BINARY);
        return imagem;
    }

    public static byte[] removerLinhasDaPauta(byte[] imageData) throws Exception {

        String encoded = Base64.getEncoder().encodeToString(imageData);
        HashMap<String, Object> params = new HashMap<>();
        params.put("imageEncoded", encoded);
        String retorno = Server.callServerPython("removerLinhasDaPauta", params);
        return Base64.getDecoder().decode(retorno);

    }

    public static void addFiguraDataSource(Figura figura) throws Exception {

        HashMap<String, Object> params = new HashMap<>();
        params.put("tipo", figura.tipo);
        params.put("figura", figura.getBase64Image());
        Server.callServerPython("addFiguraDataSource", params);

    }

    public static void salvarDsScoreReader() throws Exception {

        HashMap<String, Object> params = new HashMap<>();
        Server.callServerPython("salvarDsScoreReader", params);

    }

    public static String carregarModelo(String nome, String tipo) throws Exception {
        HashMap<String, Object> params = new HashMap<>();
        params.put("nome", nome);
        params.put("tipo", tipo);
        String retorno = Server.callServerPython("carregarModelo", params);
        return retorno;
    }

    public static opencv_core.IplImage bufferedImageToIplImage2(byte[] imageData) throws Exception {
        //Loader.load(opencv_core.class);
        ByteArrayInputStream bis = new ByteArrayInputStream(imageData);
        BufferedImage bfImage = ImageIO.read(bis);

        ToIplImage iplConverter = new OpenCVFrameConverter.ToIplImage();
        Java2DFrameConverter java2dConverter = new Java2DFrameConverter();
        IplImage iplImage = iplConverter.convert(java2dConverter.convert(bfImage));

        return iplImage;
    }

    public static opencv_core.IplImage bufferedImageToIplImage(byte[] imageData) throws Exception {

        //ByteArrayInputStream bis = new ByteArrayInputStream(imageData);
        //BufferedImage bfImage = ImageIO.read(bis);
        String path = "C:\\Users\\ascarneiro\\Desktop\\TCC\\ScoreReader\\repository\\staffless.png";
        //ImageIO.write(bfImage, "PNG", new File(path));
        opencv_core.IplImage cvLoadImage = cvLoadImage(path, CV_LOAD_IMAGE_ANYCOLOR);
        cvErode(cvLoadImage, cvLoadImage, null, 2);
        cvDilate(cvLoadImage, cvLoadImage, null, 1);
        cvSaveImage(path, new opencv_core.IplImage(cvLoadImage));
        return cvLoadImage;
//        String path = "C:\\Users\\ascarneiro\\Desktop\\TCC\\ScoreReader\\repository\\staffless.png";
//        Loader.load(opencv_core.class);
//        ByteArrayInputStream bis = new ByteArrayInputStream(imageData);
//        BufferedImage bfImage = ImageIO.read(bis);
//
//        ToIplImage iplConverter = new OpenCVFrameConverter.ToIplImage();
//        Java2DFrameConverter java2dConverter = new Java2DFrameConverter();
//        IplImage cvLoadImage = iplConverter.convert(java2dConverter.convert(bfImage));
//        cvErode(cvLoadImage, cvLoadImage, null, 2);
//        cvDilate(cvLoadImage, cvLoadImage, null, 1);
//        cvSaveImage(path, new opencv_core.IplImage(cvLoadImage));
//        return cvLoadImage;
    }

    public static BufferedImage IplImageToBufferedImage(opencv_core.IplImage src) {
        try {
            OpenCVFrameConverter.ToIplImage grabberConverter = new OpenCVFrameConverter.ToIplImage();
            Java2DFrameConverter paintConverter = new Java2DFrameConverter();
            org.bytedeco.javacv.Frame frame = grabberConverter.convert(src);
            return paintConverter.getBufferedImage(frame, 1);
        } catch (Exception e) {
        }
        return null;
    }

    public static ArrayList<Figura> segmentar(opencv_core.IplImage imagemCinza, opencv_core.IplImage imageCopy) {

        cvAdaptiveThreshold(imagemCinza, imagemCinza, 255,
                CV_ADAPTIVE_THRESH_MEAN_C, CV_THRESH_BINARY_INV, 11, 5);

        ArrayList<Figura> elementos = new ArrayList<Figura>();

        opencv_core.IplImage bounded = drawBoundBoxes(imagemCinza, imageCopy);
        cvSaveImage(DIR_DEBUG + "..\\repository\\bounded.png", new opencv_core.IplImage(bounded));

        ArrayList<Crop> cropElements = cropElements(imagemCinza, imageCopy);

        for (Crop crop : cropElements) {
            BufferedImage image = IplImageToBufferedImage(crop.getImage());
            byte[] data = bufferedImageToByteArray(image);
            String base64Image = Base64.getEncoder().encodeToString(data);

            Figura fig = new Figura(crop.x, crop.y, crop.h, crop.w, data, base64Image, "indeterminada");
            fig.fileName = crop.fileName;
            elementos.add(fig);
        }

        return elementos;
    }

    public static byte[] bufferedImageToByteArray(BufferedImage image) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Figura classificar(Figura segmentado, int k) throws Exception {
        String encoded = Base64.getEncoder().encodeToString(segmentado.getImage());
        HashMap<String, Object> params = new HashMap<>();
        params.put("imageEncoded", encoded);
        params.put("K", k);
        String retorno = Server.callServerPython("classificar", params);
        segmentado.tipo = retorno;
        return segmentado;
    }

    public static String treinarKnnPadrao(String tipo, String nome, String caminho, String dataSource, String resetar, String ieDump) throws Exception {
        HashMap<String, Object> params = new HashMap<>();
        params.put("tipo", tipo);
        params.put("nome", nome);
        params.put("caminho", caminho);
        params.put("data_source", dataSource);
        params.put("ie_dump", ieDump);
        return Server.callServerPython("treinarKnnPadrao", params);
    }

    public static ArrayList<String> classificarDebug(int k) throws Exception {
        ArrayList<String> retorno = new ArrayList<>();
        HashMap<String, Object> params = new HashMap<>();
        params.put("K", k);
        String json = Server.callServerPython("classificarDebug", params);
        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(json).getAsJsonArray();
        for (int i = 0; i < array.size(); i++) {
            JsonPrimitive get = (JsonPrimitive) array.get(i);
            retorno.add(get.getAsString());
        }
        return retorno;
    }

    public static String treinarKnnCustomizado(String tipo, String nome, String caminho, String dataSource, String resetar, HashMap parametros, String ieDump) throws Exception {
        HashMap<String, Object> params = new HashMap<>();
        params.put("tipo", tipo);
        params.put("nome", nome);
        params.put("caminho", caminho);
        params.put("data_source", dataSource);

        params.put("QT_SEMIBREVE", parametros.get("QT_SEMIBREVE"));
        params.put("QT_MINIMA", parametros.get("QT_MINIMA"));
        params.put("QT_SEMINIMA", parametros.get("QT_SEMINIMA"));
        params.put("QT_COLCHEIA", parametros.get("QT_COLCHEIA"));
        params.put("QT_SEMICOLCHEIA", parametros.get("QT_SEMICOLCHEIA"));
        params.put("QT_FUSA", parametros.get("QT_FUSA"));
        params.put("QT_SEMIFUSA", parametros.get("QT_SEMIFUSA"));
        params.put("QT_CLAVESOL", parametros.get("QT_CLAVESOL"));
        params.put("QT_CLAVEFA", parametros.get("QT_CLAVEFA"));
        params.put("QT_CLAVEDO", parametros.get("QT_CLAVEDO"));
        params.put("QT_FERMATA", parametros.get("QT_FERMATA"));
        params.put("QT_LIGADURA", parametros.get("QT_LIGADURA"));
        params.put("PAUSA_SEMIBREVE", parametros.get("PAUSA_SEMIBREVE"));
        params.put("PAUSA_MINIMA", parametros.get("PAUSA_MINIMA"));
        params.put("PAUSA_SEMINIMA", parametros.get("PAUSA_SEMINIMA"));
        params.put("PAUSA_COLCHEIA", parametros.get("PAUSA_COLCHEIA"));
        params.put("PAUSA_SEMICOLCHEIA", parametros.get("PAUSA_SEMICOLCHEIA"));
        params.put("PAUSA_FUSA", parametros.get("PAUSA_FUSA"));
        params.put("PAUSA_SEMIFUSA", parametros.get("PAUSA_SEMIFUSA"));
        params.put("QT_BARRAS_COMPASSO", parametros.get("QT_BARRAS_COMPASSO"));
        params.put("IE_DUMP", ieDump);
        return Server.callServerPython("treinarCustomizado", params);
    }

    public static ArrayList<Nota> detectarAlturaNotas(opencv_core.IplImage imagemCinza) throws Exception {
        byte[] image = bufferedImageToByteArray(IplImageToBufferedImage(imagemCinza));
        String encoded = Base64.getEncoder().encodeToString(image);
        HashMap<String, Object> params = new HashMap<>();
        params.put("imageEncoded", encoded);
        String json = Server.callServerPython("detectarAlturaNotas", params);
        ArrayList<Nota> pontos = new ArrayList<Nota>();
        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(json).getAsJsonArray();

        for (int i = 0; i < array.size(); i++) {
            JsonObject rootObj = (JsonObject) array.get(i);
            JsonObject ponto = rootObj.getAsJsonObject("ponto");

            double x = ponto.get("x").getAsDouble();
            double y = ponto.get("y").getAsDouble();
            double raio = ponto.get("raio").getAsDouble();
            int index = ponto.get("index").getAsInt();
            pontos.add(new Nota(index, x, y, raio));
        }

        return pontos;

    }

    public static ArrayList<Clave> obterInformacoesPautas(byte[] image) throws Exception {
        String encoded = Base64.getEncoder().encodeToString(image);
        HashMap<String, Object> params = new HashMap<>();
        params.put("imageEncoded", encoded);
        String json = Server.callServerPython("obterInformacoesPautas", params);
        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(json).getAsJsonArray();

        ArrayList<Clave> pautas = new ArrayList<Clave>();
        for (int i = 0; i < array.size(); i++) {
            JsonObject rootObj = (JsonObject) array.get(i);
            JsonObject pautaJson = rootObj.getAsJsonObject("pauta");

            int indexPauta = pautaJson.get("index").getAsInt();
            JsonArray linhasJson = pautaJson.get("linhas").getAsJsonArray();

            Clave pauta = new ClaveSol(indexPauta);
            for (int j = 0; j < linhasJson.size(); j++) {
                JsonObject o = (JsonObject) linhasJson.get(j);
                JsonObject linhaJson = (JsonObject) o.getAsJsonObject("linha");
                int indexLinha = linhaJson.get("index").getAsInt();
                double y = linhaJson.get("y").getAsDouble();
                Linha linhaPauta = new Linha(indexLinha, y);
                pauta.addLinha(indexLinha, linhaPauta);
            }
            pautas.add(pauta);
        }

        return pautas;

    }

    public static boolean isNegativo(int diff) {
        return diff < 0;
    }

    public static boolean isPositivo(int diff) {
        return diff > 0;
    }

    public void detectarPentagrama() {

    }

    public static ImageIcon redimencionarImagem(ImageIcon imageIcon, int maxHeight, int maxWidth) {
        int newHeight = 0, newWidth = 0;        // Variables for the new height and width
        int priorHeight = 0, priorWidth = 0;

        BufferedImage image = new BufferedImage(
                imageIcon.getIconWidth(),
                imageIcon.getIconHeight(),
                BufferedImage.TYPE_INT_RGB);

        Graphics g = image.createGraphics();
        imageIcon.paintIcon(null, g, 0, 0);
        g.dispose();

        if (imageIcon != null) {
            priorHeight = imageIcon.getIconHeight();
            priorWidth = imageIcon.getIconWidth();
        }

        // Calculate the correct new height and width
        if ((float) priorHeight / (float) priorWidth > (float) maxHeight / (float) maxWidth) {
            newHeight = maxHeight;
            newWidth = (int) (((float) priorWidth / (float) priorHeight) * (float) newHeight);
        } else {
            newWidth = maxWidth;
            newHeight = (int) (((float) priorHeight / (float) priorWidth) * (float) newWidth);
        }

        // Resize the image
        // 1. Create a new Buffered Image and Graphic2D object
        BufferedImage resizedImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = resizedImg.createGraphics();

        // 2. Use the Graphic object to draw a new image to the image in the buffer
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(image, 0, 0, newWidth, newHeight, null);
        g2.dispose();

        // 3. Convert the buffered image into an ImageIcon for return
        return (new ImageIcon(resizedImg));
    }
}
