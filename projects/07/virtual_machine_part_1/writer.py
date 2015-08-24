import re
from push_commands import push_commands 
from pop_commands import pop_commands
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

  # Handles pushing values into stack from segments
  pushFinder = re.search('push(constant|argument|local|this|that|temp|pointer)(.*)', vmLine)
  if (pushFinder):
    segment = pushFinder.group(1)
    index = pushFinder.group(2)
    # Increment SP after pushing
    assemblyCommands = push_commands[segment](index) + '\n' + sp_manager['incrementSP']
    return assemblyCommands

  # Handles popping values from stack into segments
  popFinder = re.search('pop(argument|local|this|that|temp|pointer)(.*)', vmLine)
  if (popFinder):
    segment = popFinder.group(1)
    index = popFinder.group(2)
    # Decrement SP after popping
    assemblyCommands = pop_commands[segment](index) + '\n' + sp_manager['decrementSP']
    return assemblyCommands

  # In a properly written VM program, this should never trigger, but returns the original line if
  # no code exists to handle it
  return vmLine