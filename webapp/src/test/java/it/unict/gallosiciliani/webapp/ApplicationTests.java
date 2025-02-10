package it.unict.gallosiciliani.webapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ApplicationTests {

	@Autowired
	WebAppProperties props;

	@Test
	void contextLoads() {
	}

	@Test
	void ensureDefaultCharsetIsUTF8(){
		assertEquals("UTF-8", Charset.defaultCharset().displayName());
	}

	@Test
	void ensureDataNotLoaded(){
		assertFalse(props.isLoadData());
	}
}
