# Script Runner PowerShell untuk ZISWAF Digital Counter
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8

Write-Host "========================================================================" -ForegroundColor DarkYellow
Write-Host "   KOMUNITAS FUN JAVA - JURUSAN TEKNIK INFORMATIKA FST UIN MALANG" -ForegroundColor Yellow
Write-Host "       ZISWAF DIGITAL COUNTER (Kalkulator & Kasir Syariah v3.0)" -ForegroundColor Green
Write-Host "========================================================================" -ForegroundColor DarkYellow

if (!(Test-Path "bin")) {
    New-Item -ItemType Directory -Path "bin" | Out-Null
}

Write-Host "`n[1/2] Mengompilasi berkas Java..." -ForegroundColor Cyan
$sources = Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { $_.FullName }
& javac -d bin -encoding UTF-8 $sources

if ($LASTEXITCODE -eq 0) {
    Write-Host "[OK] Kompilasi Berhasil!" -ForegroundColor Green
    Write-Host "[2/2] Menjalankan Aplikasi ZISWAF Digital Counter..." -ForegroundColor Cyan
    Start-Process -FilePath "javaw" -ArgumentList "-cp bin funjava.ZiswafApp"
} else {
    Write-Host "[ERROR] Kompilasi gagal! Periksa sintaks kode Java." -ForegroundColor Red
}
