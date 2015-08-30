import re

# Replaces relative instructions with an instruction number based on the evaluated expression
def map_relative_instructions(vmCode):
  counter = 0
  for index, line in enumerate(vmCode):
    relativeInstructionFinder = re.search('\$\$(.*)', line)
  
    if relativeInstructionFinder:
      relativeInstruction = '@' + str(counter + int(relativeInstructionFinder.group(1)))
      vmCode[index] = relativeInstruction

    labelFinder = re.search('\(.*\)', line)

    # counter is only incremented if assembly line is a not a label. Since labels are removed when the
    # assembly is converted to machine code, not incrementing the counter prevents the line number offset
    # from being incorrect. Without this feature, the relative instructions would jump too far because the
    # labels artificially inflated the current line number
    if not labelFinder:
      counter = counter + 1
      
  return vmCode
