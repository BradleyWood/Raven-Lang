package org.toylang.core.wrappers;

import org.toylang.core.Hidden;

import java.util.*;

public class ToyList extends ToyObject implements List {

    @Hidden
    public static ToyType TYPE = new ToyType(ToyList.class);
    @Hidden
    private ArrayList<ToyObject> list;

    public ToyList() {
        list = new ArrayList<>();
    }

    @Hidden
    public ToyList(ToyObject[] objects) {
        this();
        list.addAll(Arrays.asList(objects));
    }

    public List<ToyObject> getList() {
        return list;
    }

    @Hidden
    public ToyList(List<Object> obj) {
        this();
        addAll(obj);
    }

    @Override
    public ToyObject getType() {
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
    public ToyObject add(ToyObject obj) {
        list.add(obj);
        return this;
    }

    @Override
    public ToyObject set(ToyObject index, ToyObject obj) {
        if (index instanceof ToyInt) {
            return list.set(index.toInt(), obj);
        }
        return super.set(index, obj);
    }

    @Override
    public ToyObject get(ToyObject obj) {
        if (obj instanceof ToyInt) {
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
        if (o instanceof ToyObject) {
            return list.add((ToyObject) o);
        } else {
            return list.add(ToyObject.toToyLang(o));
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
        if (element instanceof ToyObject) {
            return list.set(index, (ToyObject) element);
        } else {
            return list.set(index, new ToyObject(element));
        }
    }

    @Override
    public void add(int index, Object element) {
        if (element instanceof ToyObject) {
            list.add(index, (ToyObject) element);
        } else {
            list.add(index, new ToyObject(element));
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

        ToyList toyList = (ToyList) o;

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
