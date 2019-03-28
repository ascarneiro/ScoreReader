/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scorereader.structure;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ascarneiro
 */
public class Partitura {

    private Armadura armaduraClave;
    private List<Compasso> compassos = new ArrayList<>();
    private Image image;

    public Partitura(Armadura armaduraClave, Image image) {
        this.armaduraClave = armaduraClave;
        this.image = image;
    }

    public void addCompasso(Compasso compasso) {
        compassos.add(compasso);

    }

    public void setArmaduraClave(Armadura armaduraClave) {
        this.armaduraClave = armaduraClave;
    }

    public Armadura getArmaduraClave() {
        return armaduraClave;
    }

    public Compasso getCompassos(int nrCompoasso) {
        if (!compassos.isEmpty()) {
            return compassos.get(nrCompoasso);
        }
        return null;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Image getImage() {
        return image;
    }

}
