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
import scripts as scr

"""
caminho = 'C:/Users/ascarneiro/Desktop/TCC/ScoreReader/Server/MUSCIMA/'  

CROPOBJECT_DIR = os.path.join(caminho, 'data/cropobjects_manual')
cropobject_fnames = [os.path.join(CROPOBJECT_DIR, f) for f in os.listdir(CROPOBJECT_DIR)]
docs = [parse_cropobject_list(f) for f in cropobject_fnames]

"""
scr.build_argument_parser()



