#!/bin/bash
set -euo pipefail

FLOW=${1:-maestro_flows/flows.yaml}
REPORT_DIR="maestro-report"

echo "Cleaning previous results..."
rm -rf screenshots "$REPORT_DIR" maestro-output.log || true
mkdir -p screenshots "$REPORT_DIR"

echo "Running Maestro tests: $FLOW"
maestro test "$FLOW" \
  --format html-detailed \
  --output "$REPORT_DIR/index.html" \
  | tee maestro-output.log

echo ""
echo "Report: $REPORT_DIR/index.html"
echo "Log: maestro-output.log"
