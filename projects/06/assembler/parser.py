# import regular expression library
import re

# reads the .asm file
script = open('./testScript.txt', 'r').read();
# parses it into an array where each index represents a line
lines = script.split('\n');

# removes comments and empty lines
lines = filter(lambda line: not '//' in line and line != '', lines);

# lookup for predefined symbols
predefinedSymbols = {
  'sp':'000000000000000',
  'LCL':'00000000000001',
  'ARG':'000000000000010',
  'THIS':'00000000000011',
  'THAT':'000000000000100',
  'R0':'00000000000001',   
  'R1':'00000000000010',   
  'R2':'00000000000011',   
  'R3':'00000000000100',   
  'R4':'00000000000101',   
  'R5':'00000000000110',   
  'R6':'00000000000111',   
  'R7':'00000000001000',   
  'R8':'00000000001001',   
  'R9':'00000000001010',   
  'R1O':'00000000001011',   
  'R11':'00000000001100',   
  'R12':'00000000001101',   
  'R13':'00000000001110',
  'SCREEN':'100000000000000',
  'KBD':'110000000000000'   
}

def translator(line):
  # finds all A instructions
  aInstruction = re.search('@(.*)', line)
  if (aInstruction):
    # group(1) will contain memory address reference
    memoryReference = aInstruction.group(1)
    print aInstruction.group(1)
    if (memoryReference in predefinedSymbols):
        print predefinedSymbols[aInstruction.group(1)]

binary = map(translator, lines)
print binary

binary = []

print lines