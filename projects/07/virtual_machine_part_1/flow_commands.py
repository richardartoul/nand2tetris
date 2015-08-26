from sp_manager import sp_manager

flow_commands = {
  'label'   : (lambda label: '(%s)' %(label)),
  'goto'    : (lambda label: '@%s\n0;JMP' %(label)),
  'if-goto' : (lambda label: '%s\nD=M\n%s\n@%s\nD;JNE' %(sp_manager['SP-1'], sp_manager['decrementSP'], label))
}