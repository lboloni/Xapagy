@rem echo %0
@set CURRENTDIR=%~dp0
@set JAVA=java
@set XAPAGYCLASSPATH=%CURRENTDIR%/bin
@set XAPAGYLIBPATH=%CURRENTDIR%/../XapagyLib
@set XAPAGYLIBS=%XAPAGYLIBPATH%\jfreechart-1.0.15;%XAPAGYLIBPATH%\jcommon-1.0.18
@%JAVA%  -classpath %XAPAGYCLASSPATH%;%XAPAGYLIBS% com.xapagy.Xapagy %* 