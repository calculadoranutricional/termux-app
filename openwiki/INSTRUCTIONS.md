# OpenWiki Documentation Directives & Architectural Standards

This document establishes the canonical maintenance standards, verification rules, and domain taxonomy for the **Termux App Code Wiki** (`openwiki/`).

## The 6 Canonical Architectural Domains

All technical documentation within OpenWiki must strictly adhere to the following 6 domain directories:

1. **`architecture/`**: High-level system structure, multi-module Gradle layout (`:app`, `:terminal-emulator`, `:terminal-view`, `:termux-shared`), background execution runtime (`TermuxService`), crash reporting, and system broadcast receivers.
2. **`concepts/`**: Core emulation algorithms, VT100/ANSI screen grid mechanics, native PTY bridging (`termux.c`), View rendering pipeline (`TerminalView`), touch gestures, virtual keyboards, configuration properties, and low-level POSIX/IPC mechanics.
3. **`workflows/`**: Operational lifecycles including terminal session creation and management, package store installations, bootstrap archive staging, and SAF storage access / file sharing.
4. **`operations/`**: Build system execution (`gradle :app:assembleDebug`), Android NDK compilation, signing key configurations (`signingConfigs`), release/debug packaging, ABI splitting, and forensic log analysis.
5. **`integrations/`**: Inter-Process Communication (IPC) contracts with external applications, plugin protocols (`Termux:API`, `Termux:Tasker`, `Termux:Widget`), `RUN_COMMAND` intent dispatching, and security sandboxing.
6. **`testing/`**: Local JVM testing architecture (`gradle :app:testDebugUnitTest`), VT escape sequence unit test suites, Robolectric fixtures, and verification constraints without local Android emulators or ADB.

## Documentation Structure & Metadata Standards

- **Frontmatter**: Every `.md` file must begin with valid YAML frontmatter containing `type`, `title`, `description`, and `tags`.
- **Diagrams**: Must utilize Mermaid syntax (`flowchart TD`, `sequenceDiagram`, `stateDiagram-v2`) with labels enclosed in double quotes.
- **Cross-References**: All internal links must be relative to the referencing document.
- **Evidence Verification**: Claims in `.claims/` must point to verifiable code locations using the `repo://<relative-path>#L<start>-L<end>` URI format.
- **Integrity Gate**: Modifying documentation requires recomputing SHA-256 pageVersion hashes in `.page-manifest.json` and `.claims/`, updating `.last-update.json`, and running `codegraph sync --no-color`.
