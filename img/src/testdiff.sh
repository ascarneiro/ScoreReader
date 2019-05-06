#!/bin/sh

#
# testdiff.sh - highlight differences in result of staffremoval
#
# Author:  Christoph Dalitz, Hochschule Niederrhein
# Version: 1.0, 2006.09.21
#

# config options
STAFFREM4PS=staffrem4ps
PS2PNG=ps2png
OUTFILE=diff.png
PNGVIEWER=gqview

# command line arguments
DPI=300
INFILE=""
PGMNAME="`basename $0`"
USAGEMSG="$PGMNAME <psfile>"

while [ $# -gt 0 ]
do
	case "$1" in
		-\?)   echo -e "$USAGEMSG"; exit 0;;
		*)     INFILE="$1";;
	esac
	shift
done
if [ -z "$INFILE" -o ! -r "$INFILE" ]
then
	echo -e "$USAGEMSG" 1>&2
	exit 1
fi
NOSTAFFFILE=`echo $INFILE | sed 's/\.[^.]*$//'`-nostaff.ps

# create images
$STAFFREM4PS $INFILE
$PS2PNG -dpi $DPI $INFILE
$PS2PNG -dpi $DPI $NOSTAFFFILE

# make differences visible
INFILE=`echo $INFILE | sed 's/\.ps/.png/'`
NOSTAFFFILE=`echo $NOSTAFFFILE | sed 's/\.ps/.png/'`
python <<EOF
from gamera.core import *
init_gamera()
full = load_image("$INFILE")
full = full.to_rgb()
nostaff = load_image("$NOSTAFFFILE")
nostaff=nostaff.to_onebit()
full.highlight(nostaff,RGBPixel(255,0,0))
full.save_PNG("$OUTFILE")
EOF

# show file
$PNGVIEWER $OUTFILE &
