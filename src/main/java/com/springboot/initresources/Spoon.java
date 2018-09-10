package com.springboot.initresources;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.plugins.LifecyclePluginType;
import org.pentaho.di.core.plugins.PluginFolder;
import org.pentaho.di.core.plugins.PluginRegistry;
import org.pentaho.di.core.plugins.RepositoryPluginType;
import org.pentaho.di.ui.job.dialog.JobDialogPluginType;
import org.pentaho.di.ui.repository.dialog.RepositoryDialogInterface;
import org.pentaho.di.ui.repository.dialog.RepositoryRevisionBrowserDialogInterface;
import org.pentaho.di.ui.spoon.SpoonPluginType;
import org.pentaho.di.ui.trans.dialog.TransDialogPluginType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Spoon implements CommandLineRunner {

	private static Logger logger = LogManager.getLogger(Spoon.class);
	@Override
	public void run(String... args) throws Exception {
		registerUIPluginObjectTypes();
		logger.info(">>>>>>kettle初始化开始..." + new Date());
		KettleEnvironment.init();
		logger.info(">>>>>>kettle初始化完成。" + new Date());
		
	}
	
	private static void registerUIPluginObjectTypes() {
		logger.info(">>>>>>加载插件中...");
	    RepositoryPluginType.getInstance()
	                        .addObjectType( RepositoryRevisionBrowserDialogInterface.class, "version-browser-classname" );
	    RepositoryPluginType.getInstance().addObjectType( RepositoryDialogInterface.class, "dialog-classname" );

	    PluginRegistry.addPluginType( SpoonPluginType.getInstance() );

	    SpoonPluginType.getInstance().getPluginFolders().add( new PluginFolder( "plugins/repositories", false, true ) );

	    LifecyclePluginType.getInstance().getPluginFolders().add( new PluginFolder( "plugins/spoon", false, true ) );
	    LifecyclePluginType.getInstance().getPluginFolders().add( new PluginFolder( "plugins/repositories", false, true ) );

	    PluginRegistry.addPluginType( JobDialogPluginType.getInstance() );
	    PluginRegistry.addPluginType( TransDialogPluginType.getInstance() );
	  }

}
