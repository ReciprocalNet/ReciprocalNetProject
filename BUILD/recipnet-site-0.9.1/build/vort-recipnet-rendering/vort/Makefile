#
# Makefile for vort
#
#
############################################################
#
# Notes:
#	If you have an equivalent to SUNS -fsingle option, then it would
# be a good idea to use it. If not, you should use -Dfloat=double
#
.IGNORE:

RANLIB = ranlib
X11FLAGS = -O3
LINUXFLAGS = -O2 -DSYSV
CC = gcc
LEX = flex
YACC = bison
LEXLIB = -lfl
X11LIBS = -L/usr/X11R6/lib -lX11

all: linux

linux:
	cd lib; make MFLAGS="$(LINUXFLAGS)" CC="$(CC)"
	cd art/src; make MFLAGS="$(LINUXFLAGS)" CC="$(CC)" YACC="$(YACC)" LEX="$(LEX)" LEXLIB="$(LEXLIB)"
	cd tools; make MFLAGS="$(X11FLAGS)" CC="$(CC)"
	cd X11; make MFLAGS="$(X11FLAGS)" MLIBS="$(X11LIBS)" CC="$(CC)"

clobber:
	cd lib; rm -f core *.o *.lint libvort.a
	cd tools; rm -f median greyscale vortinfo gamma targ2vort vort2ps mulmcut imtools impaste imcreate nff2art vort2pcx pcx2vort ppm2vort vort2ppm background vort2simp *.o *.lint core
	cd art/src; rm -f core *.o *.lint art dart in.artd in.netartd nart gram.c gram.h lex.c
	cd X11; rm -f core *.o *.lint ?disp disp movie
