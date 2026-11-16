#!/usr/bin/env bash
set -euo pipefail
version=2.12.1
expected=54dbb0716425626604973cbb374c585ca30272375d9ce15177a1152765ddd9d7
destination="$HOME/.local/share/maestro-$version"
if [ ! -f "$destination/bin/maestro" ]; then
  archive="$RUNNER_TEMP/maestro-$version.zip"
  curl --fail --location --retry 3 "https://github.com/mobile-dev-inc/Maestro/releases/download/cli-$version/maestro.zip" -o "$archive"
  echo "$expected  $archive" | sha256sum --check
  mkdir -p "$destination"
  unzip -q "$archive" -d "$destination"
  # Release distributions have a maestro/ top-level directory.
  if [ -d "$destination/maestro" ]; then
    cp -a "$destination/maestro/." "$destination/"
    rm -rf "$destination/maestro"
  fi
fi
chmod +x "$destination/bin/maestro"
echo "$destination/bin" >> "$GITHUB_PATH"
"$destination/bin/maestro" --version
