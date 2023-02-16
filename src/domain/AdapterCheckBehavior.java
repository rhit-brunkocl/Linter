package domain;

import java.io.IOException;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class AdapterCheckBehavior implements CheckBehavior {
	private static boolean hasTarget = false;
	private static boolean hasAdaptee = false;
	private static boolean delegatesToAdapteeMethods = false;
	public String check(ClassNode node){
		hasTarget = false;
		hasAdaptee = false;
		delegatesToAdapteeMethods = false;
		try {
			if (isAdapter(node)) {
				System.out.println("This class uses Adapter pattern!");
			} else {
				System.out.println("This class doesn't use Adapter pattern!");
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Cannot perform Adapter check on class: " + Type.getObjectType(node.name).getClassName());
		}
		return null;
	}
	
	public static boolean isAdapter(ClassNode node) throws IOException {
		
        if(node.interfaces.size() == 1) {
        	hasTarget = true;
        }
        for(MethodNode method:node.methods) {
        	method.instructions.accept(new MethodVisitor(Opcodes.ASM7) {
              @Override
              public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
                  if (opcode == Opcodes.GETFIELD || opcode == Opcodes.PUTFIELD) {
                	  hasAdaptee = true;
                  }
              }

              @Override
              public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
                  if (opcode == Opcodes.INVOKEVIRTUAL) {
                      delegatesToAdapteeMethods = true;
                  }
              }
          });
        }
        return hasTarget && hasAdaptee && delegatesToAdapteeMethods;
    }
}
