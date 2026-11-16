#!/usr/bin/env bash
set -euo pipefail
trap 'result=$?; bash ci/maestro/diagnostics.sh || true; exit "$result"' EXIT
bash ci/maestro/run.sh "${1:?shard required}"
