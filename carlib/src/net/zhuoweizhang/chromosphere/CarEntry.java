package net.zhuoweizhang.chromosphere;

public class CarEntry {
	public String name;
	public long size;
	public long offset;
	public byte[] contents;

	public CarEntry(String name) {
		this.name = name;
	}
}
