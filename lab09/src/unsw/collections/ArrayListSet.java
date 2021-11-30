/**
 *
 */
package unsw.collections;

import java.util.ArrayList;
import java.util.Iterator;

import unsw.fruit.Fruit;

/**
 * An implementation of Set that uses an ArrayList to store the elements.
 *
 * @invariant All e in elements occur only once
 *
 * @author Robert Clifton-Everest
 *
 */
public class ArrayListSet<E> implements Set<E> {

    private ArrayList<E> elements;

    public ArrayListSet() {
        elements = new ArrayList<>();
    }

    @Override
    public void add(E e) {
        if (!contains(e)){
            elements.add(e);
        }
    }

    @Override
    public void remove(E e) {
        elements.remove(e);
    }

    @Override
    public boolean contains(Object e) {
        return elements.contains(e);
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public boolean subsetOf(Set<?> other) {
        int flag = 0;
        for (E e : elements) {
            if (other.contains(e)){
                flag++;
            }

        }

        if (flag == size()){
            return true;
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return elements.iterator();
    }

    @Override
    public Set<E> union(Set<? extends E> other) {
        Set<E> tmp = new ArrayListSet<>();
        for (E a : other) {
            tmp.add(a);
        }
        for (E a : elements) {
            tmp.add(a);
        }
        return tmp;
    }

    @Override
    public Set<E> intersection(Set<? extends E> other) {
        Set<E> tmp = new ArrayListSet<>();
        for (E a : other) {
            if (contains(a)){
                tmp.add(a);
            }
        }
        return tmp;
    }

    /**
     * For this method, it should be possible to compare all other possible sets
     * for equality with this set. For example, if an ArrayListSet<Fruit> and a
     * LinkedListSet<Fruit> both contain the same elements they are equal.
     * Similarly, if a Set<Apple> contains the same elements as a Set<Fruit>
     * they are also equal.
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof Iterable<?>){
            for (Object e : (Iterable<?>) other) {
                for (Object a : elements){
                    if (e instanceof Fruit && a instanceof Fruit){
                        Fruit tmp_a = (Fruit) a;
                        Fruit tmp_e = (Fruit) e;
                        if (! tmp_a.getName().equals(tmp_e.getName()) ){
                            return false;
                        }
                    } else {
                        if (e != a){
                            return false;
                        }
                    }
                }
            }
        } else {
            return false;
        }
        return true;
    }

}
