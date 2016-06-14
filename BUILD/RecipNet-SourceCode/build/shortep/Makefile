#
# Makefile for shortep
#
# As distributed, this makefile calls for g77.  If you are running IRIX 6.x
# and want to use the SGI compiler then comment out the g77 defines and
# uncomment the IRIX defines.  The code should be fairly easily ported to
# other platforms as well.
#
.POSIX:
.SUFFIXES:
.SUFFIXES: .f .o

SHELL = /bin/sh

#
# For Linux or other systems with g77
#
#FC = g77
#FFLAGS = -O3 -ff90

#
# For IRIX 6.x with MIPSPro compilers
#
#F77 = f90
#FC = f90
#FFLAGS = -O3

OBJS = src/shortep.o src/crtguts.o src/sort.o src/adatainit.o
MAN1 = shortep.1
MAN5 = cif.5 CIF.5 star.5 STAR.5 sdt.5 SDT.5 crt.5 CRT.5 cssr.5 CSSR.5

#
#  The install locations can be adjusted here:
#
prefix = /usr/local
exec_prefix = $(prefix)
bindir = $(exec_prefix)/bin
mandir = $(prefix)/man
man1dir = $(mandir)/man1
man5dir = $(mandir)/man5

#
# Don't modify anything below this line:
################################################################################

all: shortep

shortep: $(OBJS)
	$(FC) $(FFLAGS) -o shortep $(OBJS)

install: shortep $(bindir) $(man1dir) $(man5dir)
	cp shortep $(bindir)
	chmod 755 $(bindir)/shortep
	cd man1 && cp $(MAN1) $(man1dir)
	cd man5 && cp $(MAN5) $(man5dir)

$(bindir):
	[ -d $(bindir) ] || mkdir $(bindir)

$(man1dir): $(mandir)
	[ -d $(man1dir) ] || mkdir $(man1dir)

$(man5dir): $(mandir)
	[ -d $(man5dir) ] || mkdir $(man5dir)

$(mandir): 
	[ -d $(mandir) ] || mkdir $(mandir)
	
clean:
	rm -f $(OBJS)

clobber: clean
	rm -f shortep

dist:	clobber
	cd .. && tar czf shortep.tgz shortep

uninstall:
	rm -f $(bindir)/shortep
	cd $(man1dir) && rm -f $(MAN1)
	cd $(man5dir) && rm -f $(MAN5)

