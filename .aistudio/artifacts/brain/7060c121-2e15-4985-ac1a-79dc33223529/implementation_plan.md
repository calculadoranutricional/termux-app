# Auditoría Técnica y Sincronización Integral de OpenWiki

Plan maestro para auditar, sincronizar y expandir la base documental de OpenWiki hacia los 6 dominios canónicos requeridos (`architecture/`, `concepts/`, `workflows/`, `operations/`, `integrations/`, `testing/`), implementando la verificación matemática de hashes SHA-256 en `.page-manifest.json`, esquemas Zod en `.claims/`, y sincronización con el estado real del repositorio Termux.

---

## User Review & Critical Decisions

> [!IMPORTANT]
> Se identificó una brecha estructural en la base documental existente: los directorios `operations/`, `integrations/` y `testing/` no existen en disco ni en el manifiesto actual. Además, `.last-update.json` quedó marcado con estado `"interrupted"` en una ejecución previa.
>
> Procederemos a expandir la arquitectura documental sin alterar el código fuente de producción, garantizando consistencia criptográfica al 100% con los esquemas Zod de OpenWiki.

- **Confirmación de Dominio Operativo**: Se crearán los 3 dominios faltantes (`openwiki/operations/`, `openwiki/integrations/`, `openwiki/testing/`) con sus respectivas páginas de contenido, archivos `index.md` y reclamos verificables bajo `openwiki/.claims/`.
- **Integridad de Hash Criptográfico**: Cada documento `.md` nuevo o modificado tendrá su digest SHA-256 (`^sha256:[a-f0-9]{64}$`) calculado inmediatamente tras su escritura para evitar discrepancias en `.page-manifest.json` y `.claims/`.
- **Preservación de Metadatos de Git**: Dado que el entorno opera en un contenedor sin repositorio git inicializado (`fatal: not a git repository`), preservaremos los identificadores `gitHead` canónicos existentes (`"HEAD"` o commits preexistentes) en `.page-manifest.json` y `.last-update.json`.

---

## 1. Overview & Core Concept

OpenWiki actúa como el grafo de conocimiento arquitectónico y contextual de Termux para agentes de desarrollo e ingenieros humanos. Permite realizar diagnósticos previos al código (*Read-Before-Write*), reduciendo alucinaciones y acelerando la navegación relacional.

Esta auditoría técnica evalúa la implementación real de Termux en disco (módulos `:app`, `:terminal-emulator`, `:terminal-view`, `:termux-shared`, capa nativa C/C++ y tooling Gradle) y sincroniza exhaustivamente la documentación en los 6 dominios canónicos:

1. **`architecture/`**: Jerarquía modular Gradle, runtime de background (`TermuxService`), y ciclo de vida de system receivers / intents.
2. **`concepts/`**: Motor DEC VT/ANSI (`TerminalEmulator`), puente nativo JNI/PTY (`termux.c`), renderizado `Canvas` (`TerminalView`), despachador POSIX y servidor socket `AmSocketServer`.
3. **`workflows/`**: Ciclo de vida de sesiones, flujo de inicio bootstrap (`TermuxInstaller`), integración SAF (`TermuxDocumentsProvider`) y catálogo Store (`TermuxStoreActivity`).
4. **`operations/`** *(Nuevo)*: Procedimientos de compilación con Gradle (`gradle :app:assembleDebug`), configuración de firma (`signingConfigs` debug / release), tooling CLI y telemetría forense de fallos.
5. **`integrations/`** *(Nuevo)*: Protocolos IPC con plugins de terceros (Termux:API, Termux:Tasker, Termux:Widget), Intent contracts y permisos de ejecución (`com.termux.permission.RUN_COMMAND`).
6. **`testing/`** *(Nuevo)*: Estrategia de pruebas unitarias locales JVM (`gradle :app:testDebugUnitTest`), testing de secuencias de escape ANSI/VT, restricciones de ejecución sin emulador local y reglas Robolectric.

---

## 2. User Experience & Visual Design (Estructura de Documentación)

### Jerarquía y Navegación
La base documental se estructurará de forma simétrica para facilitar la indexación vectorial y la búsqueda semántica:

