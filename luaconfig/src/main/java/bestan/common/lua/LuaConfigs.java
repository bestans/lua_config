package bestan.common.lua;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.luaj.vm2.Globals;
import org.luaj.vm2.lib.jse.JsePlatform;

import com.google.common.base.Strings;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

/**
 * lua配置管理器，启动时调用loadConfig载入所有lua配置，
 * 
 * @author yeyouhuan
 * @date:   2018年8月2日 下午7:53:04 
 */
public class LuaConfigs {
	private static LuaConfigs instance = new LuaConfigs();
	private static Globals globals = JsePlatform.standardGlobals();
	
	private Map<Class<?>, BaseLuaConfig> allConfigs = new HashMap<>();
	
	/**
	 * 获取lua配置
	 * 
	 * @param cls lua配置class
	 * @return lua配置
	 */
	@SuppressWarnings("unchecked")
	public static <T extends BaseLuaConfig> T get(Class<T> cls) {
		return (T) instance.allConfigs.get(cls);
	}
	
	
	private static boolean isSubInterface(Class<?> cls, String superName) {
		if (cls.equals(Object.class)) {
			return false;
		}
		for (var it : cls.getGenericInterfaces()) {
			if (it.getTypeName().equals(superName)) {
				return true;
			}
			try {
				if (isSubInterface(Class.forName(it.getTypeName()), superName)) {
					return true;
				}
			} catch (ClassNotFoundException e) {
				return false;
			}
		}
		return false;
	}
	
	public static List<Class<?>> loadClasses(String packageName) {
		ScanResult scanResult =
		        new ClassGraph()
		            .enableAllInfo()
		            .whitelistPackages(packageName)
		            .scan();
		var allCls = scanResult.getAllClasses();
	    ClassInfoList filtered = allCls
        .filter(classInfo -> {
        	return !(classInfo.isInterface() || classInfo.isAbstract());
        });
		return filtered.loadClasses();
	}

	public static List<Class<?>> loadClasses(String packageName, String interfaceName) {
		ScanResult scanResult =
		        new ClassGraph()
		            .enableAllInfo()
		            .whitelistPackages(packageName)
		            .scan();
		var allCls = scanResult.getAllClasses();
	    ClassInfoList filtered = allCls
        .filter(classInfo -> {
        	return !(classInfo.isInterface() || classInfo.isAbstract())
        			&& isSubInterface(classInfo.loadClass(), interfaceName);
        });
		return filtered.loadClasses();
	}

	public static List<Class<?>> loadClasses(String packageName, Class<?> baseClass) {
		ScanResult scanResult =
		        new ClassGraph()
		            .enableAllInfo()
		            .whitelistPackages(packageName)
		            .scan();
		var allCls = scanResult.getAllClasses();
	    ClassInfoList filtered = allCls
        .filter(classInfo -> {
        	return !(classInfo.isInterface() || classInfo.isAbstract())
        			&& baseClass.isAssignableFrom(classInfo.loadClass());
        });
		return filtered.loadClasses();
	}
	
	/**
	 * 载入所有lua配置
	 * 
	 * @param rootPath lua配置根目录
	 * @param packageNames lua配置java文件所在包列表
	 * @return 成功或失败
	 */
	@SuppressWarnings("unchecked")
	public static boolean loadConfig(String rootPath, String[] packageNames) {
		try {
			for (var pName : packageNames) {
				System.out.println(String.format("loadConfig rootPath=%s,package=%s", rootPath, pName));
				var classes = loadClasses(pName, BaseLuaConfig.class);
				System.out.println(String.format("loadConfig classes size=%s", classes.size()));
				for (var cls : classes) {
					if (cls.isMemberClass()) {
						//嵌套类 不处理
						continue;
					}
					if (!loadConfig(rootPath, (Class<? extends BaseLuaConfig>)cls))
						return false;
				}
			}
		} catch (Exception e) {
			System.out.println(String.format("LuaConfigs init failed.error=%s", e.getMessage()));
			return false;
		}
		
		return true;
	}
	
	/**
	 * 载入所有lua配置
	 * 
	 * @param packageNames lua配置java文件所在包列表
	 * @return 成功或失败
	 */
	public static boolean loadConfig(String[] packageNames) {
		return loadConfig("", packageNames);
	}
	
	/**
	 * 载入所有lua配置
	 * 
	 * @param packageName lua配置java文件所在包
	 * @return 成功或失败
	 */
	public static boolean loadConfig(String packageName) {
		String[] arr = { packageName };
		return loadConfig(arr);
	}
	
	/**
	 * 载入所有lua配置
	 * 
	 * @param rootPath lua配置根目录
	 * @param packageName lua配置java文件所在包
	 * @return 成功或失败
	 */
	public static boolean loadConfig(String rootPath, String packageName) {
		String[] arr = { packageName };
		return loadConfig(rootPath, arr);
	}
	
	/**
	 * @param rootPath 配置所在文件夹
	 * @param cls 配置class
	 * @return
	 */
	public static <T extends BaseLuaConfig> T loadConfigByRootPath(String rootPath, Class<T> cls) {
		var annotation = cls.getAnnotation(LuaAnnotation.class);
		String fileName = null;
		if (annotation != null) {
			fileName = annotation.fileName();
		}
		if (Strings.isNullOrEmpty(fileName)) {
			//如果没有指定配置文件名，那么使用classname和.lua组合
			fileName = cls.getSimpleName() + ".lua";
		}
		if (annotation == null || annotation.load()) {
			return loadSingleConfig(rootPath + fileName, cls);
		}
		return null;
	}
	
	/**
	 * @param fullPath 配置的完整路径
	 * @param cls 配置的class
	 * @return 指定BaseLuaConfig配置
	 */
	public static <T extends BaseLuaConfig> T loadSingleConfig(String fullPath, Class<T> cls) {
		T config = null;
		try {
			config = (T)cls.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			throw new RuntimeException(String.format("loadSingleConfig exception:class=%s,info=%s", e.getClass().getSimpleName(), e.getMessage()));
		}
		if (!config.LoadLuaConfig(globals, fullPath)) {
			return null;
		}
		return config;
	}
	
	/**
	 * 载入lua配置
	 * 
	 * @param rootPath lua配置根目录
	 * @param cls lua配置class
	 * @return 成功或失败
	 */
	public static boolean loadConfig(String rootPath, Class<? extends BaseLuaConfig> cls) {
		try {
			System.out.println(String.format("loadConfig rootPath={},class={}", rootPath, cls));
			var config = loadConfigByRootPath(rootPath, cls);
			if (null == config) return true;
			
			//设置配置单例
			try
			{
				var configInstance = cls.getField("instance");
				if (configInstance.get(null) != null) {
					throw new RuntimeException(String.format("instance has value, maybe have more than one config:%s", cls.getSimpleName()));
				}
				if (configInstance != null) {
					configInstance.set(null, config);	
				}
			} catch (NoSuchFieldException e) {
				
			}
			instance.allConfigs.put(cls, config);
			return true;
		} catch (Exception e) {
			System.out.println(String.format("LuaConfigs init failed.error={},{}", e.getClass().getSimpleName(), e.getMessage()));
		}
		return false;
	}
	
	/**
	 * 载入lua配置
	 * 
	 * @param cls lua配置class
	 * @return 成功或失败
	 */
	public static boolean loadConfig(Class<? extends BaseLuaConfig> cls) {
		return loadConfig("", cls);
	}
}
