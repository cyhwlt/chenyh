package com.springboot.resources.trans;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleSecurityException;
import org.pentaho.di.trans.steps.excelinput.ExcelInputField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.entity.database.DBToDBDto;
import com.springboot.entity.database.SQLDto;
import com.springboot.entity.excel.ExcelAnalysisDto;
import com.springboot.entity.excel.ExcelToDBDto;
import com.springboot.service.dbrepository.ExcelToDatabaseTransService;
import com.springboot.service.dbrepository.KettleDatabaseRepositoryService;
import com.springboot.service.repository.RepositoryService;
import com.springboot.util.JsonUtil;

@Controller
@RequestMapping("/trans")
public class TransExectorController {
	
	private static Logger logger = LogManager.getLogger(TransExectorController.class);
	
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
	}
	
	/**
	 * 生成表到表的转换文件
	 * @param dto
	 * @throws KettleException
	 */
	@RequestMapping(path="/generatektr", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	@ResponseBody
	public void generateKtrfile(@RequestBody DBToDBDto dto) throws KettleException{
		this.kdrService.generateKtr(dto);
	}
	
	/**
	 * 生成excel文件到库的转换文件（通用）
	 * @param dto
	 * @throws Exception
	 */
	@RequestMapping(path="/exceltotable", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	@ResponseBody
	public void excelToDatabase(@RequestBody ExcelToDBDto dto) throws Exception{
		this.etdtService.excelToDatabase(dto);
	}
	
	/**
	 * 转换时自动生成表
	 * 1.判断该表名在该数据库中是否已经存在
	 * 2.若已存在则直接返回没有sql语句可执行，若不存在 则获取excel解析后的字段，生成创建表语句
	 * 3.执行创建
	 * @throws Exception 
	 */
	@RequestMapping(path="/sql", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	@ResponseBody
	public void sql(@RequestBody SQLDto dto) throws Exception{
		this.etdtService.sql(dto);
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
