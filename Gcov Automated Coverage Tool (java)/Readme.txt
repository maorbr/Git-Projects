GCOV Automated Tool v1.4
############################################################

Coverage process, the code coverage is based on GCC capability to instrument the code running on the target, with code coverage counters. Every block of code is allocated a counter and when this block is executed the counter is incremented.
GCC creates a gcno file during compilation for every instrumented module. The gcno file contains information that enables the matching of the run-time counters ( gcda file) with the source code. Downloading the counters to a gcda file and matching them to the gcno , enables us to create coverage report.
The gcno and gcda contains checksum and version information in order to insure compatibility.

The process involves three steps:
1. Target Instrumentation
2. Download coverage counters
3. Create Report

############################################################

[Installing Prerequisites]: 
@ Java SE Runtime Environment 8 - jre-8u231-windows-x64.exe - 72.8 MB 

############################################################

[User-Manual]:
1. Open ...\bin\gcovLib\smartSIM Gcov.pdf
2. Follow "Target Instrumentation" chapter to add Gcov support to Psoc project. 
3. Run "GcovAutomatedTool.exe" browse for project folder.
4. Select the source files for coverage process and press " --> ", the selected source files shall apper in Selected source file list.  
5. Fill configuration COM port and Baud.
6. Push "Create GCDA Files" button (the operation shall download the counters to a gcda file for all the selected sources files).
7. Push the "refresh" button (all gcda files shall fetch to list).
8. Select the relevent gcda file for each source file (note: for each source file select only one gcda at once).
9. Push "Execute" button - the operation shall create coverage report in new folder for each source file and merge folder as described below:

GCOV folder path: [Project Folder]\CortexM3\ARM_GCC_541\Debug\GCOV_[DATE]_[TIME_IN_MS] - contain the gcov file and gcda file with matching time stamp. 
MERGE folder path: [Project Folder]\CortexM3\ARM_GCC_541\Debug\GCOV_MERGE - contain the gcov file [SOURCE_FILE_NAME]_[PROJECT_VERSION].gcov 

For Eaxmple:
------------
C:\GCOV\NewSmartSim_GCOV\SmartSim.cydsn\CortexM3\ARM_GCC_541\Debug\GCOV_2020-07-06_1594037292575
C:\GCOV\NewSmartSim_GCOV\SmartSim.cydsn\CortexM3\ARM_GCC_541\Debug\GCOV_MERGE

10. Change project code coverage and repeat steps 6-9 - the operation shall create coverage report in new folder for each source file and merge the new selected file with the file in merge folder.

Note: each .gcov file on merge folder will contain in the end of the file time stamp for each runing of gcda as described below: 
 
$$$$$$$$	[ [GCDA_FILE] was merged at [DATE] [TIME] ; Coverage: [COVERGE] ]	$$$$$$$$

For Eaxmple:
------------
$$$$$$$$	[ main.gcda_1594025496 was merged at 2020-07-06 15:06:12 ; Coverage: 87% ]	$$$$$$$$
$$$$$$$$	[ main.gcda_1593960017 was merged at 2020-07-06 15:06:50 ; Coverage: 87% ]	$$$$$$$$
$$$$$$$$	[ main.gcda_1593960045 was merged at 2020-07-06 15:07:54 ; Coverage: 87% ]	$$$$$$$$

###########################################################################

[Development]:
1. Run Eclipse, press import and choose General->Existing Project into Workspace then select this path "...\Gcov Automated Tool\src\GcovAutomatedCoverageTool".
2. In Eclipse press right click on project folder and then press export choose Java->Runnable JAR file and on Export destination choose this path "...\Gcov Automated Tool\jar\GcovAutomatedTool.jar".
3. Run "Launch4j" press open and choose this file "...\Gcov Automated Tool\jar\exeMaker.xml" than on 'output file' field choose this path "C:\Users\e014241\Desktop\GCOV_Tool\Gcov Automated Tool\bin\GcovAutomatedTool.exe" and on 'Jar' field choose this path "...\Gcov Automated Tool\jar\GcovAutomatedTool.jar".
4. Press "Build Warpper" - .exe file shall created.


###########################################################################