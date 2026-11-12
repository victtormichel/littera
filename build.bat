@echo off
chcp 65001 > nul
echo ========================================================
echo  Littera - Build e Testes Automatizados
echo ========================================================
powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0build.ps1"
if %ERRORLEVEL% NEQ 0 (
    echo [ERRO] O build falhou.
    pause
    exit /b %ERRORLEVEL%
)
echo [SUCESSO] Build concluído com êxito!
