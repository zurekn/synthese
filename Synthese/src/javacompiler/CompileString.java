package javacompiler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Random;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

public class CompileString {
	static Boolean debug = true;
	static String className = "j1";
	static String pathClass = "src/javacompiler/";
	static String destPathClass = "bin/javacompiler/";
	static boolean aRisque = false;

	public static void main(String[] args) throws Exception {
		// System.setProperty("java.home", "C:\\MCP-IDE\\jdk1.8.0_60\\jre");
		aRisque = false;
		Node root = DecodeScript("src/testScriptTree.txt");
		ArrayList<String> contentCode = new ArrayList<String>();
		contentCode = root.TreeToArrayList(contentCode);
		for (String st : contentCode)
			System.out.println(st);

		className += (aRisque ? "_Arisque" : "");
		ReadWriteCode(contentCode, className);
		// CompileAndExecuteClass(className);

		// Serialization d'un objet
		serializeObject(className, root);

		/*
		 * // Dé-Serialization d'un objet 
		 * deserializeObject(className);
		 */

	}

	/**
	 * 
	 * @param name
	 *            of the object, object (node) to store
	 * @return object to be output
	 * @throws IOException
	 */
	public static void serializeObject(String name, Node root)
			throws IOException {
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(
				new FileOutputStream("javaObjects_" + name + ".json"));
		objectOutputStream.writeObject(root);
		objectOutputStream.flush();
		objectOutputStream.close();
	}

	public static void deserializeObject(String name) throws IOException,
			ClassNotFoundException {
		ObjectInputStream objectInputStream = new ObjectInputStream(
				new FileInputStream("javaObjects_" + name + ".json"));
		Node readJSON = (Node) objectInputStream.readObject();
		objectInputStream.close();

		readJSON.displayTree();
	}

