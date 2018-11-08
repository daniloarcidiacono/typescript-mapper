package io.github.daniloarcidiacono.typescript.mapper.testcases.enclosing.sources;

import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptDTO;

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
