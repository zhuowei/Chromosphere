package net.zhuoweizhang.chromosphere.storage;

import android.content.*;

import com.ansca.corona.storage.AssetFileLocationInfo;

public class FileServices extends com.ansca.corona.storage.FileServices {
	public FileServices(Context context) {
		super(context);
	}

	@Override
	public AssetFileLocationInfo getAssetFileLocation(String str) {
		System.out.println("Loading " + str);
		return super.getAssetFileLocation(str);
	}
}
