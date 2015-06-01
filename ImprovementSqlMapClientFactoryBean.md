用在Spring中配置SqlMapClientFactoryBean工厂的时候用本包提供的SqlMapClientFactoryBean来替换。



&lt;bean id="sqlMapClient0" class="org.oproject.framework.orm.ibatis.SqlMapClientFactoryBean"&gt;


> 

&lt;property name="dataSource" ref="dataSource0"&gt;



&lt;/property&gt;


> 

&lt;property name="lobHandler" ref="defaultLobHandler"/&gt;


> 

&lt;property name="configLocation"&gt;


> > 

&lt;value&gt;

classpath:mapping/ds0/SqlMapConfig-ds0.xml

&lt;/value&gt;



> 

&lt;/property&gt;


> 

&lt;property name="mappingLocations"&gt;


> > 

&lt;value&gt;

classpath**:/mapping/ds0/****/**-sql${ds0}.xml

&lt;/value&gt;



> 

&lt;/property&gt;




&lt;/bean&gt;



可以解决用mappingLocations加载方式不能正确注册缓存模式到SqlMapClientFactoryBean的bug。而且加载sql配置文件的时候默认按a-z的顺序加载