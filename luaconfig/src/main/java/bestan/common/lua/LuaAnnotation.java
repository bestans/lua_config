package bestan.common.lua;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 提供一个lua配置注解，可以指定：配置文件，是否需要载入到内存，所有配置项是否可选
 * 
 * @author yeyouhuan
 * @date:   2018年8月2日 下午7:54:06 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface LuaAnnotation {
	/**
	 * @return 对应lua配置的文件名，如果不指定，那么将会采用
	 * 			classname.lua组合作为文件名
	 */
	String fileName() default "";
	
	/**
	 * @return 是否载入lua文件
	 */
	boolean load() default true;
	
	/**
	 * @return 所有配置项是否可选，true表示所有配置项可选
	 */
	boolean optional() default false;
}
