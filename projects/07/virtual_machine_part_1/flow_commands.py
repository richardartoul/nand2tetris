from sp_manager import sp_manager

flow_commands = {
  'label'   : (lambda label, currentFunction: '(%s$%s)' %(currentFunction, label)),
  'goto'    : (lambda label, currentFunction: '@%s$%s\n0;JMP' %(currentFunction, label)),
  'if-goto' : (lambda label, currentFunction: '%s\nD=M\n%s\n@%s$%s\nD;JNE' %(sp_manager['SP-1'], sp_manager['decrementSP'], currentFunction, label))
}