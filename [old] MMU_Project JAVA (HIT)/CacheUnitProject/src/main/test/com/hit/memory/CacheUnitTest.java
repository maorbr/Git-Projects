package com.hit.memory;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.hit.algorithm.IAlgoCache;
import com.hit.algorithm.LRUAlgoCacheImpl;
import com.hit.dao.DaoFileImpl;
import com.hit.dao.IDao;
import com.hit.dm.DataModel;

public class CacheUnitTest {
	private IAlgoCache<Long, DataModel<String>> algo = null;
	private CacheUnit<String> cacheUnit = null;
	private IDao<Long, DataModel<String>> idao = null;

	DataModel<String> data0, data1, data2, data3;
	
	@Before
	public void setUp() {

		algo = new LRUAlgoCacheImpl<Long, DataModel<String>>(4);
		cacheUnit = new CacheUnit<String>(algo);
		idao = new DaoFileImpl<String>("datasource.txt",4);

		data0 = new DataModel<String>((long) 0, "element 0");
		data1 = new DataModel<String>((long) 1, "element 1");
		data2 = new DataModel<String>((long) 2, "element 2");
		data3 = new DataModel<String>((long) 3, "element 3");
	}

	@Test
	public void testMain() {
		try {
			idao.save(data0);
			idao.save(data1);
			idao.save(data2);
			idao.save(data3);
			
			//check idao's save method
			assertEquals("datamodel Content on key 0 should be element 0","element 0", idao.find((long) 0).getContent());
			assertEquals("datamodel Content on key 1 should be element 1","element 1", idao.find((long) 1).getContent());
			assertEquals("datamodel Content on key 2 should be element 2","element 2", idao.find((long) 2).getContent());
			assertEquals("datamodel Content on key 3 should be element 3","element 3", idao.find((long) 3).getContent());	
			
			idao.delete(data3);
			
			//check idao's delete method
			assertEquals("datamodel Content on key 3 should be element 3",null, idao.find((long) 3));
			
			DataModel<String> testFind = idao.find((long) 0);
			
			//check idao's find method
			assertTrue("testFind's datamodel on key 0 should be with key 0", (long) 0 == testFind.getDataModelId());
			assertTrue("testFind's datamodel on key 0 should be with Content 0", "element 0".equals(testFind.getContent()));

			
			
			//check getDataModels method
			Long[] idsGet = { (long) 2, (long) 3, (long) 0 };
			
			cacheUnit.getAlgo().putElement(data0.getDataModelId(), data0);
			cacheUnit.getAlgo().putElement(data1.getDataModelId(), data1);

			DataModel<String>[] dataModelArrayGet = cacheUnit.getDataModels(idsGet);

			if (dataModelArrayGet[0] == null)
			dataModelArrayGet[0] = idao.find((long) 2);

			//idsGet[0] was taken from idao
			assertEquals("datamodel Content on key 2 from idsGet[0] should be element 2","element 2", dataModelArrayGet[0].getContent());
			//idsGet[2] was taken from cacheUnit
			assertEquals("datamodel Content on key 0 from idsGet[2] should be element 0","element 0", dataModelArrayGet[2].getContent());
			//idsGet[1] was not found so return null 
			assertEquals("datamodel Content on key 0 should be element 2",null, dataModelArrayGet[1]);
			
			//check removeDataModels method
			Long[] idsDelete = { (long) 0, (long) 1, (long) 2 };
			
			cacheUnit.removeDataModels(idsDelete);

			//idsDelete[0] was deleted
			assertEquals("datamodel Content  from idsDelete[0] should be null",null, cacheUnit.getAlgo().getElement((long) 0));
			
			
			//check putDataModels method
			@SuppressWarnings("unchecked")
			DataModel<String>[] dataModelArrayPut = new DataModel[2];
			dataModelArrayPut[0] = new DataModel<String>((long) 5, "element 5");
			dataModelArrayPut[1] = new DataModel<String>((long) 6, "element 6");
			
			cacheUnit.putDataModels(dataModelArrayPut);
			Long[] idsput = { (long) 5, (long) 6};
			
			dataModelArrayPut = cacheUnit.getDataModels(idsput);
			

			assertEquals("datamodel Content from dataModelArrayPut[0] should be element 5","element 5", dataModelArrayPut[0].getContent());
			assertEquals("datamodel Content from dataModelArrayPut[0] should be element 6","element 6", dataModelArrayPut[1].getContent());
			

		} catch (NullPointerException e) {
			e.printStackTrace();
			return;
		}
	}
}