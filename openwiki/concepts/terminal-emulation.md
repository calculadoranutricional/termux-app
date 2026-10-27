---
type: concept
title: Terminal Emulation Engine
description: Core terminal emulation engine, buffer management, character width calculations, and escape sequence processing.
tags: [terminal-emulator, vt100, ansi, csi, osc, screen-buffer, character-width]
verified:
  - by: openwiki/0.7.2
    at: 2026-10-26T20:00:32.746Z
sources:
  - id: openwiki-source-972b33d2e5c9563651112440
    resource: repo://terminal-emulator/src/main/java/com/termux/terminal/KeyHandler.java
  - id: openwiki-source-8fd04383c6023ff9af543ae3
    resource: repo://terminal-emulator/src/main/java/com/termux/terminal/TerminalBuffer.java
  - id: openwiki-source-b5f93cca038f7fd429541ae9
    resource: repo://terminal-emulator/src/main/java/com/termux/terminal/TerminalEmulator.java
  - id: openwiki-source-741b633a8091cb0b46ded7fd
    resource: repo://terminal-emulator/src/main/java/com/termux/terminal/TerminalRow.java
  - id: openwiki-source-5662f6220e676e770ea00fc4
    resource: repo://terminal-emulator/src/main/java/com/termux/terminal/WcWidth.java
generated: { by: "openwiki/0.7.2", at: "2026-10-26T20:00:32.746Z" }
---

# Terminal Emulation Engine

The Terminal Emulation Engine is the core component responsible for interpreting raw input streams from shell processes, maintaining screen and scrollback buffers, processing ANSI/VT escape sequences, tracking cursor state, and translating user input keystrokes. It emulates a subset of the X Window System `xterm` terminal, which in turn emulates a subset of Digital Equipment Corporation (DEC) VT100 and VT220 terminals.

## Architecture & Responsibilities

The terminal emulation subsystem is housed in the `com.termux.terminal` package and comprises several key classes:
- **`TerminalEmulator`**: Implements the state machine for processing code points, control characters, and escape sequences (CSI, OSC, DCS, APC), managing cursor positioning, margins, character sets, and graphic rendition attributes.
- **`TerminalBuffer`**: Manages the backing character and style storage, combining both active screen rows and scrollback history rows.
- **`TerminalRow`**: Represents an individual row of text on the screen or scrollback history, storing code points, styles, and line-wrapping flags.
- **`TerminalSession`**: Orchestrates the lifetime of a terminal session, bridging the pseudo-terminal (pty) input/output streams, `TerminalEmulator`, `TerminalBuffer`, and client callbacks.
- **`KeyHandler`**: Translates Android key events and key combinations into the corresponding escape sequences or raw character inputs expected by shells.
- **`WcWidth`**: Calculates Unicode character display widths (East Asian Width / Unicode width tables) to ensure correct column alignment and multi-column glyph handling.

<!-- openwiki: mermaid parse failed and this diagram was converted to a text fence so it does not break rendering. Fix the diagram source and restore the mermaid fence. Parser error: Heuristic: an unescaped angle bracket inside a label breaks rendering; rephrase the label. -->
```text
stateDiagram-v2
    [*] --> ESC_NONE : processCodePoint()
    ESC_NONE --> ESC : Received ESC (0x1B)
    ESC_NONE --> CSI : Received CSI (ESC [)
    ESC --> ESC_CSI : Received [
    ESC --> ESC_OSC : Received ]
    ESC --> ESC_DCS : Received P
    ESC --> ESC_APC : Received _
    
    state ESC_CSI {
        [*] --> CollectingArgs
        CollectingArgs --> CSI_QUESTION : Received ?
        CollectingArgs --> CSI_DOLLAR : Received $
        CollectingArgs --> Execution : Received Final Byte (@-~ / 0x40-0x7E)
    }
    
    state ESC_OSC {
        [*] --> CollectingOscArgs
        CollectingOscArgs --> ESC_OSC__ESC : Received ESC
        ESC_OSC__ESC --> Execution : Received \\ (ST)
        ESC_OSC__ESC --> CollectingOscArgs : Received other char
        CollectingOscArgs --> Execution : Received BEL (0x07)
    }

    Execution --> ESC_NONE : Complete Sequence
```

## State Machine & Escape Sequence Processing

The core processing loop in `TerminalEmulator` (`processCodePoint(int codePoint)`) operates as a state machine driven by `mEscapeState`. When characters arrive from the shell process, they pass through this state machine:

