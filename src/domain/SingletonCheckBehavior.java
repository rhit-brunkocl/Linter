package domain;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

public class SingletonCheckBehavior implements CheckBehavior{

	public String check(ClassNode node) {
		ArrayList<MethodNode> methods = (ArrayList<MethodNode>) node.methods;
		boolean publicConstructors = false;
		for(MethodNode method: methods) {
			//check for public constructors
			if(method.name.equals("<init>")) {
				if((method.access & Opcodes.ACC_PUBLIC) != 0) {
					publicConstructors = true;
					return "";
				}
			}
		}
		boolean hasReturnValueOfClass = false;
		for(MethodNode method: methods) {
			//check if the return type is the class itself
			if(Type.getReturnType(method.desc).getClassName().equals(Type.getObjectType(node.name).getClassName())) {
				hasReturnValueOfClass = true;
				break;
			}
		}
		ArrayList<FieldNode> fields = (ArrayList<FieldNode>) node.fields;
		for(FieldNode field: fields) {
			if(Type.getObjectType(field.desc).getClassName().equals("L" + Type.getObjectType(node.name).getClassName()+ ";") ){
				if((field.access & Opcodes.ACC_PUBLIC) == 0){
					return String.format("Issue in %s: Bad pattern, Singleton\n", Type.getObjectType(node.name).getClassName());
				}
			}
		}
		return "";
	}

	@Override
	public String check(ClassReader reader) {
		return null;
	}
}
