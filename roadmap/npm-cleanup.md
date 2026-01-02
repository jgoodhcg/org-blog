# NPM Cleanup

## Work Unit Summary

- **Status:** active
- **Problem/Intent:** Remove all remnants of npm/node from the project now that we're using Tailwind standalone binary.
- **Constraints:** None
- **Proposed Approach:** Delete `tailwind.config.js` and any other npm-related files. Already deleted `node_modules/`, `package.json`, `package-lock.json`.
- **Open Questions:** None

---

## Notes

### Already Removed (2025-01-02)

- `node_modules/`
- `package.json`
- `package-lock.json`

### Still To Remove

- `tailwind.config.js` - old v3 config, not compatible with v4 anyway

### Blocked By

This is partially blocked by tailwind-v4-migration. The config file documents the old color palette which may be useful reference when setting up new v4 theme. Remove after new CSS is in place.
