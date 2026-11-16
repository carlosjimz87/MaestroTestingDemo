#!/usr/bin/env bash
set -euo pipefail
shard="${1:?shard required}"
mkdir -p evidence
python3 ci/maestro/verify.py manifest evidence --shard "$shard"
adb wait-for-device
# Match the previous device logical viewport (480 x 1013 dp).
# Pixel 2 default height lets the IME cover the unscrollable login fields.
adb shell wm size 1080x2280
adb shell wm density 360
adb install -r qa-apk/app-debug.apk
adb logcat -c
if [ "$shard" = ai ]; then
  if [ -z "${MAESTRO_API_KEY:-}" ]; then
    echo "Manual AI requested but MAESTRO_API_KEY is absent" >&2
    exit 1
  fi
  maestro test maestro_flows/ai_visual_check.yaml --format junit --output evidence/junit.xml 2>&1 | tee evidence/maestro.log
else
  mapfile -t flows < <(python3 ci/maestro/verify.py cases --shard "$shard")
  test "${#flows[@]}" -eq 6
  maestro test "${flows[@]}" --format junit --output evidence/junit.xml 2>&1 | tee evidence/maestro.log
fi
