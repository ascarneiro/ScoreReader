/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scorereader.structure;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ascarneiro
 */
public class Compasso {

    private List<Figura> figuras = new ArrayList<>();
    private boolean retornar = false;

    public void addCompasso(Figura figura) {
        figuras.add(figura);
    }

    public Figura getFigura(int nrFigura) {
        if (!figuras.isEmpty()) {
            return figuras.get(nrFigura);
        }
        return null;
    }

    public boolean isRetornar() {
        return retornar;
    }

    public void setRetornar(boolean retornar) {
        this.retornar = retornar;
    }

}
