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
       // dataLogService.saveDataLog("1","AQMAGQABGAUkFhYj+xkE5gAQDwIO/lgFAAEAAgADAAQABWQf","TEST");
        dataLogService.saveDataLog("1807238000000188","{\"devEUI\":\"1807238000000188\",\"time\":\"2018-12-14T03:56:30.249285Z\",\"fPort\":8,\"gatewayCount\":2,\"rssi\":-41,\"fCnt\":125,\"loRaSNR\":10.5,\"data\":\"AQMUAHQYEhQSGCYAcQAtACEAtgOdEEJazQ==\"}","TEST");
    }
}
