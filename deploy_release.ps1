# Change to the directory where the script is located
Set-Location -Path $PSScriptRoot

# Function to check if a command exists
function Test-Command($cmdname) {
    return [bool](Get-Command -Name $cmdname -ErrorAction SilentlyContinue)
}

# Check if Chocolatey is installed
if (-not (Test-Command choco)) {
    Write-Host "Chocolatey is not installed. Please install it first."
    Write-Host "You can install Chocolatey by running the following command in an elevated PowerShell:"
    Write-Host "Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))"
    exit 1
}

# Check for required commands
$commands = @("mvn", "certutil")
foreach ($cmd in $commands) {
    if (-not (Test-Command $cmd)) {
        Write-Host "$cmd is not installed. Attempting to install via Chocolatey..."
        choco install $cmd -y
        if ($LASTEXITCODE -ne 0) {
            Write-Host "Failed to install $cmd. Please install it manually."
            exit 1
        }
    }
}

# Ask user for JAR file location
$jar_path = Read-Host -Prompt "Enter the full path to the JAR file"
if (-not (Test-Path $jar_path -PathType Leaf)) {
    Write-Host "Error: The specified JAR file does not exist."
    exit 1
}

# Ask user for version
$version = Read-Host -Prompt "Enter the version number"

# Run Maven install command
mvn install:install-file `
    "-DgroupId=com.badskater0729.yamltranslator" `
    "-DartifactId=YAMLTranslator" `
    "-Dversion=$version" `
    "-Dfile=$jar_path" `
    "-Dpackaging=jar" `
    "-DlocalRepositoryPath=." `
    "-DcreateChecksum=true" `
    "-DgeneratePom=true"

if ($LASTEXITCODE -ne 0) {
    Write-Host "Maven install failed. Exiting."
    exit 1
}

# Generate SHA1 and MD5 checksums
Write-Host "Generating checksums..."
Set-Location -Path "com\badskater0729\yamltranslator\YAMLTranslator\$version"

$files = @("YAMLTranslator-$version.jar", "YAMLTranslator-$version.pom")
foreach ($file in $files) {
    certutil -hashfile $file SHA1 | Select-Object -Last 1 | Out-File -FilePath "$file.sha1"
    certutil -hashfile $file MD5 | Select-Object -Last 1 | Out-File -FilePath "$file.md5"
}

Write-Host "Deployment complete. Files are ready to be committed and pushed to the repository."