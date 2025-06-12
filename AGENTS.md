# Org-Blog Agent Guidelines

This repository contains a Clojure project that generates a static site from Org-mode posts.

## Working with the code

- Clojure source code lives under `src/` and uses 2-space indentation. Keep lines under ~100 characters when possible.
- Function, variable, and namespace names use `kebab-case`.
- Posts are located in `posts/` and pages in `pages/`. Only modify these files if explicitly instructed.
- Generated output lives in `static/` and should not be edited directly.

## Setup and Validation

- Run `./validate-env.sh` to check that the environment is ready. This script verifies required commands and namespaces.
- To test site generation, run:
  ```bash
  clojure -M -e "(require 'codex-init) (System/exit (codex-init/run-site-generation-test))"
  ```
- Use the following entry points if needed:
  - `clojure -M -m codex-init validate` – validate the environment.
  - `clojure -M -m codex-init generate` – regenerate the site.
  - `clojure -M -m codex-init serve` – start the development server.

## Licensing Notice

The blog content is © Justin Good. The README states it may **not** be used for training AI models or other automated systems without permission.
