---
name: openwiki-search
description: >-
  Busqueda quirurgica de alta velocidad en la documentacion de OpenWiki usando ripgrep nativo.
  Usar siempre que el usuario pregunte o se necesite consultar arquitectura, diseno, terminal o workflows en openwiki/.
---

# OpenWiki Fast Search Skill

Esta skill optimiza la consulta de la documentacion de `openwiki/` para ahorrar tokens y responder en milisegundos sin cargar archivos completos al contexto.

## Principio
No abras archivos `.md` completos si solo necesitas un dato o seccion de la arquitectura de Termux.

## Modos de Consulta Quirurgica

1. **Buscar un concepto con contexto de lineas:**
   `rg -i -C 3 "termino_o_clase" openwiki/`

2. **Buscar exclusivamente en encabezados (# y ##):**
   `rg -i "^#+ .*termino" openwiki/`

3. **Localizar el archivo relevante de la wiki:**
   `rg -i -l "termino" openwiki/`
