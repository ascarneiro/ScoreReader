package scorereader.components.images;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import scorereader.Utilities;

/* 
 * ScrollDemo.java requires these files:
 *   Rule.java
 *   Corner.java
 *   ScrollablePicture.java
 *   images/flyingBee.jpg
 */
public class ScrollDemo extends JPanel
        implements ItemListener {

    private double zoom = 100;
    private Rule columnView;
    private Rule rowView;
    private JToggleButton isMetric;
    private ScrollablePicture picture;
    private JPanel j = new JPanel();
    private ImageIcon image;
    JScrollPane j2 = new JScrollPane();

    public ScrollDemo() {

        add(j2);
        setLayout(new BorderLayout());

        j.setLayout(new BoxLayout(j, BoxLayout.LINE_AXIS));

    }

    public void limpar() {
        j.removeAll();
    }

    public void addImagem(String caminhoImagem, boolean limpar) {
        //Get the image to use.
        ImageIcon im = createImageIcon(caminhoImagem);

        ImageIcon image = im;
        if (im != null
                && limpar) {
            image = Utilities.redimencionarImagem(im, 970, 970);

        }
        addImagem(image, limpar);

    }

    public void atualizarImagem(ImageIcon image, boolean limpar) {
        picture.setIcon(image);
        picture.repaint();
    }

    public void addImagem(ImageIcon image, boolean limpar) {
        if (limpar) {
            j.removeAll();
        }

        //Create the row and column headers.
        columnView = new Rule(Rule.HORIZONTAL, true);
        rowView = new Rule(Rule.VERTICAL, true);

        if (image != null) {
            columnView.setPreferredWidth(image.getIconWidth());
            rowView.setPreferredHeight(image.getIconHeight());
            this.image = image;
        } else {
            columnView.setPreferredWidth(320);
            rowView.setPreferredHeight(480);
        }

        //Create the corners.
        JPanel buttonCorner = new JPanel(); //use FlowLayout
        isMetric = new JToggleButton("cm", true);
        isMetric.setFont(new Font("SansSerif", Font.PLAIN, 11));
        isMetric.setMargin(new Insets(2, 2, 2, 2));
        isMetric.addItemListener(this);
        buttonCorner.add(isMetric);

        //Set up the scroll pane.
        picture = new ScrollablePicture(image, columnView.getIncrement());

        JScrollPane pictureScrollPane = new JScrollPane(picture);

        pictureScrollPane.setPreferredSize(new Dimension(300, 250));
        pictureScrollPane.setViewportBorder(
                BorderFactory.createLineBorder(new Color(210, 239, 239)));

        pictureScrollPane.setColumnHeaderView(columnView);
        pictureScrollPane.setRowHeaderView(rowView);

        pictureScrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER,
                buttonCorner);
        pictureScrollPane.setCorner(JScrollPane.LOWER_LEFT_CORNER,
                new Corner());
        pictureScrollPane.setCorner(JScrollPane.UPPER_RIGHT_CORNER,
                new Corner());

        pictureScrollPane.setBorder(null);
        //Put it in this panel.
        j.add(pictureScrollPane);

//        j.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        j2.setViewportView(j);
        add(j2);
        updateUI();
    }

    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            //Turn it to metric.
            rowView.setIsMetric(true);
            columnView.setIsMetric(true);
        } else {
            //Turn it to inches.
            rowView.setIsMetric(false);
            columnView.setIsMetric(false);
        }
        picture.setMaxUnitIncrement(rowView.getIncrement());
    }

    /**
     * Returns an ImageIcon, or null if the path was invalid.
     */
    protected static ImageIcon createImageIcon(String path) {
        if (path != null) {
            return new ImageIcon(path);
        } else {
            System.err.println("Arquivo nao encontrado: " + path);
            return null;
        }
    }

    public double getZoom() {
        return zoom;
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
    }
}
