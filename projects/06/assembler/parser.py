# import regular expression library
import re

# needed to handle command line arguments
import sys

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

compToBinary = {
  '0':'0101010',
  '1':'0111111',
  '-1':'0111010',
  'D':'0001100',
  'A':'0110000',
  '!D':'0001101',
  '!A':'0110001',
  '-D':'0001111',
  '-A':'0110011',
  'D+1':'0011111',
  'A+1':'0110111',
  'D-1':'0001110',
  'A-1':'0110010',
  'D+A':'0000010',
  'D-A':'0010011',
  'A-D':'0000111',
  'D&A':'0000000',
  'D|A':'0010101',
  'M':'1110000',
  '!M':'1110001',
  '-M':'1110011',
  'M+1':'1110111',
  'M-1':'1110010',
  'D+M':'1000010',
  'D-M':'1010011',
  'M-D':'1000111',
  'D&M':'1000000',
  'D|M':'1010101'
}

destToBinary = {
  #this is valid python haha
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
    print memoryReference

    # if its a predefined symbols
    if (memoryReference in predefinedSymbols):
        print '0' + predefinedSymbols[memoryReference]
        return '0' + predefinedSymbols[memoryReference]
    elif (re.match('[0-9]+', memoryReference)):
      # converts integer to properly formatted 15-digit binary
      print '0' + format(int(memoryReference),'015b')
      return '0' + format(int(memoryReference),'015b')

  # all C instructions
  else:
    print line
    if (line == '0'):
      print 1 + compToBinary['0'] + destToBinary['0'] + jumpToBinary['0'] 
    # This regular expression could be much more coplicated if you
    # wanted to do error handling, but I only need enough to break
    # them into their respective pieces
    cInstruction = re.search('([^=;]*)?(=)?([^;]*)?;?(.*)?', line)
    equalsSymbol = cInstruction.group(2)
    if (equalsSymbol):
      dest = cInstruction.group(1)
      comp = cInstruction.group(3)
    else:
      comp = cInstruction.group(1)
      dest = ''
    jump = cInstruction.group(4)
    binaryCInstruction = compToBinary[comp] + destToBinary[dest] + jumpToBinary[jump]
    print '111' + binaryCInstruction
    return '111' + binaryCInstruction

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
