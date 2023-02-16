package domain;


import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;

import static org.objectweb.asm.Type.getObjectType;

public class BadNameCheck implements CheckBehavior{
    private static final ArrayList<String> names = new ArrayList<>();
    private static final ArrayList<String> allowed = new ArrayList<>(Arrays.asList("in", "on", "if",
            "of", "up", "is", "or", "as"));

    public BadNameCheck() {
        //adding allowed words (2 letters)

    }

    public String check(String route, ClassReader readr, ClassNode node){
        String out = "";
        //check class name
        String className = getObjectType(node.name).getClassName();
        if(!names.contains(className)){
            names.add(className);
        }
        //Class names should begin with Upper case letters
        if(Character.isUpperCase(className.charAt(0))){
//            System.out.println("The method name " + className +
//                    " should begin with a lower case letter");
            out += "The method name " + className +
                    " should begin with a lower case letter \n";
        }

        //check method names
        for(MethodNode method : node.methods) {
           String currentName = getObjectType(method.name).getClassName();
            out += BadNameCheck.processSmallName(currentName);

        }

        for(FieldNode field : node.fields){
            String currentName = getObjectType(field.name).getClassName();
            out += BadNameCheck.processSmallName(currentName);
        }

        //check name length and similarities
        for(String checkName: names){
            //check length of name
            BadNameCheck.checkLength(checkName, out);
            int index = names.indexOf(checkName);
            for(int i = index + 1; i < names.size(); i++){
                String comparedName = names.get(i);
                double similarity = similarity(checkName.toLowerCase(), comparedName.toLowerCase());
                if(similarity > 0.85){
//                    System.out.println("the words: " + checkName + " and " + comparedName +
//                            " is too similar, the similarity is " + similarity *100 + "%");
                    out += "the words: " + checkName + " and " + comparedName +
                            " is too similar, the similarity is " + similarity *100 + "%" + "\n";
                }
            }
        }

        //to be implemented to match overall test case
        return null;
    }

    private static void checkLength(String name, String out) {
        if(name.length() > 40){
//            System.out.println("The name: " + name + " is too long");
           out += "The name: " + name + " is too long\n";
        }
        char[] chars = name.toCharArray();
        int indexLast = 0;
        for(int i = 0; i < chars.length; i++){
            char c = chars[i];
            if(Character.isUpperCase(c) || i == chars.length - 1){
//                int length = i - indexLast;
                String current = name.substring(indexLast, i);
                if(i == chars.length - 1) current = name.substring(indexLast);
//                System.out.println(current);
                if (current.length() == 2) {

//                if (length == 2 || (i == chars.length - 1 && length == 1 && Character.isLowerCase(c))) {
                    if(!allowed.contains(current.toLowerCase())){
//                        System.out.println(current + " in name " + name + " is too short for abbreviation");
                        out += current + " in name " + name + " is too short for abbreviation \n";
                    }
                }else if(current.length()>= 15){
//                    System.out.println(current + " is too long for a word in the name");
                    out += current + " is too long for a word in the name \n";
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

    private static String processSmallName(String currentName){
        String result = "";
        if(Character.isUpperCase(currentName.charAt(0))){
//            System.out.println("The name " + currentName +
//                    " should begin with a lower case letter");
            result += "The name " + currentName +
                    " should begin with a lower case letter\n";
        }
        if(!names.contains(currentName) && ! currentName.equals("<init>"))names.add(currentName);
        return result;
    }


}

