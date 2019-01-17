package com.springboot.service.common;

import org.pentaho.di.core.plugins.PluginRegistry;
import org.pentaho.di.core.plugins.StepPluginType;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.rowstoresult.RowsToResultMeta;
import org.pentaho.di.trans.steps.setvariable.SetVariableMeta;
import org.springframework.stereotype.Service;

import com.springboot.entity.trans.SetVariableDto;
import com.springboot.entity.trans.TableNamesDto;

@Service
public class JobsService {

	public StepMeta rowsToResult(TableNamesDto dto, PluginRegistry registryID) {
		RowsToResultMeta rtrMeta = new RowsToResultMeta();
		rtrMeta.setDefault();
		String pluginId = registryID.getPluginId(StepPluginType.class, rtrMeta);
		StepMeta sm = new StepMeta(pluginId, "复制记录到结果", rtrMeta);
		sm.setDraw(true);
		sm.setLocation(100, 300);
		return sm;
	}

	public void rowsFromResult(SetVariableDto dto) {
//		RowsFromResult rfrMeta = new RowsFromResult();
		
	}

	public StepMeta setVariable(SetVariableDto dto, PluginRegistry registryID) {
		SetVariableMeta svMeta = new SetVariableMeta();
		svMeta.setDefault();
		String pluginId = registryID.getPluginId(StepPluginType.class, svMeta);
		StepMeta sm = new StepMeta(pluginId, "设置变量", svMeta);
		sm.setDraw(true);
		sm.setLocation(100, 120);
		return sm;
	}

}
