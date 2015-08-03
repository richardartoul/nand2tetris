// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)

//check if either R0 or R1 is 0
@R0
D=M
@ZERO
D;JEQ

@R1
D=M
@ZERO
D;JEQ

//i will count how many times we've added R0 to itself
@i
M=1

//initialize sum with R0
@R0
D=M
//sum will keep track of the running total --- final value is multiplication result
@sum
M=D

//keep looping until i = R1
(SUMLOOP)

  //check if i equals R1
  @i
  D=M
  @R1
  D=D-M
  @END
  //if it is, go to end
  D;JEQ
  
  //load sum into d
  @sum
  D=M
  //increase D by R0
  @R0
  D=D+M
  //load new sum into memory
  @sum
  M=D

  //increment i
  @i
  M=M+1

  //repeat loop
  @SUMLOOP
  0;JMP

(END)
  //store final result in R2
  @sum
  D=M
  @R2
  M=D

  //endless loop denotes end of program
  @END
  0;JMP

//case where either value is 0
(ZERO)
  //set sum to 0
  @sum
  M=0
  //end program
  @END
  0;JMP


