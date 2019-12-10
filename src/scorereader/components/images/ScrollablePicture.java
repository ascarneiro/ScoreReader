package scorereader.components.images;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ScrollablePicture extends JLabel implements Scrollable, MouseMotionListener {

    private int maxIncremento = 1;
    private boolean imagemAusente = false;

    public void init() {

    }

    public ScrollablePicture(ImageIcon i, int m) {
        super(i);
        if (i == null) {
            imagemAusente = true;
            setText("Sem imagem");
            setHorizontalAlignment(CENTER);
            setOpaque(true);
            setFont(new Font("Tahoma", 0, 24));
            setBackground(Color.white);

        }
        maxIncremento = m;

        setAutoscrolls(true);
        addMouseMotionListener(this);
        init();
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        Rectangle r = new Rectangle(e.getX(), e.getY(), 1, 1);
        scrollRectToVisible(r);
    }

    public Dimension getPreferredSize() {
        if (imagemAusente) {
            return new Dimension(320, 480);
        } else {
            return super.getPreferredSize();
        }
    }

    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    public int getScrollableUnitIncrement(Rectangle visibleRect,
            int orientation,
            int direcao) {
        int currentPosition = 0;
        if (orientation == SwingConstants.HORIZONTAL) {
            currentPosition = visibleRect.x;
        } else {
            currentPosition = visibleRect.y;
        }

        if (direcao < 0) {
            int newPosition = currentPosition
                    - (currentPosition / maxIncremento)
                    * maxIncremento;
            return (newPosition == 0) ? maxIncremento : newPosition;
        } else {
            return ((currentPosition / maxIncremento) + 1)
                    * maxIncremento
                    - currentPosition;
        }
    }

    public int getScrollableBlockIncrement(Rectangle rect,
            int orientacao,
            int direcao) {
        if (orientacao == SwingConstants.HORIZONTAL) {
            return rect.width - maxIncremento;
        } else {
            return rect.height - maxIncremento;
        }
    }

    public boolean getScrollableTracksViewportWidth() {
        return false;
    }

    public boolean getScrollableTracksViewportHeight() {
        return false;
    }

    public void setMaxUnitIncrement(int qtPixels) {
        maxIncremento = qtPixels;
    }
}
