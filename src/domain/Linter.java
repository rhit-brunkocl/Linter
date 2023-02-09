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
	
	public String doAllTests(ArrayList<ClassNode> classes) {
		String out = "";
		out += doOneClassCheck(new HashCheckBehavior(), classes);
		return out;
	}
}
