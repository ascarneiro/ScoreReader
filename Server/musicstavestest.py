from gamera.core import *
from gamera.plugins import pil_io
from gamera.plugins import numpy_io
from gamera.toolkits.musicstaves import musicstaves_rl_simple
from numpy import fft

init_gamera()

#image = load_image("C:/Users/ascarneiro/Desktop/TCC/ScoreReader/img/score1.png")
image = load_image("C:/Users/ascarneiro/Desktop/TCC/ScoreReader/img/scale.png")
image = image.to_onebit()

ms = musicstaves_rl_simple.MusicStaves_rl_simple(image)
ms.remove_staves(crossing_symbols = 'bars')

staves = ms.get_staffpos()
for staff in staves:
    print "Staff %d has %d staves:" % (staff.staffno, len(staff.yposlist))
    for index, y in enumerate(staff.yposlist):
        print "    %d. line at y-position:" % (index+1), y

ms.image.image_save('C:/nostaves.tif', 'TIFF')
nparr = ms.image.to_numpy()
fourarr = fft.fft2(nparr)
fourarr[fourarr > 0] = 1
fourarr[fourarr < 0] = 0
fourimage = numpy_io.from_numpy(fourarr)

#ver nested_list_to_image




print(fourarr)

#print( im)
# alternative without copying:  no_staves_img = ms.image