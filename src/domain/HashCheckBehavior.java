package domain;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class HashCheckBehavior implements StyleBehavior{
	public String check(ClassNode node) {
		ArrayList<MethodNode> methods = (ArrayList<MethodNode>) node.methods;
		boolean hashMethod = false;
		boolean equalsMethod = false;
		for(MethodNode method: methods) {
			if(method.name.equals("equals")) {
				equalsMethod = true;
				if(hashMethod) {
					break;
				}
			}else if(method.name.equals("hashCode")) {
				hashMethod = true;
				if(equalsMethod) {
					break;
				}
			}
		}
		if(hashMethod && !equalsMethod) {
			return String.format("Issue in %s: hashCode() method with no equals() method.\n", node.name);
		}else if(!hashMethod && equalsMethod) {
			return String.format("Issue in %s: equals() method with no hashCode() method.\n", node.name);
		}
		return "";
	}
}
