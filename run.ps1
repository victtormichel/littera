# ====================================================================
# Script de Execução do Servidor Littera (PowerShell)
# ====================================================================

$ErrorActionPreference = "Stop"

if (!(Test-Path "target\classes\com\littera\ServerLauncher.class")) {
    Write-Host ">>> Projeto ainda não compilado. Executando build.ps1 primeiro..." -ForegroundColor Yellow
    & ".\build.ps1"
}

$java = "java"
if (Test-Path "C:\Program Files\Java\jdk-22\bin\java.exe") {
    $java = "C:\Program Files\Java\jdk-22\bin\java.exe"
}

$libJars = (Get-ChildItem "lib\*.jar" | ForEach-Object { $_.FullName }) -join ";"
$classpath = "target\classes;$libJars"

Write-Host ">>> Iniciando Littera ServerLauncher..." -ForegroundColor Cyan

# Executa o servidor embutido
& $java "-Dfile.encoding=UTF-8" -cp $classpath com.littera.ServerLauncher
