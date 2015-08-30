import re

# Replaces relative instructions with an instruction number based on the evaluated expression
def relative_instruction(index, line):
  relativeInstructionFinder = re.search('\$\$(.*)', line)
  
  if relativeInstructionFinder:
    relativeInstruction = '@' + str(index + int(relativeInstructionFinder.group(1)))
    return relativeInstruction

  # Doesn't change anything if no relative instruction is found
  return line
