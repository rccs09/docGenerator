#!/bin/zsh
# switch-java — cambia el symlink .../JavaVirtualMachines/latest a la versión indicada
# Uso: switch-java 8 | 17 | 24 | 21.0.4 (prefijo de versión)

set -euo pipefail

JVM_DIR="/Library/Java/JavaVirtualMachines"
LATEST_LINK="$JVM_DIR/latest"

if [ $# -ne 1 ]; then
  echo "Uso: switch-java <version>"
  echo "Ej:  switch-java 8   |  switch-java 17   |  switch-java 21 |  switch-java 24"
  exit 1
fi

want="$1"
selector="$want"
# java_home espera "1.8" para Java 8
[[ "$want" == "8" ]] && selector="1.8"

# Busca en /usr/libexec/java_home -V una JVM cuya versión comience con $want
path_line=$(/usr/libexec/java_home -v "$selector" 2>/dev/null || true)


if [[ -z "${path_line:-}" ]]; then
  echo "❌ No hay JDK que coincida con: $want"
  /usr/libexec/java_home -V 2>&1 | sed 's/^/   /'
  exit 1
fi

jdkdir="${path_line%/Contents/Home}"
sudo rm -f "$LATEST_LINK"
sudo ln -s "$jdkdir" "$LATEST_LINK"

echo "✅ latest -> $jdkdir"
"$LATEST_LINK/Contents/Home/bin/java" -version