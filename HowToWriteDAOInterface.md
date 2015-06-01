`#summary One-sentence summary of this page.

# 接口命名规范 #
字节码增强DAO接口命名分为两部分组成，即Ibatis的\_命名空间_+DAO(固定字符串)；_

_例如:UserDAO对应的Ibatis查询命名空间为User。_


# 接口源注释规范 #
`org.oproject.framework.orm.ibatis.bytecode.codegenerator.annotations.DynamicIbatisDAO`
如果要在一个接口上实现字节码增强，必须定义该注释。
参数：
  * 参数1 value:必填，实现类定义到spring容器中的id；
  * 参数2 `sqlMapClientTemplate`:可选, 注入到DAO实现类中的`SqlMapClientTemplate`实例ID，默认"`sqlMapClientTemplate`"。
_多数据源情况下，每个不同的数据源使用不同的`sqlMapClientTemplate`实例。_


# DAO查询方法命名规范 #
方法名对应Ibatis映射文件中的SQL语句ID。

# 方法介绍 #
|数据访问类型|实现类|方法命名规则|参数说明| 返回结果 |
|:-----------------|:--------|:-----------------|:-----------|:-------------|
|查询单条记录| `QueryForObjectHandle` | `(load|get|select|read)[\\S]*` |参数arg0 必选 | `<T> 必选` |
| 新增           | `InsertHandler` | `(save|insert|add)[\\S]*` | 参数arg0 必选 | int 新增记录数 可选 |
| 修改           | `UpdateHandler` | `update[\\S]*`   | 参数arg0 必选 | int 更新的记录数 可选 |
| 删除           | `DeleteHandle` | `(delete|remove|del)[\\S]*` | 参数arg0 必选 | int 删除记录数 可选 |
| 批量新增     | `BatchInsertHandle` | `(save|insert|add)[\\S]*` | 参数Collection 必选 | void         |
| 批量修改     | `BatchUpdateHandle` | `(batchUpdate)[\\S]*` | 参数Collection 必选 | void         |
| 批量删除     | `BatchDeleteHandle` | `(batchDelete|batchRemove|batchDel)[\\S]*` | 参数Collection 必选 | void         |
| 查询结果集  | `QueryForListHandle` | `query[\\S]*ForList` | 参数Object 必选, (int start, int size) 成对可选 | `List<T> 必选` |
| 分页查询     | `QueryForPageResultHandle` | `query[\\S]*ForPageResult` | 参数Object 可选, (int pageNum, int pageSize) 成对可选 | `PageResult<T>` |

_所有基础字节码生成处理器都定义在`org.oproject.framework.orm.ibatis.bytecode.codegenerator.method.impl`包中_

# 使用数据库本地分页SQL优化`QueryForPageResultHandle` #
本地分页查询优化接口，需要实现本地分页查询优化的SQL包装操作都应该实现该接口。
```
/**
 * <p>
 * 本地查询接口
 * 定义本地查询方法
 * </p>
 * @author aohai.li
 * @version ibatis2.x-spring3.0, 2011-3-19
 * @since v1.0
 */
public interface NativeQuery
{
	/**
	 * 包装本地SQL查询语句
	 * @param sql            原始sql语句
	 * @param skipResults    查询的行数
	 * @param maxResults     最大获取数
	 * @return    包装后的SQL语句字符串
	 */
	String warpSQLStatement(String sql, int skipResults, int maxResults);
}
```
sql参数是包装前的sql语句，返回拼装后的SQL语句。
_`(例如:Mysql是在语句后增加"limit x y"。)`_
框架提供了两种默认的实现：
  * Oracle- `org.oproject.framework.orm.ibatis.query.OracleNativeQuery`
  * MySQL- `org.oproject.framework.orm.ibatis.query.MysqlNativeQuery`

在Spring容器中配置本地查询优化器
```
<bean id="nativeQueryForOracle" class="org.oproject.framework.orm.ibatis.query.OracleNativeQuery"/>
<bean id="nativeQueryForMySQL" class="org.oproject.framework.orm.ibatis.query.MysqlNativeQuery"/>
<!-- 自定义SQL执行器 -->
<bean id="customizedSQLExecutor" class="org.oproject.framework.orm.ibatis.CustomizedSQLExecutor" init-method="init" lazy-init="false">
   <property name="nativeQuery" ref="nativeQueryForOracle" />
   <property name="sqlMapClient" ref="sqlMapClient"/>
</bean> 
```