import com_zcx_chant_common.enums.BusinessExceptionEnum;
import org.junit.Test;

public class EnumTest {

    @Test
    public void test_01() {

        BusinessExceptionEnum system_exception = BusinessExceptionEnum.valueOf("SYSTEM_EXCEPTION");

        Integer code = system_exception.code;

        String message = system_exception.message;

    }

}
