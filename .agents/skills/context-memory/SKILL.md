---
name: context-memory
description: >-
  Gestiona la memoria técnica a largo plazo en CONTEXT.md y su ciclo de auto-mejora.
  Utilizar cada vez que se diagnostique, resuelva o supere un error técnico, fallo de compilación,
  problema de CI/CD o arquitectura en Termux. Permite que la skill se auto-actualice cuando
  se detecten mejoras operativas.
---

# Skill: Context Memory & Continuous Self-Improvement

Esta skill proporciona las directivas para registrar aprendizajes y garantizar que el sistema de memoria técnica evolucione activamente.

## 1. Principio Fundamental
Todo error no trivial resuelto en el repositorio es un activo de conocimiento. Debe quedar documentado en `CONTEXT.md` en la raíz del proyecto para evitar que cualquier agente o humano tropiece con la misma piedra.

## 2. Flujo de Trabajo Operativo

### Paso 1: Consulta Previa (Retrieval)
Antes de emprender cambios arquitectónicos o cuando ocurra un fallo de compilación o CI:
1. Leer `CONTEXT.md`.
2. Verificar si el error actual coincide con algún incidente previo registrado.
3. Si existe coincidencia, aplicar la **Solución / Fix** y verificar la **Regla Preventiva**.

### Paso 2: Registro de Nuevos Incidentes
Cuando se resuelva un nuevo error técnico:
1. Abrir `CONTEXT.md`.
2. Asignar un ID incremental (`INC-XXX`).
3. Registrar los campos obligatorios:
   - **Fecha**
   - **Entorno / Componente**
   - **Síntoma / Mensaje de Error Exacto**
   - **Causa Raíz**
   - **Solución / Código de Reemplazo**
   - **Regla Preventiva**

### Paso 3: Protocolo de Auto-Mejora de la Skill (Self-Improvement Loop)
La skill no es estática: **debe mejorarse a sí misma**.
- Si durante la resolución de un problema detectas que:
  - Faltan categorías en `CONTEXT.md` (ej. NDK, C bindings, emulador terminal, SELinux).
  - Un nuevo patrón de validación automática previene fallos antes del commit.
  - La redacción de las instrucciones puede ser más clara o eficiente.
- **Acción del Agente**:
  1. Modificar este mismo archivo (`.agents/skills/context-memory/SKILL.md`) incorporando la mejora.
  2. Documentar la evolución en el registro de cambios de la skill.
  3. Notificar al usuario la auto-mejora efectuada.
