#!/bin/bash
set -e
set -x

echo "🧹 Cleaning previous screenshots and report..."
rm -rf screenshots && rm -f maestro-output.log && mkdir -p screenshots

echo "🚀 Running Maestro tests locally..."
maestro test maestro_flows/flows.yaml > maestro-output.log 2>&1
