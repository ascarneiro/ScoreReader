/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scorereader;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javax.imageio.ImageIO;
import org.bytedeco.javacpp.Loader;
import static org.bytedeco.javacpp.helper.opencv_core.CV_RGB;
import static org.bytedeco.javacpp.helper.opencv_imgcodecs.cvLoadImage;
import static org.bytedeco.javacpp.helper.opencv_imgcodecs.cvSaveImage;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Point;
import org.bytedeco.javacpp.opencv_imgproc;
import static org.bytedeco.javacpp.opencv_imgproc.CV_ADAPTIVE_THRESH_MEAN_C;
import static org.bytedeco.javacpp.opencv_imgproc.CV_CHAIN_APPROX_NONE;
import static org.bytedeco.javacpp.opencv_imgproc.CV_RETR_CCOMP;
import static org.bytedeco.javacpp.opencv_imgproc.CV_THRESH_BINARY;
import static org.bytedeco.javacpp.opencv_imgproc.CV_THRESH_BINARY_INV;
import static org.bytedeco.javacpp.opencv_imgproc.cvAdaptiveThreshold;
import static org.bytedeco.javacpp.opencv_imgproc.cvBoundingRect;
import static org.bytedeco.javacpp.opencv_imgproc.cvFindContours;
import static org.bytedeco.javacpp.opencv_imgproc.cvRectangleR;
import static org.bytedeco.javacpp.opencv_imgproc.cvThreshold;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.opencv.core.Core;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import static scorereader.image.Prototipo.CV_LOAD_IMAGE_ANYCOLOR;
import scorereader.server.Server;
import scorereader.structure.Crop;
import scorereader.structure.Figura;
import scorereader.structure.Linha;
import scorereader.structure.Pauta;
import scorereader.structure.Nota;

/**
 *
 * @author ascarneiro
 */
public class Utilities {

    public static boolean DEBUG = false;
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

