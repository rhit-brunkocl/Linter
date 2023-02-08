package domain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.util.ArrayList;

import static org.objectweb.asm.Type.getObjectType;

public class BadNameCheck {
    private final ClassNode node;
    private final ArrayList<String> names = new ArrayList<>();
    private final ArrayList<String> allowed = new ArrayList<>();

    public BadNameCheck(ClassNode node) {
        this.node = node;

        //adding allowed words (2 letters)
        this.allowed.add("on");
        this.allowed.add("if");
        this.allowed.add("in");
        this.allowed.add("of");
        this.allowed.add("up");
    }

    public void check() throws IOException {
        ClassReader superReader = new ClassReader(this.node.superName);
        ClassNode superClassNode = new ClassNode();
        superReader.accept(superClassNode, 0);

        //check class names
        String className = getObjectType(node.name).getClassName();
        names.add(className);
        //Class names should begin with Upper case letters
        if(Character.isUpperCase(className.charAt(0))){
            System.out.println("The method name " + className +
                    " should begin with a lower case letter");
        }

        //check method names
        for(MethodNode method : superClassNode.methods) {
           String currentName = getObjectType(method.name).getClassName();
            this.processSmallName(currentName);

        }

        for(FieldNode field : superClassNode.fields){
            String currentName = getObjectType(field.name).getClassName();
           this.processSmallName(currentName);
        }

        //check name length and similarities
        for(String checkName: names){
            //check length of name
            this.checkLength(checkName);
            int index = names.indexOf(checkName);
            for(int i = index + 1; i < names.size(); i++){
                String comparedName = names.get(i);
                double similarity = similarity(checkName.toLowerCase(), comparedName.toLowerCase());
                if(similarity > 0.85){
                    System.out.println("the words: " + checkName + " and " + comparedName +
                            " is too similar, the similarity is " + similarity *100 + "%");
                }
            }
        }
    }

    private void checkLength(String name) {
        if(name.length() > 40){
            System.out.println("The name: " + name + "is too long");
        }
        char[] chars = name.toCharArray();
        int indexLast = 0;
        for(int i = 0; i < chars.length; i++){
            char c = chars[i];
            if(Character.isUpperCase(c)){
                int length = i - indexLast;
                String current = name.substring(indexLast, i);
                if (length == 2) {
                    if(!this.allowed.contains(current.toLowerCase())){
                        System.out.println(current + "is too short for abbreviation");
                    }
                }else if(length >= 15){
                    System.out.println(current + "is too long for a word in the name");
                }
                indexLast = i;
            }
        }
        System.out.println();
    }


    //helper method to calculate the similarity between two names
    //no two names should be too similar.
    // Currently, the assumed max similarity is 85%
    private static double similarity(String s1, String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) { // longer should always have greater length
            longer = s2; shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) { return 1.0; /* both strings are zero length */ }
        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;
    }

    private static int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0)
                    costs[j] = j;
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1))
                            newValue = Math.min(Math.min(newValue, lastValue),
                                    costs[j]) + 1;
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0)
                costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
    }

    private void processSmallName(String currentName){
        if(Character.isUpperCase(currentName.charAt(0))){
            System.out.println("The name " + currentName +
                    " should begin with a lower case letter");
        }
        this.names.add(currentName);
    }

}

