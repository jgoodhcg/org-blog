# Site Refresh

## Work Unit Summary

- **Status:** active
- **Problem/Intent:** Current dark neon synthwave aesthetic no longer fits. Site has accumulated cruft. Want a clean, minimal canvas ready for content.
- **Constraints:** Keep Hiccup templating. Keep Tailwind (v4). Must work with pandoc-generated HTML structure.
- **Proposed Approach:** Follow the design spec. Implement calm, editorial aesthetic with warm off-white background, single accent color, generous spacing.
- **Open Questions:** What to remove vs keep?

## Design Reference

See [design-spec.md](../design-spec.md) for the full design specification. Key points:

- **Vibe:** Calm, editorial, "public technical notebook"
- **Colors:** Warm off-white bg (`#F9FAFB`), dark charcoal text (`#111827`), one muted accent
- **Typography:** Sans-serif (Inter, Source Sans 3), 65-70ch line length, 1.6-1.65 line height
- **Layout:** Single-column dominant, quiet TOC in secondary column on desktop
- **Non-goals:** No gradients, no animation, no engagement patterns

---

## Notes

### Style Options (2025-01-02)

Three directions considered:
1. **Light minimal** - white/gray bg, dark text, subtle accents
2. **Dark minimal** - dark gray (not black) bg, light text, muted accents
3. **Light warm** - cream/off-white bg, warm browns/oranges

### Things to potentially remove/simplify

- Gradient text on h1
- Neon gradient bullets
- Gradient hr elements
- Animated TOC hover effects
- Complex color palette (currently ~8 colors with shades)

### Things to keep

- Prism syntax highlighting (maybe different theme)
- Table of contents
- Read time estimate
- RSS feed
- Clean header/footer structure
