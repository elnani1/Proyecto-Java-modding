# Entorno Fabric para Minecraft 26.2 (preparado)

## Versiones (oficial fabric-example-mod 26.2)
- Minecraft: **26.2**
- Fabric Loader: **0.20.1**
- Fabric Loom: **1.19-SNAPSHOT**
- Fabric API: **0.161.0+26.2**
- Gradle: **9.8.2** (wrapper incluido)
- Java requerido: **JDK 25**

## Importante: ¿mappings?
Minecraft 26.2 es **desofuscado** y Fabric usa **Mojang Mappings oficiales** directamente (ya no se usa Yarn ni se declara `mappings` en `build.gradle`). Este proyecto ya está configurado así (ver `build.gradle`: solo `minecraft` + `implementation` de loader y fabric-api, sin línea de mappings).

## Requisitos instalados en tu sistema (Void Linux)
- JDK 25 (`java -version` → 25.0.4.1) instalado vía `xbps`
- git, unzip, zip, wget instalados vía `xbps`
- IntelliJ IDEA Community 2025.2.6.4 (build 252.28539.33) en `~/.local/opt/idea-IC-252.28539.33`

## Cómo empezar
1. Abre IntelliJ: `~/.local/bin/idea-community` (o desde el menú de aplicaciones).
2. `File > Open` → selecciona esta carpeta (`~/Projects/fabric-mod-26.2`).
3. Acepta **Import Gradle Project** / Trust Project si lo pide. IntelliJ usará el JDK 25 del sistema y el Gradle wrapper (descargará Gradle 9.8.2 la primera vez, requiere internet).
4. Espera a que termine la sincronización de Gradle (puede tardar porque descarga Minecraft y dependencias).
5. Ejecuta el cliente de desarrollo: `./gradlew runClient` o desde la pestaña Gradle en IntelliJ.

## Comandos útiles
- `./gradlew runClient` — ejecutar el juego con el mod
- `./gradlew runServer` — ejecutar servidor de desarrollo
- `./gradlew build` — compilar y generar el .jar en `build/libs/`
- `./gradlew genSources` — generar fuentes de Minecraft para navegar

## Notas
- La plantilla base usa `modid` como id y paquete `com.example`; cámbialos en `gradle.properties`, `settings.gradle`, `fabric.mod.json`, nombres de paquete/clases y recursos cuando crees tu mod real.
- Se dejó el proyecto tal cual la plantilla oficial `fabric-example-mod` rama 26.2 (sin inicializar git aquí, para no imponer nada).
