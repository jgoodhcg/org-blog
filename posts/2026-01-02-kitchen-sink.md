---
title: Kitchen Sink
date: 2026-01-02
updated: 2026-01-03
description: A test post showing off all the design elements
---

# Kitchen Sink

This post is used to test the visual styles of the blog. It contains various Markdown elements to ensure everything looks correct.

## Typography

Here is a paragraph with **bold text**, *italic text*, `inline code`, and [a link to home](/) to test the basic inline styles.

### Lists

Unordered list:

- Item one
- Item two
- Item three with a longer description that might wrap to a second line on smaller screens.

Ordered list:

1. First step
2. Second step
3. Third step

Nested list:

- Item one
  - Nested item A
  - Nested item B
- Item two

## Images

Here is a nice garden image:

![Garden](/img/2023-05-13-garden.jpg)

## LaTeX Equations

Inline equation: $E = mc^2$

Display equation:

$$
\frac{1}{(\sqrt{\phi \sqrt{5}}-\phi) e^{\frac{2}{5} \pi}} =
1+\frac{e^{-2\pi}} {1+\frac{e^{-4\pi}} {1+\frac{e^{-6\pi}}
{1+\frac{e^{-8\pi}} {1+\cdots} } } }
$$

## Code Blocks

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

A really wide code block to test scrolling:

```javascript
let exampleLongLine = "This is a very long line of code in JavaScript that might be harder to read on mobile screens due to its length and the fact that it does not contain any breaks or spaces that would naturally wrap the line.";
```

## Blockquotes


> Programming is not about typing, it's about thinking.
> — Rich Hickey 

## Tables

| Header 1 | Header 2 | Header 3 |
| :--- | :--- | :--- |
| Cell 1 | Cell 2 | Cell 3 |
| Cell 4 | Cell 5 | Cell 6 |

---

*End of kitchen sink.*
