package com.springboot.service.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pentaho.di.core.plugins.PluginRegistry;
import org.pentaho.di.core.plugins.StepPluginType;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.elasticsearchbulk.ElasticSearchBulkMeta;
import org.pentaho.di.trans.steps.elasticsearchbulk.ElasticSearchBulkMeta.Server;
import org.springframework.stereotype.Service;

import com.springboot.entity.database.ElasticsearchDto;

@Service
public class BulkloadService {
	
	//批量加载输出
		public StepMeta esBulkInsert(ElasticsearchDto dto, PluginRegistry registryID){
			// elasticsearch bulk insert
			ElasticSearchBulkMeta esbMeta = new ElasticSearchBulkMeta();
			esbMeta.setDefault();
			// general
			esbMeta.setIndex(dto.getIndex()); // 设置索引名称
			esbMeta.setType(dto.getType()); // 设置类型名
			// servers
			Server server = new Server();
			List<Server> servers = new ArrayList();
			server.address = dto.getServerAddress();
			server.port = dto.getServerPort();
			servers.add(server);
			esbMeta.setServers(servers);
			// fields
			Map<String, String> fields = new HashMap();
			esbMeta.setFieldsMap(fields);
			// settings
			Map<String, String> map = new HashMap();
			map.put("cluster.name", dto.getClusterName());
			esbMeta.setSettingsMap(map);
			
			esbMeta.setBatchSize("50000");
			esbMeta.setStopOnError(true);
			
			String esPluginId = registryID.getPluginId(StepPluginType.class, esbMeta);
			// 添加esbMeta到转换中
			StepMeta sm = new StepMeta(esPluginId, "批量加载ES", esbMeta);
			sm.setDraw(true);
			sm.setLocation(100, 100);
			return sm;
		}
}
