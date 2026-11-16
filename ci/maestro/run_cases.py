#!/usr/bin/env python3
"""Run the original cases with an Android task-removal barrier between processes."""
from pathlib import Path
import subprocess
import sys
import time
import xml.etree.ElementTree as ET

from verify import selected

def main():
    outputs = Path("evidence/cases")
    outputs.mkdir(parents=True, exist_ok=True)
    failed = False
    combined = ET.Element("testsuites")
    for flow in selected(sys.argv[1]):
        # Android can finish removing the previous task after pm clear returns,
        # killing the process Maestro immediately launches for the next flow.
        # Clear an already stopped app and let the pending removal finish first.
        subprocess.run(["adb", "shell", "am", "force-stop", "com.carlosjimz87.maestrotestingdemo"], check=True)
        subprocess.run(["adb", "shell", "pm", "clear", "com.carlosjimz87.maestrotestingdemo"], check=True)
        time.sleep(2)
        report = outputs / (flow.stem + ".xml")
        result = subprocess.run(["maestro", "test", str(flow), "--format", "junit", "--output", str(report)])
        failed |= result.returncode != 0
        if not report.is_file():
            raise RuntimeError(f"Missing case report: {flow.stem}")
        root = ET.parse(report).getroot()
        suites = [root] if root.tag == "testsuite" else list(root.iter("testsuite"))
        for suite in suites:
            combined.append(suite)
    ET.ElementTree(combined).write("evidence/junit.xml", encoding="utf-8", xml_declaration=True)
    return int(failed)

if __name__ == "__main__":
    sys.exit(main())
