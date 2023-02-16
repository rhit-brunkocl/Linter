package domain;

import java.io.FileInputStream;
import java.io.IOException;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class AdapterCheckBehavior implements CheckBehavior {
	private boolean hasTarget = false;
	private boolean hasAdaptee = false;
	private boolean delegatesToAdapteeMethods = false;
	String out = "";
	public String check(ClassNode node){
		hasTarget = false;
		hasAdaptee = false;
		delegatesToAdapteeMethods = false;
		try {
			if (isAdapter(node)) {
				out = String.format("%s uses Adapter pattern!", Type.getObjectType(node.name).getClassName());
			}
		} catch (IOException e) {
			e.printStackTrace();
			out = String.format("Cannot perform Adapter check on class: %s", Type.getObjectType(node.name).getClassName()) ;
		}
		return out;
	}

	@Override
	public String check(ClassReader reader) {
		return null;
	}

	public boolean isAdapter(ClassNode node) throws IOException {

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
