from sp_manager import sp_manager

# performs pointer arithmetic to determine which index in memory to pop the value to
# based on the segment that is being popped to, and then pops the value from the stack
# to the proper location (technically the stack isn't popped because decrementing)
# SP is handled elsewhere
def popSegment(index, segment):
  return '@%s\nD=A\n@%s\nD=M+D\n@R13\nM=D\n%s\nD=M\n@R13\nA=M\nM=D' %(index, segment, sp_manager['SP-1'])

# Instead of performing pointer arithmetic, simply pops the data off the stack
# to an address obtained by adding the index to a predetermined offset
def popOffset(index, offset):
  return '%s\nD=M\n@%s\nM=D' %(sp_manager['SP-1'], str(int(index)+offset))

# maps the segment that is being "popped to" to the proper assembly code for doing so
pop_commands = {
  'local'     : (lambda index: popSegment(index, 'LCL')),
  'argument'  : (lambda index: popSegment(index, 'ARG')), 
  'this'      : (lambda index: popSegment(index, 'THIS')), 
  'that'      : (lambda index: popSegment(index, 'THAT')),
  # temp and pointer are handled differently because pointer arithmetic doesn't need to be performed,
  # the offset is known. Its simply 3/5 + the index
  'temp'      : (lambda index: popOffset(index, 5)),
  'pointer'   : (lambda index: popOffset(index, 3)),
  'static'    : (lambda index, filename: '%s\nD=M\n@%s\nM=D' %(sp_manager['SP-1'], '%s.%s' % (filename, index)))
}