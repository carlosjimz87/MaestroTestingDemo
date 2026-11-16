#!/usr/bin/env bash
set -uo pipefail
mkdir -p evidence/diagnostics
timeout 15 adb logcat -b crash -d > evidence/diagnostics/crashes.txt 2>&1 || true
timeout 15 adb shell uiautomator dump /sdcard/ci-window.xml || true
timeout 15 adb pull /sdcard/ci-window.xml evidence/diagnostics/window.xml || true
timeout 15 adb exec-out screencap -p > evidence/diagnostics/screen.png || true
if [ -d "$HOME/.maestro/tests" ]; then
  find "$HOME/.maestro/tests" -type f \( -name '*.png' -o -name '*hierarchy*.xml' \) -exec cp {} evidence/diagnostics/ \;
fi
# Screenshots produced by the unchanged flows can be relative to their YAML.
find maestro_flows -type f -name '*.png' -exec cp {} evidence/diagnostics/ \;
