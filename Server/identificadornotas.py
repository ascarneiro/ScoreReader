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

            center = (x, y)
            r = int(r)
            #Elimina partes que nao estao dentro dos padroes
            if r >= 20 and r <= 24:
                #cv2.circle(image, center, r, (0, 255, 0), 3)

                M = cv2.moments(c)
                cX = (M["m10"] / M["m00"])
                cY = ((M["m01"] / M["m00"]))#fator diferenca

                fator = 107.79240779230769230769230769231
                #real = (cY / fator) * 100
                realX = cX - 14
                realY = cY - 14
                add = 50;
                if index % 2 == 0:
                    add = -50

                cv2.circle(image, (int(cX), int(cY)), 5, (255, 255, 255), -1)
                if self.debugMode:
                    cv2.putText(image, "(" + str(int(realX)) + " , " + str(int(realY)) + ")", (int(cX), int(cY + add)),
                                cv2.FONT_HERSHEY_TRIPLEX, 0.4, (255, 0, 0), 1, cv2.LINE_AA)

                figuras.append({"ponto": {"index": index, "x": (int(realX)), "y":(int(realY)), "center": center, "raio": r}})

            index = index + 1

        if self.debugMode:
            cv2.imwrite("C:/Users/ascarneiro/Desktop/TCC/ScoreReader/repository/alturaNotas.png", image_color)

        return figuras
