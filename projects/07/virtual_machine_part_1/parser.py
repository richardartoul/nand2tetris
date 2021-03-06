# import regular expression library
import re

# converts relative jump instructions to absolute jump instructions
from relative_instruction import map_relative_instructions

# convers virtual machine to assembly
from writer import writer

# used to get file basename from path
import os

def parse(vmCodeString, filename):

  # read .vm file
  # vmCode = open(filename, 'r').read()

  # parse into array where each index is a line
  vmCode = vmCodeString.split('\n')

  # removes lines with comments and lines that contain nothing but whitespace
  vmCode = filter(lambda line: (not line.startswith('//')) and (not re.match('^\s*$', line)), vmCode)

  # removes comments after commands
  vmCode = map(lambda line: line.split('//', 1)[0], vmCode)

  # removes all whitespace from remaining lines
  # vmCode = map(lambda line: re.sub('\s+', '', line), vmCode)

  # Trim whitespace off edges of lines
  vmCode = map(lambda line: line.strip(), vmCode)

  # converts Virtual Machine instructions to Assembly
  # filename is used to push and pop static segment
  vmCode = map(lambda line: writer(line, os.path.basename(filename)), vmCode)

  # converts array of strings of assembly instructions into one long set of instructions
  vmCode = reduce(lambda commandList, allCommands: commandList + allCommands, vmCode)

  # converts string of commands to array where each item is a string that contains a single command
  vmCode = vmCode.split('\n')

  # last item is a blank string
  vmCode.pop()

  return vmCode