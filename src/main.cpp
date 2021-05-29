#include <utils.hpp>
#include <Arduino.h>

void setup() {
  Serial.begin(57600);
  addUser("1", "1234567800000000");
  addUser("2", "7654321800000000");
}

void loop() {
  readCommand();
}