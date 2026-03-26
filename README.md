# Maestro Testing Demo

This repository demonstrates how to use **Maestro** for automated UI testing of an Android application, featuring **AI-powered visual assertions** and a full **CI/CD pipeline** with Docker-based Android emulators and GitHub Pages reporting.

---

## Table of Contents
- [Features](#features)
- [Setup](#setup)
- [Running Maestro Tests Locally](#running-maestro-tests-locally)
- [AI-Powered Testing](#ai-powered-testing)
- [MCP Integration (Claude Code)](#mcp-integration-claude-code)
- [Running Maestro Tests in CI/CD](#running-maestro-tests-in-cicd)
- [Demo Branches](#demo-branches)
- [Contributing](#contributing)
- [License](#license)

---

## Features
- **Automated UI tests** using Maestro YAML flows
- **AI-powered visual assertions** with `assertWithAI` and `assertNoDefectsWithAI`
- Test flows for:
    - Form filling and validation
    - Navigation between screens
    - Delayed operations with spinners
    - Visual defect detection via LLM
- **GitHub Actions workflow** with Docker Android emulator (Android 14)
- **HTML detailed reports** via Maestro's built-in `html-detailed` formatter
- **MCP integration** for interactive AI-assisted testing with Claude Code
- Local CI/CD simulation using `act`

---

## Setup
1. **Clone the repository**:
   ```bash
   git clone https://github.com/carlosjimz87/MaestroTestingDemo.git
   cd MaestroTestingDemo
   ```

2. **Install Maestro CLI** (v2.3.0+):
   ```bash
   curl -fsSL "https://get.maestro.mobile.dev" | bash
   ```

3. **Verify installation**:
   ```bash
   maestro --version
   ```

For more details, see the [Maestro docs](https://docs.maestro.dev/).

---

## Running Maestro Tests Locally

1. Run the full test suite:
   ```bash
   maestro test maestro_flows/flows.yaml
   ```

2. Run with HTML detailed report:
   ```bash
   maestro test maestro_flows/flows.yaml --format html-detailed --output report.html
   ```

3. Run the AI visual check flow:
   ```bash
   export MAESTRO_API_KEY=your_key_here
   maestro test maestro_flows/ai_visual_check.yaml
   ```

---

## AI-Powered Testing

Maestro v2.2.0+ includes experimental AI commands that use LLMs to validate UI state:

### `assertWithAI`
Validates visual UI state using natural language assertions:
```yaml
- assertWithAI:
    assertion: "A details screen showing 'Details for Item 2' with text fully visible"
    optional: true
```

### `assertNoDefectsWithAI`
Automatically detects common visual defects (overlapping text, clipped elements, rendering issues):
```yaml
- assertNoDefectsWithAI
```

### `extractTextWithAI`
Extracts text from screenshots when standard selectors are unreliable:
```yaml
- extractTextWithAI: "the error message displayed"
- inputText: ${aiOutput}
```

### Configuration
AI assertions require a `MAESTRO_API_KEY` environment variable. For CI, add it as a GitHub Actions secret.

---

## MCP Integration (Claude Code)

Maestro v2.3.0 includes a built-in MCP (Model Context Protocol) server that allows AI assistants to interactively control devices and run tests.

### Setup with Claude Code

Add this to your `.claude/settings.json`:
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

### What you can do
- Ask Claude Code to take screenshots and analyze the UI
- Tap elements, input text, and navigate the app via natural language
- Run Maestro flows and inspect results interactively
- Diagnose visual defects with AI-powered analysis

### Available MCP Tools
| Category | Tools |
|----------|-------|
| Device Control | `launch_app`, `stop_app`, `back`, `start_device`, `list_devices` |
| UI Interaction | `tap_on`, `input_text`, `take_screenshot`, `inspect_view_hierarchy` |
| Flow Management | `run_flow`, `run_flow_files`, `check_flow_syntax` |
| Documentation | `query_docs`, `cheat_sheet` |

---

## Running Maestro Tests in CI/CD

### GitHub Actions
The pipeline uses [docker-android](https://github.com/budtmo/docker-android) (`emulator_14.0`) to run an Android 14 emulator in CI.

1. **Add secrets** (optional, for AI features):
   - `MAESTRO_API_KEY` — enables AI-powered assertions in CI

2. **Push to trigger**:
   ```bash
   git push origin main
   ```

3. **View results**:
   - **Actions tab** — test logs and artifacts
   - **GitHub Pages** — HTML detailed report with per-step execution details

### Local Testing with act
```bash
act -j maestro-e2e --container-architecture linux/amd64
```

---

## Demo Branches

This repo is structured for a live talk demonstrating AI-powered test detection:

| Branch | Description |
|--------|-------------|
| `main` | Clean version — all tests pass (traditional + AI) |
| `demo/with-bug` | Contains a deliberate visual bug (clipped text on DetailsScreen) that traditional `assertVisible` passes but `assertNoDefectsWithAI` catches |

### The Demo Narrative
1. **Act 1** — Run traditional tests on `demo/with-bug`. They pass despite the visual bug.
2. **Act 2** — Run AI visual checks. `assertNoDefectsWithAI` catches the clipped text.
3. **Act 3** — Switch to `main` (bug fixed), run full pipeline, show the HTML report.

---

## Contributing
1. Fork the repository
2. Create a feature branch: `git checkout -b feature-name`
3. Commit your changes and push
4. Submit a pull request

---

## License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