1. **`ESC_NONE`**: Standard text rendering mode. Printable characters are written to the current cursor position, updating column and wrapping state. Control characters like carriage return (`\r`), newline (`\n`), backspace (`\b`), and tab (`\t`) are handled immediately.
2. **`ESC` (0x1B)**: Transitioned upon receiving the Escape character. Subsequent code points select parser branches:
   - `[` transitions to **`ESC_CSI`** (Control Sequence Introducer).
   - `]` transitions to **`ESC_OSC`** (Operating System Command, e.g., window title updates or clipboard operations).
   - `P` transitions to **`ESC_DCS`** (Device Control String, e.g., sixel graphics).
   - `_` transitions to **`ESC_APC`** (Application Program Command).
   - `(` or `)` transitions to character set selection (`ESC_SELECT_LEFT_PAREN` / `ESC_SELECT_RIGHT_PAREN`).
   - `#` transitions to `ESC_POUND` (e.g., screen alignment tests).
   - `%` transitions to `ESC_PERCENT` (character set selection).

### CSI (Control Sequence Introducer) Handling
CSI sequences start with `ESC [` and can include numeric parameters separated by semicolons (`;`), modifier prefixes (`?`, `>`, `$`), and intermediate bytes, terminated by a final byte in the range `0x40` to `0x7E` (`@` through `~`).
- Parameter parsing is handled dynamically via `parseArg(int b)` into `mTerminalControlArgs`.
- Common CSI sequences include cursor movement (`CUU`, `CUD`, `CUF`, `CUB`, `CUP`), screen erasure (`ED`, `EL`), text insertion/deletion (`ICH`, `DCH`, `IL`, `DL`), and Select Graphic Rendition (`SGR` via `mTextStyle`).

### OSC (Operating System Command) & DCS (Device Control String)
- **OSC (`ESC ]`)**: Collected into control arguments until terminated by `BEL` (`\a`) or String Terminator `ST` (`ESC \`). Handled by `doOsc(String string)`, which manages window titles, dynamic color changes, and clipboard interactions.
- **DCS (`ESC P`)**: Collected into raw argument buffers until terminated by `ST` (`ESC \`). Specialized DCS handlers (such as Sixel image rendering via `TerminalSixel`) process the payload block.

## Screen Buffering and Scrollback Management

`TerminalBuffer` manages rows using a circular buffer structure combined with an active screen window and a scrollback history region.

- **`TerminalRow`**: Represents a physical line of text. It stores an array of code points (`mText`) and style attributes (`mStyle`). It maintains flags for line wrapping (`mIsWrapped`) to distinguish hard newlines from soft word-wrapped lines.
- **Scrollback History**: When output exceeds the screen height, top rows scroll off the active screen into the scrollback buffer. `TerminalBuffer` manages the active viewport index (`mScreenRows`), allowing users to scroll back through historical output.
- **Resizing**: When the terminal view changes dimensions (e.g., screen rotation or window resize), `TerminalBuffer` reflows text, re-wrapping lines according to the new column width while preserving cursor relative position where possible.

## Character Width Calculations (`WcWidth`)

Accurate column positioning in a fixed-width terminal requires determining the display width of Unicode code points.
- **`WcWidth.width(int gcs)`**: Evaluates Unicode code points against East Asian Width properties and combining character tables.
- Returns `0` for zero-width combining marks and control characters.
- Returns `1` for standard narrow/wide ASCII and Latin characters.
- Returns `2` for wide and fullwidth characters (e.g., CJK ideographs, certain emoji sequences), ensuring proper grid alignment and cursor advancement across two columns.

## Key Handling

`KeyHandler` translates Android `KeyEvent` instances into the escape sequences and byte streams expected by terminal applications:
- Maps standard keys, function keys (`F1`-`F12`), navigation keys (Arrow keys, Home, End, Page Up, Page Down), and modifier combinations (Control, Alt, Shift).
- Supports application cursor key mode (`DECCKM`) and application keypad mode, altering the sequences emitted for arrow and numeric keypad keys.
- Handles modifier state propagation (e.g., `CTRL+C` producing `0x03`).

## Related Pages
- [Architecture Overview](../architecture/overview.md)
- [Rendering and View Engine](rendering-and-view.md)
<!-- openwiki: broken internal link [session-management.md] file "session-management.md" does not exist. Fix the href or restore the target, then delete this comment. -->
- [Session Management](session-management.md)
