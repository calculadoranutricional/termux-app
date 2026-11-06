---
type: integration
title: Permissions & Security Architecture
description: Security boundaries, RUN_COMMAND dangerous permission model, sharedUserId, and dotfile sandbox isolation in Termux.
tags: [integration, security, permissions, run-command, sandbox, dotfiles]
---

# Permissions & Security Architecture

Termux operates as a full-featured Linux environment inside an Android application sandbox. Because execution privileges could expose device resources or user private keys, Termux implements a strict multi-layer security architecture governing inter-app communication, file access, and process spawning.

## Security Layers

```mermaid
flowchart TD
    A["External Application / Plugin"] -->|Requires Permission| B["com.termux.permission.RUN_COMMAND (dangerous)"]
    B --> C{"Is Caller Granted?"}
    C -- No --> D["SecurityException / Rejected (EACCES)"]
    C -- Yes --> E["Path Verification & Sanity Check"]
    
    E --> F{"Executable in Sandbox or Whitelist?"}
    F -- No --> G["Execution Blocked (Invalid Path)"]
    F -- Yes --> H["Process Spawned in App UID"]
    
    H --> I["Dotfile Protection Check ('TermuxOpenReceiver$ContentProvider')"]
    I -->|Dotfiles (.ssh, .termux)| J["Access Denied / Forbidden"]
    I -->|Allowed Path| K["File Descriptor Opened"]
```

## Core Security Mechanisms

### 1. Dangerous Permission Model (`RUN_COMMAND`)
In `app/src/main/AndroidManifest.xml`:
```xml
<permission
    android:name="${TERMUX_PACKAGE_NAME}.permission.RUN_COMMAND"
    android:description="@string/permission_run_command_description"
    android:icon="@mipmap/ic_launcher"
    android:label="@string/permission_run_command_label"
    android:protectionLevel="dangerous" />
```
- Because its protection level is `dangerous`, any third-party application requesting command execution must explicitly ask the user for permission at runtime on Android 6.0+ (API 23+).
- Calling `RunCommandService` without holding this permission throws a system `SecurityException`, immediately dropping the incoming intent.

### 2. Signature & Shared User ID Model (`sharedUserId`)
In `AndroidManifest.xml`, Termux declares:
```xml
android:sharedUserId="${TERMUX_PACKAGE_NAME}"
android:sharedUserLabel="@string/shared_user_label"
```
- Official companion packages (`Termux:API`, `Termux:Boot`, `Termux:Widget`, `Termux:Float`, `Termux:Tasker`) signed with the same private release key share the Linux UID.
- This allows secure internal shared storage and direct socket access while maintaining isolation from untrusted external apps.

### 3. Dotfile & Sensitive Directory Protection
Termux protects private configuration files (such as SSH private keys in `~/.ssh/` or configuration tokens in `~/.termux/`):
- `TermuxOpenReceiver$ContentProvider` inspects requested URIs and denies access if the target path resides within protected hidden directories (`.*`).
- Prevents malicious external apps from requesting file viewing or sharing intents that read sensitive credentials.

## Related Documents
- [Plugin Protocol & Intent Dispatching](plugin-protocol-and-intents.md)
- [System Architecture Overview](../architecture/overview.md)
- [Storage Access & File Sharing](../workflows/storage-access-and-file-sharing.md)
