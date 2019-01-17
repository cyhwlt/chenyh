package com.springboot.service.job;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.plugins.PluginRegistry;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobHopMeta;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.job.entries.job.JobEntryJob;
import org.pentaho.di.job.entries.special.JobEntrySpecial;
import org.pentaho.di.job.entries.success.JobEntrySuccess;
import org.pentaho.di.job.entries.trans.JobEntryTrans;
import org.pentaho.di.job.entry.JobEntryCopy;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.entity.job.JobDto;
import com.springboot.entity.job.JobScheduleDto;
import com.springboot.entity.job.JobTransDto;
import com.springboot.entity.trans.ExtractDataDto;
import com.springboot.entity.trans.SetVariableDto;
import com.springboot.entity.trans.TableNamesDto;
import com.springboot.service.common.BulkloadService;
import com.springboot.service.common.DatabaseService;
import com.springboot.service.common.JobsService;
import com.springboot.service.common.TransService;

@Service
public class JobService {
	
	private static Logger logger = LogManager.getLogger(JobService.class);
	
	@Autowired
	private DatabaseService dbService;
	@Autowired
	private TransService transService;
	@Autowired
	private JobsService jobsService;
	@Autowired
	private BulkloadService blService;
	
	public Map<String, Object> runJob(JobScheduleDto dto) throws Exception{
		Map<String, Object> returnValue = new HashMap();
        // jobname 是Job脚本的路径及名称
        JobMeta jobMeta = new JobMeta(dto.getKjbPath(), null);
        Job job = new Job(null, jobMeta);
        job.setVariable("id", "1");
        job.setVariable("content", "content");
        job.setVariable("file", "9");
//          job.setVariable("file", dto.getLogPath());

        job.start();
        job.waitUntilFinished();
        if (job.getErrors() > 0) {
        	returnValue.put("code", -1);
        	returnValue.put("message", "There are errors during job exception!(执行job发生异常)");
        	returnValue.put("data", null);
        	logger.error("There are errors during job exception!(执行job发生异常)");
        }
        returnValue.put("code", 0);
        returnValue.put("message", "");
        returnValue.put("data", null);
        return returnValue;   
	}

	public Map<String, Object> generateJobFile(JobDto dto) {
		Map<String, Object> returnValue = new HashMap();
		try {
			JobMeta generateJob = new JobMeta();
			if(dto.isSerial()){
				generateJob = serialJob(dto);
			}else{
				generateJob = parallelJob(dto);
			}
			String xml = generateJob.getXML();
			File file = new File(dto.getFileName());
			FileUtils.writeStringToFile(file, xml, "UTF-8");
			returnValue.put("code", 0);
			returnValue.put("messsage", "");
			returnValue.put("data", xml);
		} catch (IOException e) {
			returnValue.put("code", -1);
			returnValue.put("message", e.getMessage());
			returnValue.put("data", null);
			logger.error("生成job转换文件异常：" + e.getMessage());
		}
		return returnValue;
	}
	
