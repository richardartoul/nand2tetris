# handle command line arguments
import sys

# used to get list of files in directory
import os

# function that calls parser and writes output to file
from parse_write import parse_write

fileOrPath = sys.argv[1]

# if argument is a directory, convert all .vm files to .asm
try:
  # if listdir throws an exception, then the argument was a file not a directory
  fileList = os.listdir(fileOrPath)
  for item in fileList:
    if (item.endswith('.vm')):
      parse_write(fileOrPath + '/' + item)
# if argument is a single file, convert it to .as
except:
  parse_write(fileOrPath)