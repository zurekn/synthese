package javacompiler;

import game.Character;

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
import java.util.ArrayList;
import java.util.Random;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

public class CompileString {
	static Boolean debug = false;
	static String className = "";
	static String pathClass = "Synthese/src/game/";
	static String destPathClass = "target/classes/game/";
	static String classTestName = "IAScript";
	static String packageName = "game";
	static boolean aRisque = false;
	static Class<?> c = null;
	static int nbLignesCode = 2;
	private static ArrayList<String> code;
	private static ArrayList<String> comp;
	private static ArrayList<String> var;
	private static ArrayList<String> cond;
	
	public static void generate(String geneticName)
	{
		System.setProperty("java.home", "C:\\MCP-IDE\\jdk1.8.0_60\\jre");
		aRisque = false;
		Node root = DecodeScript("AIScriptDatas.txt");
		ArrayList<String> contentCode = new ArrayList<String>();
		contentCode = root.TreeToArrayList(contentCode);
		for (String st : contentCode)
			System.out.println(st);

		className = geneticName + (aRisque ? "_Arisque" : "");
		ReadWriteCode(contentCode, className);
		// CompileAndExecuteClass(className, "run");
	}

	/*
	 * S�rialization d'un objet
	 */
	public static void serializeObject(String name, Node root)
			throws IOException {
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(
				new FileOutputStream("javaObjects_" + name + ".json"));
		objectOutputStream.writeObject(root);
		objectOutputStream.flush();
		objectOutputStream.close();
	}
	
