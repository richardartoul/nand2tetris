sp_manager = {
  'incrementSP'   : '@SP\rM=M+1\r',
  'decrementSP'   : '@SP\rM=M-1\r',
  'SP'            : '@SP\rA=M',
}

# These are added after the fact so they can reference values in the dictionary itself
sp_manager['SP-1'] = '%s\rA=A-1' %(sp_manager['SP'])
sp_manager['SP-2'] = '%s\rA=A-1' %(sp_manager['SP-1'])