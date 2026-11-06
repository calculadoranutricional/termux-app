---
type: operations
title: Operations & Build Tooling
description: Operational procedures, Gradle build configurations, signing keys, and diagnostic logging in Termux.
tags: [operations, build, gradle, signing, logging, diagnostics]
---

# Operations & Build Tooling

The **Operations** domain covers the build system, artifact compilation, signing keys, APK generation, CLI tooling, and forensic logging mechanisms of the Termux Android ecosystem.

## Key Operational Documents

- **[Build System & Signing Configurations](build-and-signing.md)**: Gradle build targets (`assembleDebug`), NDK compilation, `signingConfigs` keystores (`debug.keystore`, `testkey_untrusted.jks`), split APK architectures, and bootstrap packaging.
- **[Forensics & Logging Subsystem](forensics-and-logging.md)**: Forensic crash interception (`CrashHandler`), markdown report generation (`ReportActivity`), runtime log collection, and log levels.

## Quick Navigation

- [Back to Quickstart](../quickstart.md)
- [System Architecture](../architecture/overview.md)
- [Testing Architecture](../testing/index.md)
