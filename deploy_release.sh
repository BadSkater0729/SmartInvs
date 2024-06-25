#!/bin/bash

# Change to the directory where the script is located
cd "$(dirname "$0")" || exit

# Function to check if a command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Detect OS
if [[ "$OSTYPE" == "darwin"* ]]; then
    OS="macOS"
elif [[ "$OSTYPE" == "linux-gnu"* ]]; then
    OS="Linux"
else
    echo "Unsupported operating system. This script only works on macOS and Linux."
    exit 1
fi

# OS-specific package manager and installation commands
if [ "$OS" == "macOS" ]; then
    PKG_MANAGER="brew"
    INSTALL_CMD="brew install"
    if ! command_exists brew; then
        echo "Homebrew is not installed. Please install it first."
        echo "You can install Homebrew by running:"
        echo '/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"'
        exit 1
    fi
elif [ "$OS" == "Linux" ]; then
    if command_exists apt-get; then
        PKG_MANAGER="apt-get"
        INSTALL_CMD="sudo apt-get install -y"
    elif command_exists yum; then
        PKG_MANAGER="yum"
        INSTALL_CMD="sudo yum install -y"
    else
        echo "Unsupported Linux distribution. Please install the required packages manually."
        exit 1
    fi
fi

# Check for required commands
for cmd in mvn shasum md5sum; do
    if ! command_exists $cmd; then
        echo "$cmd is not installed. Attempting to install..."
        if [ "$OS" == "macOS" ]; then
            $INSTALL_CMD $cmd
        elif [ "$OS" == "Linux" ]; then
            case $cmd in
                mvn)
                    $INSTALL_CMD maven
                    ;;
                shasum)
                    $INSTALL_CMD perl
                    ;;
                md5sum)
                    # md5sum is usually pre-installed on Linux
                    echo "md5sum not found. Please install it manually."
                    exit 1
                    ;;
            esac
        fi
        if [ $? -ne 0 ]; then
            echo "Failed to install $cmd. Please install it manually."
            exit 1
        fi
    fi
done

# Ask user for JAR file location
read -p "Enter the full path to the JAR file: " jar_path
if [ ! -f "$jar_path" ]; then
    echo "Error: The specified JAR file does not exist."
    exit 1
fi

# Ask user for version
read -p "Enter the version number: " version

# Run Maven install command
mvn install:install-file \
    -DgroupId=fr.minuskube.inv \
    -DartifactId=folia-smart-invs \
    -Dversion="$version" \
    -Dfile="$jar_path" \
    -Dpackaging=jar \
    -DlocalRepositoryPath=. \
    -DcreateChecksum=true \
    -DgeneratePom=true

if [ $? -ne 0 ]; then
    echo "Maven install failed. Exiting."
    exit 1
fi

# Generate SHA1 and MD5 checksums
echo "Generating checksums..."
cd fr/minuskube/inv/folia-smart-invs/"$version"

for file in folia-smart-invs-"$version".{jar,pom}; do
    if [ "$OS" == "macOS" ]; then
        shasum -a 1 "$file" > "$file.sha1"
        md5 "$file" > "$file.md5"
    elif [ "$OS" == "Linux" ]; then
        sha1sum "$file" > "$file.sha1"
        md5sum "$file" > "$file.md5"
    fi
done

echo "Deployment complete. Files are ready to be committed and pushed to the repository."
