import os
import itertools
import numpy
import matplotlib.pyplot as plt
from skimage.transform import resize
import random
from sklearn.model_selection import train_test_split
from sklearn.neighbors import KNeighborsClassifier
from PIL import Image
from muscima.io import parse_cropobject_list
from sklearn.externals import joblib
from sklearn.neighbors import kneighbors_graph
from sklearn.metrics import classification_report


class Classificador(object):
    # imprimir classe das notas
    def printClasses(self, cropobjects):
        _cropobj_dict = {c.objid: c for c in cropobjects}
        output = set()
        for x in cropobjects:
            output.add(x.clsname)

        for c in output:
            print(c)

    def extrair_elementos(self, modelo):

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
                        figuras.append((c, objeto_com_haste))
            else:
                for o in c.outlinks:
                    if c.clsname == 'tie':
                        figuras.append((c, _modelo_dict[o]))
                    elif c.clsname == 'g-clef':
                        figuras.append((c, _modelo_dict[o]))
                    elif c.clsname == 'f-clef':
                        figuras.append((c, _modelo_dict[o]))
                    elif c.clsname == 'c-clef':
                        figuras.append((c, _modelo_dict[o]))
                    elif c.clsname == '8th_rest':
                        figuras.append((c, _modelo_dict[o]))
                    elif c.clsname == '16th_rest':
                        figuras.append((c, _modelo_dict[o]))
                    elif c.clsname == 'quarter_rest':
                        figuras.append((c, _modelo_dict[o]))
                    elif c.clsname == 'half_rest':
                        figuras.append((c, _modelo_dict[o]))
                    elif c.clsname == 'whole_rest':
                        figuras.append((c, _modelo_dict[o]))
                    elif c.clsname == 'thin_barline':
                        figuras.append((c, _modelo_dict[o]))

        minimas = [(n, s) for n, s in figuras if n.clsname == 'notehead-full']
        seminimas = [(n, s) for n, s in figuras if n.clsname == 'notehead-empty']
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

        return minimas, seminimas, ligadura, clavesol, clavefa,clavedo,\
               pausasemicolcheia,pausacolcheia, pausaseminima, pausaminima, \
               pausasemibreve,barracompasso


    def extrair_imagem(self, elementos, margin=1):

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

    def treinar_knn_padrao(self, nome, caminho, data_source):
        #try:
            print("DataSource :" + data_source)
            self.caminho = caminho
            self.CROPOBJECT_DIR = os.path.join(self.caminho, data_source)
            self.cropobject_fnames = [os.path.join(self.CROPOBJECT_DIR, f) for f in os.listdir(self.CROPOBJECT_DIR)]

            self.data_source = [parse_cropobject_list(f) for f in self.cropobject_fnames]

            #self.f = [self.printClasses(self.modelo) for self.modelo in self.data_source]
            self.figuras = [self.extrair_elementos(self.modelo) for self.modelo in self.data_source]



            self.treinar(self.figuras)


            print("Salvando Modelo...")
            nome = self.salvar_modelo(nome)
            print("Modelo salvo...")
            print("sucesso...")
            return nome
        #except:
         #   print("Erro ao carregar DataSource")
          #  self.data_source_carregado = False
          #  return "erro"

    def treinar(self, figuras):
        self.seminimas = list(itertools.chain(*[s for m, s, l, g, f, c, ps, pc, psm, pm, psb, bc in figuras]))
        self.minimas = list(itertools.chain(*[m for m, s, l, g, f, c, ps, pc, psm, pm, psb, bc in figuras]))
        self.ligaduras = list(itertools.chain(*[l for m, s, l, g, f, c, ps, pc, psm, pm, psb, bc in figuras]))
        self.clavesol = list(itertools.chain(*[g for m, s, l, g, f, c, ps, pc, psm, pm, psb, bc in figuras]))
        self.clavefa = list(itertools.chain(*[f for m, s, l, g, f, c, ps, pc, psm, pm, psb, bc in figuras]))
        self.clavedo = list(itertools.chain(*[c for m, s, l, g, f, c, ps, pc, psm, pm, psb, bc in figuras]))
        self.pausaseminima = list(itertools.chain(*[ps for m, s, l, g, f, c, ps, pc, psm, pm, psb, bc in figuras]))
        self.pausacolcheia = list(itertools.chain(*[pc for m, s, l, g, f, c, ps, pc, psm, pm, psb, bc in figuras]))
        self.pausasemicolcheia = list(itertools.chain(*[psm for m, s, l, g, f, c, ps, pc, psm, pm, psb, bc in figuras]))
        self.pausaminima = list(itertools.chain(*[pm for m, s, l, g, f, c, ps, pc, psm, pm, psb, bc in figuras]))
        self.pausasemibreve = list(itertools.chain(*[psb for m, s, l, g, f, c, ps, pc, psm, pm, psb, bc in figuras]))
        self.barracompasso = list(itertools.chain(*[bc for m, s, l, g, f, c, ps, pc, psm, pm, psb, bc in figuras]))

        self.img_seminimas = [self.extrair_imagem(s) for s in self.seminimas]
        self.img_minimas = [self.extrair_imagem(m) for m in self.minimas]
        self.img_ligaduras = [self.extrair_imagem(l) for l in self.ligaduras]
        self.img_clavesol = [self.extrair_imagem(g) for g in self.clavesol]
        self.img_clavefa = [self.extrair_imagem(f) for f in self.clavefa]
        self.img_clavedo = [self.extrair_imagem(c) for c in self.clavedo]
        self.img_pausaseminima = [self.extrair_imagem(ps) for ps in self.pausaseminima]
        self.img_pausacolcheia = [self.extrair_imagem(pc) for pc in self.pausacolcheia]
        self.img_pausasemicolcheia = [self.extrair_imagem(psm) for psm in self.pausasemicolcheia]
        self.img_pausaminima = [self.extrair_imagem(pm) for pm in self.pausaminima]
        self.img_pausasemibreve = [self.extrair_imagem(psb) for psb in self.pausasemibreve]
        self.img_barracompasso = [self.extrair_imagem(bc) for bc in self.barracompasso]

        self.img_seminimas = [resize(sm, (self.ALTURA, self.LARGURA)) for sm in self.img_seminimas]
        self.img_minimas = [resize(mn, (self.ALTURA, self.LARGURA)) for mn in self.img_minimas]
        self.img_ligaduras = [resize(mn, (self.ALTURA, self.LARGURA)) for mn in self.img_ligaduras]
        self.img_clavesol = [resize(mn, (self.ALTURA, self.LARGURA)) for mn in self.img_clavesol]
        self.img_clavefa = [resize(mn, (self.ALTURA, self.LARGURA)) for mn in self.img_clavefa]
        self.img_clavedo = [resize(mn, (self.ALTURA, self.LARGURA)) for mn in self.img_clavedo]
        self.img_pausaseminima = [resize(mn, (self.ALTURA, self.LARGURA)) for mn in self.img_pausaseminima]
        self.img_pausacolcheia = [resize(mn, (self.ALTURA, self.LARGURA)) for mn in self.img_pausacolcheia]
        self.img_pausasemicolcheia = [resize(mn, (self.ALTURA, self.LARGURA)) for mn in self.img_pausasemicolcheia]
        self.img_pausaminima = [resize(mn, (self.ALTURA, self.LARGURA)) for mn in self.img_pausaminima]
        self.img_pausasemibreve = [resize(mn, (self.ALTURA, self.LARGURA)) for mn in self.img_pausasemibreve]
        self.img_barracompasso = [resize(mn, (self.ALTURA, self.LARGURA)) for mn in self.img_barracompasso]


        # And re-binarize, to compensate for interpolation effects
        for self.im in self.img_seminimas:
            self.im[self.im > 0] = 1
        for self.im in self.img_minimas:
            self.im[self.im > 0] = 1
        for self.im in self.img_ligaduras:
            self.im[self.im > 0] = 1
        for self.im in self.img_clavesol:
            self.im[self.im > 0] = 1
        for self.im in self.img_clavefa:
            self.im[self.im > 0] = 1
        for self.im in self.img_clavedo:
            self.im[self.im > 0] = 1
        for self.im in self.img_pausaseminima:
            self.im[self.im > 0] = 1
        for self.im in self.img_pausacolcheia:
            self.im[self.im > 0] = 1
        for self.im in self.img_pausasemicolcheia:
            self.im[self.im > 0] = 1
        for self.im in self.img_pausaminima:
            self.im[self.im > 0] = 1
        for self.im in self.img_pausasemibreve:
            self.im[self.im > 0] = 1
        for self.im in self.img_barracompasso:
            self.im[self.im > 0] = 1

        self.rotulos_seminimas = [self.ROTULO_SEMINIMA for _ in self.img_seminimas]
        self.rotulos_minimas = [self.ROTULO_MINIMA for _ in self.img_minimas]
        self.rotulos_ligaduras = [self.ROTULO_LIGADURA for _ in self.img_ligaduras]
        self.rotulos_clavesol = [self.ROTULO_CLAVESOL for _ in self.img_clavesol]
        self.rotulos_clavefa = [self.ROTULO_CLAVEFA for _ in self.img_clavefa]
        self.rotulos_clavedo = [self.ROTULO_CLAVEDO for _ in self.img_clavedo]
        self.rotulos_pausaseminima = [self.ROTULO_PAUSASEMINIMA for _ in self.img_pausaseminima]
        self.rotulos_pausacolcheia = [self.ROTULO_PAUSACOLCHEIA for _ in self.img_pausacolcheia]
        self.rotulos_pausasemicolcheia = [self.ROTULO_PAUSASEMICOLCHEIA for _ in self.img_pausasemicolcheia]
        self.rotulos_pausaminima = [self.ROTULO_PAUSAMINIMA for _ in self.img_pausaminima]
        self.rotulos_pausasemibreve = [self.ROTULO_PAUSASEMIBREVE for _ in self.img_pausasemibreve]
        self.rotulos_barracompasso = [self.ROTULO_BARRACOMPASSO for _ in self.img_barracompasso]


        self.misturadas = self.img_seminimas +\
                          self.img_minimas + \
                          self.img_ligaduras + \
                          self.img_clavesol + \
                          self.img_clavefa + \
                          self.img_clavedo + \
                          self.img_pausaminima + \
                          self.img_pausacolcheia + \
                          self.img_pausasemicolcheia + \
                          self.img_pausaseminima + \
                          self.img_pausasemibreve + \
                          self.img_barracompasso

            # converte imagem em matrix unidimencional
        self.figuras_array_linha = [n.flatten() for n in self.misturadas]
        self.rotulos_classe = self.rotulos_seminimas + \
                              self.rotulos_minimas + \
                              self.rotulos_ligaduras + \
                              self.rotulos_clavesol + \
                              self.rotulos_clavefa + \
                              self.rotulos_clavedo + \
                              self.rotulos_pausaseminima + \
                              self.rotulos_pausacolcheia + \
                              self.rotulos_pausasemicolcheia + \
                              self.rotulos_pausaminima + \
                              self.rotulos_pausasemibreve + \
                              self.rotulos_barracompasso

        self.X_conjunto_treino, self.X_conjunto_teste, self.Y_conjunto_treino, self.Y_conjunto_teste = train_test_split(
            self.figuras_array_linha, self.rotulos_classe, test_size=0.25, random_state=42,
            stratify=self.rotulos_classe)

        self.KNN = KNeighborsClassifier(n_neighbors=self.K_VIZINHOS_PROXIMOS)
        self.KNN.fit(self.X_conjunto_treino, self.Y_conjunto_treino)


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

        classes = ['Minima', 'Seminima', 'ClaveDo', 'ClaveFa', 'ClaveSol', 'Ligadura', 'PausaSeminima',
                   'PausaColcheia', 'PausaSemiColcheia', 'PausaSemiBreve', 'PausaMinima', 'BarraCompasso']
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

            classes = ['Minima', 'Seminima', 'ClaveDo', 'ClaveFa', 'ClaveSol', 'Ligadura', 'PausaSeminima',
                       'PausaColcheia', 'PausaSemiColcheia', 'PausaSemiBreve', 'PausaMinima', 'BarraCompasso']
            encontrado = self.KNN.predict([elemento_teste])
            mensagem = 'A nota ' + nome + ' parece com uma : ' + classes[encontrado[0]];
            retorno.append(mensagem)
            print(mensagem)
        return retorno

    def salvar_modelo(self, nome):
        s = self.DIR_TREINAMENTO + nome + '.pkl';
        joblib.dump(self.KNN, s)
        return s

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

    def carregar_modelo(self, nome):
        self.KNN = joblib.load(self.DIR_TREINAMENTO + nome + '.pkl')

    def treinar_customizado(self,
                           QT_SEMIBREVE,
                           QT_MINIMA,
                           QT_SEMINIMA,
                           QT_COLCHEIA,
                           QT_SEMICOLCHEIA,
                           QT_FUSA,
                           QT_SEMIFUSA,
                           QT_CLAVESOL,
                           QT_CLAVEFA,
                           QT_CLAVEDO,
                           QT_FERMATA,
                           QT_LIGADURA,
                           PAUSA_SEMIBREVE,
                           PAUSA_MINIMA,
                           PAUSA_SEMINIMA,
                           PAUSA_COLCHEIA,
                           PAUSA_SEMICOLCHEIA,
                           PAUSA_FUSA,
                           PAUSA_SEMIFUSA):
        print('')





    def __init__(self):
          self.DIR_TREINAMENTO = 'C:/Users/ascarneiro/Desktop/TCC/ScoreReader/treinamento/'
          self.data_source_carregado = False
          self.LARGURA = 10  # Largura da figura
          self.ALTURA = 40  # altura da figura
          self.K_VIZINHOS_PROXIMOS = 5;  # numero de vizinhos proximos a verificar no KNN

          self.ROTULO_SEMINIMA = 0
          self.ROTULO_MINIMA = 1
          self.ROTULO_CLAVEDO = 2
          self.ROTULO_CLAVESOL = 3
          self.ROTULO_CLAVEFA = 4
          self.ROTULO_LIGADURA = 5
          self.ROTULO_PAUSASEMINIMA = 6
          self.ROTULO_PAUSACOLCHEIA = 7
          self.ROTULO_PAUSASEMICOLCHEIA = 8
          self.ROTULO_PAUSASEMIBREVE = 9
          self.ROTULO_PAUSAMINIMA = 10
          self.ROTULO_BARRACOMPASSO = 11




