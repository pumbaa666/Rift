package ch.correvon.rift.riftItemIcon;

import java.io.File;

public class MainRiftItemIcon
{
	public static void main(String[] args)
	{
		if(args.length != 1)
		{
			System.out.println(USAGE);
			System.exit(-1);
		}
		
		File root = new File(args[0]);
		if(!root.isDirectory())
		{
			System.out.println("'" + root + "' n'est pas un répertoire ou n'est pas lisible");
			System.exit(-1);
		}
		
		File[] files = root.listFiles();
		int nbFiles = files.length;
		File file;
		String fileName;
		String fileNameLower;
		int nbRenamedFile = 0;
		
		System.out.println("Running...");

		for(int i = 0; i < nbFiles; i++)
		{
			file = files[i];
			if(!file.isFile())
				continue;
			
			fileName = file.getName();
			fileNameLower = fileName.toLowerCase();
			if(!fileNameLower.equals(fileName))
			{
				rename(file, fileNameLower);
				nbRenamedFile++;
			}
		}
		
		System.out.println("\nFinish. " + nbRenamedFile + " files renamed");
	}
	
	private static void rename(File source, String newName)
	{
		String path = source.getParent();
		String newFileName = path + "\\" + newName;
		File newFile = new File(newFileName);
		source.renameTo(newFile);
		System.out.println("Rename '" + source.getAbsolutePath() + "' to '" + newFile.getAbsolutePath() + "'");
	}
	
	private static final String USAGE = "usage : java -jar riftItemIcon0.1.jar DIR\n" +
											"\tDIR is the directory containing picture to be renamed to lowercase";
}
