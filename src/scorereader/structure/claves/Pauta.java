/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scorereader.structure.claves;

import java.util.ArrayList;
import java.util.Map;
import scorereader.Utilities;
import scorereader.structure.Figura;
import scorereader.structure.Linha;
import scorereader.structure.Nota;

/**
 *
 * @author ascarneiro
 */
public abstract class Pauta {

    public int index = 0;

    private Pauta() {
    }

    private ArrayList<Figura> figurasPauta = new ArrayList<>();
    private ArrayList<Nota> notasPauta = new ArrayList<>();
    protected double minimo = Integer.MAX_VALUE;
    protected double maximo = Integer.MIN_VALUE;
    private int fator = 4;

    public Pauta(int index) {
        this.index = index;

    }

    public int getOrdem() {
        return index;
    }

    public void calcularEspacos() {
        for (int i = 1; i <= getLinhas().size(); i++) {

            Linha l = getLinhas().get(String.valueOf(i));
            Linha lb = getLinhas().get(String.valueOf(i + 1));
            Linha la = getLinhas().get(String.valueOf(i - 1));

            if (lb != null) {
                double diff = Math.abs((l.y - lb.y) / 2);
                l.yEspacoAbaixo = l.y + diff;
            }
            if (la != null) {
                double diff = Math.abs((l.y - la.y) / 2);
                l.yEspacoAcima = l.y - diff;
            }
            getLinhas().put(String.valueOf(i), l);
        }
    }

    public int getIndex() {
        return index;
    }

    /**
     *
     * Obrigatorios implementar
     */
    public abstract Linha getLinha(String numero);

    public abstract Map<String, Linha> getLinhas();

    public abstract String getNomeNota(int index);

    public abstract String getNomeNotaAcima(int index);

    public abstract String getNomeNotaAbaixo(int index);

    public abstract void addLinha(int numero, Linha linha);

    public void determinarNotasPauta() {
        for (Figura figura : figurasPauta) {
            for (Nota nota : notasPauta) {

                //Determina a distancia entre os pontos
                int diff = Math.abs((int) (nota.x - figura.x));
                //Caso a tolerancia seja
                if (diff < 6) {
                    figura.nota = nota;
                    break;
                }
            }
        }
    }

    public void determinarAlturaNotasPauta() {
        for (Figura figura : figurasPauta) {
            if (figura.isNotaMusical()) {
                for (int i = 1; i <= getLinhas().size(); i++) {

                    Linha linha = getLinha(String.valueOf(i));
                    int diff = ((int) (figura.nota.y - linha.y));
                    int variacao = Math.abs(diff);
                    if (variacao < fator) {//Diferenca for de menos de 2 pixels
                        figura.nota.nome = getNomeNota(linha.index);

                    } else {
                        diff = ((int) (figura.nota.y - linha.yEspacoAbaixo));
                        variacao = Math.abs(diff);
                        if (variacao < fator) {
                            //Pegar nota acima da pauta
                            figura.nota.nome = getNomeNotaAbaixo(linha.index);

                        } else {
                            diff = ((int) (figura.nota.y - linha.yEspacoAcima));
                            variacao = Math.abs(diff);
                            //Pegar nota acima da pauta
                            if (variacao < fator) {
                                figura.nota.nome = getNomeNotaAcima(linha.index);

                            }
                        }
                    }

                }
            }
        }

    }

    //Determina se a nota esta dentro da pauta de acordo com o maximo e minimo Y
    public boolean notaDentroDaPauta(Nota nota) {
        return nota.y >= (minimo - 30)
                && nota.y <= (maximo + 30);
    }

     //Determina se a Figura esta dentro da pauta de acordo com o maximo e minimo Y
    public boolean figuraDentroDaPauta(Figura figura) {
        return figura.y >= (minimo - 30)
                && figura.y <= (maximo + 30);
    }

    //Adiciona na pauta nota que [e responsabilidade da pauta
    public void addNotaPauta(Nota nota) {
        notasPauta.add(nota);
    }

    public void classificarFiguras(String kvizinhos) throws Exception {
        for (Figura figura : figurasPauta) {
            Figura classificada = Utilities.classificar(figura, Integer.parseInt(kvizinhos));
            figura.setTipo(classificada.getTipo());
        }

    }

    public void addFiguraPauta(Figura figura) {
        figurasPauta.add(figura);
    }

    public ArrayList<Figura> getFigurasPauta() {
        return figurasPauta;
    }

}
