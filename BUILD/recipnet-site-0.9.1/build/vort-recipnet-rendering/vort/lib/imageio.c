#include <stdio.h>
#include <sys/types.h>
#include <fcntl.h>
#include <stdlib.h>
#include <malloc.h>
#include <sys/file.h>
#include <unistd.h>
#include <sys/stat.h>

#include "vort.h"

/*
 * getlongsize
 *
 *	return the size of a long value
 */
int getlongsize(val)
	unsigned long	val;
{
	int	size;

	if (val > 0xffffff)
		size = 4;
	else if (val > 0xffff)
		size = 3;
	else if (val > 0xff)
		size = 2;
	else
		size = 1;

	return size;
}

/*
 * readlongval
 *
 *	reads a long value from the file fd.
 */
int readlongval(fd, val)
	int	fd;
	long	*val;
{
	unsigned char	size, byte;
	int ret;

	read(fd, &size, 1);		/* get size byte */

	*val = 0;

	ret = size;

	while (size-- > 0) {
		*val <<= 8;
		if (read(fd, &byte, 1) != 1)
			vorterror("readlongval: unable to read value.\n");
		*val |= byte;
	}

	return ret;
}

/*
 * writelongval
 *
 *	writes a long value into the file fd.
 */
int writelongval(fd, val, size)
	int		fd;
	unsigned long	val;
	unsigned char	size;
{
	unsigned char	p[sizeof(long)];
	int		i;

	if (write(fd, &size, 1) != 1)
		vorterror("writelongval: unable to write value.\n");

	p[0] = val & 0xff;
	p[1] = (val >> 8) & 0xff;
	p[2] = (val >> 16) & 0xff;
	p[3] = (val >> 24) & 0xff;

	for (i = size - 1; i >= 0; i--)
		if (write(fd, &p[i], 1) != 1)
			vorterror("writelongval: unable to write value.\n");

	return(size + 1);
}

/*
 * extractlongval
 *
 *	extracts a long value from the byte stream pointed
 * to by ptr.
 */
void extractlongval(ptr, val)
	unsigned char	*ptr;
	long		*val;
{
	unsigned char	size;

	size = *ptr++;		/* get size byte */

	*val = 0;

	while (size-- != 0) {
		*val <<= 8;
		*val |= *ptr++;
	}
}

/*
 * readvortobj
 *
 *	read in a vort object
 */
void readvortobj(fd, obj)
	int	fd;
	v_object	*obj;
{
	unsigned char	byte, *objp;
	long		size;

	read(fd, &byte, 1);

	obj->type = byte;

	readlongval(fd, &size);		/* get size of object */

	if ((objp = (unsigned char *)malloc(size)) == (unsigned char *)NULL)
		vorterror("readvortobj: unable to find space for object.\n");

	if (read(fd, objp, size) != size)
		vorterror("readvortobj: unable to read object.\n");

	obj->size = size;
	obj->ptr = objp;
}

/*
 * writetextobj
 *
 *	writes out a text object - returning it's size.
 */
int writetextobj(im)
	image	*im;
{
	unsigned long	base, end;
	unsigned char	c, size;

	base = end = lseek(im->fd, 0L, 1);

	c = V_TEXT;
	end += write(im->fd, &c, 1);

	size = getlongsize((unsigned long)titlelength(im));

	end += writelongval(im->fd, (unsigned long)(size + 2 + 6), 2);

	c = T_LENGTH;
	end += write(im->fd, &c, 1);
	end += writelongval(im->fd, (unsigned long)titlelength(im), size);

	c = T_ADDR;
	end += write(im->fd, &c, 1);
	end += writelongval(im->fd, end + 5, 4);

	end += write(im->fd, imagetitle(im), titlelength(im));

	return(end - base);
}

/*
 * writecolormapobj
 *
 *	writes out a colormap object - returning it's size.
 */
