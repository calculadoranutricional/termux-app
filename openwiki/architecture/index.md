---
type: architecture
title: Architecture & Subsystems
description: System-level component topology, Gradle multi-module hierarchy, background runtime, crash diagnostics, and intent receivers.
tags: [architecture, modules, termux-service, crash-handler, system-events, overview]
---

# Architecture & Subsystems

The **Architecture** domain provides structural overviews of the multi-module Android project, component topologies, persistent background execution runtime, exception handling, and system event dispatching.

## Key Architectural Documents

- **[System Architecture Overview](overview.md)**: High-level system architecture, component topology, processes, and service lifecycle of Termux-App.
- **[Modules and Dependencies](modules-and-dependencies.md)**: Detailed breakdown of the multi-module project structure, Gradle configuration, and inter-module dependencies in Termux app.
- **[TermuxService & Background Runtime Architecture](service-and-background-runtime.md)**: Foreground service management, session container lifecycle, notification action dispatching, and power/wake locks in TermuxService.
- **[Crash Handling & Diagnostics](crash-handling-and-diagnostics.md)**: Process-wide uncaught exception interception, telemetry log persistence, notification alerting, and Markwon crash report rendering in Termux.
- **[System Event Receivers & Open Intent Dispatching](system-event-receivers-and-intents.md)**: System broadcast lifecycle management, package updates, boot triggers, termux-open file sharing, and Android 12+ phantom process handling.

## Quick Navigation

- [Back to Quickstart](../quickstart.md)
- [Terminal Emulation Concepts](../concepts/index.md)
- [Operational Workflows](../workflows/index.md)
