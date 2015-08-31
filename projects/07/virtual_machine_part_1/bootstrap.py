from function_commands import function_commands

def createBootstrap():
  return ['@256', 'D=A', '@SP', 'M=D'] + function_commands['call']('Sys.init', 0).split('\n')