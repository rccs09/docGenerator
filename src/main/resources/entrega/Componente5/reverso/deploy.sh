#!/bin/zsh
# switch-java — cambia el symlink .../JavaVirtualMachines/latest a la versión indicada
# Uso: switch-java 8 | 17 | 24 | 21.0.4 (prefijo de versión)

set -euo pipefail

JVM_DIR="/Library/Java/JavaVirtualMachines"
LATEST_LINK="$JVM_DIR/latest"

if [ $# -neen
  echo "❌ No hay JDK que coincida con: $want"
  /usr/libexec/java_home -V 2>&1 | sed 's/^/   /'
  exit 1
fi

jdkdir="${path_line%/Contents/Home}"
sudo rm -f "$LATEST_LINK"
sudo ln -s "$jdkdir" "$LATEST_LINK"

echo "✅ latest -> $jdkdir"
"$LATEST_LINK/Contents/Home/bin/java" -version