	public JobMeta serialJob(JobDto dto) {
		try {
			JobMeta jobMeta = new JobMeta();
			jobMeta.setName(dto.getJobName());
			jobMeta.setFilename(dto.getFileName());
	
			// start
			JobEntrySpecial start = new JobEntrySpecial();
			start.setName(dto.getSerialDto().getStartDto().getStartName());
			start.setStart(dto.getSerialDto().getStartDto().isStart());
			start.setRepeat(dto.getSerialDto().getStartDto().isRepeat());
			if(dto.getSerialDto().getStartDto().isRepeat()){
				start.setSchedulerType(dto.getSerialDto().getStartDto().getSchedulerType());
				start.setIntervalMinutes(dto.getSerialDto().getStartDto().getIntervalMinutes());
				start.setIntervalSeconds(dto.getSerialDto().getStartDto().getIntervalSeconds());
				start.setHour(dto.getSerialDto().getStartDto().getHour());
				start.setMinutes(dto.getSerialDto().getStartDto().getMinutes());
				start.setWeekDay(dto.getSerialDto().getStartDto().getWeekDay());
				start.setDayOfMonth(dto.getSerialDto().getStartDto().getDayofMonth());
			}
	
			JobEntryCopy startEntry = new JobEntryCopy(start);
			startEntry.setDrawn(dto.getSerialDto().getStartDto().isDrawn());
			startEntry.setLaunchingInParallel(dto.getSerialDto().getStartDto().isParallel());
			startEntry.setLocation(100, 100);
	
			jobMeta.addJobEntry(startEntry);
	
			int size = dto.getSerialDto().getTransDto().size();
			List<JobEntryCopy> transes = new ArrayList();
			for(int i=0; i<size; i++){
				// 转换
				JobEntryTrans jeTrans = new JobEntryTrans();
				jeTrans.setName(dto.getSerialDto().getTransDto().get(i).getTransName());
				jeTrans.setFileName(dto.getSerialDto().getTransDto().get(i).getFileName());
				jeTrans.setDescription(dto.getSerialDto().getTransDto().get(i).getDescription());
				
				JobEntryCopy transCopy = new JobEntryCopy(jeTrans);
				transCopy.setDrawn(dto.getSerialDto().getTransDto().get(i).isDrawn());
				transCopy.setLocation(100, 400);
				jobMeta.addJobEntry(transCopy);
				if(i==0){
					jobMeta.addJobHop(new JobHopMeta(startEntry, transCopy));
				}else{
					jobMeta.addJobHop(new JobHopMeta(transes.get(i-1), transCopy));
				}
				transes.add(transCopy);
			}
	
			JobEntrySuccess success = new JobEntrySuccess();
			success.setName(dto.getSerialDto().getSuccessDto().get(0).getSuccessName());
			
			JobEntryCopy successEntry = new JobEntryCopy(success);
			successEntry.setDrawn(dto.getSerialDto().getSuccessDto().get(0).isDrawn());
			successEntry.setLocation(100, 800);
			jobMeta.addJobEntry(successEntry); 
	
			JobHopMeta greenHop = new JobHopMeta(transes.get(transes.size()-1), successEntry);
			greenHop.setEvaluation(true);
			jobMeta.addJobHop(greenHop);
			return jobMeta;
			} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public JobMeta parallelJob(JobDto dto){
		try{
			JobMeta jobMeta = new JobMeta();
			jobMeta.setName(dto.getJobName());
			jobMeta.setFilename(dto.getFileName());
			//start
			JobEntrySpecial start = new JobEntrySpecial();
			start.setName(dto.getParallelDto().getStartDto().getStartName());
			start.setStart(dto.getParallelDto().getStartDto().isStart());
			start.setRepeat(dto.getParallelDto().getStartDto().isRepeat());
			if(dto.getParallelDto().getStartDto().isRepeat()){
				start.setSchedulerType(dto.getParallelDto().getStartDto().getSchedulerType());
				start.setIntervalMinutes(dto.getParallelDto().getStartDto().getIntervalMinutes());
				start.setIntervalSeconds(dto.getParallelDto().getStartDto().getIntervalSeconds());
				start.setHour(dto.getParallelDto().getStartDto().getHour());
				start.setMinutes(dto.getParallelDto().getStartDto().getMinutes());
				start.setWeekDay(dto.getParallelDto().getStartDto().getWeekDay());
				start.setDayOfMonth(dto.getParallelDto().getStartDto().getDayofMonth());
			}
			JobEntryCopy startEntry = new JobEntryCopy(start);
			startEntry.setDrawn(dto.getParallelDto().getStartDto().isDrawn());
			startEntry.setLaunchingInParallel(dto.getParallelDto().getStartDto().isParallel());
			startEntry.setLaunchingInParallel(true);
			startEntry.setLocation(100, 100);
			jobMeta.addJobEntry(startEntry);
			
			//trans
			Map<String, List<JobTransDto>> transTosuccess = dto.getParallelDto().getTransTosuccess();
			for(String success: transTosuccess.keySet()){
				List<JobTransDto> transes = transTosuccess.get(success);
				int size = transes.size();
				List<JobEntryCopy> transList = new ArrayList();
				for(int i=0; i<size; i++){
					JobEntryTrans jeTrans = new JobEntryTrans();
					jeTrans.setName(transes.get(i).getTransName());
					jeTrans.setFileName(transes.get(i).getFileName());
					jeTrans.setDescription(transes.get(i).getDescription());
					
					JobEntryCopy transCopy = new JobEntryCopy(jeTrans);
					transCopy.setDrawn(transes.get(i).isDrawn());
					transCopy.setLocation(100, 400);
					jobMeta.addJobEntry(transCopy);
					if(i==0){
						jobMeta.addJobHop(new JobHopMeta(startEntry, transCopy));
					}else{
						jobMeta.addJobHop(new JobHopMeta(transList.get(i-1), transCopy));
					}
					transList.add(transCopy);
				}
				
				JobEntrySuccess successEntry = new JobEntrySuccess();
				successEntry.setName(success);
				
				JobEntryCopy successCopy = new JobEntryCopy(successEntry);
				successCopy.setDrawn(true);
				successCopy.setLocation(100, 800);
				jobMeta.addJobEntry(successCopy); 
		
				JobHopMeta greenHop = new JobHopMeta(transList.get(transList.size()-1), successCopy);
				greenHop.setEvaluation(true);
				jobMeta.addJobHop(greenHop);
			}
			return jobMeta;
		}catch(Exception e){
			logger.error(e.getMessage());
			return null;
		}
		
	}

	public void dbMigrate() throws Exception {
		//获取所有表名称转换
		this.getTableNames(null, null);
		//设置变量
		this.setVariable(null, null);
		//抽取数据
		this.extractData(null, null);
		//抽取数据作业
		this.extractDataJob(null);
		//库迁移作业
		this.dbMigrateJob();
		
	}

	private void dbMigrateJob() {
		
	}

	private JobMeta extractDataJob(JobDto dto) throws Exception {
			JobMeta jobMeta = generateJob(dto);
			String xml = jobMeta.getXML();
			File file = new File(dto.getFileName());
			FileUtils.writeStringToFile(file, xml, "UTF-8");
			return jobMeta;
	}

	private JobMeta generateJob(JobDto dto) {
		JobMeta jobMeta = new JobMeta();
		jobMeta.setName(dto.getJobName());
		
		return null;
	}

	private void extractData(ExtractDataDto dto, String transName) throws Exception {
		TransMeta generateTrans = this.generateExtractDataTrans(dto, transName);
		String xml = generateTrans.getXML();
		File file = new File(transName);
		FileUtils.writeStringToFile(file, xml, "UTF-8");
	}

	private TransMeta generateExtractDataTrans(ExtractDataDto dto, String transName) {
		TransMeta transMeta = new TransMeta();
		transMeta.setName(transName);
		PluginRegistry registryID = PluginRegistry.getInstance();
		//1.表输入
		StepMeta tableInput = this.dbService.tableInput(transMeta, dto.getTiDto(), registryID);
		transMeta.addStep(tableInput);
		//2.elasticsearch bulk insert
		StepMeta esBulkInsert = this.blService.esBulkInsert(dto.getEsDto(), registryID);
		transMeta.addStep(esBulkInsert);
		return transMeta;
	}

	private void setVariable(SetVariableDto dto, String transName) throws Exception {
		TransMeta generateTrans = this.generateSetVariableTrans(dto, transName);
		String xml = generateTrans.getXML();
		File file = new File(transName);
		FileUtils.writeStringToFile(file, xml, "UTF-8");
	}

	private TransMeta generateSetVariableTrans(SetVariableDto dto, String transName) {
		TransMeta transMeta = new TransMeta();
		transMeta.setName(transName);
		PluginRegistry registryID = PluginRegistry.getInstance();
		//1.从结果获取记录
		this.jobsService.rowsFromResult(dto);
		//2.设置变量
		StepMeta setVariable = this.jobsService.setVariable(dto, registryID);
		transMeta.addStep(setVariable);
		return transMeta;
	}

	private void getTableNames(TableNamesDto dto, String transName) throws Exception {
		TransMeta generateTrans = this.generateTableNamesTrans(dto, transName);
		String xml = generateTrans.getXML();
		File file = new File(transName);
		FileUtils.writeStringToFile(file, xml, "UTF-8");
	}
	
	public TransMeta generateTableNamesTrans(TableNamesDto dto, String transName) throws Exception{
		TransMeta transMeta = new TransMeta();
		String[] dbXml = this.dbService.generateDBxml(dto.getTiDto().getDbDto());
		transMeta.setName(transName);
		//添加数据库转换
		DatabaseMeta dbMeta = new DatabaseMeta(dbXml[0]);
		transMeta.addDatabase(dbMeta);
		// registry是给每个步骤生成一个标识ID
		PluginRegistry registryID = PluginRegistry.getInstance();
		//1.表输入
		StepMeta tableInput = this.dbService.tableInput(transMeta, dto.getTiDto(), registryID);
		transMeta.addStep(tableInput);
		//2.字段选择
		StepMeta selectValue = this.transService.selectValue(dto, registryID);
		transMeta.addStep(selectValue);
		//3.复制记录到结果
		StepMeta rowsToResult = this.jobsService.rowsToResult(dto, registryID);
		transMeta.addStep(rowsToResult);
		return transMeta;
		
	}

	
}
