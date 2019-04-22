import cherrypy
import cv2
import glob
import numpy as np
import MuscimaFunctions as mf
import matplotlib.pyplot as plt
import matplotlib.image as mpimg
import base64
import io
from PIL import Image


class Server(object):  
  def __init__(self):
      self.m = mf.MuscimaFunctions()
      self.indexImage = 0;

  @cherrypy.expose
  def main(self):
    self.m.main()

  @cherrypy.expose
  def analiseImage(self, width, height, image64):
    imgdata = base64.b64decode(str(image64))
    image = Image.open(io.BytesIO(imgdata))
    converted = cv2.cvtColor(np.array(image), cv2.COLOR_BGR2RGB)
    cv2.imwrite('C:/tcc/ScoreReader/WSImages/loco.jpg',  converted) 
    return "Sucess!"
    
  @cherrypy.expose
  def imageFromDir(self):
    images = []
    files = glob.glob ("C:/TCC/ScoreReader/img/*.jpg")
    for file in files:
        print(file)
        image = cv2.imread (file)
        images.append (image)
        img = mpimg.imread(image)
        imgplot = plt.imshow(img)
        plt.show()
        #print('images shape:', np.array(images).shape)


cherrypy.quickstart(Server())