
SET numOfKtails=8
SET numOfModelsTests=10
setlocal enabledelayedexpansion enableextensions
Setlocal EnableDelayedExpansion

set list=Columba Heretix JArgs Jeti lucane OpenHospital RapidMiner tagsoup Thingamablog java.util.Formatter java.util.StringTokenizer java.net.DatagramSocket java.net.MultiCastSocket java.net.Socket java.net.URL
			
(for %%a in (%list%) do (
	echo %%a
	FOR /L %%P IN (1,1,%numOfModelsTests%) DO (
	FOR /L %%P IN (1,1,9) DO ( 
		SET /a R=%%P
		start /wait java -jar modelGenerator.jar CreateModel %%a !R! 2 1
		
		REM FOR /L %%G IN (1,1,%numOfKtails%) DO ( 
		REM	start /wait java -jar modelGenerator.jar Testktails %%a !R! 2 1 1
		REM )
		FOR /L %%G IN (1,1,%numOfKtails%) DO ( 
			start /wait java -jar modelGenerator.jar Testktails %%a !R! 2 2 1
		)
	)
	)
REM FOR /L %%Z IN (2,2,8) DO (
	REM   numofstate = 0 numoflogs = %%Z numOfOrig = %%Z
REM	SET /a R=%%Z
REM	FOR /L %%P IN (1,1,%numOfModelsTests%) DO ( 
REM	start /wait java -jar modelGenerator.jar CreateModel %%a 0 %%Z !R!
REM	REM   numofstate = 0 numoflogs = %%Z k = 1 numOfOrig = %%Z 
REM	FOR /L %%G IN (1,1,%numOfKtails%) DO ( 
REM	start /wait java -jar modelGenerator.jar Testktails %%a 0 %%Z 1 !R!
REM	)
REM	REM   numofstate = 0 numoflogs = %%Z  k = 2 numOfOrig = %%Z 
REM	FOR /L %%G IN (1,1,%numOfKtails%) DO ( 
REM	start /wait java -jar modelGenerator.jar Testktails %%a 0 %%Z 2 !R!
REM	)
REM	)
	
	REM   numofstate = 0 numoflogs = %%Z numOfOrig = %%Z -1
REM	SET /a R=%%Z-1
REM	FOR /L %%P IN (1,1,%numOfModelsTests%) DO ( 
REM	start /wait java -jar modelGenerator.jar CreateModel %%a 1 %%Z !R!
REM	REM   numofstate = 0 numoflogs = %%Z k = 1 numOfOrig = %%Z 
REM	FOR /L %%G IN (1,1,%numOfKtails%) DO ( 
REM	start /wait java -jar modelGenerator.jar Testktails %%a 1 %%Z 1 !R!
REM	)
	REM   numofstate = 0 numoflogs = %%Z  k = 2 numOfOrig = %%Z 
REM	FOR /L %%G IN (1,1,%numOfKtails%) DO ( 
REM	start /wait java -jar modelGenerator.jar Testktails %%a 1 %%Z 2 !R!
REM	)
REM	)
	
	REM   numofstate = 0 numoflogs = %%Z numOfOrig = %%Z -1
REM	SET /a R=%%Z
REM	FOR /L %%P IN (1,1,%numOfModelsTests%) DO ( 
REM	start /wait java -jar modelGenerator.jar CreateModelRecursive %%a 1 %%Z 1
	REM   numofstate = 0 numoflogs = %%Z k = 1 numOfOrig = %%Z 
REM	FOR /L %%G IN (1,1,%numOfKtails%) DO ( 
REM	start /wait java -jar modelGenerator.jar Testktails %%a 1 %%Z 1 1 true
REM	)
	REM   numofstate = 0 numoflogs = %%Z  k = 2 numOfOrig = %%Z 
REM	FOR /L %%G IN (1,1,%numOfKtails%) DO ( 
REM	start /wait java -jar modelGenerator.jar Testktails %%a 1 %%Z 2 1 true
REM	)
REM	)
REM )

)) 

