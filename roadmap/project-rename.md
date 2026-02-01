# Project Rename

## Work Unit Summary

- **Status:** approved
- **Problem/Intent:** Project is named "org-blog" but no longer uses org-mode. Name should reflect what it actually is.
- **Constraints:** Requires coordinated changes across multiple systems: GitHub repo, Digital Ocean app platform, local directory, and all Clojure namespaces.
- **Proposed Approach:** Rename to `blog`. Rename in order: local directory, Clojure namespaces (all `org-blog.*` → `blog.*`), GitHub repo, Digital Ocean instance.
- **Decision:** Rename to **blog**.

---

## Notes

### Scope of Changes

1. **Local directory** - `src/org_blog/` → `src/blog/`
2. **Clojure namespaces** - All files in `src/` need `(ns org-blog.*)` -> `(ns blog.*)` and `:require` updates.
   - `blog.core`
   - `blog.posts`
   - `blog.common.markdown`
   - `blog.common.files`
   - `blog.common.components`
   - `blog.pages.*`
   - `blog.dev-server`
3. **Dev Configuration** - Update namespace references in:
   - `dev/user.clj`
   - `dev/codex_init.clj`
4. **Shell Scripts** - Update environment checks and setup logs:
   - `validate-env.sh`
   - `codex-setup.sh`
5. **Documentation** - Update "How to Run" commands and references:
   - `README.md`
   - `README.org`
   - `AGENTS.md`
6. **External Systems** (Manual Steps)
   - **GitHub repo** - Settings → Rename repository
   - **Digital Ocean** - App Platform instance name

### Execution Plan
1. Move `src/org_blog` to `src/blog`.
2. Find and replace `org-blog` with `blog` in `src/`, `dev/`, and `*.sh` files.
3. Update documentation (`README`, `AGENTS.md`).
4. Verify via `clojure -M -e "(require 'blog.core)"`.