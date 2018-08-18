package io.github.daniloarcidiacono.typescript.mapper.misc;

import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptDTO;

import java.util.List;
import java.util.Map;

@TypescriptDTO(identifier = "Person")
public class Person {
    public String name;
    public int age;
    public boolean hasChildren;
    public List<String> tags;
    public Map<String, String> emails;
}