#include <utils.hpp>
#include <Arduino.h>

Servo motor;

void setup() {
  motor.attach(MOTOR_PIN);
  Serial.begin(57600);
  User user1 = addUser("1", "1234567800000000");
  char* m1 = string2ptr(String("open") + String("#") + user1.id);
  aes128_enc_single((uint8_t *)(user1.password).c_str(), m1);
  String* plain_text_splitted1 = authorize(user1.id, String(m1));
  process_cmd(plain_text_splitted1, motor);
  
  delay(200);

  User user2 = addUser("2", "8765432100000000");
  char* m2 = string2ptr(String("lock") + String("#") + user2.id);
  aes128_enc_single((uint8_t *)(user2.password).c_str(), m2);
  String* plain_text_splitted2 = authorize(user2.id, String(m2));
  process_cmd(plain_text_splitted2, motor);
}

void loop() {
  // recv_cmd();
  // delay(1000);
}