import cherrypy
import cv2
import glob
import numpy as np
import MuscimaFunctions as mf
import matplotlib.pyplot as plt
import matplotlib.image as mpimg

class Server(object):  
  def __init__(self):
      self.m = mf.MuscimaFunctions()

  @cherrypy.expose
  def main(self):
    self.m.main()

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