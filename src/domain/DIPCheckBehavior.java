package domain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.signature.SignatureReader;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

public class DIPCheckBehavior implements CheckBehavior{
	public String check(ClassNode classNode) {
		String out = "";
		
		List<MethodNode> methods = (List<MethodNode>) classNode.methods;
		
		for(MethodNode method: methods) {
			AbstractInsnNode node = method.instructions.getFirst();
			while(node != null) {
				if(node.getType() == AbstractInsnNode.METHOD_INSN) {
					MethodInsnNode methodInsnNode = (MethodInsnNode) node;
					String methodName = methodInsnNode.owner + "." + methodInsnNode.name;
					ClassReader reader;
					try {
						reader = new ClassReader(methodInsnNode.owner);
						ClassNode methodClassNode = new ClassNode();
						reader.accept(methodClassNode, ClassReader.EXPAND_FRAMES);
						for(String interfaceName: methodClassNode.interfaces) {
							ClassReader intReader = new ClassReader(interfaceName);
							ClassNode interfaceNode = new ClassNode();
							intReader.accept(interfaceNode, ClassReader.EXPAND_FRAMES);
							List<MethodNode> interfaceMethods = (List<MethodNode>) interfaceNode.methods;
							for(MethodNode intMethod: interfaceMethods) {
								if(methodInsnNode.name.equals(intMethod.name)) {
									out += String.format("Issue in %s: method %s() calls method %s() which is a DIP violation\n", Type.getObjectType(classNode.name).getClassName(), method.name, intMethod.name);
									break;
								}
							}
						}
						if(methodClassNode.superName != null) {
							ClassReader superReader = new ClassReader(methodClassNode.superName);
							ClassNode interfaceNode = new ClassNode();
							superReader.accept(interfaceNode, ClassReader.EXPAND_FRAMES);
							List<MethodNode> superClassMethods = (List<MethodNode>) interfaceNode.methods;
							for(MethodNode intMethod: superClassMethods) {
								if(methodInsnNode.name.equals(intMethod.name) && !methodInsnNode.name.equals("<init>")) {
									out += String.format("Issue in %s: method %s() calls method %s() which is a DIP violation\n", Type.getObjectType(classNode.name).getClassName(), method.name, intMethod.name);
									break;
								}
							}
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				node = node.getNext();
			}
		}
		
		return out;
	}
	
	
	
}
