package it.unict.gallosiciliani.webapp;

import cz.cvut.kbss.ontodriver.jena.config.JenaOntoDriverProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ApplicationTests {

	@Autowired
	WebAppProperties props;

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

	@Test
	void ensureUsingInMemoryStorage(){
		assertEquals(JenaOntoDriverProperties.IN_MEMORY, props.getPersistence().getJenaStorageType());
	}
}
