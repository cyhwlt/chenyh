package com.springboot.resources.excel;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pentaho.di.trans.steps.excelinput.ExcelInputField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.entity.excel.ExcelAnalysisDto;
import com.springboot.service.excel.ExcelService;
import com.springboot.util.JsonUtil;

@Controller
@RequestMapping("/excel")
public class ExcelController {
	
	private static Logger logger = LogManager.getLogger(ExcelController.class);
	
	@Autowired
	private ExcelService xlsService;
	
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
		return this.xlsService.upload(request, file, model);
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
		ExcelInputField[] results = this.xlsService.analysisFile(dto.getFilePath(), dto.getSheetNumber());
		return JsonUtil.objectToJson(results);
	}
	
	/**
	 * 获取excel所有的sheet
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(path="/sheetName", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	@ResponseBody
	public List<String> getSheetNames(@RequestBody ExcelAnalysisDto dto) throws Exception{
		return this.xlsService.getSheetNames(dto.getFilePath());
	}

}
