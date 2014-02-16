package net.zhuoweizhang.chromosphere.storage;

import android.content.*;

import java.io.*;

import com.ansca.corona.storage.*;

public class FileServices extends com.ansca.corona.storage.FileServices {
	private static File redirectFile = new File("/sdcard/winprogress/smth.apk");
	private static ZipResourceFile redirectResFile;
	public FileServices(Context context) {
		super(context);
	}

	@Override
	public AssetFileLocationInfo getAssetFileLocation(String str) {
		System.out.println("Loading " + str);
		ZipResourceFile resFile = null;
		try {
			resFile = getRedirectFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (resFile == null)
			return super.getAssetFileLocation(str);
		String name = "assets/" + str;
		ZipResourceFile.ZipEntryRO entry = resFile.getEntry(name);
		System.out.println(entry);
		if (entry == null)
			return super.getAssetFileLocation(str);
		AssetFileLocationInfo.Settings settings = new AssetFileLocationInfo.Settings();
		settings.setPackageFile(entry.mFile);
		settings.setAssetFilePath(str);
		settings.setZipEntryName(name);
		settings.setByteOffsetInPackage(entry.mOffset);
		settings.setByteCountInPackage(entry.mCompressedLength);
		settings.setIsCompressed(!entry.isUncompressed());
		return new AssetFileLocationInfo(settings);
	}

	public static ZipResourceFile getRedirectFile() throws IOException {
		if (redirectFile == null) return null;
		if (redirectResFile == null) redirectResFile = new ZipResourceFile(redirectFile);
		return redirectResFile;
	}

	@Override
	public File getPatchExpansionFile() {
		return redirectFile;
	}

	@Override
	public File getExpansionFileDirectory() {
		return redirectFile.getParentFile();
	}
}
