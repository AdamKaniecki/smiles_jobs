package pl.zajavka.infrastructure.business;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import pl.zajavka.infrastructure.business.dao.BusinessCardDAO;
import pl.zajavka.infrastructure.database.repository.mapper.AddressMapper;
import pl.zajavka.infrastructure.database.repository.mapper.BusinessCardMapper;
import pl.zajavka.infrastructure.security.mapper.UserMapper;
import pl.zajavka.integration.AbstractIT;

public class BusinessCardServiceTest extends AbstractIT {
    @Mock
    private BusinessCardMapper businessCardMapper;
    @Mock
    private UserMapper userMapper;
    @Mock
    private AddressMapper addressMapper;
    @Mock
    private BusinessCardDAO businessCardDAO;
    @InjectMocks
    private BusinessCardService businessCardService;





}
