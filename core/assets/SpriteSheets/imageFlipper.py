import pygame, os
files = os.listdir()
fNum = 0
fName = input("file name >>> ")
for file in files:
    if file.endswith('.png'):
        imageIn = pygame.transform.flip(pygame.image.load(file),True,False)
        pygame.image.save(imageIn,"%s_%d.png"%(fName,fNum))
        fNum+=1
