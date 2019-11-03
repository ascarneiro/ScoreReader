package scorereader.components.list;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;
import scorereader.Utilities;
import scorereader.structure.Figura;

public class FiguraRenderer extends JPanel implements ListCellRenderer<Figura> {

    private JLabel lbIcon = new JLabel();
    private JPanel panelIcon;

    public FiguraRenderer() {
        setLayout(new BorderLayout(5, 5));

        panelIcon = new JPanel();
        panelIcon.setBorder(new EmptyBorder(5, 5, 5, 5));
        panelIcon.add(lbIcon);

        add(panelIcon, BorderLayout.WEST);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Figura> list,
            Figura figura, int index, boolean isSelected, boolean cellHasFocus) {
        lbIcon.setIcon(Utilities.redimencionarImagem(new ImageIcon(figura.getImage()), 40, 40));
        lbIcon.setOpaque(true);
        if (isSelected) {
            lbIcon.setBackground(list.getSelectionBackground());
            setBackground(list.getSelectionBackground());
            panelIcon.setBackground(list.getSelectionBackground());
        } else { // when don't select
            lbIcon.setBackground(list.getBackground());
            setBackground(list.getBackground());
            panelIcon.setBackground(list.getBackground());
        }
        return this;
    }

}