int writecolormapobj(im)
	image		*im;
{
	unsigned char	c;
	unsigned long	base, end;

	base = end = lseek(im->fd, 0L, 1);

	c = V_COLORMAP;
	end += write(im->fd, &c, 1);
	end += writelongval(im->fd, 12L, 1);

	c = C_SIZE;
	end += write(im->fd, &c, 1);
	end += writelongval(im->fd, (unsigned long)cmapsize(im), 4);

	c = C_ADDR;
	end += write(im->fd, &c, 1);
	end += writelongval(im->fd, end + 5, 4);

	end += write(im->fd, im->red, cmapsize(im));
	end += write(im->fd, im->green, cmapsize(im));
	end += write(im->fd, im->blue, cmapsize(im));

	return(end - base);
}

/*
 * writeimageobj
 *
 *	write out an image object - returning it's size.
 */
int writeimageobj(im)
	image	*im;
{
	unsigned char	size, c;
	unsigned long	base, end;
	int		count;

	c = V_IMAGE;
	write(im->fd, &c, 1);

	base = lseek(im->fd, 0L, 1);

	/*
	 * make room for size of imagedata
	 */
	end = lseek(im->fd, 5L, 1);

	c = I_IMWIDTH;
	end += write(im->fd, &c, 1);
	size = getlongsize((unsigned long)imagewidth(im));
	end += writelongval(im->fd, (unsigned long)imagewidth(im), size);

	c = I_IMHEIGHT;
	end += write(im->fd, &c, 1);
	size = getlongsize((unsigned long)imageheight(im));
	end += writelongval(im->fd, (unsigned long)imageheight(im), size);

	c = I_IMDEPTH;
	end += write(im->fd, &c, 1);
	size = getlongsize((unsigned long)imagedepth(im));
	end += writelongval(im->fd, (unsigned long)imagedepth(im), size);

	count = 0;

	if (redchannel(im)) {
		c = I_RED;
		end += write(im->fd, &c, 1);
		end += writelongval(im->fd, (long)0, 0);
		count++;
	}

	if (greenchannel(im)) {
		c = I_GREEN;
		end += write(im->fd, &c, 1);
		c = 0;
		end += write(im->fd, &c, 1);
		count++;
	}

	if (bluechannel(im)) {
		c = I_BLUE;
		end += write(im->fd, &c, 1);
		c = 0;
		end += write(im->fd, &c, 1);
		count++;
	}

	if (alphachannel(im)) {
		c = I_ALPHA;
		end += write(im->fd, &c, 1);
		c = 0;
		end += write(im->fd, &c, 1);
	}

	c = I_BACKGND;
	end += write(im->fd, &c, 1);
	c = count;
	end += write(im->fd, &c, 1);
	if (redchannel(im)) {
		c = imagebackgnd(im).r;
		end += write(im->fd, &c, 1);
	}
	if (greenchannel(im)) {
		c = imagebackgnd(im).g;
		end += write(im->fd, &c, 1);
	}
	if (bluechannel(im)) {
		c = imagebackgnd(im).b;
		end += write(im->fd, &c, 1);
	}

	c = I_DATE;
	end += write(im->fd, &c, 1);
	end += writelongval(im->fd, (unsigned long)imagedate(im), 4);

	if (rlecoded(im)) {
		c = I_RLE_CODED;
		end += write(im->fd, &c, 1);
		c = 0;
		end += write(im->fd, &c, 1);
	}

	if (colormapped(im)) {
		c = I_COLORMAP;
		end += write(im->fd, &c, 1);
		end += writelongval(im->fd, im->cmaddr, 4);
	}

	if (imagefragment(im)) {

		c = I_XADDR;
		end += write(im->fd, &c, 1);
		size = getlongsize((unsigned long)imagexaddr(im));
		end += writelongval(im->fd, (unsigned long)imagexaddr(im), 4);

		c = I_YADDR;
		end += write(im->fd, &c, 1);
		size = getlongsize((unsigned long)imageyaddr(im));
		end += writelongval(im->fd, (unsigned long)imageyaddr(im), 4);

		c = I_ORIGWIDTH;
		end += write(im->fd, &c, 1);
		size = getlongsize((unsigned long)imageorigwidth(im));
		end += writelongval(im->fd, (unsigned long)imageorigwidth(im), 4);

		c = I_ORIGHEIGHT;
		end += write(im->fd, &c, 1);
		size = getlongsize((unsigned long)imageorigheight(im));
		end += writelongval(im->fd, (unsigned long)imageorigheight(im), 4);
	}

	c = I_ADDR;
	end += write(im->fd, &c, 1);
	end += writelongval(im->fd, end + 5, 4);

	lseek(im->fd, base, 0);
	writelongval(im->fd, end - (base + 5), 4);

	return(end - base + 1);
}

