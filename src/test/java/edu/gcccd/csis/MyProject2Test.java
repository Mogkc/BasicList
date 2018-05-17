package edu.gcccd.csis;

import org.junit.Test;

import java.io.File;
import java.math.BigInteger;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;

public class MyProject2Test {

    /**
     * Adding two long integer values
     */
    @Test
    public void testAdditionOfTwo() {
        MyProject2 project = new MyProject2();

        assertEquals("0",
                listToString(project.addition(new NodeList<Integer>(), new NodeList<Integer>())));

        NodeList<Integer> n1 = convert("00"), n2 = convert("0000");
        assertEquals("0", listToString(project.addition(n2, n1)));

        n1 = convert("1"); n2 = convert("2");
        assertEquals("3", listToString(project.addition(n2, n1)));

        n1 = convert("1234"); n2 = convert("4321");
        assertEquals("5555", listToString(project.addition(n2, n1)));

        NodeList<Integer> nines = new NodeList<>(), small = new NodeList<>();
        for (int i = 0; i < 300; i++) {
            nines.append(9);
        }

        Iterator<Integer> check = project.addition(nines, small).iterator(), against = nines.iterator();
        assertEquals(nines.getLength(), project.addition(nines, small).getLength());
        while (check.hasNext()) {
            assertEquals(check.next().intValue(), against.next().intValue());
        }
        small.append(0);
        check = project.addition(nines, small).iterator();
        against = nines.iterator();
        assertEquals(nines.getLength(), project.addition(nines, small).getLength());
        while (check.hasNext()) {
            assertEquals(check.next().intValue(), against.next().intValue());
        }
        small.append(1);
        assertEquals(nines.getLength()+1, project.addition(nines, small).getLength());
        check = project.addition(nines, small).iterator();
        assertEquals(1, check.next().intValue());
        while (check.hasNext()) {
            assertEquals(0, check.next().intValue());
        }

        n1 = Project2.generateNumber(50);
        n2 = Project2.generateNumber(50);
        BigInteger num1 = convert(n1), num2 = convert(n2);
        assertEquals(num1.add(num2), convert(project.addition(n1, n2)));
    }

    @Test
    public void testAdditionOfMany() {
        MyProject2 project = new MyProject2();
        NodeList<NodeList<Integer>> list = new NodeList<>();
        NodeList<Integer> n1 = Project2.generateNumber(30);
        list.append(n1);
        assertEquals(n1.getLength(), project.addition(list.iterator() ).getLength());
        BigInteger num1 = convert(n1);
        for (int i=0; i < 10; i++) {
            NodeList<Integer> n2 = Project2.generateNumber(30);
            num1 = num1.add(convert(n2));
            list.append(n2);
        }
        assertEquals(num1, convert(project.addition(list.iterator())));
    }

    @Test
    public void testSaveAndLoad() {
        final String test = "result.bin";
        MyProject2 project = new MyProject2();
        for (int i = 0; i < 15; i++) {
            NodeList<Integer> a = Project2.generateNumber(50);
            project.save(a, test);
            assertEquals(a.getLength(), project.load(test).getLength());
        }
    }

    NodeList<Integer> convert(final String in) {
        NodeList<Integer> out = new NodeList<>();
        for (int i = 0; i < in.length()-1; i++) {
            out.append(Character.getNumericValue(in.charAt(i)));
        }
        return out;
    }

    BigInteger convert(final NodeList<Integer> in) {
        return new BigInteger(listToString(in) );
    }

    String listToString(final NodeList<Integer> in) {
        Iterator run = in.iterator();
        String out = "";
        while (run.hasNext()) {
            out = out.concat(run.next().toString());
        }
        return out;
    }
}
