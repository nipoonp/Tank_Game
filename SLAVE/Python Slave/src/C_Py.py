import random
import string
import urllib2
import json
import hashlib
import binascii
import cherrypy
import os
import array
import threading
import time
import socket
import platform
import subprocess
from cherrypy.test.sessiondemo import page

fullObjectDictionary = {}

gameStated = "0"
challengerUPI = "No Challenger"
master = "None"
# challengerUsername = "No Challenger"
dataReceived = "keyType 1 value 0"
playerDictionary = {}
challenge_requests = []
send_host = "localhost"
send_port = 10048
listen_ip = "0.0.0.0"
listen_port = 10080
website = "http://cs302.pythonanywhere.com/"
password = 'password'
hashpass = 'COMPSYS302-2016'
hex_dig = hashlib.sha256(password + hashpass)
password = hex_dig.hexdigest()
details = {
    'username': 'username',
    'password': password,
    'ip': '10.103.137.39',
    'port': str(listen_port),
    'location': '0',
    'enc': '0'
    }
class MainApp(object):
    # CherryPy Configuration
    _cp_config = {'tools.encode.on': True,
                  'tools.encode.encoding': 'utf-8',
                  'tools.sessions.on' : 'True',
                 }

    # If they try somewhere we don't know, catch it here and send them to the right place.
    @cherrypy.expose
    def default(self, *args, **kwargs):
        """The default page, given when we don't recognise where the request is for."""
        Page = "I don't know where you're trying to go, so have a 404 Error."
        cherrypy.response.status = 404
        return Page


    # PAGES (which return HTML that can be viewed in browser)
    @cherrypy.expose
    def index(self):

        Page = """<head>
                    <title>DBZ Wars</title>
                    <link href="/static/css/back.css" rel="stylesheet"
                </head>"""
        Page += '<div class="main">'
        Page += "<p>Welcome to Combat 2.0!</p>"
        Page += '</div>'
        Page += '<a href="login" class="myButton">LETS PLAY!</a>'
        Page += "<br/>"
        # Chat_box ="""
        #         <form action = "messageForm">
        #             <input id = "message" type="text" name="message_text">
        #             <input id = "send" type="submit" value="Send">
        #         </form>
        #         """
        # Page += Chat_box

        return Page

    @cherrypy.expose
    def playerlist(self):
        Page = '''
        <!DOCTYPE html>
        <html lang="en">
            <head>
                <link href='http://fonts.googleapis.com/css?family=Droid+Sans' rel='stylesheet' type='text/css'>
                <meta charset="utf-8">
                <title>Player List</title>
                <link href="/static/css/style.css" media="screen" rel="stylesheet" type="text/css" />
                <link href="/static/css/iconic.css" media="screen" rel="stylesheet" type="text/css" />
                <script src="prefix-free.js"></script>
            </head>
        <body>
            <div class="wrap">
            <nav>
                <ul class="menu">
                    <li><a href="index"><span class="iconic home"></span> Home</a></li>
                    <li><a href="viewchallenges"><span class="iconic plus-alt"></span> Show Challenges</a>
                    </li>
                    <li><a href="#"><span class="iconic magnifying-glass"></span> View Friends</a>
                    </li>
                    <li><a href="pingall"><span class="iconic map-pin"></span> Refresh List</a>
                    </li>
                    <li><a href="signout"><span class="iconic map-pin"></span> Log out</a>
                    </li>
                </ul>
                <div class="clearfix"></div>
            </nav>
            </div>
        </body>
        </html>
        '''
        Page += "Hello " + cherrypy.session['username'] + "!<br/>"
        Page += "<br>This is the list of online players<br/>"
        Page += "<br/>"
        # playerList = []
        playersOnline = self.getList()
        if (playersOnline[0:1] == "0"):
            playersArray = []
            playerList = []
            playersOnline = playersOnline.replace("0, Online user list returned\r\n", "")
            playersOnline = playersOnline.replace("\r\n", " ")
            playersArray = playersOnline.split(' ')
            playersArray.pop(-1)
            for ix in range(0, len(playersArray)):
                pArray = playersArray[ix].split(',')
                playerList.append(pArray)
            for ind in range(0, len(playerList)):
                playerDictionary[(playerList[ind])[0]] = (playerList[ind])[slice(1,len(playerList[ind]))]
            for i in range(0, len(playerList)):
                for j in range(0, len(playerList[i])):
                    Page += (playerList[i])[j] + "   "
                Page += '<form action="sendchallenge">'
                Page += '<input type="hidden" name="sender" value=' + str((playerList[i])[0]) + '>'
                Page += '<input type="submit" value="Challenge">'
                Page += '</form>'
                Page += "<br/>"
                Page += '<form action="sendchallenge">'
                Page += '<input type="hidden" name="sender" value=' + str((playerList[i])[0]) + '>'
                Page += '<input type="submit" value="Add Friend">'
                Page += '</form>'
                Page += "<br/>"
            # Page += '<form action="pingall">'
            # # Page += '<input type="hidden" name="playerId" value=' + str(i) + '>'
            # Page += '<input type="button" value="PING ALL">'
            # Page += '</form>'
            # Page += "<br/>"
        else:
            Page = "ERROR HAS OCCURED"
        return Page

    @cherrypy.expose
    def login(self):
        Page = '''
        <!DOCTYPE html>
        <html >
          <head>
            <meta charset="UTF-8">
            <title>Login Form</title>


            <link rel="stylesheet" href="css/normalize.css">


                <style>
              /* NOTE: The styles were added inline because Prefixfree needs access to your styles and they must be inlined if they are on local disk! */
              @import url(http://fonts.googleapis.com/css?family=Open+Sans);
        .btn { display: inline-block; *display: inline; *zoom: 1; padding: 4px 10px 4px; margin-bottom: 0; font-size: 13px; line-height: 18px; color: #333333; text-align: center;text-shadow: 0 1px 1px rgba(255, 255, 255, 0.75); vertical-align: middle; background-color: #f5f5f5; background-image: -moz-linear-gradient(top, #ffffff, #e6e6e6); background-image: -ms-linear-gradient(top, #ffffff, #e6e6e6); background-image: -webkit-gradient(linear, 0 0, 0 100%, from(#ffffff), to(#e6e6e6)); background-image: -webkit-linear-gradient(top, #ffffff, #e6e6e6); background-image: -o-linear-gradient(top, #ffffff, #e6e6e6); background-image: linear-gradient(top, #ffffff, #e6e6e6); background-repeat: repeat-x; filter: progid:dximagetransform.microsoft.gradient(startColorstr=#ffffff, endColorstr=#e6e6e6, GradientType=0); border-color: #e6e6e6 #e6e6e6 #e6e6e6; border-color: rgba(0, 0, 0, 0.1) rgba(0, 0, 0, 0.1) rgba(0, 0, 0, 0.25); border: 1px solid #e6e6e6; -webkit-border-radius: 4px; -moz-border-radius: 4px; border-radius: 4px; -webkit-box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.2), 0 1px 2px rgba(0, 0, 0, 0.05); -moz-box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.2), 0 1px 2px rgba(0, 0, 0, 0.05); box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.2), 0 1px 2px rgba(0, 0, 0, 0.05); cursor: pointer; *margin-left: .3em; }
        .btn:hover, .btn:active, .btn.active, .btn.disabled, .btn[disabled] { background-color: #e6e6e6; }
        .btn-large { padding: 9px 14px; font-size: 15px; line-height: normal; -webkit-border-radius: 5px; -moz-border-radius: 5px; border-radius: 5px; }
        .btn:hover { color: #333333; text-decoration: none; background-color: #e6e6e6; background-position: 0 -15px; -webkit-transition: background-position 0.1s linear; -moz-transition: background-position 0.1s linear; -ms-transition: background-position 0.1s linear; -o-transition: background-position 0.1s linear; transition: background-position 0.1s linear; }
        .btn-primary, .btn-primary:hover { text-shadow: 0 -1px 0 rgba(0, 0, 0, 0.25); color: #ffffff; }
        .btn-primary.active { color: rgba(255, 255, 255, 0.75); }
        .btn-primary { background-color: #4a77d4; background-image: -moz-linear-gradient(top, #6eb6de, #4a77d4); background-image: -ms-linear-gradient(top, #6eb6de, #4a77d4); background-image: -webkit-gradient(linear, 0 0, 0 100%, from(#6eb6de), to(#4a77d4)); background-image: -webkit-linear-gradient(top, #6eb6de, #4a77d4); background-image: -o-linear-gradient(top, #6eb6de, #4a77d4); background-image: linear-gradient(top, #6eb6de, #4a77d4); background-repeat: repeat-x; filter: progid:dximagetransform.microsoft.gradient(startColorstr=#6eb6de, endColorstr=#4a77d4, GradientType=0);  border: 1px solid #3762bc; text-shadow: 1px 1px 1px rgba(0,0,0,0.4); box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.2), 0 1px 2px rgba(0, 0, 0, 0.5); }
        .btn-primary:hover, .btn-primary:active, .btn-primary.active, .btn-primary.disabled, .btn-primary[disabled] { filter: none; background-color: #4a77d4; }
        .btn-block { width: 100%; display:block; }

        * { -webkit-box-sizing:border-box; -moz-box-sizing:border-box; -ms-box-sizing:border-box; -o-box-sizing:border-box; box-sizing:border-box; }

        html { width: 100%; height:100%; overflow:hidden; }

        body {
            width: 100%;
            height:100%;
            font-family: 'Open Sans', sans-serif;
            background: #092756;
            background: -moz-radial-gradient(0% 100%, ellipse cover, rgba(104,128,138,.4) 10%,rgba(138,114,76,0) 40%),-moz-linear-gradient(top,  rgba(57,173,219,.25) 0%, rgba(42,60,87,.4) 100%), -moz-linear-gradient(-45deg,  #670d10 0%, #092756 100%);
            background: -webkit-radial-gradient(0% 100%, ellipse cover, rgba(104,128,138,.4) 10%,rgba(138,114,76,0) 40%), -webkit-linear-gradient(top,  rgba(57,173,219,.25) 0%,rgba(42,60,87,.4) 100%), -webkit-linear-gradient(-45deg,  #670d10 0%,#092756 100%);
            background: -o-radial-gradient(0% 100%, ellipse cover, rgba(104,128,138,.4) 10%,rgba(138,114,76,0) 40%), -o-linear-gradient(top,  rgba(57,173,219,.25) 0%,rgba(42,60,87,.4) 100%), -o-linear-gradient(-45deg,  #670d10 0%,#092756 100%);
            background: -ms-radial-gradient(0% 100%, ellipse cover, rgba(104,128,138,.4) 10%,rgba(138,114,76,0) 40%), -ms-linear-gradient(top,  rgba(57,173,219,.25) 0%,rgba(42,60,87,.4) 100%), -ms-linear-gradient(-45deg,  #670d10 0%,#092756 100%);
            background: -webkit-radial-gradient(0% 100%, ellipse cover, rgba(104,128,138,.4) 10%,rgba(138,114,76,0) 40%), linear-gradient(to bottom,  rgba(57,173,219,.25) 0%,rgba(42,60,87,.4) 100%), linear-gradient(135deg,  #670d10 0%,#092756 100%);
            filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#3E1D6D', endColorstr='#092756',GradientType=1 );
        }
        .login {
            position: absolute;
            top: 50%;
            left: 50%;
            margin: -150px 0 0 -150px;
            width:300px;
            height:300px;
        }
        .login h1 { color: #fff; text-shadow: 0 0 10px rgba(0,0,0,0.3); letter-spacing:1px; text-align:center; }

        input {
            width: 100%;
            margin-bottom: 10px;
            background: rgba(0,0,0,0.3);
            border: none;
            outline: none;
            padding: 10px;
            font-size: 13px;
            color: #fff;
            text-shadow: 1px 1px 1px rgba(0,0,0,0.3);
            border: 1px solid rgba(0,0,0,0.3);
            border-radius: 4px;
            box-shadow: inset 0 -5px 45px rgba(100,100,100,0.2), 0 1px 1px rgba(255,255,255,0.2);
            -webkit-transition: box-shadow .5s ease;
            -moz-transition: box-shadow .5s ease;
            -o-transition: box-shadow .5s ease;
            -ms-transition: box-shadow .5s ease;
            transition: box-shadow .5s ease;
        }
        input:focus { box-shadow: inset 0 -5px 45px rgba(100,100,100,0.4), 0 1px 1px rgba(255,255,255,0.2); }

            </style>


                <script src="js/prefixfree.min.js"></script>


          </head>

          <body>

            <div class="login">
            <h1>Please login...</h1>

            <form action="/signin" method="post" enctype="multipart/form-data">
                <input type="text" name="username" value = "Username"/><br/>
                <input type="password" name="password" value = "Password"/>
                <input type="submit" value="Login"/>
            </form>



        </div>

                <script src="js/index.js"></script>




          </body>
        </html>
        '''
        return Page

    # LOGGING IN AND OUT
    @cherrypy.expose
    def signin(self, username=None, password=None):
        """Check their name and password and send them either to the main page, or back to the main login screen."""
        error = self.authoriseUserLogin(username, password)
        if (error == 0):

            cherrypy.session['username'] = username;
            thread_report = threading.Thread(target=self.reportThread)
            thread_report.start()

            # t_send = threading.Thread(target=self.sendData)
            # t_send.start()

            t_receive = threading.Thread(target=self.receiveData)
            t_receive.start()

            t_init_data_receive = threading.Thread(target=self.receiveInitGameData)
            t_init_data_receive.start()

            t_data_receive = threading.Thread(target=self.receiveGameData)
            t_data_receive.start()





            # subprocess(['java', '-jar', 'tank.jar'])

            raise cherrypy.HTTPRedirect('/playerlist')
        else:
            raise cherrypy.HTTPRedirect('/login')

    def authoriseUserLogin(self, username, password):
        password = self.hashpassword(password)
        details['username'] = username
        details['password'] = password
        return self.report()

    def hashpassword(self, password):
        hex_dig = hashlib.sha256(password + hashpass)
        passwordHash = hex_dig.hexdigest()
        return passwordHash

    @cherrypy.expose
    def signout(self):
        """Logs the current user out, expires their session"""

        username = cherrypy.session.get('username')
        if (username == None):
            pass
        else:
            cherrypy.lib.sessions.expire()
        self.logoff()
        raise cherrypy.HTTPRedirect('/')

    def list_centralserver_API(self):
        URL = website + "listAPI/"
        req = urllib2.Request(URL)
        REQUEST = urllib2.urlopen(req)
        returned = REQUEST.read()
        return returned

    def report(self):
        print("Reported")

        URL = website + "report/?username=" + details['username'] + "&password=" + details['password'] + "&ip=" + details['ip'] + "&port=" + details['port'] + "&location=" + details['location'] + "&enc=" + details['enc']
        req = urllib2.Request(URL)
        REQUEST = urllib2.urlopen(req)
        returned = REQUEST.read()
        print(returned)
        if (returned[:1] == '0'):
            return 0
        else:
            return 1

    def logoff(self):
        URL = website + "logoff/?username=" + details['username'] + "&password=" + details['password'] + "&enc=" + details['enc']
        req = urllib2.Request(URL)
        REQUEST = urllib2.urlopen(req)
        returned = REQUEST.read()
        print(returned[:1])
        if (returned[:1] == '0'):
            return 0
        else:
            return 1

    def getList(self):
        URL = website + "getList/?username=" + details['username'] + "&password=" + details['password']
        req = urllib2.Request(URL)
        REQUEST = urllib2.urlopen(req)
        returned = REQUEST.read()
        return returned

