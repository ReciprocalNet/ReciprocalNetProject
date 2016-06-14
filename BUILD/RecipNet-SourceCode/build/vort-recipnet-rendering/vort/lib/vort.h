/*
 * header details for pixel files
 */
#define	VSTR_SIZE	6
#define	VERSION_STR	"VORT01"

/*
 * compress magic numbers
 */

#define CMAGIC_SIZE	2
#define CMAGIC		"\037\235"	/* 1F 9D */

/*
 * tag values 
 */
#define	V_DIRECTORY	0	/* directory */

#define	D_PARENT	0	/* address of parent directory */
#define	D_NULL		1	/* empty entry */
#define	D_OBJECT	2	/* address of object in directory */

#define	V_IMAGE		1	/* image */

#define	I_ADDR		0	/* address of image in file */		
#define	I_IMWIDTH	1	/* image width in pixels */
#define	I_IMHEIGHT	2	/* image height in pixels */
#define	I_IMDEPTH	3	/* image depth in bits */
#define	I_RED		4	/* red channel present */
#define	I_GREEN		5	/* green channel present */
#define	I_BLUE		6	/* blue channel present */
#define	I_ALPHA		7	/* alpha channel present */
#define	I_BACKGND	8	/* background colour */
#define	I_DATE		9	/* creation date */
#define	I_COLORMAP	10	/* red, green, and blue channels in colourmap */
#define	I_RLE_CODED	11	/* image is run length encoded */
#define	I_XADDR		12	/* x coord of image if fragment */
#define	I_YADDR		13	/* y coord of image if fragment */
#define	I_ORIGWIDTH	14	/* width of whole image we are fragment of */
#define	I_ORIGHEIGHT	15	/* height of whole image we are fragment of */

#define	V_TEXT		2	/* text */

#define	T_ADDR		0	/* address of text in file */
#define	T_LENGTH	1	/* length of text in characters */

#define	V_COLORMAP	3	/* colourmap */

#define	C_ADDR		0	/* size of map */
#define	C_SIZE		1	/* size of each channel */
#define	C_RED		2	/* red channel present */
#define	C_GREEN		3	/* green channel present */
#define	C_BLUE		4	/* blue channel present */

/*
 * header details for pixel files
 */
#define	_BASE		0xffff0000	/* base of magic number */

/*
 * format types
 */
#define	_FLAT		0x0000		/* straight rasters */
#define	_RLE_CODED	0x1000		/* mixed run-length encoded file */
#define _COLOR_MAP	0x2000		/* colour mapped image */

/*
 * limits
 */
#define	PIX_MAXRUN	127		/* max run in run-length encoded */

/*
 * channel types
 */
#define	_R		0x0100		/* red channel */
#define	_G		0x0200		/* green channel */
#define	_B		0x0400		/* blue channel */
#define	_A		0x0800		/* alpha channel */

/*
 * magic numbers and their format structures
 *	- type order is the same as byte order
 */
#define	PIX_RGB		(_R | _G | _B)
#define	PIX_RLE		(_RLE_CODED | _R | _G | _B)
#define	PIX_RGBA	(_R | _G | _B | _A)
#define	PIX_RLEA	(_RLE_CODED | _R | _G | _B | _A)

/*
 * colour map type - currently max size 256 so each pixel is a single byte index
 */
#define	PIX_CMAP	(_COLOR_MAP | _R | _G | _B)
#define	PIX_RLECMAP	(_COLOR_MAP | _RLE_CODED | _R | _G | _B)
#define	PIX_ACMAP	(_COLOR_MAP | _R | _G | _B | _A)
#define	PIX_RLEACMAP	(_COLOR_MAP | _RLE_CODED | _R | _G | _B | _A)

/*
 * file name suffix
 */
#define	PIX_SUFFIX	".pix"

/*
 * VORT object structure
 */
typedef struct __vo {
	unsigned int	type;
	unsigned int	size;
	unsigned char	*ptr;
} v_object;

typedef struct __co {
	unsigned int	r, g, b;
} v_color;

/*
 * image structure details
 */

#define	IBUF_SIZE	4096			/* default size of image buffer */

