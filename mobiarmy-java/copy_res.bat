@echo off
echo Copying res and data directories...
xcopy "c:\Users\Admin\Desktop\Mobiarmy\mobiarmy_buinhulac\mobiarmy_buinhulac\res" "c:\Users\Admin\Desktop\Mobiarmy\mobiarmy-java\res\" /E /I /Y
xcopy "c:\Users\Admin\Desktop\Mobiarmy\mobiarmy_buinhulac\mobiarmy_buinhulac\data" "c:\Users\Admin\Desktop\Mobiarmy\mobiarmy-java\data\" /E /I /Y
echo Done!
pause
