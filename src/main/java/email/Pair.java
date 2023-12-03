package email;

import java.util.Date;

public class Pair<K, V, T> {

    private K key;
    private V value;
    private Date time;

    public Pair(K key, V value, Date time) {
        this.key = key;
        this.value = value;
        this.time = time;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public Date getTime() {
        return time;
    }
}
