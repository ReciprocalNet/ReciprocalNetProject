
MFLAGS = -g
CFLAGS = $(MFLAGS) -I../lib
LIB = ../lib/libvort.a
OBJS = median.o pixinfo.o greyscale.o targ2pix.o pix2ps.o gamma.o mulmcut.o \
	background.o vort2ppm.o ppm2vort.o

PROGS = median greyscale vortinfo targ2vort vort2ps vort2simp gamma \
	mulmcut nff2art imcreate impaste pcx2vort vort2pcx background \
	vort2ppm ppm2vort

DEST = /usr/local/bin
SHELL = /bin/sh

all: $(PROGS)

install:
	cp $(PROGS) $(DEST)
	for prog in $(PROGS); \
	do \
		chmod a+x $(DEST)/$${prog} ; \
		echo $${prog} ; \
	done ; 

gamma: gamma.o $(LIB)
	$(CC) $(CFLAGS) -o gamma gamma.o $(LIB) -lm

median: median.o $(LIB)
	$(CC) $(CFLAGS) -o median median.o $(LIB) -lm

greyscale: greyscale.o $(LIB)
	$(CC) $(CFLAGS) -o greyscale greyscale.o $(LIB) -lm

vortinfo: vortinfo.o $(LIB)
	$(CC) $(CFLAGS) -o vortinfo vortinfo.o $(LIB) -lm

targ2vort: targ2vort.o $(LIB)
	$(CC) $(CFLAGS) -o targ2vort targ2vort.o $(LIB) -lm

vort2ps: vort2ps.o $(LIB)
	$(CC) $(CFLAGS) -o vort2ps vort2ps.o $(LIB)

vort2simp: vort2simp.o $(LIB)
	$(CC) $(CFLAGS) -o vort2simp vort2simp.o $(LIB)

vort2pcx: vort2pcx.o $(LIB)
	$(CC) $(CFLAGS) -o vort2pcx vort2pcx.o $(LIB)

pcx2vort: pcx2vort.o $(LIB)
	$(CC) $(CFLAGS) -o pcx2vort pcx2vort.o $(LIB)

imcreate: imcreate.o $(LIB)
	$(CC) $(CFLAGS) -o imcreate imcreate.o $(LIB)

impaste: impaste.o $(LIB)
	$(CC) $(CFLAGS) -o impaste impaste.o $(LIB)

nff2art: nff2art.o $(LIB)
	$(CC) $(CFLAGS) -o nff2art nff2art.o -lm

mulmcut: mulmcut.o $(LIB)
	$(CC) $(CFLAGS) -o mulmcut mulmcut.o $(LIB)

background: background.o $(LIB)
	$(CC) $(CFLAGS) -o background background.o $(LIB)

vort2sun: vort2sun.o $(LIB)
	$(CC) $(CFLAGS) -o vort2sun vort2sun.o $(LIB) -lpixrect
	 
vort2ppm: vort2ppm.o $(LIB)
	$(CC) $(CFLAGS) -o vort2ppm vort2ppm.o $(LIB)

ppm2vort: ppm2vort.o $(LIB)
	$(CC) $(CFLAGS) -o ppm2vort ppm2vort.o $(LIB)

clean:
	rm -f *.o core *.lint

clobber:
	rm -f *.o core *.lint $(PROGS)

lint:
	lint *.c -I../lib > tools.lint

$(OBJS):\
../lib/vort.h

