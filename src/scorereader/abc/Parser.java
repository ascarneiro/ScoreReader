/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scorereader.abc;

import java.util.ArrayList;
import scorereader.structure.Elemento;

/**
 *
 * @author ascarneiro
 */
public class Parser {

    private StringBuilder score = new StringBuilder();

    private String build(StringBuilder score) {
        return "";
    }

    public String compile(ArrayList<Elemento> elementos) {
        StringBuilder score = make(elementos);
        String dirScore = build(score);
        return dirScore;
    }

    private StringBuilder make(ArrayList<Elemento> elementos) {
        score = new StringBuilder();
        score.append(Tipo.TITULO).append(" ").append(getTitulo(elementos));
        score.append(Tipo.COMPOSITOR).append(" ").append(getCompositor(elementos));
        score.append(Tipo.TONALIDADE).append(" ").append(getTonalidade(elementos));
        score.append(Tipo.MEDIDA).append(" ").append(getMedida(elementos));
        score.append(Tipo.MEDIDA).append(" ").append(getMusica(elementos));
        return score;
    }

    private String getTitulo(ArrayList<Elemento> elementos) {
        return "";
    }

    private String getCompositor(ArrayList<Elemento> elementos) {
        return "";
    }

    private String getMedida(ArrayList<Elemento> elementos) {
        return "";
    }

    private String getTonalidade(ArrayList<Elemento> elementos) {
        return "";
    }

    private String getMusica(ArrayList<Elemento> elementos) {
        return "";
    }
}
