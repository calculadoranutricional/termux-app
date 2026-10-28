<!-- CODEGRAPH_START -->
## CodeGraph

In repositories indexed by CodeGraph (a `.codegraph/` directory exists at the repo root), reach for it BEFORE grep/find or reading files when you need to understand or locate code:

- **Primary Command**: `codegraph explore "<symbol or question>"` (shows source code + call paths in one shot).
- **Targeted Inspection**: Use `codegraph node <symbol>` for caller/callee hierarchy, or `codegraph query "<search>"` for fast symbol lookup.
- **Index Sync**: Run `codegraph sync` after editing code to keep the index fresh. Refer to `codegraph --help` for full options.

If there is no `.codegraph/` directory, skip CodeGraph entirely — indexing is the user's decision.
<!-- CODEGRAPH_END -->

<!-- CONTEXT_MEMORY_START -->
## Memoria de Contexto y Aprendizaje (CONTEXT.md)

En la raíz del repositorio existe el archivo `CONTEXT.md`, el cual funciona como la memoria técnica a largo plazo de problemas, errores superados, particularidades de la arquitectura y soluciones ya validadas.

- **Antes de diagnosticar o modificar**: Consultar obligatoriamente `CONTEXT.md` para verificar si un problema similar ya ocurrió y cuál fue su resolución comprobada.
- **Al resolver un error o bug no trivial**: Registrar de inmediato la entrada en `CONTEXT.md` siguiendo la estructura:
  - **Fecha / Componente**
  - **Error o Síntoma** (mensajes de error exactos de Gradle/Javac/CI/Logcat)
  - **Causa Raíz** (por qué falló)
  - **Solución / Código de reemplazo** (cómo se corrigió)
  - **Regla Preventiva** (qué hacer para no repetir el error)
- **Auto-mejora continua**: Los agentes tienen la instrucción explícita de mejorar la skill `.agents/skills/context-memory/SKILL.md` si descubren formas más eficaces de organizar, validar o documentar la memoria del proyecto.
<!-- CONTEXT_MEMORY_END -->

<!-- OPENWIKI:START -->

## OpenWiki

This repository has a generated `openwiki/` evidence index. It is optional just-in-time context, not required startup reading.

- Do not enumerate, preload, or search wikis at task start. Use retrieval when the user asks for it, when unfamiliar architecture or dependency behavior materially affects the task, or when source inspection leaves an important uncertainty. Stop once the question is grounded.
- When those conditions apply and OpenWiki retrieval tools are available, use `openwiki_search` for just-in-time context and `openwiki_read` for the relevant complete sections. If search returns `workspace_required`, ask which listed workspace to use and retry with its ID.
- Use `openwiki_list_workspaces` or `openwiki_list_wikis` when workspace membership itself needs to be discovered.
- If the retrieval tools are unavailable, read `openwiki/quickstart.md` and follow its links to the relevant pages.
- Treat source code and tests as authoritative. A brief's unknowns and review items are verification gaps, not automatic requirements.
- Prefer the narrowest quiet validation that proves the changed behavior. Preserve complete failure output.

The scheduled OpenWiki GitHub Actions workflow refreshes the repository wiki. Do not hand-edit generated OpenWiki pages unless explicitly asked; prefer updating source code/docs and letting OpenWiki regenerate.

<!-- OPENWIKI:END -->