	/*
	 * D�-s�rialize un objet
	 */
	public static void deserializeObject(String name) throws IOException,
			ClassNotFoundException {
		ObjectInputStream objectInputStream = new ObjectInputStream(
				new FileInputStream("javaObjects_" + name + ".json"));
		Node readJSON = (Node) objectInputStream.readObject();
		objectInputStream.close();

		readJSON.displayTree();
	}
	
/** Creation of genetic AI tree from a text file.
 * 	Creation stages : 
 * - Decode text file and store datas in variables
 * - Choose randomly one condition branch and build its condition
 * - Choose randomly NbCodeLine code lines in the condition branch
 * - Choose randomly NbCodeLine code lines outside the condition branch 
 * @param scriptPath : the text file containing all needed datas
 * @return Node : the resulting script tree
 */
	public static Node DecodeScript(String scriptPath) {
		File fichier = new File(scriptPath);
		Node root = new Node("run(Character ch)");
		cond = new ArrayList<String>();
		var = new ArrayList<String>();
		comp = new ArrayList<String>();
		code = new ArrayList<String>();
		boolean condBool = false;
		boolean compBool = false;
		boolean varBool = false;
		boolean codeBool = false;
		// ====== Lecture du fichier texte ==========
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
			debugSys("DecodeScript : var="+var);
			debugSys("DecodeScript : code="+code);
			debugSys("DecodeScript : comp="+comp);
			debugSys("DecodeScript : cond="+cond);
			// ======= Construction de l'arbre ==========
			root = addFullCondition(root); // Ajout de conditions et code � l'int�rieur de ces conditions
			root = addCodeLineAlea(code,nbLignesCode,root);// Ajout des lignes de code � la racinde du run()
			br.close();// fermeture du fichier txt
		} catch (Exception e) {
				System.out.println("Decode : "+e.toString());
		}
		return root;
	}
	
	/** Add a full condition branch node with code lines inside.
	 * 
	 *  Should be use like : node = addFullCondition(node)
	 *  
	 * @param resNode : node where we append condition
	 * @return : resulting node with conditions appended
	 */
	public static Node addFullCondition(Node resNode)
	{
					int rand = 0;
					String[] partsRandomCond = getParam(cond, -1); // condition al�atoire
					Node nodeCond = new Node(""); // init. supernoeud de condition
					String conditionFull = ""; // init. valeur textuelle de la condition
					if (partsRandomCond[0].contains("if") // test du cas if / while
							|| partsRandomCond[0].contains("while")) {
						if (partsRandomCond[0].contains("while")) { // Risqu� si while
							nodeCond.setValue("while");
							aRisque = true;
						} else
							nodeCond.setValue("if");
						String[] partsRandomVar = getParam(var, -1); // ligne variable al�atoire
						conditionFull += partsRandomVar[0]; // concat�ner nom de la variable
						switch(partsRandomVar[1]){
							case "int" :
								String[] partsRandomComp = getParam(comp, 0);
								conditionFull += getCompInt(partsRandomComp, partsRandomVar); // concat�ner la partie droite de la comparaison
							break;
							case "boolean" :
								String[] partsRandomCompBool = getParam(comp, 1);
								conditionFull += getCodeBool(partsRandomCompBool);
							break;
						}
					}
					if (partsRandomCond[0].contains("for"))  // Cas d'un for
					{
						nodeCond.setValue("for"); // Ajoute "for" au niveau juste en dessous du noeud resNode
						conditionFull += "int i = 0 ; i"; // concat�ne l'initialisation
						String[] partsRandomComp = getParam(comp, 0); // r�cup�re les comparateurs pour les int
						rand = new Random().nextInt(partsRandomComp.length); // choisis un comparateur int al�atoire
						rand = (rand <= 2) ? 2 : 3; // soit "<=" soit ">=" autoris�s
						int condRandom = rand;
						conditionFull += partsRandomComp[rand]; // concat�nation du comparateur
						String[] partsRandomVar = null;
						do {
							partsRandomVar = getParam(var, -1); //  recuperation d'une ligne "variable" int aleatoire
						} while (!partsRandomVar[1].contains("int"));
						// ajout de l'it�ration. i-- si ">=" , i++ si "<="
						if (condRandom == 2 || condRandom == 6 )
							conditionFull += partsRandomVar[0] + "; i--";
						else
							conditionFull += partsRandomVar[0] + "; i++";
					}
					resNode.addChild(nodeCond); // lier les supernoeuds
					Node condFullNode = new Node(conditionFull); // cr�ation du sous-noeud
					nodeCond.addChild(condFullNode); // Ajout du sous-noeud au supernoeud de condition
					debugSys("conditionfull : " + conditionFull);
					nodeCond = addCodeLineAlea(code,nbLignesCode,nodeCond); // *** Ajout des lignes de code dans la branche
					if (partsRandomCond[0].contains("if")  // Cas du if avec un else
							&& partsRandomCond[2].contains("else")) {
						Node nodeElse = new Node("else");
						resNode.addChild(nodeElse);
						nodeElse = addCodeLineAlea(code,nbLignesCode,nodeElse); // *** Ajout des lignes de code dans le ELSE
					}
		return resNode;
	}
	

	/** R�cup�ration d'un param�tre
	 * 
	 * @param cond
	 *            : toutes les possibilit�s pour chaque param
	 * @param pos
	 *            : la position du param�tre que l'on veut. si n�gatif, on en
	 *            prend un al�atoire.
	 * @return un param al�atoire
	 */
	public static String[] getParam(ArrayList<String> param, int pos) {
		int randPosition = 0;
		String dataLine;
		if (pos < 0) {
			randPosition = new Random().nextInt(param.size()); // random sur les positions
			dataLine = (param.get(randPosition)); // recuperation de la condition a la
										// position rand
		} else {
			dataLine = (param.get(pos));
		}
		if (debug)
			System.out.println("random : pos= "+randPosition+" string= " + dataLine);
		
		dataLine = dataLine.replace("\"", "");
		dataLine = dataLine.replace("[", "");
		dataLine = dataLine.replace(" ", "");
		dataLine = dataLine.replace("]", "");
		String[] partsRandom = dataLine.split(","); // implode des params de la
													// condition dans parts
		return partsRandom;
	}
	
	/**
	 * Construit un bout de code pour comparer 2 chiffres.
	 * Ne construit pas la partie gauche de la comparaison
	 * 
	 * @param pos
	 * @param param
	 * @return bout de code (comparateur) � concat�ner
	 */
	
	public static String getCompInt(String[] partsRandomComp,
			String[] partsRandomVar) {
		// Choix du comparateur (==, <=, >=, >, <, != )
		int rand = 0;
		rand = new Random().nextInt(partsRandomComp.length);
		rand = (rand == 0) ? 1 : rand;
		
		if (debug)
			System.out.println("getComptInt random : " + rand);
		
		// calcul de la variable comparante (� droite du comparateur)
		//String test = "("+Integer.parseInt(partsRandomVar[2])+"+new Random().nextInt("+partsRandomVar[3]+"))";
		String[] testVar = getParam(var,-1);
		String rightVar = testVar[0]; 
		debugSys("getComptInt : rightVar = "+rightVar);
		// concat�nation du comparateur et de la valeur comparante
		return (partsRandomComp[rand] + " " + rightVar);
	}

	/**
	 * Construit un bout de code pour comparer 2 bool�ens.
	 * Ne construit pas le bool�en de gauche.
	 * 
	 * @param pos
	 * @param param
	 * @return bout de code (comparateur) � concat�ner
	 */
	public static String getCodeBool(String[] partsRandomComp) {
		return partsRandomComp[1] + " "
				+ ((new Random().nextInt(1) == 1) ? "true" : "false");

	}

	/**  Ajoute un certain nombre de lignes de code . 
	 * Les lignes sont contenues dans la section "code" de la base
	 * Les lignes sont ajout�es au param�tre Node resNode
	 * 	
	 * @param code
	 * @param nbMaxLine
	 * @param resNode
	 * @return Node resNode
	 */
	public static Node addCodeLineAlea(ArrayList<String> code,int nbMaxLine,Node resNode){
		int nbLnCode = new Random().nextInt(nbMaxLine) + 1;
		int rand=0;
		// Choix al�atoire de la ligne de code
		for (int i = 0; i < nbLnCode; i++) {
			rand = new Random().nextInt(code.size());
			resNode.addChild(new Node(code.get(rand)));
		}
		return resNode;
	}
		
	/** print message only when debugging
	 * 
	 * @param message : message to print
	 */
	public static void debugSys(String message)
	{
		if(debug)
			System.out.println(message);
	}

	/*
	 * Compilation, d�placement, et instanciation d'une classe � partir d'un
	 * String
	 */
	public static IAGenetic CompileAndInstanciateClass(String className) {

		System.setProperty("java.home", "C:\\MCP-IDE\\jdk1.8.0_60\\jre");
		
		// Compilation de la classe du joueur IA
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		System.out.println("IAGenetic : "+pathClass + className + ".java");
		@SuppressWarnings("unused")
		int result = compiler.run(null, null, null, pathClass + className + ".java");
		//System.out.println("Compile result code = " + result);

		// D�placement du fichier .CLASS du r�pertoire /src au /bin
		File afile = new File(pathClass + className + ".CLASS");
		File destFile = new File(destPathClass + afile.getName());
		if (destFile.exists())
			destFile.delete();
		afile.renameTo(new File(destPathClass + afile.getName()));

		// Instanciation de la classe du joueur IA
		Class<?> c = null;
		Object obj = null;
		try {
			c = Class.forName(packageName + "." + className);
			obj = c.newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return new IAGenetic(c,obj, className);
	}

	/** Read skelet class (classTestName), construct then write text content in "className" class by calling WriteCode
	 * 
	 * @param codeContent : Generated code to push in "run" method
	 * @param className : should correspond to the monster id which will run this script.
	 */
	public static void ReadWriteCode(ArrayList<String> codeContent,String className) {
		Boolean inRun = false;
		Boolean isAdded = false;
		File fichier = new File(pathClass + classTestName +".java");
		ArrayList<String> content = new ArrayList<String>();
		// lecture du fichier java
		try {
			InputStream ips = new FileInputStream(fichier);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String ligne;
			while ((ligne = br.readLine()) != null) {
				if (ligne.contains(classTestName))
					ligne = ligne.replace(classTestName, className);
				content.add(ligne);
				if (inRun && !isAdded && ligne.contains("{")) {
					// Ajout des lignes de code d�sir�es dans la ArrayList
					for (String ln : codeContent)
						content.add("\t\t" + ln);
					isAdded = true;
				} else {
					if (ligne.contains("run("))
						inRun = true;
				}
			}
			br.close();
		} catch (Exception e) {
			System.out.println("ReadWriteCode"+e.toString());
		}

		WriteCode(content, className);
	}

	/**
	 * Write code content in "className" class
	  * @param content : all code to write in file.
	  * @param className : should correspond to the monster id which will run this script.
	  */
	public static void WriteCode(ArrayList<String> content, String className) {
		// cr�ation du fichier qui va �craser l'ancien fichier java
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
			System.out.println("WriteCode : "+e.toString());
		}
	}

}
