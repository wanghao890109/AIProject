package com.hao.common.utils.array;

public class BinaryArray {
    private int[] mKeys;
    private int mSize;

    public BinaryArray() {
        this(8);
    }

    public BinaryArray(int initialCapacity) {
        mKeys = new int[initialCapacity];
        mSize = 0;
    }

    public void put(int key) {
        int i = ContainerHelpers.binarySearch(mKeys, mSize, key);
        if (i < 0) {
            i = ~i;
            mKeys = ContainerHelpers.insert(mKeys, mSize, i, key);
            mSize++;
        }
    }

    public boolean delete(int key) {
        int i = ContainerHelpers.binarySearch(mKeys, mSize, key);

        if (i >= 0) {
            System.arraycopy(mKeys, i + 1, mKeys, i, mSize - (i + 1));
            mSize--;
            return true;
        }
        return false;
    }

    public boolean find(int key) {
        int i = ContainerHelpers.binarySearch(mKeys, mSize, key);
        return i >= 0;
    }

    public void clear() {
        mSize = 0;
    }

    public int size() {
        return mSize;
    }

    public int keyAt(int index) {
        return mKeys[index];
    }

    @Override
    public String toString() {
        if (size() <= 0) {
            return "{}";
        }

        StringBuilder buffer = new StringBuilder(mSize * 28);
        buffer.append('{');
        for (int i = 0; i < mSize; i++) {
            if (i > 0) {
                buffer.append(", ");
            }
            int key = keyAt(i);
            buffer.append(key);
        }
        buffer.append('}');
        return buffer.toString();
    }
}
