package org.raven.core.wrappers;

import org.raven.core.Intrinsics;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class TListFacade<T> extends TList {

    private final Class<T> componentType;

    public TListFacade(final List list, final Class<T> componentType) {
        super(list);
        this.componentType = componentType;
    }

    @Override
    public T get(final int index) {
        return list.get(index).coerce(componentType);
    }

    @Override
    public T remove(final int index) {
        return list.remove(index).coerce(componentType);
    }

    @Override
    public T set(final int index, final Object element) {
        if (element instanceof TObject) {
            return list.set(index, (TObject) element).coerce(componentType);
        } else {
            return list.set(index, Intrinsics.wrap(element)).coerce(componentType);
        }
    }

    @Override
    public boolean containsAll(Collection c) {
        return super.containsAll(c);
    }

    @Override
    public List subList(final int fromIndex, final int toIndex) {
        return new TListFacade(super.subList(fromIndex, toIndex), componentType);
    }

    @Override
    public Iterator iterator() {
        return new IteratorFacade(super.iterator());
    }

    @Override
    public ListIterator listIterator(final int index) {
        return new ListIteratorFacade(super.listIterator(index));
    }

    @Override
    public ListIterator listIterator() {
        return new ListIteratorFacade(super.listIterator());
    }

    private class IteratorFacade implements Iterator<T> {

        private final Iterator<TObject> iterator;

        private IteratorFacade(final Iterator<TObject> iterator) {
            this.iterator = iterator;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public T next() {
            return iterator.next().coerce(componentType);
        }
    }

    private class ListIteratorFacade implements ListIterator<T> {

        private final ListIterator<TObject> iterator;

        private ListIteratorFacade(final ListIterator<TObject> iterator) {
            this.iterator = iterator;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public T next() {
            return iterator.next().coerce(componentType);
        }

        @Override
        public boolean hasPrevious() {
            return iterator.hasPrevious();
        }

        @Override
        public T previous() {
            return iterator.previous().coerce(componentType);
        }

        @Override
        public int nextIndex() {
            return iterator.nextIndex();
        }

        @Override
        public int previousIndex() {
            return iterator.previousIndex();
        }

        @Override
        public void remove() {
            iterator.remove();
        }

        @Override
        public void set(final T o) {
            iterator.set(Intrinsics.wrap(o));
        }

        @Override
        public void add(final T o) {
            iterator.add(Intrinsics.wrap(o));
        }
    }
}
