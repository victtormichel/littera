# ====================================================================
# Script de Compilação, Testes e Empacotamento do Littera (PowerShell)
# ====================================================================

$ErrorActionPreference = "Stop"

Write-Host "`n>>> [LITTERA] Iniciando processo de compilação..." -ForegroundColor Cyan

# 1. Localização do Java Development Kit (JDK)
$javac = "javac"
if (Test-Path "C:\Program Files\Java\jdk-22\bin\javac.exe") {
    $javac = "C:\Program Files\Java\jdk-22\bin\javac.exe"
}

$java = "java"
if (Test-Path "C:\Program Files\Java\jdk-22\bin\java.exe") {
    $java = "C:\Program Files\Java\jdk-22\bin\java.exe"
}

Write-Host ">>> Utilizando compilador: $javac"

# 2. Criação das pastas de saída
$classesDir = "target\classes"
$testClassesDir = "target\test-classes"
$distDir = "target"

if (!(Test-Path $classesDir)) { New-Item -ItemType Directory -Force -Path $classesDir | Out-Null }
if (!(Test-Path $testClassesDir)) { New-Item -ItemType Directory -Force -Path $testClassesDir | Out-Null }

# 3. Cópia dos recursos (db.properties, scripts SQL)
Write-Host ">>> Copiando recursos para target\classes..."
Copy-Item -Path "src\main\resources\*" -Destination $classesDir -Recurse -Force

# 4. Construção do Classpath com as bibliotecas em lib/
$libJars = (Get-ChildItem "lib\*.jar" | ForEach-Object { $_.FullName }) -join ";"

# 5. Compilação do Código Fonte Principal
Write-Host ">>> Compilando código-fonte principal (src\main\java)..."
$mainSources = Get-ChildItem -Path "src\main\java" -Filter "*.java" -Recurse | ForEach-Object { $_.FullName }

& $javac -encoding UTF-8 -cp "$libJars" -d $classesDir $mainSources
if ($LASTEXITCODE -ne 0) {
    Write-Error "Falha na compilação do código-fonte principal."
    exit 1
}
Write-Host ">>> [OK] Código principal compilado com sucesso." -ForegroundColor Green

# 6. Compilação dos Testes Unitários
Write-Host ">>> Compilando testes unitários (src\test\java)..."
$testSources = Get-ChildItem -Path "src\test\java" -Filter "*.java" -Recurse | ForEach-Object { $_.FullName }

& $javac -encoding UTF-8 -cp "$classesDir;$libJars" -d $testClassesDir $testSources
if ($LASTEXITCODE -ne 0) {
    Write-Error "Falha na compilação dos testes."
    exit 1
}
Write-Host ">>> [OK] Testes compilados com sucesso." -ForegroundColor Green

# 7. Execução dos Testes com JUnit Platform Console Standalone
Write-Host "`n>>> Executando Testes Unitários com JUnit 5..." -ForegroundColor Cyan
$junitConsoleJar = Get-ChildItem "lib\junit-platform-console-standalone*.jar" | Select-Object -First 1 -ExpandProperty FullName

& $java "-Dfile.encoding=UTF-8" -jar $junitConsoleJar execute `
    --class-path "$classesDir;$testClassesDir;$libJars" `
    --scan-class-path `
    --details=summary

if ($LASTEXITCODE -ne 0) {
    Write-Warning "Alguns testes falharam. Verifique a saída acima."
} else {
    Write-Host ">>> [OK] Todos os testes unitários passaram com sucesso!" -ForegroundColor Green
}

# 8. Empacotamento do arquivo WAR
Write-Host "`n>>> Criando arquivo WAR (littera.war)..." -ForegroundColor Cyan
$warStaging = "target\war-staging"
if (Test-Path $warStaging) { Remove-Item -Recurse -Force $warStaging }
New-Item -ItemType Directory -Force -Path "$warStaging\WEB-INF\classes" | Out-Null
New-Item -ItemType Directory -Force -Path "$warStaging\WEB-INF\lib" | Out-Null

# Copia arquivos estáticos da web
Copy-Item -Path "src\main\webapp\*" -Destination $warStaging -Recurse -Force
# Copia classes compiladas
Copy-Item -Path "$classesDir\*" -Destination "$warStaging\WEB-INF\classes" -Recurse -Force
# Copia jars necessários
Copy-Item -Path "lib\sqlite-jdbc*.jar" -Destination "$warStaging\WEB-INF\lib" -Force
Copy-Item -Path "lib\mysql-connector-j*.jar" -Destination "$warStaging\WEB-INF\lib" -Force
Copy-Item -Path "lib\jakarta.servlet.jsp.jstl*.jar" -Destination "$warStaging\WEB-INF\lib" -Force
Copy-Item -Path "lib\slf4j*.jar" -Destination "$warStaging\WEB-INF\lib" -Force

$jarTool = "jar"
if (Test-Path "C:\Program Files\Java\jdk-22\bin\jar.exe") {
    $jarTool = "C:\Program Files\Java\jdk-22\bin\jar.exe"
}

Push-Location $warStaging
& $jarTool -cf "..\littera.war" *
Pop-Location

Write-Host ">>> [OK] Pacote target\littera.war criado com sucesso!" -ForegroundColor Green
Write-Host ">>> Build concluído com êxito!`n" -ForegroundColor Green
