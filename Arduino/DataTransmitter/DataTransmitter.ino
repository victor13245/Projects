/*
 * This ESP32 code is created by esp32io.com
 *
 * This ESP32 code is released in the public domain
 *
 * For more detail (instruction and wiring diagram), visit https://esp32io.com/tutorials/esp32-gps
 */

#include <TinyGPS++.h>
#include "BluetoothSerial.h"

#define GPS_BAUDRATE 9600  // The default baudrate of NEO-6M is 9600
#define SOUND_SPEED 0.034
#define CM_TO_INCH 0.393701

#if !defined(CONFIG_BT_ENABLED) || !defined(CONFIG_BLUEDROID_ENABLED)
#error Bluetooth is not enabled! Please run `make menuconfig` to and enable it
#endif

BluetoothSerial SerialBT;
TinyGPSPlus gps;  // the TinyGPS++ object

const int trigPin = 27;
const int echoPin = 26;
//const int ledPin = 12;

long duration;
float distanceCm;
String coords;
String result;

void setup() {
  Serial.begin(9600);
  Serial2.begin(GPS_BAUDRATE);
  SerialBT.begin("ESP32test"); //Bluetooth device name

  //pinMode(ledPin, OUTPUT);
  pinMode(trigPin, OUTPUT); // Sets the trigPin as an Output
  pinMode(echoPin, INPUT); // Sets the echoPin as an Input


  Serial.println(F("ESP32 - GPS module"));
}

void loop() {
  if (Serial2.available() > 0) {
    if (gps.encode(Serial2.read())) {
      if (gps.location.isValid()) {
        if (gps.speed.isValid()) {
          coords = (String)gps.location.lat() + ";" + (String)gps.location.lng() + "/" + gps.speed.kmph();

          // Clears the trigPin
          digitalWrite(trigPin, LOW);
          delayMicroseconds(2);
          // Sets the trigPin on HIGH state for 10 micro seconds
          digitalWrite(trigPin, HIGH);
          delayMicroseconds(10);
          digitalWrite(trigPin, LOW);
  
          // Reads the echoPin, returns the sound wave travel time in microseconds
          duration = pulseIn(echoPin, HIGH);
  
          // Calculate the distance
          distanceCm = duration * SOUND_SPEED/2;

          result = coords + "/" + (String)distanceCm;
          
          SerialBT.println(result);
          
      } 

      }

      Serial.println(gps.satellites.value()); // Number of satellites in use (u32)
      Serial.println();
    }
    /*else
    {
      // Clears the trigPin
          digitalWrite(trigPin, LOW);
          delayMicroseconds(2);
          // Sets the trigPin on HIGH state for 10 micro seconds
          digitalWrite(trigPin, HIGH);
          delayMicroseconds(10);
          digitalWrite(trigPin, LOW);
  
          // Reads the echoPin, returns the sound wave travel time in microseconds
          duration = pulseIn(echoPin, HIGH);
  
          // Calculate the distance
          distanceCm = duration * SOUND_SPEED/2;

          result = "INVALID/INVALID/" + (String)distanceCm;
          
          SerialBT.println(result);
          Serial.println("TEST");
      } */
  }
  
  
  if (millis() > 5000 && gps.charsProcessed() < 10)
    Serial.println(F("No GPS data received: check wiring"));
  delay(10);
}
