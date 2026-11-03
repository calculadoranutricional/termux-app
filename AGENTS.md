<!-- OPENWIKI:START -->

## OpenWiki

This repository has a generated `openwiki/` evidence index. It is optional just-in-time context, not required startup reading.

- Do not enumerate, preload, or search wikis at task start. Use retrieval when the user asks for it, when unfamiliar architecture or dependency behavior materially affects the task, or when source inspection leaves an important uncertainty. Stop once the question is grounded.
- When those conditions apply and OpenWiki retrieval tools are available, use `openwiki_search` for just-in-time context and `openwiki_read` for the relevant complete sections. If search returns `workspace_required`, ask which listed workspace to use and retry with its ID.
- Use `openwiki_list_workspaces` or `openwiki_list_wikis` when workspace membership itself needs to be discovered.
- If the retrieval tools are unavailable, read `openwiki/quickstart.md` and follow its links to the relevant pages.
- Treat source code and tests as authoritative. A brief's unknowns and review items are verification gaps, not automatic requirements.
- Prefer the narrowest quiet validation that proves the changed behavior. Preserve complete failure output.

The scheduled OpenWiki GitHub Actions workflow refreshes the repository wiki. Do not hand-edit generated OpenWiki pages unless explicitly asked; prefer updating source code/docs and letting OpenWiki regenerate.

<!-- OPENWIKI:END -->

<!-- CODEGRAPH:START -->
## CodeGraph (MANDATORY STEP 2 - NON-INTERACTIVE)

In repositories where `.codegraph/` exists, CodeGraph serves as the mandatory structural navigation engine. It **MUST** always be present and executed in non-interactive mode.

1. **Automatic Installation & Initialization**:
   - If `codegraph` is not found in `$PATH`, the agent **MUST** immediately install it via:
     ```bash
     npm install -g @colbymchenry/codegraph
     ```
   - If the index is not initialized or database is missing, initialize non-interactively with:
     ```bash
     codegraph init -y --no-color
     ```

2. **Strict Non-Interactive Execution**:
   - All CodeGraph commands **MUST** use flags that prevent prompts, interactive TTY requirements, or ANSI formatting issues:
     - Always pass `--no-color` (or set `CODEGRAPH_TELEMETRY=0`).
     - Never run interactive commands or omit `-y` on initialization.
     - For index synchronization: `codegraph sync --no-color` (or `codegraph sync -q`).

3. **Mandatory First-Hop Lookup via OpenWiki Anchors**:
   - You **MUST** use CodeGraph as your primary tool before executing any `grep`, `find`, or raw file inspection (`view_file`).
   - Feed the anchor symbols discovered in Step 1 directly into:
     - `codegraph explore --no-color "<symbol or query>"` (shows source code + call paths).
     - `codegraph node --no-color <symbol>` (inspects caller/callee hierarchy and definitions).
     - `codegraph query --no-color "<search>"` (fast symbol lookup).
   - For JNI/native methods identified in the graph, inspect the corresponding C sources under `src/main/jni/` or `src/main/cpp/`.
   - Bypassing CodeGraph in favor of arbitrary file reading or text grepping when `.codegraph/` is present is strictly prohibited.

4. **Mandatory Index Synchronization**:
   - You **MUST** run `codegraph sync --no-color` immediately after creating, modifying, or deleting source files to preserve index freshness for downstream tasks.
<!-- CODEGRAPH:END -->
