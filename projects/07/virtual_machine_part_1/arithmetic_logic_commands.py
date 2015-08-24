from sp_manager import sp_manager

arithmetic_logic_mapper = {
  'add' : '%s\rD=M\rA=A+1\rD=D+M\rA=A-1\rM=D\r' %(sp_manager['SP-2']),
  'sub' : '',
  'neg' : '',
  'eq'  : '',
  'gt'  : '',
  'lt'  : '',
  'and' : '',
  'or'  : '',
  'not' : ''
}