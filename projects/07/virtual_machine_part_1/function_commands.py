from sp_manager import sp_manager
from itertools import repeat
from push_commands import push_commands
from pop_commands import pop_commands

def initializeLocalSegmentZero(n):
  # push constant zero, then pop into local segment at appropriate index
  initializeZero = '%s\n%s' %(push_commands['constant'](0), pop_commands['local'](counter))
  repeatedString = '';
  # initialize local[0]..local[n-1] to zero
  for counter in range(n):
    repeatedString = repeatedString + initializeZero + '\n'
  return repeatedString

flow_commands = {
  'function'   : (lambda (functionName, numLocalVariables): '(%s\n%s)' %(functionName, ,initializeLocalSegmentZero)),
}