        for (; contours != null; contours = contours.h_next()) {
            opencv_core.CvRect r = cvBoundingRect(contours, 0);
            if ((r.height() * r.width()) > 10) { //se a area do contorno for maior que a area minima
                opencv_core.CvRect bb = cvBoundingRect(contours, 0);
                bb.x(bb.x() - 5);
                bb.y(bb.y() - 5);
                bb.width(bb.width() + 10);
                bb.height(bb.height() + 10);
                cvRectangleR(imagemBB, bb, CV_RGB(0, 255, 0), 1, 8, 0);
                // Adding Text

                opencv_core.CvScalar c = CV_RGB(0, 0, 0);
                opencv_imgproc.cvPutText(
                        imagemBB, // Matrix obj of the image
                        "(" + bb.x() + " , " + bb.y() + ")", // Text to be added
                        new opencv_core.CvPoint(bb.x(), bb.y() - 25), // point
                        opencv_imgproc.cvFont(1), // front face
                        c // Scalar object for color
                );

            }
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
                bb.x(bb.x() - 5);
                bb.y(bb.y() - 5);
                bb.width(bb.width() + 10);
                bb.height(bb.height() + 10);
                cvRectangleR(imagemBB, bb, CV_RGB(0, 255, 0), 1, 8, 0);
                opencv_core.Rect rectCrop = new opencv_core.Rect(bb.x(), bb.y(), bb.width(), bb.height());
                opencv_core.Mat croppedImage = new opencv_core.Mat(new opencv_core.Mat(imagemBB), rectCrop);
                opencv_core.IplImage crop = new opencv_core.IplImage(croppedImage);
                elements.add(new Crop(bb.x(), bb.y(), bb.width(), bb.height(), crop));

                if (DEBUG) {
                    cvSaveImage(DIR_DEBUG + "crop" + System.currentTimeMillis() + ".png", crop);
                }
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

    public static byte[] removerLinhasDaPauta(byte[] imageData) {

        String encoded = Base64.getEncoder().encodeToString(imageData);
        HashMap<String, Object> params = new HashMap<>();
        params.put("imageEncoded", encoded);
        String retorno = Server.callServerPython("removerLinhasDaPauta", params);
        return Base64.getDecoder().decode(retorno);

    }

    public static opencv_core.IplImage bufferedImageToIplImage(byte[] imageData) throws Exception {
        //ByteArrayInputStream bis = new ByteArrayInputStream(imageData);
        //BufferedImage bfImage = ImageIO.read(bis);

        String path = "C:\\Users\\ascarneiro\\Desktop\\TCC\\ScoreReader\\repository\\staffless.png";
        //ImageIO.write(bfImage, "PNG", new File(path));

        return cvLoadImage(path, CV_LOAD_IMAGE_ANYCOLOR);
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

        if (DEBUG) {
            opencv_core.IplImage bounded = drawBoundBoxes(imagemCinza, imageCopy);
            cvSaveImage(DIR_DEBUG + "bounded.png", new opencv_core.IplImage(bounded));
        }

        if (DEBUG) {
            ArrayList<Crop> cropElements = cropElements(imagemCinza, imageCopy);

            for (Crop crop : cropElements) {
                BufferedImage image = IplImageToBufferedImage(crop.getImage());
                byte[] data = bufferedImageToByteArray(image);
                String base64Image = Base64.getEncoder().encodeToString(data);

                elementos.add(new Figura(crop.x, crop.y, crop.h, crop.w, data, base64Image, "undefined-yet"));
            }
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

    public static Figura classificar(Figura segmentado) {
        String encoded = Base64.getEncoder().encodeToString(segmentado.getImage());
        HashMap<String, Object> params = new HashMap<>();
        params.put("imageEncoded", encoded);
        String retorno = Server.callServerPython("classificar", params);
        segmentado.tipo = retorno;
        return segmentado;
    }

    public static ArrayList<Nota> detectarAlturaNotas(opencv_core.IplImage imagemCinza) {
        byte[] image = bufferedImageToByteArray(IplImageToBufferedImage(imagemCinza));
        String encoded = Base64.getEncoder().encodeToString(image);
        HashMap<String, Object> params = new HashMap<>();
        params.put("imageEncoded", encoded);
        String json = Server.callServerPython("detectarAlturaNotas", params);
        ArrayList<Nota> pontos = new ArrayList<Nota>();
        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(json).getAsJsonArray();

        for (int i = 0; i < array.size(); i++) {
            JsonObject rootObj = (JsonObject) array.get(0);
            JsonObject ponto = rootObj.getAsJsonObject("ponto");

            double x = ponto.get("x").getAsDouble();
            double y = ponto.get("y").getAsDouble();
            double raio = ponto.get("raio").getAsDouble();
            int index = ponto.get("index").getAsInt();
            pontos.add(new Nota(index, x, y, raio));
        }

        return pontos;

    }

    public static ArrayList<Pauta> obterInformacoesPautas(byte[] image) {
        String encoded = Base64.getEncoder().encodeToString(image);
        HashMap<String, Object> params = new HashMap<>();
        params.put("imageEncoded", encoded);
        String json = Server.callServerPython("obterInformacoesPautas", params);
        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(json).getAsJsonArray();

        ArrayList<Pauta> pautas = new ArrayList<Pauta>();
        for (int i = 0; i < array.size(); i++) {
            JsonObject rootObj = (JsonObject) array.get(i);
            JsonObject pautaJson = rootObj.getAsJsonObject("pauta");

            int indexPauta = pautaJson.get("index").getAsInt();
            JsonArray linhasJson = pautaJson.get("linhas").getAsJsonArray();

            Pauta pauta = new Pauta(indexPauta);
            for (int j = 0; j < linhasJson.size(); j++) {
                JsonObject o = (JsonObject) linhasJson.get(j);
                JsonObject linhaJson = (JsonObject) o.getAsJsonObject("linha");
                int indexLinha = linhaJson.get("index").getAsInt();
                double y = linhaJson.get("y").getAsDouble();
                Linha linhaPauta = new Linha(indexLinha, y);
                pauta.addLinha(String.valueOf(indexLinha), linhaPauta);
            }
            pautas.add(pauta);
        }

        return pautas;

    }

    public void detectarPentagrama() {

    }
}
