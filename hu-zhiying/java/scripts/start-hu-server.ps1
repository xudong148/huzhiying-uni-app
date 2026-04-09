param(
    [string]$Profile = 'test',
    [switch]$SkipBuild
)

$ErrorActionPreference = 'Stop'

$javaRoot = Resolve-Path (Join-Path $PSScriptRoot '..')
$serverRoot = Join-Path $javaRoot 'hu-server'

if (-not $SkipBuild) {
    Push-Location $javaRoot
    try {
        & mvn -q -pl hu-server -am -DskipTests package
        if ($LASTEXITCODE -ne 0) {
            exit $LASTEXITCODE
        }
    } finally {
        Pop-Location
    }
}

$jar = Get-ChildItem -Path (Join-Path $serverRoot 'target') -Filter '*.jar' |
    Where-Object { $_.Name -notlike 'original-*' } |
    Sort-Object LastWriteTime -Descending |
    Select-Object -First 1

if (-not $jar) {
    throw 'No runnable hu-server jar was found. Build the server first.'
}

& java "-Dspring.profiles.active=$Profile" -jar $jar.FullName
exit $LASTEXITCODE
