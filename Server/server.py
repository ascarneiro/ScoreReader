import cherrypy
import json


class Animal(object):
    def toJSON(self):
        return json.dumps(self, default=lambda o: o.__dict__, sort_keys=True, indent=4)

class Server(object):
    @cherrypy.expose
    def calcularMedia(self):
        return 'Media e: %d' % ((7 + 10 + 9) / 3,) 

    @cherrypy.expose
    def classificar(self):
        return "A nota e Do :P...."

    @cherrypy.expose
    @cherrypy.tools.json_out()
    def getJson(self):
        return   {
                    "message": "Hello World!"
                }

    @cherrypy.expose
    @cherrypy.tools.json_out()
    def asJson(self):
        animal = Animal()
        animal.classe   = "mamifero"
        animal.especie  = "carneiro"
        return animal.toJSON()
	
cherrypy.quickstart(Server())