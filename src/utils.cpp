#include <utils.hpp>

User* users = new User[MAX_USERS_NUMBER];
int users_size = 0; 

void open(Servo motor) {
  motor.write(180);
}

void lock(Servo motor) {
  for (int pos = 180; pos >= 0; pos -= 1) {
    motor.write(pos);
    delay(5);
  }
}


String* authorize(String id, String cypher_text_string) {
  char* password = getPassword(id);
  if(!password) {
      Serial.println("user not found");
      return NULL;
  }
  char* cypher_text = string2ptr(cypher_text_string);

  aes128_dec_single((uint8_t *)password, cypher_text);

  String plain_text = String(cypher_text);
  delete[] cypher_text;

  String* plain_text_splitted = new String[MAX_SPLIT_SIZE];
  plain_text_splitted = split(plain_text, '#');

  if(plain_text_splitted[1] != id) {
      Serial.println("user not allowed");
      return NULL;
  }
  else {
      Serial.println("Success!");
      return plain_text_splitted;
  }
}

User addUser(String id, String password) {
  User newUser = User(id, password);
  users[users_size] = newUser;
  users_size ++;
  return newUser;
}

void removeUser(String id) {
  for(int i=0; i < users_size; i++) {
    if(users[i].id == id) {
      users[i] = User();
    }
  }
  users_size --;
}

char* getPassword(String id) {
  for(int i=0; i < users_size; i++) {
    if(users[i].id == id) {
      return(users[i].password.c_str());
    }
  }
  return NULL;
}

char* string2ptr(String str) {
    char* writable = new char[str.length() + 1];
    str.toCharArray(writable, str.length() + 1);
    writable[str.length()] = '\0';
    return writable;
}

BYTE* password2key(String password) {
    BYTE key[16] ;

    for (int j = 0; j < 16; j++) {
        key[j] =  password[j];
    }

    return key;
}

String* split(String str , char c) {
  int size = 0;
  String* splitted = new String[MAX_SPLIT_SIZE];
  String word = "";
  for(int iter = 0 ; iter < str.length() ; iter++)
  {
      if(str[iter] == c)
      {
          splitted[size] = word;
          word = "";
          size += 1;
      }
      else
      {
          word += (str[iter]);
      }
  }
  splitted[size] = word;
  return splitted;
}

String recv_cmd() {
  String message;
  // if(Serial.available()) {
  // message = Serial.readStringUntil('\n');
  // if(message.length() == 0) {
  //   return "";
  // }
  // // }
  // String* message_splitted = split(message, '#');
  // String id = message_splitted[0];
  // String cypher_text_string = message_splitted[1];

  // Serial.println("here 1");

  // String* plain_text_splitted = authorize(id, cypher_text_string);
  
  return "";
}

void process_cmd(String* plain_text_splitted, Servo motor) {
  if(plain_text_splitted[0] == "open") {
    Serial.println("unlocking");
    open(motor);
  }
  if(plain_text_splitted[0] == "lock") {
    Serial.println("locking");
    lock(motor);
  }
  if(plain_text_splitted[0] == "add") {
    Serial.println("adding user");
    addUser(plain_text_splitted[1], plain_text_splitted[2]);
  }
  if(plain_text_splitted[0] == "remove") {
    Serial.println("removing user");
    removeUser(plain_text_splitted[1]);
  }
  delete[] plain_text_splitted;
}