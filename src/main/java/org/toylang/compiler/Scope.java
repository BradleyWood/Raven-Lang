package org.toylang.compiler;

import java.util.ArrayList;
import java.util.LinkedList;

public class Scope {

    private final LinkedList<ArrayList<String>> scope = new LinkedList<>();

    public void beginScope() {
        scope.addFirst(new ArrayList<>());
    }

    public void endScope() {
        scope.removeFirst();
    }

    public void putVar(final String name) {
        scope.getFirst().add(name);
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

}
