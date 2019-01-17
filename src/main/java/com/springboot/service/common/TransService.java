package com.springboot.service.common;

import org.pentaho.di.core.plugins.PluginRegistry;
import org.pentaho.di.core.plugins.StepPluginType;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.selectvalues.SelectValuesMeta;
import org.springframework.stereotype.Service;

import com.springboot.entity.trans.TableNamesDto;

@Service
public class TransService {

	public StepMeta selectValue(TableNamesDto dto, PluginRegistry registryID) {
		SelectValuesMeta svMeta = new SelectValuesMeta();
		String pluginId = registryID.getPluginId(StepPluginType.class, svMeta);
		svMeta.setDefault();
		svMeta.setSelectRename(dto.getFieldName());
		
		StepMeta sm = new StepMeta(pluginId, "字段选择", svMeta);
		sm.setDraw(true);
		sm.setLocation(200,100);
		return sm;
	}

}
