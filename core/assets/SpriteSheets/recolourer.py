import pygame, os
colInR = int(input("R >>> "))
colInG = int(input("G >>> "))
colInB = int(input("B >>> "))
colFrom = (colInR,colInG,colInB)
colToR = int(input("R2 >>> "))
colToG = int(input("G2 >>> "))
colToB = int(input("B2 >>> "))
fileName = input('file name >>> ')
colTo = (colToR,colToG,colToB)
files = os.listdir()
fNum = 0
#print(files)
for file in files:
    if file.endswith('.png'):
        print(file)
        imgIn = pygame.image.load(file)
        imgOut = pygame.Surface((imgIn.get_width(),imgIn.get_height()),pygame.SRCALPHA)
        for x in range(imgIn.get_width()):
            for y in range(imgIn.get_height()):
                if imgIn.get_at((x,y)) == colFrom:
                    imgOut.set_at((x,y),colTo)
                else:
                    imgOut.set_at((x,y),imgIn.get_at((x,y)))
        pygame.image.save(imgOut,"%s_%d.png"%(fileName,fNum))
        fNum+=1
