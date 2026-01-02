# Tailwind v4 Migration

## Work Unit Summary

- **Status:** done
- **Problem/Intent:** Site uses Tailwind v4 standalone binary but config is still v3 JS-based. CSS compilation fails with "unknown utility class" errors. Old output.css from v3 build is being served unchanged.
- **Constraints:** Must use standalone binary (no npm). V4 uses CSS-based configuration with `@theme` directive instead of JS config.
- **Proposed Approach:** Rewrite `css/input.css` for v4 syntax. Move color/font definitions into `@theme` block. Delete old `tailwind.config.js`. Regenerate output.css.
- **Open Questions:** What color palette to use? (Depends on site-refresh work unit)

---

## Notes

### Current State (2025-01-02)

Files involved:
- `tailwind.config.js` - old v3 JS config, defines synthwave colors (cyan, pink, purple, green neon palette)
- `css/input.css` - uses `@tailwind` directives and `@apply` with custom color names
- `static/css/output.css` - 3000+ lines, stale from old v3 build

Error when running v4 binary:
```
Error: Cannot apply unknown utility class `bg-black`
```

V4 migration requires:
1. Replace `@tailwind base/components/utilities` with `@import "tailwindcss"`
2. Define theme in CSS with `@theme { }` block
3. Custom colors go in `--color-*` CSS variables inside `@theme`
