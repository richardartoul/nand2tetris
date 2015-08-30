# handle command line arguments
import sys

# used to get list of files in directory
import os

# function that calls parser and writes output to file
from parse_write import parse_write

from parser import parse

from write_file import write_file

from bootstrap import createBootstrap

fileOrPath = sys.argv[1]

# initialize string to have bootstrap code at beginning
concatenatedFiles = createBootstrap()

# if argument is a directory, convert all .vm files to .asm
try:
  # if listdir throws an exception, then the argument was a file not a directory
  fileList = os.listdir(fileOrPath)
  for fileName in fileList:
    if (fileName.endswith('.vm')):
      # parse_write(fileOrPath + '/' + item)
      concatenatedFiles = concatenatedFiles + open(fileOrPath + fileName, 'r').read()
  folderName = os.path.basename(os.path.normpath(fileOrPath))
  translatedCode = parse(concatenatedFiles, folderName)
  write_file(translatedCode, fileOrPath + folderName + '.vm')
# if argument is a single file, convert it to .as
except:
  translatedCode = parse(open(fileOrPath, 'r').read(), os.path.basename(fileOrPath))
  write_file(translatedCode, fileOrPath)