/*
 * buffer size for image files
 */
static int	ibufsize = IBUF_SIZE;

/*
 * imagebufsize
 *
 *	set the size for the image buffer. Must be called before open
 * image to take effect.
 */
void imagebufsize(size)
	int	size;
{
	ibufsize = size;
}

/*
 * readimage
 *
 *	returns an image structure for the image in the file referenced by
 * the file descriptor fd. Returns NULL if the image hasn't got a VORT
 * header.
 */
image *readimage(fd)
	int	fd;
{
	image	*im;
	int	flags;
	long	addr, oldaddr;
	v_object	obj;
	char	vers[VSTR_SIZE];

	oldaddr = lseek(fd, 0L, 1);

	if (read(fd, vers, VSTR_SIZE) != VSTR_SIZE) {
		lseek(fd, oldaddr, 0);
		return((image *)NULL);
	}

	if (strncmp(vers, VERSION_STR, VSTR_SIZE - 2) != 0) {
		lseek(fd, oldaddr, 0);
		return((image *)NULL);
	}

	if ((im = (image *)malloc(sizeof(image))) == (image *)NULL)
		return((image *)NULL);
	
	imagetitle(im) = "";
	titlelength(im) = 0;
	imagefragment(im) = 0;

	im->ibufsize = ibufsize;

	im->reading = 1;

	im->fd = fd;

	if ((im->ibuf = (unsigned char *)malloc(im->ibufsize)) == (unsigned char *)NULL)
		vorterror("readimage: unable to allocate space for image buffer.\n");

	readlongval(im->fd, &addr);		/* get start address */

	lseek(im->fd, addr, 0);			/* seek to first object */

	readvortobj(im->fd, &obj);

	setupimage(im, &obj);

	free(obj.ptr);

	im->ebufp = im->ibufp = im->ibuf + im->ibufsize;

	lseek(im->fd, im->imaddr, 0);		/* seek to start of image */

	return(im);
}

/*
 * openimage
 *
 *	returns an image structure for image file name. Takes same arguments
 * as fopen(3), the only difference been if the file name "-" is used stdin
 * or stdout is assumed.
 */
