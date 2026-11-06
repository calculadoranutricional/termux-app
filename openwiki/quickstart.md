---
type: concept
title: Quickstart & Wiki Navigation
description: Central routing and task-oriented overview across all 6 canonical domains for the Termux App code wiki.
tags: [quickstart, navigation, routing, architecture, terminal, operations, integrations, testing]
---

# Quickstart & Wiki Navigation

Welcome to the **Termux App Code Wiki**, an in-depth developer documentation reference covering the architecture, internal modules, terminal emulation engine, view rendering, workflows, build operations, integrations, and testing semantics of the Termux Android application (`termux-app`).

## Wiki Routing Map across the 6 Canonical Domains

Use this guide to navigate across the 6 canonical domains of the Termux App codebase. Each page explores a critical subsystem with architectural overviews, control flows, state lifecycles, and verification points.

### 1. Architecture (`architecture/`)
- **[System Architecture Overview](architecture/overview.md)**: High-level system architecture, component topology, process/service structure, and security boundaries.
- **[Modules and Dependencies](architecture/modules-and-dependencies.md)**: Breakdown of Gradle modules (`:app`, `:terminal-emulator`, `:terminal-view`, `:termux-shared`), inter-module dependencies, and library integration.
- **[TermuxService & Background Runtime Architecture](architecture/service-and-background-runtime.md)**: Foreground service lifecycle, headless multi-client session management, wakelocks, and persistent notification actions.
- **[Crash Handling & Diagnostics](architecture/crash-handling-and-diagnostics.md)**: Uncaught exception handling (`CrashHandler`), forensic telemetry (`TermuxCrashUtils`), notification alerts, and Markwon-powered markdown reports (`ReportActivity`).
- **[System Event Receivers & Open Intent Dispatching](architecture/system-event-receivers-and-intents.md)**: System broadcast lifecycle management (`SystemEventReceiver`), package updates, boot triggers, `termux-open` file sharing, and Android 12+ phantom process handling.

### 2. Concepts (`concepts/`)
- **[Terminal Emulation Engine](concepts/terminal-emulation.md)**: DEC VT/ANSI escape sequence processing, screen buffer management (`TerminalBuffer`, `TerminalRow`), color schemes, and screen text handling.
- **[Native JNI & Pseudoterminal (PTY) Subsystem](concepts/native-jni-and-pty.md)**: Native C PTY allocation (`/dev/ptmx`), termios input mode configuration, JNI bridges (`termux.c`), and `SO_PEERCRED` local socket security.
- **[Terminal View & Rendering](concepts/rendering-and-view.md)**: UI rendering pipeline in `TerminalView`, touch gesture translation, scrolling, font metrics, text selection, and rich clipboard content resolution.
- **[Virtual Keyboard & Extra Keys Toolbar](concepts/virtual-keyboard-and-extra-keys.md)**: On-screen extra keys bar (`ExtraKeysView`), modifier state machine (Ctrl, Alt, Fn), swipe macros, and `termux.properties` key mapping.
- **[Settings & Properties Architecture](concepts/settings-and-preferences.md)**: Dual-tier configuration model (`TermuxAppSharedPreferences` GUI vs `termux.properties` dotfile) and live styling reload pipeline (`ACTION_RELOAD_STYLE`).
- **[Subprocess IPC & Android Manager Socket Server](concepts/ipc-and-socket-server.md)**: Native `AF_UNIX` domain socket server (`AmSocketServer`), `SO_PEERCRED` security verification, and high-performance `am` intent execution.
- **[Shell Environment & Background Execution Subsystem](concepts/shell-environment-and-execution.md)**: Hierarchical POSIX and Android shell environment synthesis (`IShellEnvironment`), non-interactive `AppShell` process execution, and `StreamGobbler` stream handling.
- **[Filesystem & POSIX Dispatcher Subsystem](concepts/filesystem-and-posix-dispatcher.md)**: Low-level POSIX filesystem inspection, `NativeDispatcher` syscall bridging (`stat`/`lstat`), `FileAttributes` metadata extraction, and safe file operations.

