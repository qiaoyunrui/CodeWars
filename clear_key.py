# 清除 gradle.properties 文件中不宜公开的信息
import os

fileName = "gradle.properties"

keys = []
keys.append("KEYSTORE_PASS")
keys.append("ALIAS_NAME")
keys.append("ALIAS_PASS")

content = []


def has(content, keys):
    for key in keys:
        if key in content:
            return True
    return False


if not os.path.exists(fileName):
    print("File not found!")
    exit()

with open(fileName, "r") as f:
    for line in f.readlines():
        if not has(line, keys):
            if line.strip() != "":
                content.append(line)

with open(fileName, "w") as f:
    for line in content:
        f.write(line)

print("Complete")
