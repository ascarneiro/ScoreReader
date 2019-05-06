import os
import itertools
import random
import numpy
import matplotlib.pyplot as plt
from skimage.transform import resize
from sklearn.model_selection import train_test_split
from sklearn.neighbors import KNeighborsClassifier
from sklearn.metrics import classification_report
from muscima.io import parse_cropobject_list

class Classificador(object):  
  
  # Bear in mind that the outlinks are integers, only valid within the same document.
  # Therefore, we define a function per-document, not per-dataset.
  def extrairNotas(self, cropobjects):
    """Finds all ``(full-notehead, stem)`` pairs that form
    quarter or half notes. Returns two lists of CropObject tuples:
    one for quarter notes, one of half notes.

    :returns: quarter_notes, half_notes
    """
    _cropobj_dict = {c.objid: c for c in cropobjects}
    notes = []
    for c in cropobjects:
        if (c.clsname == 'notehead-full') or (c.clsname == 'notehead-empty'):
            _has_stem = False
            _has_beam_or_flag = False
            stem_obj = None
            for o in c.outlinks:
                _o_obj = _cropobj_dict[o]
                if _o_obj.clsname == 'stem':
                    _has_stem = True
                    stem_obj = _o_obj
                elif _o_obj.clsname == 'beam':
                    _has_beam_or_flag = True
                elif _o_obj.clsname.endswith('flag'):
                    _has_beam_or_flag = True
            if _has_stem and (not _has_beam_or_flag):
                # We also need to check against quarter-note chords.
                # Stems only have inlinks from noteheads, so checking
                # for multiple inlinks will do the trick.
                if len(stem_obj.inlinks) == 1:
                    notes.append((c, stem_obj))

    quarter_notes = [(n, s) for n, s in notes if n.clsname == 'notehead-full']
    half_notes = [(n, s) for n, s in notes if n.clsname == 'notehead-empty']
    return quarter_notes, half_notes

  def addNotesOnCanvasMatrix(self, cropobjects, margin=1):
    """Paste the cropobjects' mask onto a shared canvas.
    There will be a given margin of background on the edges."""

    # Get the bounding box into which all the objects fit
    top = min([c.top for c in cropobjects])
    left = min([c.left for c in cropobjects])
    bottom = max([c.bottom for c in cropobjects])
    right = max([c.right for c in cropobjects])

    # Create the canvas onto which the masks will be pasted
    height = bottom - top + 2 * margin
    width = right - left + 2 * margin
    canvas = numpy.zeros((height, width), dtype='uint8')

    for c in cropobjects:
        # Get coordinates of upper left corner of the CropObject
        # relative to the canvas
        _pt = c.top - top + margin
        _pl = c.left - left + margin
        # We have to add the mask, so as not to overwrite
        # previous nonzeros when symbol bounding boxes overlap.
        canvas[_pt:_pt+c.height, _pl:_pl+c.width] += c.mask

    canvas[canvas > 0] = 1
    return canvas

  def resizeImages40X10AndBinarize(self):
    self.qn_resized = [resize(self.qn, (40, 10)) for self.qn in self.qn_images]
    self.hn_resized = [resize(self.hn, (40, 10)) for self.hn in self.hn_images]

    # And re-binarize, to compensate for interpolation effects
    for self.qn in self.qn_resized:
        self.qn[self.qn > 0] = 1
    for self.hn in self.hn_resized:
        self.hn[self.hn > 0] = 1

  def train(self):
    # Randomly pick an equal number of quarter-notes.
    self.n_hn = len(self.hn_resized)
    random.shuffle(self.qn_resized)
    self.qn_selected = self.qn_resized[:self.n_hn]
    self.qn_labels = [self.Q_LABEL for _ in self.qn_selected]
    self.hn_labels = [self.H_LABEL for _ in self.hn_resized]


    self.notes = self.qn_selected + self.hn_resized
    # Flatten data
    self.notes_flattened = [n.flatten() for n in self.notes]
    self.labels = self.qn_labels + self.hn_labels

    #25% corresponde ao tamanho do teste
    self.X_train, self.X_test, self.y_train, self.y_test = train_test_split(
                self.notes_flattened,
                self.labels,
                test_size=0.25,
                random_state=42,
               stratify=self.labels
               )
    self.K=5
    
    
  #Algoritmo de classificacao
  def classifyTest(self):
    # Trying the defaults first.
    clf = KNeighborsClassifier(n_neighbors=self.K)
    clf.fit(self.X_train, self.y_train)

    KNeighborsClassifier(
          algorithm='auto',
          leaf_size=30,
          metric='minkowski',
          metric_params=None,
          n_jobs=1,
          n_neighbors=5, p=2,
          weights='uniform'
          )
    self.y_test_pred = clf.predict(self.X_test)
    print(classification_report(self.y_test, self.y_test_pred, target_names=['half', 'quarter']))


  def load(self):
    #Extrai notas do DataSet
    qns_and_hns = [self.extrairNotas(self.cropobjects) for self.cropobjects in self.docs]

    #Some operation
    self.qns = list(itertools.chain(*[self.qn for self.qn, self.hn in qns_and_hns]))
    self.hns = list(itertools.chain(*[self.hn for self.qn, self.hn in qns_and_hns]))
  
    print(len(self.qns))
    print(len(self.hns))

    #Coloca imagem no canvas
    self.qn_images = [self.addNotesOnCanvasMatrix(self.qn) for self.qn in self.qns]
    self.hn_images = [self.addNotesOnCanvasMatrix(self.hn) for self.hn in self.hns]

    #Redimenciona em 40 X 10 e transforma em matriz de 0 e 1
    self.resizeImages40X10AndBinarize()

    #Treina modelo
    self.train()

    #Classifica usando os dados do proprio modelo
    self.classifyTest()

    def classify(self, mask):
      print(mask)

    #Construtor
    def __init__(self):
      self.caminho = 'C:/Users/ascarneiro/Desktop/TCC/ScoreReader/Server/MUSCIMA/'  
      
      self.CROPOBJECT_DIR = os.path.join(self.caminho, 'data/cropobjects_manual')
      self.cropobject_fnames = [os.path.join(self.CROPOBJECT_DIR, f) for f in os.listdir(self.CROPOBJECT_DIR)]
      self.docs = [parse_cropobject_list(f) for f in self.cropobject_fnames]
      self.Q_LABEL = 1
      self.H_LABEL = 0
      
      #Load everything.......
      load()

