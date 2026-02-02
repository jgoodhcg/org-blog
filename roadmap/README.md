# Roadmap Structure

This directory contains project planning and work tracking using markdown files.

## Files

| File | Purpose |
|------|---------|
| `index.md` | Canonical project state: goal, current focus, and directory of work units |
| `_template.md` | Starter template for new work units |
| `*.md` | Individual work unit files (with frontmatter) |
| `archived/` | Completed or dropped work units moved here to keep root clean |

## Work Unit Frontmatter

Every work unit file **must** begin with YAML frontmatter for machine parsing:

```yaml
---
title: "Feature Name"
status: idea | planned | active | paused | done | dropped
description: "One-line summary of what this work unit accomplishes"
tags: [area/frontend, type/feature]  # Optional but recommended
priority: medium                      # high | medium | low
created: 2024-01-15
updated: 2024-01-20
effort: M                             # Optional: XS | S | M | L | XL
depends-on: []                        # Optional: filenames of blocking work units
---
```

## Status Definitions

| Status | Meaning | Kanban Column |
|--------|---------|---------------|
| `idea` | Captured but not yet scoped | Backlog |
| `planned` | Scoped and ready to start | Backlog |
| `active` | Currently being worked on | In Progress |
| `paused` | Started but blocked or deprioritized | In Progress |
| `done` | Shipped and working | Done |
| `dropped` | Decided not to pursue | (hidden) |

## Rules

1. **Status lives in frontmatter** - Not in prose.
2. **One work unit at a time** - Agents operate on a single work unit per session.
3. **Scope splits create new files** - If a work unit grows, split into separate files.
4. **Never delete** - Mark abandoned work as `dropped` and keep the file for context or move to `archived/`.
5. **Update dates** - Update the `updated` field whenever you modify a work unit.
6. **Consistent tags** - Use `area/`, `type/`, `tech/` prefixes.