# Maestro Testing Demo

This repository demonstrates how to use **Maestro** for automated UI testing of an Android application. It includes sample test flows for navigation, delayed operations, and interaction validation. Additionally, it provides configurations for running the tests in **GitHub Actions** or locally with **act**.

---

## 📋 Table of Contents
- [Features](#features)
- [Setup](#setup)
- [Running Maestro Tests Locally](#running-maestro-tests-locally)
- [Running Maestro Tests in CI/CD](#running-maestro-tests-in-cicd)
    - [GitHub Actions](#github-actions)
    - [Local Testing with act](#local-testing-with-act)
- [Test Flows](#test-flows)
- [Contributing](#contributing)
- [License](#license)

---

## ✨ Features
- **Automated UI tests** using Maestro's YAML configurations.
- Test flows for:
    - Form filling and validation.
    - Navigation between screens.
    - Delayed operations with spinners.
- **GitHub Actions workflow** for CI/CD automation.
- Local CI/CD simulation using `act`.

---

## ⚙️ Setup
1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-repo/maestro-testing-demo.git
   cd maestro-testing-demo
   ```

2. **Install Maestro CLI**:

    - **MacOs, Linux or Windows (WSL)**:
    ```bash
    curl -fsSL "https://get.maestro.mobile.dev" | bash
    ```
   
3. **Verify installation**:
   ```bash
   maestro --version
   ```

---

## 🚀 Running Maestro Tests Locally

1. Navigate to the project directory.
   ```bash
   cd maestro-testing-demo
   ```

2. Run the test flows in terminal:
   ```bash
   maestro test maestro_flows/flows.yaml
   ```

3. Run the test flows with report output:
   ```bash
   maestro test maestro_flows/flows.yaml --output report.xml
   ```

4. View the results:
   ```bash
   cat report.xml
   ```

---

## 🤖 Running Maestro Tests in CI/CD

### GitHub Actions
This repository includes a GitHub Actions workflow to automate Maestro tests on pushes or pull requests to the `main` branch.

1. Ensure your workflow file is located in `.github/workflows/maestro.yml`.
2. Push your changes to trigger the workflow:
   ```bash
   git add .
   git commit -m "Add Maestro tests"
   git push origin main
   ```
3. View test results under the **Actions** tab in your GitHub repository.

### Local Testing with act
If you want to simulate GitHub Actions locally:

1. Install `act`:
   ```bash
   brew install act # macOS
   sudo apt-get install act # Linux
   ```

2. Run the workflow:
   ```bash
   act -j maestro-tests --container-architecture linux/amd64
   ```

---

## 🧪 Test Flows

### Example Flows
- **Navigation Test**:
  Verifies navigation to the details page and back.
- **Delayed Operation Test**:
  Validates spinner visibility and success message after a delay.
- **Login Test**:
  Simulates user login with form filling and validation.

### Directory Structure
- `flows.yaml`: Main entry point for Maestro tests.
- `login.yaml`, `home.yaml`, `details_view.yaml`: Modular flows for specific app areas.

---

## 🤝 Contributing
We welcome contributions! Please:
1. Fork the repository.
2. Create a feature branch:
   ```bash
   git checkout -b feature-name
   ```
3. Commit your changes and push to your fork.
4. Submit a pull request.

---

## 📜 License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

