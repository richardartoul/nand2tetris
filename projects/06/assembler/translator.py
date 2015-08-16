# symbol to binary codes dictionaries
from binaryCodes import predefinedSymbols
from binaryCodes import compToBinary
from binaryCodes import destToBinary
from binaryCodes import jumpToBinary

# import regular expression libary
import re

def translator(line):
  # finds all A instructions
  aInstruction = re.search('@(.*)', line)
  if (aInstruction):
    # group(1) will contain memory address reference
    memoryReference = aInstruction.group(1)

    # if its a predefined symbol
    if (memoryReference in predefinedSymbols):
        return '0' + predefinedSymbols[memoryReference]

    elif (re.match('[0-9]+', memoryReference)):
      # converts integer to properly formatted 15-digit binary
      return '0' + format(int(memoryReference),'015b')

  # all C instructions
  else:
    # This regular expression could be much more complicated if you
    # wanted to do error handling, but I only need enough to break
    # them into their respective pieces
    cInstruction = re.search('([^=;]*)?(=)?([^;]*)?;?(.*)?', line)
    equalsSymbol = cInstruction.group(2)
    # depending on whether or not '=' is present, the ordering changes
    if (equalsSymbol):
      dest = cInstruction.group(1)
      comp = cInstruction.group(3)
    else:
      comp = cInstruction.group(1)
      dest = ''
    jump = cInstruction.group(4)

    binaryCInstruction = compToBinary[comp] + destToBinary[dest] + jumpToBinary[jump]
    return '111' + binaryCInstruction