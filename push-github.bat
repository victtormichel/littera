@echo off
chcp 65001 > nul
echo ========================================================
echo  Littera - Publicação Automática no GitHub
echo ========================================================
powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0push-github.ps1" %*
pause
