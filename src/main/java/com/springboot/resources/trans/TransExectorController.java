package com.springboot.resources.trans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.springboot.entity.database.DBToDBDto;
import com.springboot.entity.database.SQLDto;
import com.springboot.entity.excel.ExcelToDBDto;
import com.springboot.service.dbrepository.ExcelToDatabaseTransService;
import com.springboot.service.dbrepository.KettleDatabaseRepositoryService;
import com.springboot.service.repository.RepositoryService;

@Controller
@RequestMapping("/trans")
public class TransExectorController {
	
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private KettleDatabaseRepositoryService kdrService;
	@Autowired
	private ExcelToDatabaseTransService etdtService;
	
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
	 * 通用数据转换接口
	 * @param repositoryName 资源库名称
	 * @param fileName .krt文件名
	 * @return 
	 * @throws KettleSecurityException
	 * @throws KettleException
	 */
	@RequestMapping(path="/excelinput/{repositoryName}/{fileName}", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	@ResponseBody
	public Map<String, Object> transInformation(@PathVariable("repositoryName") String repositoryName, @PathVariable("fileName") String fileName) throws Exception{
		
		Object isConnect = this.repositoryService.connectRepository(repositoryName);
		Map<String, Object> runTrans = new HashMap();
		if(isConnect != null){
			 runTrans = this.kdrService.runTrans(fileName);
		}
		return runTrans;
	}
	
	/**
	 * 生成表到表的转换文件
	 * @param dto
	 * @throws KettleException
	 */
	@RequestMapping(path="/generatektr", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	@ResponseBody
	public Map<String, Object> generateKtrfile(@RequestBody DBToDBDto dto) throws Exception{
		return this.kdrService.generateKtr(dto);
	}
	
	/**
	 * 生成excel文件到库的转换文件（通用）
	 * @param dto
	 * @return 
	 * @throws Exception
	 */
	@RequestMapping(path="/exceltotable", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	@ResponseBody
	public Map<String, Object> excelToDatabase(@RequestBody ExcelToDBDto dto) throws Exception{
		return this.etdtService.excelToDatabase(dto);
	}
	
	/**
	 * 转换时自动生成表
	 * 1.判断该表名在该数据库中是否已经存在
	 * 2.若已存在则直接返回没有sql语句可执行，若不存在 则获取excel解析后的字段，生成创建表语句
	 * 3.执行创建
	 * @return 
	 * @throws Exception 
	 */
	@RequestMapping(path="/sql", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	@ResponseBody
	public Map<String, Object> createTable(@RequestBody SQLDto dto) throws Exception{
		return this.etdtService.sql(dto);
	}
	
	// 测试接口
	@RequestMapping(path="/test/{tableName}", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	@ResponseBody
	public List<String> test(@PathVariable("tableName") String tableName){
//		return this.etdtService.getColumnNames(tableName);
//		return this.etdtService.getColumnTypes(tableName);
		return null;
	}
}
