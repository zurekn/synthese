package javacompiler;

public class CompilerHandler {
	Class<?> c = null;
	Object obj = null;
	
	public CompilerHandler(Class<?> c, Object obj) {
		super();
		this.c = c;
		this.obj = obj;
	}

	public Class<?> getC() {
		return c;
	}

	public void setC(Class<?> c) {
		this.c = c;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}
	
	
}
