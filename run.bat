@echo off
chcp 65001 > nul
echo ========================================================
echo  Littera - Inicializando Servidor da Aplicacao
echo ========================================================
powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0run.ps1"
if %ERRORLEVEL% NEQ 0 (
    echo [ERRO] Falha ao executar o servidor.
    pause
    exit /b %ERRORLEVEL%
)
