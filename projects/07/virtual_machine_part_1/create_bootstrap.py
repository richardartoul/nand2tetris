from function_commands import function_commands

def create_bootstrap():
  return ['@256', 'D=A', '@SP', 'M=D', 'call Sys.init 0', 'label WHILESYS', 'goto WHILESYS']
