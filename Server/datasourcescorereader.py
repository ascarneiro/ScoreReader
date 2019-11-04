import numpy


class ScoreReaderDataSet(object):

    def __init__(self):

        self.semibreve = []
        self.minimas = []
        self.seminimas = []
        self.colcheias = []
        self.claveSol = []
        self.claveFa = []
        self.claveDo = []
        self.sustenidos = []
        self.bemois = []

        self.pausaSembreve = []
        self.pausaMinimas = []
        self.pausaSeminimas = []
        self.pausaColcheias = []

        self.barraCompasso = []

    def addFigura(self, tipo, img):
        cinza = img.convert('L')  # converte para escala cinza
        cinza = cinza.point(lambda x: 0 if (x < 128) else 255, '1')  # binariza imagem
        imagem = numpy.array(cinza, dtype='uint8')
        if 'Semibreve' == tipo:
            self.semibreve.append(imagem)
        elif 'Seminima' == tipo:
            self.seminimas.append(imagem)
        elif 'Minima' == tipo:
            self.minimas.append(imagem)
        elif 'Colcheia' == tipo:
            self.colcheias.append(imagem)
        elif 'ClaveSol' == tipo:
            self.claveSol.append(imagem)
        elif 'ClaveDo' == tipo:
            self.claveDo.append(imagem)
        elif 'ClaveFa' == tipo:
            self.claveFa.append(imagem)
        elif 'Sustenidos' == tipo:
            self.sustenidos.append(imagem)
        elif 'Bemois' == tipo:
            self.bemois.append(imagem)
        elif 'PausaSemiBreve' == tipo:
            self.pausaSembreve.append(imagem)
        elif 'PausaMinima' == tipo:
            self.pausaMinimas.append(imagem)
        elif 'PausaSeminima' == tipo:
            self.pausaSeminimas.append(imagem)
        elif 'PausaColcheia' == tipo:
            self.pausaColcheias.append(imagem)
        elif 'BarraCompasso' == tipo:
            self.barraCompasso.append(imagem)

    def figuras(self, tipo):
        vazio = []
        if 'Semibreve' == tipo:
            return self.semibreve
        elif 'Seminima' == tipo:
            return self.seminimas
        elif 'Minima' == tipo:
            return self.minimas
        elif 'Colcheia' == tipo:
            return self.colcheias
        elif 'ClaveSol' == tipo:
            return self.claveSol
        elif 'ClaveDo' == tipo:
            return self.claveDo
        elif 'ClaveFa' == tipo:
            return self.claveFa
        elif 'Sustenidos' == tipo:
            return self.sustenidos
        elif 'Bemois' == tipo:
            return self.bemois
        elif 'PausaSemiBreve' == tipo:
            return self.pausaSembreve
        elif 'PausaMinima' == tipo:
            return self.pausaMinimas
        elif 'PausaSeminima' == tipo:
            return self.pausaSeminimas
        elif 'PausaColcheia' == tipo:
            return self.pausaColcheias
        elif 'BarraCompasso' == tipo:
            return self.barraCompasso
        elif 'Ligadura' == tipo:
            return self.barraCompasso
        return vazio