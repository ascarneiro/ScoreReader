import cv2
import numpy as np


class IdentificaNotas(object):
    def __init__(self):
        self.debugMode = True;


    #Recebe a imagem ja tratada e detecta o centro dos pontos que correspondera a altura das notas musicais
    #retorna array com pontos centrais das notas
    def detectarPontosNotas(self, image_color):
        kernel = np.ones((2, 2), np.uint8)
        # Tentativa e erro no gimp numero de iteracoes
        image_color = cv2.erode(image_color, kernel, iterations=9)
        image_color = cv2.dilate(image_color, kernel, iterations=13)
        image_color = cv2.erode(image_color, kernel, iterations=9)

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
            # Elimina partes que nao estao dentro dos padroes
            if r >= 15 and r <= 25:
                # cv2.circle(image, center, int(r), (0, 255, 0), 3)

                M = cv2.moments(c)
                cX = (M["m10"] / M["m00"])
                cY = ((M["m01"] / M["m00"]))  # fator diferenca

                fator = 0.01054682218725924630306120164507
                realX = cX
                realY = cY
                add = 50
                if index % 2 == 0:
                    add = -50

                cv2.circle(image_color, (int(realX), int(realY)), 5, (255, 255, 255), -1)
                cv2.putText(image_color, "(" + str(int(realX)) + " , " + str(int(realY)) + ")",
                            (int(cX), int(cY + add)),
                            cv2.FONT_HERSHEY_TRIPLEX, 0.4, (255, 0, 0), 1, cv2.LINE_AA)

                figuras.append(
                    {"ponto": {"index": index, "x": (int(realX -14)), "y": (int(realY-14)), "center": center, "raio": r}})

            index = index + 1

        if self.debugMode:
            cv2.imwrite("C:/Users/ascarneiro/Desktop/TCC/ScoreReader/repository/alturaNotas.png", image_color)

        return figuras
