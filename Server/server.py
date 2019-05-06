import cherrypy
import cv2
import glob
import numpy as np
import matplotlib.pyplot as plt
import matplotlib.image as mpimg
import base64
import io
import cStringIO
import pickle

from PIL import Image as pil_image
from gamera.core import *
from gamera.plugins import pil_io
from gamera.plugins import numpy_io
from gamera.toolkits.musicstaves import musicstaves_rl_simple
from numpy import fft


class Server(object):  
  def __init__(self):
    init_gamera()
    self.DIR = 'C:/Users/ascarneiro/Desktop/TCC/ScoreReader/repository/'
    self.array = None
    self.debug = False

  def encodeImageStr(self, image):
    nparr = image.to_numpy()
    fourarr = fft.fft2(nparr)
    img = pil_image.fromarray(nparr)

    buffer = cStringIO.StringIO()
    img.save(buffer, format="PNG")
    imageEncoded = base64.b64encode(buffer.getvalue())
    if self.debug:
        print(imageEncoded)

  def convertToCvImage(self, imageEncoded):
    base64DecodedImage = base64.b64decode(str(imageEncoded))
    image = pil_image.open(io.BytesIO(base64DecodedImage))
    cvImage = cv2.cvtColor(np.array(image), cv2.COLOR_BGR2RGB)
    return cvImage


  def convertToPilImage(self, imageEncoded):
    cvImage = self.convertToCvImage(imageEncoded)
    pilImage = pil_image.fromarray(cvImage)
    return pilImage


  def convertToGameraImage(self, pilImage):
    gameraImage = pil_io.from_pil(pilImage)
    return gameraImage

  

  @cherrypy.expose
  def removerLinhasDaPauta(self, imageEncoded):
    pilImage = self.convertToPilImage(imageEncoded)
    gameraImage = self.convertToGameraImage(pilImage)
    ms = musicstaves_rl_simple.MusicStaves_rl_simple(gameraImage)
    ms.remove_staves(crossing_symbols = 'bars')

    staves = ms.get_staffpos()
    for staff in staves:
        print "Staff %d has %d staves:" % (staff.staffno, len(staff.yposlist))
        for index, y in enumerate(staff.yposlist):
            print "    %d. line at y-position:" % (index+1), y

    ms.image.image_save(self.DIR + 'staffless.png', 'PNG')
    imageEncoded = self.encodeImageStr(ms.image)
    if self.debug:
        print(imageEncoded)

    return imageEncoded
  
  def binarizeAndRotateImageMatrix(self, cvImage):
    #Tom de cinza
    ret, thresh_img = cv2.threshold(cvImage, 127, 255, cv2.THRESH_BINARY)
    binarized = np.array(thresh_img)

    if self.debug:
        img = pil_image.fromarray(self.array)
        img.show()

    #converte valores > 0 para1 caso for 0 entao 0
    binarized = np.where(binarized > 0, 1, 0)
    unidm = np.concatenate(binarized, axis=1)

    #Inverte matriz, devido imagem invertida
    unidm = unidm[::-1]

    #rotaciona a matriz 270 graus para imagem ficar em "Pe"
    unidm =  np.rot90(unidm)
    unidm =  np.rot90(unidm)
    unidm = np.rot90(unidm)
    return unidm

  @cherrypy.expose
  def classificar(self, imageEncoded):
    pilImage = self.convertToPilImage(imageEncoded)
    gameraImage = self.convertToGameraImage(pilImage)
    nestedlist = gameraImage.to_nested_list()

    cvImage = self.convertToCvImage(imageEncoded)

    #Converte imagem em sequencia de 0 e 1
    binarized = self.binarizeAndRotateImageMatrix(cvImage) 
    self.array = binarized  
    return np.array_str(binarized)


  @cherrypy.expose
  def plotImage(self):
    np.savetxt(self.DIR + 'arrayImageBinary.txt', self.array, delimiter=' ', newline='\n', fmt='%i')  
cherrypy.quickstart(Server())
