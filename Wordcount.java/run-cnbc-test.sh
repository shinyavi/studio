#!/usr/bin/env bash
# Run CNBC Selenium test (ensures Homebrew Maven is on PATH)
set -euo pipefail
cd "$(dirname "$0")"
export PATH="/opt/homebrew/bin:${PATH}"
exec mvn test -Dtest=CnbcSignInTest "$@"
