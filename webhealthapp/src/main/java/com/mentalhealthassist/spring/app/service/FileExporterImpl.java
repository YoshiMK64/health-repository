package com.mentalhealthassist.spring.app.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Service;

//for backup
@Service
public class FileExporterImpl implements FileExporter {
	
	private static final String EXPORT_DIRECTORY ="C:/Users/mini_/Downloads";
	
	//for tracking
	private Logger myLogger = Logger.getLogger(getClass().getName());
	
	@Override
	public Path export(String fileContent, String fileName) {
		
		//initialise path using export directory and given file name
		Path filePath = Paths.get(EXPORT_DIRECTORY, fileName);
		
		try {
			//Path for backup files
			Path exportedFilePath = Files.write(filePath, fileContent.getBytes(), StandardOpenOption.CREATE);
			return exportedFilePath;
			
		}catch(IOException e) {
			
			//log exception
			myLogger.error(e.getMessage());
		}
		
		return null;
	}

}
