package com.springboot.resources.repository;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleSecurityException;
import org.pentaho.di.repository.RepositoriesMeta;
import org.pentaho.di.repository.RepositoryMeta;
import org.pentaho.di.repository.kdr.KettleDatabaseRepositoryMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springboot.entity.database.DatabaseDto;
import com.springboot.entity.database.RepositoryDto;
import com.springboot.service.repository.RepositoryService;

@Component
@RequestMapping("/repository")
public class RepositoryController {
	
	private static Logger logger = LogManager.getLogger(RepositoryController.class);
	private RepositoriesMeta input;
	
	@Autowired
	private RepositoryService repositoryService;

	/**
	 * 新建资源库
	 * @param dto:提供资源库名称，类型，描述及数据库对象
	 * @throws KettleException 
	 */
	@RequestMapping(path="/add", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	@ResponseBody
	public void newRepository(@RequestBody RepositoryDto dto) throws KettleException {
		input = new RepositoriesMeta();
		input.readData();
		
		// 数据库列表
		List<DatabaseMeta> dbrmList = new ArrayList<DatabaseMeta>();
		for(int j = 0; j < this.input.nrDatabases(); j++) {
			dbrmList.add(input.getDatabase(j));
		}
		// 构建KettleDatabaseRepositoryMeta(对应源码中的getInfo())
		KettleDatabaseRepositoryMeta kdrm = new KettleDatabaseRepositoryMeta();
		kdrm.setId(dto.getRepositoryType());
		kdrm.setName(dto.getName());
		kdrm.setDescription(dto.getDecrible());
		kdrm.setConnection(dbrmList.get(0));
		input.addRepository(kdrm);
		input.writeData();
	}
	
	/**
	 * 获取所有资源库
	 * @return
	 * @throws KettleException
	 */
	@RequestMapping(path="/allRepositories", method=RequestMethod.GET, produces="application/json")
	@ResponseBody
	public List<RepositoryDto> findRepositories() throws KettleException {
		input = new RepositoriesMeta();
		input.readData();
		// 资源库列表
		List<RepositoryDto> rmList = new ArrayList<RepositoryDto>();
		for(int i = 0; i < this.input.nrRepositories(); i++) {
			RepositoryMeta repository = input.getRepository(i);
			RepositoryDto dto = new RepositoryDto();
			dto.setName(repository.getName());
			dto.setDecrible(repository.getDescription());
			dto.setRepositoryType(repository.getId());
			rmList.add(dto);
		}
		return rmList;
	}
	
	/**
	 * 获取所有数据资源库连接
	 * @return
	 * @throws KettleException
	 */
	@RequestMapping(path="/dbrepositories", method=RequestMethod.GET, produces="application/json")
	@ResponseBody
	public List<DatabaseDto> findDatabases() throws KettleException {
		input = new RepositoriesMeta();
		input.readData();
		// 数据库列表
		List<DatabaseDto> dbrmList = new ArrayList<DatabaseDto>();
		for(int j = 0; j < this.input.nrDatabases(); j++) {
			DatabaseMeta meta = input.getDatabase(j);
			logger.info("<<<<<<<<<"+meta);
			DatabaseDto dto = new DatabaseDto();
			dto.setConnName(meta.getDisplayName());
			dto.setConnType(meta.getPluginId());
			dto.setDbName(meta.getDatabaseName());
			dto.setHostName(meta.getHostname());
			dto.setUserName(meta.getUsername());
			dbrmList.add(dto);
		}
		return dbrmList;
	}
	
	/**
	 * 连接资源库：选中一个资源库，建立连接
	 * @throws KettleException 
	 * @throws KettleSecurityException 
	 */
	@RequestMapping(path="/connectrepository/{repositoryName}", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	@ResponseBody
	public void connectRepository(@PathVariable("repositoryName") String repositoryName) throws KettleSecurityException, KettleException{
		this.repositoryService.connectRepository(repositoryName);
	}
}
