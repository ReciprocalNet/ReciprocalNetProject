
CC=cc
MFLAGS = -O
CFLAGS = $(MFLAGS) -I../lib 
MLIBS = -lX11
LIBS = ../lib/libvort.a $(MLIBS)
#LIBS = -lvort $(MLIBS) -lm
OBJS = disp.o movie.o

all: disp movie

disp: disp.o 
	$(CC) $(CFLAGS) -o disp disp.o $(LIBS)

movie: movie.o 
	$(CC) $(CFLAGS) -o movie movie.o $(LIBS)

clean:
	rm -f *.o core

clobber:
	rm -f disp ?disp movie core

lint:
	lint -I../lib disp.c > disp.lint
	lint -I../lib movie.c > movie.lint