#     @cherrypy.expose
#     @cherrypy.tools.json_in()
#     @cherrypy.tools.json_out()
#     def json_in(self, accept = None):
#         if(cherrypy.request.method == "POST"):
#             # print(cherrypy.request.json)
#             print("challenge received")
#             data_get = cherrypy.request.json
#             print(data_get)
#             input_d = data_get
#             print(input_d)
#             Page = input_d
#         elif(cherrypy.request.method == "GET"):
#             print("Challenge Accepted or Declined")
#             challenger_ip = cherrypy.request.remote.ip
#             Page = "GET"
#         else:
#             Page = "<p> Error </p>"
#         return Page

    @cherrypy.expose
    @cherrypy.tools.json_out()

    @cherrypy.expose
    def listAPI(self):
        return "THESE ARE MY API"




    @cherrypy.expose()
    def viewchallenges(self):
        Page = ""
        for i in range (0, len(challenge_requests)):
            Page += challenge_requests[i]
            Page += '<form action="/acceptrespond">'
            Page += '<input type="hidden" name="i" value=' + str(i) + '>'
            Page += '<input type="submit" value="Yes">'
            Page += '</form>'
            Page += '<form action="/declinerespond">'
            Page += '<input type="hidden" name="i" value=' + str(i) + '>'
            Page += '<input type="submit" value="No">'
            Page += '</form>'
            Page += "<br/>"

        return Page

    @cherrypy.expose()
    @cherrypy.tools.json_out()
    def acceptrespond(self, i=None):
        playerSendIP = (playerDictionary[challenge_requests[int(i)]])[1]
        playerSendPort = (playerDictionary[challenge_requests[int(i)]])[2]
        output_dic = {"sender": details['username'], "accept" : "1"}
        data = json.dumps(output_dic)
        t_data_existing = threading.Thread(target=self.decode_getExistingObjects())
        t_data_existing.start()
        challengerUPI = challenge_requests[int(i)]
        master = 0
        URL = "http://" + playerSendIP + ":" + playerSendPort + "/respond"
        req = urllib2.Request(URL, data, headers={'Content-Type':'application/json'})
        REQUEST = urllib2.urlopen(req)


    @cherrypy.expose()
    @cherrypy.tools.json_out()
    def declinerespond(self,i = None):

        playerSendIP = (playerDictionary[challenge_requests[int(i)]])[1]
        playerSendPort = (playerDictionary[challenge_requests[int(i)]])[2]
        output_dic = {"sender": details['username'], "accept" : "0"}
        data = json.dumps(output_dic)

        URL = "http://" + playerSendIP + ":" + playerSendPort + "/respond"
        req = urllib2.Request(URL, data, headers = {'Content-Type':'application/json'})
        REQUEST = urllib2.urlopen(req)








    @cherrypy.expose
    @cherrypy.tools.json_in()
    def challenge(self):

        data_in = cherrypy.request.json
        var = data_in["sender"]
        challenge_requests.append(var)

        print("this guy: " + var + " challenged you")


        return "0"


