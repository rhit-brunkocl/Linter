package domain;

import jdk.internal.org.objectweb.asm.commons.Method;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.util.ArrayList;

import static org.objectweb.asm.Type.getObjectType;

public class TemplateCheck {


    public TemplateCheck(){

    }

    public String check(ClassNode node){
        ClassReader reader = null;
        try {
            reader = new ClassReader(getObjectType(node.name).getClassName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ArrayList<String> methodNames = new ArrayList<>();
        for(MethodNode method: node.methods){
            methodNames.add(getObjectType(method.name).getClassName());
        }
        TemplateMethodDetector detector = new TemplateMethodDetector(methodNames);
        reader.accept(detector, ClassReader.SKIP_DEBUG);
        if (detector.isTemplateMethodUsed()) {
//            System.out.println("Template method is used.");
            return "Template Method is used";
        } else {
//            System.out.println("Template method is not used.");
            return "Template method is not used";
        }
    }

    public class TemplateMethodDetector extends ClassVisitor {
//        private String templateMethodName;
        private ArrayList<String> templateMethodNames;
        private ArrayList<String> methodUsed;
//        private boolean templateMethodUsed = false;

        public TemplateMethodDetector(ArrayList<String> parentNames) {
            super(Opcodes.ASM7);
            this.templateMethodNames = parentNames;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            if (templateMethodNames.contains(name)) {
                methodUsed.add(name);
                return null;
            }
            return super.visitMethod(access, name, descriptor, signature, exceptions);
        }

        public boolean isTemplateMethodUsed() {
            double percent = 1.00 * methodUsed.size() / templateMethodNames.size();
            return percent > 0.7;
        }
    }
}
