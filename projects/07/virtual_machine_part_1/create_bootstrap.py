from function_commands import function_commands

def create_bootstrap():
  return ['@256', 'D=A', '@SP', 'M=D', function_commands['call']('Sys.init', 0)]
