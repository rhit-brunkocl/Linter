package domain;

import java.util.ArrayList;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

public class Linter {
	
	public String doOneClassCheck(CheckBehavior behavior, ArrayList<String> routes, ArrayList<ClassReader> readers, ArrayList<ClassNode> classes) {
		String out = "";
		for(int i = 0; i < classes.size(); i++){
			out += behavior.check(routes.get(i), readers.get(i), classes.get(i));
		}
//		for(ClassNode node: classes) {
//			out += behavior.check(node);
//		}
		return out;
	}
	
	public String doAllTests(ArrayList<String> routes, ArrayList<ClassReader> readers, ArrayList<ClassNode> classes, ArrayList<CheckBehavior> checks) {
		String out = "";
		for(CheckBehavior c: checks) {
			out += doOneClassCheck(c, routes, readers, classes);
		}
		if(out.equals("")) {
			return "0 issues found.\n";
		}
		return out;
	}
}
