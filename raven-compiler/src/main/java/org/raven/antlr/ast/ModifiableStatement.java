package org.raven.antlr.ast;

import org.raven.antlr.Modifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModifiableStatement extends Statement {

    private List<Modifier> modifiers;

    ModifiableStatement(final Modifier... modifiers) {
        this(new ArrayList<>(Arrays.asList(modifiers)));
    }

    ModifiableStatement(final List<Modifier> modifiers) {
        this.modifiers = modifiers;
    }

    public final List<Modifier> getModifiers() {
        return modifiers;
    }

    public boolean isPublic() {
        return getModifiers().contains(Modifier.PUBLIC);
    }

    public boolean isPrivate() {
        return getModifiers().contains(Modifier.PRIVATE);
    }

    public boolean isOpen() {
        return getModifiers().contains(Modifier.OPEN);
    }

    public boolean isFinal() {
        return !isOpen();
    }

    public void addModifier(final Modifier modifier) {
        if (hasModifier(modifier))
            return;
        getModifiers().add(modifier);
    }

    public void removeModifier(final Modifier modifier) {
        getModifiers().remove(modifier);
    }

    public void setPublic() {
        getModifiers().remove(Modifier.PRIVATE);
        if (!isPublic()) {
            getModifiers().add(Modifier.PUBLIC);
        }
    }

    public void setPrivate() {
        getModifiers().remove(Modifier.PUBLIC);
        if (!isPrivate()) {
            getModifiers().add(Modifier.PRIVATE);
        }
    }

    public boolean hasModifier(final Modifier modifier) {
        return getModifiers().contains(modifier);
    }

    public int modifiers() {
        int mod = 0;
        for (Modifier modifier : getModifiers()) {
            mod += modifier.getModifier();
        }
        return mod;
    }
}
