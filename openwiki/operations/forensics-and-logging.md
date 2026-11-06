---
type: operations
title: Forensics & Logging Subsystem
description: Diagnostic telemetry, uncaught exception handling with CrashHandler, markdown reporting, and runtime logging in Termux.
tags: [operations, logging, diagnostics, crash, forensics, markwon]
---

# Forensics & Logging Subsystem

Termux integrates a comprehensive diagnostic and telemetry capture pipeline designed to intercept runtime errors, generate structured forensic crash reports, and display them in a clean Markdown viewer without requiring an external debugger.

## Forensic Interception Flow

```mermaid
sequenceDiagram
    autonumber
    participant App as "Termux Application / Service"
    participant Handler as "CrashHandler ('termux-shared')"
    participant File as "Crash Log File ('crash_log.md')"
    participant Viewer as "ReportActivity ('io.noties.markwon')"

    App->>Handler: Uncaught Exception thrown on Thread
    Handler->>Handler: Collect Device, OS & App Context
    Handler->>File: Write Structured Forensic Markdown Report
    Handler->>Viewer: Launch ReportActivity via System Intent
    Viewer->>Viewer: Parse and Render via Markwon Engine
    Viewer-->>App: Provide Share & Copy Actions to User
```

## Core Components

### 1. Uncaught Exception Interception (`CrashHandler.java`)
`CrashHandler` implements `Thread.UncaughtExceptionHandler`. Initialized during `TermuxApplication.onCreate()`, it traps unexpected runtime failures:
- Captures active thread state, stack traces, and cause chains.
- Collects environment details: Android OS build version, kernel release, architecture (`Build.SUPPORTED_ABIS`), Termux app version, and package variant.
- Serializes telemetry into a structured Markdown document saved under the app's internal cache or storage directory.

### 2. Runtime Telemetry Viewer (`ReportActivity.java`)
When a crash occurs or when the user invokes diagnostics, `ReportActivity` is launched:
- Uses the **Markwon** markdown rendering library (`io.noties.markwon:core`, `io.noties.markwon:ext-strikethrough`, `io.noties.markwon:linkify`, `io.noties.markwon:recycler`).
- Displays formatted stack traces in monospace blocks with syntax styling.
- Features top app bar action items allowing the user to copy the raw markdown to the clipboard or share it via Android's `Intent.ACTION_SEND`.

### 3. Unified Logger (`Logger.java`)
`com.termux.shared.logger.Logger` provides granular logging across all modules:
- Supports standard log levels: `VERBOSE`, `DEBUG`, `INFO`, `WARN`, `ERROR`.
- Features extended logging methods (e.g. `logVerboseExtended`) for multiline intent dumps and shell outputs.
- Integrates with user settings (`TermuxAppSharedPreferences`) allowing dynamic log level toggling without app recompilation.

## Related Documents
- [Crash Handling & Diagnostics Architecture](../architecture/crash-handling-and-diagnostics.md)
- [Build System & Signing Configurations](build-and-signing.md)
- [Settings & Preferences](../concepts/settings-and-preferences.md)