#         \sender = cherrypy.request.json
#         playerSendIP = (playerDictionary[sender])[1]
#         playerSendPort = (playerDictionary[sender])[2]
#
#         URL = "http://"+ playerSendIP + ":" + playerSendPort + "/respond?sender=smup473&accept=1"
#         req = urllib2.Request(URL)
#         REQUEST = urllib2.urlopen(req)
#         returned = REQUEST.read()


    @cherrypy.expose
    @cherrypy.tools.json_out()
    def sendchallenge(self, sender=None):
        print("sending challenge")

        playerSendIP = (playerDictionary[sender])[1]
        playerSendPort = (playerDictionary[sender])[2]
        me = {"sender": details['username']}
        me = json.dumps(me)

        URL = "http://" + playerSendIP + ":" + playerSendPort + "/challenge"
        req = urllib2.Request(URL, me, headers = {'Content-Type':'application/json'})
        REQUEST = urllib2.urlopen(req)
        print(REQUEST)
        returned = REQUEST.read()
        print(returned)

        # subprocess.call(['java', '-jar', 'Master.jar'])

        raise cherrypy.HTTPRedirect('/playerlist')



    #
    #     raise cherrypy.HTTPRedirect('/challengePage?sender='+sender)
    #
    #
    # def challengePage(self, sender=None):
    #     Page = sender + " has challengeed you!"
    #     return Page

    @cherrypy.expose
    @cherrypy.tools.json_in()
    def respond(self):
        input_data = cherrypy.request.json
        sender = input_data["sender"]
        accept = input_data["accept"]

        if (accept == "1"):
            challengerUPI = sender
            master = 1

        print(sender,accept)
        return "0"


    # client


    @cherrypy.expose
    @cherrypy.tools.json_in()
    def receiveKey(self):



        input_data = cherrypy.request.json
        sender = input_data["sender"]
        keyType = input_data["keyType"]
        value = input_data["value"]
        try:
            stamp = input_data["stamp"]
        except:
            stamp = None


        # if(sender == challengerUsername):
        # format ie) "keyType 2 value 1"
        print("keyType "+keyType+ " value "+value)

        # self.sendData("keyType "+keyType+ " value "+value)

        return "0: <Action was successful>"

    # master
    @cherrypy.expose
    @cherrypy.tools.json_out()
    @cherrypy.tools.json_in()
    def getKey(self):
        input_data = cherrypy.request.json
        sender = input_data["sender"]
        keyType = input_data["keyType"]
        # format ie) "keyType 2 value 1"

        # dataReceived = self.receiveData()
        # print("Data being received is: ", dataReceived)
        dataReceiveArray = dataReceived.split(" ")
        keyTypeSend = dataReceiveArray[1]
        valueSend = dataReceiveArray[3]

        if (keyTypeSend == keyType):
            out_dic = {'value':valueSend, 'error': "0"}
            out_dic = json.dumps(out_dic)

        else:
            out_dic = {'value':"0", 'error': "0"}
            out_dic = json.dumps(out_dic)

        return out_dic



        # return dataReceived

    def sendData(self, dataSend=None): #to java
        sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        sock.connect(("localhost", 10048))
        # dataSend = keyType
        # print("Data being sent is: ", dataSend)
        sock.sendall(dataSend)
        time.sleep(0.01)
        sock.close

    def sendExistingObectsToJava(self, dataSend=None): #to java
        sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        sock.connect(("localhost", 10070))
        # dataSend = keyType
        # print("Data being sent is: ", dataSend)
        sock.sendall(dataSend)
        sock.close

    def sendInitObectsToJava(self, dataSend=None): #to java
        sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        sock.connect(("localhost", 10071))
        # dataSend = keyType
        # print("Data being sent is: ", dataSend)
        sock.sendall(dataSend)
        sock.close

    # def receiveData(self):
    #     s = socket.socket()  # Create a socket object
    #     s.bind(("localhost", 10049))  # Bind to the port
    #     s.listen(5)  # Now wait for client connection.
    #     c, addr = s.accept()  # Establish connection with client.
    #     dataReceived = c.recv(2048)
    #     # print("Data being received is: ", dataReceived)
    #     c.close()  # Close the connection
    #     # return dataReceived


        # return "ERROR CODE, VALUE"


    @cherrypy.tools.json_out()
    def receiveInitGameData(self): #from java
        s = socket.socket()  # Create a socket object
        s.bind(("localhost", 10047))  # Bind to the port
        s.listen(5)  # Now wait for client connection.
        while True:
            c, addr = s.accept()  # Establish connection with client.
            dataReceive = c.recv(2048)
            # print("Data being received is: ", dataReceive)


            dataReceive = dataReceive.replace("\r\n", "")
            dataReceiveArray = dataReceive.split(" ")
            sender = details['username']
            objectID = dataReceiveArray[0]
            objType = dataReceiveArray[1]
            x = dataReceiveArray[2]
            y = dataReceiveArray[3]
            angle = dataReceiveArray[4]
            state = dataReceiveArray[5]
            alive = dataReceiveArray[6]

            fullObjectDictionary[objectID] = dataReceiveArray[1:7]

            out_dic = {"sender":sender, "objectID":objectID, "objType":int(objType), "x":int(x), "y":int(y), "angle":int(angle), "state":int(state), "alive":int(alive)}
            out_dic = json.dumps(out_dic)
            URL = "http://10.103.137.39:10080/receiveObject"
            req = urllib2.Request(URL, out_dic, headers = {'Content-Type':'application/json'})
            REQUEST = urllib2.urlopen(req)
            returned = REQUEST.read()

            c.close()  # Close the connection
            # time.sleep(0.01)


    def receiveGameData(self): #from java
        s = socket.socket()  # Create a socket object
        s.bind(("localhost", 10046))  # Bind to the port
        s.listen(5)  # Now wait for client connection.
        while True:
            c, addr = s.accept()  # Establish connection with client.
            dataReceive = c.recv(2048)

            # print("Data being received is: ", dataReceive)

            dataReceive = dataReceive.replace("\r\n", "")
            dataReceiveArray = dataReceive.split(" ")
            # print(dataReceiveArray)
            fullObjectDictionary[dataReceiveArray[0]] = dataReceiveArray[1:7]

            c.close()  # Close the connection
            # time.sleep(0.01)

    # master
    @cherrypy.expose
    @cherrypy.tools.json_in()
    def receiveObject(self):

        input_data = cherrypy.request.json
        sender = input_data["sender"]
        objectID = input_data["objectID"]
        objType = input_data["objType"]
        x = input_data["x"]
        y = input_data["y"]
        angle = input_data["angle"]
        state = input_data["state"]
        alive = input_data["alive"]


        print("Received---------------------------------------")
        # print(sender,objectID,objType,x,y,angle,state,alive)


        dataSend = objectID + " " + str(objType) + " " + str(x) + " " + str(y) + " " + str(angle) + " " + str(state) + " " + str(alive)

        self.sendInitObectsToJava(dataSend)

        return "ERROR CODE"

    # client
    @cherrypy.tools.json_out()
    @cherrypy.tools.json_in()
    @cherrypy.expose

    def getObject(self):
        input_data = cherrypy.request.json
        sender = input_data["sender"]
        objectID = input_data["objectID"]

        returnArray = fullObjectDictionary[objectID]

        objType = returnArray[0]
        x = returnArray[1]
        y = returnArray[2]
        angle = returnArray[3]
        state = returnArray[4]
        alive = returnArray[5]

        out_dic = {"objectID":objectID, "objType":int(objType), "x":int(x), "y":int(y), "angle":int(angle), "state":int(state), "alive":int(alive)}
        out_dic = json.dumps(out_dic)
        return out_dic


    def decode_getObject(self, id = None):
        URL = "http://10.103.137.43:10050" + "/getObject"
        data = {'sender':details['username'], 'objectID': id}
        data = json.dumps(data)
        req = urllib2.Request(URL, data, headers={'Content-Type':'application/json'})
        REQUEST = urllib2.urlopen(req)
        returned = REQUEST.read()
        returned = json.loads(returned)
        returned = eval(returned)

        angle = returned["angle"]
        objectID = returned["objectID"]
        alive = returned["alive"]
        objType = returned["objType"]
        state = returned["state"]
        x = returned["x"]
        y = returned["y"]

        dataSend = objectID + " " + str(objType) + " " + str(x) + " " + str(y) + " " + str(angle) + " " + str(state) + " " + str(alive)

        self.sendExistingObectsToJava(dataSend)



    # client
    @cherrypy.expose
    @cherrypy.tools.json_out()
    @cherrypy.tools.json_in()
    def getExistingObjects(self):
        input_data = cherrypy.request.json
        sender = input_data["sender"]

        returnList = []
        for x in fullObjectDictionary:
            if (((fullObjectDictionary[x])[5] == '1') and ((fullObjectDictionary[x])[0] != '1')):
                id = x
                type = (fullObjectDictionary[x])[0]
                alive = 1
                returnList.append([id,type,alive])

        outputList = {"error":"0", 'objects':returnList}
        outputList = json.dumps(outputList)
        return outputList



    def decode_getExistingObjects(self):

            while(True):
                URL = "http://10.103.137.43:10050" + "/getExistingObjects"
                data = {'sender':details['username']}
                data = json.dumps(data)
                req = urllib2.Request(URL, data, headers={'Content-Type':'application/json'})
                REQUEST = urllib2.urlopen(req)
                returned = REQUEST.read()
                returned = json.loads(returned)
                returned = eval(returned)
                length = len(returned["objects"])
                print(returned)
                print("This is returned ^^^^^")
                for i in range(length):
                    listI = (returned["objects"])[i]
                    self.decode_getObject(listI[0])

                time.sleep(0.03)



    # both
    @cherrypy.expose
    def timeLeft(self, sender=None):
        return "ERROR CODE, time"

    # both
    @cherrypy.expose
    def gameState(self, sender=None):
        return "ERROE CODE, state"

    # master
    @cherrypy.expose
    def setState(self, sender=None, state=None):
        return "ERROR CODE"

    # master
    @cherrypy.expose
    def setScore(self, sender=None, masterScore=None, slaveScore=None):
        return "ERROE CODE"



    @cherrypy.expose
    def rules(self, sender=None):
        return "THESE ARE MY RULES"

    @cherrypy.expose
    def ping(self, sender=None):
        return "0"

    def pingall(self):
        print("PINGING ALL")

    def reportThread(self):
        while (True):
            print ("Reporting...")
            self.report()
            time.sleep(30)

