package domain;

import org.objectweb.asm.tree.ClassNode;

public interface CheckBehavior {
	public String check(ClassNode node);
}
