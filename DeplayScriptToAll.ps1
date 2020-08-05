# Powershell script to deplay all scripts in the script folder to Minecraft saves.

$ScriptsFolder = "survival_scripts"
$FullScriptsPath = [IO.Path]::Combine($PSScriptRoot, $ScriptsFolder)

$MinecraftScriptsFolder = "scripts"
$MinecraftSavesPath = [IO.Path]::Combine($env:APPDATA, ".minecraft\saves")

$MinecraftSavesFolders = Get-ChildItem -Directory $MinecraftSavesPath
$ScarpetScriptsPaths = @()
foreach($MinecraftFolder in $MinecraftSavesFolders)
{
    $FullScriptPath = [IO.Path]::Combine($MinecraftSavesPath, $MinecraftFolder, $MinecraftScriptsFolder)
    if (-not (Test-Path -LiteralPath $FullScriptPath)) {   
        try {
            # New-Item -Path $FullScriptPath -ItemType Directory -ErrorAction Stop | Out-Null #-Force
        }
        catch {
            Write-Error -Message "Unable to create scripts directory in world '$MinecraftFolder'. Error was: $_" -ErrorAction Stop
        }
        #"Successfully created scripts directory in world '$MinecraftFolder'."
        #$ScarpetScriptsPaths += $FullScriptPath
    
    }
    else {
        "Script directory already existed in world '$MinecraftFolder'."
        $ScarpetScriptsPaths += $FullScriptPath
    }
}

foreach($ScarpetScriptsPath in $ScarpetScriptsPaths)
{
    Get-ChildItem -Path $FullScriptsPath -Recurse -File | Copy-Item -Destination $ScarpetScriptsPath
}