package domain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.util.ArrayList;

import static org.objectweb.asm.Type.getObjectType;

public class BadNameCheck {
    private final ClassNode node;
    private final ArrayList<String> names = new ArrayList<>();

    public BadNameCheck(ClassNode node) {
        this.node = node;
    }

    public void check() throws IOException {
        ClassReader superReader = new ClassReader(this.node.superName);
        ClassNode superClassNode = new ClassNode();
        superReader.accept(superClassNode, 0);

        String className = getObjectType(node.name).getClassName();
        names.add(className);


        for(MethodNode method : superClassNode.methods) {
           String currentName = getObjectType(method.name).getClassName();
           //check case

        }

        for(String checkName: names){
            this.checkLength(checkName);
        }
    }

    private void checkLength(String name) {
        char[] characters = ;
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

}

//public class MethodNode ... {
//public int access;
//public String name;
//public String desc;
//public String signature;
//public List<String> exceptions;
//public List<AnnotationNode> visibleAnnotations;
//public List<AnnotationNode> invisibleAnnotations;
//public List<Attribute> attrs;
//public Object annotationDefault;
//public List<AnnotationNode>[] visibleParameterAnnotations;
//public List<AnnotationNode>[] invisibleParameterAnnotations;
//public InsnList instructions;
//public List<TryCatchBlockNode> tryCatchBlocks;
//public List<LocalVariableNode> localVariables;
//public int maxStack;
//public int maxLocals;
//        }