```
openwiki/
├── quickstart.md                     # Entrada universal con enrutamiento a los 6 dominios
├── index.md                          # Portal raíz con catálogo de dominios
├── INSTRUCTIONS.md                   # Directivas de actualización y mantenimiento
├── .page-manifest.json               # Manifiesto Zod con pageVersion y sourceFingerprint
├── .last-update.json                 # Estado global de actualización (status: success)
├── .claims/                          # Reclamos técnicos formales con URIs repo://
│   ├── architecture/
│   ├── concepts/
│   ├── workflows/
│   ├── operations/
│   ├── integrations/
│   └── testing/
├── architecture/                     # Topología de sistemas y servicios
├── concepts/                         # Fundamentos teóricos y mecanismos POSIX/VT
├── workflows/                        # Flujos operativos e interacción con el usuario
├── operations/                       # Build system, signing, debug logs
├── integrations/                     # Plugins externos y contratos de intents
└── testing/                          # Test suites, aserciones y límites de ejecución
```

### Reglas de Estilo y Formato
- **Frontmatter YAML**: Encabezado en la línea 1 con `type`, `title`, `description` y `tags`.
- **Diagramas Mermaid**: Diagramas `flowchart TD` y `sequenceDiagram` con nodos y etiquetas entre comillas dobles (`["..."]`) para garantizar compatibilidad con renderizadores Markdown.
- **Enlaces Relativos Robustos**: Todos los enlaces cruzados utilizarán rutas relativas válidas (ej. `[Volver a Operations](./index.md)` o `[Ver TerminalEmulator](../concepts/terminal-emulation.md)`).

---

## 3. Key Product Decisions & Trade-Offs

### Decisión 1: Creación de Dominios Faltantes vs Extensión de Archivos Existentes
- **Enfoque Elegido**: Crear directorios dedicados para `operations/`, `integrations/` y `testing/`, cada uno con su respectivo `index.md` y documentos técnicos especializados.
- **Razón**: El estándar canónico de 6 dominios de OpenWiki requiere una separación nítida entre compilación/operaciones, interfaces de integración externa y aseguramiento de calidad/testing, evitando sobrecargar las páginas de arquitectura.
- **Alternativa Descartada**: Fusionar la información de testing y compilación en `architecture/overview.md`. Habría inflado el archivo por encima del umbral de contexto recomendado y roto la especificación de los 6 dominios.

### Decisión 2: Pipeline Criptográfico de Manifiesto y Claims
- **Enfoque Elegido**: Escribir los archivos Markdown primero, luego ejecutar un script determinista en Python/Node para calcular los hashes SHA-256 exactos y generar los bloques en `.page-manifest.json` y `.claims/`.
- **Razón**: Los validadores Zod fallan ante cualquier discrepancia de un solo carácter en `pageVersion`. La generación algorítmica garantiza concordancia del 100%.

---

## 4. Technical Architecture & Data Strategy

