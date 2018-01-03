import os
import json

fileName = ".keys.json"

if os.path.exists(fileName):
    print("File has existed!")
    exit()

keys = {}

KEYSTORE_PASS = 'KEYSTORE_PASS'
ALIAS_NAME = 'ALIAS_NAME'
ALIAS_PASS = 'ALIAS_PASS'

DESC_STRING = "Please input '%s':"

print(DESC_STRING % KEYSTORE_PASS)
keystorePassword = input()
print(DESC_STRING % ALIAS_NAME)
aliasName = input()
print(DESC_STRING % ALIAS_PASS)
aliasPassword = input()
keys[KEYSTORE_PASS] = keystorePassword
keys[ALIAS_NAME] = aliasName
keys[ALIAS_PASS] = aliasPassword

jsonContent = json.dumps(keys)

with open(fileName, "w") as f:
    f.write(jsonContent)

print("File '.keys.json' has been created.")
