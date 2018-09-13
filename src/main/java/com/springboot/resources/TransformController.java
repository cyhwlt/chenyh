package com.springboot.resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.repository.RepositoryDirectory;
import org.pentaho.di.repository.RepositoryDirectoryInterface;
import org.pentaho.di.repository.RepositoryElementMetaInterface;
import org.pentaho.di.repository.kdr.KettleDatabaseRepository;
import org.pentaho.di.repository.kdr.KettleDatabaseRepositoryMeta;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springboot.entity.KettleDatabaseRepositoryDto;
import com.springboot.entity.RepositoryTreeDto;
import com.springboot.util.Constant;
import com.springboot.util.JsonUtil;
import com.springboot.util.KettleUtil;

@Controller
@RequestMapping("/trans")
public class TransformController {

	private static Logger logger = LogManager.getLogger(TransformController.class);
	
	@RequestMapping(path = "/exceltosql", method = RequestMethod.GET)
	public void excelToSql(/*@PathVariable("fileUrl") String url, @PathVariable("pc") String pc*/) {
		String url = "E:/file.xls";
		String pc = "yiruku";
		KettleUtil etl = new KettleUtil("111.ktr");
		Map<String, String> para = new HashMap<String, String>();
		para.put("FILE_PATH", url);
		para.put("PC", pc);
		etl.runTransformation(para);
		logger.info("转换成功");
	}
	
	@RequestMapping(path="/connectrepository", method=RequestMethod.POST, consumes="application/json", produces="application/json")
	@ResponseBody
	public void getAllDirectory(@RequestBody KettleDatabaseRepositoryDto dto) throws KettleException {
		KettleDatabaseRepository databaseRepository = connectRepository(dto);
		List<RepositoryTreeDto> allRepositoryTreeList = new ArrayList<RepositoryTreeDto>();
		
		List<RepositoryTreeDto> repositoryTreeList = getAllDirectoryTreeList(databaseRepository, "/", allRepositoryTreeList);

		for (RepositoryTreeDto repositoryTree : repositoryTreeList){
			logger.info("<<<<<<<<<<<<<<<<<<<<<<<");
			System.out.println(repositoryTree);
			logger.info(JsonUtil.objectToJson(repositoryTree));
		}
	}
	
	public KettleDatabaseRepository connectRepository(KettleDatabaseRepositoryDto dto) throws KettleException {
		//数据库连接元对象
    	//DatabaseMeta databaseMeta = new DatabaseMeta("repository", "MYSQL", 
				//"Native", "localhost", "kettle-repository", "3306", "root", "1234"); 
		if(dto != null) {
			DatabaseMeta databaseMeta = new DatabaseMeta(dto.getConnName(), dto.getConnType(), dto.getConnWay(), dto.getHostName(), dto.getDatabaseName(), dto.getDatabasePort(), dto.getUserName(), dto.getPassword());
			
			//资源库元对象
			KettleDatabaseRepositoryMeta repositoryInfo = new KettleDatabaseRepositoryMeta();
			repositoryInfo.setConnection(databaseMeta);
			//资源库
			KettleDatabaseRepository repository = new KettleDatabaseRepository();
			repository.init(repositoryInfo);
			repository.connect("admin", "admin");
			
			return repository;
		}
        
        return null;
	}
	
	public static List<RepositoryTreeDto> getAllDirectoryTreeList(KettleDatabaseRepository kettleDatabaseRepository, 
			String path, List<RepositoryTreeDto> allRepositoryTreeList) throws KettleException{
		//获取Job和Transformation和Directory的信息
		List<RepositoryTreeDto> repositoryTreeList = getJobAndTrans(kettleDatabaseRepository, path);
		if (repositoryTreeList.size() != 0){
			for (RepositoryTreeDto repositoryTree : repositoryTreeList){
				//如果有子Directory或者Job和Transformation。那么递归遍历
				if (!repositoryTree.isLasted()){
					getAllDirectoryTreeList(kettleDatabaseRepository, repositoryTree.getPath(), allRepositoryTreeList);
					allRepositoryTreeList.add(repositoryTree);
				}else{
					allRepositoryTreeList.add(repositoryTree);
				}
			}
		}				
		return allRepositoryTreeList;		
	}
	
