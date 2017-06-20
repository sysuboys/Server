package org.sysuboys.diaryu.business.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

@Service
public class DiaryFileService implements IDiaryFileService {

	public void create(String filename, InputStream in) throws IOException {
		FileUtils.copyInputStreamToFile(in, new File("." + File.separator + "diary" + File.separator + filename));
	}

	public File get(String filename) {
		return new File("." + File.separator + "diary" + File.separator + filename);
	}

}
