@echo off
chcp 65001 >nul
title ZISWAF Digital Counter - Fun Java IT Incubation 2026
echo ========================================================================
echo    KOMUNITAS FUN JAVA - JURUSAN TEKNIK INFORMATIKA FST UIN MALANG
echo        ZISWAF DIGITAL COUNTER (Kalkulator & Kasir Syariah v3.0)
echo ========================================================================
echo.
echo Mengompilasi source code Java...
if not exist bin mkdir bin

javac -d bin -encoding UTF-8 src\funjava\model\*.java src\funjava\storage\*.java src\funjava\util\*.java src\funjava\view\*.java src\funjava\*.java

if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Kompilasi gagal! Periksa instalasi JDK Anda.
    pause
    exit /b %ERRORLEVEL%
)

echo [OK] Kompilasi berhasil! Membuka GUI Aplikasi Kasir ZISWAF...
echo.
start javaw -cp bin funjava.ZiswafApp
exit /b 0