	/*
	 * Décodage du script du joueur IA
	 */
	public static Node DecodeScript(String scriptPath) {
		File fichier = new File(scriptPath);
		Node root = new Node("run()");
		ArrayList<String> cond = new ArrayList<String>();
		ArrayList<String> var = new ArrayList<String>();
		ArrayList<String> comp = new ArrayList<String>();
		ArrayList<String> code = new ArrayList<String>();
		boolean condBool = false;
		boolean compBool = false;
		boolean varBool = false;
		boolean codeBool = false;

		// ====== Lecture du script ==========
		try {
			InputStream ips = new FileInputStream(fichier);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String ligne;
			while ((ligne = br.readLine()) != null) {

				if (condBool) {
					if (ligne.contains("}"))
						condBool = false;
					else
						cond.add(ligne);
				}

				if (varBool) {
					if (ligne.contains("}"))
						varBool = false;
					else
						var.add(ligne);
				}

				if (compBool) {
					if (ligne.contains("}"))
						compBool = false;
					else
						comp.add(ligne);
				}

				if (codeBool) {
					if (ligne.contains("}"))
						codeBool = false;
					else
						code.add(ligne);
				}

				if (ligne.contains("cond"))
					condBool = true;

				if (ligne.contains("comp"))
					compBool = true;

				if (ligne.contains("var"))
					varBool = true;

				if (ligne.contains("code"))
					codeBool = true;
				// script.add(ligne);
			}
			// ======= Construction de l'arbre ==========
			int rand = 0;
			// *** récupérer condition
			String[] partsRandomCond = getParam(cond, -1);
			
			// initialisation du noeud + code final
			Node nodeCond = new Node("");
			String conditionFull = "";
			
			// test du cas if / while
			if (partsRandomCond[0].contains("if")
					|| partsRandomCond[0].contains("while")) {
				// Risqué si while
				if (partsRandomCond[0].contains("while")) {
					nodeCond.setValue("while");
					aRisque = true;
				} else
					nodeCond.setValue("if");
				
				// *** récupérer variable aléatoire
				String[] partsRandomVar = getParam(var, -1);
				
				// *** traitement de variable
				conditionFull += partsRandomVar[0]; // concaténation du nom de
													// la variable
				if (partsRandomVar[1].contains("int")) {
					String[] partsRandomComp = getParam(comp, 0);
					conditionFull += getCompInt(partsRandomComp, partsRandomVar);
				}

				if (partsRandomVar[1].contains("boolean")) {
					String[] partsRandomComp = getParam(comp, 1);
					debugSys("Fin if/while");
					conditionFull += getCodeBool(partsRandomComp);
				}
			}
			// Cas de la condition for
			if (partsRandomCond[0].contains("for")) 
			{
				// *** ajout de la condition for
				nodeCond.setValue("for");
				conditionFull += "int i = 0 ; i"; // concaténation de l'initialisation
				String[] partsRandomComp = getParam(comp, 0);
				rand = new Random().nextInt(partsRandomComp.length);
				rand = (rand <= 2) ? 2 : 3; // voir 1 , 3
				int condRandom = rand;
				conditionFull += partsRandomComp[rand]; // concaténation du
														// comparateur
				String[] partsRandomVar = null;
				do {
					//  recuperation d'une ligne "variable" aleatoire
					partsRandomVar = getParam(var, -1);
					// tant qu'on a pas de int...
				} while (!partsRandomVar[1].contains("int"));

				// i-- si >= , i++ si <=
				if (condRandom == 2)
					conditionFull += partsRandomVar[0] + "; i--";
				else
					conditionFull += partsRandomVar[0] + "; i++";
			}
			
			// *** ajout du noeud de condition
			root.addChild(nodeCond);
			// ajout du neud de comparaison de condition
			Node condFullNode = new Node(conditionFull);
			nodeCond.addChild(condFullNode); // Ajout du noeud de condition de
												// la boucle
			if (debug)
				System.out.println("conditionfull : " + conditionFull);

			// *** ========== Ajout des lignes de code dans les boucles =========
			nodeCond = addCodeLineAlea(code,10,nodeCond);

			if (partsRandomCond[0].contains("if")
					&& partsRandomCond[2].contains("else")) {
				Node nodeElse = new Node("else");
				root.addChild(nodeElse);
				// *** Ajout des lignes de code
				nodeElse = addCodeLineAlea(code,10,nodeElse);
			}
			// *** Ajout des lignes de code à la racinde du run()
			root = addCodeLineAlea(code,10,root);
			
			// fermeture du fichier
			br.close();
		} catch (Exception e) {
			if (debug)
				System.out.println(e.toString());
		}

		return root;
	}
	

	/** Récupération d'un parametre
	 * 
	 * @param cond
	 *            : toutes les possibilités pour chaque param
	 * @param pos
	 *            : la position du paramètre que l'on veut. si négatif, on en
	 *            prend un aléatoire.
	 * @return un param aléatoire
	 */
	public static String[] getParam(ArrayList<String> param, int pos) {
		int rand = 0;
		rand = new Random().nextInt(param.size()); // random sur les positions
		String random;
		if (pos < 0) {
			random = (param.get(rand)); // recuperation de la condition a la
										// position rand
		} else {
			random = (param.get(pos));
		}
		if (debug)
			System.out.println("random : " + random);
		
		random = random.replace("\"", "");
		random = random.replace("[", "");
		random = random.replace(" ", "");
		random = random.replace("]", "");
		String[] partsRandom = random.split(","); // implode des params de la
													// condition dans parts
		return partsRandom;
	}
	
	

	/**
	 * Construit un bout de code pour comparer 2 chiffres
	 * 
	 * @param pos
	 * @param param
	 * @return bout de code (comparateur) à concaténer
	 */
	
	public static String getCompInt(String[] partsRandomComp,
			String[] partsRandomVar) {
		int rand = 0;
		rand = new Random().nextInt(partsRandomComp.length);
		rand = (rand == 0) ? 1 : rand;
		if (debug)
			System.out.println("getComptInt random : " + rand);
		// concaténation du comparateur
		return (partsRandomComp[rand] + " " + (new Random().nextInt(Integer
				.parseInt(partsRandomVar[3])) + Integer
				.parseInt(partsRandomVar[2])));
	}

