package org.raven.core.wrappers;

import org.raven.core.Hidden;
import org.raven.core.Intrinsics;

import java.lang.reflect.Array;
import java.util.*;

public class TList extends TObject implements List {

    @Hidden
    public static TType TYPE = new TType(TList.class);

    @Hidden
    protected ArrayList<TObject> list;

    private boolean allSameType = true;
    private TType componentType = null;

    public TList() {
        list = new ArrayList<>();
    }

    @Hidden
    public TList(final TObject... objects) {
        this();
        list.addAll(Arrays.asList(objects));
    }

    public List<TObject> getList() {
        return list;
    }

    @Hidden
    public TList(final List<Object> obj) {
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
    public boolean contains(final Object o) {
        return list.contains(o);
    }

    @Override
    public TObject EQ(final TObject obj) {
        if (obj instanceof TList) {
            return list.equals(((TList) obj).list) ? TBoolean.TRUE : TBoolean.FALSE;
        }
        return TBoolean.FALSE;
    }

    @Override
    public TObject NE(final TObject obj) {
        return EQ(obj).not();
    }

    @Override
    public Iterator<TObject> iterator() {
        return list.iterator();
    }

    private void checkType(final TType c) {
        if (componentType == null) {
            componentType = c;
        } else if (allSameType && !componentType.equals(c)) {
            allSameType = false;
        }
    }

    @Override
    public TObject add(final TObject obj) {
        list.add(obj);
        checkType((TType) obj.getType());
        return this;
    }

    @Override
    public TObject set(final TObject index, final TObject obj) {
        checkType((TType) obj.getType());
        if (index instanceof TInt) {
            return list.set(index.toInt(), obj);
        }
        return super.set(index, obj);
    }

    @Override
    public TObject get(final TObject obj) {
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
    public boolean add(final Object o) {
        if (o instanceof TObject) {
            add((TObject) o);
        } else {
            add(Intrinsics.wrap(o));
        }
        return true;
    }

    @Override
    public boolean remove(final Object o) {
        return list.remove(o);
    }

    @Override
    public boolean addAll(final Collection c) {
        final int size = list.size();
        for (Object o : c) {
            add(o);
        }
        return size != list.size();
    }

    @Override
    public boolean addAll(final int index, final Collection c) {
        final int size = list.size();
        final Iterator it = c.iterator();
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
    public Object get(final int index) {
        return list.get(index);
    }

    @Override
    public Object set(final int index, final Object element) {
        if (element instanceof TObject) {
            return list.set(index, (TObject) element);
        } else {
            return list.set(index, Intrinsics.wrap(element));
        }
    }

    @Override
    public void add(final int index, final Object element) {
        TObject tobj;
        if (element instanceof TObject) {
            tobj = (TObject) element;
        } else {
            tobj = Intrinsics.wrap(element);
        }
        list.add(index, tobj);
        checkType((TType) tobj.getType());
    }

    @Override
    public Object remove(final int index) {
        return list.remove(index);
    }

    @Override
    public int indexOf(final Object o) {
        return list.indexOf(o);
    }

    @Override
    public int lastIndexOf(final Object o) {
        return list.lastIndexOf(o);
    }

    @Hidden
    @Override
    public ListIterator<TObject> listIterator() {
        return list.listIterator();
    }

    @Hidden
    @Override
    public ListIterator<TObject> listIterator(final int index) {
        return list.listIterator(index);
    }

    @Override
    public List<TObject> subList(final int fromIndex, final int toIndex) {
        return list.subList(fromIndex, toIndex);
    }

    @Override
    public boolean retainAll(final Collection c) {
        return list.retainAll(c);
    }

    @Override
    public boolean removeAll(final Collection c) {
        return list.removeAll(c);
    }

    @Override
    public boolean containsAll(final Collection c) {
        return list.containsAll(c);
    }

    @Hidden
    @Override
    public Object[] toArray(final Object[] a) {
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

    @Override
    public Object coerce(final Class clazz) {
        if (clazz.isArray()) {
            Object array;
            if (allSameType && clazz.isArray()) {
                array = Array.newInstance(clazz.getComponentType(), size());
            } else if (clazz.equals(Object[].class)) {
                array = new Object[size()];
            } else {
                return super.coerce(clazz);
            }
            for (int i = 0; i < list.size(); i++) {
                Array.set(array, i, list.get(i).coerce(clazz.getComponentType()));
            }
            return array;
        } else if (List.class.isAssignableFrom(clazz)) {
            return toObject();
        }
        return super.coerce(clazz);
    }

    @Override
    public int coerceRating(final Class clazz) {
        if (clazz.isArray() || List.class.isAssignableFrom(clazz)) {
            return COERCE_IDEAL;
        }
        return super.coerceRating(clazz);
    }

    @Hidden
    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        return o instanceof TList && Objects.equals(list, ((TList) o).list);
    }

    @Hidden
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (list != null ? list.hashCode() : 0);
        return result;
    }
}
