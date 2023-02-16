package domain;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static org.objectweb.asm.Type.getObjectType;

public class TemplateCheck implements CheckBehavior {


    public TemplateCheck(){

    }


    @Override
    public String check(String route, ClassReader reader, ClassNode node){
        ArrayList<Boolean> checkMarks = new ArrayList<>();
        ArrayList<Integer> passes = new ArrayList<Integer>();
        ArrayList<String> methodNames = new ArrayList<>();

        //check abstract methods
        passes.add(0,0);
        passes.add(1,0);
        passes.add(2,0);

        ClassReader cr = reader;

        ClassWriter cw = new ClassWriter(cr, 0);
        AbstractVisitor visitor = new AbstractVisitor(cw, passes);
        cr.accept(visitor, 0);

        HookVisitor hookVisitor = new HookVisitor(cw, visitor.returnAbstracts(),passes);

        for(int i : passes){
            System.out.println(i);
        }

        return null;
    }

//    @Override
//    public String check(ClassNode node){
//        String className = getObjectType(node.name).getClassName();
//        ArrayList<Boolean> checkMarks = new ArrayList<>();
//        ArrayList<Integer> passes = new ArrayList<Integer>();
//        ArrayList<String> methodNames = new ArrayList<>();
//        for(MethodNode method: node.methods){
//            methodNames.add(getObjectType(method.name).getClassName());
//        }
//
//        //check abstract methods
//        passes.add(0,0);
//        passes.add(1,0);
//        passes.add(2,0);
//
//
//        ClassReader cr = null;
//        try {
//            cr = new ClassReader(className);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        ClassWriter cw = new ClassWriter(cr, 0);
//        AbstractVisitor visitor = new AbstractVisitor(cw, passes);
//        cr.accept(visitor, 0);
//
//        HookVisitor hookVisitor = new HookVisitor(cw, visitor.returnAbstracts(),passes);

        //check childs
        //get the package name first
//        String internalName = node.name;
//        int lastSlashIndex = internalName.lastIndexOf('/');
//        String packageName = (lastSlashIndex == -1) ? "" : internalName.substring(0, lastSlashIndex).replace('/', '.');
//
//        ArrayList<String> classesInPackage = this.findAllClassesUsingClassLoader(packageName);
//        for(String classn: classesInPackage) {
//            try {
//                System.out.println(classn);
//
//                ClassReader reader = new ClassReader(classn);
//                ClassWriter writer = new ClassWriter(cr, 0);
//                ExtendVisitor ev = new ExtendVisitor(cw, classn, passes);
//                cr.accept(ev, 0);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }

//        for(int i : passes){
//            System.out.println(i);
//        }
//       return null;
//    }

    public class TemplateMethodDetector extends ClassVisitor {
//        private String templateMethodName;
        private final ArrayList<String> templateMethodNames;
        private final ArrayList<String> methodUsed;
//        private boolean templateMethodUsed = false;

        public TemplateMethodDetector(ArrayList<String> parentNames) {
            super(Opcodes.ASM7);
            this.templateMethodNames = parentNames;
            this.methodUsed = new ArrayList<String>();
        }

        public boolean isTemplateMethodUsed() {
            double percent = 1.00 * methodUsed.size() / templateMethodNames.size();
            return percent > 0.7;
        }
    }

    private class AbstractVisitor extends ClassVisitor {
        ArrayList<Integer> passes;
        ArrayList<String> abstracts;
        ArrayList<String> implemented;

        public AbstractVisitor(ClassVisitor cv, ArrayList<Integer> passes) {
            super(Opcodes.ASM5, cv);
            this.passes = passes;
            this.abstracts = new ArrayList<>();
            this.implemented = new ArrayList<>();
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
            if ((access & Opcodes.ACC_ABSTRACT) != 0) {
                // Abstract method found
                abstracts.add(name);
                passes.set(0, passes.get(0) + 1);
            } else {
                // Implemented method found
                System.out.println("Implemented method: " + name);
                implemented.add(name);
            }
            return mv;
        }

        private ArrayList<String> returnAbstracts(){
            return this.abstracts;
        }

        private ArrayList<String> returnImplemented(){
            return this.implemented;
        }
    }



    public class ExtendVisitor extends ClassVisitor {
        String parentName;
        ArrayList<Integer> passes;

        public ExtendVisitor(ClassVisitor cv, String name, ArrayList<Integer> passes) {
            super(Opcodes.ASM7, cv);
            this.parentName = name;
            this.passes = passes;
        }

        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            // Check if the superclass is present
            if (superName != null && superName.equals(parentName)) {
                System.out.println("super class found");
                passes.set(1, passes.get(1) + 1);
            }

            // Check if any interfaces are present
            for (String interFace : interfaces) {
                if (interFace.equals(parentName)) {
                    //not considering template pattern with interface as the parent
                    //but putting this here for possibilities
                }
            }

            super.visit(version, access, name, signature, superName, interfaces);
        }


        }

        public ArrayList<String> findAllClassesUsingClassLoader(String packageName) {
            InputStream stream = ClassLoader.getSystemClassLoader()
                    .getResourceAsStream(packageName.replaceAll("[.]", "/"));
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            ArrayList<Class> classSet =  (ArrayList<Class>) reader.lines()
                    .filter(line -> line.endsWith(".class"))
                    .map(line -> getClass(line, packageName))
                    .collect(Collectors.toList());
            int prelength = packageName.length() + 1;
            ArrayList<String> classNames = new ArrayList<>();
            for(Class aclass : classSet){
                if(!classNames.contains(aclass.getName().substring(prelength)))
                    classNames.add(aclass.getName().substring(prelength));
            }
            return classNames;
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



    public class HookVisitor extends ClassVisitor {

        ArrayList<String> abstracts;
        ArrayList<Integer> passes;

        public HookVisitor(ClassVisitor cv, ArrayList<String> abstracts, ArrayList<Integer> passes) {
            super(Opcodes.ASM5, cv);
            this.abstracts = abstracts;
            this.passes = passes;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
            return new MethodVisitor(Opcodes.ASM5, mv) {

                @Override
                public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
                    if (abstracts.contains(name)) {
                        passes.set(2, passes.get(2) + 1);
                    }
                    super.visitMethodInsn(opcode, owner, name, desc, itf);
                }
            };
        }
    }


}
