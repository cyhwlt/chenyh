package com.springboot.resources.trans;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleSecurityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springboot.entity.KtrDto;
import com.springboot.service.dbrepository.KettleDatabaseRepositoryService;
import com.springboot.service.repository.RepositoryService;

@Controller
@RequestMapping("/trans")
public class TransExectorController {
	
	private static Logger logger = LogManager.getLogger(TransExectorController.class);
	
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private KettleDatabaseRepositoryService kdrService;
	
	/**
	 * 获取资源库下的数据连接列表
	 * @return
	 * @throws KettleException
	 */
	@RequestMapping(path="/allDatabases", method=RequestMethod.GET, produces="application/json")
	@ResponseBody
	public String[] findDatabases() throws KettleException {
		DatabaseMeta meta = new DatabaseMeta();
		String[] checkParameters = meta.checkParameters();
		return checkParameters;
	}
	
	/**
	 * 数据转换接口
	 * @param repositoryName 资源库名称
	 * @param fileName .krt文件名
	 * @throws KettleSecurityException
	 * @throws KettleException
	 */
	@RequestMapping(path="/excelinput/{repositoryName}/{fileName}", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	@ResponseBody
	public void transInformation(@PathVariable("repositoryName") String repositoryName, @PathVariable("fileName") String fileName) throws KettleSecurityException, KettleException{
		
		Object isConnect = this.repositoryService.connectRepository(repositoryName);
		if(isConnect != null){
			this.kdrService.runTrans(fileName);
		}
		//获取kdr文件
//		SpoonPerspectiveManager manager = SpoonPerspectiveManager.getInstance();
//		if(manager != null && manager.getActivePerspective() != null){
//			EngineMetaInterface activeMeta = manager.getActivePerspective().getActiveMeta();
//			if(activeMeta instanceof TransMeta){
//			TransMeta transMeta = (TransMeta)activeMeta;
//			}
//		}
	}
	
	/**
	 * 生成.ktr文件
	 * @param dto
	 * @throws KettleException
	 */
	@RequestMapping(path="/generatektr", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	@ResponseBody
	public void generateKtrfile(@RequestBody KtrDto dto) throws KettleException{
		this.kdrService.generateKtr(dto);
	}
}