image *openimage(name, flag)
	char	*name;
	char	*flag;
{
	image	*im;
	int	flags;
	long	addr;
	v_object	obj;
	char	vers[VSTR_SIZE];
	char	buf[BUFSIZ];
	FILE	*cin;

	if (name == (char *)0)
		return((image *)NULL);

	if ((im = (image *)malloc(sizeof(image))) == (image *)NULL)
		return((image *)NULL);
	
	im->written = im->reading = 0;

	im->compressed = 0;

	imagetitle(im) = "";
	titlelength(im) = 0;
	imagefragment(im) = 0;

	if (strcmp(flag, "r") == 0) {
		im->reading = 1;
		flags = O_RDONLY;
	} else if (strcmp(flag, "w") == 0)
/*
		flags = O_WRONLY | O_CREAT | O_TRUNC;
*/
		flags = O_RDWR | O_CREAT | O_TRUNC;
	else {
		vortwarning("openimage: bad open flag.\n");
		return((image *)NULL);
	}

	if (strcmp(name, "-") == 0)
		im->fd = (im->reading) ? 0 : 1;
	else
		if ((im->fd = open(name, flags, S_IRUSR | S_IWUSR | S_IRGRP | S_IWGRP | S_IROTH)) < 0)
			return((image *)NULL);

	im->ibufsize = ibufsize;

	if ((im->ibuf = (unsigned char *)malloc(im->ibufsize)) == (unsigned char *)NULL)
		vorterror("openimage: unable to allocate space for image buffer.\n");

	if (!im->reading) {
		im->ibufp = im->ibuf;		/* initialise bufp to start */
		return(im);
	}

	if (read(im->fd, vers, VSTR_SIZE) != VSTR_SIZE)
		return((image *)NULL);

#ifndef VMS
#ifndef PC
	if (strncmp(vers, CMAGIC, CMAGIC_SIZE) == 0) {
		/* we have a compressed file, close im->fd,
		   do a popen, keep going */
		char cmd[256];
		sprintf (cmd, "uncompress < %s", name);
		if ((cin = popen (cmd, "r")) == (FILE *) NULL) {
			sprintf(buf, "Unable to open compressed file %s\n", name);
			vortwarning(buf);
			return((image *)NULL);
		}
		close (im->fd);

		im->fd = fileno(cin);
		im->f = cin;
		im->compressed = 1;
		
		if (read(im->fd, vers, VSTR_SIZE) != VSTR_SIZE) {
			pclose (cin);
			return((image *)NULL);
		}
	}
#endif
#endif

	if (strncmp(vers, VERSION_STR, VSTR_SIZE - 2) != 0) {
		sprintf(buf, "openimage: %s is not a vort file.\n", name);
		vortwarning(buf);
		return((image *)NULL);
	}

	readlongval(im->fd, &addr);		/* get start address */

	lseek(im->fd, addr, 0);			/* seek to first object */

	readvortobj(im->fd, &obj);

	setupimage(im, &obj);

	free(obj.ptr);

	im->ebufp = im->ibufp = im->ibuf + im->ibufsize;

	lseek(im->fd, im->imaddr, 0);		/* seek to start of image */

	return(im);
}

/*
 * makeimage
 *
 *	returns an image structure for image file fd. fd must be set up
 * for writing.
 */
image *makeimage(fd)
	int	fd;
{
	image	*im;
	int	flags;
	long	addr;
	v_object	obj;
	char	vers[VSTR_SIZE];
	char	buf[BUFSIZ];
	FILE	*cin;

	if (fd < 0)
		return((image *)NULL);

	if ((im = (image *)malloc(sizeof(image))) == (image *)NULL)
		return((image *)NULL);
	
	im->written = im->reading = 0;

	im->compressed = 0;

	imagetitle(im) = "";
	titlelength(im) = 0;
	imagefragment(im) = 0;

	im->fd = fd;

	im->ibufsize = ibufsize;

	if ((im->ibuf = (unsigned char *)malloc(im->ibufsize)) == (unsigned char *)NULL)
		vorterror("openimage: unable to allocate space for image buffer.\n");

	im->ibufp = im->ibuf;		/* initialise bufp to start */
	return(im);
}

/*
 * getimagedata
 *
 *	set the details in im with the image data in obj
 */
