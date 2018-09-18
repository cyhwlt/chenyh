package com.springboot.service.repository;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleSecurityException;
import org.pentaho.di.core.plugins.PluginRegistry;
import org.pentaho.di.core.plugins.RepositoryPluginType;
import org.pentaho.di.repository.RepositoriesMeta;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.repository.RepositoryMeta;
import org.pentaho.di.ui.repository.model.RepositoriesModel;
import org.springframework.stereotype.Service;

@Service
public class RepositoryService {
	
	private static Logger logger = LogManager.getLogger(RepositoryService.class);
	private RepositoriesModel model;
	private RepositoriesMeta input;
	
	public Object connectRepository(String repositoryName) throws KettleSecurityException, KettleException{
		RepositoryMeta selectedRepository = getSelectedRepository(repositoryName);
		RepositoryMeta repositoryMeta = input.getRepository(model.getRepositoryIndex(selectedRepository));
		Repository repository = PluginRegistry.getInstance().loadClass(RepositoryPluginType.class, repositoryMeta.getId(), Repository.class );
		repository.init(repositoryMeta);
		repository.connect("admin", "admin");
		if(repository.isConnected()){
			logger.info("连接成功");
			return repository;
		}else{
			logger.error("连接失败");
			return null;
		}
	}
	
	/**
	 * 获取被选中的资源库
	 * @param repositoryName
	 * @return
	 */
	public RepositoryMeta getSelectedRepository(String repositoryName){
		model = new RepositoriesModel();
		input = new RepositoriesMeta();
		List<RepositoryMeta> repositoryList = new ArrayList<RepositoryMeta>();
		try {
			input.readData();
	        for ( int i = 0; i < input.nrRepositories(); i++ ) {
	        	repositoryList.add(input.getRepository( i ) );
	        }
	        model.setAvailableRepositories(repositoryList);
	        if(repositoryList != null && repositoryList.size() > 0){
	        	for (RepositoryMeta meta : repositoryList) {
					if(meta != null && meta.getName().equals(repositoryName)){
						return meta;
					}
				}
	        }
		} catch (KettleException e) {
			logger.error(e);
		}
		return null;
	}
}
