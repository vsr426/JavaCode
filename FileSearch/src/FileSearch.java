import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileSearch {
	
	private String path;
	private String regex;
	private String zipFileName;
	private Pattern pattern =null;
	private ArrayList<Path> zipList = new ArrayList<Path>();

	
	
	public static void main(String[] args) {
		FileSearch app = new FileSearch();
		switch (Math.min(args.length,  3)) {
		case 0: 
			System.out.println("USAGE: USAGE: FileSearch path [regex] [zipfile]");
			return;
		case 3: app.setZipFileName(args[2]);
		case 2: app.setRegex(args[1]);
		case 1: 
			app.setPath(args[0]);
			break;
		default: System.out.println("Invalid number of arguments");
		}

		try {
			app.walkDirectory(Paths.get(app.getPath()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	private void walkDirectory(Path path) throws IOException {
		Stream<Path> files=null;
		if (path !=null) {
			files = Files.walk(path, FileVisitOption.FOLLOW_LINKS);
		}
		Stream<Path> matchedFiles = files.filter(x -> x.toFile().isFile());
		matchedFiles.forEach(file -> {
			try {
				processFile(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		createZipFile();
	}


	private void createZipFile() throws FileNotFoundException, IOException {
		Path baseDir = Paths.get(this.getPath());
		String zipFilePath = Paths.get(this.getPath(), this.getZipFileName()).toString();
		System.out.println("Zipping started");
		System.out.println("zipFilename is ");
		try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFilePath)) ) {
			System.out.printf("zip base Directory %s\n", baseDir);
			for (Path file: zipList){
				String filename = getRelativePath(file, baseDir);
				System.out.printf("relative path is %s", filename);
				ZipEntry zipEntry = new ZipEntry(filename);
				zipEntry.setLastModifiedTime(Files.getLastModifiedTime(file));
				zos.putNextEntry(zipEntry);
				Files.copy(file, zos);
				zos.closeEntry();
			}

			
		}
		
	}


	private String getRelativePath(Path file, Path baseDir) {
		System.out.println("Relative File Name final " +file.toString());
		String fileName = file.toAbsolutePath().toString().substring(baseDir.toAbsolutePath().toString().length());
		System.out.println("Relative File Name relativize " +fileName);
		fileName = fileName.replace("\\", "/");
		System.out.println("Relative File Name repalce " +fileName);
		while(fileName.startsWith("/")) {
			fileName = fileName.substring(1);
		}
		System.out.println("Relative File Name final " +fileName);
		return fileName;
	}


	private void processFile(Path file) throws IOException {
		if ( searchFile(file) == true) {
			System.out.printf("Adding File %s to zip file \n", file.toAbsolutePath().toString());
			zipList.add(file);
		}else {
			 System.out.printf("File %s not matched to String \n", file.toAbsolutePath().toString());
		}
	}


	private boolean searchFile(Path file) throws IOException {
/*		System.out.println("SearchFile method Started");
		System.out.println(pattern.toString());
		System.out.println(regex);
		System.out.println(zipFileName);
		System.out.print(file.toFile().getName() + "\t");*/
		return (regex != null && file.toFile().getName().contains(".txt")) ?   Files.lines(file, StandardCharsets.UTF_8).anyMatch(line -> pattern.matcher(line).find()) : false;
		
	}



	public String getPath() {
		return path;
	}


	public void setPath(String path) {
		this.path = path;
	}


	public String getRegex() {
		return regex;
	}


	public void setRegex(String regex) {
		this.regex = regex;
		pattern = Pattern.compile(regex);
	}


	public String getZipFileName() {
		return zipFileName;
	}


	public void setZipFileName(String zipFileName) {
		this.zipFileName = zipFileName;
	}

}
