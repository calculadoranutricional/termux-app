---
type: concept
title: Core Concepts & Subsystems
description: In-depth technical guides covering terminal emulation, native JNI/PTY bridges, view rendering, extra keys, dual settings, and POSIX dispatchers.
tags: [concepts, emulation, jni, pty, rendering, extra-keys, settings, ipc, posix]
---

# Core Concepts & Subsystems

The **Concepts** domain explores the foundational mechanics and low-level implementations powering terminal emulation, input translation, hardware key mapping, local IPC, and POSIX filesystem interactions.

## Key Conceptual Documents

- **[Terminal Emulation Engine](terminal-emulation.md)**: Core terminal emulation engine, buffer management, character width calculations, and escape sequence processing.
- **[Native JNI & Pseudoterminal (PTY) Subsystem](native-jni-and-pty.md)**: Architecture and low-level mechanics of native PTY subprocess spawning, termios configuration, and local Unix domain socket credential verification.
- **[Terminal View & Rendering](rendering-and-view.md)**: UI rendering pipeline, touch gestures, touch input handling, Gboard rich content pasting, and text selection in terminal-view.
- **[Virtual Keyboard & Extra Keys Toolbar](virtual-keyboard-and-extra-keys.md)**: Architecture of the virtual extra keys bar, touch gestures, special modifier buttons, termux.properties matrix configuration, and terminal input dispatching.
- **[Settings & Properties Architecture](settings-and-preferences.md)**: Dual configuration subsystem spanning Android SharedPreferences and file-based termux.properties with live reload broadcast mechanics.
- **[Subprocess IPC & Android Manager Socket Server](ipc-and-socket-server.md)**: Native AF_UNIX domain socket server architecture, peer credential validation, JNI integration, and high-performance am intent execution without JVM startup overhead.
- **[Shell Environment & Background Execution Subsystem](shell-environment-and-execution.md)**: Hierarchical POSIX and Android shell environment synthesis, non-interactive AppShell process execution, and StreamGobbler stream handling.
- **[Filesystem & POSIX Dispatcher Subsystem](filesystem-and-posix-dispatcher.md)**: Low-level POSIX filesystem inspection, NativeDispatcher syscall bridging, FileAttributes metadata extraction, and safe file operations.

## Quick Navigation

- [Back to Quickstart](../quickstart.md)
- [System Architecture](../architecture/index.md)
- [Operational Workflows](../workflows/index.md)
