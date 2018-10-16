package com.springboot.resources.trans;

import java.io.IOException;

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

import com.springboot.entity.DBToDBDto;
import com.springboot.entity.ExcelAnalysisDto;
import com.springboot.entity.ExcelToDBDto;
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
	 * excel文件上传
	 * @param request
	 * @param file
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(path="/upload", method=RequestMethod.POST, produces="application/json")
	@ResponseBody
	public String  uploadExcel(HttpServletRequest request, @RequestParam("upload")MultipartFile file, Model model) throws IOException {
		return kdrService.upload(request, file, model);
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
	 * 解析excel文件
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(path="/analysis", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	@ResponseBody
	public String anaylsisExcel(@RequestBody ExcelAnalysisDto dto) throws Exception{
		ExcelInputField[] results = this.etdtService.analysisFile(dto.getFilePath(), dto.getSheetNumber());
		return JsonUtil.objectToJson(results);
	}
}
