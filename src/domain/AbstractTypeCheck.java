package domain;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class AbstractTypeCheck implements CheckBehavior {
	private ArrayList<String> abstractMethods = new ArrayList<>();
	
	public String check(ClassNode node) {
		ClassReader superClassReader = null;
		try {
			superClassReader = new ClassReader(node.superName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        ClassNode superClassNode = new ClassNode();
        superClassReader.accept(superClassNode, 0);

        for (MethodNode method : superClassNode.methods) {
            if ((method.access & Opcodes.ACC_ABSTRACT) != 0) {
                abstractMethods.add(method.name + method.desc);
            }
        }

        for (MethodNode method : node.methods) {
            if ((method.access & Opcodes.ACC_SYNTHETIC) == 0) {
                abstractMethods.remove(method.name + method.desc);
            }
        }

        if(abstractMethods.size() != 0) {
        	//System.out.println("Issue in "+ node.name + ": Missing implementation of abstract methods from parent class.");
        	return String.format("Issue in %s: Missing implementation of abstract methods from parent class.\n", Type.getObjectType(node.name).getClassName());
        }else {
        	return "";
        }
    }

    @Override
    public String check(ClassReader reader) {
        return null;
    }
}
