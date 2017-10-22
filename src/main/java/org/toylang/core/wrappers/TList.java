package org.toylang.core.wrappers;

import org.toylang.core.Hidden;

import java.util.*;

public class TList extends TObject implements List {

    @Hidden
    public static TType TYPE = new TType(TList.class);
    @Hidden
    private ArrayList<TObject> list;

    public TList() {
        list = new ArrayList<>();
    }

    @Hidden
    public TList(TObject[] objects) {
        this();
        list.addAll(Arrays.asList(objects));
    }

    public List<TObject> getList() {
        return list;
    }

    @Hidden
    public TList(List<Object> obj) {
        this();
        addAll(obj);
    }

    @Override
    public TObject getType() {
        return TYPE;
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return list.contains(o);
    }

    @Override
    public Iterator iterator() {
        return list.iterator();
    }

    @Override
    public TObject add(TObject obj) {
        list.add(obj);
        return this;
    }

    @Override
    public TObject set(TObject index, TObject obj) {
        if (index instanceof TInt) {
            return list.set(index.toInt(), obj);
        }
        return super.set(index, obj);
    }

    @Override
    public TObject get(TObject obj) {
        if (obj instanceof TInt) {
            return list.get(obj.toInt());
        }
        return super.get(obj);
    }

    @Hidden
    @Override
    public Object[] toArray() {
        Object[] obja = new Object[list.size()];
        for (int i = 0; i < list.size(); i++) {
            obja[i] = list.get(i).toObject();
        }
        return obja;
    }

    @Override
    public boolean add(Object o) {
        if (o instanceof TObject) {
            return list.add((TObject) o);
        } else {
            return list.add(TObject.toToyLang(o));
        }
    }

    @Override
    public boolean remove(Object o) {
        return list.remove(o);
    }

    @Override
    public boolean addAll(Collection c) {
        int size = list.size();
        for (Object o : c) {
            add(o);
        }
        return size != list.size();
    }

    @Override
    public boolean addAll(int index, Collection c) {
        int size = list.size();
        Iterator it = c.iterator();
        for (int i = index; i < c.size(); i++) {
            add(i, it.next());
        }
        return size != list.size();
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public Object get(int index) {
        return list.get(index);
    }

    @Override
    public Object set(int index, Object element) {
        if (element instanceof TObject) {
            return list.set(index, (TObject) element);
        } else {
            return list.set(index, new TObject(element));
        }
    }

    @Override
    public void add(int index, Object element) {
        if (element instanceof TObject) {
            list.add(index, (TObject) element);
        } else {
            list.add(index, new TObject(element));
        }
    }

    @Override
    public Object remove(int index) {
        return list.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    @Hidden
    @Override
    public ListIterator listIterator() {
        return list.listIterator();
    }

    @Hidden
    @Override
    public ListIterator listIterator(int index) {
        return list.listIterator(index);
    }

    @Override
    public List subList(int fromIndex, int toIndex) {
        return list.subList(fromIndex, toIndex);
    }

    @Override
    public boolean retainAll(Collection c) {
        return list.retainAll(c);
    }

    @Override
    public boolean removeAll(Collection c) {
        return list.removeAll(c);
    }

    @Override
    public boolean containsAll(Collection c) {
        return list.containsAll(c);
    }

    @Hidden
    @Override
    public Object[] toArray(Object[] a) {
        return toArray();
    }

    @Hidden
    @Override
    public Object toObject() {
        return list;
    }

    @Override
    public String toString() {
        return list.toString();
    }

    @Hidden
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        TList toyList = (TList) o;

        return list != null ? list.equals(toyList.list) : toyList.list == null;
    }

    @Hidden
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (list != null ? list.hashCode() : 0);
        return result;
    }
}
