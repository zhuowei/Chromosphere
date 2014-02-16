#!/bin/sh
FILETOPATCH="coronares/lib/armeabi-v7a/libcorona.so"
OFFSET=`objdump -T $FILETOPATCH |grep _ZN3Rtt7Runtime17VerifyApplicationEPKc|cut -b 1-8`
python patchit.py $FILETOPATCH $OFFSET "0100a0e3 1eff2fe1"