#     @cherrypy.expose
#     def sendData(self, keyPress=None):
#         # while (True):
#         sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
#         sock.connect(("localhost", 10048))
#         dataSend = keyPress
#         print("Data being sent is: ", dataSend)
#         sock.sendall(dataSend)
#         sock.close
        # print("sendData sleeping")
        # time.sleep(1)
    #
    @cherrypy.tools.json_out()
    def receiveData(self): #from java
        s = socket.socket()  # Create a socket object
        s.bind(("localhost", 10049))  # Bind to the port
        s.listen(5)  # Now wait for client connection.
        while True:
            c, addr = s.accept()  # Establish connection with client.
            dataReceive = c.recv(2048)
            dataReceiveArray = dataReceive.split(" ")
            keyTypeSend = dataReceiveArray[1]
            valueSend = dataReceiveArray[3]
            sender = "smup473"
            out_dic = {"sender":sender, "keyType":keyTypeSend, "value":valueSend}
            out_dic = json.dumps(out_dic)
            URL = "http://10.103.137.43:10050/receiveKey"
            req = urllib2.Request(URL, out_dic, headers = {'Content-Type':'application/json'})
            REQUEST = urllib2.urlopen(req)
            returned = REQUEST.read()

#             URL = "http://172.23.93.52:10050/receiveKey?sender=s`mup473&keyType="+ keyTypeSend + "&value=" + valueSend
#             req = urllib2.Request(URL)
#             REQUEST = urllib2.urlopen(req)
#             returned = REQUEST.read()


            # print("Data being received is: ", dataReceive)
            c.close()  # Close the connection
            # time.sleep(0.01)

def runMainApp():
    # Create an instance of MainApp and tell Cherrypy to send all requests under / to it. (ie all of them)
    conf = {
         '/': {
             'tools.sessions.on': True,
             'tools.staticdir.root': os.path.abspath(os.getcwd())
         },
         '/static': {
             'tools.staticdir.on': True,
             'tools.staticdir.dir': './Public'
         }
     }
    cherrypy.tree.mount(MainApp(), "/", conf)
    # Tell Cherrypy to listen for connections on the configured address and port.
    cherrypy.config.update({'server.socket_host': listen_ip,
                            'server.socket_port': listen_port,
                            'engine.autoreload.on': True,
                           })
    print ("=========================")
    print ("University of Auckland")
    print ("COMPSYS302 - Software Design Application")
    print ("========================================")
    # Start the web server
    cherrypy.engine.start()
    # And stop doing anything else. Let the web server take over.
    cherrypy.engine.block()
# Run the function to start everything
runMainApp()
