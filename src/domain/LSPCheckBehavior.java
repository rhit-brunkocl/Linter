package domain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LSPCheckBehavior implements CheckBehavior {
	String out = "";
	private ArrayList<String> LSPViolations = new ArrayList<>();
	private ArrayList<ClassNode> superClasses = new ArrayList<>();
    public String check (ClassNode classNode) {
    	String superName = classNode.superName;
    	while(superName != null) {
    		if(superName.equals("java/lang/Object"))
    			break;
    		ClassReader reader = null;
			try {
				reader = new ClassReader(superName);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
             ClassNode superClassNode = new ClassNode();
             reader.accept(superClassNode, ClassReader.EXPAND_FRAMES);
             this.superClasses.add(superClassNode);
             superName = superClassNode.superName;
    	}
        for(MethodNode method : classNode.methods) {
        	String methodName = method.name;
        	for(ClassNode superNode: this.superClasses) {
        		MethodNode similarMethod = this.checkInheritMethods(method, superNode);
        		 if (similarMethod != null && !method.name.equals("<init>")) {
        			 Type subType = Type.getMethodType(method.desc);
        			 String subReturn = subType.getReturnType().getClassName();
        			 Type superType = Type.getMethodType(similarMethod.desc);
        			 String superReturn = superType.getReturnType().getClassName();
        			 if(!subReturn.equals(superReturn)) {
        				 out += String.format("Issue in %s: method %s() has a different return type with method %s from %s which is a LSP violation\n ", 
        						 Type.getObjectType(classNode.name).getClassName(),
									method.name,
									similarMethod.name,
									Type.getObjectType(superNode.name).getClassName());
        			 }
        			 Type subParams[] = subType.getArgumentTypes();
        			 Type superParams[] = superType.getArgumentTypes();
        			 String[] subArgs = new String[subParams.length];
        			 String[] superArgs = new String [superParams.length];
        			 for(int i = 0; i<subArgs.length; i++) {
        				 subArgs[0] = subParams[0].getClassName();
        			 }
        			 for(int i = 0; i<superArgs.length; i++) {
        				 superArgs[0] = superParams[0].getClassName();
        			 }
        			 if(!Arrays.equals(subArgs,superArgs)) {
        				 out += String.format("Issue in %s: method %s() has different argument types with method %s from %s which is a LSP violation\n ", 
        						 Type.getObjectType(classNode.name).getClassName(),
									method.name,
									similarMethod.name,
									Type.getObjectType(superNode.name).getClassName());
        			 }
        			 if(similarMethod.instructions.size() != 0 && method.instructions.size() == 0) {
        				 out+= String.format("Issue in %s: empty method %s() and non-empty parent method %s from %s is not which is a LSP violation\n ", 
        						 Type.getObjectType(classNode.name).getClassName(),
									method.name,
									similarMethod.name,
									Type.getObjectType(superNode.name).getClassName());
        			 }
                 }
        	}
        }
        return out;
    }

	@Override
	public String check(ClassReader reader) {
		return null;
	}

	public MethodNode checkInheritMethods(MethodNode toCheck, ClassNode superClass) {
    	for(MethodNode method : superClass.methods) {
    		if(method.name.equals(toCheck.name)) {
    			return method;
    		}
    	}
    	return null;
    }
    
    public void checkReturnType(MethodNode subMethod, MethodNode superMethod) {
    	Type subType = Type.getMethodType(subMethod.desc);
    	String subReturn = subType.getReturnType().getClassName();
    	Type superType = Type.getMethodType(superMethod.desc);
    	String superReturn = superType.getReturnType().getClassName();
    }
    
}
