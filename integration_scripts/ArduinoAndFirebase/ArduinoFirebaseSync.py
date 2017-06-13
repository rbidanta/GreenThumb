import serial
from firebase import firebase
import time
import requests
import json
import sys

#firebase = firebase.FirebaseApplication('https://projectgreenthumb-8cdc5.firebaseio.com/', None)

firebase_url = 'https://greenthumb-ed5a9.firebaseio.com/'
#firebase_url = 'https://greenthumbhackathon.firebaseio.com/'

#Connect to Serial Port for communication
ser = serial.Serial('/dev/tty.usbserial-DN01JOJN', 115200, timeout=1)

userid = sys.argv[1];
plantid = sys.argv[2];

#Setup a loop to send Temperature values at fixed intervals
#in seconds
fixed_interval = 10
while 1:
  try:
    #temperature value obtained from Arduino + LM35 Temp Sensor

    sensor_reading = ser.readline();

    print sensor_reading
    #current time and date
    if sensor_reading:
    #if 1:
            readings =  sensor_reading.split(';');

            if readings[0]:
                sunlight = readings[0];
            if readings[1]:
                ph = readings[1];
            if readings[2]:
                temperature = readings[2];
                temperature = temperature.replace('\r\n',"");
            if readings[3]:
                moisture =readings[3];
                moisture = moisture.replace('\r\n',"");


            #sunlight = '2522';
            #ph='67';
            #temperature='12';
            #moisture='23';
            #vstamp = int(round(time.time() * 1000))


            #current location name
            farm_location = 'Bloomington-Farm-Co';
            print sunlight + ',' + ph + ',' + temperature+','+moisture;

            #parent = {userid:{'Plants':{plantid:{thresholdValues:}}}

            #insert record
            data = {'sunlight':sunlight,'ph':ph,'temperature':temperature,'moisture':moisture};

            #result = firebase.post('/tempsensor',data,{'print': 'pretty'}, {'X_FANCY_HEADER': 'VERY FANCY'});
            result = requests.patch(firebase_url + '/' + userid + '/'+'Plants/'+plantid+'/thresholdValues.json', data=json.dumps(data))

            print 'Record inserted. Result Code = ' + str(result.status_code) + ',' + result.text
            time.sleep(fixed_interval);
    else:

            print 'Hello There!! No Sensor Reading recieved'

  except IOError:
    print('Error! Something went wrong.')
  time.sleep(fixed_interval)
