package net.zhuoweizhang.chromosphere;

import java.io.*;
import java.util.*;

/*
structure:
int 4 byte magic number //0
int: 1 //4
int: length of header //8
int: number of entries in header //12
each entry:
int: 1
int: file offset
int: length of filename
char: filename, 4-byte aligned
*/

public class CarFile {

	private File fileLoc;
	private RandomAccessFile file;
	private int count;

	private final LinkedHashMap<String, CarEntry> entries = new LinkedHashMap<String, CarEntry>();

	public CarFile(File fileLoc) throws IOException {
		this.fileLoc = fileLoc;
		//mmap the car file
		this.file = new RandomAccessFile(this.fileLoc, "r");
		this.file.seek(12);
		this.count = Integer.reverseBytes(this.file.readInt());
		for (int i = 0; i < count; i++) {
			int type = this.file.readInt();
			int offset = Integer.reverseBytes(this.file.readInt());
			int filenameLength = Integer.reverseBytes(this.file.readInt());
			byte[] mybuf = new byte[filenameLength];
			this.file.read(mybuf);
			String fileName = new String(mybuf, "UTF-8");
			long nextPosition = ((this.file.getFilePointer() >> 2) + 1) << 2;
			CarEntry entry = new CarEntry(fileName);
			entry.offset = offset;
			this.file.seek(offset + 8);
			entry.size = Integer.reverseBytes(this.file.readInt());
			entries.put(fileName, entry);
			this.file.seek(nextPosition);
		}
	}

	public Collection<CarEntry> entries() {
		return entries.values();
	}

	public byte[] getBytes(CarEntry entry) throws IOException {
		this.file.seek(entry.offset + 12);
		byte[] myBytes = new byte[(int) entry.size];
		this.file.read(myBytes);
		return myBytes;
	}

}