	/**
	 * Construit un bout de code pour comparer 2 booléens
	 * 
	 * @param pos
	 * @param param
	 * @return bout de code (comparateur) à concaténer
	 */
	public static String getCodeBool(String[] partsRandomComp) {
		// conditionFull += partsRandomComp[1] + " " + ((new Random().nextInt(1)
		// == 1)? "true" : "false");
		if (debug)
			System.out.println("getCodeBool");
		return partsRandomComp[1] + " "
				+ ((new Random().nextInt(1) == 1) ? "true" : "false");

	}

	public static Node addCodeLineAlea(ArrayList<String> code,int nbMaxLine,Node resNode){
		int nbLnCode = new Random().nextInt(nbMaxLine) + 1;
		int rand=0;
		// Choix aléatoire de la ligne de code
		for (int i = 0; i < nbLnCode; i++) {
			rand = new Random().nextInt(code.size());
			resNode.addChild(new Node(code.get(rand)));
		}
		return resNode;
	}
	
	
	
	
	/**
	 * 
	 * @param className
	 */
	public static void debugSys(String message)
	{
		if(debug)
			System.out.println(message);
	}
	/*
	 * Compilation, déplacement, et instanciation d'une classe à partir d'un
	 * String
	 */
	public static void CompileAndExecuteClass(String className) {
		// Compilation de la classe du joueur IA
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		int result = compiler.run(null, null, null, pathClass + className
				+ ".java");
		System.out.println("Compile result code = " + result);

		// Déplacement du fichier .CLASS du répertoire /src au /bin
		File afile = new File(pathClass + className + ".CLASS");
		File destFile = new File(destPathClass + afile.getName());
		if (destFile.exists())
			destFile.delete();
		if (afile.renameTo(new File(destPathClass + afile.getName()))) {
			System.out.println("File is moved successful!");
		} else {
			System.out.println("File is failed to move!");
		}

		// Instanciation de la classe du joueur IA
		Class<?> noparams[] = {};// pas de paramètre
		Class<?> paramInt[] = new Class[1];// paramètre int
		paramInt[0] = Integer.TYPE;
		Class<?> c;
		try {
			c = Class.forName("javacompiler." + className);
			Object obj = c.newInstance();
			Method method = c.getDeclaredMethod("run", noparams);
			// Ajouter une insertion dans log du temps d'exécution
			method.invoke(obj, null);

			method = c.getDeclaredMethod("setLife", paramInt);
			method.invoke(obj, 1);
			method = c.getDeclaredMethod("run", noparams);
			method.invoke(obj, null);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
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

	/*
	 * Lecture dans un fichier .java
	 */
	public static void ReadWriteCode(ArrayList<String> codeContent,
			String className) {
		Boolean inRun = false;
		Boolean isAdded = false;
		File fichier = new File(pathClass + "maClasseTest.java");
		ArrayList<String> content = new ArrayList<String>();

		// lecture du fichier java
		try {
			InputStream ips = new FileInputStream(fichier);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String ligne;
			while ((ligne = br.readLine()) != null) {
				if (ligne.contains("public class maClasseTest"))
					ligne = ligne.replace("maClasseTest", className);
				content.add(ligne);
				if (inRun && !isAdded && ligne.contains("{")) {
					// Ajout des lignes de code désirées dans la ArrayList
					for (String ln : codeContent)
						content.add("\t\t" + ln);
					isAdded = true;
				} else {
					if (ligne.contains("run()"))
						inRun = true;
				}
			}
			br.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		WriteCode(content, className);
	}

	/*
	 * Ecriture dans un fichier .java
	 */
	public static void WriteCode(ArrayList<String> content, String className) {
		// création du fichier qui va écraser l'ancien fichier java
		try {
			FileWriter fw = new FileWriter(new File(pathClass + className
					+ ".java"));
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter fichierSortie = new PrintWriter(bw);
			for (String ln : content) {
				fichierSortie.println(ln);
			}
			fichierSortie.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

}
