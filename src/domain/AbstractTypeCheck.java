package domain;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class AbstractTypeCheck {
	private ClassNode node;
	private ArrayList<String> abstractMethods = new ArrayList<>();

	public AbstractTypeCheck(ClassNode node) {
		this.node = node;
	}
	
	public void check() throws IOException {
		ClassReader superClassReader = new ClassReader(this.node.superName);
        ClassNode superClassNode = new ClassNode();
        superClassReader.accept(superClassNode, 0);

        for (MethodNode method : superClassNode.methods) {
            if ((method.access & Opcodes.ACC_ABSTRACT) != 0) {
                abstractMethods.add(method.name + method.desc);
            }
        }

        for (MethodNode method : this.node.methods) {
            if ((method.access & Opcodes.ACC_SYNTHETIC) == 1) {
                abstractMethods.remove(method.name + method.desc);
            }
        }

        if(abstractMethods.size() != 0) {
        	System.out.println("Missing implementation of abstract methods from parent class" + this.abstractMethods);
        }else {
        	System.out.println("Class have impemented all abstract methods or there's no abstract classes");
        }
    }
}
