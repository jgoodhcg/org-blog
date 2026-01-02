# Resume Update

## Work Unit Summary

- **Status:** active
- **Problem/Intent:** Skills section is visually unappealing as a grid of text. Want a word cloud instead. Also need to add AI/CLI tools to skills.
- **Constraints:** Resume is generated from `pages/resume.edn`. Hiccup template in `src/org_blog/pages/resume.clj`.
- **Proposed Approach:** Replace grid-based skills section with a word cloud layout. Add AI CLI tools (Claude Code, Cursor, etc.) to the skills data.
- **Open Questions:** How to implement word cloud in pure CSS/Hiccup? What size variation to use? Which AI tools to list?

---

## Notes

### Current Skills Structure (resume.edn)

Skills are organized as categories with keywords:
```clojure
{:name "Category Name"
 :keywords ["skill1" "skill2" ...]}
```

### Word Cloud Approaches

1. **CSS-only** - Random-ish positioning with flexbox wrap, varying font sizes
2. **Fixed layout** - Predefined positions/sizes, looks intentional
3. **Tag cloud** - Inline with varying sizes, simpler than true cloud

### AI Tools to Add

Need to decide which to include:
- Claude Code (CLI)
- Cursor
- Copilot
- ChatGPT
- Other agentic tools?
