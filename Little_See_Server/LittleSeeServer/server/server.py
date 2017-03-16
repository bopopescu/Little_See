import tornado.web
import tornado.ioloop
import socket

ip = "0.0.0.0"

class ParentRequesyHandler(tornado.web.RequestHandler):
    def showInfo(self):
        if ip != self.request.remote_ip:
            print("source ip :" , self.request.remote_ip)

class Handler_ZhiHu(ParentRequesyHandler):
    def get(self):
        ParentRequesyHandler.showInfo(self)
        message = self.get_argument("date", None)
        # from server.server_zhihu import re_data_zhihu
        # json = re_data_zhihu(message)
        # self.write(json)

class Handler_HaoQiXin(ParentRequesyHandler):
    def get(self):
        ParentRequesyHandler.showInfo(self)
        message = self.get_argument("date", None)
        # from server.server_haoqixin import re_data_haoqixin
        # json = re_data_haoqixin(message)
        # self.write(json)

class Handler_Image(ParentRequesyHandler):
    def get(self):
        ParentRequesyHandler.showInfo(self)
        message = self.get_argument("date", None)
        statu = self.get_argument("statu" , None)
        # from server.server_image import re_data_image
        # json = re_data_image(message , statu)
        # self.write(json)


class Handler_Image_Old(ParentRequesyHandler):
    def get(self):
        ParentRequesyHandler.showInfo(self)
        message = self.get_argument("date", None)
        statu = self.get_argument("statu" , None)
        # from server.server_image import re_data_image_old
        # json = re_data_image_old(message , statu)
        # self.write(json)

class Main(ParentRequesyHandler):
    def get(self, *args, **kwargs):
        ParentRequesyHandler.showInfo(self)
        self.write("hello")

class Handler_Diary(ParentRequesyHandler):
    def get(self):
        ParentRequesyHandler.showInfo(self)
        indexclass = self.get_argument("indexclass" , None)

        from LittleSeeServer.server.server_diary import re_data_diary
        json = re_data_diary(indexclass)
        self.write(json)


application = tornado.web.Application([
    (r"/" , Main ),
    (r"/zhihu" , Handler_ZhiHu),
    (r"/haoqixin" , Handler_HaoQiXin),
    (r"/image" , Handler_Image),
    (r"/imageold" , Handler_Image_Old),
    (r"/diary" , Handler_Diary)

])

def runServer():
    port = 9006
    application.listen(port)

    localIP = socket.gethostbyname(socket.gethostname())
    print("run in %s:%s"%(localIP,port))
    tornado.ioloop.IOLoop.instance().start()