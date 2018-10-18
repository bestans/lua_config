package bestan.test.lua;

import bestan.common.lua.LuaConfigs;

public class TestMain {

	public static void main(String[] args) {
		//加载指定包里的所有BaseLuaConfig
		LuaConfigs.loadConfig("E:\\lua_config\\luaconfig\\", "bestan.test.lua");
		//根据class获取对应配置
		var cfg = LuaConfigs.get(TestLuaConfig.class);
		System.out.println("cfg=" + cfg);
	}
}
