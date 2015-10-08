# nand2tetris

Building a computer from first principles; starting with NAND gates and building all the way up to a general purpose computer that can play tetris.

## Hardware

This portion of the project is complete. NAND gates and DFFs were used as elementary units to construct other logic gates, an ALU, RAM, a CPU, and finally a general purpose computer chip. All hardware was designed using a hardware description language.

The completed chips can be found in folders 01 through 05.

## Software

### Hack Assembler

Wrote an assembler for the Hack assembly language in python that can be found [here](https://github.com/richardartoul/nand2tetris/tree/master/projects/06/assembler). I also wrote some simple test programs directly in the Hack assembly language. Those programs can be found [here](https://github.com/richardartoul/nand2tetris/tree/master/projects/04).

### Virtual Machine Translator

Wrote a virtual machine translator (compiler back-end) that translates stack-based virtual machine code (similar to the Java virtual machine) into the Hack assembly language. This program was written in python and can be found [here](https://github.com/richardartoul/nand2tetris/tree/master/projects/07/virtual_machine_part_1).

### Compiler Front-End - In Progress

I Wrote the compiler front-end (the part that translates the object-oriented Jack language into virtual machine code) in the Clojure language. This program consists of a tokenizer, lexical parser, as well as a virtual machine code writing module.

The tokenizer and lexical parser were written from scratch in Clojure using a recursive descent algorithm. Tools like Lex and Yacc that simplify this process were not used.

### Operating System

I plan to write a general purpose operating system for the general purpose computer. This operating system will be written in the Jack language and will consist of a simple API for interacting with the computer hardware. For example: printing text to the screen, drawing various shapes, mathematical functions, etc.

### Tetris

Once the operating system is complete, I will write an implementation of Tetris using the Jack programming language in combination with the operating system described above.
