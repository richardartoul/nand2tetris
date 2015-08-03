// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input. 
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel. When no key is pressed, the
// program clears the screen, i.e. writes "white" in every pixel.

// Put your code here.

//set number of 16bit blocks to color
@8192
D=A
@screenSize
M=D

(MAINLOOP)
  //If key is pressed, blacken screen
  @KBD
  D=M
  @BLACKSCREEN
  D;JNE

  //else whiten screen
  @WHITESCREEN
  0;JMP

(BLACKSCREEN)
  //counter to know when we've hit every pixel
  @i
  M=0

  (BLACKLOOP)
    //check if screen is completely black
    @i
    D=M
    @screenSize
    D=D-M
    //if it is, go back to main loop
    @MAINLOOP
    D;JEQ

    //else keep painting black

    //block will point to sequence of 16 pixels that will be blacked out
    @SCREEN
    D=A
    @i
    D=D+M
    @block
    M=D

    //-1 is represented as all 1's in 2's complement i.e BLACK
    @block
    A=M
    M=-1

    //increment i
    @i
    M=M+1

    //repeat loop
    @BLACKLOOP
    0;JMP


(WHITESCREEN)
  //counter to know when we've hit every pixel
  @i
  M=0

  (WHITELOOP)
    //check if screen is completely white
    @i
    D=M
    @screenSize
    D=D-M
    //if it is, go back to main loop
    @MAINLOOP
    D;JEQ

    //else keep painting white

    //block will point to sequence of 16 pixels that will be blacked out
    @SCREEN
    D=A
    @i
    D=D+M
    @block
    M=D

    //0 corresponds to white
    @block
    A=M
    M=0

    //increment i
    @i
    M=M+1

    //repeat loop
    @WHITELOOP
    0;JMP