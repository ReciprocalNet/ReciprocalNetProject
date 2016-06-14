#
#
# Whereever you put the BINARY VOGLE HERSHEY FONTS....
#
.PHONY: clean clobber

FONTLIB=/usr/local/lib/hershey/

CC = gcc
CFLAGS = -DFONTLIB=\"$(FONTLIB)\"
YACC = bison -y
LEX = flex -l

text3d:	gram.o lex.o text3d.o
	$(CC) -o text3d text3d.o gram.o lex.o -lfl -lm
	
lex.o: lex.c gram.h

gram.c gram.h: gram.y
	$(YACC) -d -b $* $*.y
	mv $*.tab.c $*.c
	mv $*.tab.h $*.h

clean:
	rm -f *.o gram.[ch] lex.c

clobber: clean
	rm -f text3d

