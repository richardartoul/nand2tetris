# handle command line arguments
import sys

# import file parsing function
from parser import parse

inputFilename = sys.argv[1]

# parses file and converts virtual machine to assembly
vmCode = parse(inputFilename)

outputFilename = inputFilename.replace('.vm', '.asm')

# write assembly to file
f = open(outputFilename, 'w')
f.write('\n'.join(vmCode))
f.close()