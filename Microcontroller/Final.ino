#include "DHT.h"
#include <Adafruit_MLX90614.h>
#include <WiFi.h>
#include <WiFiManager.h>  
#include <HTTPClient.h>
#include <ArduinoJson.h>


String HOST_NAME = "https://irrigationapi-production.up.railway.app";
String PATH_NAME_DATA = "/data";
String PATH_NAME_BASELINE = "/baseline";
String PATH_NAME_WEATHER = "/weather";


#define LED 2
Adafruit_MLX90614 mlx = Adafruit_MLX90614();

int moisture, decision;
float cwsi, vpd, es, ea;

float intercept,es_intercept, slope;
float tdry = 35.55;
float tadry = 32.8;
float tnws = 27.43;
float tanws = 28.5;

float dtu,dtl;

#define AOUT_PIN 35
#define DHTPIN 5
#define RELAY_PIN 26
#define DHTTYPE DHT11   // DHT 11

WiFiManager wm;


DHT dht(DHTPIN, DHTTYPE);

void setup() {
  Serial.begin(115200);
  pinMode(RELAY_PIN, OUTPUT);
  pinMode(LED,OUTPUT);
  digitalWrite(LED, HIGH);
  
  //wm.resetSettings();
  bool res;
  res = wm.autoConnect("ESP32-1");
  
  if(!res) {
        Serial.println("Failed to connect");
        // ESP.restart();
    } 
    else {
        //if you get here you have connected to the WiFi    
        Serial.println("connected :)");
        digitalWrite(LED,LOW);
    }
  
  Serial.print("Connected to WiFi network with IP Address: ");
  Serial.println(WiFi.localIP());
  
  if (!mlx.begin()) {
    Serial.println("Error connecting to MLX sensor. Check wiring.");
    while (1);
  };
  dht.begin();
  
}

