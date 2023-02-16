package domain;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

import static org.objectweb.asm.Type.*;

public class TightCouplingCheck implements CheckBehavior{
    private String packageName = "tests";
    private String prefix = "L" + packageName + "/";
    private int prelength = packageName.length() + 1;

    public String check(ClassNode node){
        String internalName = node.name;
        int lastSlashIndex = internalName.lastIndexOf('/');
        this.packageName = (lastSlashIndex == -1) ? "" : internalName.substring(0, lastSlashIndex).replace('/', '.');

        String className = getObjectType(node.name).getClassName();
        System.out.println();
        System.out.println("For class " + className + " : " );
        HashSet<String> usedFor = countDesc(node);
        HashSet<String> usedIn = countUsage(node);
        //print classed used for methods ie input / output
        if(usedFor.size() > 3 ){
            System.out.println("Too many classes used for methods");
            System.out.println(usedFor.size() + " classes were used : ");
            for(String s : usedFor){
                System.out.print(s + " ");
            }
            System.out.println();
        }

        //print classes used inside methods
        if(usedIn.size() > 3 ){
            System.out.println("Too many classes used inside methods");
            System.out.println(usedIn.size() + " classes were used : ");
            for(String s : usedIn){
                System.out.print(s + " ");
            }
            System.out.println();
        }


        //print total
        HashSet<String> total = new HashSet<>();
        total.addAll(usedFor);
        total.addAll(usedIn);

        if(total.size() > 4 ){
            System.out.println("Too many classes used inside the class");
            System.out.println(total.size() + " classes were used : ");
            for(String s : total){
                System.out.print(s + " ");
            }
            System.out.println();
        }

        return null;
    }

    @Override
    public String check(ClassReader reader) {
        return null;
    }

    public ArrayList<Class> findAllClassesUsingClassLoader(String packageName) {
        InputStream stream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(packageName.replaceAll("[.]", "/"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        return (ArrayList<Class>) reader.lines()
                .filter(line -> line.endsWith(".class"))
                .map(line -> getClass(line, packageName))
                .collect(Collectors.toList());
    }

    private Class getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "."
                    + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            // handle the exception
        }
        return null;
    }

    private HashSet<String> countDesc(ClassNode node){
        HashSet<String> used = new HashSet<>();

        String className = getObjectType(node.name).getClassName();
        ArrayList<Class> classSet = findAllClassesUsingClassLoader(packageName);
        HashSet<String> classNames = new HashSet<>();
        for(Class aclass : classSet){
            classNames.add(aclass.getName().substring(prelength));
        }
        classNames.remove(className);

        for(MethodNode method : node.methods) {
            HashSet<String> usedInMethod = new HashSet<String>();
            for(String name : classNames){
                if(method.desc.contains(name)){
                    usedInMethod.add(name);
                }
            }
//            if(usedInMethod.size() > 1) System.out.println("method " + getObjectType(method.name).getClassName() +
//                    " is related with "+ + usedInMethod.size() + " other classes " );
            used.addAll(usedInMethod);
        }

        for(FieldNode field : node.fields){
            for(String name : classNames){
                if(field.desc.contains(name)){
                    used.add(name);
                }
            }
        }

//        if(used.size() > 3){
//            System.out.println("Class " + className + " has too many references of other classes");
//            System.out.println(used.size() + " Other classes are referenced " );
//            System.out.println("Used classes: ");
//            for(String s : used){
//                System.out.print(s + ", ");
//            }
//            System.out.println(" ");
//
//        }
        //to be implemented to match overall test case
        return used;
    }

    private HashSet<String> countUsage(ClassNode node){
        String className = getObjectType(node.name).getClassName();

        //or doubly linked list?
        try {
            HashSet<String> used = new HashSet<>();
            ClassReader cr = new ClassReader(className);
            ClassVisitor cv = new ClassVisitor(Opcodes.ASM7){
                @Override
                public MethodVisitor visitMethod(int access, String name, String desc,
                                                 String signature, String[] exceptions){
                    MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
                    return new MethodVisitor(Opcodes.ASM7, mv) {
                        @Override
                        public void visitTypeInsn(int opcode, String type) {
                            used.add(type.substring(prelength));
                        }
                    };
                }
            };

            cr.accept(cv, ClassReader.EXPAND_FRAMES);

            System.out.println("classes used in methods: ");
            if(used.size() == 0){
                System.out.print("no other classes are used inside methods");
                System.out.println();
            }else {
                for (String s : used) {
                    System.out.println(s);
                    System.out.println();
                }
            }
            return used;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
