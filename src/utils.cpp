#include <utils.hpp>

class User {
  public:
    User(String id_, String password_) {
      id = id_;
      password = password_;
    }

    User() {};

    String id;
    String password;
};

User* users = new User[MAX_USERS_NUMBER];
int users_size = 0;

String* authorize(String id, String cypher_text_string) {

    String user_password = getPassword(id);

    if(user_password == "") {
        Serial.println("user not found");
        return NULL;
    }

    uint8_t* key = password2key(user_password);
    char* cypher_text = string2ptr(cypher_text_string);
    Serial.println(cypher_text);

    aes128_dec_single(key, cypher_text);

    String plain_text = String(cypher_text);
    delete[] cypher_text;

    String* plain_text_splitted = new String[MAX_SPLIT_SIZE];
    plain_text_splitted = split(plain_text, '#');

    if(plain_text_splitted[1] != id) {
        Serial.println("user not allowed");
        return NULL;
    }
    else {
        return plain_text_splitted;
    }
}

void addUser(String id, String password) {
  users[users_size] = User(id, password);
  users_size ++;
}

void removeUser(String id) {
  for(int i=0; i < users_size; i++) {
    if(users[i].id == id) {
      users[i] = User();
    }
  }
  users_size --;
}

String getPassword(String id) {
  for(int i=0; i < users_size; i++) {
    if(users[i].id == id) {
      return(users[i].password);
    }
  }
  return "";
}

char* string2ptr(String str) {
    char* writable = new char[str.length() + 1];
    str.toCharArray(writable, str.length() + 1);
    writable[str.length()] = '\0';
    return writable;
}

uint8_t* password2key(String password) {
    uint8_t key[16] ;

    for (int j = 0; j < 16; j++) {
        key[j] =  static_cast<uint8_t>(password[j] - '0');
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

String readCommand() {
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

  // String* authorize_check = authorize(id, cypher_text_string);
  delay(1000);
  String* authorize_check = authorize("1", "1234567800000000");
  return "";
}