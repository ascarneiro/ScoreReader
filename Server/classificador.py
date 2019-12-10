import os
import itertools
import numpy
import matplotlib.pyplot as plt
from skimage.transform import resize
import random
from sklearn.model_selection import train_test_split

from PIL import Image
from muscima.io import parse_cropobject_list
from sklearn.externals import joblib
from sklearn.neighbors import kneighbors_graph
from sklearn.metrics import classification_report
from sklearn.manifold import Isomap
from sklearn.neighbors import NearestNeighbors
import datasourcescorereader as dscoreader
from sklearn.cluster import KMeans

import numpy as np
import matplotlib.pyplot as plt
from sklearn import datasets
from sklearn.model_selection import train_test_split
from sklearn.decomposition import PCA
from sklearn.discriminant_analysis import LinearDiscriminantAnalysis
from sklearn.neighbors import KNeighborsClassifier
from sklearn.pipeline import make_pipeline
from sklearn.preprocessing import StandardScaler
from mpl_toolkits.mplot3d import Axes3D



class Classificador(object):

    def __init__(self):
        self.dataSourceScoreReader = dscoreader.ScoreReaderDataSet()
        self.DIR_TREINAMENTO = 'C:/Users/ascarneiro/Desktop/TCC/ScoreReader/treinamento/'
        self.data_source_carregado = False
        self.LARGURA = 10  # Largura da figura
        self.ALTURA = 40  # altura da figura
        self.K_VIZINHOS_PROXIMOS = 5;  # numero de vizinhos proximos a verificar no KNN
        self.RANDOMSTATE =  42
        self.NOME_MODELO = ""
        self.TIPO_MODELO = ""

        self.ROTULO_SEMINIMA = 0
        self.ROTULO_MINIMA = 1
        self.ROTULO_CLAVEDO = 2
        self.ROTULO_CLAVESOL = 3
        self.ROTULO_CLAVEFA = 4
        self.ROTULO_PAUSACOLCHEIA = 5
        self.ROTULO_PAUSASEMINIMA = 6
        self.ROTULO_PAUSASEMICOLCHEIA = 7
        self.ROTULO_PAUSASEMIBREVE = 8
        self.ROTULO_PAUSAMINIMA = 9
        self.ROTULO_BARRACOMPASSO = 10
        self.ROTULO_SUSTENIDOS = 11
        self.ROTULO_BEMOIS = 12
        self.ROTULO_MARCACAO_TEMPO = 13
        self.ROTULO_COLCHEIA = 14
        self.ROTULO_SEMIBREVE = 15
        self.ROTULO_OUTROS = 16
        self.ROTULO_SEMICOLCHEIAS = 17

        self.NR_CLUSTERS = 15


    # imprimir classe das notas
    def printClasses(self, cropobjects):
        _cropobj_dict = {c.objid: c for c in cropobjects}
        output = set()
        for x in cropobjects:
            output.add(x.clsname)

        for c in output:
            print(c)

    def extrair_elementos_score_reader(self, dataSource):

        print ('Carregando do DataSource ScoreReader')
        seminimas = dataSource.figuras('Seminima')
        minimas = dataSource.figuras('Minima')
        clavesol = dataSource.figuras('ClaveSol')
        clavefa = dataSource.figuras('ClaveFa')
        clavedo = dataSource.figuras('ClaveDo')
        pausasemicolcheia = dataSource.figuras('PausaSemiColcheia')
        pausacolcheia = dataSource.figuras('PausaColcheia')
        pausaseminima = dataSource.figuras('PausaSeminima')
        pausaminima = dataSource.figuras('PausaMinima')
        pausasemibreve = dataSource.figuras('PausaSemibreve')
        barracompasso = dataSource.figuras('BarraCompasso')
        sustenidos = dataSource.figuras('Sustenidos')
        bemois = dataSource.figuras('Bemois')
        marcacaoTempo = dataSource.figuras('Tempo')
        colcheias = dataSource.figuras('Colcheia')
        semibreves = dataSource.figuras('Semibreve')
        outros_simbolos = dataSource.figuras('Outros')
        semicolcheias = dataSource.figuras('SemiColcheia')


        return minimas, seminimas,  clavesol, clavefa, clavedo, \
               pausasemicolcheia, pausacolcheia, pausaseminima, pausaminima, \
               pausasemibreve, barracompasso, sustenidos, bemois, marcacaoTempo, colcheias, semibreves, outros_simbolos, semicolcheias



    def extrair_elementos_muscima(self, modelo, parametros):

        print ('Carregando do DataSource MUSCIMA++')
        semibreves = []
        minimas = []
        seminimas = []
        colcheias = []
        semicolcheias = []
        fusas = []
        semifusas = []
        clavesol = []
        clavefa = []
        clavedo = []
        fermatas = []
        pausasemibreve = []
        pausaseminima = []
        pausaminima = []
        pausacolcheia = []
        pausasemicolcheia = []
        pausafusa = []
        pausasemifusa = []
        fermata = []
        ligadura = []
        barracompasso = []
        sustenidos = []
        bemois = []
        marcacaotempo = []
        colcheias = []
        outros_simbolos = []


        _modelo_dict = {elem.objid: elem for elem in modelo}
        figuras = []
        for c in modelo:
            if (c.clsname == 'notehead-full') or \
                    (c.clsname == 'notehead-empty'):

                possui_haste = False
                possui_viga_ou_bandeira = False
                objeto_com_haste = None
                for o in c.outlinks:
                    _o_obj = _modelo_dict[o]
                    if _o_obj.clsname == 'stem':
                        possui_haste = True
                        objeto_com_haste = _o_obj
                    elif _o_obj.clsname == 'beam':
                        possui_viga_ou_bandeira = True
                    elif _o_obj.clsname.endswith('flag'):
                        possui_viga_ou_bandeira = True
                if possui_haste and (not possui_viga_ou_bandeira):
                    # Tambem precisamos verificar os acordes de seminima.
                    # As hastes tem apenas inlinks de notas, portanto, verificar
                    # para varios inlinks fara o procedimento.
                    if len(objeto_com_haste.inlinks) == 1:
                        if c.clsname == 'notehead-full' and parametros.QT_SEMINIMA > 0:
                            figuras.append((c, objeto_com_haste))
                            parametros.QT_SEMINIMA = parametros.QT_SEMINIMA -1
                        elif c.clsname == 'notehead-empty' and parametros.QT_MINIMA > 0:
                            figuras.append((c, objeto_com_haste))
                            parametros.QT_MINIMA = parametros.QT_MINIMA - 1
            else:
                if c.clsname == 'tie' and parametros.QT_LIGADURA > 0:
                    figuras.append((c, c))
                    parametros.QT_LIGADURA = parametros.QT_LIGADURA - 1

                elif c.clsname == 'g-clef' and parametros.QT_CLAVESOL > 0:
                    figuras.append((c, c))
                    parametros.QT_CLAVESOL = parametros.QT_CLAVESOL - 1

                elif c.clsname == 'f-clef' and parametros.QT_CLAVEFA > 0:
                    figuras.append((c, c))
                    parametros.QT_CLAVEFA = parametros.QT_CLAVEFA - 1

                elif c.clsname == 'c-clef' and parametros.QT_CLAVEDO > 0:
                    figuras.append((c, c))
                    parametros.QT_CLAVEDO = parametros.QT_CLAVEDO - 1

                elif c.clsname == '8th_rest' and parametros.QT_PAUSA_COLCHEIA > 0:
                    figuras.append((c, c))
                    parametros.QT_PAUSA_COLCHEIA = parametros.QT_PAUSA_COLCHEIA - 1

                elif c.clsname == '16th_rest' and parametros.QT_PAUSA_SEMICOLCHEIA > 0:
                    figuras.append((c, c))
                    parametros.QT_PAUSA_SEMICOLCHEIA = parametros.QT_PAUSA_SEMICOLCHEIA - 1

                elif c.clsname == 'quarter_rest' and parametros.QT_PAUSA_SEMINIMA > 0:
                    figuras.append((c, c))
                    parametros.QT_PAUSA_SEMINIMA = parametros.QT_PAUSA_SEMINIMA - 1

                elif c.clsname == 'half_rest' and parametros.QT_PAUSA_MINIMA > 0:
                    figuras.append((c, c))
                    parametros.QT_PAUSA_MINIMA = parametros.QT_PAUSA_MINIMA - 1

                elif c.clsname == 'whole_rest' and parametros.QT_PAUSA_SEMIBREVE > 0:
                    figuras.append((c, c))
                    parametros.QT_PAUSA_SEMIBREVE = parametros.QT_PAUSA_SEMIBREVE - 1

                elif c.clsname == 'thin_barline' and parametros.QT_BARRAS_COMPASSO > 0:
                    figuras.append((c, c))
                    parametros.QT_BARRAS_COMPASSO = parametros.QT_BARRAS_COMPASSO - 1;

        seminimas = [(n, s) for n, s in figuras if n.clsname == 'notehead-full']
        minimas = [(n, s) for n, s in figuras if n.clsname == 'notehead-empty']
        ligadura = [(n, s) for n, s in figuras if n.clsname == 'tie']
        clavesol = [(n, s) for n, s in figuras if n.clsname == 'g-clef']
        clavefa = [(n, s) for n, s in figuras if n.clsname == 'f-clef']
        clavedo = [(n, s) for n, s in figuras if n.clsname == 'c-clef']

        pausasemicolcheia = [(n, s) for n, s in figuras if n.clsname == '16th_rest']
        pausacolcheia = [(n, s) for n, s in figuras if n.clsname == '8th_rest']
        pausaseminima = [(n, s) for n, s in figuras if n.clsname == 'quarter_rest']
        pausaminima = [(n, s) for n, s in figuras if n.clsname == 'half_rest']
        pausasemibreve = [(n, s) for n, s in figuras if n.clsname == 'whole_rest']
        barracompasso = [(n, s) for n, s in figuras if n.clsname == 'thin_barline']

        return minimas, seminimas, clavesol, clavefa, clavedo,\
               pausasemicolcheia,pausacolcheia, pausaseminima, pausaminima, \
               pausasemibreve, barracompasso, sustenidos, bemois, marcacaotempo, colcheias, semibreves, outros_simbolos, semicolcheias


    def extrair_imagem(self, tipo, elementos, margin=1):
        if tipo == 'MANUSCRITO':
            # Get the bounding box into which all the objects fit
            top = min([c.top for c in elementos])
            left = min([c.left for c in elementos])
            bottom = max([c.bottom for c in elementos])
            right = max([c.right for c in elementos])

            # Create the canvas onto which the masks will be pasted
            height = bottom - top + 2 * margin
            width = right - left + 2 * margin
            canvas = numpy.zeros((height, width), dtype='uint8')

            for c in elementos:
                # Get coordinates of upper left corner of the CropObject
                # relative to the canvas
                _pt = c.top - top + margin
                _pl = c.left - left + margin
                # We have to add the mask, so as not to overwrite
                # previous nonzeros when symbol bounding boxes overlap.
                canvas[_pt:_pt + c.height, _pl:_pl + c.width] += c.mask

            canvas[canvas > 0] = 1
            return canvas
        else:
            return elementos

    def plotar_imagem(self, mask):
        plt.imshow(mask, cmap='gray', interpolation='nearest')
        plt.show()

    def plotar_imagens(self, masks, row_length=5):
        n_masks = len(masks)
        n_rows = n_masks // row_length + 1
        n_cols = min(n_masks, row_length)
        fig = plt.figure()
        for i, mask in enumerate(masks):
            plt.subplot(n_rows, n_cols, i + 1)
            plt.imshow(mask, cmap='gray', interpolation='nearest')
        # Let's remove the axis labels, they clutter the image.
        for ax in fig.axes:
            ax.set_yticklabels([])
            ax.set_xticklabels([])
            ax.set_yticks([])
            ax.set_xticks([])
        plt.show()

    def treinar_knn(self, tipo, nome, caminho, data_source, parametros, ie_dump):
        #try:

            self.NOME_MODELO = nome
            self.TIPO_MODELO = tipo
            self.caminho = caminho

            if tipo == 'MANUSCRITO':
                if ie_dump == "S":
                    print('Carregando Modelo MUSCIMA++')
                    self.data_source = self.carregar_objeto()
                    print('Modelo MUSCIMA++ carregado')
                else:
                    print('Criando Modelo MUSCIMA++')
                    self.CROPOBJECT_DIR = os.path.join(self.caminho, data_source)
                    self.cropobject_fnames = [os.path.join(self.CROPOBJECT_DIR, f) for f in os.listdir(self.CROPOBJECT_DIR)]
                    self.data_source = [parse_cropobject_list(f) for f in self.cropobject_fnames]
                    self.salvar_objeto(self.data_source)
                    print('Modelo MUSCIMA++ carregado e salvo')
            elif tipo == 'DIGITAL':
                    print('Carregando Modelo ScoreReader')
                    self.dataSourceScoreReader = self.loadDataSourceScoreReader()
                    print('Modelo carregado')
            elif tipo == 'MANUSCRITO':
                    print('Nao implementado ainda.....')

            if tipo == 'MANUSCRITO':
                print('Extraindo figuras modelo MUSCIMA++')
                self.figuras = [self.extrair_elementos_muscima(self.modelo, parametros) for self.modelo in self.data_source]
                print('Figuras modelo MUSCIMA++ extraidas com sucesso...')
            elif tipo == 'DIGITAL':
                print('Extraindo figuras modelo ScoreReader')
                self.figuras = [self.extrair_elementos_score_reader(self.dataSourceScoreReader)]
                print('Figuras modelo ScoreReader extraidas com sucesso...')

            self.treinar(tipo, self.figuras)


            print("Salvando Modelo...")
            nome = self.salvar_modelo(nome, tipo)
            print("Modelo salvo...")
            print("sucesso...")
            return nome

    def addFiguraDataSource(self, tipo, imagem):
        self.dataSourceScoreReader.addFigura(tipo, imagem)

    def treinar(self, tipo, figuras):
        seminimas = list(itertools.chain(*[s for m, s,  g, f, c, ps, pc, psm, pm, psb, bc, st, bm, mt, cc, sb, os, smc in figuras]))
        minimas = list(itertools.chain(*[m for m, s,  g, f, c, ps, pc, psm, pm, psb, bc, st, bm, mt, cc, sb, os, smc in figuras]))
        clavesol = list(itertools.chain(*[g for m, s,  g, f, c, ps, pc, psm, pm, psb, bc, st, bm, mt, cc, sb, os, smc in figuras]))
        clavefa = list(itertools.chain(*[f for m, s,  g, f, c, ps, pc, psm, pm, psb, bc, st, bm, mt, cc, sb, os, smc in figuras]))
        clavedo = list(itertools.chain(*[c for m, s,  g, f, c, ps, pc, psm, pm, psb, bc, st, bm, mt, cc, sb, os, smc in figuras]))
        pausacolcheia = list(itertools.chain(*[pc for m, s, g, f, c, ps, pc, psm, pm, psb, bc, st, bm, mt, cc, sb, os, smc in figuras]))
        pausaseminima = list(itertools.chain(*[psm for m, s,  g, f, c, ps, pc, psm, pm, psb, bc, st, bm, mt, cc, sb, os, smc in figuras]))
        pausasemicolcheia = list(itertools.chain(*[ps for m, s,  g, f, c, ps, pc, psm, pm, psb, bc, st, bm, mt, cc, sb, os, smc in figuras]))
        pausaminima = list(itertools.chain(*[pm for m, s,  g, f, c, ps, pc, psm, pm, psb, bc, st, bm, mt, cc, sb, os, smc in figuras]))
        pausasemibreve = list(itertools.chain(*[psb for m, s,  g, f, c, ps, pc, psm, pm, psb, bc, st, bm, mt, cc, sb, os, smc in figuras]))
        barracompasso = list(itertools.chain(*[bc for m, s,  g, f, c, ps, pc, psm, pm, psb, bc, st, bm, mt, cc, sb, os, smc in figuras]))
        sustenidos = list(itertools.chain(*[st for m, s, g, f, c, ps, pc, psm, pm, psb, bc, st, bm, mt, cc, sb, os, smc in figuras]))
        bemois = list(itertools.chain(*[bm for m, s, g, f, c, ps, pc, psm, pm, psb, bc, st, bm, mt, cc, sb, os, smc in figuras]))
        marcacaotempo = list(itertools.chain(*[mt for m, s, g, f, c, ps, pc, psm, pm, psb, bc, st, bm, mt, cc, sb, os, smc in figuras]))
        colcheias = list(itertools.chain(*[cc for m, s, g, f, c, ps, pc, psm, pm, psb, bc, st, bm, mt, cc, sb, os, smc in figuras]))
        semibreves = list(itertools.chain(*[sb for m, s, g, f, c, ps, pc, psm, pm, psb, bc, st, bm, mt, cc, sb, os, smc in figuras]))
        outros_simbolos = list(itertools.chain(*[os for m, s, g, f, c, ps, pc, psm, pm, psb, bc, st, bm, mt, cc, sb, os, smc in figuras]))
        semicolcheias = list(itertools.chain(*[smc for m, s, g, f, c, ps, pc, psm, pm, psb, bc, st, bm, mt, cc, sb, os, smc in figuras]))




        img_seminimas = [self.extrair_imagem(tipo, s) for s in seminimas]
        img_minimas = [self.extrair_imagem(tipo, m) for m in minimas]
        img_clavesol = [self.extrair_imagem(tipo, g) for g in clavesol]
        img_clavefa = [self.extrair_imagem(tipo, f) for f in clavefa]
        img_clavedo = [self.extrair_imagem(tipo, c) for c in clavedo]
        img_pausacolcheia = [self.extrair_imagem(tipo, pc) for pc in pausacolcheia]
        img_pausaseminima = [self.extrair_imagem(tipo, ps) for ps in pausaseminima]
        img_pausasemicolcheia = [self.extrair_imagem(tipo, psm) for psm in pausasemicolcheia]
        img_pausaminima = [self.extrair_imagem(tipo, pm) for pm in pausaminima]
        img_pausasemibreve = [self.extrair_imagem(tipo, psb) for psb in pausasemibreve]
        img_barracompasso = [self.extrair_imagem(tipo, bc) for bc in barracompasso]
        img_sustenidos = [self.extrair_imagem(tipo, bc) for bc in sustenidos]
        img_bemois = [self.extrair_imagem(tipo, bm) for bm in bemois]
        img_marcacao_tempo = [self.extrair_imagem(tipo, mt) for mt in marcacaotempo]
        img_colcheias = [self.extrair_imagem(tipo, cc) for cc in colcheias]
        img_semibreves = [self.extrair_imagem(tipo, cc) for cc in semibreves]
        img_outros_simbolos = [self.extrair_imagem(tipo, cc) for cc in outros_simbolos]
        img_semicolcheias = [self.extrair_imagem(tipo, cc) for cc in semicolcheias]

        #if tipo == 'MANUSCRITO':
        img_seminimas = [resize(sm, (self.ALTURA, self.LARGURA)) for sm in img_seminimas]
        img_minimas = [resize(mn, (self.ALTURA, self.LARGURA)) for mn in img_minimas]
        img_clavesol = [resize(mn, (self.ALTURA, self.LARGURA)) for mn in img_clavesol]
        img_clavefa = [resize(mn, (self.ALTURA, self.LARGURA)) for mn in img_clavefa]
        img_clavedo = [resize(mn, (self.ALTURA, self.LARGURA)) for mn in img_clavedo]
        img_pausacolcheia = [resize(mn, (self.ALTURA, self.LARGURA)) for mn in img_pausacolcheia]
        img_pausaseminima = [resize(mn, (self.ALTURA, self.LARGURA)) for mn in img_pausaseminima]
        img_pausasemicolcheia = [resize(mn, (self.ALTURA, self.LARGURA)) for mn in img_pausasemicolcheia]
        img_pausaminima = [resize(mn, (self.ALTURA, self.LARGURA)) for mn in img_pausaminima]
        img_pausasemibreve = [resize(mn, (self.ALTURA, self.LARGURA)) for mn in img_pausasemibreve]
        img_barracompasso = [resize(mn, (self.ALTURA, self.LARGURA)) for mn in img_barracompasso]
        img_sustenidos = [resize(mn, (self.ALTURA, self.LARGURA)) for mn in img_sustenidos]
        img_bemois = [resize(mn, (self.ALTURA, self.LARGURA)) for mn in img_bemois]
        img_marcacao_tempo = [resize(mn, (self.ALTURA, self.LARGURA)) for mn in img_marcacao_tempo]
        img_colcheias = [resize(mn, (self.ALTURA, self.LARGURA)) for mn in img_colcheias]
        img_semibreves = [resize(mn, (self.ALTURA, self.LARGURA)) for mn in img_semibreves]
        img_outros_simbolos = [resize(mn, (self.ALTURA, self.LARGURA)) for mn in img_outros_simbolos]
        img_semicolcheias = [resize(mn, (self.ALTURA, self.LARGURA)) for mn in img_semicolcheias]

        # And re-binarize, to compensate for interpolation effects
        for im in img_seminimas:
            im[im > 0] = 1
        for im in img_minimas:
            im[im > 0] = 1
        for im in img_clavesol:
            im[im > 0] = 1
        for im in img_clavefa:
            im[im > 0] = 1
        for im in img_clavedo:
            im[im > 0] = 1
        for im in img_pausacolcheia:
            im[im > 0] = 1
        for im in img_pausaseminima:
            im[im > 0] = 1
        for im in img_pausasemicolcheia:
            im[im > 0] = 1
        for im in img_pausaminima:
            im[im > 0] = 1
        for im in img_pausasemibreve:
            im[im > 0] = 1
        for im in img_barracompasso:
            im[im > 0] = 1
        for im in img_sustenidos:
            im[im > 0] = 1
        for im in img_bemois:
            im[im > 0] = 1
        for im in img_marcacao_tempo:
            im[im > 0] = 1
        for im in img_colcheias:
            im[im > 0] = 1
        for im in img_semibreves:
            im[im > 0] = 1
        for im in img_outros_simbolos:
            im[im > 0] = 1
        for im in img_semicolcheias:
            im[im > 0] = 1

        rotulos_seminimas = [self.ROTULO_SEMINIMA for _ in img_seminimas]
        rotulos_minimas = [self.ROTULO_MINIMA for _ in img_minimas]
        rotulos_clavesol = [self.ROTULO_CLAVESOL for _ in img_clavesol]
        rotulos_clavefa = [self.ROTULO_CLAVEFA for _ in img_clavefa]
        rotulos_clavedo = [self.ROTULO_CLAVEDO for _ in img_clavedo]
        rotulos_pausacolcheia = [self.ROTULO_PAUSACOLCHEIA for _ in img_pausacolcheia]
        rotulos_pausaseminima = [self.ROTULO_PAUSASEMINIMA for _ in img_pausaseminima]
        rotulos_pausasemicolcheia = [self.ROTULO_PAUSASEMICOLCHEIA for _ in img_pausasemicolcheia]
        rotulos_pausaminima = [self.ROTULO_PAUSAMINIMA for _ in img_pausaminima]
        rotulos_pausasemibreve = [self.ROTULO_PAUSASEMIBREVE for _ in img_pausasemibreve]
        rotulos_barracompasso = [self.ROTULO_BARRACOMPASSO for _ in img_barracompasso]
        rotulos_sustenido = [self.ROTULO_SUSTENIDOS for _ in img_sustenidos]
        rotulos_bemois = [self.ROTULO_BEMOIS for _ in img_bemois]
        rotulos_marcacao_tempo = [self.ROTULO_MARCACAO_TEMPO for _ in img_marcacao_tempo]
        rotulos_colcheias = [self.ROTULO_COLCHEIA for _ in img_colcheias]
        rotulos_semibreves = [self.ROTULO_SEMIBREVE for _ in img_semibreves]
        rotulos_outros_simbolos = [self.ROTULO_OUTROS for _ in img_outros_simbolos]
        rotulos_semicolcheias = [self.ROTULO_SEMICOLCHEIAS for _ in img_semicolcheias]


        self.misturadas = img_seminimas +\
                          img_minimas + \
                          img_clavesol + \
                          img_clavefa + \
                          img_clavedo + \
                          img_pausaminima + \
                          img_pausacolcheia + \
                          img_pausaseminima + \
                          img_pausasemicolcheia + \
                          img_pausasemibreve + \
                          img_barracompasso+ \
                          img_sustenidos + \
                          img_bemois + \
                          img_marcacao_tempo  + \
                          img_colcheias + \
                          img_semibreves + \
                          img_outros_simbolos + \
                          img_semicolcheias

            # converte imagem em matrix unidimencional
        self.figuras_array_linha = [n.flatten() for n in self.misturadas]
        self.rotulos_classe = rotulos_seminimas + \
                              rotulos_minimas + \
                              rotulos_clavesol + \
                              rotulos_clavefa + \
                              rotulos_clavedo + \
                              rotulos_pausacolcheia + \
                              rotulos_pausaseminima + \
                              rotulos_pausasemicolcheia + \
                              rotulos_pausaminima + \
                              rotulos_pausasemibreve + \
                              rotulos_barracompasso+ \
                              rotulos_sustenido + \
                              rotulos_bemois + \
                              rotulos_marcacao_tempo  + \
                              rotulos_colcheias  + \
                              rotulos_semibreves  + \
                              rotulos_outros_simbolos + \
                              rotulos_semicolcheias

        self.X_conjunto_treino, self.X_conjunto_teste, self.Y_conjunto_treino, self.Y_conjunto_teste = train_test_split(
            self.figuras_array_linha, self.rotulos_classe, test_size=0.40, random_state=self.RANDOMSTATE,
            stratify=self.rotulos_classe)

        self.KNN = KNeighborsClassifier(n_neighbors=self.K_VIZINHOS_PROXIMOS)
        self.KNN.fit(self.X_conjunto_treino, self.Y_conjunto_treino)


    def getDadosRotulo(self, dados, rotulos, rotulo, indice):
        ret = []
        size = len(dados) -1;
        for idx in range(0, size):
            try:
                if (rotulos[idx] == rotulo):
                    ret.append(dados[idx][indice])
            except:
                print ('Out of range')

        return ret

    def visualizaPontos(self, dados, rotulos, d1, d2):
        fig, ax = plt.subplots()
        ax.scatter(self.getDadosRotulo(dados, rotulos, self.ROTULO_SEMINIMA, d1), self.getDadosRotulo(dados, rotulos, self.ROTULO_SEMINIMA, d2), c='black', marker='.')
        ax.scatter(self.getDadosRotulo(dados, rotulos, self.ROTULO_MINIMA, d1), self.getDadosRotulo(dados, rotulos, self.ROTULO_MINIMA, d2), c='blue', marker='.')
        #ax.scatter(self.getDadosRotulo(dados, rotulos, self.ROTULO_CLAVEDO, d1), self.getDadosRotulo(dados, rotulos, self.ROTULO_CLAVEDO, d2), c='purple', marker='.')
        #ax.scatter(self.getDadosRotulo(dados, rotulos, self.ROTULO_CLAVESOL, d1),self.getDadosRotulo(dados, rotulos, self.ROTULO_CLAVESOL, d2), c='yellow', marker='.')
        #ax.scatter(self.getDadosRotulo(dados, rotulos, self.ROTULO_CLAVEFA, d1),self.getDadosRotulo(dados, rotulos, self.ROTULO_CLAVEFA, d2), c='red', marker='.')
        #ax.scatter(self.getDadosRotulo(dados, rotulos, self.ROTULO_PAUSACOLCHEIA, d1),self.getDadosRotulo(dados, rotulos, self.ROTULO_PAUSACOLCHEIA, d2), c='lime', marker='.')
        #ax.scatter(self.getDadosRotulo(dados, rotulos, self.ROTULO_PAUSASEMINIMA, d1),self.getDadosRotulo(dados, rotulos, self.ROTULO_PAUSASEMINIMA, d2), c='cyan', marker='.')
        #ax.scatter(self.getDadosRotulo(dados, rotulos, self.ROTULO_PAUSASEMICOLCHEIA, d1),self.getDadosRotulo(dados, rotulos, self.ROTULO_PAUSASEMICOLCHEIA, d2), c='orange', marker='.')
        #ax.scatter(self.getDadosRotulo(dados, rotulos, self.ROTULO_PAUSASEMIBREVE, d1),self.getDadosRotulo(dados, rotulos, self.ROTULO_PAUSASEMIBREVE, d2), c='green', marker='.')
        #ax.scatter(self.getDadosRotulo(dados, rotulos, self.ROTULO_PAUSAMINIMA, d1),self.getDadosRotulo(dados, rotulos, self.ROTULO_PAUSAMINIMA, d2), c='aqua', marker='.')
        #ax.scatter(self.getDadosRotulo(dados, rotulos, self.ROTULO_BARRACOMPASSO, d1),self.getDadosRotulo(dados, rotulos, self.ROTULO_BARRACOMPASSO, d2), c='navy', marker='.')
        #ax.scatter(self.getDadosRotulo(dados, rotulos, self.ROTULO_SUSTENIDOS, d1),self.getDadosRotulo(dados, rotulos, self.ROTULO_SUSTENIDOS, d2), c='indigo', marker='.')
        #ax.scatter(self.getDadosRotulo(dados, rotulos, self.ROTULO_BEMOIS, d1),self.getDadosRotulo(dados, rotulos, self.ROTULO_BEMOIS, d2), c='teal', marker='.')
        #ax.scatter(self.getDadosRotulo(dados, rotulos, self.ROTULO_MARCACAO_TEMPO, d1),self.getDadosRotulo(dados, rotulos, self.ROTULO_MARCACAO_TEMPO, d2), c='gold', marker='.')
        #ax.scatter(self.getDadosRotulo(dados, rotulos, self.ROTULO_COLCHEIA, d1),self.getDadosRotulo(dados, rotulos, self.ROTULO_COLCHEIA, d2), c='crimson', marker='.')
        #ax.scatter(self.getDadosRotulo(dados, rotulos, self.ROTULO_SEMIBREVE, d1),self.getDadosRotulo(dados, rotulos, self.ROTULO_SEMIBREVE, d2), c='maroon', marker='.')
        #ax.scatter(self.getDadosRotulo(dados, rotulos, self.ROTULO_OUTROS, d1),self.getDadosRotulo(dados, rotulos, self.ROTULO_OUTROS, d2), c='orchid', marker='.')#
        plt.savefig(self.DIR_TREINAMENTO + 'distribuicao.png')



    def showDistribuicao(self, tipo):
          plt.ioff()
          if tipo == 'KNN_PCA':
              # Reduce dimension to 2 with PCA
              pca = make_pipeline(StandardScaler(),
                                  PCA(n_components=2, random_state=self.RANDOMSTATE))
              # Fit the method's model
              pca.fit(self.X_conjunto_treino, self.Y_conjunto_treino)
              # Fit a nearest neighbor classifier on the embedded training set
              self.KNN.fit(pca.transform(self.X_conjunto_treino), self.Y_conjunto_treino)

              # Compute the nearest neighbor accuracy on the embedded test set
              acc_knn = self.KNN.score(pca.transform(self.X_conjunto_teste), self.Y_conjunto_teste)

              # Embed the data set in 2 dimensions using the fitted model
              X_embedded = pca.transform(self.figuras_array_linha)
              # Plot the projected points and show the evaluation score
              plt.scatter(X_embedded[:, 0], X_embedded[:, 1], c=self.rotulos_classe, s=5, cmap='tab20c')
              plt.title("{}, KNN (k={})\nTeste Acuracia = {:.2f}".format("PCA",
                                                          self.K_VIZINHOS_PROXIMOS,
                                                          acc_knn))
              plt.legend()

              plt.savefig(self.DIR_TREINAMENTO + 'distribuicao.png')
          elif tipo == 'KNN_LDA':
              # Reduce dimension to 2 with LinearDiscriminantAnalysis
              lda = make_pipeline(StandardScaler(),
                                  LinearDiscriminantAnalysis(n_components=2))

              lda.fit(self.X_conjunto_treino, self.Y_conjunto_treino)
              self.KNN.fit(lda.transform(self.X_conjunto_treino), self.Y_conjunto_treino)

              # Compute the nearest neighbor accuracy on the embedded test set
              acc_knn = self.KNN.score(lda.transform(self.X_conjunto_teste), self.Y_conjunto_teste)

              # Embed the data set in 2 dimensions using the fitted model
              X_embedded = lda.transform(self.figuras_array_linha)
              # Plot the projected points and show the evaluation score
              plt.scatter(X_embedded[:, 0], X_embedded[:, 1], c=self.rotulos_classe, s=5, cmap='tab20c')
              plt.title("{}, KNN (k={})\nTeste Acuracia  = {:.2f}".format("LDA",
                                                                              self.K_VIZINHOS_PROXIMOS,
                                                                              acc_knn))
              plt.legend()
              plt.savefig(self.DIR_TREINAMENTO + 'distribuicao.png')
          elif tipo == 'KMEANS_CENTROIDS':
              # Initializing KMeans
              kmeans = KMeans(n_clusters=self.NR_CLUSTERS)
              kmeans = kmeans.fit(self.X_conjunto_treino, self.Y_conjunto_treino)
              labels = kmeans.predict(self.X_conjunto_treino)
              C = kmeans.cluster_centers_
              ax = Axes3D(fig)
              ax.legend()
              ax.scatter(C[:, 0], C[:, 1], C[:, 2], marker='*', c='#050505', s=1000)
              plt.savefig(self.DIR_TREINAMENTO + 'distribuicao.png')

          elif tipo == 'KMEANS':
               kmeans = KMeans(init='k-means++', n_clusters=self.NR_CLUSTERS, random_state=42)
               kmeans.fit(self.X_conjunto_treino, self.Y_conjunto_treino)
               size = len(self.X_conjunto_treino) -1
               X_iso = Isomap(n_neighbors=size).fit_transform(self.X_conjunto_treino, self.Y_conjunto_treino)
               clusters = kmeans.fit_predict(self.X_conjunto_treino)
               plt.scatter(X_iso[:, 0], X_iso[:, 1], s=30, c=clusters)
               plt.legend()
               plt.savefig(self.DIR_TREINAMENTO + 'distribuicao.png')

          elif tipo == 'PROPRIO':

               treinamentoRotulos = [
                       self.ROTULO_SEMINIMA,
                       self.ROTULO_MINIMA,
                       self.ROTULO_CLAVEDO,
                       self.ROTULO_CLAVESOL,
                       self.ROTULO_CLAVEFA,
                       self.ROTULO_PAUSACOLCHEIA,
                       self.ROTULO_PAUSASEMINIMA,
                       self.ROTULO_PAUSASEMICOLCHEIA,
                       self.ROTULO_PAUSASEMIBREVE,
                       self.ROTULO_PAUSAMINIMA,
                       self.ROTULO_BARRACOMPASSO,
                       self.ROTULO_SUSTENIDOS,
                       self.ROTULO_BEMOIS,
                       self.ROTULO_MARCACAO_TEMPO,
                       self.ROTULO_COLCHEIA,
                       self.ROTULO_SEMIBREVE,
                       self.ROTULO_OUTROS,
                       self.ROTULO_SEMICOLCHEIAS
               ]
               self.visualizaPontos(self.rotulos_classe, treinamentoRotulos)
          print('...........')


    def printf(*args):
        together = ''.join(map(str, args))  # avoid the arg is not str
        print(together)
        return together


    def isDataSourceCarregado(self):
        return self.data_source_carregado

    def classificar(self, img, K):
        img = img.resize((self.LARGURA, self.ALTURA))
        cinza = img.convert('L')  # converte para escala cinza
        cinza = cinza.point(lambda x: 0 if (x < 128) else 255, '1')  # binariza imagem
        elemento_teste = numpy.array(cinza, dtype='uint8').flatten()  # converte em array e achata imagem

        classes = ['Seminima', 'Minima', 'ClaveDo', 'ClaveSol', 'ClaveFa', 'PausaColcheia', 'PausaSeminima',
                   'PausaSemiColcheia', 'PausaSemiBreve', 'PausaMinima', 'BarraCompasso',
                   'Sustenido', 'Bemol', 'MarcacaoTempo', 'Colcheia', 'Semibreve', 'OutrosSimbolos', 'SemiColcheia']
        encontrado = self.KNN.predict([elemento_teste])
        return classes[encontrado[0]];

    def classificar_debug(self, K):
        retorno = []
        for index in range(0, 12):
            nome = str(index) + '.png';
            img = Image.open(self.caminho + '/../../notas/' + nome)  # Le a imagem a comparar
            img = img.resize((self.LARGURA, self.ALTURA))
            cinza = img.convert('L')  # converte para escala cinza
            cinza = cinza.point(lambda x: 0 if (x < 128) else 255, '1')  # binariza imagem
            elemento_teste = numpy.array(cinza, dtype='uint8').flatten()  # converte em array e achata imagem

            classes = ['Seminima', 'Minima', 'ClaveDo', 'ClaveSol', 'ClaveFa', 'PausaColcheia', 'PausaSeminima',
                       'PausaSemiColcheia', 'PausaSemiBreve', 'PausaMinima', 'BarraCompasso',
                       'Sustenido', 'Bemol', 'MarcacaoTempo', 'Colcheia', 'Semibreve', 'OutrosSimbolos', 'SemiColcheia']
            encontrado = self.KNN.predict([elemento_teste])

            mensagem = 'A nota ' + nome + ' parece com uma : ' + classes[encontrado[0]];
            retorno.append(mensagem)
            print(mensagem)
        return retorno

    def salvar_modelo(self, nome, tipo):
        s = self.DIR_TREINAMENTO + tipo + "_" + nome + '.pkl';
        joblib.dump(self.KNN, s)
        return s

    def salvar_objeto(self, o):
        s = self.DIR_TREINAMENTO + 'MUSCIMA.pkl';
        joblib.dump(o, s)
        return s

    def carregar_objeto(self):
        return joblib.load(self.DIR_TREINAMENTO + 'MUSCIMA.pkl')

    def plot_classification_report(self, cr, title='Classification report ', with_avg_total=False, cmap=plt.cm.Blues):

        lines = str(cr).split('\n')

        classes = []
        plotMat = []
        t = ''
        for line in lines[2: (len(lines) - 3)]:
            # print(line)
            t = line.split()
            # print(t)
            classes.append(t[0])
            v = [float(x) for x in t[1: len(t) - 1]]
            print(v)
            plotMat.append(v)

        if with_avg_total:
            aveTotal = lines[len(lines) - 1].split()
            classes.append('avg/total')
            vAveTotal = [float(x) for x in t[1:len(aveTotal) - 1]]
            plotMat.append(vAveTotal)

        plt.imshow(plotMat, interpolation='nearest', cmap=cmap)
        plt.title(title)
        plt.colorbar()
        x_tick_marks = numpy.arange(3)
        y_tick_marks = numpy.arange(len(classes))
        plt.xticks(x_tick_marks, ['precision', 'recall', 'f1-score'], rotation=45)
        plt.yticks(y_tick_marks, classes)
        plt.tight_layout()
        plt.ylabel('Classes')
        plt.xlabel('Measures')
        plt.savefig(self.DIR_TREINAMENTO + 'chart.png')

    def carregar_modelo(self, nome, tipo):
        self.KNN = joblib.load(nome)



    def loadDataSourceScoreReader(self):
        self.dataSourceScoreReader = joblib.load(self.DIR_TREINAMENTO + 'SCORE_READER_DATASOURCE.ds')
        return self.dataSourceScoreReader

    def saveDataSourceScoreReader(self):
        s = self.DIR_TREINAMENTO + 'SCORE_READER_DATASOURCE.ds';
        joblib.dump(self.dataSourceScoreReader, s)

    def salvarModeloAtual(self):
        nome = "Modelo ainda vazio"
        if self.NOME_MODELO != "" and self.TIPO_MODELO != "":
            nome = self.salvar_modelo(self.NOME_MODELO, self.TIPO_MODELO)
            print('Modelo: ' + nome + ' salvo.....')
        return nome





