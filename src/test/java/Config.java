import mock.ConfigMock;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Set;

public class Config{

    @Test
    public void testCreateConf(){
        ConfigMock config = ConfigMock.getInstance();
    }

    @Test
    public void testInitMapConf(){
        ConfigMock config = ConfigMock.getInstance();
        Set<String> keys = config.getKeysMap();
        for(String key: keys){
            System.out.println(
                    key +":"+config.getConfigValue(key)
            );
        }
    }

    @Test
    public void testValidityConf() throws IOException {
        ConfigMock config = ConfigMock.getInstance();
        config.isValid();
    }

}
