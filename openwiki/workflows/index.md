---
type: workflow
title: Lifecycles & Operational Workflows
description: Comprehensive operational workflows detailing session management, command execution, package store installations, bootstrap staging, and SAF file sharing.
tags: [workflows, session-lifecycle, command-execution, store, bootstrap, storage]
---

# Lifecycles & Operational Workflows

The **Workflows** domain documents end-to-end operational lifecycles, user journeys, package store automation, and integration pipelines across the Termux application.

## Key Workflow Documents

- **[Terminal Session Lifecycle & Management](session-lifecycle.md)**: End-to-end flow of terminal session creation, drawer controller management, UI switching, background execution, and destruction across TermuxService, TermuxSession, and TerminalSession.
- **[Command Execution Workflow](command-execution.md)**: Execution of external commands, RunCommandService intent handling, AmSocketServer integration, and shell environment setup in Termux.
- **[Termux Store & Package Management Workflow](package-management-and-store.md)**: Package discovery, dynamic installation inspection, guide dialogs, direct active session execution, and curated catalog management in TermuxStoreActivity.
- **[Storage Access Framework & External File Sharing Workflow](storage-access-and-file-sharing.md)**: Document provider integration via TermuxDocumentsProvider and external file/URL sharing via FileReceiverActivity.
- **[Termux Bootstrap & Environment Installation Workflow](bootstrap-installation.md)**: Packaging, native JNI bootstrap extraction, symlink resolution, POSIX permissions setup, and storage initialization in TermuxInstaller and TermuxBootstrap.

## Quick Navigation

- [Back to Quickstart](../quickstart.md)
- [System Architecture](../architecture/index.md)
- [Core Concepts](../concepts/index.md)
