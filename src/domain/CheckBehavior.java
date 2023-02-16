package domain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import java.io.FileInputStream;

public interface CheckBehavior {
	public String check(ClassNode node);

	public String check(ClassReader reader);
}
