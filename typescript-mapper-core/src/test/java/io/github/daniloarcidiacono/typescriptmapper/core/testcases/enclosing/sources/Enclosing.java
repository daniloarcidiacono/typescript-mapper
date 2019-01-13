package io.github.daniloarcidiacono.typescriptmapper.core.testcases.enclosing.sources;

import io.github.daniloarcidiacono.typescriptmapper.core.annotation.TypescriptDTO;

@TypescriptDTO
public class Enclosing {
    @TypescriptDTO
    class EnclosingFirst {
        @TypescriptDTO
        class EnclosingFirstNested {
        }

        @TypescriptDTO
        class Other {
        }
    }

    @TypescriptDTO
    class EnclosingSecond {
    }
}
