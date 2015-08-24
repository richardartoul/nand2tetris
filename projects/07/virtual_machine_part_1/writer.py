import re
from push_commands import push_commands 
from sp_manager import sp_manager
from arithmetic_logic_commands import arithmetic_logic_mapper

def writer(vmLine):
  # Handles stack arithmetic for all lines that are mathematical or logical operators
  if (vmLine in arithmetic_logic_mapper):
    if (vmLine == "not" or vmLine == "neg"):
      return arithmetic_logic_mapper[vmLine]
    # Only decrement SP if its a binary operator
    else:
      return arithmetic_logic_mapper[vmLine] + sp_manager['decrementSP']

  # Handles pushing constants
  pushConstantFinder = re.search('pushconstant(.*)', vmLine)
  if (pushConstantFinder):
    constant = pushConstantFinder.group(1)
    # Increment SP after pushing
    assemblyCommands = push_commands['pushConstant'](constant) + '\n' + sp_manager['incrementSP']
    return assemblyCommands

  # In a properly written VM program, this should never trigger, but returns the original line if
  # no code exists to handle it
  return vmLine