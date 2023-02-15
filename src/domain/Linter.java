package domain;

import java.util.ArrayList;

import org.objectweb.asm.tree.ClassNode;

public class Linter {
	
	public String doOneClassCheck(CheckBehavior behavior, ArrayList<ClassNode> classes) {
		String out = "";
		for(ClassNode node: classes) {
			out += behavior.check(node);
		}
		return out;
	}
	
	public String doAllTests(ArrayList<ClassNode> classes, ArrayList<CheckBehavior> checks) {
		String out = "";
		for(CheckBehavior c: checks) {
			out += doOneClassCheck(c, classes);
		}
		if(out.equals("")) {
			return "0 issues found.\n";
		}
		return out;
	}
}
