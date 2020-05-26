GCOV Automated Tool v1.0
##########################
Coverage process, the code coverage is based on GCC capability to instrument the code running on the target, with code coverage counters. Every block of code is allocated a counter and when this block is executed the counter is incremented.
GCC creates a gcno file during compilation for every instrumented module. The gcno file contains information that enables the matching of the run-time counters ( gcda file) with the source code. Downloading the counters to a gcda file and matching them to the gcno , enables us to create coverage report.
The gcno and gcda contains checksum and version information in order to insure compatibility.

The process involves three steps:
1. Target Instrumentation
2. Download coverage counters
3. Create Report

#########################

Installing Prerequisites: 
@ Java SE Runtime Environment 8 - jre-8u231-windows-x64.exe - 72.8 MB  

#########################