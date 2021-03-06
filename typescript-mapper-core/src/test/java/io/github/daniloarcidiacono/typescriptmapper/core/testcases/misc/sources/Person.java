package io.github.daniloarcidiacono.typescriptmapper.core.testcases.misc.sources;

import io.github.daniloarcidiacono.typescriptmapper.core.annotation.TypescriptComments;
import io.github.daniloarcidiacono.typescriptmapper.core.annotation.TypescriptDTO;

import java.util.List;
import java.util.Map;

@TypescriptDTO(identifier = "Person")
public class Person {
    @TypescriptComments("The name of the person.")
    public String name;
    public int age;
    public boolean hasChildren;
    public List<String> tags;

    @TypescriptComments({
        "The emails of the person.",
        "Key is provider, value is email."
    })
    public Map<String, String> emails;
}