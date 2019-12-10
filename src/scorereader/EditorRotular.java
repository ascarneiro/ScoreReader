/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scorereader;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import scorereader.components.list.FiguraRenderer;
import scorereader.structure.Figura;

/**
 *
 * @author ascarneiro
 */
public class EditorRotular extends javax.swing.JFrame {

    public static int width = 350;
    public static int height = 200;

    private String selecionado = "Seminima";
    private ArrayList<Figura> rotular = new ArrayList<Figura>();
    private ArrayList<Figura> semibreve = new ArrayList<Figura>();
    private ArrayList<Figura> minimas = new ArrayList<Figura>();
    private ArrayList<Figura> seminimas = new ArrayList<Figura>();
    private ArrayList<Figura> colcheias = new ArrayList<Figura>();
    private ArrayList<Figura> semicolcheias = new ArrayList<Figura>();
    private ArrayList<Figura> barraCompasso = new ArrayList<Figura>();
    private ArrayList<Figura> pausaSemibreve = new ArrayList<Figura>();
    private ArrayList<Figura> pausaMinima = new ArrayList<Figura>();
    private ArrayList<Figura> pausaSeminima = new ArrayList<Figura>();
    private ArrayList<Figura> pausaColcheia = new ArrayList<Figura>();
    private ArrayList<Figura> pausaSemicolcheias = new ArrayList<Figura>();
    private ArrayList<Figura> claveSol = new ArrayList<Figura>();
    private ArrayList<Figura> claveFa = new ArrayList<Figura>();
    private ArrayList<Figura> claveDo = new ArrayList<Figura>();
    private ArrayList<Figura> sustenidos = new ArrayList<Figura>();
    private ArrayList<Figura> bemois = new ArrayList<Figura>();
    private ArrayList<Figura> marcacaoTempo = new ArrayList<Figura>();
    private ArrayList<Figura> outros_simbolos = new ArrayList<Figura>();

    private JList<Figura> rotularJL;
    private JList<Figura> semibreveJL;
    private JList<Figura> minimasJL;
    private JList<Figura> seminimasJL;
    private JList<Figura> colcheiasJL;
    private JList<Figura> semiColcheiasJL;
    private JList<Figura> barraCompassoJL;
    private JList<Figura> sustenidosJL;
    private JList<Figura> bemoisJL;
    private JList<Figura> selectedJList;

    private JList<Figura> pausaSemibreveJL;
    private JList<Figura> pausaMinimaJL;
    private JList<Figura> pausaSeminimaJL;
    private JList<Figura> pausaSemicolcheiaJL;
    private JList<Figura> pausaColcheiaJL;
    private JList<Figura> claveSolJL;
    private JList<Figura> claveFaJL;
    private JList<Figura> claveDoJL;
    private JList<Figura> marcacaoTempoJL;
    private JList<Figura> outrosSimbolosJL;

