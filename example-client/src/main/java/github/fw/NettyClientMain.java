package github.fw;

import github.fw.annotation.RpcScan;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author shuang.kou
 * @createTime 2020年05月10日 07:25:00
 */
@RpcScan(basePackage = {"github.javaguide"})    // 扫描指定包下的类
public class NettyClientMain {
    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(NettyClientMain.class);
        // 在ioc容器中获得一个HelloController对象
        HelloController helloController = (HelloController) applicationContext.getBean("helloController");
        helloController.test();
    }
}
