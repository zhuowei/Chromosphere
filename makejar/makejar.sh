rm corona.jar MyCoronaActivity-signed-aligned_dex2jar.jar coronafile.jar
rm -r com

dex2jar MyCoronaActivity-signed-aligned.apk
7z x MyCoronaActivity-signed-aligned_dex2jar.jar com/ansca/corona/storage/FileServices.class
7z d MyCoronaActivity-signed-aligned_dex2jar.jar com/ansca/corona/storage/FileServices.class
7z d MyCoronaActivity-signed-aligned_dex2jar.jar android
7z a coronafile.jar com/ansca/corona/storage/FileServices.class
java -jar jarjar-1.4.jar process jarjar_rules.txt MyCoronaActivity-signed-aligned_dex2jar.jar corona.jar
rm -r coronares
apktool d MyCoronaActivity-signed-aligned.apk coronares
cp -r coronares/res ../coronalib/
cp -r coronares/lib/* ../coronalib/libs/
cp corona.jar ../coronalib/libs/
cp coronafile.jar ../coronalib/libs/
