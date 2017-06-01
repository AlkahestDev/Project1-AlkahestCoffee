import os
fname = input()
fNum = 0
for fileName in os.listdir():
    if fileName.endswith(".png"):
        os.rename(fileName, "%s_%d.png" % (fname,fNum))
        fNum+=1
