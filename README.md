# TaskFlow - Maestro Testing Demo

A task manager Android app built with Jetpack Compose, designed to showcase **Maestro E2E testing** with AI-powered visual assertions, a full **CI/CD pipeline**, and **MCP integration** with Claude Code.

---

## Table of Contents
- [The App](#the-app)
- [Tech Stack](#tech-stack)
- [Setup](#setup)
- [Maestro Test Flows](#maestro-test-flows)
- [Running Tests](#running-tests)
- [AI-Powered Testing](#ai-powered-testing)
- [MCP Integration (Claude Code)](#mcp-integration-claude-code)
- [CI/CD Pipeline](#cicd-pipeline)
- [Demo Branches](#demo-branches)
- [Presentation Guide](#presentation-guide)
- [License](#license)

---

## The App

**TaskFlow** is a task management app with 8 screens and real-world UI patterns:

| Screen | Features | Maestro Patterns |
|--------|----------|-----------------|
| **Login** | Form validation, snackbar errors | `inputText`, `assertVisible`, error states |
| **Task List** | 24 tasks, pull-to-refresh, swipe-to-delete, FAB | `scroll`, `swipe`, `pullToRefresh` |
| **Task Detail** | Priority badge, category, due date, toggle complete | Deep navigation, dynamic assertions |
| **Add Task** | Form with dropdowns, validation | `tapOn` dropdown, form validation |
| **Search** | Real-time filtering, clear button | `inputText`, `clearText`, filtered results |
| **Profile** | Toggle switches, logout dialog | `Switch` toggle, `AlertDialog` interaction |
| **Statistics** | Progress bars, category chart | AI visual assertions on charts |
| **Settings** | Preferences, snackbar feedback | Toggle states, toast verification |

Bottom navigation with 3 tabs: Tasks, Search, Profile.

---

## Tech Stack

| Component | Version |
|-----------|---------|
| AGP | 9.1.0 |
| Kotlin | 2.3.20 |
| Compose BOM | 2026.03.00 |
| Gradle | 9.3.1 |
| compileSdk | 36 |
| targetSdk | 35 |
| Maestro CLI | Latest |
| Docker Android | emulator_14.0 |

---

## Setup

1. **Clone**:
   ```bash
   git clone https://github.com/carlosjimz87/MaestroTestingDemo.git
   cd MaestroTestingDemo
   ```

2. **Install Maestro CLI**:
   ```bash
   curl -fsSL "https://get.maestro.mobile.dev" | bash
   maestro --version
   ```

3. **Build the app**:
   ```bash
   ./gradlew assembleDebug
   ```

4. **Install on emulator/device**:
   ```bash
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```

---

## Maestro Test Flows

13 test flows organized with reusable subflows:

```
maestro_flows/
  flows.yaml                    # Main orchestrator (12 flows)
  ai_visual_check.yaml          # AI visual regression suite
  subflows/
    login.yaml                  # Reusable login subflow
    logout.yaml                 # Reusable logout subflow
  tests/
    01_login_validation.yaml    # Empty fields, invalid creds, error messages
    02_login_success.yaml       # Happy path login
    03_task_list_scroll.yaml    # scrollUntilVisible through 24 items
    04_task_create.yaml         # Fill form, save, verify in list
    05_task_detail.yaml         # Navigate to detail, verify content
    06_task_complete.yaml       # Toggle completion state
    07_task_swipe_delete.yaml   # Swipe left to delete
    08_search_filter.yaml       # Search, filter results, clear
    09_profile_settings.yaml    # Toggles, settings, logout dialog
    10_pull_to_refresh.yaml     # Pull-to-refresh gesture
    11_navigation_tabs.yaml     # Bottom tab switching
    12_form_validation.yaml     # Submit empty form, verify error
    13_ai_regression.yaml       # AI catches visual bugs
```

### Maestro Features Demonstrated

| Feature | Flows |
|---------|-------|
| `assertVisible` / `assertNotVisible` | All |
| `scrollUntilVisible` | 03, 08 |
| `swipe` (delete, refresh) | 07, 10 |
| `inputText` / `clearText` | 01, 02, 04, 08 |
| `runFlow` (subflow reuse) | All via login.yaml |
| `assertWithAI` / `assertNoDefectsWithAI` | 13, ai_visual_check |
| `extendedWaitUntil` | 02, 10 |
| `env` variables | All login flows |
| Dialog interaction | 07, 09, 12 |
| `back` navigation | 05, 09 |

---

## Running Tests

### Full suite
```bash
./run_maestro.sh
```

### Individual flow
```bash
./run_maestro.sh maestro_flows/tests/03_task_list_scroll.yaml
```

### With HTML report
```bash
maestro test maestro_flows/flows.yaml --format html-detailed --output report.html
```

### AI visual checks (requires API key)
```bash
export MAESTRO_API_KEY=your_key_here
maestro test maestro_flows/ai_visual_check.yaml
```

---

## AI-Powered Testing

### `assertWithAI`
Natural language visual assertions:
```yaml
- assertWithAI:
    assertion: "The HIGH priority badge is displayed in RED color"
```

### `assertNoDefectsWithAI`
Automatic visual defect detection (clipping, overlapping, rendering issues):
```yaml
- assertNoDefectsWithAI
```

AI assertions require `MAESTRO_API_KEY` environment variable.

---

## MCP Integration (Claude Code)

Maestro's built-in MCP server enables AI-assisted interactive testing.

### Setup
Add to `.mcp.json`:
```json
{
  "mcpServers": {
    "maestro": {
      "command": "maestro",
      "args": ["mcp"]
    }
  }
}
```

### Available Tools
| Category | Tools |
|----------|-------|
| Device Control | `launch_app`, `stop_app`, `back`, `start_device`, `list_devices` |
| UI Interaction | `tap_on`, `input_text`, `take_screenshot`, `inspect_view_hierarchy` |
| Flow Management | `run_flow`, `run_flow_files`, `check_flow_syntax` |
| Documentation | `query_docs`, `cheat_sheet` |

Use Claude Code to take screenshots, analyze UI, run flows, and generate new test flows interactively.

---

## CI/CD Pipeline

GitHub Actions workflow with Docker Android emulator:

1. **Build** - Gradle build with caching + lint check
2. **Test** - Maestro E2E suite (12 traditional + AI visual checks)
3. **Report** - GitHub Step Summary + HTML reports + GitHub Pages deployment

### Trigger
Push or PR to `main` branch.

### View Results
- **Actions tab** - Step summary with test results inline
- **Artifacts** - Download full reports and screenshots (30-day retention)
- **GitHub Pages** - Landing page with links to all reports and screenshot gallery

### Secrets
- `MAESTRO_API_KEY` (optional) - Enables AI-powered assertions in CI

---

## Demo Branches

| Branch | Description |
|--------|-------------|
| `main` | All tests pass (traditional + AI) |
| `demo/with-bug` | Priority color bug (HIGH shows green instead of red) + text overflow. Traditional tests pass, AI catches the visual regression |

### Presentation 3-Act Structure

| Act | What | Branch |
|-----|------|--------|
| **Act 1: Setup** | Install Maestro, show CLI, explore Studio, walk through TaskFlow app | main |
| **Act 2: The Bug** | Traditional tests pass on bugged branch, AI catches priority color bug | demo/with-bug |
| **Act 3: Pipeline** | Fix bug, push, CI runs, show GitHub Pages report | main |

---

## Presentation Guide

### Phase 1: Installation & Config
- Install Maestro CLI
- Show `maestro --version` and `maestro studio`
- Walk through the TaskFlow app on emulator
- Show the Maestro flow YAML structure

### Phase 2: CLI & Desktop Usage
- Run individual flows: `maestro test maestro_flows/tests/03_task_list_scroll.yaml`
- Run full suite with report: `./run_maestro.sh`
- Open Maestro Studio for interactive exploration
- Show MCP integration: Claude Code takes screenshots, analyzes UI, runs flows

### Phase 3: Pipeline & Reports
- Push to trigger CI pipeline
- Show GitHub Actions with Docker emulator
- View Step Summary in Actions tab
- Navigate GitHub Pages report (landing page, Maestro report, AI report, screenshots)
- Demo the `demo/with-bug` branch: AI catches what traditional tests miss

---

## License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
