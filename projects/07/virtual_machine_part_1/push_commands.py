from sp_manager import sp_manager

push_commands = {
  'pushConstant'  : (lambda constant: '@%s\rD=A\r%s\rM=D' % (constant, sp_manager['SP'])) 
}