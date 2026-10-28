# Memoria de Contexto y Soluciones de Errores (CONTEXT.md)

Este documento funciona como la **memoria técnica a largo plazo** del proyecto. Registra problemas técnicos no triviales, fallos de compilación, comportamientos inesperados y sus soluciones comprobadas para evitar cometer los mismos errores.

---

## Índice de Entornos y Componentes
1. [Build & CI/CD (GitHub Actions / Gradle)](#1-build--cicd-github-actions--gradle)
2. [Componentes Android & UI de Termux](#2-componentes-android--ui-de-termux)
3. [Ejecución de Comandos & Shell](#3-ejecución-de-comandos--shell)

---

## 1. Build & CI/CD (GitHub Actions / Gradle)

### INC-001: Error de compilación Javac al importar `AppCompatActivityUtils`
- **Fecha**: 2026-10-27
- **Entorno**: GitHub Actions CI (`compileDebugJavaWithJavac`, Workflows `Build`, `Unit tests`, `Attach Debug APKs To Release`)
- **Síntoma / Mensaje de error**:
  ```text
  /home/runner/work/termux-app/termux-app/app/src/main/java/com/termux/app/store/TermuxStoreActivity.java:18: error: cannot find symbol
    import com.termux.shared.activities.AppCompatActivityUtils;
                                       ^
      symbol:   class AppCompatActivityUtils
      location: package com.termux.shared.activities
  ```
- **Causa Raíz**:
  En el módulo `termux-shared`, la clase `AppCompatActivityUtils` no pertenece al paquete `com.termux.shared.activities`, sino a `com.termux.shared.activity.media`.
- **Solución / Fix**:
  Corregir la directiva de importación:
  ```java
  // Incorrecto:
  import com.termux.shared.activities.AppCompatActivityUtils;

  // Correcto:
  import com.termux.shared.activity.media.AppCompatActivityUtils;
  ```
- **Regla Preventiva**:
  Antes de referenciar utilidades compartidas (`*Utils`) en clases nuevas de `app`, verificar la ruta exacta del paquete en `termux-shared` mediante búsqueda directa o CodeGraph (`codegraph explore "<Clase>"`).

---

## 2. Componentes Android & UI de Termux

### INC-002: Declaración obligatoria de nuevas Activities en AndroidManifest
- **Fecha**: 2026-10-27
- **Entorno**: Runtime Android
- **Regla Preventiva**:
  Toda nueva Activity (ej. `TermuxStoreActivity`) debe registrarse en `app/src/main/AndroidManifest.xml` con su respectivo tema (`@style/Theme.TermuxApp.DayNight.NoActionBar`) y `android:exported="false"` a menos que requiera acceso explícito externo.

---

## 3. Ejecución de Comandos & Shell

### INC-003: Disparo de comandos interactivos desde la UI hacia la terminal viva
- **Fecha**: 2026-10-27
- **Patrón Comprobado**:
  Para disparar un comando desde una pantalla secundaria (como una tienda o gestor) y que el usuario vea los logs en tiempo real en la terminal principal de Termux:
  1. Escribir un script ejecutable temporal en `context.getCacheDir()`.
  2. Enviar el intent con la acción `TERMUX_SERVICE.ACTION_SERVICE_EXECUTE` hacia `TermuxService.class`.
  3. Reordenar `TermuxActivity` al frente con `FLAG_ACTIVITY_REORDER_TO_FRONT`.
