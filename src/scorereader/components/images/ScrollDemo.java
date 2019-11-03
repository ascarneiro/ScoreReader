/*
 * Copyright (c) 1995, 2011, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package scorereader.components.images;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;

/* 
 * ScrollDemo.java requires these files:
 *   Rule.java
 *   Corner.java
 *   ScrollablePicture.java
 *   images/flyingBee.jpg
 */
public class ScrollDemo extends JPanel
        implements ItemListener {
    
    private Rule columnView;
    private Rule rowView;
    private JToggleButton isMetric;
    private ScrollablePicture picture;
    private JPanel j = new JPanel();
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
        ImageIcon image = createImageIcon(caminhoImagem);
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

        //Set the corners.
        //In theory, to support internationalization you would change
        //UPPER_LEFT_CORNER to UPPER_LEADING_CORNER,
        //LOWER_LEFT_CORNER to LOWER_LEADING_CORNER, and
        //UPPER_RIGHT_CORNER to UPPER_TRAILING_CORNER.  In practice,
        //bug #4467063 makes that impossible (in 1.4, at least).
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
    
}
