YFLAGS = -d
MFLAGS = -DSTATS -DTIME_STATS -I../../lib -O
CFLAGS = $(MFLAGS) -I../../lib 
LEXLIB = -lfl


SRCS = gram.c main.c lex.c prepro.c render.c gensamp.c trraw.c trgrid.c \
	tradapt.c message.c matrix.c solve.c rt.c stree.c shade.c light.c \
	expr.c symbol.c object.c tile.c bbox.c csg.c comp.c sphere.c box.c \
	geom.c interp.c alg.c torus.c hfield.c polygon.c ellipse.c cone.c \
	cylinder.c ring.c super.c smalloc.c util.c map.c mask.c texture.c \
	grid.c patch.c blob.c noise.c twentyf.c repeat.c readall.c

OBJS = gram.o main.o lex.o prepro.o render.o gensamp.o trraw.o trgrid.o \
	tradapt.o message.o matrix.o solve.o rt.o stree.o shade.o light.o \
	expr.o symbol.o object.o tile.o bbox.o csg.o comp.o sphere.o box.o \
	geom.o interp.o alg.o torus.o hfield.o polygon.o ellipse.o cone.o \
	cylinder.o ring.o super.o smalloc.o util.o map.o mask.o texture.o \
	grid.o patch.o blob.o noise.o twentyf.o repeat.o

OBJS2 = gram.o lex.o prepro.o gensamp.o message.o matrix.o solve.o rt.o \
	stree.o shade.o light.o expr.o symbol.o object.o tile.o bbox.o \
	csg.o comp.o sphere.o box.o geom.o interp.o alg.o torus.o hfield.o \
	polygon.o ellipse.o cone.o cylinder.o ring.o super.o mask.o smalloc.o \
	grid.o patch.o blob.o util.o map.o texture.o noise.o twentyf.o repeat.o\
        readall.o

OBJS3 = dart.o readall.o

all: art in.netartd nart in.artd dart

art: $(OBJS) ../../lib/libvort.a
	$(CC) $(CFLAGS) -o art $(OBJS) ../../lib/libvort.a -lm $(LEXLIB)

dart: dart.o readall.o ../../lib/libvort.a
	$(CC) $(CFLAGS) -o dart dart.o readall.o ../../lib/libvort.a -lm

in.netartd: in.netartd.o
	$(CC) $(CFLAGS) -o in.netartd in.netartd.o

in.artd: $(OBJS2) in.artd.o ../../lib/libvort.a
	$(CC) $(CFLAGS) -o in.artd in.artd.o $(OBJS2) ../../lib/libvort.a -lm $(LEXLIB)

nartc: nartc.o
	$(CC) $(CFLAGS) -o nartc nartc.o $(ARTLIBS)

nart: nart.o
	$(CC) $(CFLAGS) -o nart nart.o $(ARTLIBS)

lint: $(SRCS)
	lint -DNOSILLYNESS $(SRCS) -I../../lib -lm > art.lint

clean:
	rm -f *.o core

clobber:
	rm -f art nart dart nartc in.netartd in.artd *.o core gram.h gram.c lex.c

$(OBJS) : art.h gram.h macro.h ../../lib/vort.h

$(OBJS3) : art.h readall.h

alg.o solve.o torus.o : poly.h

lex.o: lex.c gram.h

gram.o: gram.c
	$(CC) -c -O -I../../lib gram.c

gram.c gram.h: gram.y
	$(YACC) $(YFLAGS) -b $* $*.y
	mv $*.tab.c $*.c
	mv $*.tab.h $*.h

.l.c:
	$(LEX) $(LFLAGS) -t $*.l > $*.c

