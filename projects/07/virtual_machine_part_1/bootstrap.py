def createBootstrap():
  return '@256\nD=A\n@SP\nM=D\ncall Sys.init 0\nlabel WHILESYS\n goto WHILESYS'