void getimagedata(im, obj)
	image		*im;
	v_object	*obj;
{
	unsigned char	*p;
	unsigned long	tmp;
	v_object	nxtobj;
	int		indx;

	if (obj->type != V_IMAGE)
		vorterror("getimagedata: object passed not an image.\n");

	imagetype(im) = 0;
	imagefragment(im) = 0;

	for (p = obj->ptr; p < &obj->ptr[obj->size]; p += p[1] + 2) {
		switch (*p) {
		case I_ADDR:		/* address of object */
			extractlongval(p + 1, &tmp);
			im->imaddr = tmp;
			break;
		case I_IMWIDTH:
			extractlongval(p + 1, &tmp);
			imagewidth(im) = tmp;
			break;
		case I_IMHEIGHT:
			extractlongval(p + 1, &tmp);
			imageheight(im) = tmp;
			break;
		case I_IMDEPTH:
			extractlongval(p + 1, &tmp);
			imagedepth(im) = tmp;
			break;
		case I_RED:
			imagetype(im) |= _R;
			extractlongval(p + 1, &tmp);	/* bits per channel ignored */
			break;
		case I_GREEN:
			imagetype(im) |= _G;
			extractlongval(p + 1, &tmp);	/* bits per channel ignored */
			break;
		case I_BLUE:
			imagetype(im) |= _B;
			extractlongval(p + 1, &tmp);	/* bits per channel ignored */
			break;
		case I_ALPHA:
			imagetype(im) |= _A;
			extractlongval(p + 1, &tmp);	/* bits per channel ignored */
			break;
		case I_BACKGND:				/* background colour */
			indx = 2;
			if (redchannel(im))
				imagebackgnd(im).r = p[indx++];
			if (greenchannel(im))
				imagebackgnd(im).g = p[indx++];
			if (bluechannel(im))
				imagebackgnd(im).b = p[indx++];
			break;
		case I_DATE:
			extractlongval(p + 1, &tmp);
			imagedate(im) = tmp;
			break;
		case I_COLORMAP:
			extractlongval(p + 1, &tmp);
			im->cmaddr = tmp;
			imagetype(im) |= _COLOR_MAP;
			lseek(im->fd, tmp, 0);
			readvortobj(im->fd, &nxtobj);
			getcolormapdata(im, &nxtobj);
			free(nxtobj.ptr);
			break;
		case I_RLE_CODED:
			imagetype(im) |= _RLE_CODED;
			extractlongval(p + 1, &tmp);	/* next value ignored */
			break;
		case I_XADDR:
			extractlongval(p + 1, &tmp);
			imagexaddr(im) = tmp;
			imagefragment(im) = 1;
			break;
		case I_YADDR:
			extractlongval(p + 1, &tmp);
			imageyaddr(im) = tmp;
			imagefragment(im) = 1;
			break;
		case I_ORIGWIDTH:
			extractlongval(p + 1, &tmp);
			imageorigwidth(im) = tmp;
			imagefragment(im) = 1;
			break;
		case I_ORIGHEIGHT:
			extractlongval(p + 1, &tmp);
			imageorigheight(im) = tmp;
			imagefragment(im) = 1;
			break;
		default:
			/* skip */;
		}
	}

}

/*
 * getcolormapdata
 *
 *	get the colourmap data out of obj for im
 */
void getcolormapdata(im, obj)
	image	*im;
	v_object	*obj;
{
	unsigned char	*p;
	long		tmp, addr;

	if (obj->type != V_COLORMAP)
		vorterror("getcolormapdata: object passed not a colourmap.\n");

	addr = 0;

	for (p = obj->ptr; p < &obj->ptr[obj->size]; p += p[1] + 2) {
		switch (*p) {
		case C_ADDR:
			extractlongval(p + 1, &addr);
			break;
		case C_SIZE:
			extractlongval(p + 1, &tmp);
			cmapsize(im) = tmp;
			break;
		case C_RED:
		case C_GREEN:
		case C_BLUE:
			/*
			 * assumed at the moment...
			 */
		default:
			/* skip */;
		}
	}
			
	lseek(im->fd, addr, 0);
	im->red = (unsigned char *)malloc(cmapsize(im));
	read(im->fd, im->red, cmapsize(im));
	im->green = (unsigned char *)malloc(cmapsize(im));
	read(im->fd, im->green, cmapsize(im));
	im->blue = (unsigned char *)malloc(cmapsize(im));
	read(im->fd, im->blue, cmapsize(im));
}

/*
 * gettextdata
 *
 *	set the text data in im with the text data in obj
 */
