package edu.gcccd.csis;

import java.util.Iterator;
import java.io.*;
import java.util.Scanner;

public class MyProject2 implements Project2 {

    /**The iterator method will be called 3L + 2S + 1 times,
    // where L is the length of the longer NodeList
    // and S is the length of the shorter.
    // Because the most significant impact on runtime scales linearly
    // with the longer NodeList, this method is O(L)
     */
    @Override
    public NodeList<Integer> addition(NodeList<Integer> nodeList1, NodeList<Integer> nodeList2) {
        int lengthDifference = nodeList1.getLength() - nodeList2.getLength();
        Iterator<Integer> a = nodeList1.iterator(), b = nodeList2.iterator();
        NodeList<Integer> sum = new NodeList<>(), chain = new NodeList<>();
        Integer current = 0, next = 0, dominoes = 0, spaceBetween = 0;

        //Equalize their lengths
        if(lengthDifference != 0) {
            while (lengthDifference > 0) {
                current = next;
                next = a.next();
                if (next.equals(9)) {
                    dominoes++;
                } else if (dominoes.compareTo(0) > 0){
                    spaceBetween = spaceBetween+dominoes+1;
                    dominoes = 0;
                } else { spaceBetween++; }
                sum.append(current);
                lengthDifference--;
            }
            while (lengthDifference < 0) {
                current = next;
                next = b.next();
                if (next.equals((Integer) 9)) {
                    dominoes++;
                } else if (dominoes.compareTo(0) > 0){
                    spaceBetween = spaceBetween+dominoes;
                    dominoes = 0;
                } else {spaceBetween++;}
                sum.append(current);
                lengthDifference++;
            }
        }
        //Now current should be the digit prior to the start of the addition process,
        //And the iterators a and b should have the same number of digits remaining
        while (a.hasNext() && b.hasNext()) {
            if (next.equals(9)) {
                dominoes++;
            } else {
                if (dominoes.equals(0)) {
                    spaceBetween++;
                } else {
                    if (next.compareTo(9) > 0) {
                        chain.append(spaceBetween);
                        chain.append(dominoes);
                        spaceBetween = 0;
                        dominoes = 0;
                        next = next - 10;
                    } else {
                        spaceBetween = spaceBetween + dominoes +1;
                        dominoes = 0;
                    }
                }
            }
            sum.append(current);
            current = next;
            next = a.next() + b.next();
        }
        //Next is now the last digit resulting from the addition
        sum.append(current);
        sum.append(next);
        chain.append(-1);

        //Now iterate through the sum, taking care of any domino effects
        a = sum.iterator(); b = chain.iterator();
        NodeList<Integer> complete = new NodeList<>();
        int place = b.next();
        //Remove leading zeroes
        while (a.hasNext()) {
            current = a.next();
            if (!current.equals(0) || place==0) {
                break;
            }
        }
        //Current is now the first non-zero member of sum
        do {
            //If this is the start of a chain, increment it,
            //then set everything until the end of the chain to 0
            if (place == 0) {
                place = b.next();
                complete.append(current+1);
                while (place > 0) {
                    complete.append(0);
                    current = a.hasNext() ? a.next() : current;
                    place--;
                }
                place = b.next();
            } else {
                complete.append(current);
                current = a.hasNext() ? a.next() : current;
                place--;
            }
        } while (a.hasNext());
        return complete;
    }

    /**
     * For each NodeList stored beyond the first, the other addition method is called.
     * (See above for analysis)
     * Because the length of the NodeLists stored is unknown,
     * it is difficult to predict the total number of times iterator.next() is called.
     * However, on the parameter given itself, next() will be called L times,
     * where L is the length remaining on the NodeList the iterator is attached to.
     * i.e. if there are 5 NodeLists and the iterator was called 2 times before being passed...
     * then next() could only be called twice.
     * */
    @Override
    public NodeList<Integer> addition(Iterator<NodeList<Integer>> iterator) {
        NodeList<Integer> sum = new NodeList<>();
        while (iterator.hasNext()){
            NodeList<Integer> toAdd = iterator.next();
            sum = addition(sum, toAdd);
        }
        return sum;
    }

    /**
     * next() is called exactly L times, where L is the length of the NodeList.
     * O(L)
     * */
    @Override
    public void save(NodeList<Integer> nodeList, String fileName) {
        try {
            File saveTo = new File(fileName);
            FileWriter writer = new FileWriter(saveTo);
            for (Integer a: nodeList) {
                writer.write(a.intValue());
            }
            writer.close();
        } catch (IOException ignored) {
            ignored.printStackTrace();
        }
    }

    /**
     * next() is never called. Because of linear scaling,
     * O(L), where L is the length of the number stored within the file.
     * */
    @Override
    public NodeList<Integer> load(String fileName) {
        Scanner reads = new Scanner(fileName);
        reads.useDelimiter("");
        final NodeList<Integer> nodeList = new NodeList<>();
        while (reads.hasNext()) {
            nodeList.append(
                    Character.getNumericValue(reads.next().charAt(0)));
        }
        return nodeList;
    }

    public static void main(final String[] args) {
        final int L = 30;

        final NodeList<Integer> n1 = Project2.generateNumber(L); // (head 1st) e.g. 3457
        final NodeList<Integer> n2 = Project2.generateNumber(L); // (head 1st) e.g. 682

        final Project2 p = new MyProject2();

        Project2.print(p.addition(n1, n2)); //  n1+n2, e.g. 4139

        final NodeList<NodeList<Integer>> listOfLists = new NodeList<>();
        for (int i = 0; i < L; i++) {
            listOfLists.append(Project2.generateNumber(L));
        }
        p.save(p.addition(listOfLists.iterator()), "result.bin");
        Project2.print(p.load("result.bin"));
    }
}
