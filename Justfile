# Justfile for org-blog

# Start the dev server (blocks)
server:
    clj -M -e "(do (require 'org-blog.dev-server) (org-blog.dev-server/start-server) @(promise))"

# Watch Tailwind CSS changes
watch-css:
    tailwindcss -i ./css/input.css -o ./static/css/output.css --watch

# Start both server and tailwind watcher
dev:
    just server & just watch-css

# Regenerate the site (one-off)
build:
    clj -M -e "(do (require 'org-blog.core) (org-blog.core/regenerate-site) (shutdown-agents))"

# Install e2e dependencies
e2e-install:
    cd e2e && npm install

# Run smoke tests
smoke:
    cd e2e && npx playwright test tests/smoke.spec.ts

# Run snapshots stream tests
test-snapshots:
    cd e2e && npx playwright test tests/snapshots.spec.ts

# Take a screenshot of a specific path (usage: just screenshot /resume)
screenshot path="":
    cd e2e && npx tsx scripts/screenshot.ts {{path}}

# Run the blog navigation flow
flow-nav:
    cd e2e && npx tsx scripts/flow-blog-nav.ts
