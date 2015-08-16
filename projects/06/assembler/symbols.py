# import regular expression library
import re

# import predefined symbols
from binaryCodes import predefinedSymbols

# symbols and symbol count stored in global scope so they can be modified
# in each iteration of map
symbols = {}
labelCount = 0
variableCount = 0

def convertLabels(lineNumber, line):
  # convertSymbols, labelCount, and variableCount are global variables
  # this prevents convertSymbols from instantiating local versions of each
  global labelCount
  global symbols

  # regex to identify labels
  labelFinder = re.search('\((.*)\)', line)
  
  if labelFinder:

    label = labelFinder.group(1)

    # add label and corresponding line number to symbols
    # substract labelCount because all labels will eventually be removed
    # this keeps the line numbers accurate even after labels are removed
    symbols[label] = lineNumber - labelCount

    # increment labelCount everytime a label is found
    # to maintain line numbers after labels are removed which will cause a
    # line number offset
    labelCount += 1

    # return None so that labels can easily be removed later
    return None

  # if line didn't contain a label
  return line

def convertVariables(lineNumber, line):
  global variableCount

  # regex to identify variables
  variableFinder = re.search('@([^0-9].*)', line)

  if variableFinder:
    variable = variableFinder.group(1)

    # handle predefined symbol case
    if (variable in predefinedSymbols):
      return line
      
    # handle case when variable has already been seen before
    elif (variable in symbols):
      return '@' + str(symbols[variable])
    else:
      # user-defined variables start at memory[16]
      symbols[variable] = variableCount + 16
      variableCount += 1
      return '@' + str(symbols[variable])

  # if line didn't contain a variable
  return line


