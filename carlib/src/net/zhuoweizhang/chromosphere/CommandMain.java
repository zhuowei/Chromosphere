package net.zhuoweizhang.chromosphere;

import java.io.*;
import java.util.*;

public class CommandMain {

	public static void main(String[] args) throws Exception {
		if (args[0].equals("d")) {
			decomp(args[1], args[2]);
		} else if (args[0].equals("b")) {
			build(args);
		} else {
			System.err.println("use: ./run.sh d infile.car outdir");
			System.err.println("or:  ./run.sh b outfile.car infile1 infile2...");
		}
	}

	public static void decomp(String infile, String outdir) throws IOException {
		CarFile carFile = new CarFile(new File(infile));
		for (CarEntry entry: carFile.entries()) {
			byte[] dat = carFile.getBytes(entry);
			File outputFile = new File(outdir, entry.name);
			FileOutputStream out = new FileOutputStream(outputFile);
			out.write(dat);
			out.close();
			System.out.print(outdir + "/" + entry.name + " ");
		}
		System.out.println();
	}

	public static void build(String[] args) throws IOException {
		File outfile = new File(args[1]);
		List<CarEntry> mylist = new ArrayList<CarEntry>();
		for (int i = 2; i < args.length; i++) {
			File infile = new File(args[i]);
			byte[] bytes = new byte[(int) infile.length()];
			FileInputStream fis = new FileInputStream(infile);
			fis.read(bytes);
			fis.close();
			CarEntry entry = new CarEntry(infile.getName());
			entry.contents = bytes;
			mylist.add(entry);
		}
		FileOutputStream fos = new FileOutputStream(outfile);
		CarWriter.write(fos, mylist);
		fos.close();
	}
}
