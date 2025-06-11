#!/usr/bin/env bash
# validate-env.sh — Validate Org-Blog environment for Codex (non-interactive)
set -euo pipefail

# Log levels for machine-readable output
LOG_INFO="INFO"
LOG_ERROR="ERROR"

log() {
  local level="$1"
  local message="$2"
  echo "[$level] $message"
}

# Exit codes
EXIT_SUCCESS=0
EXIT_MISSING_COMMAND=1
EXIT_MISSING_FILE=2
EXIT_CLOJURE_ERROR=3

log "$LOG_INFO" "Validating Org-Blog environment for Codex"

# Check if essential tools are installed
check_command() {
  if ! command -v "$1" &> /dev/null; then
    log "$LOG_ERROR" "Command not found: $1"
    exit $EXIT_MISSING_COMMAND
  else
    log "$LOG_INFO" "Command found: $1"
  fi
}

check_command clojure
check_command java
check_command npm

# Check if essential project files exist
check_file() {
  if [ ! -f "$1" ]; then
    log "$LOG_ERROR" "File not found: $1"
    exit $EXIT_MISSING_FILE
  else
    log "$LOG_INFO" "File found: $1"
  fi
}

check_directory() {
  if [ ! -d "$1" ]; then
    log "$LOG_ERROR" "Directory not found: $1"
    exit $EXIT_MISSING_FILE
  else
    log "$LOG_INFO" "Directory found: $1"
  fi
}

check_file "deps.edn"
check_directory "src"
check_directory "posts"
check_directory "dev"
check_file "dev/codex_init.clj"

# Test Clojure command-line execution
log "$LOG_INFO" "Testing Clojure command-line execution"
if ! clojure -e "(System/exit 0)" &> /dev/null; then
  log "$LOG_ERROR" "Clojure command-line execution failed"
  exit $EXIT_CLOJURE_ERROR
else
  log "$LOG_INFO" "Clojure command-line execution successful"
fi

# Test org-blog namespace loading
log "$LOG_INFO" "Testing org-blog namespace loading"
if ! clojure -e "(require 'org-blog.core) (System/exit 0)" &> /dev/null; then
  log "$LOG_ERROR" "Failed to load org-blog.core namespace"
  exit $EXIT_CLOJURE_ERROR
else
  log "$LOG_INFO" "org-blog.core namespace loaded successfully"
fi

log "$LOG_INFO" "Environment validation complete - ready for Codex"
exit $EXIT_SUCCESS