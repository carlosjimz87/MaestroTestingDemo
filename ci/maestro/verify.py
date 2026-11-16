#!/usr/bin/env python3
"""Partition the unchanged acceptance suite and verify run-bound JUnit evidence."""
import argparse
import hashlib
import html
import json
import os
from pathlib import Path
import re
import subprocess
import xml.etree.ElementTree as ET

ROOT = Path(__file__).resolve().parents[2]
def cases():
    names = re.findall(r"^- runFlow: (tests/[^\n]+)", (ROOT / "maestro_flows/flows.yaml").read_text(), re.M)
    if len(names) != 12 or len(set(names)) != 12 or [int(Path(n).name[:2]) for n in names] != list(range(1, 13)):
        raise ValueError("Acceptance manifest must contain exactly cases 01 through 12")
    return [ROOT / "maestro_flows" / name for name in names]

def selected(shard):
    items = cases()
    return items[:6] if str(shard) == "1" else items[6:] if str(shard) == "2" else []

def provenance(shard):
    return dict(run_id=os.environ["GITHUB_RUN_ID"], attempt=os.environ["GITHUB_RUN_ATTEMPT"],
                candidate=os.environ["CI_CANDIDATE"], revision=os.environ["GITHUB_SHA"],
                tree=subprocess.check_output(["git", "rev-parse", "HEAD^{tree}"], text=True).strip(),
                shard=str(shard), cases=[p.stem for p in selected(shard)])

def inspect(directory, expected_meta):
    actual = []
    for shard in ("1", "2"):
        folder = directory / ("shard-" + shard)
        meta = json.loads((folder / "provenance.json").read_text())
        for key in ("run_id", "attempt", "candidate", "revision", "tree"):
            if meta.get(key) != expected_meta.get(key):
                raise ValueError(f"Shard {shard}: wrong provenance {key}")
        expected = [p.stem for p in selected(shard)]
        if meta.get("shard") != shard or meta.get("cases") != expected:
            raise ValueError("Wrong shard manifest")
        root = ET.parse(folder / "junit.xml").getroot()
        found = []
        for suite in root.iter("testsuite"):
            for key in ("failures", "errors", "skipped"):
                if int(suite.get(key, "0")) != 0:
                    raise ValueError(f"Shard {shard}: nonzero {key}")
        for case in root.iter("testcase"):
            name = Path(case.get("file") or case.get("name", "")).stem
            if name not in expected or name in found:
                raise ValueError(f"Unexpected/duplicate case: {name}")
            if case.get("status") != "SUCCESS" or any(case.find(k) is not None for k in ("failure", "error", "skipped")):
                raise ValueError(f"Case did not pass: {name}")
            found.append(name)
            actual.append(dict(case=name, seconds=float(case.get("time", "0")), shard=shard))
        if set(found) != set(expected) or len(found) != 6:
            raise ValueError(f"Shard {shard}: missing cases")
    if len(actual) != 12:
        raise ValueError("Expected twelve cases")
    return actual

def main():
    p = argparse.ArgumentParser()
    p.add_argument("command", choices=["cases", "manifest", "apk", "verify"])
    p.add_argument("path", nargs="?", type=Path)
    p.add_argument("--shard", default="1", choices=["1", "2", "ai"])
    args = p.parse_args()
    if args.command == "cases":
        for case in selected(args.shard):
            print(case)
        return
    meta = provenance(args.shard)
    args.path.mkdir(parents=True, exist_ok=True)
    if args.command == "manifest":
        (args.path / "provenance.json").write_text(json.dumps(meta, indent=2))
    elif args.command == "apk":
        apk = args.path / "app-debug.apk"
        meta["apk_sha256"] = hashlib.sha256(apk.read_bytes()).hexdigest()
        (args.path / "provenance.json").write_text(json.dumps(meta, indent=2))
    else:
        try:
            if os.environ.get("BUILD_RESULT") != "success" or os.environ.get("SHARDS_RESULT") != "success":
                raise ValueError("Build or shard job failed, was skipped or cancelled")
            apk_meta = json.loads((args.path / "apk/provenance.json").read_text())
            for key in ("run_id", "attempt", "candidate", "revision", "tree"):
                if apk_meta.get(key) != meta[key]:
                    raise ValueError("APK belongs to another revision/run")
            apk = args.path / "apk/app-debug.apk"
            if hashlib.sha256(apk.read_bytes()).hexdigest() != apk_meta["apk_sha256"]:
                raise ValueError("APK digest mismatch")
            actual = inspect(args.path, meta)
            result = dict(meta, passed=True, cases=actual, apk_sha256=apk_meta["apk_sha256"])
            (args.path / "summary.json").write_text(json.dumps(result, indent=2))
            rows = "".join(f"<tr><td>{html.escape(c['case'])}</td><td>PASS</td><td>{c['seconds']:.1f}s</td><td>{c['shard']}</td></tr>" for c in actual)
            page = f"<!doctype html><meta charset=utf-8><title>Maestro CI</title><h1>12/12 cases passed</h1><p>Run {html.escape(meta['run_id'])}, attempt {html.escape(meta['attempt'])}</p><table><tr><th>Case</th><th>Result</th><th>Time</th><th>Shard</th></tr>{rows}</table><p><a href='shard-1/junit.xml'>Shard 1 JUnit</a> · <a href='shard-2/junit.xml'>Shard 2 JUnit</a></p>"
            (args.path / "index.html").write_text(page)
            summary = os.environ.get("GITHUB_STEP_SUMMARY")
            if summary:
                with open(summary, "a") as f:
                    f.write("## Maestro acceptance: 12/12 passed\n\n" + "\n".join(f"- {c['case']}: PASS ({c['seconds']:.1f}s)" for c in actual) + "\n")
        except Exception as error:
            (args.path / "failure.txt").write_text(str(error))
            raise

if __name__ == "__main__":
    main()
