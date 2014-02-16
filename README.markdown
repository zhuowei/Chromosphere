A launcher for Corona-based games. Mainly designed for on-device development.

- app is the actual application.
- coronalib is the library that contains a stripped down version of the Corona runtime. Because of legalities, this ships as an empty library.
- makejar is the script that generates coronalib. It requires dex2jar, 7zip, apktool (and jarjar, which is shipped), and of course a Corona-based
application.
- carlib is a library to read and write Corona ARchive files.

