# 向 gradle.properties 中添加 key

import os
import json

fromFileName = ".keys.json"
toFileName = "gradle.properties"

KEYSTORE_PASS = 'KEYSTORE_PASS'
ALIAS_NAME = 'ALIAS_NAME'
ALIAS_PASS = 'ALIAS_PASS'

if not os.path.exists(fromFileName):
    print("The %s not found." % fromFileName)
    exit()

with open(fromFileName, "r") as f:
    jsonKey = f.read()

dictKey = json.loads(jsonKey)

if not dictKey:
    print("Error")
    exit()

with open(toFileName, "a") as f:
    f.write("\n%s=%s" % (KEYSTORE_PASS, dictKey[KEYSTORE_PASS]))
    f.write("\n%s=%s" % (ALIAS_NAME, dictKey[ALIAS_NAME]))
    f.write("\n%s=%s" % (ALIAS_PASS, dictKey[ALIAS_PASS]))

print("Complete")
