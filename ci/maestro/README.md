# Maestro CI performance

The Android acceptance suite is still cases 01–12 from `maestro_flows/flows.yaml`.
The app, assertions, login and per-case clearState are unchanged.

## Pipeline
- `build`: one Gradle invocation builds the debug APK and runs blocking lint.
  Gradle setup v6.4.1 uses the open-source **basic** cache provider and the existing wrapper.
- Two independent Ubuntu 24.04 runners test cases 01–06 and 07–12 against that exact APK.
  Each uses native Android 14 / API34, google_apis x86_64 Pixel2, KVM, 2 cores, 2048M RAM,
  headless rendering and disabled system animations.
- A clean boot snapshot is cached **before APK installation**. Shard data is never cached.
  Cache identity includes the emulator version and emulator workflow configuration.
- Maestro CLI 2.12.1 is checksum-pinned. Only its installed distribution is cached.
- `maestro-e2e` requires successful build and both shards, twelve unique passing JUnit cases,
  and matching run, attempt, revision, tree and APK digest. Missing/skipped/cancelled/duplicate
  cases or stale evidence fail the gate. This replaces tolerance of E2E failures with explicit
  enforcement of the existing twelve-case acceptance criteria.
- Each shard always uploads evidence. The combined artifact includes JUnit, HTML index,
  CLI output, lint and screenshots/diagnostics. The debug APK is a separate artifact.
- Public standard Linux runners and standard Actions caches only; no paid service,
  enhanced/commercial Gradle cache, build scan submission or new credentials.
- Feature branches run via PRs (including GitHub native stacks), avoiding duplicate pushes.
  New commits cancel older runs of the same PR. Main verification is not cancelled.

## Manual runs and benchmarking
`workflow_dispatch` offers `cache_mode=normal|cold` and `run_ai=false|true`.
AI is **never automatic** and performance measurements always leave it false.
Explicit AI runs use the existing MAESTRO_API_KEY and may consume that provider's existing credits.
A missing credential fails an explicitly requested AI job rather than pretending it ran.

The cold mode bypasses restores and saves for Gradle, Maestro and AVD caches.
Use one cold dispatch and two normal runs/re-runs at the same final source revision.
Compare complete wall time, phases and total runner-minutes, not just the fastest shard:

```sh
python3 ci/maestro/benchmark.py --baseline BASELINE1 BASELINE2 --cold COLD_RUN \
  --warm WARM_RUN1 WARM_RUN2 --output benchmark.json
```

Historical successful same-suite runs are the baseline; their sequential AI time is excluded
so moving AI to manual is not presented as an emulator/test speedup. Report cold and warm
separately, including cache hits/misses in Actions. Target: >=25% lower median warm latency.
If the target or stability is not met, the PR stays draft with actual measurements.

Artifacts are `maestro-qa-apk-ATTEMPT`, `maestro-shard-N-ATTEMPT` and
`maestro-test-results-ATTEMPT`, retained seven days. Re-run **all jobs** for a new attempt;
re-running just failed shards cannot reuse an APK from an earlier attempt.
Pages publishes the combined report only after successful pushes to main.

## Local verification
```sh
PYTHONDONTWRITEBYTECODE=1 python3 -m unittest discover -s ci/maestro -p 'test_*.py' -v
```
No Android application unit-test result or Android7 runtime pass is implied by these CI-tool tests.
The APK continues to declare minSdk24 and use core-library desugaring. No production release
or merge is part of preparing this PR.
