package it.unict.gallosiciliani.webapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;

import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ApplicationTests {

	@Autowired
	private WebAppProperties props;

	@Test
	void contextLoads() {
	}

	@Test
	void getURIProperty(){
		assertNotNull(props.getNs());
	}

	@Test
	void ensureDefaultCharsetIsUTF8(){
		assertEquals("UTF-8", Charset.defaultCharset().displayName());
	}
}
