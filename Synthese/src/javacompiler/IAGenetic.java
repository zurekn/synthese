package javacompiler;

import java.awt.Image;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import data.Stats;

public class IAGenetic {
	static Class<?> c = null;
	static Object obj = null;
	static String className = "";
	
	public IAGenetic(Class<?> c_, Object obj_, String className_) {
		super();
		c = c_;
		obj = obj_;
		className = className_;
	}
	
	public static void InvokeInitPlayer(int x, int y, String id, String caracterClass) {
		try {
			Class<?>[] paramTypes = {int.class, int.class, String.class, String.class};
			Method method = c.getDeclaredMethod("init"+className, paramTypes);
			// Ajouter une insertion dans log du temps d'exécution
			method.invoke(obj, x, y, id,  caracterClass);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public static void InvokeRun() {
		try {
			Method method = c.getDeclaredMethod("run");
			// Ajouter une insertion dans log du temps d'exécution
			method.invoke(obj);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	public static void InvokeSetNumber(int val) {
		try {
			Class<?>[] paramTypes = {int.class};
			Method method = c.getDeclaredMethod("setNumber", paramTypes);
			// Ajouter une insertion dans log du temps d'exécution
			method.invoke(obj, val);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	public static void InvokeSetSizeCharacter(int size) {
		try {
			Class<?>[] paramTypes = {int.class};
			Method method = c.getDeclaredMethod("setSizeCharacter", paramTypes);
			// Ajouter une insertion dans log du temps d'exécution
			method.invoke(obj, size);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public static Integer InvokeGetNumber() {
		try {
			Method method = c.getDeclaredMethod("getNumber");
			// Ajouter une insertion dans log du temps d'exécution
			return (Integer) method.invoke(obj);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public Stats InvokeGetStats() {
		Stats stats = null;
		try {
			Method method = c.getDeclaredMethod("stats.getStats");
			// Ajouter une insertion dans log du temps d'exécution
			stats = (Stats) method.invoke(obj);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return stats;
	}
	
	public Image InvokeGetIcon() {
		Image img = null;
		try {
			Method method = c.getDeclaredMethod("getIcon");
			// Ajouter une insertion dans log du temps d'exécution
			img = (Image) method.invoke(obj);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return img;
	}
	
	public Class<?> getC() {
		return c;
	}

	public void setC(Class<?> c_) {
		c = c_;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj_) {
		obj = obj_;
	}
		
}
