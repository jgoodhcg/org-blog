#!/usr/bin/env bash
# codex-setup.sh — Setup script for Org-Blog in Codex environment
# This script builds on the base Clojure setup script

set -euo pipefail

# Source location of the original setup script
ORIGINAL_SETUP="./setup.sh"

echo "=== Setting up Org-Blog for Codex ==="

# 1. Run the original Clojure setup script if it exists
if [ -f "$ORIGINAL_SETUP" ]; then
  echo "Running base Clojure setup script..."
  bash "$ORIGINAL_SETUP"
else
  echo "Warning: Base setup script ($ORIGINAL_SETUP) not found"
  echo "Installing essential dependencies manually..."
  
  # Minimal setup from the original script
  sudo apt-get update -qq
  sudo apt-get install -yqq curl rlwrap default-jdk
  
  if ! command -v clojure >/dev/null 2>&1; then
    CLJ_VERSION=1.11.1.1165
    curl -s "https://download.clojure.org/install/linux-install-${CLJ_VERSION}.sh" | sudo bash
    sudo ln -sf /usr/local/bin/clojure /usr/local/bin/clj
  fi
  
  # Setup proxy if needed
  export JAVA_TOOL_OPTIONS="-Dhttp.proxyHost=proxy -Dhttp.proxyPort=8080 -Dhttps.proxyHost=proxy -Dhttps.proxyPort=8080"
  
  mkdir -p ~/.m2
  cat > ~/.m2/settings.xml <<'EOF'
<settings>
  <proxies>
    <proxy>
      <active>true</active>
      <protocol>http</protocol>
      <host>proxy</host>
      <port>8080</port>
    </proxy>
  </proxies>
</settings>
EOF
fi

# 2. Install Node.js dependencies for CSS processing
echo "Installing Node.js dependencies..."
npm install

# 3. Pre-download all Clojure dependencies
echo "Pre-downloading Clojure dependencies..."
clojure -P

# 4. Make validation script executable
chmod +x ./validate-env.sh

# 5. Run validation to ensure everything is ready
echo "Validating environment..."
./validate-env.sh

echo "=== Org-Blog setup for Codex complete ==="
echo "To use in Codex agent environment (non-interactive):"
echo "# Run site validation:"
echo "clojure -M -e \"(require 'codex-init) (System/exit (codex-init/validate-environment))\""
echo "# Generate site:"
echo "clojure -M -e \"(require 'codex-init) (System/exit (codex-init/run-site-generation-test))\""
echo "# Start dev server (if your agent supports long-running processes):"
echo "clojure -M -e \"(require 'codex-init) (codex-init/start-dev-environment)\""
echo "# Or use the -main function directly:"
echo "clojure -M -m codex-init validate"
echo "clojure -M -m codex-init generate"
echo "clojure -M -m codex-init serve"