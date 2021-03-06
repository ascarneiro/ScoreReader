/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scorereader.abc;

/**
 *
 * @author ascarneiro
 */
public class ABC_NOTATION {

    public static final String[] TONALIDADE_SUSTENIDO = new String[]{"C", "G", "D", "A", "E", "B", "F#", "C#"};
    public static final String[] TONALIDADE_BEMOL = new String[]{"C", "F", "Bb", "Eb", "Ab", "Db", "Gb", "Cb"};
    
    public static final String CLAVE_SOL = "treble";
    public static final String CLAVE_FA = "bass";
    public static final String CLAVE_DO = "alto";
    
    //SUPLEMENTARES INFERIORES
    public static final String __LA = "A,";
    public static final String __SI = "B,";
    public static final String __DO = "C,";
    public static final String __RE = "D,";
    public static final String __MI = "E,";
    public static final String __FA = "F,";
    public static final String __SOL = "G,";

    //OITAVA INFERIOR
    public static final String _LA = "A";
    public static final String _SI = "B";
    public static final String _DO = "C";
    public static final String _RE = "D";
    public static final String _MI = "E";
    public static final String _FA = "F";
    public static final String _SOL = "G";

    //OITAVA SUPERIOR
    public static final String LA_ = "a";
    public static final String SI_ = "b";
    public static final String DO_ = "c";
    public static final String RE_ = "c";
    public static final String MI_ = "e";
    public static final String FA_ = "f";
    public static final String SOL_ = "g";

    //SUPLEMENTARES SUPERIORES
    public static final String LA__ = "a'";
    public static final String SI__ = "b'";
    public static final String DO__ = "c'";
    public static final String RE__ = "c'";
    public static final String MI__ = "e'";
    public static final String FA__ = "f'";
    public static final String SOL__ = "g'";

    public static final String TITULO = "T:";
    public static final String TONALIDADE_E_CLAVE = "K:";
    public static final String MEDIDA = "M:";
    public static final String COMPOSITOR = "C:";

    public static final String SEMIBREVE = "1";
    public static final String SEMINIMA = "2";
    public static final String MINIMA = "4";
    public static final String COLCHEIA = "8";
    public static final String SEMICOLCHEIA = "16";

    public static final String PAUSA_SEMIBREVE = "z4";
    public static final String PAUSA_SEMINIMA = " z2";
    public static final String PAUSA_MINIMA = " z4";
    public static final String PAUSA_COLCHEIA = "z";
    public static final String PAUSA_SEMICOLCHEIA = "z/";


    public static final String BEMOL = "_";
    public static final String SUSTENIDO = "";// ^ TODO rever isso
    public static final String BEQUADRO = "=";
    
    public static final String BARRA_COMPASSO = "|";
    public static final String INICIO = "[";
    public static final String FINAL = "]";
    public static final String BARRA_INICIO_RETONELLO = "|:";
    public static final String BARRA_FIM_RETONELO = ":|";

}
