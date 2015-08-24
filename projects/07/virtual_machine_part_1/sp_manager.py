# Since the stack pointer is constantly being updated, this dictionary contains the assembly
# commands for frequently used manipulations of SP
sp_manager = {
  'incrementSP'   : '@SP\nM=M+1\n',
  'decrementSP'   : '@SP\nM=M-1\n',
  'SP'            : '@SP\nA=M',
}

# These are added after the fact so they can reference values in the dictionary itself
sp_manager['SP-1'] = '%s\nA=A-1' %(sp_manager['SP'])
sp_manager['SP-2'] = '%s\nA=A-1' %(sp_manager['SP-1'])