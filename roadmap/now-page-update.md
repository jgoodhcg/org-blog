---
title: "Now Page Update"
status: draft
description: "Refresh Now page content and add a visual Memento Mori life chart."
tags: [area/content, type/enhancement]
priority: medium
created: 2026-02-01
updated: 2026-02-14
---

# Now Page Update

## Problem / Intent

The Now page needs a content refresh and a visual "Memento Mori" life chart to visualize time passed/remaining.

## Constraints

`pages/now.md` (content) and `src/org_blog/pages/now.clj` (logic).

## Proposed Approach

- Update the markdown content to reflect current focus.
- Implement a chart component (Hiccup/SVG) that renders a grid of weeks/months/years lived vs expected lifespan.

## Open Questions

- What is the expected lifespan for the chart? 80? 90?
- Weeks or months resolution? (Weeks = ~4000 dots, Months = ~1000 dots).

## Notes