typedef struct __i {
	int		fd;			/* file descriptor */
	int		compressed;		/* was this a compressed file */
	FILE		*f;			/* pointer to popened file */
	int		reading;		/* are we open for reading? */
	int		written;		/* written to the file? */
	int		fragment;		/* is this file a fragment */
	unsigned long	imaddr;			/* address of the image */
	unsigned long	cmaddr;			/* address of the colormap */
	int		magic;			/* magic number */
	unsigned short	pixw, pixh, pixd;	/* width and height in pixels */
	unsigned short	xaddr, yaddr;		/* x and y address of fragment */
	unsigned short	origwidth, origheight;	/* original height and width of image */
	long		date;			/* date created (secs from 1/1/70) */
	int		tlength;		/* length of title */
	char		*title;			/* image title */
	v_color		background;		/* background colour */
	unsigned char	run,			/* current position in run */
			r, g, b, a;		/* current pixel */
	int		ibufsize;		/* image buffer size - bytes */
	unsigned char	*ibuf;			/* pointer to image buffer */
	unsigned char	*ibufp;			/* pointer to buffer position */
	unsigned char	*ebufp;			/* pointer to end of buffer */
	unsigned short	mapsize;
	unsigned char	*red, *green, *blue;	/* colour map details */
} image;

/*
 * macros for checking and setting the type from the magic number
 */
#define	imagetype(a)		(a)->magic

#define	redchannel(a)		((a)->magic & _R)
#define	greenchannel(a)		((a)->magic & _G)
#define	bluechannel(a)		((a)->magic & _B)
#define	alphachannel(a)		((a)->magic & _A)

#define colormapped(a)		((a)->magic & _COLOR_MAP)

#define rlecoded(a)		((a)->magic & _RLE_CODED)

/*
 * function prototypes
 */

/* hdr.c */
void	copyheader (image *a, image *b);
void	setcmap (image *im, int numc, unsigned char *red, unsigned char *green,
		unsigned char *blue);
int	writeheader (image *im);

/* imageio.c */
int	getlongsize (unsigned long val);
int	readlongval (int fd, long *val);
int	writelongval (int fd, unsigned long val, int size);
void	extractlongval (unsigned char *ptr, long *val);
void	readvortobj (int fd, v_object *obj);
int	writetextobj (image *im);
int	writecolormapobj (image *im);
int	writeimageobj (image *im);
void	imagebufsize (int size);
image	*readimage (int fd);
image	*openimage (char *name, char *flag);
image	*makeimage (int fd);
void	getimagedata (image *im, v_object *obj);
void	getcolormapdata (image *im, v_object *obj);
void	gettextdata (image *im, v_object *obj);
void	setupimage (image *im, v_object *obj);
int	readbyte (image *im);
int	writebyte (image *im, int byte);
int	closeimage (image *im);
int	finishimage (image *im);

/* imagepos.c */
int	imagepos (image *im, int x, int y);

/* readmapl.c */
int	readmappedline (image *in, unsigned char *line);
int	readmappedaline (image *in, unsigned char *line, unsigned char *alpha);

/* readrgba.c */
int	readrgbline (image *in, unsigned char *red, unsigned char *green,
		unsigned char *blue);
int	readrgbaline (image *in, unsigned char *red, unsigned char *green,
		unsigned char *blue, unsigned char *alpha);

/* vorterr.c */
void	vorterror (char *s);
void	vortwarning (char *s);

/* writmapl.c */
int	writemappedline (image *out, unsigned char *line);
int	writemappedaline (image *out, unsigned char *line, unsigned char *alpha);

/* writrgba.c */
int	writergbline (image *out, unsigned char *red, unsigned char *green,
		unsigned char *blue);
int	writergbaline (image *out, unsigned char *red, unsigned char *green,
		unsigned char *blue, unsigned char *alpha);

/*
 * general macros for getting access to image details
 */
#define	imagewidth(a)	(a)->pixw
#define	imageheight(a)	(a)->pixh
#define	imagedepth(a)	(a)->pixd

#define	imagedate(a)	(a)->date

#define	imagefile(a)	(a)->fd

#define	imagetitle(a)	(a)->title
#define	titlelength(a)	(a)->tlength

#define	cmapsize(a)	(a)->mapsize

#define	redmap(a)	(a)->red
#define	greenmap(a)	(a)->green
#define	bluemap(a)	(a)->blue

#define	imagebackgnd(a)	(a)->background

/*
 * fragment information
 */
#define imagexaddr(a)		(a)->xaddr
#define imageyaddr(a)		(a)->yaddr

#define imageorigwidth(a)	(a)->origwidth
#define imageorigheight(a)	(a)->origheight

#define imagefragment(a)	(a)->fragment
