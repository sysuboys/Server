package org.sysuboys.diaryu.business.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface IDiaryFileService {

	void create(String filename, InputStream in) throws IOException;

	File get(String filename);

}
