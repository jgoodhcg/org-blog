---
title: "Project Snapshots (Stream)"
status: done
description: "Create a separate stream feed for shorter, visual project snapshots with infinite scroll."
tags: [area/content, type/feature, tech/htmx]
priority: high
created: 2026-02-01
updated: 2026-02-01
---

# Project Snapshots (Stream)

## Problem / Intent

- The user wants "project snapshot functionality": a new publish workflow for screenshots/images.
- These items are shorter, more visual, and frequent ("Project Postcards").
- We want an "infinite scroll" feed experience where users can browse full content without clicking into every single page.
- These should be distinct from standard long-form essays.

## Constraints

- Static site architecture (no database).
- Use HTMX for the infinite scroll.

## Proposed Approach

- **Content Source:** Standard Markdown files in `posts/` but tagged with `snapshot`.
- **Feed Page (`/snapshots/`):** A dedicated page for this stream.
- **Pagination Strategy (Static):**
    - The build process generates standard full pages (e.g., `/snapshots/page/2/index.html`).
    - **HTMX Logic:**
        - The "Load More" trigger uses `hx-get="/snapshots/page/2/"` (fetching the full page).
        - It uses `hx-select="#feed-stream > *"` to extract *only* the feed items (and the next trigger) from that full page.
        - It uses `hx-swap="outerHTML"` to replace itself with that content.
    - This ensures every page is addressable and valid SEO/HTML, while providing a smooth SPA-like infinite scroll.
- **Post Rendering:** Snapshots are rendered as full blocks in the feed.
- **Homepage Filter:** `home.clj` filters out posts tagged `snapshot`.

## Notes

### Technical Implementation

- **Generator:** `src/org_blog/pages/snapshots.clj` handles both the main index and paginated pages.
- **Filtering:** `src/org_blog/pages/home.clj` excludes snapshots.
- **Testing:** E2E test added in `e2e/tests/snapshots.spec.ts`.