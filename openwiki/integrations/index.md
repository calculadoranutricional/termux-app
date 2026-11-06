---
type: integration
title: Integrations & Plugin Protocols
description: External app integrations, plugin protocol specifications (Termux:API, Termux:Tasker, Termux:Widget), and security boundaries.
tags: [integration, plugins, tasker, widget, api, intents, permissions]
---

# Integrations & Plugin Protocols

The **Integrations** domain outlines how Termux interfaces with external applications, automation tools, and the official plugin ecosystem (`Termux:API`, `Termux:Tasker`, `Termux:Widget`, `Termux:Boot`, `Termux:Float`).

## Key Integration Documents

- **[Plugin Protocol & Intent Dispatching](plugin-protocol-and-intents.md)**: Specification of `RUN_COMMAND` intent protocols, `RunCommandService` execution pipeline, `TermuxPluginUtils`, and asynchronous result callbacks.
- **[Permissions & Security Architecture](permissions-and-security.md)**: The `com.termux.permission.RUN_COMMAND` permission model, sandbox boundaries, pending intent verifications, and dotfile protection mechanisms.

## Quick Navigation

- [Back to Quickstart](../quickstart.md)
- [Command Execution Workflow](../workflows/command-execution.md)
- [System Architecture](../architecture/overview.md)
