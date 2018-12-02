import com.heqichao.springBootDemo.SpringBootDemoApplication;
import com.heqichao.springBootDemo.module.service.DataLogService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * 测试模板
 * Created by heqichao on 2018-12-1.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringBootDemoApplication.class)
public class ModelTest {

    @Autowired
    private DataLogService dataLogService;

    @Test
    public void  saveDataLog(){
        dataLogService.saveDataLog("1","AQMAGQABGAUkFhYj+xkE5gAQDwIO/lgFAAEAAgADAAQABWQf");
    }
}
