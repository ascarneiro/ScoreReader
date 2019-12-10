package scorereader.components.images;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import scorereader.Utilities;

public class ScrollDemo extends JPanel implements ItemListener {

    private Rule colunas;
    private Rule linhas;
    private JToggleButton isMetric;
    private ScrollablePicture painelImagem;
    private JPanel jpanel = new JPanel();
    private ImageIcon imagem;
    private ImageIcon imagemOriginal;
    JScrollPane jscroolPane = new JScrollPane();
    private int ZOOM = 1100;

    public ScrollDemo() {

        add(jscroolPane);
        setLayout(new BorderLayout());

        jpanel.setLayout(new BoxLayout(jpanel, BoxLayout.LINE_AXIS));

    }

    public void limpar() {
        jpanel.removeAll();
    }

    public void addImagem(String caminhoImagem, boolean limpar) {
        ImageIcon imagemLocal = criarImageIcon(caminhoImagem);
        imagemOriginal = imagemLocal;
        ImageIcon imagem = imagemLocal;
        if (imagemLocal != null
                && limpar) {
            imagem = Utilities.redimencionarImagem(imagemLocal, 1100, 1100);
            imagemLocal.getImage().flush();

        }
        addImagem(imagem, limpar);

    }

    public void atualizarImagem(ImageIcon image, boolean limpar) {
        painelImagem.setIcon(image);
        painelImagem.repaint();
    }

    public void addImagem(ImageIcon image, boolean limpar) {
        if (limpar) {
            jpanel.removeAll();
        }

        colunas = new Rule(Rule.HORIZONTAL, true);
        linhas = new Rule(Rule.VERTICAL, true);

        if (image != null) {
            colunas.setPreferredWidth(image.getIconWidth());
            linhas.setPreferredHeight(image.getIconHeight());
            if (this.imagem != null
                    && this.imagem != null) {
                this.imagem.getImage().flush();
                image.getImage().flush();
            }
            this.imagem = image;
        } else {
            colunas.setPreferredWidth(320);
            linhas.setPreferredHeight(480);
        }

        JPanel buttonCorner = new JPanel();
        isMetric = new JToggleButton("cm", true);
        isMetric.setFont(new Font("SansSerif", Font.PLAIN, 11));
        isMetric.setMargin(new Insets(2, 2, 2, 2));
        isMetric.addItemListener(this);
        buttonCorner.add(isMetric);

        painelImagem = new ScrollablePicture(image, colunas.getIncrement());

        JScrollPane pictureScrollPane = new JScrollPane(painelImagem);

        pictureScrollPane.setPreferredSize(new Dimension(300, 250));
        pictureScrollPane.setViewportBorder(
                BorderFactory.createLineBorder(new Color(210, 239, 239)));

        pictureScrollPane.setColumnHeaderView(colunas);
        pictureScrollPane.setRowHeaderView(linhas);

        pictureScrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER,
                buttonCorner);
        pictureScrollPane.setCorner(JScrollPane.LOWER_LEFT_CORNER,
                new Corner());
        pictureScrollPane.setCorner(JScrollPane.UPPER_RIGHT_CORNER,
                new Corner());

        pictureScrollPane.setBorder(null);
        jpanel.add(pictureScrollPane);

        jscroolPane.setViewportView(jpanel);
        add(jscroolPane);
        updateUI();

        painelImagem.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    ZOOM += 100;
                } else {
                    ZOOM -= 100;

                }

                if (imagemOriginal != null) {
                    ImageIcon imageIcon = Utilities.redimencionarImagem(imagemOriginal, ZOOM, ZOOM);
                    imageIcon.getImage().flush();

                    addImagem(imageIcon, true);
                }

            }

        });
    }

    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            linhas.setIsMetric(true);
            colunas.setIsMetric(true);
        } else {
            linhas.setIsMetric(false);
            colunas.setIsMetric(false);
        }
        painelImagem.setMaxUnitIncrement(linhas.getIncrement());

    }

    protected static ImageIcon criarImageIcon(String path) {
        if (path != null) {
            ImageIcon im = new ImageIcon(path);
            return im;
        } else {
            System.err.println("Arquivo nao encontrado: " + path);
            return null;
        }
    }

}
