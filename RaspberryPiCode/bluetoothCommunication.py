import serial
import sys
import os
import time
import RPi.GPIO as IO

 
class SerialComm:
    def __init__(self):
        self.port = serial.Serial("/dev/serial0", baudrate=9600, timeout=1)
 
    def read_serial(self):
        res = self.port.read(50)
        if len(res):
            return res.splitlines()
        else:
            return []
 
    def send_serial(self, text):
        self.port.write(text)

def main():
    delay = 100;
    IO.setmode(IO.BCM);
    IO.setup(14,IO.IN); 
    ble_comm = SerialComm()
    while True:
        time.sleep(delay);
        if(IO.input(14)==False):  
            ble_comm.send_serial("emergency".encode());
            delay = 10;
        else:
            ble_comm.send_serial("normal".encode());
            delay = 100;
 
if __name__ == "__main__":
    main()
