package testclasses;

import java.util.Arrays;
import java.util.List;

public class Container {

    public List list;

    public Container(List list) {
        this.list = list;
    }

    public Container(Object[] oa) {
        this(Arrays.asList(oa));
    }

    public Object get(int idx) {
        return list.get(idx);
    }
}
