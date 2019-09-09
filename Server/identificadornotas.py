import cv2
import numpy as np

class IdentificaNotas(object):
    def __init__(self):
        self.debugMode = True;


    #Responsavel por erodir e dilatar imagem para que elimine notas falso positivas
    #retorna imagem melhor
    def melhorarImagem(self, img):
        kernel = np.ones((2, 2), np.uint8)
        #Tentativa e erro no gimp numero de iteracoes
        img = cv2.erode(img, kernel, iterations=11)
        img = cv2.dilate(img, kernel, iterations=13)
        img = cv2.erode(img, kernel, iterations=4)
        if self.debugMode:
            cv2.imwrite("C:/Users/ascarneiro/Desktop/TCC/ScoreReader/repository/erodidaDilatada.png", img)
        return img


    #Recebe a imagem ja tratada e detecta o centro dos pontos que correspondera a altura das notas musicais
    #retorna array com pontos centrais das notas
    def detectarPontosNotas(self, image_color):
        image_ori = cv2.cvtColor(image_color, cv2.COLOR_BGR2GRAY)

        lower_bound = np.array([0, 0, 10])
        upper_bound = np.array([255, 255, 195])

        image = image_color

        mask = cv2.inRange(image_color, lower_bound, upper_bound)

        mask = cv2.adaptiveThreshold(image_ori, 255, cv2.ADAPTIVE_THRESH_MEAN_C, cv2.THRESH_BINARY_INV, 33, 2)

        contours = cv2.findContours(mask.copy(), cv2.RETR_EXTERNAL,
                                    cv2.CHAIN_APPROX_SIMPLE)[0]
        contours.sort(key=lambda x: cv2.boundingRect(x)[0])

        figuras = []
        index = 1
        for c in contours:
            (x, y), r = cv2.minEnclosingCircle(c)
            center = (int(x), int(y))
            r = int(r)
            #Elimina partes que nao estao dentro dos padroes
            if r >= 20 and r <= 24:
                cv2.circle(image, center, r, (0, 255, 0), 3)
                figuras.append({"ponto": {"index": index, "x": x, "y":y, "center": center, "raio": r}})

            index = index + 1

        if self.debugMode:
            cv2.imwrite("C:/Users/ascarneiro/Desktop/TCC/ScoreReader/repository/alturaNotas.png", image_color)

        return figuras
