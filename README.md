# My Static Blog Generator

## What is this?
A personal static blog generator written in Clojure. It converts Markdown files to HTML for my personal [website](https://jgood.online). The generated static site is hosted on Digital Ocean App Platform.

## How it works
The generator takes Markdown files from the posts and pages directories, processes them through Pandoc, and applies styling and layout templates using Hiccup. It includes:

- Automatic conversion of Markdown to HTML using Pandoc
- Development server
- Responsive design using Tailwind CSS
- Automatic table of contents generation
- Read time estimation
- RSS feed generation
- Support for custom pages
- Code syntax highlighting
- LaTeX rendering in the browser

## Project Structure
- `posts/` - Blog posts written in Markdown
- `pages/` - Custom static pages
- `src/` - Clojure source code for generating the site
- `static/` - Generated site output
- `css/` - Tailwind CSS source files

## Technical Requirements
- [Pandoc](https://pandoc.org/) for Markdown conversion
- [Clojure](https://clojure.org/) (1.10+)
- [Tailwind CSS](https://tailwindcss.com/blog/standalone-cli) standalone binary

## Local Development Setup

The project uses `just` as a command runner for common tasks.

```shell
# Install e2e dependencies
just e2e-install

# Start the dev server and Tailwind CSS watcher
just dev

# Regenerate all static files (one-off)
just build

# Preview at http://localhost:8081
```

### Manual Setup (Alternative)

If you don't have `just` installed:

```shell
# Start an nREPL server (for editor connection)
clj -M:nrepl
```

In the REPL:
```clojure
(org-blog.dev-server/start-server)    ; Start server on port 8081
(org-blog.core/regenerate-site)       ; Regenerate all static files
```

In a separate terminal:
```shell
tailwindcss -i ./css/input.css -o ./static/css/output.css --watch
```

## Visual Validation & Testing

We use Playwright for smoke tests and visual "agentic flows" to ensure the site looks correct.

```shell
# Run smoke tests
just smoke

# Run a navigation flow and capture screenshots
just flow-nav

# Take a screenshot of a specific route
just screenshot /resume
```

Screenshots are saved to `e2e/screenshots/`.

### Connecting Your Editor

Start the nREPL server with `clj -M:nrepl`. It will print a port number.

- **Emacs/CIDER**: `M-x cider-connect-clj` → localhost → port
- **VS Code/Calva**: Command Palette → "Calva: Connect to a Running REPL Server"
- **IntelliJ/Cursive**: Run → Edit Configurations → Clojure REPL → Remote

## Content Creation Process

### Writing Blog Posts
Write Markdown files in the `posts/` directory using the filename format `YYYY-MM-DD-title.md`.

Posts use YAML frontmatter for metadata:

```markdown
---
title: My Post Title
date: 2024-01-15
description: A description of the post
thumbnail: /img/thumbnail.png
tags: [tag1, tag2, tag3]
---

Post content here...
```

After regenerating static files, commit changes and Digital Ocean automatically publishes the updated site.

### Creating Static Pages
Place Markdown content in the `pages/` directory. Some pages skip Markdown entirely and are written directly in Clojure using Hiccup (like the resume page which uses EDN data).

## Deployment
The site is automatically deployed when changes are pushed to the main branch. Digital Ocean App Platform publishes all files from the `static/` directory.

## License

### Software License
The code for this blog generator is provided under the MIT License:

MIT License

Copyright (c) 2023-2025 Justin Good

### Content License
The blog content (posts, articles, and other written materials) is Justin Good.

All rights reserved. The content of this blog may not be used for training AI models, machine learning algorithms, or other automated systems without explicit permission from the author. You may read, share, and link to the content, but usage for AI training, republishing, or commercial purposes requires prior written consent.