void gettextdata(im, obj)
	image	*im;
	v_object	*obj;
{
	unsigned char	*p;
	long		tmp, addr;

	if (obj->type != V_TEXT)
		vorterror("gettextdata: object passed not text.\n");

	addr = titlelength(im) = 0;

	for (p = obj->ptr; p < &obj->ptr[obj->size]; p += p[1] + 2) {
		switch (*p) {
		case T_ADDR:		/* address of text */
			extractlongval(p + 1, &addr);
			break;
		case T_LENGTH:		/* number of characters */
			extractlongval(p + 1, &tmp);
			titlelength(im) = tmp;
			break;
		default:
			/* skip */;
		}
	}

	if (titlelength(im) != 0) {
		if ((imagetitle(im) = (char *)malloc(titlelength(im))) != (char *)NULL) {
			lseek(im->fd, addr, 0);
			read(im->fd, imagetitle(im), titlelength(im));
		} else
			vorterror("gettextdata: unable to allocate space for string.\n");
	}
}

/*
 * setupimage
 *
 *	get the image details out of the object header
 */
void setupimage(im, obj)
	image		*im;
	v_object	*obj;
{
	long		addr;
	v_object	nxtobj;
	unsigned char	*p;
	
	if (obj->type != V_DIRECTORY)
		vorterror("setupimage: main object not a directory.\n");

	for (p = obj->ptr; p < &obj->ptr[obj->size]; p += p[1] + 2) {
		switch (*p) {
		case D_OBJECT:		/* address of object */
			extractlongval(p + 1, &addr);
			lseek(im->fd, addr, 0);
			readvortobj(im->fd, &nxtobj);
			switch (nxtobj.type) {
			case V_IMAGE:
				getimagedata(im, &nxtobj);
				free(nxtobj.ptr);
				break;
			case V_TEXT:
				gettextdata(im, &nxtobj);
				free(nxtobj.ptr);
				break;
			default:
				/* skip */;
			}
			break;
		default:
			/* skip */;
		}
	}
}

/*
 * readbyte
 *
 *	returns a byte from the image or EOF if the end is reached
 */
int readbyte(im)
	image		*im;
{
	int	count;

	if (im->ibufp == im->ebufp) {
		if ((count = read(im->fd, im->ibuf, im->ibufsize)) <= 0)
			return(EOF);
		im->ibufp = im->ibuf;
		im->ebufp = im->ibuf + count;
	}

	return(*im->ibufp++);
}

/*
 * writebyte
 *
 *	writes a byte to the image buffer, flushing it if it is full
 */
int writebyte(im, byte)
	image		*im;
	unsigned char	byte;
{
	im->written = 1;

	if (im->ibufp == im->ibuf + im->ibufsize) {
		if (write(im->fd, im->ibuf, im->ibufsize) <= 0)
			return(EOF);
		im->ibufp = im->ibuf;
	}

	*im->ibufp++ = byte;

	return(1);
}

/*
 * closeimage
 *
 *	close up an image file
 */
int closeimage(im)
	image		*im;
{
	int		fd;
	FILE		*f;
	int		compressed;
					/* flush buffers if neccessary */

	if (!im->reading && im->written) {
		if (write(im->fd, im->ibuf, im->ibufp - im->ibuf) == 0)
			return(EOF);
	}

	free(im->ibuf);

	if (titlelength(im) != 0)
		free(imagetitle(im));

	if (colormapped(im)) {
		free(redmap(im));
		free(greenmap(im));
		free(bluemap(im));
	}

	fd = im->fd;
	f = im->f;
	compressed = im->compressed;

	free(im);

#ifndef VMS
#ifndef PC
	if (compressed)
		return(pclose(f));
	else
#endif
#endif
		return(close(fd));
}

/*
 * finishimage
 *
 *	finishes writing and frees up an image file
 */
int finishimage(im)
	image		*im;
{
	int		fd;
	FILE		*f;
	int		compressed;
					/* flush buffers if neccessary */

	if (!im->reading && im->written) {
		if (write(im->fd, im->ibuf, im->ibufp - im->ibuf) == 0)
			return(EOF);
	}

	free(im->ibuf);

	if (titlelength(im) != 0)
		free(imagetitle(im));

	if (colormapped(im)) {
		free(redmap(im));
		free(greenmap(im));
		free(bluemap(im));
	}

	fd = im->fd;
	f = im->f;
	compressed = im->compressed;

	free(im);

	return 0;
}
