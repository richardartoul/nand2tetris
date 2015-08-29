from relative_instruction import relative_instruction

def write_file(vmCode, filename):
  outputFilename = filename.replace('.vm', '.asm')

  # convert relative jump instruction commands to absolute jump instructions
  vmCode = map(lambda (index, line): relative_instruction(index, line), enumerate(vmCode))

  # write assembly to file
  f = open(outputFilename, 'w')
  f.write('\n'.join(vmCode))
  f.close()