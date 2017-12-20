import java.util.Arrays;
import java.util.HashMap;

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

class TriGram {
    public Character[] value;

    @Override
    public String toString() {
        return "TriGram{" +
                "value = " + Arrays.toString(value) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TriGram triLine = (TriGram) o;

        return Arrays.equals(value, triLine.value);

    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(value);
    }

    public TriGram(Character first, Character second, Character third) {
        value = new Character[3];
        value[0] = first;
        value[1] = second;
        value[2] = third;
    }
}

class PermutationBranch {
    HashMap<TriGram, Integer> trigrams = new HashMap<>();
    public PermutationBranch parent;

    @Override
    public String toString() {
        String toRet = "";
        for (TriGram triGram : trigrams.keySet())
        toRet += triGram.toString() + "Counts: " + trigrams.get(triGram);
        return toRet;
    }

    public void setParent(PermutationBranch parent) {
        this.parent = parent;
    }

    public boolean containsTrigram(TriGram triGram){
        return trigrams.containsKey(triGram);
    }

    public void putTrigram(TriGram triGram, Integer counts){
        trigrams.put(triGram, counts);
    }

    public void addCounts(TriGram triGram, Integer counts){
        trigrams.replace(triGram, trigrams.get(triGram) + counts);
    }

    public void putOrAppendTrigram(TriGram triGram, Integer counts){
        if (!containsTrigram(triGram))
            putTrigram(triGram, counts);
        addCounts(triGram, counts);
    }
}