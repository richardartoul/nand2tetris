@1
D=A
@ARG
A=M+D
D=M
@SP
A=M
M=D
@SP
M=M+1
@SP
A=M
A=A-1
D=M
@4
M=D
@SP
M=M-1
@0
D=A
@SP
A=M
M=D
@SP
M=M+1
@0
D=A
@THAT
D=M+D
@R13
M=D
@SP
A=M
A=A-1
D=M
@R13
A=M
M=D
@SP
M=M-1
@1
D=A
@SP
A=M
M=D
@SP
M=M+1
@1
D=A
@THAT
D=M+D
@R13
M=D
@SP
A=M
A=A-1
D=M
@R13
A=M
M=D
@SP
M=M-1
@0
D=A
@ARG
A=M+D
D=M
@SP
A=M
M=D
@SP
M=M+1
@2
D=A
@SP
A=M
M=D
@SP
M=M+1
@SP
A=M
A=A-1
A=A-1
D=M
A=A+1
D=D-M
@SP
A=M
A=A-1
A=A-1
M=D
@SP
M=M-1
@0
D=A
@ARG
D=M+D
@R13
M=D
@SP
A=M
A=A-1
D=M
@R13
A=M
M=D
@SP
M=M-1
($MAIN_LOOP_START)
@0
D=A
@ARG
A=M+D
D=M
@SP
A=M
M=D
@SP
M=M+1
@SP
A=M
A=A-1
D=M
@SP
M=M-1
@$COMPUTE_ELEMENT
D;JNE
@$END_PROGRAM
0;JMP
($COMPUTE_ELEMENT)
@0
D=A
@THAT
A=M+D
D=M
@SP
A=M
M=D
@SP
M=M+1
@1
D=A
@THAT
A=M+D
D=M
@SP
A=M
M=D
@SP
M=M+1
@SP
A=M
A=A-1
A=A-1
D=M
A=A+1
D=D+M
@SP
A=M
A=A-1
A=A-1
M=D
@SP
M=M-1
@2
D=A
@THAT
D=M+D
@R13
M=D
@SP
A=M
A=A-1
D=M
@R13
A=M
M=D
@SP
M=M-1
@4
D=M
@SP
A=M
M=D
@SP
M=M+1
@1
D=A
@SP
A=M
M=D
@SP
M=M+1
@SP
A=M
A=A-1
A=A-1
D=M
A=A+1
D=D+M
@SP
A=M
A=A-1
A=A-1
M=D
@SP
M=M-1
@SP
A=M
A=A-1
D=M
@4
M=D
@SP
M=M-1
@0
D=A
@ARG
A=M+D
D=M
@SP
A=M
M=D
@SP
M=M+1
@1
D=A
@SP
A=M
M=D
@SP
M=M+1
@SP
A=M
A=A-1
A=A-1
D=M
A=A+1
D=D-M
@SP
A=M
A=A-1
A=A-1
M=D
@SP
M=M-1
@0
D=A
@ARG
D=M+D
@R13
M=D
@SP
A=M
A=A-1
D=M
@R13
A=M
M=D
@SP
M=M-1
@$MAIN_LOOP_START
0;JMP
($END_PROGRAM)