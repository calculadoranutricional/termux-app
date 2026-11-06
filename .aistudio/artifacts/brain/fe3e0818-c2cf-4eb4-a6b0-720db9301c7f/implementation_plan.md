# Auditoría Técnica Integral y Sincronización Criptográfica de OpenWiki

Auditoría técnica exhaustiva y sincronización matemática de la base documental en `openwiki/` y sus metadatos criptográficos (`.page-manifest.json`, `.claims/`, `.last-update.json`) frente a la implementación real del código fuente de Termux en disco.

## User Review & Critical Decisions

> [!IMPORTANT]
> Decisiones confirmadas en la Fase 1 de alineación interactiva:
> - **Alcance de Auditoría**: Auditoría integral y exhaustiva sobre los 6 dominios canónicos (`architecture/`, `concepts/`, `workflows/`, `operations/`, `integrations/`, `testing/`) y todos sus metadatos asociados.
> - **Verificación de Claims**: Verificación estricta línea a línea y actualización de las URIs de evidencia (`repo://...#Lxx-Lyy`) contra los números de línea reales del código fuente actual.
> - **Independencia de Git**: Ejecución puramente basada en lectura y cómputo directo de hashes e inspección de disco (sin invocación a comandos `git log`, `git diff` ni `git rev-parse`).

---

## 1. Overview & Core Concept

- **Propósito**: Garantizar la veracidad técnica, integridad estructural y consistencia criptográfica al 100% de la documentación arquitectónica de Termux en `openwiki/`, contrastando cada afirmación técnica con la implementación real en disco de los 4 módulos Gradle (`:app`, `:terminal-emulator`, `:terminal-view`, `:termux-shared`) y el runtime nativo C/JNI.
- **Destinatarios**: Desarrolladores del core de Termux, ingenieros de integración de plugins (`Termux:API`, `Termux:Tasker`, `Termux:Widget`), y agentes de IA que navegan y extienden la plataforma mediante `AGENTS.md`.
- **Valor Clave**: Elimina alucinaciones técnicas, documenta con precisión los componentes recientes (catálogo de Termux Store, aliases Zsh, Gboard IME content commit, control de sesiones) y certifica la validez matemática del validador Zod de OpenWiki.

---

## 2. User Experience & Documentation System Design

- **Estructura de Navegación**:
  - `openwiki/quickstart.md`: Matriz central de enrutamiento por tareas hacia los 6 dominios.
  - `openwiki/INSTRUCTIONS.md`: Normas y directivas arquitectónicas obligatorias.
  - Índices canónicos: `index.md` en cada una de las 6 carpetas temáticas para acceso jerárquico.
- **Estándar Tipográfico y Visual**:
  - Frontmatter YAML canónico en la línea 1 de cada página (`type`, `title`, `description`, `tags`).
  - Diagramas Mermaid en bloques con etiquetas explícitas entre comillas dobles para compatibilidad de parsing.
  - Enlaces relativos estrictos entre documentos de la wiki.
- **Trazabilidad de Afirmaciones (Claims & Evidence)**:
  - Cada página `.md` cuenta con su contraparte en `.claims/<dominio>/<archivo>.json`.
  - Cada claim declara su afirmación técnica verificable vinculada a evidencia precisa mediante `repo://<ruta>#L<inicio>-L<fin>`.

---

## 3. Key Product Decisions & Trade-Offs

- **Decisión 1: Inspección de Disco vs. Historial Git**
  - *Enfoque Elegido*: Lectura e inspección estática directa con herramientas nativas (`view_file`, cálculo SHA-256 en memoria o script Python/Node sin Git).
  - *Razón*: El entorno de ejecución en la nube no garantiza la presencia de metadatos de historial git completos ni ramas upstream, haciéndolo 100% determinista y resiliente.
- **Decisión 2: Sincronización Criptográfica Ordenada**
  - *Enfoque Elegido*: Escritura y formato final de los `.md` primero -> cálculo SHA-256 exacto de 64 caracteres -> inyección simétrica en `.page-manifest.json` y `.claims/`.
  - *Razón*: Garantiza que `pageVersion` coincida de manera idéntica en el manifiesto Zod y en los claims, evitando discrepancias de validación.

---

## 4. Technical Architecture & Verification Strategy

### Arquitectura de Integridad de OpenWiki

```
┌──────────────────────────────────────────────────────────────────────────────────┐
│                   PIPELINE DE AUDITORÍA Y SINCRONIZACIÓN OPENWIKI                │
├──────────────────────────────────────────────────────────────────────────────────┤
│                                                                                  │
│   [1. INSPECCIÓN EN DISCO] ──► Código Fuente Real (Java, C, XML, Gradle)         │
│             │                                                                    │
│             ├── Contraste de Componentes: TermuxStore, TerminalEmulator, etc.   │
│             ▼                                                                    │
│   [2. AUDITORÍA 6 DOMINIOS] ──► openwiki/{architecture,concepts,workflows,...}   │
│             │                                                                    │
│             ├── Actualización / Redacción de páginas con Frontmatter & Mermaid   │
│             ▼                                                                    │
│   [3. CÓMPUTO SHA-256] ──► Cálculo de pageVersion para cada archivo .md          │
│             │                                                                    │
│             ├── Inyección simétrica en .page-manifest.json                       │
│             ├── Sincronización de claims en .claims/ (repo://...#Lxx-Lyy)        │
│             ├── Actualización de openwiki/.last-update.json (timestamp UTC)      │
│             ▼                                                                    │
│   [4. QUALITY GATE] ──► compile_applet ──► gradle :app:testDebugUnitTest        │
│             │                                                                    │
│             └── codegraph sync --no-color ──► Certificación Final                │
│                                                                                  │
└──────────────────────────────────────────────────────────────────────────────────┘
```

### Componentes y Dominios a Auditar

1. **`architecture/`**:
   - `overview.md`, `modules-and-dependencies.md`, `service-and-background-runtime.md`, `crash-handling-and-diagnostics.md`, `system-event-receivers-and-intents.md`.
2. **`concepts/`**:
   - `terminal-emulation.md`, `native-jni-and-pty.md`, `rendering-and-view.md`, `virtual-keyboard-and-extra-keys.md`, `settings-and-preferences.md`, `ipc-and-socket-server.md`, `shell-environment-and-execution.md`, `filesystem-and-posix-dispatcher.md`.
3. **`workflows/`**:
   - `session-lifecycle.md`, `command-execution.md`, `package-management-and-store.md`, `storage-access-and-file-sharing.md`, `bootstrap-installation.md`.
4. **`operations/`**:
   - `build-and-signing.md`, `forensics-and-logging.md`.
5. **`integrations/`**:
   - `plugin-protocol-and-intents.md`, `permissions-and-security.md`.
6. **`testing/`**:
   - `unit-testing-and-test-suites.md`.

### Plan de Verificación y Quality Gate

1. **Compilación Limpia**: Ejecución de `compile_applet` para confirmar cero errores de build en el código.
2. **Pruebas Unitarias JVM**: Ejecución de `gradle :app:testDebugUnitTest` para certificar que todas las pruebas pasen sin regresión.
3. **Sincronización de Grafo**: Ejecución de `codegraph sync --no-color`.
4. **Validación Criptográfica**: Verificación estricta de que todos los hashes SHA-256 de las páginas correspondan exactamente a los registrados en el manifiesto y en los claims.
