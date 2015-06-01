# 安装配置步骤 #

## 添加jar ##
下载最新的ibatis2-spring**.jar，并关联到项目中。**

## 配置spring的上下文 ##
编辑项目中spring的配置文件，添加各种数据库操作方法的字节码生成逻辑处理器
```
<bean class="org.oproject.framework.orm.ibatis.bytecode.codegenerator.method.impl.InsertHandler"/>
<bean class="org.oproject.framework.orm.ibatis.bytecode.codegenerator.method.impl.UpdateHandler"/>
<bean class="org.oproject.framework.orm.ibatis.bytecode.codegenerator.method.impl.QueryForObjectHandle"/>
<bean class="org.oproject.framework.orm.ibatis.bytecode.codegenerator.method.impl.QueryForListHandle"/>
<bean class="org.oproject.framework.orm.ibatis.bytecode.codegenerator.method.impl.DeleteHandle"/>
<bean class="org.oproject.framework.orm.ibatis.bytecode.codegenerator.method.impl.BatchDeleteHandle"/>
<bean class="org.oproject.framework.orm.ibatis.bytecode.codegenerator.method.impl.BatchInsertHandle"/>
<bean class="org.oproject.framework.orm.ibatis.bytecode.codegenerator.method.impl.BatchUpdateHandle"/>
<bean class="org.oproject.framework.orm.ibatis.bytecode.codegenerator.method.impl.QueryForPageResultHandle"/>
```
> 注册spring启动加载器，用于动态字节码生成DAO。
```
<bean class="org.oproject.framework.orm.ibatis.config.DynamicIbatisBeanFactoryPostProcessor">
	<property name="scanPath">
		<list>
			<value>_org_._yourcompany_._XXX_.**.dao</value>
		</list>
	</property>
</bean>
```
## 编写第一个DAO接口 ##
添加接口,接口命名规范**{Namespace}**DAO
```
@DynamicIbatisDAO(value = "userDAO", sqlMapClientTemplate = "sqlMapClientTemplate")
public interface UserDAO {

	User getUser(String id);
}
```
编写SQL映射文件
```
<sqlMap namespace="User">  
<select id="getUser" resultClass="User" parameterClass="string">
    select t.id, t.name from t_user t where id = #value#
</select>
......
```
在service中调用DAO
```
@Autowired
@Qualifier("userDAO")
private UserDAO userDAO;
```