### 3. Workflows (`workflows/`)
- **[Terminal Session Lifecycle & Management](workflows/session-lifecycle.md)**: End-to-end flow of terminal session creation (`TerminalSession`), session drawer controller (`TermuxSessionsListViewController`), failsafe sessions, process execution, and destruction.
- **[Command Execution Workflow](workflows/command-execution.md)**: External command execution, `RunCommandService` intent handling, broadcast receivers, and shell integration.
- **[Termux Store & Package Management](workflows/package-management-and-store.md)**: Curated package catalog discovery, dynamic binary presence inspection, guide dialogs, and direct active session execution (`ACTION_EXECUTE_IN_CURRENT_SESSION`).
- **[Storage Access & File Sharing](workflows/storage-access-and-file-sharing.md)**: Storage Access Framework integration (`TermuxDocumentsProvider`) exposing `$HOME`, and external share intent handling (`FileReceiverActivity`) for downloads, URLs, and scripts.
- **[Termux Bootstrap & Environment Installation](workflows/bootstrap-installation.md)**: Native archive extraction (`libtermux-bootstrap.so`), staging promotion, `SYMLINKS.txt` resolution, POSIX permissions, and initial storage setup.

### 4. Operations (`operations/`)
- **[Build System & Signing Configurations](operations/build-and-signing.md)**: Gradle build targets (`assembleDebug`), NDK compilation, `signingConfigs` keystores (`debug.keystore`, `testkey_untrusted.jks`), split APK architectures, and bootstrap packaging.
- **[Forensics & Logging Subsystem](operations/forensics-and-logging.md)**: Forensic crash interception (`CrashHandler`), markdown report generation (`ReportActivity`), runtime log collection, and log levels.

### 5. Integrations (`integrations/`)
- **[Plugin Protocol & Intent Dispatching](integrations/plugin-protocol-and-intents.md)**: IPC intent contracts, `RUN_COMMAND` service pipeline, result callbacks, and Termux plugin ecosystem communication (`Termux:API`, `Termux:Tasker`, `Termux:Widget`).
- **[Permissions & Security Architecture](integrations/permissions-and-security.md)**: Security boundaries, `RUN_COMMAND` dangerous permission model, `sharedUserId`, and dotfile sandbox isolation.

### 6. Testing (`testing/`)
- **[Unit Testing & Test Suites](testing/unit-testing-and-test-suites.md)**: Architecture of local JVM unit tests, `TerminalTestCase` mock harness, Robolectric fixtures, parser assertions, and zero-emulator verification policies.

---

## Development Quickstart

### Project Structure
Termux is structured as a multi-module Android Gradle project (`settings.gradle`):
1. **`:app`**: The primary Android application module containing UI activities (`TermuxActivity`, `TermuxStoreActivity`, `SettingsActivity`), services (`TermuxService`, `RunCommandService`), receivers (`TermuxOpenReceiver`, `SystemEventReceiver`), and configuration handlers.
2. **`:terminal-emulator`**: Pure Java core terminal emulation logic, VT100/ANSI parser, grid buffers, sixel graphics support, and terminal state machine.
3. **`:terminal-view`**: Android View implementation (`TerminalView`) responsible for rendering terminal grids, handling input gestures, and text selection.
4. **`:termux-shared`**: Shared utility library containing file permissions, intents, notification helpers, and common utilities used across Termux apps and plugins.

### Building & Testing Commands
```bash
# Compile debug APK
gradle :app:assembleDebug

# Run JVM unit tests across the app module
gradle :app:testDebugUnitTest

# Run terminal emulator unit tests
gradle :terminal-emulator:test
```

---

## Contribution & Maintenance Standards
When exploring or updating the wiki:
- Keep cross-references canonical and relative to the current page directory.
- Ground architectural claims in repository files (`repo://<path>#L...`).
- Provide Mermaid sequence or flowchart diagrams for complex control flows and lifecycles.
- Keep `.page-manifest.json` and `.claims/` synchronized with exact SHA-256 page hashes.
