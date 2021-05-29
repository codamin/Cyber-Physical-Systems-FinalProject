#include <Arduino.h>
#include <AESLib.h>

#define MAX_SPLIT_SIZE 3
#define MAX_USERS_NUMBER 10


String* authorize(String id, char* key, char* cypher_text);
void addUser(String id, String password);
void removeUser(String id);
String getPassword(String id);
void processCommand(String cmd);
char* string2ptr(String str);
uint8_t* password2key(String password);
String* split(String str , char c);
String readCommand();