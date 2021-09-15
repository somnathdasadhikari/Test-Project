package com.corprecruital.testproject.map;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;

import com.corprecruital.testproject.exception.*;


import org.junit.jupiter.api.Test;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SuperMapTest {

	SuperMap<Integer, String> superMapwithDefaultSize;
	SuperMap<Integer, String> superMapwithCustomSize;
	
	@BeforeAll
	public void init() {
		superMapwithDefaultSize = new SuperMap<>();
		superMapwithCustomSize = new SuperMap<>(3);
	}
	
	@Test
	@DisplayName("get method should throw exception when key is null")
	public void getWithNullKey() {
		assertThrows(NullKeyException.class, ()->{
			superMapwithDefaultSize.get(null);
		});
	}
	
	@Test
	@DisplayName("get method should throw exception when key is not found")
	public void getWithInvalidKey() {
		assertThrows(NoDataFoundException.class, ()->{
			superMapwithDefaultSize.get(100);
		});
	}
	
	@Test
	@DisplayName("get method should return data successfully")
	public void get() {
		superMapwithDefaultSize.put(10, "ABC");
		assertEquals("ABC", superMapwithDefaultSize.get(10));
	}
	
	@Test
	@DisplayName("put method should save data successfully")
	public void put() {
		superMapwithDefaultSize.put(10, "ABC");
		assertEquals(1, superMapwithDefaultSize.size());
		assertEquals("ABC", superMapwithDefaultSize.get(10));
	}
	
	@Test
	@DisplayName("put method should throw exception when key is null")
	public void putWithNullKey() {
		assertThrows(NullKeyException.class, ()->{
			superMapwithDefaultSize.put(null, "XYZ");
		});
	}
	
	@Test
	@DisplayName("put menthod concurrency test, may not alwasy pass as put method is non-synchronized")
	public void putWithConCurrency() throws InterruptedException {
		int numberOfThreads = 5000;
		ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);
		CountDownLatch latch = new CountDownLatch(numberOfThreads);
		for(int i = 0; i < numberOfThreads; i++) {
			Integer key = i+1;
			service.execute(()->{
				superMapwithDefaultSize.put(key, "ABC");
				latch.countDown();
			});
		}
		latch.await();
		assertEquals(numberOfThreads, superMapwithDefaultSize.size());
	}
	
	@Test
	@DisplayName("remove method should throw exception when key is null")
	public void removeWithNullKey() {
		assertThrows(NullKeyException.class, ()->{
			superMapwithDefaultSize.remove(null);
		});
	}
	
	@Test
	@DisplayName("remove method should throw exception when key is not found")
	public void removeWithInvalidKey() {
		assertThrows(NoDataFoundException.class, ()->{
			superMapwithDefaultSize.remove(1000);
		});
	}
	
	@Test
	@DisplayName("remove method should delete data successfully")
	public void remove() {
		superMapwithDefaultSize.put(10, "ABC");
		superMapwithDefaultSize.put(15, "XYZ");
		assertEquals(2, superMapwithDefaultSize.size());
		Integer key = superMapwithDefaultSize.remove(15);
		assertEquals(15, key);
		assertEquals(1, superMapwithDefaultSize.size());
	}
	

}
