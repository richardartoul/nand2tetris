import re
from push_commands import push_commands 
from sp_manager import sp_manager
from arithmetic_logic_commands import arithmetic_logic_mapper

def writer(vmLine):
  if (vmLine in arithmetic_logic_mapper):
    return arithmetic_logic_mapper[vmLine] + sp_manager['decrementSP']

  pushConstantFinder = re.search('pushconstant(.*)', vmLine)
  if (pushConstantFinder):
    constant = pushConstantFinder.group(1)
    assemblyCommands = push_commands['pushConstant'](constant) + '\r' + sp_manager['incrementSP']
    return assemblyCommands

  return vmLine