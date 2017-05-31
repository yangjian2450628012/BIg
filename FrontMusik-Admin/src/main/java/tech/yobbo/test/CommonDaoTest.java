package tech.yobbo.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tech.yobbo.bean.Order;
import tech.yobbo.dao.BaseDao;

import java.util.List;
import java.util.Map;

/**
 * Created by xiaoJ on 5/23/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring/MyBatis.xml"})
public class CommonDaoTest<Users> extends BaseDao<Users> {

    /**
     * 查询50万条数据，多次运行时间结果
     * (结果，用Map容器来装数据，jVM内存溢出)
     * 改为40万条数据，测试运行结果,结果内存还是溢出
     * 改为38.5万条数据，测试运行结果
     * 1.  26.155秒(s)
     * 2.  26.278秒(s)
     * 3.  25.747秒(s)
     * 平均 ： 26.06秒(s)
     * 几乎比装载到bean容器要慢13.67秒，所以尽量创建实体对象来装数据
     */
    @Test
    public void test() {
        long startTime = System.currentTimeMillis();//获取当前时间
        String sql = "select id,pid,price,num,uid,atime,utime,isdel from my_orders limit 0,385000";
        List<Map<String,Object>> l = this.findManyData(sql);
        for (int i=0;i<l.size();i++){
            Map<String,Object> map = l.get(i);
            System.out.println(map.get("atime"));
        }
        long endTime = System.currentTimeMillis(); //结束时间
        System.out.println("程序运行时间："+(endTime-startTime)+"ms");
    }

    /**
     * 查询50万条数据，时间多次对比
     * 1.  33.247秒(s)
     * 2.  33.474秒(s)
     * 3.  32.931秒(s)
     * 再把查询条数改为跟上面的一样，测试速度
     * 1.  12.719秒(s)
     * 2.  12.042秒(s)
     * 3.  12.409秒(s)
     * 平均 : 12.39秒
     */
    @Test
    public void testU(){
        long startTime = System.currentTimeMillis();//获取当前时间
       List<Order> s = this.findByU();
       for(int i=0;i<s.size();i++){
           Order o = s.get(i);
           System.out.println(o.getAtime());
       }
       long endTime = System.currentTimeMillis(); //结束时间
        System.out.println("程序运行时间："+(endTime-startTime)+"ms");
    }
}
