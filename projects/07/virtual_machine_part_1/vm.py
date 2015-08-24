# handle command line arguments
import sys

# import file parsing function
from parser import parse

inputFilename = sys.argv[1]

vmCode = parse(inputFilename)

print vmCode

outputFilename = inputFilename.replace('.vm', '.asm')

f = open(outputFilename, 'w')
# write the binary instructions to a file, separating them with new lines
f.write('\n'.join(vmCode))
f.close()