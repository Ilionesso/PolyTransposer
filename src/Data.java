import java.util.Arrays;

/**
 * Created by Ilia Sheiko on 20/12/2017.
 */
class TriLine {
    public int[] value;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TriLine triLine = (TriLine) o;

        return Arrays.equals(value, triLine.value);

    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(value);
    }

    public TriLine(int first, int second, int third) {
        value = new int[3];
        value[0] = first;
        value[1] = second;
        value[2] = third;
    }
}

class TriChar {
    public Character[] value;

    @Override
    public String toString() {
        return "TriChar{" +
                "value=" + Arrays.toString(value) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TriChar triLine = (TriChar) o;

        return Arrays.equals(value, triLine.value);

    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(value);
    }

    public TriChar(Character first, Character second, Character third) {
        value = new Character[3];
        value[0] = first;
        value[1] = second;
        value[2] = third;
    }
}