	public static List<RepositoryTreeDto> getJobAndTrans(KettleDatabaseRepository repository, 
			String path) throws KettleException {
		//获取当前的路径信息
		RepositoryDirectoryInterface rDirectory = repository.loadRepositoryDirectoryTree().findDirectory(path);
		//获取Directory信息
		List<RepositoryTreeDto> repositoryTreeList = getDirectory(repository, rDirectory);
		//获取Job和Transformation的信息
		List<RepositoryElementMetaInterface> li = repository.getJobAndTransformationObjects(rDirectory.getObjectId(), false);
		if (null != li) {
			for (RepositoryElementMetaInterface repel : li) {
				if (Constant.TYPE_JOB.equals(repel.getObjectType().toString())) {						
					RepositoryTreeDto repositoryTree = new RepositoryTreeDto();
					StringBuilder stringBuilder = new StringBuilder();
					stringBuilder.append(Constant.TYPE_JOB).append(rDirectory.getObjectId().toString()).append("@").append(repel.getObjectId().toString());
					repositoryTree.setId(stringBuilder.toString());
					repositoryTree.setParent(rDirectory.getObjectId().toString());
					repositoryTree.setText(repel.getName());
					repositoryTree.setType(Constant.TYPE_JOB);
					repositoryTree.setLasted(true);
					repositoryTreeList.add(repositoryTree);
				}else if (Constant.TYPE_TRANS.equals(repel.getObjectType().toString())){
					RepositoryTreeDto repositoryTree = new RepositoryTreeDto();
					StringBuilder stringBuilder = new StringBuilder();
					stringBuilder.append(Constant.TYPE_TRANS).append(rDirectory.getObjectId().toString()).append("@").append(repel.getObjectId().toString());
					repositoryTree.setId(stringBuilder.toString());
					repositoryTree.setParent(rDirectory.getObjectId().toString());
					repositoryTree.setText(repel.getName());
					repositoryTree.setType(Constant.TYPE_TRANS);
					repositoryTree.setLasted(true);
					repositoryTreeList.add(repositoryTree);
				}
			}
		}
		return repositoryTreeList;
	}
	
	private static List<RepositoryTreeDto> getDirectory(KettleDatabaseRepository repository, 
			RepositoryDirectoryInterface rDirectory) throws KettleException {
		List<RepositoryTreeDto> repositoryTreeList = new ArrayList<RepositoryTreeDto>();
		if (null != repository && null != rDirectory){			
			RepositoryDirectoryInterface tree = repository.loadRepositoryDirectoryTree().findDirectory(rDirectory.getObjectId());
			if (rDirectory.getNrSubdirectories() > 0){
				for (int i = 0; i < rDirectory.getNrSubdirectories(); i++) {				
					RepositoryDirectory subTree = tree.getSubdirectory(i);
					RepositoryTreeDto repositoryTree = new RepositoryTreeDto();
					repositoryTree.setId(subTree.getObjectId().toString());
					repositoryTree.setParent(rDirectory.getObjectId().toString());
					repositoryTree.setText(subTree.getName());
					repositoryTree.setPath(subTree.getPath());
					//判断是否还有子Directory或者Job和Transformation
					List<RepositoryElementMetaInterface> RepositoryElementMetaInterfaceList = 
							repository.getJobAndTransformationObjects(subTree.getObjectId(), false);					
					if (subTree.getNrSubdirectories() > 0 || RepositoryElementMetaInterfaceList.size() > 0){
						repositoryTree.setLasted(false);
					}else{
						repositoryTree.setLasted(true);
					}
					repositoryTreeList.add(repositoryTree);
				}	
			}					
		}
		return repositoryTreeList;	
	}
}
