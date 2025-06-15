public class MyHashMap<K, V> {
    private static class Entry<K, V> {
        final K key;
        V value;
        Entry<K, V> next;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Entry<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Entry[INITIAL_CAPACITY];
        size = 0;
    }

    private int getIndex(K key) {
        return Math.abs(key.hashCode()) % table.length;
    }

    public V get(K key) {
        int index = getIndex(key);
        Entry<K, V> entry = table[index];

        while (entry != null) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
            entry = entry.next;
        }

        return null;
    }

    public void put(K key, V value) {
        if (key == null) return;

        resizeIfNeeded();

        int index = getIndex(key);
        Entry<K, V> entry = table[index];

        while (entry != null) {
            if (entry.key.equals(key)) {
                entry.value = value;
                return;
            }
            entry = entry.next;
        }

        Entry<K, V> newEntry = new Entry<>(key, value);
        newEntry.next = table[index];
        table[index] = newEntry;
        size++;
    }

    public void remove(K key) {
        if (key == null) return;

        int index = getIndex(key);
        Entry<K, V> entry = table[index];
        Entry<K, V> prev = null;

        while (entry != null) {
            if (entry.key.equals(key)) {
                if (prev == null) {
                    table[index] = entry.next;
                } else {
                    prev.next = entry.next;
                }
                size--;
                return;
            }
            prev = entry;
            entry = entry.next;
        }
    }

    private void resizeIfNeeded() {
        if ((float) size / table.length >= LOAD_FACTOR) {
            Entry<K, V>[] oldTable = table;
            table = new Entry[table.length * 2];
            rehash(oldTable);
        }
    }

    private void rehash(Entry<K, V>[] oldTable) {
        for (Entry<K, V> entry : oldTable) {
            while (entry != null) {
                Entry<K, V> next = entry.next;
                int newIndex = Math.abs(entry.key.hashCode()) % table.length;
                entry.next = table[newIndex];
                table[newIndex] = entry;

                entry = next;
            }
        }
    }
}