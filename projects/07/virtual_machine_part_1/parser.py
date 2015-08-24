# import regular expression library
import re

# import end of program instructions


from writer import writer

def parse(filename):
  # read .vm file
  vmCode = open(filename, 'r').read()

  # parse into array where each index is a line
  vmCode = vmCode.split('\n')

  # removes lines with comments and lines that contain nothing but whitespace
  vmCode = filter(lambda line: (not line.startswith('//')) and (not re.match('^\s*$', line)), vmCode)

  # removes comments after commands
  vmCode = map(lambda line: line.split('//', 1)[0], vmCode)

  # removes all whitespace from remaining lines
  vmCode = map(lambda line: re.sub('\s+', '', line), vmCode)

  # converts Virtual Machine instructions to Assembly
  vmCode = map(writer, vmCode)

  return vmCode