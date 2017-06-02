import os
def renameFiles():
    fname = input("file name >>> ")
    fNum = 0
    for fileName in os.listdir():
        if fileName.endswith(".png"):
            os.rename(fileName, "%s_%d.png" % (fname,fNum))
            fNum+=1
if __name__ == '__main__':
    renameFiles()
