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

compToBinary: {
  '0':'101010',
  '1':'111111',
  '-1':'111010',
  'D':'001100',
  'A':'110000',
  '!D':'001101',
  '!A':'110001',
  '-D':'001111',
  '-A':'110011',
  'D+1':'011111',
  'A+1':'1101111',
  'D-1':'001110',
  'A-1':'110010',
  'D+A':'000010',
  'D-A':'010011',
  'A-D':'000111',
  'D&A':'000000',
  'D|A':'010101',
  'M':'110000',
  '!M':'110001',
  '-M':'110011',
  'M+1':'110111',
  'M-1':'110010',
  'D+M':'000010',
  'D-M':'010011',
  'M-D':'000111',
  'D&M':'000000',
  'D|M':'010101'
}

destToBinary = {
  '':'000',
  'M':'001',
  'D':'010',
  'MD':'011',
  'A':'100',
  'AM':'101',
  'AD':'110',
  'AMD':'111'
}

jumpToBinary = {
  '':'000',
  'JGT':'001',
  'JEQ':'010',
  'JGE':'011',
  'JLT':'100',
  'JNE':'101',
  'JLE':'110',
  'JMP':'111'
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
  # all C instructions
  else:
    print 'c:' + line

binary = map(translator, lines)
print binary

binary = []

print lines