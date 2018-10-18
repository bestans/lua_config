package bestan.common.lua;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * lua配置的参数所用注解
 * @author yeyouhuan
 * @date:   2018年8月2日 下午8:44:00 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface LuaParamAnnotation {
	/**
	 * @return 指定配置项可选与否
	 */
	LuaParamPolicy policy() default LuaParamPolicy.NORMAL;
	
	public enum LuaParamPolicy{
		/**
		 *表示配置项由class级别的注解optional控制是否需要配置 
		 */
		NORMAL,
		
		/**
		 *表示该配置项是可选的 
		 */
		OPTIONAL,
		
		/**
		 * 表示该配置项是不可缺少的
		 */
		REQUIRED,
	}
}
