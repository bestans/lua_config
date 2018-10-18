package bestan.test.lua;

import java.util.List;
import java.util.Map;

import bestan.common.lua.BaseLuaConfig;

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
