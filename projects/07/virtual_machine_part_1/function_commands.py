from sp_manager import sp_manager
from itertools import repeat
from push_commands import push_commands
from pop_commands import pop_commands
from flow_commands import flow_commands
import random

def initializeLocalSegmentZero(n):
  n = int(n)
  # push constant zero, then pop into local segment at appropriate index
  # def initializeZero(counter):
    # return '%s' %(push_commands['constant'](0))
  repeatedString = '';
  # initialize local[0]..local[n-1] to zero
  for counter in range(n):
    if (counter == 0):
      repeatedString = repeatedString + '\n'
    repeatedString = repeatedString + '%s\n%s\n' %(push_commands['constant'](0), sp_manager['incrementSP'])
  return repeatedString

def pushReturn(functionName):
  return '@%s\nD=A\n%s\nM=D\n%s' %(functionName + '$return', sp_manager['SP'], sp_manager['incrementSP'])

def pushPointer(pointer):
  return '@%s\nD=M\n%s\nM=D\n%s' %(pointer, sp_manager['SP'], sp_manager['incrementSP'])

def pushState(functionName):
  return '%s\n%s\n%s\n%s\n%s' %(pushReturn(functionName), pushPointer('LCL'), pushPointer('ARG'), pushPointer('THIS'), pushPointer('THAT'))

def repositionARG(numArguments):
  return '@SP\nD=M\n@5\nD=D-A\n@%s\nD=D-A\n@ARG\nM=D' %(numArguments)

def repositionLCL():
  return '@SP\nD=M\n@LCL\nM=D'

def repositionARGAndLCL(numArguments):
  return '%s\n%s' %(repositionARG(numArguments),repositionLCL())

##############################################################

def createFrame():
  return '@LCL\nD=M\n@frame\nM=D'

def createRetAddr():
  return '@frame\nD=M\n@5\nD=D-A\nA=D\nD=M\n@retAddr\nM=D'

# This one is tricky. Once the active function is complete, the value it should RETURN is at the top of the stack.
# This value needs to be popped OFF the stack (running function), and into the first index of the argument segment
# If you look at the global stack diagrams in chapter 8 of NAND2Tetris, you'll see this positions the popped return
# value to be the value that is at the top of the stack for the CALLING function once control returns to it
def popReturn():
  return '%s' %(pop_commands['argument'](0))

def returnSetup():
  return '%s\n%s\n%s' %(createFrame(), createRetAddr(), popReturn())

def restoreCallerSP():
  return '@ARG\nD=M\n@SP\nM=D+1'

def restoreCallerPointer(pointer, offset):
  return '@frame\nD=M\n@%s\nD=D-A\nA=D\nD=M\n@%s\nM=D' %(offset, pointer)

def restoreCallerState():
  return '%s\n%s\n%s\n%s\n%s' %(restoreCallerSP(), restoreCallerPointer('THAT', 1), restoreCallerPointer('THIS', 2), restoreCallerPointer('ARG', 3), restoreCallerPointer('LCL', 4))

def gotoRetAddr():
  return '@retAddr\nA=M\n0;JMP'

def call(functionName, numArguments):
  functionLabel = functionName + str(random.randrange(100000))
  return '%s\n%s\n%s\n%s' %(pushState(functionLabel), repositionARGAndLCL(numArguments), flow_commands['goto'](functionName), flow_commands['label'](functionLabel + '$return'))

function_commands = {
  'function': (lambda functionName, numLocalVariables: '(%s)%s' %(functionName, initializeLocalSegmentZero(numLocalVariables))),
  'call'    : call,
  'return'  : (lambda: '%s\n%s\n%s' %(returnSetup(), restoreCallerState(), gotoRetAddr()))
}