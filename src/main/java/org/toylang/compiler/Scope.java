package org.toylang.compiler;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;

public class Scope {

    private final Stack<ArrayList<String>> scope = new Stack<>();

    public void beginScope() {
        scope.push(new ArrayList<>());
    }

    public void endScope() {
        scope.pop();
    }

    public void putVar(final String name) {
        if (findVar(name) != -1) {
            Errors.put("Redeclaration of variable: " + name);
        } else {
            scope.peek().add(name);
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
