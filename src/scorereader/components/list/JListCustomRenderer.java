package scorereader.components.list;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import scorereader.structure.Figura;

public class JListCustomRenderer extends JFrame {

    public static int width = 350;
    public static int height = 200;
    private JList<Figura> listaFiguras;

    public JListCustomRenderer(ArrayList<Figura> figuras) {
        // add main panel
        add(createMainPanel(figuras));

    }

    private JPanel createMainPanel(ArrayList<Figura> figuras) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        // create list book and set to scrollpane and add to panel
        panel.add(new JScrollPane(listaFiguras = createListFiguras(figuras)),
                BorderLayout.CENTER);
        return panel;
    }

    private JList<Figura> createListFiguras(ArrayList<Figura> figuras) {
        // create List model
        DefaultListModel<Figura> model = new DefaultListModel<>();

        for (Figura figura : figuras) {
            model.addElement(figura);
        }

        // create JList with model
        JList<Figura> list = new JList<Figura>(model);
        list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        // set cell renderer
        list.setCellRenderer(new FiguraRenderer());
        return list;
    }

}
