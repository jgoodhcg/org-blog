# Personal Blog Design Specification

## Core Identity

**Design a calm, editorial personal blog for a thoughtful software engineer.**
The site is optimized for long-form technical and reflective writing, prioritizing clarity, readability, and longevity over novelty, engagement, or growth.

The site should feel like a **public technical notebook**—authored, patient, and trustworthy.

---

## Global Priorities (in order)

1. Reading comfort
2. Typographic clarity
3. Visual calm
4. Structural usefulness
5. Long-term durability

If a design decision improves (1–3) at the expense of visual novelty, choose it.

---

## Baseline Vibe

* Quietly authoritative
* Personal but not confessional
* Technical without being corporate
* Calm, literate, and restrained
* Designed for rereading and revisiting

The design should assume the reader is patient and curious.

---

## Layout Rules

### Reading Column

* Use a dominant single-column layout for article content
* Target line length: **65–70ch**
* Never allow side content to reduce reading comfort
* Generous vertical rhythm; no dense layouts

### Navigation

* Minimal and visually quiet
* Content always has priority over navigation
* No feed-first or timeline-first framing

---

## Table of Contents (Required, but Quiet)

### Behavior

* TOC is enabled by default
* Automatically hide TOC if:

  * Fewer than ~2–3 headings
  * Viewport is narrow (mobile)
* TOC is optional support, never mandatory structure

### Placement

* Secondary column on desktop
* Inline or collapsed on small screens
* Never boxed, never dominant

### Visual Style

* Smaller than body text
* Muted color (metadata tier)
* No background panels
* No numbering unless content demands it

### Interaction

* Subtle hover states only
* No progress bars
* No aggressive scroll tracking
* TOC should feel like a **margin note**, not a control panel

---

## Typography

### Body Text

* Sans-serif, highly readable, calm
* Examples: Inter, Source Sans 3, IBM Plex Sans
* Line height: **1.6–1.65**
* Dark gray text, not pure black

### Headings

* Same font family as body
* Hierarchy driven by size and weight, not decoration
* H1–H3 only; avoid deep nesting

### Metadata

* Smaller size
* Muted color
* Always visually secondary

### Code

* Monospace font (IBM Plex Mono, JetBrains Mono)
* Slightly smaller than body text
* Low-contrast theme
* Code blocks are calm and readable, never flashy

---

## Color System

### Backgrounds

* Primary: warm off-white / paper-like
  Examples: `#F9FAFB`, `#F8FAFC`
* Avoid stark white and pure black

### Text

* Primary text: dark charcoal
  Example: `#111827`, `#1F2937`
* Secondary / metadata: muted gray
  Example: `#6B7280`

### Accent Color

* Use **one** muted accent color only
* Examples:

  * Desaturated blue / indigo
  * Calm teal
* Accent is used sparingly for:

  * Links
  * Subtle emphasis
* Accent color must never dominate the page

---

## Spacing & Rhythm

* Paragraph spacing: ~1.5rem
* Section spacing: 2–3× paragraph spacing
* Major breaks: visibly distinct
* Never compress spacing to “fit more content”

Whitespace is intentional and functional.

---

## Content Elements

### Images

* Used only when they clarify or support ideas
* Aligned with text column
* No full-bleed images by default
* Subtle rounding only

### Blockquotes

* Simple left rule
* Same font as body
* Muted tone

### Lists

* Generous spacing
* No dense bullet clusters

---

## Interaction Philosophy

* Default experience is static
* Interactions must feel optional
* No animation that competes with reading
* No urgency signals
* Scrolling should feel uninterrupted

---

## Explicit Non-Goals

The site must **not** resemble:

* A startup blog
* A content marketing platform
* A newsletter landing page
* A feed-driven or engagement-optimized site

Avoid:

* Card-heavy layouts
* Loud gradients
* Over-animation
* Trend-driven UI patterns
* Calls to action competing with content

---

## Final Identity Reminder

> This is a **personal, editorial writing space** for a reflective software engineer.
> It is a place to think in public, not to perform.


