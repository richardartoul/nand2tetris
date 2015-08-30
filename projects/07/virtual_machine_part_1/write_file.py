# import file parsing function
from parser import parse

def write_file(vmCode, filename):
  # parses file and converts virtual machine to assembly

  outputFilename = filename.replace('.vm', '.asm')

  # write assembly to file
  f = open(outputFilename, 'w')
  f.write('\n'.join(vmCode))
  f.close()