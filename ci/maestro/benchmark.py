#!/usr/bin/env python3
"""Measure completed Actions runs, including parallel wall time and runner usage."""
import argparse
from datetime import datetime
import json
from pathlib import Path
import statistics
import subprocess

REPO = "carlosjimz87/MaestroTestingDemo"
def api(path):
    return json.loads(subprocess.check_output(["gh", "api", f"repos/{REPO}/{path}"], text=True))
def date(value):
    return datetime.fromisoformat(value.replace("Z", "+00:00"))
def measure(run_id, baseline=False):
    run = api(f"actions/runs/{run_id}")
    if run["status"] != "completed" or run["conclusion"] != "success":
        raise ValueError(f"Run {run_id} is not successfully completed")
    jobs = api(f"actions/runs/{run_id}/jobs?per_page=100")["jobs"]
    active = [j for j in jobs if j["started_at"] and j["completed_at"] and j["conclusion"] != "skipped"]
    wall = (max(date(j["completed_at"]) for j in active) - date(run["run_started_at"])).total_seconds()
    runner = sum((date(j["completed_at"]) - date(j["started_at"])).total_seconds() for j in active)
    phases = {}
    for job in active:
        for step in job["steps"]:
            if step["conclusion"] == "success" and step["started_at"] and step["completed_at"]:
                seconds = (date(step["completed_at"]) - date(step["started_at"])).total_seconds()
                phases[f"{job['name']} / {step['name']}"] = seconds
    ai = sum(v for k, v in phases.items() if k.endswith("/ Run AI Visual Checks")) if baseline else 0
    return dict(run_id=run_id, url=run["html_url"], sha=run["head_sha"],
                attempt=run["run_attempt"], wall_seconds=wall, comparable_seconds=wall-ai,
                excluded_baseline_ai_seconds=ai, runner_minutes=runner/60, phases=phases)

def main():
    p=argparse.ArgumentParser()
    p.add_argument("--baseline", nargs="+", required=True, type=int)
    p.add_argument("--cold", required=True, type=int)
    p.add_argument("--warm", nargs=2, required=True, type=int)
    p.add_argument("--output", type=Path, required=True)
    args=p.parse_args()
    baseline=[measure(i, True) for i in args.baseline]
    cold=measure(args.cold)
    warm=[measure(i) for i in args.warm]
    old=statistics.median(r["comparable_seconds"] for r in baseline)
    new=statistics.median(r["comparable_seconds"] for r in warm)
    result=dict(baseline=baseline,cold=cold,warm=warm,baseline_median_seconds=old,
                warm_median_seconds=new,improvement_percent=100*(old-new)/old,
                target_met=new<=old*0.75,
                note="Historical baseline excludes sequential external AI. Cold result is reported separately. Runner minutes can increase with parallelism; public standard runners only.")
    args.output.parent.mkdir(parents=True,exist_ok=True)
    args.output.write_text(json.dumps(result,indent=2))
    print(json.dumps(result,indent=2))
if __name__=="__main__":
    main()