    /**
     * Creates new form Rotulador
     */
    public EditorRotular(ArrayList<Figura> rotular) {
        this.rotular = new ArrayList<Figura>(rotular);
        initComponents();
        rotularJL = getJlist(rotular);
        semibreveJL = getJlist(semibreve);
        minimasJL = getJlist(minimas);
        seminimasJL = getJlist(seminimas);
        colcheiasJL = getJlist(colcheias);
        barraCompassoJL = getJlist(barraCompasso);
        barraCompassoJL = getJlist(barraCompasso);
        sustenidosJL = getJlist(sustenidos);
        bemoisJL = getJlist(bemois);
        pausaSemibreveJL = getJlist(pausaSemibreve);
        pausaMinimaJL = getJlist(pausaMinima);
        pausaSeminimaJL = getJlist(pausaSeminima);
        pausaColcheiaJL = getJlist(pausaColcheia);
        claveSolJL = getJlist(claveSol);
        claveFaJL = getJlist(claveFa);
        claveDoJL = getJlist(claveDo);
        marcacaoTempoJL = getJlist(marcacaoTempo);
        outrosSimbolosJL = getJlist(outros_simbolos);
        semiColcheiasJL = getJlist(semicolcheias);
        pausaSemicolcheiaJL = getJlist(pausaSemicolcheias);

        ROTULAR_P.add(createMainPanel(rotularJL));
        SEMIBREVE_P.add(createMainPanel(semibreveJL));
        MINIMAS_P.add(createMainPanel(minimasJL));
        SEMINIMAS_P.add(createMainPanel(seminimasJL));
        COLCHEIAS_P.add(createMainPanel(colcheiasJL));
        BARRA_COMPASSO_P.add(createMainPanel(barraCompassoJL));
        PAUSA_SEMIBREVE_P.add(createMainPanel(pausaSemibreveJL));
        PAUSA_MINIMA_P.add(createMainPanel(pausaMinimaJL));
        PAUSA_SEMINIMA_P.add(createMainPanel(pausaSeminimaJL));
        PAUSA_COLCHEIA_P.add(createMainPanel(pausaColcheiaJL));
        CLAVE_SOL_P.add(createMainPanel(claveSolJL));
        CLAVE_FA_P.add(createMainPanel(claveFaJL));
        CLAVE_DO_P.add(createMainPanel(claveDoJL));
        SUSTENIDOS_P.add(createMainPanel(sustenidosJL));
        BEMOIS_P.add(createMainPanel(bemoisJL));
        MARCACAO_TEMPO_P.add(createMainPanel(marcacaoTempoJL));
        OUTROS_SIMBOLOS_P.add(createMainPanel(outrosSimbolosJL));
        SEMICOLCHEIAS_P.add(createMainPanel(semiColcheiasJL));
        PAUSA_SEMICOLCHEIA_P.add(createMainPanel(pausaSemicolcheiaJL));

        SEMINIMA.requestFocus();
        setSelecionado(SEMINIMA);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        painelBotoes = new javax.swing.JPanel();
        SEMIBREVE = new javax.swing.JButton();
        SEMINIMA = new javax.swing.JButton();
        CLAVE_SOL = new javax.swing.JButton();
        MINIMA = new javax.swing.JButton();
        COLCHEIA = new javax.swing.JButton();
        SEMICOLCHEIA = new javax.swing.JButton();
        BARRA_COMPASSO = new javax.swing.JButton();
        CLAVE_FA = new javax.swing.JButton();
        REMOVER = new javax.swing.JButton();
        IE_PAUSA = new javax.swing.JCheckBox();
        SELECIONANDO = new javax.swing.JLabel();
        CLAVE_DO = new javax.swing.JButton();
        ADICIONAR = new javax.swing.JButton();
        SUSTENIDOS = new javax.swing.JButton();
        BEMOIS = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        MARCACAO_TEMPO = new javax.swing.JButton();
        OUTROS_SIMBOLOS = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        Container = new javax.swing.JPanel();
        ROTULAR_P = new javax.swing.JPanel();
        SEMINIMAS_P = new javax.swing.JPanel();
        COLCHEIAS_P = new javax.swing.JPanel();
        BARRA_COMPASSO_P = new javax.swing.JPanel();
        MINIMAS_P = new javax.swing.JPanel();
        SUSTENIDOS_P = new javax.swing.JPanel();
        BEMOIS_P = new javax.swing.JPanel();
        CLAVE_SOL_P = new javax.swing.JPanel();
        CLAVE_DO_P = new javax.swing.JPanel();
        SEMIBREVE_P = new javax.swing.JPanel();
        PAUSA_SEMIBREVE_P = new javax.swing.JPanel();
        PAUSA_MINIMA_P = new javax.swing.JPanel();
        PAUSA_SEMINIMA_P = new javax.swing.JPanel();
        PAUSA_COLCHEIA_P = new javax.swing.JPanel();
        MARCACAO_TEMPO_P = new javax.swing.JPanel();
        OUTROS_SIMBOLOS_P = new javax.swing.JPanel();
        SEMICOLCHEIAS_P = new javax.swing.JPanel();
        PAUSA_SEMICOLCHEIA_P = new javax.swing.JPanel();
        CLAVE_FA_P = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        painelBotoes.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        SEMIBREVE.setText("Semibreve");
        SEMIBREVE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SEMIBREVEActionPerformed(evt);
            }
        });
        painelBotoes.add(SEMIBREVE, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, 100, 33));

        SEMINIMA.setText("Seminima");
        SEMINIMA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SEMINIMAActionPerformed(evt);
            }
        });
        painelBotoes.add(SEMINIMA, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 10, 110, 33));

        CLAVE_SOL.setText("Clave Sol");
        CLAVE_SOL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CLAVE_SOLActionPerformed(evt);
            }
        });
        painelBotoes.add(CLAVE_SOL, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 10, 120, 33));

        MINIMA.setText("Minima");
        MINIMA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MINIMAActionPerformed(evt);
            }
        });
        painelBotoes.add(MINIMA, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 10, 120, 33));

        COLCHEIA.setText("Colcheia");
        COLCHEIA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                COLCHEIAActionPerformed(evt);
            }
        });
        painelBotoes.add(COLCHEIA, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 50, 100, 33));

        SEMICOLCHEIA.setText("SemiColcheia");
        SEMICOLCHEIA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SEMICOLCHEIAActionPerformed(evt);
            }
        });
        painelBotoes.add(SEMICOLCHEIA, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 50, 120, 33));

        BARRA_COMPASSO.setText("Barra Compasso");
        BARRA_COMPASSO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BARRA_COMPASSOActionPerformed(evt);
            }
        });
        painelBotoes.add(BARRA_COMPASSO, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 50, 110, 33));

        CLAVE_FA.setText("Clave Fa");
        CLAVE_FA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CLAVE_FAActionPerformed(evt);
            }
        });
        painelBotoes.add(CLAVE_FA, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 10, 110, 33));

        REMOVER.setText("Remover");
        REMOVER.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                REMOVERActionPerformed(evt);
            }
        });
        painelBotoes.add(REMOVER, new org.netbeans.lib.awtextra.AbsoluteConstraints(1310, 10, 120, 33));

        IE_PAUSA.setText("Pausa");
        IE_PAUSA.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                IE_PAUSAMouseReleased(evt);
            }
        });
        painelBotoes.add(IE_PAUSA, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 80, 30));

        SELECIONANDO.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        SELECIONANDO.setText("Selecionando: Semibreve");
        painelBotoes.add(SELECIONANDO, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 90, 290, -1));

        CLAVE_DO.setText("Clave Do");
        CLAVE_DO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CLAVE_DOActionPerformed(evt);
            }
        });
        painelBotoes.add(CLAVE_DO, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 50, 120, 30));

        ADICIONAR.setText("Agrupar");
        ADICIONAR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ADICIONARActionPerformed(evt);
            }
        });
        painelBotoes.add(ADICIONAR, new org.netbeans.lib.awtextra.AbsoluteConstraints(1190, 10, 120, 33));

        SUSTENIDOS.setText("Sustenidos");
        SUSTENIDOS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SUSTENIDOSActionPerformed(evt);
            }
        });
        painelBotoes.add(SUSTENIDOS, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 10, 120, 33));

        BEMOIS.setText("Bemois");
        BEMOIS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BEMOISActionPerformed(evt);
            }
        });
        painelBotoes.add(BEMOIS, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 50, 120, 33));

        jButton1.setText("Enviar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        painelBotoes.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1190, 50, 120, 30));

        MARCACAO_TEMPO.setText("Tempo");
        MARCACAO_TEMPO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MARCACAO_TEMPOActionPerformed(evt);
            }
        });
        painelBotoes.add(MARCACAO_TEMPO, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 50, 110, 30));

        OUTROS_SIMBOLOS.setText("Outros");
        OUTROS_SIMBOLOS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OUTROS_SIMBOLOSActionPerformed(evt);
            }
        });
        painelBotoes.add(OUTROS_SIMBOLOS, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 10, 110, 30));

        jSplitPane1.setTopComponent(painelBotoes);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jScrollPane3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        Container.setMaximumSize(new java.awt.Dimension(10, 10));
        Container.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        ROTULAR_P.setBackground(new java.awt.Color(255, 255, 255));
        ROTULAR_P.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Rotular", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 24))); // NOI18N
        ROTULAR_P.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        ROTULAR_P.setMaximumSize(new java.awt.Dimension(10, 10));
        ROTULAR_P.setOpaque(false);
        ROTULAR_P.setPreferredSize(new java.awt.Dimension(10, 10));
        ROTULAR_P.setLayout(new java.awt.BorderLayout());
        Container.add(ROTULAR_P, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1530, 390));

        SEMINIMAS_P.setBackground(new java.awt.Color(255, 255, 255));
        SEMINIMAS_P.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Semimas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 22))); // NOI18N
        SEMINIMAS_P.setMaximumSize(new java.awt.Dimension(10, 10));
        SEMINIMAS_P.setOpaque(false);
        SEMINIMAS_P.setPreferredSize(new java.awt.Dimension(10, 10));
        SEMINIMAS_P.setLayout(new java.awt.BorderLayout());
        Container.add(SEMINIMAS_P, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 390, 210, 200));

        COLCHEIAS_P.setBackground(new java.awt.Color(255, 255, 255));
        COLCHEIAS_P.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Colcheias", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 22))); // NOI18N
        COLCHEIAS_P.setMaximumSize(new java.awt.Dimension(5, 5));
        COLCHEIAS_P.setMinimumSize(new java.awt.Dimension(5, 5));
        COLCHEIAS_P.setOpaque(false);
        COLCHEIAS_P.setPreferredSize(new java.awt.Dimension(5, 5));
        COLCHEIAS_P.setLayout(new java.awt.BorderLayout());
        Container.add(COLCHEIAS_P, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 390, 210, 200));

        BARRA_COMPASSO_P.setBackground(new java.awt.Color(255, 255, 255));
        BARRA_COMPASSO_P.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Barra Compasso", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 22))); // NOI18N
        BARRA_COMPASSO_P.setMaximumSize(new java.awt.Dimension(5, 5));
        BARRA_COMPASSO_P.setMinimumSize(new java.awt.Dimension(5, 5));
        BARRA_COMPASSO_P.setOpaque(false);
        BARRA_COMPASSO_P.setPreferredSize(new java.awt.Dimension(5, 5));
        BARRA_COMPASSO_P.setLayout(new java.awt.BorderLayout());
        Container.add(BARRA_COMPASSO_P, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 800, 210, 210));

        MINIMAS_P.setBackground(new java.awt.Color(255, 255, 255));
        MINIMAS_P.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Minimas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 22))); // NOI18N
        MINIMAS_P.setMaximumSize(new java.awt.Dimension(10, 10));
        MINIMAS_P.setOpaque(false);
        MINIMAS_P.setPreferredSize(new java.awt.Dimension(10, 10));
        MINIMAS_P.setLayout(new java.awt.BorderLayout());
        Container.add(MINIMAS_P, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 390, 210, 200));

        SUSTENIDOS_P.setBackground(new java.awt.Color(255, 255, 255));
        SUSTENIDOS_P.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Sustenidos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 22))); // NOI18N
        SUSTENIDOS_P.setMaximumSize(new java.awt.Dimension(5, 5));
        SUSTENIDOS_P.setMinimumSize(new java.awt.Dimension(5, 5));
        SUSTENIDOS_P.setOpaque(false);
        SUSTENIDOS_P.setPreferredSize(new java.awt.Dimension(5, 5));
        SUSTENIDOS_P.setLayout(new java.awt.BorderLayout());
        Container.add(SUSTENIDOS_P, new org.netbeans.lib.awtextra.AbsoluteConstraints(1260, 390, 210, 200));

        BEMOIS_P.setBackground(new java.awt.Color(255, 255, 255));
        BEMOIS_P.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Bemois", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 22))); // NOI18N
        BEMOIS_P.setMaximumSize(new java.awt.Dimension(5, 5));
        BEMOIS_P.setMinimumSize(new java.awt.Dimension(5, 5));
        BEMOIS_P.setOpaque(false);
        BEMOIS_P.setPreferredSize(new java.awt.Dimension(5, 5));
        BEMOIS_P.setLayout(new java.awt.BorderLayout());
        Container.add(BEMOIS_P, new org.netbeans.lib.awtextra.AbsoluteConstraints(1050, 390, 210, 200));

        CLAVE_SOL_P.setBackground(new java.awt.Color(255, 255, 255));
        CLAVE_SOL_P.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Clave Sol", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 22))); // NOI18N
        CLAVE_SOL_P.setMaximumSize(new java.awt.Dimension(5, 5));
        CLAVE_SOL_P.setMinimumSize(new java.awt.Dimension(5, 5));
        CLAVE_SOL_P.setOpaque(false);
        CLAVE_SOL_P.setPreferredSize(new java.awt.Dimension(5, 5));
        CLAVE_SOL_P.setLayout(new java.awt.BorderLayout());
        Container.add(CLAVE_SOL_P, new org.netbeans.lib.awtextra.AbsoluteConstraints(1050, 590, 210, 210));

        CLAVE_DO_P.setBackground(new java.awt.Color(255, 255, 255));
        CLAVE_DO_P.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Clave Do", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 22))); // NOI18N
        CLAVE_DO_P.setMaximumSize(new java.awt.Dimension(5, 5));
        CLAVE_DO_P.setMinimumSize(new java.awt.Dimension(5, 5));
        CLAVE_DO_P.setOpaque(false);
        CLAVE_DO_P.setPreferredSize(new java.awt.Dimension(5, 5));
        CLAVE_DO_P.setLayout(new java.awt.BorderLayout());
        Container.add(CLAVE_DO_P, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 800, 210, 210));

        SEMIBREVE_P.setBackground(new java.awt.Color(255, 255, 255));
        SEMIBREVE_P.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Semibreve", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 22))); // NOI18N
        SEMIBREVE_P.setMaximumSize(new java.awt.Dimension(10, 10));
        SEMIBREVE_P.setOpaque(false);
        SEMIBREVE_P.setPreferredSize(new java.awt.Dimension(10, 10));
        SEMIBREVE_P.setLayout(new java.awt.BorderLayout());
        Container.add(SEMIBREVE_P, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 390, 210, 200));

        PAUSA_SEMIBREVE_P.setBackground(new java.awt.Color(255, 255, 255));
        PAUSA_SEMIBREVE_P.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Pausa Semibreve", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 22))); // NOI18N
        PAUSA_SEMIBREVE_P.setMaximumSize(new java.awt.Dimension(10, 10));
        PAUSA_SEMIBREVE_P.setOpaque(false);
        PAUSA_SEMIBREVE_P.setPreferredSize(new java.awt.Dimension(10, 10));
        PAUSA_SEMIBREVE_P.setLayout(new java.awt.BorderLayout());
        Container.add(PAUSA_SEMIBREVE_P, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 590, 210, 210));

        PAUSA_MINIMA_P.setBackground(new java.awt.Color(255, 255, 255));
        PAUSA_MINIMA_P.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Pausa Minima", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 22))); // NOI18N
        PAUSA_MINIMA_P.setMaximumSize(new java.awt.Dimension(10, 10));
        PAUSA_MINIMA_P.setOpaque(false);
        PAUSA_MINIMA_P.setPreferredSize(new java.awt.Dimension(10, 10));
        PAUSA_MINIMA_P.setLayout(new java.awt.BorderLayout());
        Container.add(PAUSA_MINIMA_P, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 590, 210, 210));

        PAUSA_SEMINIMA_P.setBackground(new java.awt.Color(255, 255, 255));
        PAUSA_SEMINIMA_P.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Pausa Seminima", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 22))); // NOI18N
        PAUSA_SEMINIMA_P.setMaximumSize(new java.awt.Dimension(10, 10));
        PAUSA_SEMINIMA_P.setOpaque(false);
        PAUSA_SEMINIMA_P.setPreferredSize(new java.awt.Dimension(10, 10));
        PAUSA_SEMINIMA_P.setLayout(new java.awt.BorderLayout());
        Container.add(PAUSA_SEMINIMA_P, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 590, 210, 210));

        PAUSA_COLCHEIA_P.setBackground(new java.awt.Color(255, 255, 255));
        PAUSA_COLCHEIA_P.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Pausa Colcheia", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 22))); // NOI18N
        PAUSA_COLCHEIA_P.setMaximumSize(new java.awt.Dimension(10, 10));
        PAUSA_COLCHEIA_P.setOpaque(false);
        PAUSA_COLCHEIA_P.setPreferredSize(new java.awt.Dimension(10, 10));
        PAUSA_COLCHEIA_P.setLayout(new java.awt.BorderLayout());
        Container.add(PAUSA_COLCHEIA_P, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 590, 210, 210));

        MARCACAO_TEMPO_P.setBackground(new java.awt.Color(255, 255, 255));
        MARCACAO_TEMPO_P.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Marcacao tempo", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 22))); // NOI18N
        MARCACAO_TEMPO_P.setMaximumSize(new java.awt.Dimension(10, 10));
        MARCACAO_TEMPO_P.setOpaque(false);
        MARCACAO_TEMPO_P.setPreferredSize(new java.awt.Dimension(10, 10));
        MARCACAO_TEMPO_P.setLayout(new java.awt.BorderLayout());
        Container.add(MARCACAO_TEMPO_P, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 800, 210, 210));

        OUTROS_SIMBOLOS_P.setBackground(new java.awt.Color(255, 255, 255));
        OUTROS_SIMBOLOS_P.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Outros", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 22))); // NOI18N
        OUTROS_SIMBOLOS_P.setMaximumSize(new java.awt.Dimension(10, 10));
        OUTROS_SIMBOLOS_P.setOpaque(false);
        OUTROS_SIMBOLOS_P.setPreferredSize(new java.awt.Dimension(10, 10));
        OUTROS_SIMBOLOS_P.setLayout(new java.awt.BorderLayout());
        Container.add(OUTROS_SIMBOLOS_P, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 800, 210, 210));

        SEMICOLCHEIAS_P.setBackground(new java.awt.Color(255, 255, 255));
        SEMICOLCHEIAS_P.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Semicolcheias", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 22))); // NOI18N
        SEMICOLCHEIAS_P.setMaximumSize(new java.awt.Dimension(5, 5));
        SEMICOLCHEIAS_P.setMinimumSize(new java.awt.Dimension(5, 5));
        SEMICOLCHEIAS_P.setOpaque(false);
        SEMICOLCHEIAS_P.setPreferredSize(new java.awt.Dimension(5, 5));
        SEMICOLCHEIAS_P.setLayout(new java.awt.BorderLayout());
        Container.add(SEMICOLCHEIAS_P, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 390, 210, 200));

        PAUSA_SEMICOLCHEIA_P.setBackground(new java.awt.Color(255, 255, 255));
        PAUSA_SEMICOLCHEIA_P.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Pausa Semicolcheia", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 22))); // NOI18N
        PAUSA_SEMICOLCHEIA_P.setMaximumSize(new java.awt.Dimension(10, 10));
        PAUSA_SEMICOLCHEIA_P.setOpaque(false);
        PAUSA_SEMICOLCHEIA_P.setPreferredSize(new java.awt.Dimension(10, 10));
        PAUSA_SEMICOLCHEIA_P.setLayout(new java.awt.BorderLayout());
        Container.add(PAUSA_SEMICOLCHEIA_P, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 590, 210, 210));

        CLAVE_FA_P.setBackground(new java.awt.Color(255, 255, 255));
        CLAVE_FA_P.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Clave Fa", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 22))); // NOI18N
        CLAVE_FA_P.setMaximumSize(new java.awt.Dimension(5, 5));
        CLAVE_FA_P.setMinimumSize(new java.awt.Dimension(5, 5));
        CLAVE_FA_P.setOpaque(false);
        CLAVE_FA_P.setPreferredSize(new java.awt.Dimension(5, 5));
        CLAVE_FA_P.setLayout(new java.awt.BorderLayout());
        Container.add(CLAVE_FA_P, new org.netbeans.lib.awtextra.AbsoluteConstraints(1260, 590, 210, 210));

        jScrollPane3.setViewportView(Container);

        jPanel2.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        jSplitPane1.setRightComponent(jPanel2);

        getContentPane().add(jSplitPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void COLCHEIAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_COLCHEIAActionPerformed
        setSelecionado(COLCHEIA);
    }//GEN-LAST:event_COLCHEIAActionPerformed

    private void SEMIBREVEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SEMIBREVEActionPerformed
        setSelecionado(SEMIBREVE);
    }//GEN-LAST:event_SEMIBREVEActionPerformed

    private void MINIMAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MINIMAActionPerformed
        setSelecionado(MINIMA);
    }//GEN-LAST:event_MINIMAActionPerformed

    private void SEMINIMAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SEMINIMAActionPerformed
        setSelecionado(SEMINIMA);
    }//GEN-LAST:event_SEMINIMAActionPerformed

    private void SEMICOLCHEIAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SEMICOLCHEIAActionPerformed
        setSelecionado(SEMICOLCHEIA);
    }//GEN-LAST:event_SEMICOLCHEIAActionPerformed

    private void BARRA_COMPASSOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BARRA_COMPASSOActionPerformed
        setSelecionado(BARRA_COMPASSO);
    }//GEN-LAST:event_BARRA_COMPASSOActionPerformed

    private void CLAVE_SOLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CLAVE_SOLActionPerformed
        setSelecionado(CLAVE_SOL);
    }//GEN-LAST:event_CLAVE_SOLActionPerformed

    private void CLAVE_FAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CLAVE_FAActionPerformed
        setSelecionado(CLAVE_FA);
    }//GEN-LAST:event_CLAVE_FAActionPerformed

    private void CLAVE_DOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CLAVE_DOActionPerformed
        setSelecionado(CLAVE_DO);
    }//GEN-LAST:event_CLAVE_DOActionPerformed

    private void ADICIONARActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ADICIONARActionPerformed

        List<Figura> selecionados = definirTipo(getSelecionadosRotular(), selecionado);

        if (selecionado.equalsIgnoreCase("PausaSemibreve")) {
            pausaSemibreve.addAll(selecionados);
            recarregar(pausaSemibreve, pausaSemibreveJL);
        } else if (selecionado.equalsIgnoreCase("PausaMinima")) {

            pausaMinima.addAll(selecionados);
            recarregar(pausaMinima, pausaMinimaJL);
        } else if (selecionado.equalsIgnoreCase("PausaSeminima")) {
            pausaSeminima.addAll(selecionados);
            recarregar(pausaSeminima, pausaSeminimaJL);
        } else if (selecionado.equalsIgnoreCase("PausaColcheia")) {
            pausaColcheia.addAll(selecionados);
            recarregar(pausaColcheia, pausaColcheiaJL);
        } else if (selecionado.equalsIgnoreCase("Pausasemicolcheia")) {
            pausaSemicolcheias.addAll(selecionados);
            recarregar(pausaSemicolcheias, pausaSemicolcheiaJL);
        } else if (selecionado.equalsIgnoreCase("Semibreve")) {
            semibreve.addAll(selecionados);
            recarregar(semibreve, semibreveJL);
        } else if (selecionado.equalsIgnoreCase("Minima")) {
            minimas.addAll(selecionados);
            recarregar(minimas, minimasJL);
        } else if (selecionado.equalsIgnoreCase("Seminima")) {
            seminimas.addAll(selecionados);
            recarregar(seminimas, seminimasJL);
        } else if (selecionado.equalsIgnoreCase("Colcheia")) {
            colcheias.addAll(selecionados);
            recarregar(colcheias, colcheiasJL);
        } else if (selecionado.equalsIgnoreCase("Semicolcheia")) {
            semicolcheias.addAll(selecionados);
            recarregar(semicolcheias, semiColcheiasJL);
        } else if (selecionado.equalsIgnoreCase("Barra Compasso")) {
            barraCompasso.addAll(selecionados);
            recarregar(barraCompasso, barraCompassoJL);
        } else if (selecionado.equalsIgnoreCase("Clave Sol")) {
            claveSol.addAll(selecionados);
            recarregar(claveSol, claveSolJL);
        } else if (selecionado.equalsIgnoreCase("Clave Fa")) {
            claveFa.addAll(selecionados);
            recarregar(claveFa, claveFaJL);
        } else if (selecionado.equalsIgnoreCase("Clave Do")) {
            claveDo.addAll(selecionados);
            recarregar(claveDo, claveDoJL);
        } else if (selecionado.equalsIgnoreCase("Sustenidos")) {
            sustenidos.addAll(selecionados);
            recarregar(sustenidos, sustenidosJL);
        } else if (selecionado.equalsIgnoreCase("Bemois")) {
            bemois.addAll(selecionados);
            recarregar(bemois, bemoisJL);
        } else if (selecionado.equalsIgnoreCase("Tempo")) {
            marcacaoTempo.addAll(selecionados);
            recarregar(marcacaoTempo, marcacaoTempoJL);
        } else if (selecionado.equalsIgnoreCase("Outros")) {
            outros_simbolos.addAll(selecionados);
            recarregar(outros_simbolos, outrosSimbolosJL);
        }


    }//GEN-LAST:event_ADICIONARActionPerformed

    private void SUSTENIDOSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SUSTENIDOSActionPerformed
        setSelecionado(SUSTENIDOS);
    }//GEN-LAST:event_SUSTENIDOSActionPerformed

    private void BEMOISActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BEMOISActionPerformed
        setSelecionado(BEMOIS);
    }//GEN-LAST:event_BEMOISActionPerformed

    private void REMOVERActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_REMOVERActionPerformed

        if (selectedJList != null
                && selectedJList != rotularJL) {
            List<Figura> selecionados = selectedJList.getSelectedValuesList();
            rotular.addAll(selecionados);

            List<Figura> remover = selectedJList.getSelectedValuesList();
            ArrayList<Figura> lista = getRespectivoArray(selectedJList);
            for (Figura r : remover) {
                lista.remove(r);
            }

            recarregar(lista, selectedJList);
            recarregar(rotular, rotularJL);
            selectedJList.repaint();
            rotularJL.repaint();
        } else {
            JOptionPane.showMessageDialog(this, "Para remover selecione um o mais itens nas caixas de rotulacao!");
        }
    }//GEN-LAST:event_REMOVERActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            for (Figura f : semibreve) {
                Utilities.addFiguraDataSource(f);
            }

            for (Figura f : minimas) {
                Utilities.addFiguraDataSource(f);
            }

            for (Figura f : seminimas) {
                Utilities.addFiguraDataSource(f);
            }

            for (Figura f : colcheias) {
                Utilities.addFiguraDataSource(f);
            }

            for (Figura f : claveSol) {
                Utilities.addFiguraDataSource(f);
            }

            for (Figura f : claveFa) {
                Utilities.addFiguraDataSource(f);
            }

            for (Figura f : claveDo) {
                Utilities.addFiguraDataSource(f);
            }

            for (Figura f : pausaSemibreve) {
                Utilities.addFiguraDataSource(f);
            }

            for (Figura f : pausaMinima) {
                Utilities.addFiguraDataSource(f);
            }

            for (Figura f : pausaSeminima) {
                Utilities.addFiguraDataSource(f);
            }

            for (Figura f : pausaColcheia) {
                Utilities.addFiguraDataSource(f);
            }

            for (Figura f : sustenidos) {
                Utilities.addFiguraDataSource(f);
            }

            for (Figura f : bemois) {
                Utilities.addFiguraDataSource(f);
            }

            for (Figura f : barraCompasso) {
                Utilities.addFiguraDataSource(f);
            }

            for (Figura f : marcacaoTempo) {
                Utilities.addFiguraDataSource(f);
            }

            for (Figura f : outros_simbolos) {
                Utilities.addFiguraDataSource(f);
            }

            for (Figura f : semicolcheias) {
                Utilities.addFiguraDataSource(f);
            }

            for (Figura f : pausaSemicolcheias) {
                Utilities.addFiguraDataSource(f);
            }

            Utilities.salvarDsScoreReader();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void MARCACAO_TEMPOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MARCACAO_TEMPOActionPerformed
        setSelecionado(MARCACAO_TEMPO);
    }//GEN-LAST:event_MARCACAO_TEMPOActionPerformed

    private void OUTROS_SIMBOLOSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OUTROS_SIMBOLOSActionPerformed
        setSelecionado(OUTROS_SIMBOLOS);
    }//GEN-LAST:event_OUTROS_SIMBOLOSActionPerformed

    private void IE_PAUSAMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_IE_PAUSAMouseReleased
        Component[] components = painelBotoes.getComponents();
        for (Component component : components) {
            if (component instanceof JButton) {
                JButton jb = ((JButton) component);
                if (selecionado.startsWith(jb.getText())
                        || selecionado.replace("Pausa", "").startsWith(jb.getText())) {
                    if (!jb.getText().contains("Clave")
                            && !jb.getText().contains("Suste")
                            && !jb.getText().contains("Bem")
                            && !jb.getText().contains("Tem")
                            && !jb.getText().contains("Barr")
                            && !jb.getText().contains("Outros")) {
                        jb.doClick();
                    }
                }
            }
        }
    }//GEN-LAST:event_IE_PAUSAMouseReleased

    public ArrayList<Figura> getRespectivoArray(JList<Figura> lista) {
        if (lista == semibreveJL) {
            return semibreve;
        } else if (lista == minimasJL) {
            return minimas;
        } else if (lista == seminimasJL) {
            return seminimas;
        } else if (lista == colcheiasJL) {
            return colcheias;
        } else if (lista == pausaSemibreveJL) {
            return pausaSemibreve;
        } else if (lista == pausaMinimaJL) {
            return pausaMinima;
        } else if (lista == pausaSeminimaJL) {
            return pausaSeminima;
        } else if (selectedJList == pausaColcheiaJL) {
            return pausaColcheia;
        } else if (lista == sustenidosJL) {
            return sustenidos;
        } else if (lista == bemoisJL) {
            return bemois;
        } else if (lista == claveSolJL) {
            return claveSol;
        } else if (lista == claveFaJL) {
            return claveFa;
        } else if (lista == claveDoJL) {
            return claveDo;
        } else if (lista == semiColcheiasJL) {
            return semicolcheias;
        } else if (lista == outrosSimbolosJL) {
            return outros_simbolos;
        }

        return new ArrayList<Figura>();
    }

    public void setSelecionado(JButton jb) {
        if (!jb.getText().contains("Clave")
                && !jb.getText().contains("Suste")
                && !jb.getText().contains("Bem")
                && !jb.getText().contains("Tem")
                && !jb.getText().contains("Barr")
                && !jb.getText().contains("Outros")) {
            this.selecionado = IE_PAUSA.isSelected() ? "Pausa" + jb.getText() : jb.getText();

        } else {
            this.selecionado = jb.getText();
        }

        SELECIONANDO.setText("Selecionando: " + selecionado);
    }

    private JList getJlist(ArrayList<Figura> figuras) {
        return createListFiguras(figuras);
    }

    private JPanel createMainPanel(JList lista) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        // create list book and set to scrollpane and add to panel
        panel.add(new JScrollPane(lista),
                BorderLayout.CENTER);
        return panel;
    }

    private void recarregar(ArrayList<Figura> figuras, JList lista) {
        DefaultListModel<Figura> model = new DefaultListModel<>();

        for (Figura figura : figuras) {
            model.addElement(figura);
        }
        lista.setModel(model);
        lista.repaint();
        lista.updateUI();
    }

    private JList<Figura> createListFiguras(ArrayList<Figura> figuras) {
        // create List model
        DefaultListModel<Figura> model = new DefaultListModel<>();

        for (Figura figura : figuras) {
            model.addElement(figura);
        }

        // create JList with model
        JList<Figura> list = new JList<Figura>(model);
        list.addFocusListener(new FocusAdapter() {

            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                selectedJList = (JList<Figura>) e.getComponent();
            }

        });
        list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        list.setVisibleRowCount(-1);
        // set cell renderer
        list.setCellRenderer(new FiguraRenderer());
        return list;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ADICIONAR;
    private javax.swing.JButton BARRA_COMPASSO;
    private javax.swing.JPanel BARRA_COMPASSO_P;
    private javax.swing.JButton BEMOIS;
    private javax.swing.JPanel BEMOIS_P;
    private javax.swing.JButton CLAVE_DO;
    private javax.swing.JPanel CLAVE_DO_P;
    private javax.swing.JButton CLAVE_FA;
    private javax.swing.JPanel CLAVE_FA_P;
    private javax.swing.JButton CLAVE_SOL;
    private javax.swing.JPanel CLAVE_SOL_P;
    private javax.swing.JButton COLCHEIA;
    private javax.swing.JPanel COLCHEIAS_P;
    private javax.swing.JPanel Container;
    private javax.swing.JCheckBox IE_PAUSA;
    private javax.swing.JButton MARCACAO_TEMPO;
    private javax.swing.JPanel MARCACAO_TEMPO_P;
    private javax.swing.JButton MINIMA;
    private javax.swing.JPanel MINIMAS_P;
    private javax.swing.JButton OUTROS_SIMBOLOS;
    private javax.swing.JPanel OUTROS_SIMBOLOS_P;
    private javax.swing.JPanel PAUSA_COLCHEIA_P;
    private javax.swing.JPanel PAUSA_MINIMA_P;
    private javax.swing.JPanel PAUSA_SEMIBREVE_P;
    private javax.swing.JPanel PAUSA_SEMICOLCHEIA_P;
    private javax.swing.JPanel PAUSA_SEMINIMA_P;
    private javax.swing.JButton REMOVER;
    private javax.swing.JPanel ROTULAR_P;
    private javax.swing.JLabel SELECIONANDO;
    private javax.swing.JButton SEMIBREVE;
    private javax.swing.JPanel SEMIBREVE_P;
    private javax.swing.JButton SEMICOLCHEIA;
    private javax.swing.JPanel SEMICOLCHEIAS_P;
    private javax.swing.JButton SEMINIMA;
    private javax.swing.JPanel SEMINIMAS_P;
    private javax.swing.JButton SUSTENIDOS;
    private javax.swing.JPanel SUSTENIDOS_P;
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JPanel painelBotoes;
    // End of variables declaration//GEN-END:variables

    private List<Figura> getSelecionadosRotular() {
        List<Figura> selecionados = rotularJL.getSelectedValuesList();
        for (Figura s : selecionados) {
            rotular.remove(s);
        }
        recarregar(rotular, rotularJL);
        return selecionados;
    }

    private List<Figura> definirTipo(List<Figura> selecionados, String selecionado) {
        for (Figura s : selecionados) {
            s.setTipo(selecionado.replace(" ", ""));
        }
        return selecionados;
    }
}
