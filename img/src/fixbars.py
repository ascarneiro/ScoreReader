#!/usr/bin/python
# -*- mode: python; indent-tabs-mode: nil; tab-width: 4 -*-
# vim: set tabstop=4 shiftwidth=4 expandtab:

##############################################################################
# fixbars.py
#   - fixes too short bar lines in some programs
#
# History:
#   2005-09-21  Christoph Dalitz  first creation
##############################################################################

import sys
import os
import re

# supported creators as [name, PSCreatorCode, homepage URL, -m supported]
creators = [ \
#	["abc2ps", "abc2ps", "http://www.ihp-ffo.de/~msm/", "yes"], \ # (untested)
	["abcm2ps", "abcm2ps", "http://moinejf.free.fr/", "yes"], \
	["abctab2ps", "abctab2ps", "http://www.lautengesellschaft.de/cdmm/", "yes"], \
	["lilypond-2.2", "does not set DSC comment", "http://www.lilypond.org/web/", "yes"], \
	["lilypond-2.8", "LilyPond 2\.8", "http://www.lilypond.org/web/", "yes"], \
	["musixtex", "does not set DSC comment", "http://www.ctan.org/tex-archive/macros/musixtex/taupin/", "yes"], \
	["mup", "Mup", "http://www.arkkra.com/", "yes"], \
	["pmw", "Philip's Music", "http://www.quercite.com/pmw.html", "yes"], \
	["tab", "tab, ", "http://www.cs.dartmouth.edu/~wbc/lute/AboutTab.html", "yes"], \
	["ps2ps", "Ghostscript.*(pswrite)", "http://www.cs.wisc.edu/~ghost/", "yes"], \
	["acrobat", "PScript.*\.dll|PSCRIPT\.DRV|PDFCreator ", "Acrobat Distiller", "yes"] \
]

#
# parse command line arguments
#
class c_opt:
    infile = ""
    outfile = ""
    creator = ""
    markred = False
    compressed = False
    def error_exit(self):
        usage = "Usage:\n" + \
                "\t" + sys.argv[0] + " [options] <infile>\n" + \
                "Options:\n" + \
                "\t-o outfile output file name. When omitted, the\n" + \
                "\t           infile stem gets the extension '-nostaff'\n" + \
                "\t           (or '-redstaff' for option -m)\n" + \
                "\t-c creator creater of the postscript file.\n" + \
                "\t           Use '-h' for a list of supported creators\n" + \
                "\t           When omitted, the value is guessed from the\n" + \
                "\t-z         infile is compressed with gzip\n" + \
                "\t           (automatically set when infile ends with \".gz\")\n" + \
                "\t-h         print supported creators and exit\n"
        sys.stderr.write(usage)
        sys.exit(1)
    def supported_creators(self, clist):
        ret = sys.argv[0] + \
              " supported creators as options for '-c':\n\n" + \
              "\t%-15s (homepage)\n" % "(name)"
        for c in clist:
            ret += ("\t%-15s " % c[0]) + c[2] + "\n"
        ret += "\nFor other creators try to process the PS or PDF file\n" + \
               "with ps2ps or pdf2ps and then use the creator option ps2ps\n"
        return ret
opt = c_opt()

# parse command line
i = 1
while i < len(sys.argv):
    if sys.argv[i] == "-o":
        i += 1; opt.outfile = sys.argv[i]
    elif sys.argv[i] == '-c':
        i += 1; opt.creator = sys.argv[i]
        knowncreator = 0
        for c in creators:
            if c[0] == opt.creator:
                knowncreator = 1
                break
        if not knowncreator:
            sys.stderr.write("Unsupported creator '" + opt.creator + "'\n")
            sys.stderr.write(opt.supported_creators(creators))
            sys.exit(1)
    elif sys.argv[i] == '-z':
        opt.compressed = True
    elif sys.argv[i] == '-h':
        print opt.supported_creators(creators)
        sys.exit(0)
    elif sys.argv[i][0] == '-':
        opt.error_exit()
    else:
        opt.infile = sys.argv[i]
    i += 1

if (opt.infile == "") or not os.path.isfile(opt.infile):
    sys.stderr.write("File '" + opt.infile + "' not found.\n")
    opt.error_exit()
if (opt.outfile == ""):
    postfix = "-fixedbars"
    segs = opt.infile.split(".")
    if len(segs) > 1:
        if segs[-1] == "gz":
            del segs[-1]
        segs[-2] += postfix
        opt.outfile = ".".join(segs)
    else:
        opt.outfile = opt.infile + postfix

