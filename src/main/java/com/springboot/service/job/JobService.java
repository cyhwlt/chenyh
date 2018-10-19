package com.springboot.service.job;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pentaho.di.core.logging.LogLevel;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobHopMeta;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.job.entries.abort.JobEntryAbort;
import org.pentaho.di.job.entries.special.JobEntrySpecial;
import org.pentaho.di.job.entries.success.JobEntrySuccess;
import org.pentaho.di.job.entries.trans.JobEntryTrans;
import org.pentaho.di.job.entries.writetolog.JobEntryWriteToLog;
import org.pentaho.di.job.entry.JobEntryCopy;
import org.springframework.stereotype.Service;

import com.springboot.entity.JobDto;
import com.springboot.entity.JobScheduleDto;

@Service
public class JobService {
	
	private static Logger logger = LogManager.getLogger(JobService.class);
	
	public void runJob(){
		try {
            // jobname 是Job脚本的路径及名称
            JobMeta jobMeta = new JobMeta("res\\job019.kjb", null);
//            JobMeta jobMeta = new JobMeta(dto.getKjbPath(), null);

            Job job = new Job(null, jobMeta);
            job.setVariable("id", "1");
            job.setVariable("content", "content");
            job.setVariable("file", "res\\kettle_log019");
//            job.setVariable("file", dto.getLogPath());

            job.start();
            job.waitUntilFinished();
            if (job.getErrors() > 0) {
            	logger.error("There are errors during job exception!(执行job发生异常)");
            }
        } catch (Exception e) {
        	logger.error(e.getMessage());
        }
	}

	public void generateJobFile(JobDto dto) {
		JobMeta generateJob = generateJob(dto);
		String xml = generateJob.getXML();
		File file = new File(dto.getJobName());
		try {
			FileUtils.writeStringToFile(file, xml, "UTF-8");
		} catch (IOException e) {
			logger.error("生成job转换文件异常：" + e.getMessage());
		}
	}
	
	public JobMeta generateJob(JobDto dto) {
		try {
		JobMeta jobMeta = new JobMeta();
		jobMeta.setName(dto.getJobName());
		jobMeta.setFilename(dto.getJobName());

		// Create and configure start entry
		JobEntrySpecial start = new JobEntrySpecial();
		start.setName("START");
		start.setStart(true);

		JobEntryCopy startEntry = new JobEntryCopy(start);
		jobMeta.addJobEntry(startEntry);

//		// Create and configure entry
//		JobEntryWriteToLog writeToLog = new JobEntryWriteToLog();
//		writeToLog.setName("Output PDI Stats");
//		writeToLog.setLogLevel(LogLevel.MINIMAL);
//		writeToLog.setLogSubject("Logging PDI Build Information:");
//		writeToLog.setLogMessage("Version: ${Internal.Kettle.Version}\n" +
//		"Build Date: ${Internal.Kettle.Build.Date}");
		
		// 转换
		JobEntryTrans jeTrans = new JobEntryTrans();
		jeTrans.setName("转换");
		jeTrans.setFileName("E:\\kettleTest\\exceltodb.ktr");
		jeTrans.setDescription("");
		jeTrans.setPassingAllParameters(false);

//		JobEntryCopy writeToLogEntry = new JobEntryCopy(writeToLog);
		JobEntryCopy transCopy = new JobEntryCopy(jeTrans);

		jobMeta.addJobEntry(transCopy); 
		jobMeta.addJobHop(new JobHopMeta(startEntry, transCopy));

		JobEntrySuccess success = new JobEntrySuccess();
		success.setName("Success");
		JobEntryCopy successEntry = new JobEntryCopy(success);
		jobMeta.addJobEntry(successEntry); 

		JobHopMeta greenHop = new JobHopMeta(transCopy, successEntry);
		greenHop.setEvaluation(true);
		jobMeta.addJobHop(greenHop);
		return jobMeta;
		} catch (Exception e) {
		e.printStackTrace();
		return null;
		}
		}
}
