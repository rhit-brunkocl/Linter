package domain;

import java.util.ArrayList;

import org.objectweb.asm.tree.ClassNode;

public class Linter {
	
	public String doCursoryStyleCheck(StyleBehavior behavior, ArrayList<ClassNode> classes) {
		String out = "";
		for(ClassNode node: classes) {
			out += behavior.check(node);
		}
		return out;
	}
	
	
	
	public String doAllTests(ArrayList<ClassNode> classes) {
		String out = "";
		out += doCursoryStyleCheck(new HashCheckBehavior(), classes);
		return out;
	}
}
