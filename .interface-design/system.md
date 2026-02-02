# Design System

## Direction

**Personality:** Warmth & Approachability (Calm Editorial)
**Foundation:** Neutral (Paper/Slate)
**Depth:** Flat / Minimal

## Tokens

### Spacing
Base: 4px
Scale: [4, 8, 12, 16, 24, 32, 48, 64]

### Colors
```css
--color-paper: #F9FAFB;        /* slate-50 */
--color-paper-dark: #F3F4F6;   /* slate-100 */
--color-text: #111827;         /* slate-900 */
--color-text-secondary: #6B7280; /* slate-500 */
--color-accent: #4F6C8F;       /* custom blue-grey */
--color-accent-hover: #3D5A7A; /* darker blue-grey */
--color-border: #E5E7EB;       /* slate-200 */
```

### Radius
Scale: [4px, 8px] (Standard rounded)

### Typography
Font Sans: "Inter", "Source Sans 3", system-ui, sans-serif
Font Mono: "JetBrains Mono", "IBM Plex Mono", ui-monospace, monospace
Scale:
- h1: 3xl (30px/36px)
- h2: 2xl (24px/32px)
- h3: xl (20px/28px)
- h4: lg (18px/28px)
- p: base (1.125rem / 18px)

## Patterns

### Link
- Style: Underline, offset-2
- Color: Accent
- Hover: Accent-hover

### Blockquote
- Border: Left 2px solid border-color
- Text: Italic, text-secondary
- Padding: pl-6

### Code Block
- Font: Mono
- Background: Paper-dark
- Padding: p-4
- Radius: rounded-lg

## Decisions

| Decision | Rationale | Date |
|----------|-----------|------|
| Editorial/Calm Theme | Existing `input.css` defines a specific "calm, editorial palette". | 2026-02-01 |
| Inter + JetBrains Mono | Standard open source pairing for high readability + code clarity. | 2026-02-01 |
| Custom Accent Color | Using `#4F6C8F` (Steel Blue) instead of standard Tailwind blue for a softer feel. | 2026-02-01 |
