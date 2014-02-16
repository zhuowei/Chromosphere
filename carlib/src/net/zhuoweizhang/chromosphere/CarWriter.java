package net.zhuoweizhang.chromosphere;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

public class CarWriter {

	public static void write(OutputStream outputStream, List<CarEntry> entries) throws IOException {
		//First, write header
		//Is it just me, or does this remind me of a PTPatch?
		//anyways.

		Charset utf8 = Charset.forName("UTF-8");

		DataOutputStream os = new DataOutputStream(outputStream);
		os.writeInt(r(0x01636172)); //car
		os.writeInt(r(1)); //I dunno
		int headerLength = calcHeaderLength(entries);
		os.writeInt(r(headerLength));
		os.writeInt(r(entries.size())); //count
		int nextFileBegin = (3 * 4) + headerLength;
		for (int i = 0; i < entries.size(); i++) {
			CarEntry ent = entries.get(i);
			os.writeInt(r(1)); //1
			os.writeInt(r(nextFileBegin)); //file offset
			os.writeInt(r(ent.name.length())); //length of name
			os.write(ent.name.getBytes(utf8)); //name
			for (int j = ent.name.length() & 0x3; j < 4; j++) {
				os.write(0); //padding
			}
			nextFileBegin += (3 * 4) + ent.contents.length + ((ent.contents.length & 0x3) == 0? 0: 4 - (ent.contents.length & 0x3));
		}
		//now write the files
		for (int i = 0; i < entries.size(); i++) {
			CarEntry ent = entries.get(i);
			os.writeInt(r(2)); //probably begin of file?
			int pad = ((ent.contents.length & 0x3) == 0? 0: 4 - (ent.contents.length & 0x3));
			os.writeInt(r(ent.contents.length + 4 + pad)); //length of tag
			os.writeInt(r(ent.contents.length)); //length of file
			os.write(ent.contents); //bytes
			for (int j = 0; j < pad; j++) {
				os.write(0); //padding
			}
		}
		//finally EOF
		os.writeInt(0xffffffff);
		os.writeInt(0);
	}

	private static int r(int i) {
		return Integer.reverseBytes(i);
	}

	private static int calcHeaderLength(List<CarEntry> entries) {
		//4 + 4 + 4 + (length of name, rounded up to 4)
		int length = 4;
		for (CarEntry e: entries) {
			length += (3 * 4) + (((e.name.length() >> 2) + 1) << 2);
		}
		return length;
	}
}
