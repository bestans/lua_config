# What is luaconfig ?  luaconfig工具介绍
You can use the tool to add, delete and modify configure easily, just like drink water.
简单说，就是一个lua配置读取工具。新建、修改、删除配置选项就像喝水一样简单
* 支持多种数据格式 Integer/int/Boolean/boolean/Short/short/
   String/Long/long/BaseLuaConfig/List<T>/map<T1, T2> 等类型
* 支持自定义格式
* 配置错误，有很清晰的错误提示，不像json一样格式奇葩，格式错了很难定位。
* 方便扩展
# Requirements 需求环境
You need jdk 10, eclipse, and these maven dependencies.
需要jdk10 + eclipse + 以下maven依赖库
```
	<dependency>
	    <groupId>org.luaj</groupId>
	    <artifactId>luaj-jse</artifactId>
	    <version>3.0.1</version>
	</dependency>
	<dependency>
	    <groupId>io.github.classgraph</groupId>
	    <artifactId>classgraph</artifactId>
	    <version>LATEST</version>
	</dependency>
	<dependency>
	    <groupId>com.google.guava</groupId>
	    <artifactId>guava</artifactId>
	    <version>24.0-jre</version>
	</dependency>
  ```
# How to use ?  用法介绍
* first define your configure class. like this
  首先定义你自己的配置class文件
```java
  public class TestLuaConfig extends BaseLuaConfig {
	public int ivalue;
	public long lvalue;
	public String strValue;
	public CustomValue customValue;
	public Map<Integer, MapValue> mapValue;
	public List<ListValue> listValue;
	
	public static class MapValue extends BaseLuaConfig {
		public Integer first;
		public Integer second;
	}
	public static class ListValue extends BaseLuaConfig {
		public Integer first;
		public Integer second;
	}
	public static class CustomValue extends BaseLuaConfig {
		public Integer first;
		public Integer second;
	}
}
```
* then add a lua configure file,like TestLuaConfig.lua
 然后新建一个TestLuaConfig.lua，像下面一样配置各个选项
```lua
local config =
{
	ivalue = 11111,
	lvalue = 1000022222222222,
	strValue = "strvalue",
	customValue= {
		first = 1000, second = 1001,
	},
	mapValue = {
		[1] = { first = 1000, second = 1001, },
		[2] = { first = 1000, second = 1001, },
	},
	listValue = {
		{ first = 1000, second = 1001, },
		{ first = 1002, second = 1003, },
	},
}

return config
```
* load configs and use it. the loadConfig method specified root path and the package of TestLuaConfig
根据指定的根目录和配置class所在包，你可以读取并使用配置了
```java
public class TestMain {
	public static void main(String[] args) {
		//加载指定包里的所有BaseLuaConfig
		LuaConfigs.loadConfig("E:\\lua_config\\luaconfig\\", "bestan.test.lua");
		//根据class获取对应配置
		var cfg = LuaConfigs.get(TestLuaConfig.class);
		System.out.println("cfg=" + cfg);
	}
}
```
* output info
```
loadConfig rootPath=E:\lua_config\luaconfig\,package=bestan.test.lua
loadConfig classes size=4
loadConfig rootPath={},class={}
cfg={[ivalue=11111][lvalue=1000022222222222][strValue=strvalue][customValue={[first=1000][second=1001]}][mapValue={1={[first=1000][second=1001]}, 2={[first=1000][second=1001]}}][listValue=[{[first=1000][second=1001]}, {[first=1002][second=1003]}]]}
```

You can see bestan.test.lua.TestMain, run it then you will find a lot of other features. Welcome to star!
具体可以尝试跑一下bestan.test.lua.TestMain，还有很多其他特性。欢迎点赞！
