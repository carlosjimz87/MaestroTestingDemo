#!/usr/bin/env bash
set -euo pipefail
shard="${1:?shard required}"
mkdir -p evidence
python3 ci/maestro/verify.py manifest evidence --shard "$shard"
adb wait-for-device
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
