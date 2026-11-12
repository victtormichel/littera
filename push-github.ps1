# ====================================================================
# Script de Publicação no GitHub (PowerShell)
# ====================================================================

param(
    [string]$repoUrl
)

$ErrorActionPreference = "Continue"

Write-Host "`n========================================================" -ForegroundColor Cyan
Write-Host "         LITTERA - PUBLICACAO NO GITHUB                 " -ForegroundColor Cyan
Write-Host "========================================================`n" -ForegroundColor Cyan

if (-not $repoUrl -or $repoUrl.Trim() -eq "") {
    Write-Host "Passo 1: Crie um repositorio publico no GitHub em: https://github.com/new" -ForegroundColor Yellow
    Write-Host "         (Nao marque 'Add a README file', pois ja temos um completo)`n"
    $repoUrl = Read-Host "Cole aqui a URL HTTPS do seu repositorio (ex: https://github.com/seu-usuario/littera.git)"
}

if (-not $repoUrl -or $repoUrl.Trim() -eq "") {
    Write-Host "[ERRO] Nenhuma URL informada. Operacao cancelada." -ForegroundColor Red
    exit 1
}

$repoUrl = $repoUrl.Trim()

Write-Host "`n>>> Configurando remote 'origin' para: $repoUrl" -ForegroundColor Cyan
git remote remove origin 2>$null
git remote add origin $repoUrl
git branch -M main

Write-Host ">>> Enviando codigo-fonte para o branch 'main'..." -ForegroundColor Cyan
git push -u origin main

if ($LASTEXITCODE -eq 0) {
    Write-Host "`n>>> [SUCESSO] Projeto publicado com exito no GitHub!" -ForegroundColor Green
    Write-Host ">>> Acesse seu repositorio em: $repoUrl`n" -ForegroundColor Green
} else {
    Write-Host "`n>>> [ATENCAO] Ocorreu uma pendencia de autenticacao ou conexao." -ForegroundColor Yellow
    Write-Host "    Verifique se voce tem permissao no repositorio e tente novamente.`n"
}
