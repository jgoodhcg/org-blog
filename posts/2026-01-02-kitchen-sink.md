---
title: Kitchen Sink
date: 2026-01-02
description: A test post showing off all the design elements
---

# Kitchen Sink

This post is used to test the visual styles of the blog. It contains various Markdown elements to ensure everything looks correct.

## Typography

Here is a paragraph with **bold text**, *italic text*, and [a link to home](/) to test the basic inline styles.

### Lists

Unordered list:
- Item one
- Item two
  - Nested item
- Item three

Ordered list:
1. First step
2. Second step
3. Third step

## Code Blocks

Inline code: `(defn hello [world] (println world))`.

Fenced code block with Clojure:

```clojure
(ns org-blog.core
  (:require [hiccup.core :refer [html]]))

(defn render-page [title content]
  (html
   [:html
    [:head [:title title]]
    [:body content]]))
```

## Blockquotes

> "The best way to predict the future is to invent it."
> — Alan Kay

## Tables

| Feature | Support | Note |
| :--- | :--- | :--- |
| Markdown | Yes | via Pandoc |
| Clojure | Yes | Native |
| Style | Tailwind | v4 |

---

*End of kitchen sink.*