# try to autodetect whether infile is compressed
if opt.infile.endswith(".gz"):
    opt.compressed = True

# read entire file into memory
# this is not strictly necessary for all creators, but simplifies things
if opt.compressed:
    fin = os.popen("gunzip -c '" + opt.infile + "'", 'r')
else:
    fin = open(opt.infile,'r')
lines = fin.readlines()
fin.close()

# try to guess creator
if (opt.creator == ""):
    couldbelilypond22 = 0
    couldbemusixtex = 0
    for x in lines:
        if x.startswith("%%Creator:"):
            for c in creators:
                if re.search(c[1],x[10:]):
                    opt.creator = c[0]
                    break
                elif re.search("dvips",x[10:]):
                    couldbelilypond22 = 1
                    couldbemusixtex = 1
                    break
            if (opt.creator != ""):
                break
        if couldbelilypond22 and \
           (x.startswith("/lyscale") or x.startswith("/lilypond")):
            opt.creator = "lilypond-2.2"
            break
        if couldbemusixtex and x.startswith("%DVIPSSource:  TeX"):
            opt.creator = "musixtex"
            break
    if (opt.creator == ""):
        sys.stderr.write("Cannot identify PS creator. Use option '-f'\n")
        sys.stderr.write(opt.supported_creators(creators))
        sys.exit(2)
    else:
        sys.stdout.write("Creator autoguessed as " + opt.creator + "\n")

# when option -m, check whether creator supports this
if opt.markred:
    for c in creators:
        if opt.creator == c[0] and c[3] == "no":
            sys.stderr.write("Option -m not supported for creator " + \
                             opt.creator + "\n")
            sys.exit(1)

# file for modified output
print "Write output to " + opt.outfile
fout = open(opt.outfile,'w')

# abctab2ps
# draw bars somewhat longer
if (opt.creator == "abctab2ps"):
    lineno = 0
    linestoskip = 21
    for x in lines:
        if lineno > 0 and lineno < linestoskip:
            lineno += 1
            continue
        if x.startswith("/bar "):
            lineno = 1
            # write replacement macros
            fout.write("/bar { %% x bar - single bar\n\
  -0.34 moveto  0 24.68 rlineto stroke\n\
} bind def\n\
\n\
/dbar { %% x dbar - thin double bar\n\
   -0.34 moveto 0 24.68 rlineto -3 -24.68 rmoveto\n\
   0 24.68 rlineto stroke\n\
} bind def\n\
\n\
/fbar1 { %% x fbar1 - fat double bar at start\n\
  -0.34 moveto  0 24.68 rlineto 3 0 rlineto 0 -24.68 rlineto \n\
  currentpoint fill moveto\n\
  3 0 rmoveto 0 24.68 rlineto stroke\n\
} bind def\n\
\n\
/fbar2 { %% x fbar2 - fat double bar at end\n\
  -0.34 moveto  0 24.68 rlineto -3 0 rlineto 0 -24.68 rlineto \n\
  currentpoint fill moveto\n\
  -3 0 rmoveto 0 24.68 rlineto stroke\n\
} bind def\n\
\n")
        elif x.startswith("/tabbar "):
            lineno = 1
            # write replacement macros
            fout.write("/tabbar { %% x h tabbar - single bar in tab\n\
  0.68 add exch -0.34 moveto 0 exch rlineto stroke\n\
} bind def\n\
\n\
/tabdbar { %% x h tabdbar - thin double bar in tab\n\
  0.68 add exch -0.34 moveto dup 0 exch rlineto dup -3 exch neg rmoveto\n\
  0 exch rlineto stroke\n\
} bind def\n\
\n\
/tabfbar1 { %% x h tabfbar1 - fat double bar at start\n\
  0.68 add exch -0.34 moveto dup 0 exch rlineto 3 0 rlineto \n\
  dup 0 exch neg rlineto currentpoint fill moveto\n\
  3 0 rmoveto dup 0 exch rlineto stroke\n\
} bind def\n\
\n\
/tabfbar2 { %% x tabfbar2 - fat double bar at end\n\
  0.68 add exch -0.34 moveto dup 0 exch rlineto -3 0 rlineto \n\
  dup 0 exch neg rlineto currentpoint fill moveto\n\
  -3 0 rmoveto dup 0 exch rlineto stroke\n\
} bind def\n\
\n")
        else:
            fout.write(x)
            


# cleanup
fout.close()

