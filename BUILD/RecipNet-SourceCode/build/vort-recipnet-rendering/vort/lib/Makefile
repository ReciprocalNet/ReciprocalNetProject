
CC=cc
MFLAGS = -fsingle -O
RANLIB = ranlib
CFLAGS = $(MFLAGS)

OBJS = readrgba.o writrgba.o readmapl.o writmapl.o \
	imagepos.o imageio.o hdr.o vorterr.o

LIB = libvort.a

$(LIB): $(OBJS)
	rm -f $@
	ar rcv $@ $(OBJS)
	$(RANLIB) $@

$(OBJS):\
vort.h

lint:
	lint *.c -lm > vort.lint

clean:
	rm -f *.o core

clobber:
	rm -f libvort.a *.o core
