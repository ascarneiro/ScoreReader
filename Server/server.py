import cherrypy
import cv2
import numpy as np
import base64
import io
import cStringIO
import classificador as clf
import identificadornotas as utils
import parametros as params
import json
from PIL import Image as pil_image
from gamera.core import *
from gamera.plugins import pil_io
from gamera.toolkits.musicstaves import musicstaves_rl_simple
from numpy import fft


class Server(object):
  def __init__(self):
    self.loaddingDataSource = False;
    init_gamera()
    self.data_carregado = False
    self.DIR = 'C:/Users/ascarneiro/Desktop/TCC/ScoreReader/repository/'
    self.array = None
    self.debug = True
    self.classificador = clf.Classificador()

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
    ms.remove_staves(crossing_symbols = 'bars', num_lines=5)

    #Printar quantas pautas tem
    if self.debug:
      pautas = ms.get_staffpos()
      for pauta in pautas:
          print "Staff %d has %d staves:" % (pauta.staffno, len(pauta.yposlist))
          for index, y in enumerate(pauta.yposlist):
              print "    %d. line at y-position:" % (index+1), y

    imageEncoded = self.encodeImageStr(ms.image)
    ms.image.image_save(self.DIR + 'staffless.png', 'PNG')

    if self.debug:
        print(imageEncoded)

    return imageEncoded

  @cherrypy.expose
  def obterInformacoesPautas(self, imageEncoded):
    retorno = []
    pilImage = self.convertToPilImage(imageEncoded)
    gameraImage = self.convertToGameraImage(pilImage)
    ms = musicstaves_rl_simple.MusicStaves_rl_simple(gameraImage)
    ms.remove_staves(crossing_symbols='bars', num_lines=5)
    pautas = ms.get_staffpos()


    cv2.imwrite(self.DIR + 'posicaoLinhas.png', self.convertToCvImage(imageEncoded))
    img = cv2.imread(self.DIR + 'posicaoLinhas.png')

    for pauta in pautas:
      linhas = []
      for index, y in enumerate(pauta.yposlist):
        linhas.append({"linha": {"index": (index + 1), "y": y}})
        cv2.putText(img, "y(" + str(y) +")", (520, int(y)), cv2.FONT_HERSHEY_TRIPLEX,0.4, (0, 0, 255  ), 1, cv2.LINE_AA)

      retorno.append({"pauta": {"index": pauta.staffno, "linhas":linhas, "yposlist": pauta.yposlist}})

    cv2.imwrite(self.DIR + 'posicaoLinhas.png', img)

    return json.dumps(retorno)

  @cherrypy.expose
  def detectarAlturaNotas(self, imageEncoded):

    image = self.convertToCvImage(imageEncoded)
    identificaNotas = utils.IdentificaNotas()
    return json.dumps(identificaNotas.detectarPontosNotas(image))


  def doVerificaCarregouDataSource(self):
    status = "Aguarde carregando dataSource.."
    while not self.classificador.isDataSourceCarregado():
      status = status + "."
      print(status)


  @cherrypy.expose
  def carregarModelo(self, nome):
    print("Carregando Modelo...")
    self.classificador.carregar_modelo(nome)
    print("Modelo carregado...")


  @cherrypy.expose
  def classificarDebug(self, K):
    #self.doVerificaCarregouDataSource()
    return json.dumps(self.classificador.classificar_debug(int(K)))

  @cherrypy.expose
  def classificar(self, imageEncoded, K):
    #self.doVerificaCarregouDataSource()
    img = self.convertToPilImage(imageEncoded)
    return self.classificador.classificar(img, int(K))

  @cherrypy.expose
  def treinarKnnPadrao(self, nome, caminho, data_source, ie_dump):

      parametros = params.Parametros()
      #if not self.loaddingDataSource and \
      #        (not self.classificador.isDataSourceCarregado()):
      #   print("Treinando Modelo...")
      self.loaddingDataSource = True
      nome = self.classificador.treinar_knn(nome, caminho, data_source, parametros, ie_dump)
      self.loaddingDataSource = False
      print("Modelo treinado e carregado...")
      return nome

  @cherrypy.expose
  def treinarCustomizado(self, nome, caminho, data_source,
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
                         PAUSA_SEMIFUSA,
                         QT_BARRAS_COMPASSO,
                         IE_DUMP):

      parametros = params.Parametros()
      parametros.QT_SEMIBREVE = int(QT_SEMIBREVE)
      parametros.QT_MINIMA = int(QT_MINIMA)
      parametros.QT_SEMINIMA = int(QT_SEMINIMA)
      parametros.QT_COLCHEIA = int(QT_COLCHEIA)
      parametros.QT_SEMICOLCHEIA = int(QT_SEMICOLCHEIA)
      parametros.QT_FUSA = int(QT_FUSA)
      parametros.QT_SEMIFUSA = int(QT_SEMIFUSA)
      parametros.QT_CLAVESOL = int(QT_CLAVESOL)
      parametros.QT_CLAVEFA = int(QT_CLAVEFA)
      parametros.QT_CLAVEDO = int(QT_CLAVEDO)
      parametros.QT_FERMATA = int(QT_FERMATA)
      parametros.QT_LIGADURA = int(QT_LIGADURA)
      parametros.QT_BARRAS_COMPASSO = int(QT_BARRAS_COMPASSO)

      if (PAUSA_SEMIBREVE == "S") :
          parametros.QT_PAUSA_SEMIBREVE = int(QT_SEMIBREVE)
      if (PAUSA_MINIMA == "S"):
          parametros.QT_PAUSA_MINIMA = int(QT_MINIMA)
      if (PAUSA_SEMINIMA == "S"):
          parametros.QT_PAUSA_SEMINIMA = int(QT_SEMINIMA)
      if (PAUSA_COLCHEIA == "S"):
          parametros.QT_PAUSA_COLCHEIA = int(QT_COLCHEIA)
      if (PAUSA_SEMICOLCHEIA == "S"):
          parametros.QT_PAUSA_SEMICOLCHEIA = int(QT_SEMICOLCHEIA)
      if (PAUSA_FUSA == "S"):
          parametros.QT_PAUSA_FUSA = int(0)
      if (PAUSA_SEMIFUSA == "S"):
          parametros.QT_PAUSA_SEMIFUSA = int(0)

      if not self.loaddingDataSource and \
              (not self.classificador.isDataSourceCarregado()):
          print("Treinando Modelo customizado...")
          self.loaddingDataSource = True
          nome = self.classificador.treinar_knn(nome, caminho, data_source, parametros, IE_DUMP)
          self.loaddingDataSource = False
          print("Modelo customizado treinado e carregado...")
          return nome
      else:
          return "Erro"




s = Server()
cherrypy.config.update({'server.socket_port': 8090})
cherrypy.quickstart(s)

