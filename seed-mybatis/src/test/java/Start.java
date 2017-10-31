//import com.czy.seed.mybatis.entity.TestEntity;
//import com.czy.seed.mybatis.mapper.MySqlMapper;
//import com.czy.seed.mybatis.base.QueryParams;
//import org.springframework.beans.factory.BeanFactory;
//import org.springframework.beans.factory.support.BeanDefinitionRegistry;
//import org.springframework.beans.factory.support.DefaultListableBeanFactory;
//import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
//import org.springframework.context.support.ClassPathXmlApplicationContext;
//import org.springframework.core.io.ClassPathResource;
//
//import java.util.List;
//
///**
// * Created by panlc on 2017-03-13.
// */
//public class Start {
//
//    public static ClassPathXmlApplicationContext applicationContext;
//
//    public static void main(String[] args) {
//        applicationContext =
//                new ClassPathXmlApplicationContext(new String[]{"classpath:applicationContext.xml"});
//        applicationContext.start();
//
//        try {
//            System.out.println(applicationContext.getBean("datasource-default"));
//            System.out.println(applicationContext.getBean("datasource-ds1"));
//
//            MySqlMapper bean = applicationContext.getBean(MySqlMapper.class);
//
//            TestEntity testEntity = new TestEntity();
//            testEntity.setName("tsetest");
//            long insert = bean.insert(testEntity);
//            bean.deleteByPrimaryKey(testEntity.getId());
//            System.out.println(insert);
//            testEntity.setId(12L);
//            testEntity.setName("updated");
//            int i = bean.updateByPrimaryKey(testEntity);
//            int i1 = bean.updateByPrimaryKeySelective(testEntity);
//            QueryParams queryParams = new QueryParams(TestEntity.class);
//            queryParams.selectProperties("name");
//            QueryParams.Criteria criteria = queryParams.createCriteria();
//            criteria.andLike("name", "et");
//            List<TestEntity> testEntities = bean.selectListByParams(queryParams);
//            bean.deleteByParams(queryParams);
//            System.out.println(testEntities);
//
//            System.out.println(i);
//            System.in.read();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void test() {
//        BeanDefinitionRegistry reg = new DefaultListableBeanFactory();
//        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(reg);
//        reader.loadBeanDefinitions(new ClassPathResource("bean1.xml"));
//        reader.loadBeanDefinitions(new ClassPathResource("bean2.xml"));
//        BeanFactory bf = (BeanFactory) reg;
//    }
//
//}
