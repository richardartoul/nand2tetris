from sp_manager import sp_manager

push_commands = {
  'pushConstant'  : (lambda constant: '@%s\nD=A\n%s\nM=D' % (constant, sp_manager['SP'])) 
}