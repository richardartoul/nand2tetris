# import regular expression library
import re

# needed to handle command line arguments
import sys

# import the translation function
from translator import translator

inputFilename = sys.argv[1]

# reads the .asm file
script = open(inputFilename, 'r').read()

# parses it into an array where each index represents a line
lines = script.split('\n')

# removes lines with comments and lines that contain nothing but whitespace
lines = filter(lambda line: (not '//' in line) and (not re.match('^\s*$', line)), lines)

# removes labels
lines = filter(lambda line: not re.match('\(.*\)', line), lines)

# removes all whitespace from remaining lines
lines = map(lambda line: re.sub('\s+', '', line), lines)

# maps all the assembly commands to binary
binary = map(translator, lines)

# removes any invalid commands
binary = filter(lambda binary: isinstance(binary, str), binary)

print binary

outputFilename = inputFilename.replace('.asm', '.hack')

f = open(outputFilename, 'w')
# write the binary instructions to a file, separating them with new lines
f.write('\n'.join(binary))
f.close()