void loop() {
  //delay(2000);
  HTTPClient http;
  String payload;

  http.begin(HOST_NAME + PATH_NAME_BASELINE);
  int httpResponseCode = http.GET();
  String plantData = http.getString();
  http.end();
  
  DynamicJsonDocument plant(1024); // Adjust size as needed based on your JSON
  DeserializationError Error = deserializeJson(plant, plantData);

  intercept = plant[0]["plants_datum"]["intercept"];
  slope = plant[0]["plants_datum"]["slope"];
  //tdry = plant[0]["plants_datum"]["tdry"];
  //tnws = plant[0]["plants_datum"]["tnws"];

  // Soil Moisture
  int value = analogRead(AOUT_PIN); // read the analog value from sensor
  moisture = ( 100 - ( (value/4095.00) * 100 ) );
  
  // DHT Sensor
  float h = dht.readHumidity();
  
  // Read temperature as Celsius (the default)
  float t = dht.readTemperature();

  // Canopy Temperature
  float object_temp = mlx.readObjectTempC();
  
  //saturation vapor pressure
  es = 0.6108* exp((17.27 *t) / (t + 237.3)) ;

  //saturation vapor pressure for dtu
  es_intercept = (0.6108* exp((17.27 *(t+intercept)) / ((t+intercept) + 237.3))) ;

  //actual vapor pressure
  ea = es*(h/100);

  //Vapor Pressure Deficit (VPD)in kilopascal
  vpd = es-ea;

  //dtl
  dtl = intercept + (slope * vpd);

  //dt upper
  dtu = intercept + (slope * (es - es_intercept));

  //Weather Prediction
  http.begin(HOST_NAME + PATH_NAME_WEATHER);
  httpResponseCode = http.GET();
  String weather_prediction = http.getString();
  http.end();

   // Parse JSON data
  StaticJsonDocument<200> doc;  // Adjust the capacity according to your JSON response size
  DeserializationError error = deserializeJson(doc, weather_prediction);
  const char* conditionText = doc["condition"]["text"];


  //calculate cwsi
  cwsi = ((object_temp - t)-(tnws-tanws))/((tdry - tadry)-(tnws-tanws));

  //decision
  if (cwsi < 0.3) //low
  {
    if (moisture > 65){
      decision = 0; //dont turn on pump
      goto point;
    }
    else{
      while (moisture<65){//turn the pump on. after it reaches 50%, pump off
        int before_moisture = ( 100 - ( (analogRead(AOUT_PIN)/4095.00) * 100 ) );
        digitalWrite(RELAY_PIN, HIGH);
        Serial.print("Moisture Before : ");
        Serial.println(before_moisture);
        Serial.print("CWSI : ");
        Serial.println(cwsi);
        delay(3000);
        digitalWrite(RELAY_PIN, LOW);
        int after_moisture = ( 100 - ( (analogRead(AOUT_PIN)/4095.00) * 100 ));
        Serial.print("Moisture After : ");
        Serial.println(after_moisture);
        moisture = after_moisture;
      }
      decision = 1;
      goto point;
    }
  }
  else if (cwsi >= 0.3 && cwsi <0.5){//CWSI Medium
    if (moisture >= 75){
      decision = 0;
      goto point;
    }
    else if(moisture >60 && moisture <=75){
      if(strstr(conditionText, "rain") != NULL){
        decision = 0; //because condition contain rain, dont turn on pump
        goto point;
      }
      else{
        while (moisture<65){//turn the relay high. after it reaches 50%, relay low
          int before_moisture = ( 100 - ( (analogRead(AOUT_PIN)/4095.00) * 100 ) );
          Serial.print("Moisture Before : ");
          Serial.println(before_moisture);
          Serial.print("CWSI : ");
          Serial.println(cwsi);
          digitalWrite(RELAY_PIN, HIGH);
          delay(2000);
          digitalWrite(RELAY_PIN, LOW);
          int after_moisture = ( 100 - ( (analogRead(AOUT_PIN)/4095.00) * 100 ));
          Serial.print("Moisture After : ");
          Serial.println(after_moisture);
          moisture = after_moisture;
        }
        decision = 1;
        goto point;
      }
    }
    else{
      while(moisture<60){
        int before_moisture = ( 100 - ( (analogRead(AOUT_PIN)/4095.00) * 100 ) );
          Serial.print("Moisture Before : ");
          Serial.println(before_moisture);
          Serial.print("CWSI : ");
          Serial.println(cwsi);
          digitalWrite(RELAY_PIN, HIGH);
          delay(2000);
          digitalWrite(RELAY_PIN, LOW);
          int after_moisture = ( 100 - ( (analogRead(AOUT_PIN)/4095.00) * 100 ));
          Serial.print("Moisture After : ");
          Serial.println(after_moisture);
          moisture = after_moisture;
      }
      decision = 1;
      goto point;
    }
  }
  else{//CWSI High
    if (moisture > 75){
      decision = 0;
      goto point;
    }
    else if (moisture>60 && moisture<75){
      if(strstr(conditionText, "rain") != NULL){
        decision = 0; //because condition contain rain, dont turn on pump
        goto point;
      }
      else{
        while (moisture<65){//turn the relay high. after it reaches 50%, relay low
          int before_moisture = ( 100 - ( (analogRead(AOUT_PIN)/4095.00) * 100 ) );
          Serial.print("Moisture Before : ");
          Serial.println(before_moisture);
          Serial.print("CWSI : ");
          Serial.println(cwsi);
          digitalWrite(RELAY_PIN, HIGH);
          delay(2000);
          digitalWrite(RELAY_PIN, LOW);
          int after_moisture = ( 100 - ( (analogRead(AOUT_PIN)/4095.00) * 100 ));
          Serial.print("Moisture After : ");
          Serial.println(after_moisture);
          moisture = after_moisture;
        }
        decision = 1;
        goto point;
      }
    }
    else{
      while (moisture<60){
        //turn the relay high. after it reaches 50%, relay low
        int before_moisture = ( 100 - ( (analogRead(AOUT_PIN)/4095.00) * 100 ) );
        Serial.print("Moisture Before : ");
        Serial.println(before_moisture);
        Serial.print("CWSI : ");
        Serial.println(cwsi);
        digitalWrite(RELAY_PIN, HIGH);
        delay(2000);
        digitalWrite(RELAY_PIN, LOW);
        int after_moisture = ( 100 - ( (analogRead(AOUT_PIN)/4095.00) * 100 ));
        Serial.print("Moisture After : ");
        Serial.println(after_moisture);
        moisture = after_moisture;
      }
      decision = 1;
      goto point;
    }
  }

  
  point :
  //Initiate json document
  StaticJsonDocument<200> jsonDocument;
  jsonDocument["canopy_temperature"] = object_temp;
  jsonDocument["air_temperature"] = t;
  jsonDocument["soil_moisture"] = moisture;
  jsonDocument["relative_humidity"] = h;
  jsonDocument["cwsi"] = cwsi;
  //jsonDocument["weather_prediction"] = conditionText;
  jsonDocument["decision"] = decision;

  // Serialize JSON to a string
  String jsonString;
  serializeJson(jsonDocument, jsonString);

  Serial.println(jsonString);

 
  
  // Post to database

  
  
  http.begin(HOST_NAME + PATH_NAME_DATA);
  http.addHeader("Content-Type", "application/json"); // Change content type to JSON

  int httpCode = http.POST(jsonString);

  if (httpCode > 0) {
    if (httpCode == HTTP_CODE_OK) {
      String payload = http.getString();
      Serial.println(payload);
    } else {
      Serial.printf("[HTTP] POST... code: %d\n", httpCode);
    }
  } else {
    Serial.printf("[HTTP] POST... failed, error: %s\n", http.errorToString(httpCode).c_str());
  }

  http.end();
  delay(1200000);
}
