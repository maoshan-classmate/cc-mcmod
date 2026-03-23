# Multi-Mod Project Migration Design

## Overview

Convert the single-mod Minecraft Forge project into a multi-module Gradle project to manage multiple mods in one repository.

## Current State

- Single Gradle project at `E:\mc_mod`
- Minecraft 1.20.1 + Forge 47.3.0
- One mod: `shanmod`

## Target State

```
mc-mods/                                    # Root project (parent)
├── build.gradle                            # Shared plugin configuration
├── settings.gradle                         # Multi-module configuration
├── gradle.properties                       # Shared MC/Forge version config
├── gradlew / gradlew.bat
└── submodules/
    └── shanmod/                            # shanmod submodule
        ├── build.gradle
        └── src/main/java/...              # All source code
```

## Migration Steps

### 1. Create directory structure
```
mkdir -p submodules/shanmod
```

### 2. Move current project to submodule
- Move `src/` to `submodules/shanmod/src/`
- Move `gradle.properties` content adjustment (shared at root)
- Create `submodules/shanmod/build.gradle`

### 3. Root-level configuration

**`build.gradle`** - Shared plugins:
- `net.minecraftforge.gradle` plugin
- `eclipse`, `idea` plugins
- Version variables from `gradle.properties`

**`settings.gradle`** - Multi-module format:
```groovy
pluginManagement { ... }
include 'submodules:shanmod'
```

**`gradle.properties`** - Shared config:
- `minecraft_version=1.20.1`
- `forge_version=47.3.0`
- `org.gradle.java.home=...`

### 4. Submodule configuration

**`submodules/shanmod/build.gradle`**:
```groovy
plugins {
    id 'java-library'
    id 'eclipse'
    id 'idea'
    id 'maven-publish'
    id 'net.minecraftforge.gradle'
}

java.toolchain.languageVersion = JavaLanguageVersion.of(17)

// Module-specific mod properties
def modSpecifics = [
    mod_id: 'shanmod',
    mod_name: 'Shan Mod',
    // ...
]

// Inherit from root project
minecraft {
    mappings channel: mapping_channel, version: mapping_version
    // ...
}

dependencies {
    minecraft "net.minecraftforge:forge:${minecraft_version}-${forge_version}"
}
```

## Build Commands

| Command | Effect |
|---------|--------|
| `./gradlew :submodules:shanmod:build` | Build only shanmod |
| `./gradlew build` | Build all submodules |
| `./gradlew :submodules:shanmod:clean` | Clean specific module |
| `./gradlew clean` | Clean all |

## Output Locations

- `submodules/shanmod/build/libs/shanmod-1.0.0.jar`
- `submodules/shanmod/build/reobfJar/output.jar`

## Key Points

1. **Shared configuration**: MC version, Forge version, Java toolchain, and plugins defined once at root
2. **Independent builds**: Each submodule can be built, tested, and run independently
3. **Future extensibility**: Easy to add new submodules (e.g., `submodules/anothermod/`)
4. **No shared code between mods**: Current requirement is management only, not code sharing
