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


class Classificador(object):
    # imprimir classe das notas
    def printClasses(self, cropobjects):
        _cropobj_dict = {c.objid: c for c in cropobjects}
        output = set()
        for x in cropobjects:
            output.add(x.clsname)

        for c in output:
            print(c)

    def extrair_figuras_modelo(self, modelo):
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

        notas_seminimas = [(n, s) for n, s in figuras if n.clsname == 'notehead-full']
        notas_minimas = [(n, s) for n, s in figuras if n.clsname == 'notehead-empty']
        return notas_minimas, notas_seminimas

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

    def carregar_data_source(self, caminho, data_source):
        try:
            print("DataSource :" + data_source)
            self.caminho = caminho
            self.CROPOBJECT_DIR = os.path.join(self.caminho, data_source)
            self.cropobject_fnames = [os.path.join(self.CROPOBJECT_DIR, f) for f in os.listdir(self.CROPOBJECT_DIR)]

            self.data_source = [parse_cropobject_list(f) for f in self.cropobject_fnames]
            self.minimas_e_seminimas = [self.extrair_figuras_modelo(self.modelo) for self.modelo in self.data_source]

            self.seminimas = list(itertools.chain(*[sm for sm, mn in self.minimas_e_seminimas]))
            self.minimas = list(itertools.chain(*[mn for sm, mn in self.minimas_e_seminimas]))

            self.img_seminimas = [self.extrair_imagem(sm) for sm in self.seminimas]
            self.img_minimas = [self.extrair_imagem(mn) for mn in self.minimas]

            self.img_red_seminimas = [resize(sm, (self.ALTURA, self.LARGURA)) for sm in self.img_seminimas]
            self.img_red_minimas = [resize(mn, (self.ALTURA, self.LARGURA)) for mn in self.img_minimas]

            # And re-binarize, to compensate for interpolation effects
            for self.sm in self.img_red_seminimas:
                self.sm[self.sm > 0] = 1
            for self.mn in self.img_red_minimas:
                self.mn[self.mn > 0] = 1

            # Randomly pick an equal number of quarter-notes.

            self.size = len(self.img_red_minimas)
            random.shuffle(self.img_red_seminimas)
            self.seminimas_selecionadas = self.img_red_seminimas[:self.size]

            self.rotulos_seminimas = [self.ROTULO_SEMINIMA for _ in self.seminimas_selecionadas]
            self.rotulos_minimas = [self.ROTULO_MINIMA for _ in self.img_red_minimas]

            self.misturadas = self.seminimas_selecionadas + self.img_red_minimas
            # converte imagem em matrix unidimencional
            self.figuras_array_linha = [nota.flatten() for nota in self.misturadas]
            self.rotulos_classe = self.rotulos_minimas + self.rotulos_seminimas

            self.X_conjunto_treino, self.X_conjunto_teste, self.Y_conjunto_treino, self.Y_conjunto_teste = train_test_split(
                self.figuras_array_linha, self.rotulos_classe, test_size=0.25, random_state=42,
                stratify=self.rotulos_classe)

            self.load_knn()

            self.data_source_carregado = True
        except:
            print("Erro ao carregar DataSource")
            self.data_source_carregado = False


    def load_knn(self):
        self.KNN = KNeighborsClassifier(n_neighbors=self.K_VIZINHOS_PROXIMOS)
        self.KNN.fit(self.X_conjunto_treino, self.Y_conjunto_treino)

    def isDataSourceCarregado(self):
        return self.data_source_carregado

    def classificar(self, img, K):

        if self.K_VIZINHOS_PROXIMOS != K:
            self.K_VIZINHOS_PROXIMOS = K
            self.load_knn()

        img = img.resize((self.LARGURA, self.ALTURA))
        cinza = img.convert('L')  # converte para escala cinza
        cinza = cinza.point(lambda x: 0 if (x < 128) else 255, '1')  # binariza imagem
        elemento_teste = numpy.array(cinza, dtype='uint8').flatten()  # converte em array e achata imagem

        classes = ['Minima', 'Seminima']
        encontrado = self.KNN.predict([elemento_teste])
        return classes[encontrado[0]];

    def classificar_debug(self, K):
        if self.K_VIZINHOS_PROXIMOS != K:
            self.K_VIZINHOS_PROXIMOS = K
            self.load_knn()

        retorno = []
        for index in range(0, 12):
            nome = str(index) + '.png';
            img = Image.open(self.caminho + '/../../notas/' + nome)  # Le a imagem a comparar
            img = img.resize((self.LARGURA, self.ALTURA))
            cinza = img.convert('L')  # converte para escala cinza
            cinza = cinza.point(lambda x: 0 if (x < 128) else 255, '1')  # binariza imagem
            elemento_teste = numpy.array(cinza, dtype='uint8').flatten()  # converte em array e achata imagem

            classes = ['Minima', 'Seminima']
            encontrado = self.KNN.predict([elemento_teste])
            mensagem = 'A nota ' + nome + ' parece com uma : ' + classes[encontrado[0]];
            retorno.append(mensagem)
            print(mensagem)
        return retorno

    def __init__(self):
          self.data_source_carregado = False
          self.LARGURA = 10  # Largura da figura
          self.ALTURA = 40  # altura da figura
          self.K_VIZINHOS_PROXIMOS = 5;  # numero de vizinhos proximos a verificar no KNN

          self.ROTULO_MINIMA = 1
          self.ROTULO_SEMINIMA = 0

