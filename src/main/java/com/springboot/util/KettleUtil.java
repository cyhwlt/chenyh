package com.springboot.util;

import java.io.File;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;

public class KettleUtil {
	private static Logger logger = LogManager.getLogger(KettleUtil.class);
	public String RES_DIR = "res";
	private String fullFileName;
	
	public KettleUtil(String fileName) {
		fullFileName = System.getProperty("user.dir") + File.separator + RES_DIR;
		fullFileName += File.separator + fileName;
	}
	
	public void runTransformation(Map<String, String> paras) {
		try {
//			KettleEnvironment.init();
			TransMeta tm = new TransMeta(fullFileName);
			Trans trans = new Trans(tm);
			for(Map.Entry<String, String> entry: paras.entrySet()){
				trans.setVariable(entry.getKey(), entry.getValue());
			}
			trans.execute(null);
			trans.waitUntilFinished();
			if(trans.getErrors() > 0) {
				throw new RuntimeException("There wereerrors during transformation execution.");
			}
		} catch (KettleException e) {
			logger.error(e);
		}
		
	}
}
