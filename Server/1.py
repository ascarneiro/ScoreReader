import os
import itertools
import numpy
import matplotlib.pyplot as plt
from skimage.transform import resize
import random
from sklearn.model_selection import train_test_split
from sklearn.neighbors import KNeighborsClassifier
from PIL import Image
from muscima.io import parse_cropobject_list #DataSource



#imprimir classe das notas
def printClasses(cropobjects):
    _cropobj_dict = {c.objid: c for c in cropobjects}
    output = set()
    for x in cropobjects:
        output.add(x.clsname)

    for c in output:
        print(c)


def extrair_figuras_modelo(modelo):
    _modelo_dict = {elem.objid: elem for elem in modelo}
    figuras = []
    for c in modelo:
        if (c.clsname == 'notehead-full') or\
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


def extrair_imagem(elementos, margin=1):

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
        canvas[_pt:_pt+c.height, _pl:_pl+c.width] += c.mask

    canvas[canvas > 0] = 1
    return canvas


def plotar_imagem(mask):
    plt.imshow(mask, cmap='gray', interpolation='nearest')
    plt.show()

def plotar_imagens(masks, row_length=5):
    n_masks = len(masks)
    n_rows = n_masks // row_length + 1
    n_cols = min(n_masks, row_length)
    fig = plt.figure()
    for i, mask in enumerate(masks):
        plt.subplot(n_rows, n_cols, i+1)
        plt.imshow(mask, cmap='gray', interpolation='nearest')
    # Let's remove the axis labels, they clutter the image.
    for ax in fig.axes:
        ax.set_yticklabels([])
        ax.set_xticklabels([])
        ax.set_yticks([])
        ax.set_xticks([])
    plt.show()


LARGURA = 10 #Largura da figura
ALTURA = 40 # altura da figura
K = 5; #numero de vizinhos proximos a verificar no KNN




caminho = 'C:/Users/ascarneiro/Desktop/TCC/ScoreReader/Server/MUSCIMA/'
CROPOBJECT_DIR = os.path.join(caminho, 'data/cropobjects_manual')

cropobject_fnames = [os.path.join(CROPOBJECT_DIR, f) for f in os.listdir(CROPOBJECT_DIR)]
data_source = [parse_cropobject_list(f) for f in cropobject_fnames]
minimas_e_seminimas = [extrair_figuras_modelo(modelo) for modelo in data_source]

seminimas = list(itertools.chain(*[sm for sm, mn in minimas_e_seminimas]))
minimas = list(itertools.chain(*[mn for sm, mn in minimas_e_seminimas]))

img_seminimas = [extrair_imagem(sm) for sm in seminimas]
img_minimas = [extrair_imagem(mn) for mn in minimas]


img_red_seminimas = [resize(sm, (ALTURA, LARGURA)) for sm in img_seminimas]
img_red_minimas = [resize(mn, (ALTURA, LARGURA)) for mn in img_minimas]

# And re-binarize, to compensate for interpolation effects
for sm in img_red_seminimas:
    sm[sm > 0] = 1
for mn in img_red_minimas:
    mn[mn > 0] = 1


# Randomly pick an equal number of quarter-notes.

size  = len(img_red_minimas)
random.shuffle(img_red_seminimas)
seminimas_selecionadas = img_red_seminimas[:size]

rotulos_seminimas = [ROTULO_SEMINIMA for _ in seminimas_selecionadas]
rotulos_minimas = [ROTULO_MINIMA for _ in img_red_minimas]

misturadas = seminimas_selecionadas + img_red_minimas
#converte imagem em matrix unidimencional
figuras_array_linha = [nota.flatten() for nota in misturadas]
rotulos_classe = rotulos_minimas + rotulos_seminimas


X_conjunto_treino, X_conjunto_teste, Y_conjunto_treino, Y_conjunto_teste = train_test_split(
    figuras_array_linha, rotulos_classe, test_size=0.25, random_state=42,
    stratify=rotulos_classe)


K_VIZINHOS_PROXIMOS=K

for index in range(0, 12):
    KNN = KNeighborsClassifier(n_neighbors=K_VIZINHOS_PROXIMOS)
    KNN.fit(X_conjunto_treino, Y_conjunto_treino)
    nome = str(index) + '.png';
    img = Image.open(caminho + '/../../notas/' + nome) #Le a imagem a comparar
    img = img.resize((LARGURA, ALTURA))
    cinza = img.convert('L')  #converte para escala cinza
    cinza = cinza.point(lambda x: 0 if (x < 128) else 255, '1')  #binariza imagem
    elemento_teste = numpy.array(cinza,  dtype='uint8').flatten() #converte em array e achata imagem

    classes = ['Minima', 'Seminima']
    encontrado = KNN.predict([elemento_teste])
    print('A nota ' + nome + ' parece com uma : ' + classes[encontrado[0]])
