from sp_manager import sp_manager

# performs pointer arithmetic to determine which index in memory to grab the value
# from based on the segment that is being pushed from, and then pushes that value
# onto the stack
def pushSegment(index, segment):
  return '@%s\nD=A\n@%s\nA=M+D\nD=M\n%s\nM=D' %(index, segment, sp_manager['SP'])

# maps the segment that is being pushed from to the proper assembly code for doing so
push_commands = {
  # constant is handled differently because there is no segment or offset
  # it simply push literal values into the stack
  'constant'  : (lambda constant: '@%s\nD=A\n%s\nM=D' % (constant, sp_manager['SP'])),
  'local'     : (lambda index: pushSegment(index, 'LCL')),
  'argument'  : (lambda index: pushSegment(index, 'ARG')), 
  'this'      : (lambda index: pushSegment(index, 'THIS')), 
  'that'      : (lambda index: pushSegment(index, 'THAT')),
  # temp is handled differently because pointer arithmetic doesn't need to be performed
  # the offset is known, its simply 5 + the index
  'temp'      : (lambda index: '@%s\nD=M\n%s\nM=D' %(str(int(index)+5), sp_manager['SP']))
}