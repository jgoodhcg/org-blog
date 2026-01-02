# Roadmap Structure

This directory contains project planning and work tracking using markdown files.

## Files

| File | Purpose |
|------|---------|
| `index.md` | Canonical project state: goal, current focus, active work units, key links |
| `_template.md` | Starter template for new work units |
| `log.md` | Append-only notes (optional) |
| `*.md` | Individual work units |
| `archived/` | Completed or dropped work units moved here to keep root clean |

## Work Unit Format

Each work unit file starts with a summary block:

```
## Work Unit Summary
- **Status:** idea | active | paused | done | dropped
- **Problem/Intent:** What problem does this solve or what's the goal?
- **Constraints:** Technical or scope limitations
- **Proposed Approach:** How we plan to tackle it
- **Open Questions:** Unresolved decisions or unknowns
```

Followed by narrative notes as work progresses.

## Rules

1. **Status lives in work units** - The index links to active work but doesn't duplicate status
2. **No checklists** - Keep detail as narrative prose, not subtasks
3. **One work unit at a time** - LLMs operate on a single work unit per session
4. **Scope splits create new files** - If a work unit grows, split into separate files
5. **Never delete** - Mark abandoned work as `dropped` and keep the file for context
6. **Index stays short** - 3-7 active links max, no task lists
7. **Log is append-only** - No retroactive edits to log.md
8. **Archive when done** - Move completed or dropped work units to `archived/` to keep root clean
