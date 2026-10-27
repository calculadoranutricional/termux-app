<!-- CODEGRAPH_START -->
## CodeGraph

In repositories indexed by CodeGraph (a `.codegraph/` directory exists at the repo root), reach for it BEFORE grep/find or reading files when you need to understand or locate code:

- **Primary Command**: `codegraph explore "<symbol or question>"` (shows source code + call paths in one shot).
- **Targeted Inspection**: Use `codegraph node <symbol>` for caller/callee hierarchy, or `codegraph query "<search>"` for fast symbol lookup.
- **Index Sync**: Run `codegraph sync` after editing code to keep the index fresh. Refer to `codegraph --help` for full options.

If there is no `.codegraph/` directory, skip CodeGraph entirely — indexing is the user's decision.
<!-- CODEGRAPH_END -->

<!-- OPENWIKI:START -->
## OpenWiki

This repository has a generated `openwiki/` evidence index. It is optional just-in-time context, not required startup reading:

- Do not preload wikis at task start. Inspect `openwiki/quickstart.md` or topic files only when unfamiliar architecture affects the task.
- Treat source code and tests as authoritative over generated summaries.
- Local updates can be run via `openwiki --update`, while the scheduled GitHub Actions workflow refreshes the documentation automatically on GitHub.
- Do not hand-edit generated OpenWiki pages; prefer updating source code and tests.
<!-- OPENWIKI:END -->