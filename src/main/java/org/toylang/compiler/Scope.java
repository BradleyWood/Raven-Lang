package org.toylang.compiler;

import java.util.ArrayList;
import java.util.LinkedList;

public class Scope {

    private final LinkedList<ArrayList<String>> scope = new LinkedList<>();

    public void beginScope() {
        scope.addLast(new ArrayList<>());
    }

    public void endScope() {
        if (scope.isEmpty())
            throw new RuntimeException("underflow");
        scope.removeLast();
    }

    public void putVar(final String name) {
        if (findVar(name) != -1) {
            Errors.put("Redeclaration of variable: " + name);
        } else {
            scope.getLast().add(name);
        }
    }

    public int findVar(final String name) {
        int idx = 0;

        for (ArrayList<String> list : scope) {
            int i = list.indexOf(name);
            if (i != -1) {
                return idx + i;
            }
            idx += list.size();
        }
        return -1;
    }

    public int count() {
        return scope.size();
    }

    public void clear() {
        scope.clear();
    }
}
