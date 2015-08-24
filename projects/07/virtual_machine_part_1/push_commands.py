from sp_manager import sp_manager

# performs pointer arithmetic to determine which index in memory to grab the value
# from based on the segment that is being pushed from, and then pushes that value
# onto the stack
def pushSegment(index, segment):
  return '@%s\nD=A\n@%s\nA=M+D\nD=M\n%s\nM=D' %(index, segment, sp_manager['SP'])

# Instead of performing pointer arithmetic, simply pushes the data onto the stack
# from an address obtained by adding the index to a predetermined offset
def pushOffset(index, offset):
  return '@%s\nD=M\n%s\nM=D' %(str(int(index)+offset), sp_manager['SP'])

# maps the segment that is being pushed from to the proper assembly code for doing so
push_commands = {
  # constant is handled differently because there is no segment or offset
  # it simply push literal values into the stack
  'constant'  : (lambda constant: '@%s\nD=A\n%s\nM=D' % (constant, sp_manager['SP'])),
  'local'     : (lambda index: pushSegment(index, 'LCL')),
  'argument'  : (lambda index: pushSegment(index, 'ARG')), 
  'this'      : (lambda index: pushSegment(index, 'THIS')), 
  'that'      : (lambda index: pushSegment(index, 'THAT')),
  # temp and pointer are handled differently because pointer arithmetic doesn't need to be performed,
  # the offset is known. Its simply 3/5 + the index
  'temp'      : (lambda index: pushOffset(index,5)),
  'pointer'   : (lambda index: pushOffset(index,3))
}