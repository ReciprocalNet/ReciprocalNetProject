#include <stdio.h>
#include "art.h"
#include "gram.h"

/*
 * with most ray tracers 24 is all the bits you get...
 */
#ifndef NOSILLYNESS
static struct bits {
	unsigned long	bit1:1,
			bit2:1,
			bit3:1,
			bit4:1,
			bit5:1,
			bit6:1,
			bit7:1,
			bit8:1,
			bit9:1,
			bit10:1,
			bit11:1,
			bit12:1,
			bit13:1,
			bit14:1,
			bit15:1,
			bit16:1,
			bit17:1,
			bit18:1,
			bit19:1,
			bit20:1,
			bit21:1,
			bit22:1,
			bit23:1,
			bit24:1,
			bit25:1;
} twentyfivemodemask;
#endif

/*
 * twentyfivebit
 *
 *	this turns the twentyfifth bit on and off. It's for when
 * 24 bits just isn't enough and you need that bit more...
 */
twentyfivebit(flag)
	int	flag;
{
#ifndef NOSILLYNESS
	twentyfivemodemask.bit25 = flag;
#endif
}