### Diagrama del Pipeline de Auditoría y Verificación

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                      PIPELINE DE SINCRONIZACIÓN OPENWIKI                    │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  [1. INSPECCIÓN EN DISCO] ──► Módulos: :app, :emulator, :view, :shared      │
│            │                                                                │
│            ▼                                                                │
│  [2. GENERACIÓN DE DOMINIOS]                                                │
│       ├── operations/    (build-and-signing.md, forensics-and-logging.md)   │
│       ├── integrations/  (plugin-protocol-and-intents.md, permissions.md)   │
│       └── testing/       (unit-testing-and-test-suites.md)                  │
│            │                                                                │
│            ▼                                                                │
│  [3. NAVEGACIÓN Y PORTALES]                                                 │
│       ├── quickstart.md  (enrutamiento a los 6 dominios)                    │
│       ├── index.md       (catálogo unificado)                               │
│       └── INSTRUCTIONS.md (directivas canónicas)                            │
│            │                                                                │
│            ▼                                                                │
│  [4. HASHING SHA-256 DETERMINISTA]                                          │
│       ├── .page-manifest.json (pageVersion, sourceFingerprint, runId v4)    │
│       ├── .claims/<dominio>/*.json (declaraciones técnicas + repo:// URIs)  │
│       └── .last-update.json (status: "success", timestamp UTC)              │
│            │                                                                │
│            ▼                                                                │
│  [5. CIERRE DE INTEGRIDAD / QUALITY GATE]                                   │
│       ├── Verificación de sintaxis de manifiestos y claims                  │
│       └── Sincronización de grafo con codegraph sync --no-color             │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

### Especificación de Metadatos y Esquema Zod

1. **`.page-manifest.json`**:
   - `pageVersion`: `sha256:<64_hex_chars>` exacto del archivo `.md`.
   - `completedBy`: `manual-audit/gemini-3.8-flash`.
   - `completedRunId`: UUID v4 generado determinísticamente.
   - `gitHead`: Preservar el valor de cabecera (`"HEAD"` o commit hash actual).
   - `sourceFingerprint`: `sha256:<64_hex_chars>`.

2. **`.claims/<dominio>/<archivo>.json`**:
   - `schemaVersion`: 1.
   - `pageVersion`: Idéntico al asignado en el manifiesto.
   - `claims[].id`: `claim_<snake_case_identificador>`.
   - `claims[].statement`: Afirmación técnica empíricamente comprobable en el código.
   - `claims[].evidence[].resource`: `repo://<ruta_relativa>#L<inicio>-L<fin>`.
   - `verification.by`: `manual-audit/gemini-3.8-flash`.
   - `verification.at`: Timestamp ISO-8601 UTC actual.

---

## Plan de Ejecución Paso a Paso

1. **Paso 1: Creación del Dominio `openwiki/operations/`**:
   - `openwiki/operations/index.md`: Portal del dominio.
   - `openwiki/operations/build-and-signing.md`: Flujos de compilación Gradle (`assembleDebug`, `testDebugUnitTest`), configuración de `signingConfigs` (debug / untrusted), arquitecturas ABI (`x86`, `arm64-v8a`) y empaquetado de bootstrap.
   - `openwiki/operations/forensics-and-logging.md`: Captura forense de crashes (`CrashHandler`), volcado en `crash_log.md` y visualización mediante Markwon en `ReportActivity`.

2. **Paso 2: Creación del Dominio `openwiki/integrations/`**:
   - `openwiki/integrations/index.md`: Portal del dominio.
   - `openwiki/integrations/plugin-protocol-and-intents.md`: Protocolo de intents con Termux:API, Termux:Tasker y Termux:Widget; despacho de scripts mediante `RunCommandService` y socket IPC local.
   - `openwiki/integrations/permissions-and-security.md`: Permiso `com.termux.permission.RUN_COMMAND`, validación de tokens/autorización, aislamiento de sandbox y protección de dotfiles en providers.

3. **Paso 3: Creación del Dominio `openwiki/testing/`**:
   - `openwiki/testing/index.md`: Portal del dominio.
   - `openwiki/testing/unit-testing-and-test-suites.md`: Suites de pruebas en `terminal-emulator/src/test/` (ApcTest, ByteQueueTest, DecSetTest, WcWidthTest), pruebas Robolectric en `:app`, restricciones de ejecución en sandbox sin emulador local.

4. **Paso 4: Actualización de Portales de Navegación**:
   - Actualizar `openwiki/quickstart.md` para mapear los 6 dominios completos con tareas tipo.
   - Actualizar `openwiki/index.md` con los 6 dominios.
   - Actualizar `openwiki/INSTRUCTIONS.md` fijando las directivas canónicas.

5. **Paso 5: Generación de Claims y Manifiesto Zod**:
   - Crear los archivos de claims en `.claims/operations/`, `.claims/integrations/` y `.claims/testing/`.
   - Calcular los hashes SHA-256 de todos los archivos `.md` nuevos y modificados.
   - Actualizar `openwiki/.page-manifest.json` incorporando las nuevas páginas con sus `pageVersion` y fingerprints.
   - Actualizar `openwiki/.last-update.json` estableciendo `"status": "success"` y el timestamp actual.

6. **Paso 6: Sincronización de CodeGraph e Integridad**:
   - Ejecutar `codegraph sync --no-color` para actualizar el índice relacional.
   - Emitir el informe de auditoría y certificación técnica final.
