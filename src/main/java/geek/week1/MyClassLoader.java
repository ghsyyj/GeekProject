package geek.week1;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * （必做）自定义一个 Classloader，加载一个 Hello.xlass 文件，执行 hello 方法，
 * 此文件内容是一个 Hello.class 文件所有字节（x=255-x）处理后的文件。文件群里提供。
 * @author Bruce
 *
 */
public class MyClassLoader extends ClassLoader {
	private static final String className = "Hello";
    private static final String methodName = "hello";

	/**
	 * 重写findClass方法.
	 */
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		File file = new File("F:\\学习视频\\java进阶训练营\\week1\\Hello.xlass\\Hello.xlass");
		try (InputStream is = new FileInputStream(file)) {
			// 读取数据
			int length = is.available();
			byte[] byteArray = new byte[length];
			is.read(byteArray);
			byte[] targetArray = new byte[byteArray.length];
			for (int i = 0; i < byteArray.length; i++) {
				targetArray[i] = (byte) (255 - byteArray[i]);
			}
            return defineClass(name, targetArray, 0, targetArray.length);
		} catch (Exception e) {
			throw new ClassNotFoundException(name, e);
		}

	}

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        // 创建类加载器
        ClassLoader classLoader = new MyClassLoader();
        // 加载相应的类
        Class<?> clazz = classLoader.loadClass(className);
        // 看看里面有些什么方法
        for (Method m : clazz.getDeclaredMethods()) {
            System.out.println(clazz.getSimpleName() + "." + m.getName());
        }
        // 创建对象
        Object instance = clazz.getDeclaredConstructor().newInstance();
        // 调用实例方法
        Method method = clazz.getMethod(methodName);

        method.invoke(instance);
	}

}
