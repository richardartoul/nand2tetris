from sp_manager import sp_manager

def binary_stack_arithmetic(operation):
  return '%s\nD=M\nA=A+1\n%s\n%s\nM=D\n' %(sp_manager['SP-2'], operation, sp_manager['SP-2'])

def unary_stack_arithmetic(operation):
  return '%s\n%s\n' %(sp_manager['SP-1'], operation)

# Issues hack instructions that perform stack arithmetic for each of the allowed 
# mathematic and logical operators 
arithmetic_logic_mapper = {
  'add' : binary_stack_arithmetic('D=D+M'),
  'sub' : binary_stack_arithmetic('D=D-M'),
  'and' : binary_stack_arithmetic('D=D&M'),
  'or'  : binary_stack_arithmetic('D=D|M'),
  'neg' : unary_stack_arithmetic('M=-M'),
  'not' : unary_stack_arithmetic('M=!M'),
  # There is a small trick here: Normally, the equality operators would require creating
  # unique labels and jump commands for each one. For example, if D === 0 then jump to a label
  # then that label would modify the stack and set true, then jump again to resume code. Instead,
  # I implemented a relative instruction number operator "$$". For example, "$$4" allows me to jump
  # 4 lines of code ahead based on a certain condition. This feature works because right before all
  # the commands are written to a file, a function scans through each one (keeping track of the current
  # instruction number), and replaces the relative jumps with absolute jumps. For example, if "$$4" shows
  # up on line 4, then it will be replaced with "@8"
  'eq'  : binary_stack_arithmetic('D=D-M\n@R13\nM=-1\n@$$4\nD;JEQ\n@R13\nM=0\n@R13\nD=M'),
  'gt'  : binary_stack_arithmetic('D=D-M\n@R13\nM=-1\n@$$4\nD;JGT\n@R13\nM=0\n@R13\nD=M'),
  'lt'  : binary_stack_arithmetic('D=D-M\n@R13\nM=-1\n@$$4\nD;JLT\n@R13\nM=0\n@R13\nD=M')
}