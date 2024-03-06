/*
  WiFiAccessPoint.ino creates a WiFi access point and provides a web server on it.

  Steps:
  1. Connect to the access point "yourAp"
  2. Point your web browser to http://192.168.4.1/H to turn the LED on or http://192.168.4.1/L to turn it off
     OR
     Run raw TCP "GET /H" and "GET /L" on PuTTY terminal with 192.168.4.1 as IP address and 80 as port

  Created for arduino-esp32 on 04 July, 2018
  by Elochukwu Ifediora (fedy0)
*/

#include <WiFi.h>
#include <WiFiClient.h>
#include <WiFiAP.h>
#include <ESP32Servo.h>

#define LED_BUILTIN 2   
const char *ssid = "ReteauaMea";
const char *password = "parola123";

int secunde = 0;
int minute = 0;
int ore = 0;
int waitingTime = 10;

Servo myservo;  

int pos = 0;    
int i = 0;
// Recommended PWM GPIO pins on the ESP32 include 2,4,12-19,21-23,25-27,32-33 
int servoPin = 13;

WiFiServer server(80);


void setup() {
  pinMode(12, OUTPUT);

  // Allow allocation of all timers
  ESP32PWM::allocateTimer(0);
  ESP32PWM::allocateTimer(1);
  ESP32PWM::allocateTimer(2);
  ESP32PWM::allocateTimer(3);
  myservo.setPeriodHertz(50);   
  myservo.attach(servoPin, 500, 2400); 

  Serial.begin(115200);
  Serial.println();
  Serial.println("Configuring access point...");

  
  if (!WiFi.softAP(ssid, password)) {
    log_e("Soft AP creation failed.");
    while(1);
  }
  IPAddress myIP = WiFi.softAPIP();
  Serial.print("AP IP address: ");
  Serial.println(myIP);
  server.begin();

  Serial.println("Server started");
  secunde = 0;
  minute = 0;
  ore = 0;
  waitingTime = 10;
}

void loop() {
  WiFiClient client = server.available();   

  if (client) {                             
    Serial.println("New Client.");          
    String currentLine = "";                

    
    while (client.connected()) {            


      //Serial.println("> " + Serial.readString()
      
      if (client.available()) {             
        char c = client.read();             
        Serial.write(c);                    
        if (c == '\n') {                    

          
          if (currentLine.length() == 0) {
            
            client.println("HTTP/1.1 200 OK");
            client.println("Content-type:text/html");
            client.println();

            // the content of the HTTP response follows the header:
            client.print("Click <a href=\"/A\">here</a> for feeding your cat.<br>");
            client.print("Click <a href=\"/B\">here</a> to increment the waiting time.<br>");
            client.print("Click <a href=\"/C\">here</a> to decrement the waiting time.<br>");


            
            client.println();
            
            break;
          } else {    
            currentLine = "";
          }
        } else if (c != '\r') {  
          currentLine += c;      
        }

        
        if (currentLine.endsWith("GET /A")) {

            secunde = 0;
            minute = 0;
            ore = 0;
            digitalWrite(12,HIGH);
            for (pos = 0; pos <= 130; pos += 1) { 
              // in steps of 1 degree
              myservo.write(pos);    
              delay(7);             
              }

            delay(100);

            for(i = 0; i < 12; i += 1)
            {
      
              for (pos = 130; pos >= 55; pos -= 1) { 
                myservo.write(pos);    
                delay(7);             
              }

              delay(50);
            
              for (pos = 55; pos <= 130; pos += 1) { 
                // in steps of 1 degree
                myservo.write(pos);    
                delay(7);             
                }

            }

        

            for (pos = 130; pos >= 0; pos -= 1) { 
              myservo.write(pos);    
              delay(7);             
            }
            digitalWrite(12,LOW);
            
        }

        if (currentLine.endsWith("GET /B")) {
          waitingTime++;
        }

        if (currentLine.endsWith("GET /C")) {
          waitingTime--;
        }
       
      }
    }
    // close the connection:
    client.stop();
    Serial.println("Client Disconnected.");
    
  }


  if(secunde >= waitingTime)
      {
      secunde = 0;
      minute = 0;
      ore = 0;
      digitalWrite(12,HIGH);
      for (pos = 0; pos <= 130; pos += 1) { 
              myservo.write(pos);    
              delay(7);             
            }

      delay(100);

      for(i = 0; i < 12 ; i += 1)
      {
      
      for (pos = 130; pos >= 55; pos -= 1) { 
              myservo.write(pos);    
              delay(7);             
            }

            delay(50);
            
            for (pos = 55; pos <= 130; pos += 1) { 
              // in steps of 1 degree
              myservo.write(pos);    
              delay(7);             
            }

        }

        

       for (pos = 130; pos >= 0; pos -= 1) { 
              myservo.write(pos);    
              delay(7);            
            }

      digitalWrite(12,LOW);
      
      }

    secunde++;
    if(secunde >= 60)
    {
      secunde = 0;
      minute++;
    }
    if(minute >= 60)
    {
      minute = 0;
      ore++;
    }
    delay(1000);
  
}
