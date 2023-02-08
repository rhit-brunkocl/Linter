package domain;

import org.objectweb.asm.tree.ClassNode;

public interface StyleBehavior {
	public String check(ClassNode node);
}
