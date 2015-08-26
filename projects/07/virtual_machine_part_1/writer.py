import re
from push_commands import push_commands 
from pop_commands import pop_commands
from flow_commands import flow_commands
from sp_manager import sp_manager
from arithmetic_logic_commands import arithmetic_logic_mapper

def writer(vmLine, filename):
  # Handles stack arithmetic for all lines that are mathematical or logical operators
  if (vmLine in arithmetic_logic_mapper):
    if (vmLine == "not" or vmLine == "neg"):
      return arithmetic_logic_mapper[vmLine]
    # Only decrement SP if its a binary operator
    else:
      return arithmetic_logic_mapper[vmLine] + sp_manager['decrementSP'] + '\n'

  # Handles pushing values into stack from segments
  pushFinder = re.search('push(constant|argument|local|this|that|temp|pointer|static)(.*)', vmLine)
  if (pushFinder):
    segment = pushFinder.group(1)
    index = pushFinder.group(2)
    # static segment is handled independently because its dependent on filename
    if (segment == "static"):
      return push_commands["static"](index, filename) + '\n' + sp_manager['incrementSP'] + '\n'
    # Increment SP after pushing
    return push_commands[segment](index) + '\n' + sp_manager['incrementSP'] + '\n'

  # Handles popping values from stack into segments
  popFinder = re.search('pop(argument|local|this|that|temp|pointer|static)(.*)', vmLine)
  if (popFinder):
    segment = popFinder.group(1)
    index = popFinder.group(2)
    # static segment is handled independently because its dependent on filename
    if (segment == "static"):
      return pop_commands["static"](index, filename) + '\n' + sp_manager['decrementSP'] + '\n'
    # Decrement SP after popping
    return pop_commands[segment](index) + '\n' + sp_manager['decrementSP'] + '\n'

  # Handles Program Flow commands
  flowFinder = re.search('(label|goto|if-goto)(.*)', vmLine)
  if (flowFinder):
    flowCommand = flowFinder.group(1)
    flowLabel = flowFinder.group(2)
    return flow_commands[flowCommand](flowLabel) + '\n'
  # In a properly written VM program, this should never trigger, but returns the original line if
  # no code exists to handle it
  return vmLine