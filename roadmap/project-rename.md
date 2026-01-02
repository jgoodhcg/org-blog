# Project Rename

## Work Unit Summary

- **Status:** idea
- **Problem/Intent:** Project is named "org-blog" but no longer uses org-mode. Name should reflect what it actually is.
- **Constraints:** Requires coordinated changes across multiple systems: GitHub repo, Digital Ocean app platform, local directory, and all Clojure namespaces.
- **Proposed Approach:** Choose a new name. Rename in order: local directory, Clojure namespaces (all `org-blog.*` → new name), GitHub repo, Digital Ocean instance.
- **Open Questions:** What to rename it to?

---

## Notes

### Scope of Changes

1. **Local directory** - `/Users/justingood/projects/org-blog/` → new path
2. **Clojure namespaces** - All files in `src/org_blog/` use `org-blog.*` namespace
   - `org-blog.core`
   - `org-blog.posts`
   - `org-blog.common.markdown`
   - `org-blog.common.files`
   - `org-blog.common.components`
   - `org-blog.pages.*`
   - `org-blog.dev-server`
3. **Directory structure** - `src/org_blog/` → `src/new_name/`
4. **GitHub repo** - Settings → Rename repository
5. **Digital Ocean** - App Platform instance name

### Name Ideas

(to be decided)
