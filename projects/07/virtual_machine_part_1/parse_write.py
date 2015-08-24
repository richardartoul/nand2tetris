# import file parsing function
from parser import parse

def parse_write(filename):
  # parses file and converts virtual machine to assembly
  vmCode = parse(filename)

  outputFilename = filename.replace('.vm', '.asm')

  # write assembly to file
  f = open(outputFilename, 'w')
  f.write('\n'.join(vmCode))
  f.close()