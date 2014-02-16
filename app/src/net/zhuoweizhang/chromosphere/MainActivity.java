package net.zhuoweizhang.chromosphere;

import android.app.Activity;
import android.os.Bundle;
import android.content.Context;

import com.ansca.corona.CoronaActivity;

import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.util.zip.*;

public class MainActivity extends CoronaActivity
{
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		try {
			buildRezFile(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.main);
	}

	public static void buildRezFile(Context context) throws IOException {
		//here's what we're going to do:
		//we're going to package all the lua files (renaming them to .lu along the way)
		//their lua engine, oddly enough, will load non-compiled lua files.
		//make sure to drop our own config.lu instead of the non-compiled one.
		//then, we write this to /sdcard/winprogress/chromosphere.zip
		//and make the loader load this.
		File inputDir = new File("/sdcard/winprogress/corona/");
		File[] files = inputDir.listFiles();
		List<CarEntry> carList = new ArrayList<CarEntry>();
		File outputFile = new File("/sdcard/winprogress/chromosphere.zip");
		FileOutputStream zipfos = new FileOutputStream(outputFile);
		ZipOutputStream zipos = new ZipOutputStream(zipfos);
		zip(inputDir, inputDir, zipos, carList);

		//now build the resource.car
		CarEntry configEntry = new CarEntry("config.lu");
		InputStream configIs = context.getAssets().open("custom_config.lu");
		ByteArrayOutputStream configBytes = new ByteArrayOutputStream();
		copy(configIs, configBytes);
		configIs.close();
		configBytes.close();
		configEntry.contents = configBytes.toByteArray();
		carList.add(configEntry);

		ZipEntry resourceCarEntry = new ZipEntry("resource.car");
		zipos.putNextEntry(resourceCarEntry);
		CarWriter.write(zipos, carList);
		zipos.closeEntry();

		zipos.close();
	}


	/*
	 * The following is based on code from PocketTool by Josh Huelsman.
	 */

	public static void zip(File orig, File folder, ZipOutputStream zipFolder, List<CarEntry> carlist) throws IOException{
		File[] files = folder.listFiles();
		int i;
		for(i = 0; i < files.length; i++){
			File file = files[i];
			if(file.isDirectory()){
				zip(orig, file, zipFolder, carlist);
				continue;
			}
			String filename = file.getName();
			String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
			if (extension.equals("lua") || extension.equals("lu")) {
				if (filename.equals("config.lua")) continue;
				//read in the files
				byte[] mybytes = new byte[(int) file.length()];
				FileInputStream fis = new FileInputStream(file);
				fis.read(mybytes);
				fis.close();
				String newName = filename.substring(0, filename.lastIndexOf(".")) + ".lu";
				CarEntry entry = new CarEntry(newName);
				entry.contents = mybytes;
				carlist.add(entry);
				continue;
			}
			String name = file.getAbsolutePath().substring(orig.getAbsolutePath().toString().length() +1);
			if(name.equals("") != true){
				ZipEntry entry = new ZipEntry(name);
				zipFolder.putNextEntry(entry);
				if(file.isDirectory() != true){
					copy(new FileInputStream(file), zipFolder);
				}
			}
			zipFolder.closeEntry();
			
		}
	}

	public static void copy(InputStream in, OutputStream out)
			throws IOException {
		if (in == null) {

		}
		if (out == null) {

		}

		// Transfer bytes from in to out
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		//in.close();
		//out.close();
	}

}
