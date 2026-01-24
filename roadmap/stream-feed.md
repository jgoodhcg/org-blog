# Stream / Microblog Feed

## Work Unit Summary

- **Status:** planned
- **Problem/Intent:**
    - The blog needs a home for "quick" series content (Project Postcards, Notes, Decision Points) that is distinct from the main essay feed.
    - These items are shorter, more visual, and frequent. Mixing them with long-form essays clutters the homepage.
    - We want a "scrollable feed" experience where users can browse full content without clicking into every single page.
- **Constraints:** Static site architecture (no database).
- **Proposed Approach:**
    - **New Page (`/stream`):** A dedicated page for series content.
    - **HTMX "Lazy" Loading:** The stream page uses HTMX to fetch and inject the full content of posts.
    - **Static API:** The build process generates standard full pages for these posts (e.g., `/posts/xyz.html`) AND potentially partials (or we just use `hx-select` to grab the `article` from the full page).
    - **Homepage Filter:** Update the main index generator to *exclude* posts with series tags (`project-postcards`, `notes`, etc.).
- **Open Questions:**
    - Do we use `hx-select` to grab content from existing full pages (easiest, no new artifacts)?
    - Or do we generate specific "partial" HTML files?
    - How do we handle pagination? (A simple "Load More" button at the bottom that fetches the next batch).

---

## Technical Tasks

1.  **Tagging:** Ensure `markdown.clj` reliably exposes `tags`.
2.  **Filter Logic:** Create a helper to split posts into "Essays" vs "Stream Items" based on tags.
3.  **Stream Page Generator:**
    - Create `src/org_blog/pages/stream.clj`.
    - Render a list of "skeletons" or just the first few items.
    - Implement the "Load More" mechanism (likely pointing to a pre-generated `stream/page-2.html` or similar).
4.  **Homepage Update:** Modify `src/org_blog/pages/home.clj` to filter out stream tags.
