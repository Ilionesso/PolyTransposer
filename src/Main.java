import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Ilia Sheiko on 20/12/2017.
 */
public class Main {
    static HashMap<TriLine, HashMap<TriGram, Integer>> triLineMap;
    static int minimalCount = 3;
    static int secondMinimalCount = 2;

    public static void main(String[] args){
        String strInput = "HNREHVTAONEAARTECUATOTOEESHOFUNHDSISAYEOOIOOETOTENAEWWTGISESHLLDMOFVUYEUHSXOAHTOIHRONHEAOVUODDHARTRXTNSIMWSCDKERENOMYENSDFECXETRLOIHNSHEDDNTTHMRHUYMLE";
        char[] input = strInput.toCharArray();
        int heigth = 6;
        int width = 25;
        triLineMap = new HashMap<>();
        try {
            inspectTable(input, heigth, width);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static ArrayList<ArrayList<Character>> makeLines(char[] input, int heigth, int width){
        ArrayList<ArrayList<Character>> lines = new ArrayList<>();
        for (int i = 0; i < heigth; i++){
            ArrayList<Character> newLine = new ArrayList<>(width);
            for (int j = 0; j < width; j++)
                newLine.add(input[i * width + j]);
            lines.add(newLine);
        }
        return lines;
    }

    private static void inspectVerticals(int heigth, int width, ArrayList<ArrayList<Character>> lines){
        for (int firstLine = 0; firstLine < heigth; firstLine++)
            for (int secondLine = 0; secondLine < heigth; secondLine++)
                if (secondLine != firstLine)
                    for (int thirdLine = 0; thirdLine < heigth; thirdLine++) {
                        if (thirdLine != secondLine && thirdLine != firstLine) {
                            TriLine newUberKey = new TriLine(firstLine, secondLine, thirdLine);
                            triLineMap.put(newUberKey, new HashMap<>());
                            for (int iter = 0; iter < width; iter++) {
                                TriGram triGram = new TriGram(lines.get(firstLine).get(iter), lines.get(secondLine).get(iter), lines.get(thirdLine).get(iter));
                                if (!triLineMap.get(newUberKey).containsKey(triGram))
                                    triLineMap.get(newUberKey).put(triGram, 1);
                                else triLineMap.get(newUberKey).put(triGram, triLineMap.get(newUberKey).get(triGram)+1);
                            }
                        }
                    }
    }

    private static void inspectTable(char[] input, int heigth, int width) throws IOException {
        //Build lines of symbols
        ArrayList<ArrayList<Character>> lines = makeLines(input, heigth, width);
        System.out.println("Lines are built");

        //Build map about frequencies of 3 symbols in 3 lines
        inspectVerticals(heigth, width, lines);
        System.out.println("TriLine map is built");

        int[] lineNumbers = new int[heigth];
        for (int i = 0; i < heigth; i++) lineNumbers[i] = i;
        ArrayList<ArrayList<Integer>> permutations = permute(lineNumbers);
        System.out.println("Writing is processing");
        File file = new File("output.txt");
        BufferedWriter output = new BufferedWriter(new FileWriter(file));
        for (ArrayList<Integer> permutation : permutations){

            HashMap<TriGram, Integer> triGrams = new HashMap<>();
            HashMap<TriGram, Integer> goodTriGrams = new HashMap<>();
            HashMap<TriGram, Integer> fineTriGrams = new HashMap<>();
            for (int i = 0; i < heigth; i++){
                if (i < heigth-2) {
                    int j = i+1;
                    int k = i+2;
                    TriLine triLine = new TriLine(permutation.get(i), permutation.get(j), permutation.get(k));
                    HashMap<TriGram, Integer> localTriGrams = triLineMap.get(triLine);
                    for (TriGram characters : localTriGrams.keySet())
                        if (!triGrams.containsKey(characters))
                            triGrams.put(characters, localTriGrams.get(characters));
                        else triGrams.replace(characters, triGrams.get(characters) + localTriGrams.get(characters));
                }
                else{
                    int firstLine = i;
                    int secondLine = i+1 < heigth ? i + 1 : i + 1 - heigth;
                    int thirdLine = i+1 < heigth ? i + 1 : i + 1 - heigth;

                    for (int iter = 0; iter < width-1; iter++) {
                        TriGram triGram = secondLine > firstLine ?
                                  new TriGram(lines.get(firstLine).get(iter), lines.get(secondLine).get(iter), lines.get(thirdLine).get(iter+1))
                                : new TriGram(lines.get(firstLine).get(iter), lines.get(secondLine).get(iter + 1), lines.get(thirdLine).get(iter+1));
                        if (!triGrams.containsKey(triGram))
                            triGrams.put(triGram, 1);
                        else triGrams.put(triGram, triGrams.get(triGram)+1);
                    }
                }
            }
            for (TriGram triGram : triGrams.keySet()){
                if (triGrams.get(triGram) == minimalCount)
                    goodTriGrams.put(triGram, triGrams.get(triGram));
                if (triGrams.get(triGram) == secondMinimalCount)
                    fineTriGrams.put(triGram, triGrams.get(triGram));
            }

            if (!goodTriGrams.isEmpty() && fineTriGrams.keySet().size()>5){
                output.write("Permutation: ");
                for (Integer integer : permutation)
                    output.write(integer+ " ");
                output.write("Decoded: " + inputToPermutation(permutation, input, heigth, width));
                output.write("\n");
                for (TriGram characters : goodTriGrams.keySet()){
                    output.write("  Trigram: " + characters.toString());
                    output.write("\n  Count: " + goodTriGrams.get(characters)+ "\n");
                    output.write("\n");
                }
                output.write("=======================\n");
            }

        }
        output.close();
    }

    private static void inspectPermutation(ArrayList<Integer> permutation, int depth, int heigth, PermutationBranch parent){
        PermutationBranch branch = new PermutationBranch();
        branch.setParent(parent);
        if (depth >= 3){
            TriLine triLine = new TriLine(permutation.get(depth), permutation.get(depth-1), permutation.get(depth-2));
            HashMap<TriGram, Integer> localTriGrams = triLineMap.get(triLine);
            for (TriGram triGram : localTriGrams.keySet())
                branch.putOrAppendTrigram(triGram, localTriGrams.get(triGram));
        }
    }

    private static String inputToPermutation(ArrayList<Integer> key, char[] input, int heigth, int width){
        String toRet = "";
        for (int i = 0; i < width; i++)
            for (int j = 0; j < heigth; j++)
                toRet += input[i + key.get(j)*width];

        return toRet;
    }

    public static ArrayList<ArrayList<Integer>> permute(int[] num) {
        ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();

        //start from an empty list
        result.add(new ArrayList<Integer>());

        for (int i = 0; i < num.length; i++) {
            //list of list in current iteration of the array num
            ArrayList<ArrayList<Integer>> current = new ArrayList<ArrayList<Integer>>();

            for (ArrayList<Integer> l : result) {
                // # of locations to insert is largest index + 1
                for (int j = 0; j < l.size()+1; j++) {
                    // + add num[i] to different locations
                    l.add(j, num[i]);

                    ArrayList<Integer> temp = new ArrayList<Integer>(l);
                    current.add(temp);

                    //System.out.println(temp);

                    // - remove num[i] add
                    l.remove(j);
                }
            }

            result = new ArrayList<ArrayList<Integer>>(current);
        }

        return result;
    }
}

