# Design Refactor Feedback

- [x] **Name Alignment**: Fix name alignment; it currently feels centered but should be left-aligned.
- [x] **Code Block Styling**: Replace the Solarized Light theme with something that fits the general site theme better.
    - [x] Update to `prism-coy` for a softer look.
- [x] **Now Page**: Restore the content of the 'now' page to match the original Org file version.
- [x] **Kitchen Sink**: Add a "kitchen sink" file to test various content elements.
    - [x] Add images, LaTeX, and wide code blocks.
- [x] **File Organization**: Move old Org files into a directory for discarded posts.
- [x] **Post Metadata & Footer**:
    - [x] Remove the bottom horizontal divider used for "last updated" (clashes with footer).
    - [x] Move "last updated" to the top of the post.
    - [x] Add a "first published" date option.
    - [x] Stack metadata vertically for better readability.
- [x] **Mobile Experience**:
    - [x] Fix horizontal scrolling (nav wrapping, flexbox constraints).
    - [x] Fix list spacing.
- [x] **Layout Consistency**: Ensure Home, Archive, and Resume pages align with Posts (`max-w-4xl`).
    - [x] Implement pixel-perfect alignment by using shared flex container structure with phantom sidebar on all pages.