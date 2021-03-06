# import regular expression library
import re

# needed to handle command line arguments
import sys

# import label managing function
from symbols import convertLabels

# import variable managing function
from symbols import convertVariables

# import the translation function
from translator import translator

inputFilename = sys.argv[1]

# reads the .asm file
script = open(inputFilename, 'r').read()

# parses it into an array where each index represents a line
lines = script.split('\n')

# removes lines with comments and lines that contain nothing but whitespace
lines = filter(lambda line: (not line.startswith('//')) and (not re.match('^\s*$', line)), lines)

# removes comments after commands
lines = map(lambda line: line.split('//', 1)[0], lines)

# removes all whitespace from remaining lines
lines = map(lambda line: re.sub('\s+', '', line), lines)

labels = [convertLabels(lineNumber, line) for lineNumber, line in enumerate(lines)]
# removes labels now that they've been processed
labels = filter(lambda line: isinstance(line, str), labels)

variables = [convertVariables(lineNumber, line) for lineNumber, line in enumerate(labels)]

# maps all the assembly commands to binary
binary = map(translator, variables)

# removes any invalid commands and labels
binary = filter(lambda binary: isinstance(binary, str), binary)

outputFilename = inputFilename.replace('.asm', '.hack')

f = open(outputFilename, 'w')
# write the binary instructions to a file, separating them with new lines
f.write('\n'.join(binary))
f.close()
