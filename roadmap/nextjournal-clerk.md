---
title: "Nextjournal Clerk for Data-Rich Posts"
status: idea
description: "Enable data-rich, interactive posts/pages (charts, tables, notebooks) using Nextjournal Clerk."
tags: [area/content, type/enhancement, tech/clojure]
priority: low
created: 2026-02-01
updated: 2026-02-01
---

# Nextjournal Clerk for Data-Rich Posts

## Problem / Intent

Enable data-rich, interactive posts/pages (charts, tables, notebooks) beyond static Markdown.

## Constraints

Keep the site static-first; avoid adding a runtime server dependency or a heavy build pipeline.

## Proposed Approach

Evaluate `nextjournal/clerk` for static HTML export and define how Clerk artifacts would flow into `posts/` or `pages/` alongside current Pandoc/Hiccup rendering.

## Open Questions

- How to integrate Clerk output with existing templates/styles?
- How to manage assets and caching?
- What authoring workflow fits current Markdown posts?

## Notes