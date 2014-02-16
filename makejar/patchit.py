from __future__ import print_function

import sys
with open(sys.argv[1], "rb") as infile:
	infilearr = bytearray(infile.read())
inptr = int(sys.argv[2], 16)
optstr = sys.argv[3].strip().replace(" ", "")
for i in range(0, len(optstr), 2):
	infilearr[inptr] = int(optstr[i:i+2], 16)
	inptr += 1
with open(sys.argv[1], "wb") as outfile:
	outfile.write(infilearr)

