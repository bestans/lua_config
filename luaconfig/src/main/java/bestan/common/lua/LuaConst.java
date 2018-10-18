package bestan.common.lua;

public class LuaConst {
	/**
	 * lua中可配置的最大数值 <br>
	 * Lua语言里面的数字类型是number，<br>
	 * 双精度浮点数能存储的有效位数最大是53，<br>
	 * 这里的位数是指化成二进制之后的位数，<br>
	 * 如果位数大于53，则存在不精确存储。而53位最多可以存储的整数即为2^53
	 */
	public static final long MAX_LUA_VALUE = (1L << 53) - 1;
}
